//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 03:19:34 PM CEST 
//


package net.trevize.labelthem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}sourceImage"/>
 *         &lt;element ref="{}sourceAnnotation"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sourceImage",
    "sourceAnnotation"
})
@XmlRootElement(name = "source")
public class Source {

    @XmlElement(required = true)
    protected SourceImage sourceImage;
    @XmlElement(required = true)
    protected SourceAnnotation sourceAnnotation;

    /**
     * Gets the value of the sourceImage property.
     * 
     * @return
     *     possible object is
     *     {@link SourceImage }
     *     
     */
    public SourceImage getSourceImage() {
        return sourceImage;
    }

    /**
     * Sets the value of the sourceImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceImage }
     *     
     */
    public void setSourceImage(SourceImage value) {
        this.sourceImage = value;
    }

    /**
     * Gets the value of the sourceAnnotation property.
     * 
     * @return
     *     possible object is
     *     {@link SourceAnnotation }
     *     
     */
    public SourceAnnotation getSourceAnnotation() {
        return sourceAnnotation;
    }

    /**
     * Sets the value of the sourceAnnotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceAnnotation }
     *     
     */
    public void setSourceAnnotation(SourceAnnotation value) {
        this.sourceAnnotation = value;
    }

}