package com.appcharge.server.models.player;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerData {

    private String status;
    private String playerProfileImage;
    private String publisherPlayerId;
    private String playerName;
    private List<Offer> offers;
    private List<String> segments;
    private List<Balance> balances;
    private Focus focus;

    @Getter
    @Setter
    public static class Offer {
        private String publisherOfferId;
        private List<ProductSequenceItem> productsSequence;
    }

    @Getter
    @Setter
    public static class ProductSequenceItem{
        private String index;
        private List<Product> products;
    }

    @Getter
    @Setter
    public static class Product{
        private String publisherProductId;
        private int quantity;
    }

    @Getter
    @Setter
    public static class Balance{
        private String publisherProductId;
        private int quantity;
    }

    @Getter
    @Setter
    public static class Focus{
        private String publisherSpecialOfferId;
        private String publisherBundleId;
    }
}
