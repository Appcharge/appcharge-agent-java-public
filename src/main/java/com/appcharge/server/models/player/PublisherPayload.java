package com.appcharge.server.models.player;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PublisherPayload {
    private String appChargePaymentId;
    private LocalDateTime purchaseDateAndTimeUtc;
    private String gameId;
    private String playerId;
    private String bundleName;
    private String bundleId;
    private String sku;
    private int priceInCents;
    private String currency;
    private BigDecimal priceInDollar;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private String action;
    private String actionStatus;
    private List<Product> products;
    private String publisherToken;
}