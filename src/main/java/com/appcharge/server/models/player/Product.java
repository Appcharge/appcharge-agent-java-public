package com.appcharge.server.models.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private int amount;
    private ProductId productId;
    private String sku;
    private String name;
}
