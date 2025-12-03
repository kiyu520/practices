package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    int prod_id;
    String prod_name;
    double price;
    String type;
    double quantity;
    int sup_id;
}
