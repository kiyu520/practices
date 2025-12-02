package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private Integer exesConId;         // 供应商ID（主键）
    private String supName;            // 供应商名称
    private String supAddress;         // 地址
    private String postcode;           // 邮编
    private String supTelephone;       // 电话
    private String supFax;             // 传真
    private String supRelationer;      // 联系人
    private String supEmail;           // 邮箱
}
