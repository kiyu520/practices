package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    int pro_id;
    String pro_name;
    double price;
    String type;
    double quantity;
    int sup_id;
}
