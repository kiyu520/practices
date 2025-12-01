package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    int pro_id;
    String pro_name;
    float price;
    String type;
    float quantity;
    int sup_id;
}
