package net.trevize.labelme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.trevize.labelme.gui.LabelMeAnnotationPanel;
import net.trevize.labelme.gui.LabelMeAnnotationPanel;
import net.trevize.labelme.gui.LabelMeImagePanel;
import net.trevize.labelthem.jaxb.Annotation;

/**
 * Using Batik for the displaying of the image and his annotation (bounding
 * polygons). 
 * Using a JTree (with checkbox for displaying or not the polygon) for all 
 * objects on the image.
 * The JTree could be generated from the XML annotation file, and checkboxes
 * are added only to 'object' XML element.
 * 
 * A net.trevize.labelthem.jaxb.Annotation or a path to such an annotation
 * has to be given in parameter to the constructor, with also the path to
 * ~LabelMe/database/images
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LabelMeImageViewerPanel.java - May 22, 2009
 */

public class LabelMeImageViewer extends JPanel implements WindowListener {

	private net.trevize.labelthem.jaxb.Annotation a;

	/*
	 * path to ~LabelMe/database/images
	 * the annotation file is given in arguments to the class constructor.
	 */
	private String pathToLabelMeImages = "/media/streeling/LabelMe/database/images";

	/*
	 * f_image is the image frame,
	 * f_annot is the metadata / JTree frame.
	 */
	private JFrame f_image, f_annot;

	private LabelMeImagePanel lmip;
	private LabelMeAnnotationPanel lmap;

	private JAXBContext jc;
	private Unmarshaller u;
	private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

	/*
	 * The variable imageMaxSizeOnScreen is the size of the 
	 * LabelMeImagePanel containing the JSVGCanvas (which is containing 
	 * the image).
	 * 
	 * The image is resized but we keep the image ratio, so the image size
	 * is not really the size of the panel containing the image.
	 * 
	 * NOTE: the size of the image on screen is in reality the size of the
	 * JSVGCanvas.
	 */
	private Dimension imageMaxSizeOnScreen;

	public LabelMeImageViewer(String pathToAnnotation,
			String pathToLabelMeImages) {
		//create the JAXBContext.
		try {
			jc = JAXBContext.newInstance("net.trevize.labelthem.jaxb");
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//instantiate an Unmarshaller.
		try {
			u = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//create an XMLStreamReader on the annotation file.
		XMLStreamReader xmlsr = null;
		try {
			xmlsr = xmlInputFactory.createXMLStreamReader(new FileInputStream(
					new File(pathToAnnotation)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (XMLStreamException e1) {
			e1.printStackTrace();
		}

		//load the annotation.
		a = null;
		try {
			a = (Annotation) u.unmarshal(xmlsr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		this.pathToLabelMeImages = pathToLabelMeImages;

		init_f_image(a); //init the image frame.
		init_f_annot(a); //init the meta / JTree frame.
	}

	public LabelMeImageViewer(net.trevize.labelthem.jaxb.Annotation a,
			String pathToLabelMeImages) {
		this.a = a;
		this.pathToLabelMeImages = pathToLabelMeImages;

		init_f_image(a); //initialize the image frame.
		init_f_annot(a); //initialize the annotation frame.
	}

	/*
	 * initialize the image frame.
	 */
	private void init_f_image(Annotation a) {
		f_image = new JFrame();
		f_image.addWindowListener(this);

		Dimension imageMaxSizeOnScreen = new Dimension(768, 512);
		lmip = new LabelMeImagePanel(a, pathToLabelMeImages,
				imageMaxSizeOnScreen);
		f_image.getContentPane().setLayout(new BorderLayout());
		f_image.getContentPane().add(lmip, BorderLayout.CENTER);
		f_image.pack();
		f_image.setLocation(412, 128);
		f_image.setVisible(true);
	}

	/*
	 * initialize the annotation frame.
	 */
	private void init_f_annot(Annotation a) {
		f_annot = new JFrame();
		f_annot.addWindowListener(this);

		f_annot.getContentPane().setLayout(new BorderLayout());
		f_annot.getContentPane().add(new LabelMeAnnotationPanel(lmip, a),
				BorderLayout.CENTER);
		f_annot.setSize(256, 512);
		f_annot.setLocation(42, 42);
		f_annot.setVisible(true);
	}

	public static void main(String args[]) {

		// setting the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		new LabelMeImageViewer("./p1010844.xml",
				"/home/nicolas/LabelMe/database/images");

	}

	/***************************************************************************
	 * implementing WindowListener
	 **************************************************************************/

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(e.getSource() == f_image || e.getSource() == f_annot){
			f_image.dispose();
			f_annot.dispose();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
