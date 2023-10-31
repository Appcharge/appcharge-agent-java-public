package com.appcharge.server.models.player;

import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;

@Getter
@Setter
public class Product {
    private BigInteger amount;
    private ProductId productId;
    private String sku;
    private String name;
}
