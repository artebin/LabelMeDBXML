//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 03:19:34 PM CEST 
//


package net.trevize.labelthem.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


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
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="keyword">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="wnstkeyword">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="wordForm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="synsetType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
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
    "keywordOrWnstkeyword"
})
@XmlRootElement(name = "kbannotation")
public class Kbannotation {

    @XmlElements({
        @XmlElement(name = "keyword", type = Kbannotation.Keyword.class),
        @XmlElement(name = "wnstkeyword", type = Kbannotation.Wnstkeyword.class)
    })
    protected List<java.lang.Object> keywordOrWnstkeyword;

    /**
     * Gets the value of the keywordOrWnstkeyword property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keywordOrWnstkeyword property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeywordOrWnstkeyword().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Kbannotation.Keyword }
     * {@link Kbannotation.Wnstkeyword }
     * 
     * 
     */
    public List<java.lang.Object> getKeywordOrWnstkeyword() {
        if (keywordOrWnstkeyword == null) {
            keywordOrWnstkeyword = new ArrayList<java.lang.Object>();
        }
        return this.keywordOrWnstkeyword;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "content"
    })
    public static class Keyword {

        @XmlValue
        protected String content;

        /**
         * Gets the value of the content property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContent() {
            return content;
        }

        /**
         * Sets the value of the content property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContent(String value) {
            this.content = value;
        }

    }


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
     *         &lt;element name="wordForm" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="synsetType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "wordForm",
        "synsetType",
        "id"
    })
    public static class Wnstkeyword {

        @XmlElement(required = true)
        protected String wordForm;
        @XmlElement(required = true)
        protected String synsetType;
        @XmlElement(required = true)
        protected String id;

        /**
         * Gets the value of the wordForm property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWordForm() {
            return wordForm;
        }

        /**
         * Sets the value of the wordForm property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWordForm(String value) {
            this.wordForm = value;
        }

        /**
         * Gets the value of the synsetType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSynsetType() {
            return synsetType;
        }

        /**
         * Sets the value of the synsetType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSynsetType(String value) {
            this.synsetType = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

    }

}
