/**
 * TGetInfo_Input.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.epages.ws.crossselling2.model;


/**
 * input value of a getInfo() call.
 */
public class TGetInfo_Input  implements java.io.Serializable {
    /* product object path */
    private java.lang.String product;

    /* cross selling type could be 'Accessory', 'ProductComparison'
     * or 'CrossSelling'(default) - may use '*' for all */
    private java.lang.String type;

    public TGetInfo_Input() {
    }

    public TGetInfo_Input(
           java.lang.String product,
           java.lang.String type) {
           this.product = product;
           this.type = type;
    }


    /**
     * Gets the product value for this TGetInfo_Input.
     * 
     * @return product   * product object path
     */
    public java.lang.String getProduct() {
        return product;
    }


    /**
     * Sets the product value for this TGetInfo_Input.
     * 
     * @param product   * product object path
     */
    public void setProduct(java.lang.String product) {
        this.product = product;
    }


    /**
     * Gets the type value for this TGetInfo_Input.
     * 
     * @return type   * cross selling type could be 'Accessory', 'ProductComparison'
     * or 'CrossSelling'(default) - may use '*' for all
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this TGetInfo_Input.
     * 
     * @param type   * cross selling type could be 'Accessory', 'ProductComparison'
     * or 'CrossSelling'(default) - may use '*' for all
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TGetInfo_Input)) return false;
        TGetInfo_Input other = (TGetInfo_Input) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.product==null && other.getProduct()==null) || 
             (this.product!=null &&
              this.product.equals(other.getProduct()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getProduct() != null) {
            _hashCode += getProduct().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TGetInfo_Input.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn://epages.de/WebService/CrossSellingTypes/2007/01", "TGetInfo_Input"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("product");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Product"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
