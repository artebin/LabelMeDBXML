package net.trevize.labelme.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import net.trevize.labelthem.jaxb.Annotation;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * This class provide a method taking in input a LabelMe annotation object 
 * (from JAXB), and provide in output an XML Document which is a SVG document 
 * for the indicated annotation, displaying the linked image and object 
 * polygons.
 * 
 * The method take in input the size of the image in the SVG document 
 * (polygons are updated in function of the image size in the SVG).
 * 
 * For the painting attributes of SVG elements see: 
 * http://www.w3.org/TR/SVG11/painting.html
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LabelMeDocument2SVG.java - May 22, 2009
 */

public class LabelMeDocument2SVG {

	private net.trevize.labelthem.jaxb.Annotation a;

	//path to ~LabelMe/database/images
	private String pathToLabelMeImages;

	//the image real size.
	private Dimension imageSize;

	//the maximal size on the screen (keeping aspect ratio).
	private Dimension imageMaxSizeOnScreen;

	//the real size on the screen abiding the imageMaxSizeOnScreen and the aspect ratio.
	private Dimension imageSizeOnScreen;

	private SVGDocument svgDoc;

	private Element svgRoot;

	private Element img;

	private HashMap<net.trevize.labelthem.jaxb.Object, Element> objectsElementsMap;

	private Vector<Element> polygons;

	private String polygonStyle_visible = "stroke:#fff000;stroke-width:2;fill:none;";

	private String polygonStyle_notVisible = "stroke:#fff000;stroke-width:2;fill:none;visibility:hidden";

	public SVGDocument getSVGDocument(Annotation a, String pathToLabelMeImages,
			Dimension imageMaxSizeOnScreen) {

		this.a = a;
		this.pathToLabelMeImages = pathToLabelMeImages;
		this.imageMaxSizeOnScreen = imageMaxSizeOnScreen;
		objectsElementsMap = new HashMap<net.trevize.labelthem.jaxb.Object, Element>();

		//get the image real dimension.
		BufferedImage bimg = loadBufferedImage(pathToLabelMeImages + "/"
				+ a.getFolder().getContent() + "/"
				+ a.getFilename().getContent());
		imageSize = new Dimension(bimg.getWidth(), bimg.getHeight());
		bimg = null;

		//get the resize X and Y factor (for updating polygon points).
		//get the new image dimension abiding the imageMaxSizeOnScreen dimension. 
		imageSizeOnScreen = getScaledSize(imageSize.width, imageSize.height,
				imageMaxSizeOnScreen.width, imageMaxSizeOnScreen.height);

		//creating the SVGDocument.
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		svgDoc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
		SVGOMDocument omDocument = (SVGOMDocument) svgDoc;
		try {
			URL url = new File(pathToLabelMeImages + "/"
					+ a.getFolder().getContent()).toURL();
			omDocument.setURLObject(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// get the root element (the svg element)
		svgRoot = svgDoc.getDocumentElement();

		// set the width and height attribute on the root svg element
		svgRoot.setAttributeNS(null, "width", "" + imageSizeOnScreen.width);
		svgRoot.setAttributeNS(null, "height", "" + imageSizeOnScreen.height);

		// create the image node.
		img = svgDoc.createElementNS(svgNS, "image");
		img.setAttributeNS(null, "x", "0");
		img.setAttributeNS(null, "y", "0");
		img.setAttributeNS(null, "width", "" + imageSizeOnScreen.width);
		img.setAttributeNS(null, "height", "" + imageSizeOnScreen.height);
		img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", a
				.getFilename().getContent());

		// attach the image to the svg root element
		svgRoot.appendChild(img);

		float scale_x = (float) imageSizeOnScreen.width
				/ (float) imageSize.width;
		float scale_y = (float) imageSizeOnScreen.height
				/ (float) imageSize.height;

		// create polygons nodes.
		polygons = new Vector<Element>();
		StringBuffer points_list = null;
		for (net.trevize.labelthem.jaxb.Object o : a.getObject()) {

			points_list = new StringBuffer();

			for (net.trevize.labelthem.jaxb.Pt pt : o.getPolygon().getPt()) {

				int new_x = (int) (Integer.parseInt(pt.getX().getContent()) * scale_x);

				int new_y = (int) (Integer.parseInt(pt.getY().getContent()) * scale_y);

				points_list.append("" + new_x + "," + new_y + " ");

			}

			// create a polygon node.
			Element polygon = svgDoc.createElementNS(svgNS, "polygon");
			//add the polygon in the polygons list.
			polygons.add(polygon);
			//add the polygon in the net.trevize.labelthem.jaxb.Object / org.w3c.dom.Element mapping.
			objectsElementsMap.put(o, polygon);
			//setting the polygon attributes.
			polygon.setAttributeNS(null, "points", points_list.toString());
			polygon.setAttributeNS(null, "style", polygonStyle_visible);
			//attach the polygon to the svg root element.
			svgRoot.appendChild(polygon);

		}

		return svgDoc;
	}

	public Dimension getSVGDocumentSize() {
		return imageSizeOnScreen;
	}

	public void setSVGDocumentMaxSize(Dimension newImageMaxSizeOnScreen) {
		System.out.println("setSVGDocumentMaxSize()");

		imageMaxSizeOnScreen = newImageMaxSizeOnScreen;

		//get the resize X and Y factor (for updating polygon points).
		//get the new image dimension abiding the imageMaxSizeOnScreen dimension. 
		imageSizeOnScreen = getScaledSize(imageSize.width, imageSize.height,
				imageMaxSizeOnScreen.width, imageMaxSizeOnScreen.height);

		// set the width and height attribute on the root svg element
		svgRoot.setAttributeNS(null, "width", "" + imageSizeOnScreen.width);
		svgRoot.setAttributeNS(null, "height", "" + imageSizeOnScreen.height);

		// set the width and height attribute on the image element
		img.setAttributeNS(null, "width", "" + imageSizeOnScreen.width);
		img.setAttributeNS(null, "height", "" + imageSizeOnScreen.height);

		float scale_x = (float) imageSizeOnScreen.width
				/ (float) imageSize.width;
		float scale_y = (float) imageSizeOnScreen.height
				/ (float) imageSize.height;

		// create polygons nodes.
		StringBuffer points_list = null;
		for (int i = 0; i < a.getObject().size(); ++i) {
			net.trevize.labelthem.jaxb.Object o = a.getObject().get(i);

			points_list = new StringBuffer();

			for (net.trevize.labelthem.jaxb.Pt pt : o.getPolygon().getPt()) {

				int new_x = (int) (Integer.parseInt(pt.getX().getContent()) * scale_x);

				int new_y = (int) (Integer.parseInt(pt.getY().getContent()) * scale_y);

				points_list.append("" + new_x + "," + new_y + " ");

			}

			// update the polygon node.
			Element polygon = polygons.get(i);
			polygon.setAttributeNS(null, "points", points_list.toString());

		}
	}

	public void setPolygonVisibility(net.trevize.labelthem.jaxb.Object object,
			boolean visible) {
		Element polygon = objectsElementsMap.get(object);
		if (visible) {
			polygon.setAttributeNS(null, "style", polygonStyle_visible);
		} else {
			polygon.setAttributeNS(null, "style", polygonStyle_notVisible);
		}
	}

	private BufferedImage loadBufferedImage(String filepath) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(filepath));
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

		else {
			float scale_x = (float) maxWidth / (float) width;
			float scale_y = (float) maxHeight / (float) height;

			if (scale_x < scale_y) {
				imageDim.width = (int) (width * maxWidth / width);
				imageDim.height = (int) (height * maxWidth / width);
			} else {
				imageDim.width = (int) (width * maxHeight / height);
				imageDim.height = (int) (height * maxHeight / height);
			}

		}

		return imageDim;
	}
}
