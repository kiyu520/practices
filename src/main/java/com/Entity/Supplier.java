package com.Entity;

public class Supplier {
    private String supplyname;
    private int supplierid;
    private String supplierpostcode;
    private String supplieraddress;
    private String supplierphone;
    private String supplieremail;
    private String supplierfax;
    private String supplierrelationer;
    public Supplier(){

    }
    public Supplier(int supplierid,String supplyname,String supplieraddress,String supplierpostcode,String supplierphone,String supplierfax,String supplieremail,String supplierrelationer) {}
    public String getSupplyname() {
        return supplyname;
    }
    public void setSupplyname(String supplyname) {
        this.supplyname = supplyname;
    }
    public int getSupplierid() {
        return supplierid;
    }
    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }
    public String getSupplierpostcode() {
        return supplierpostcode;
    }
    public void setSupplierpostcode(String supplierpostcode) {
        this.supplierpostcode = supplierpostcode;
    }
    public String getSupplieraddress() {
        return supplieraddress;
    }
    public void setSupplieraddress(String supplieraddress) {
        this.supplieraddress = supplieraddress;
    }
    public String getSupplierphone() {
        return supplierphone;
    }
    public void setSupplierphone(String supplierphone) {
        this.supplierphone = supplierphone;
    }
    public String getSupplieremail() {
        return supplieremail;
    }
    public void setSupplieremail(String supplieremail) {
        this.supplieremail = supplieremail;
    }
    public String getSupplierfax() {
        return supplierfax;
    }
    public void setSupplierfax(String supplierfax) {
        this.supplierfax = supplierfax;
    }
    public String getSupplierrelationer() {
        return supplierrelationer;
    }
    public void setSupplierrelationer(String supplierrelationer) {
        this.supplierrelationer = supplierrelationer;
    }
}
