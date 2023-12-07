package com.appcharge.server.models.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PublisherPayload {
    private String appChargePaymentId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime purchaseDateAndTimeUtc;
    private String gameId;
    private String playerId;
    private String bundleName;
    private String bundleId;
    private String sku;
    private int priceInCents;
    private String currency;
    private BigDecimal priceInDollar;
    private BigDecimal priceNetInDollar;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal originalPriceInDollar;
    private String action;
    private String actionStatus;
    private List<Product> products;
    private String paymentMethod;
}