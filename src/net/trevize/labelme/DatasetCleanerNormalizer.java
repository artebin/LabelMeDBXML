package net.trevize.labelme;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.trevize.labelme.jaxb.Annotation;
import net.trevize.utils.SystemCommandHandler;

/**
 * The goal of this class is to make some cleaning and normalization on the 
 * LabelMe annotations and images dataset.
 * 
 * In a first time, the cleaning only consists in applying a trimpp() 
 * on the free-text annotation (an improved version of String.trim()).
 * Indeed, some fields are cleaned.
 * 
 * In a second time, a normalization is done on the images, in resizing it or
 * creating thumbnails.
 * This step is a little tricky, because objects polygon have to be updated.
 * 
 * There is also a filename normalisation, (for the %2520, which could be
 *  tricky), but it only concern the annotation XML filename, the image filename
 * referenced in the XML annotation file doesn't change.
 * 
 * IMPORTANT: When an free-text annotation is empty (i.e. contains only
 * space caracters, or \n or \r or \t for exemple) the Object node is deleted.
 * 
 * This class should be applied on the annotation dataset before the indexing
 * in XML database like MonetDB or Berkeley DB XML. 
 * (in order to make the querying faster).
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * DatasetNormalization.java - Jan 16, 2009
 */

public class DatasetCleanerNormalizer {

	private String datasetAnnotationsPath;

	private String datasetImagesPath;

	private Dimension maxImagesSize;

	private JAXBContext jc;

	private Unmarshaller u;

	private Marshaller m;

	private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

	private SystemCommandHandler sch;

	//for verifying the filename (MonetDB for example doesn't like URL-encoding).
	private String filename_filter = "^.*(%2520).*$";
	private Pattern filename_pattern = Pattern.compile(filename_filter);

	public DatasetCleanerNormalizer() {
		sch = new SystemCommandHandler();
	}

	/**
	 * For cleaning all the LabelMe dataset.
	 * @param datasetPath
	 * @param indexPath
	 */
	public void cleanLabelMeDataset(String datasetAnnotationsPath,
			String datasetImagesPath, Dimension maxImagesSize) {
		System.out.println("cleanLabelMeDataset begin.");

		this.datasetAnnotationsPath = datasetAnnotationsPath;
		this.datasetImagesPath = datasetImagesPath;
		this.maxImagesSize = maxImagesSize;

		//create the JAXBContext.
		try {
			jc = JAXBContext.newInstance("net.trevize.labelme.jaxb");
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//instantiate an Unmarshaller.
		try {
			u = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//instantiate a Marshaller.
		try {
			m = jc.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		_processDataset();

		System.out.println("cleanLabelMeDataset end.");
	}

	private void _processDataset() {
		System.out.println("_processDataset begin.");

		File[] dl = new File(datasetAnnotationsPath).listFiles();

		for (File d : dl) {
			_processDir(d.getName());
		}

		System.out.println("_processDataset end.");
	}

	private void _processDir(String dir_path) {
		System.out.println("\tentering " + datasetAnnotationsPath + "/"
				+ dir_path);

		File dir = new File(datasetAnnotationsPath + "/" + dir_path);

		/*
		 * we have some .txt files in the dataset for storing statistics, 
		 * we skip them.
		 */
		if (!dir.isDirectory()) {
			return;
		}

		//iterate on all file on the current directory.
		File[] lf = dir.listFiles();

		//iterate on all XML annotation files in the current directory.
		for (File f : lf) {
			//if the file is not an annotation file then go to the next iteration.
			if (!f.getName().endsWith(".xml")) {
				continue;
			}

			//create an XMLStreamReader on the annotation file.
			XMLStreamReader xmlsr = null;
			try {
				xmlsr = xmlInputFactory
						.createXMLStreamReader(new FileInputStream(f));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (XMLStreamException e1) {
				e1.printStackTrace();
			}

			//load the annotation.
			Annotation a = null;
			try {
				a = (Annotation) u.unmarshal(xmlsr);
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			//clean content.
			cleanContent(a);

			//normalize the XML element filename.
			//the real renaming of files is done later, because we have
			//loaded the XML annotation file, we have to finish updating before
			//renaming the file.
			if (filename_pattern.matcher(a.getFilename().getContent()).find()) {
				a.getFilename().setContent(
						a.getFilename().getContent().replace("%2520", "_"));
			}

			/*
			 * reducing the image size if necessary.
			 * when reducing the image size, we have to update the polygon
			 * points.
			 * To doing this, we have to get the resize ratio on width and 
			 * height.
			 */
			convertImage(a);
			
			if(maxImagesSize != null){
				resizeImage(a);
			}
			
			//re-write the XML annotation file.
			try {
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile());
				m.marshal(new JAXBElement<Annotation>(new QName("annotation"),
						Annotation.class, a), fos);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//the XML annotation file is rewritten and close, now we can
			//rename the file if it is needed.

			//normalize the image filename.
			File img = new File(datasetImagesPath + "/"
					+ a.getFolder().getContent() + "/"
					+ a.getFilename().getContent());
			normalizeFileName(img);
			//normalize the annotation filename.
			normalizeFileName(f);

		}//end for all file in the current directory.
	}

	private void cleanContent(Annotation a) {
		//cleaning content.
		if (a.getFilename() != null) {
			a.getFilename().setContent(trimpp(a.getFilename().getContent()));
		}

		if (a.getFolder() != null) {
			a.getFolder().setContent(trimpp(a.getFolder().getContent()));
		}

		if (a.getSource() != null & a.getSource().getSourceAnnotation() != null) {
			a.getSource().getSourceAnnotation().setContent(
					trimpp(a.getSource().getSourceAnnotation().getContent()));
		}

		if (a.getSource() != null & a.getSource().getSourceImage() != null) {
			a.getSource().getSourceImage().setContent(
					trimpp(a.getSource().getSourceImage().getContent()));
		}

		List<net.trevize.labelme.jaxb.Object> a_objects = a.getObject();
		for (int i = 0; i < a_objects.size(); ++i) {
			net.trevize.labelme.jaxb.Object a_object = a.getObject().get(i);

			if (a_object.getDate() != null) {
				a_object.getDate().setContent(
						trimpp(a_object.getDate().getContent()));
			}

			if (a_object.getDeleted() != null) {
				a_object.getDeleted().setContent(
						trimpp(a_object.getDeleted().getContent()));
			}

			if (a_object.getId() != null) {
				a_object.getId().setContent(
						trimpp(a_object.getId().getContent()));
			}

			if (a_object.getName() != null) {
				a_object.getName().setContent(
						trimpp(a_object.getName().getContent()));
			}

			if (a_object.getVerified() != null) {
				a_object.getVerified().setContent(
						trimpp(a_object.getVerified().getContent()));
			}

			if (a_object.getViewpoint() != null
					&& a_object.getViewpoint().getAzimuth() != null) {
				a_object.getViewpoint().getAzimuth().setContent(
						trimpp(a_object.getViewpoint().getAzimuth()
								.getContent()));
			}

			if (a_object.getPolygon() != null
					&& a_object.getPolygon().getUsername() != null) {
				a_object.getPolygon().getUsername()
						.setContent(
								trimpp(a_object.getPolygon().getUsername()
										.getContent()));
			}

			if (a_object.getPolygon() != null
					&& a_object.getPolygon().getPt() != null) {
				List<net.trevize.labelme.jaxb.Pt> a_pts = a_object.getPolygon()
						.getPt();
				for (int j = 0; j < a_pts.size(); ++j) {
					net.trevize.labelme.jaxb.Pt a_pt = a_pts.get(j);

					if (a_pt.getX() != null) {
						a_pt.getX()
								.setContent(trimpp(a_pt.getX().getContent()));
					}

					if (a_pt.getY() != null) {
						a_pt.getY()
								.setContent(trimpp(a_pt.getY().getContent()));
					}
				}
			}
		}
	}

	/**
	 * Some files in the dataset could have name using URL-encoding, this
	 * method renames these files.
	 * For instance, replace %2520 by '_'.
	 * (the double URL-encoding is tricky for some xml database).
	 * 
	 * @param f the file to rename
	 * @return a File object for the renamed file
	 */
	private File normalizeFileName(File f) {
		String name = f.getName();
		if (filename_pattern.matcher(name).find()) {
			File res = new File(f.getParent() + "/"
					+ name.replace("%2520", "_"));
			f.renameTo(res);
			return res;
		} else {
			return f;
		}
	}

	/**
	 * Convert in jpg the image using ImageMagick.
	 * @param a
	 */
	private void convertImage(Annotation a) {
		//we assume there is only one point in the filename, it's the point for
		//the file extension. For instance example.jpg
		//		String filenameWithoutExtension = a.getFilename().getContent().split(
		//				"\\.")[0];
		String[] values = a.getFilename().getContent().split("/");

		String filenameWithoutExtension = values[values.length - 1];

		//prepare and launch the convert command.
		/*
		String command_convert = "convert " + datasetImagesPath + "/"
				+ a.getFolder().getContent() + "/"
				+ a.getFilename().getContent() + " " + datasetImagesPath + "/"
				+ a.getFolder().getContent() + "/" + filenameWithoutExtension
				+ ".jpg";
		sch.exec(command_convert);
		*/

		//updating the filename in the annotation file.
		//a.getFilename().setContent(filenameWithoutExtension + ".jpg");
		a.getFilename().setContent(filenameWithoutExtension);
	}

	/**
	 * Resize the image using ImageMagick.
	 * @param a
	 */
	private void resizeImage(Annotation a) {
		//we assume there is only one point in the filename, it's the point for
		//the file extension. For instance example.jpg
		String filenameWithoutExtension = a.getFilename().getContent().split(
				".")[0];

		//getting the image size to updating the polygon points.
		BufferedImage bi = loadImage(datasetImagesPath + "/"
				+ a.getFolder().getContent() + "/" + filenameWithoutExtension
				+ ".jpg");

		//if we have to resize the image considering maxImagesSize.
		if (bi.getWidth() > maxImagesSize.width
				|| bi.getHeight() > maxImagesSize.height) {

			Dimension newDim = getScaledSize(bi.getWidth(), bi.getHeight(),
					maxImagesSize.width, maxImagesSize.height);

			float scale_x = (float) newDim.width / (float) bi.getWidth();
			float scale_y = (float) newDim.height / (float) bi.getHeight();

			for (net.trevize.labelme.jaxb.Object o : a.getObject()) {
				for (net.trevize.labelme.jaxb.Pt pt : o.getPolygon().getPt()) {
					pt.getX().setContent(
							""
									+ (int) (Integer.parseInt(pt.getX()
											.getContent()) * scale_x));

					pt.getY().setContent(
							""
									+ (int) (Integer.parseInt(pt.getY()
											.getContent()) * scale_y));
				}
			}

			//resize the image with ImageMagick.
			String command_resize = "convert -resize " + maxImagesSize.width
					+ "x" + maxImagesSize.height + "\\> " + datasetImagesPath
					+ "/" + a.getFolder().getContent() + "/"
					+ a.getFilename().getContent() + " " + datasetImagesPath
					+ "/" + a.getFolder().getContent() + "/"
					+ a.getFilename().getContent();
			sch.exec(command_resize);

		}
	}

	/**
	 * A method for creating a BufferedImage Object using the ImageIO.read(...) 
	 * static method.
	 * @param path to the image to load
	 * @return BufferedImage the created BufferedImage object
	 */
	private BufferedImage loadImage(String path) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}

	private Dimension getScaledSize(int width, int height, int maxWidth,
			int maxHeight) {
		Dimension imageDim = new Dimension();

		if (width <= maxWidth && height <= maxHeight) {
			imageDim.width = width;
			imageDim.height = height;
		}

		else

		if (width > maxWidth && height > maxHeight) {
			if (height > width) {
				imageDim.width = (int) width * maxHeight / height;
				imageDim.height = maxHeight;
			} else {
				imageDim.height = (int) height * maxWidth / width;
				imageDim.width = maxWidth;
			}
		}

		else

		if (width > maxWidth) {
			imageDim.height = (int) height * maxWidth / width;
			imageDim.width = maxWidth;
		}

		else

		if (height > maxHeight) {
			imageDim.height = (int) height * maxWidth / width;
			imageDim.width = maxWidth;
		}

		return imageDim;
	}


	/**
	 * An augmented version of the String.trim() method which remove '\n', '\r'
	 * and '\t'.
	 * @param s
	 * @return a corrected String based on the String given in parameter.
	 */
	private String trimpp(String s) {
		/*
		 * analyzing between the content of the string.
		 */
		s = s.replaceAll("\r", " ");
		s = s.replaceAll("\n", " ");
		s = s.replaceAll("\t", " ");

		/*
		 * String.trim() removes \n, \r and \t from the head and the queue
		 * of a string.
		 */
		s = s.trim();

		/*
		 * It could be a problem to transform '\t' by '', it could be more
		 * interesting to tranform '\t' by ' ' and to transform '\s+' by 
		 * the String ' '. 
		 */
		s = s.replaceAll("\\s+", " ");

		return s;
	}

	public static void main(String args[]) {
		/*
		String datasetAnnotationsPath = args[0];

		String datasetImagesPath = args[1];

		int maxImageWidth = Integer.parseInt(args[2]);

		int maxImageHeight = Integer.parseInt(args[3]);

		DatasetCleanerNormalizer dc = new DatasetCleanerNormalizer();

		dc.cleanLabelMeDataset(datasetAnnotationsPath, datasetImagesPath,
				new Dimension(maxImageWidth, maxImageHeight));
		*/
	}
	
}
