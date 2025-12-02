package com.Frame;

/**
 * 供应商实体类，对应数据库suppliers表
 */
public class Supplier {
    // 供应商id（主键）
    private int exesConId;
    // 供应商名称
    private String supName;
    // 供应商地址
    private String supAddress;
    // 邮编
    private String postcode;
    // 电话
    private String supTelephone;
    // 传真
    private String supFax;
    // 联系人
    private String supRelationer;
    // 邮箱
    private String supEmail;

    // 无参构造
    public Supplier() {}

    // 全参构造
    public Supplier(int exesConId, String supName, String supAddress, String postcode,
                    String supTelephone, String supFax, String supRelationer, String supEmail) {
        this.exesConId = exesConId;
        this.supName = supName;
        this.supAddress = supAddress;
        this.postcode = postcode;
        this.supTelephone = supTelephone;
        this.supFax = supFax;
        this.supRelationer = supRelationer;
        this.supEmail = supEmail;
    }

    // Getter和Setter方法
    public int getExesConId() {
        return exesConId;
    }

    public void setExesConId(int exesConId) {
        this.exesConId = exesConId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupAddress() {
        return supAddress;
    }

    public void setSupAddress(String supAddress) {
        this.supAddress = supAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getSupTelephone() {
        return supTelephone;
    }

    public void setSupTelephone(String supTelephone) {
        this.supTelephone = supTelephone;
    }

    public String getSupFax() {
        return supFax;
    }

    public void setSupFax(String supFax) {
        this.supFax = supFax;
    }

    public String getSupRelationer() {
        return supRelationer;
    }

    public void setSupRelationer(String supRelationer) {
        this.supRelationer = supRelationer;
    }

    public String getSupEmail() {
        return supEmail;
    }

    public void setSupEmail(String supEmail) {
        this.supEmail = supEmail;
    }
}