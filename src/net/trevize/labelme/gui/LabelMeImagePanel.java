package net.trevize.labelme.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import net.trevize.gui.layout.CellStyle;
import net.trevize.gui.layout.XGridBag;
import net.trevize.labelthem.jaxb.Annotation;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

/**
 *   * The image is resized following the panel size, i.e. when resizing the panel
 *     the image is reduced or enlarged to fit the panel. If the panel is bigger
 *     than the image, the image is no more enlarged (this is brought by the
 *     net.trevize.LabelMeDocument2SVG.getScaledSize(...) method which return the
 *     real image size when the new width/height for scaling is bigger than the 
 *     real image size).
 * 
 * TODO:
 *   * add a zoom action (using the CTRL-MouseWheel user interaction).
 *   * add an image status (like the zoom factor, like in GIMP).
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LabelMeImagePanel.java - May 27, 2009
 */

public class LabelMeImagePanel extends JPanel implements ComponentListener {

	private Annotation a;

	private String pathToLabelMeImages;

	private LabelMeDocument2SVG lmd2svg;

	private SVGDocument svgDoc;

	private XGridBag xgb0;

	private JSVGCanvas svgCanvas0;

	private Dimension imageMaxSizeOnScreen;

	private Dimension imageSizeOnScreen;

	public LabelMeImagePanel(net.trevize.labelthem.jaxb.Annotation a,
			String pathToLabelMeImages, Dimension imageMaxSizeOnScreen) {
		this.a = a;
		this.pathToLabelMeImages = pathToLabelMeImages;
		this.imageMaxSizeOnScreen = imageMaxSizeOnScreen;

		init();
		addComponentListener(this);
	}

	public Dimension getImageMaxSizeOnScreen() {
		return imageMaxSizeOnScreen;
	}

	public void setImageMaxSizeOnScreen(Dimension imageMaxSizeOnScreen) {
		this.imageMaxSizeOnScreen = imageMaxSizeOnScreen;
		lmd2svg.setSVGDocumentMaxSize(imageMaxSizeOnScreen);
		svgCanvas0.setDocument(svgDoc);
	}

	public void setPolygonVisibility(net.trevize.labelthem.jaxb.Object object,
			boolean visible) {
		lmd2svg.setPolygonVisibility(object, visible);
		svgCanvas0.setDocument(svgDoc);
	}

	private void init() {
		setLayout(new GridBagLayout());
		xgb0 = new XGridBag(this);

		svgCanvas0 = new JSVGCanvas();
		lmd2svg = new LabelMeDocument2SVG();

		/*
		System.out.println("trying to load: " + a.getFolder().getContent() + "/"
				+ a.getFilename().getContent());
		*/

		svgDoc = lmd2svg.getSVGDocument(a, pathToLabelMeImages,
				imageMaxSizeOnScreen);
		svgCanvas0.setDocument(svgDoc);
		imageSizeOnScreen = lmd2svg.getSVGDocumentSize();

		CellStyle style0 = new CellStyle(1., 1., GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		xgb0.add(svgCanvas0, style0, 0, 0);

		setPreferredSize(imageSizeOnScreen);
		setSize(imageSizeOnScreen);
	}

	/***************************************************************************
	 * implementation of ComponentListener.
	 **************************************************************************/

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		setImageMaxSizeOnScreen(getSize());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

}
