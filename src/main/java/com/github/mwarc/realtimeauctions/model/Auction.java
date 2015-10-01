package com.github.mwarc.realtimeauctions.model;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Auction {

    private final String id;
    private final BigDecimal price;
    private final String buyer;
    private final ZonedDateTime endingTime;

    public Auction(String id, BigDecimal price, String buyer, ZonedDateTime endingTime) {
        this.id = id;
        this.price = price;
        this.buyer = buyer;
        this.endingTime = endingTime;
    }

    public Auction(String id) {
        this(id, BigDecimal.ZERO, "", ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(3));
    }

    public String getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getBuyer() {
        return buyer;
    }

    public ZonedDateTime getEndingTime() {
        return endingTime;
    }

    @Override
    public String toString() {
        return "Auction{" +
            "id='" + id + '\'' +
            ", price=" + price +
            ", buyer='" + buyer + '\'' +
            ", endingTime=" + endingTime +
            '}';
    }
}
