package com.appcharge.server.models.events;

import com.appcharge.server.enums.events.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class EventsRequest {
    public long timestamp;
    public EventType event;
    public String appChargePaymentId;
    public String playerId;
    public PublisherEventOffer offer;
    public Map<String, Object> sessionMetadata;
    public String paymentMethod;
    public String reason;

    @Getter
    @Setter
    public static class PublisherEventOffer {
        String offerName;
        String offerInternalId;
        String offerExternalId;
        double priceInDollar;
        int priceInCents;
        double subtotal;
        double tax;
        String currency;
        double originalPriceInDollar;
        String country;
        List<OfferSnapshotProduct> products;
    }
    @Getter
    @Setter
    public static class OfferSnapshotProduct {
        public String name;
        public String sku;
        public int amount;
    }
}
