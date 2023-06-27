package com.appcharge.server.models.player;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Price {
    private BigDecimal subTotal;
    private BigDecimal tax;
}