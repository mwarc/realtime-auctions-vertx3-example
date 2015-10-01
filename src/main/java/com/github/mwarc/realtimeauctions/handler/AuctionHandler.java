package com.github.mwarc.realtimeauctions.handler;

import com.github.mwarc.realtimeauctions.model.Auction;
import com.github.mwarc.realtimeauctions.repository.AuctionRepository;
import com.github.mwarc.realtimeauctions.validation.Validator;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Optional;

public class AuctionHandler {

    private final AuctionRepository repository;
    private final Validator validator;

    public AuctionHandler(AuctionRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void handleGetAuction(RoutingContext context) {
        String auctionId = context.request().getParam("id");

        Optional<Auction> auction = this.repository.getById(auctionId);
        if (auction.isPresent()) {
            context.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(auction.get()));
        } else {
            context.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(404)
                .end();
        }
    }

    public void handleChangeAuction(RoutingContext context) {
        String auctionId = context.request().getParam("id");
        Auction auctionRequest = new Auction(
            auctionId,
            new BigDecimal(context.getBodyAsJson().getString("price")),
            context.user().principal().getString("sub"),
            null
        );

        if (validator.validate(auctionRequest)) {
            this.repository.updatePriceAndBuyer(auctionRequest);
            context.vertx().eventBus().publish("auction." + auctionId, Json.encodePrettily(auctionRequest));

            context.response()
                .setStatusCode(200)
                .end();
        } else {
            context.response()
                .setStatusCode(422)
                .end();
        }
    }

    public void initAuctionInSharedData(RoutingContext context) {
        String auctionId = context.request().getParam("id");

        Optional<Auction> auction = this.repository.getById(auctionId);
        if(!auction.isPresent()) {
            this.repository.insert(new Auction(auctionId));
        }

        context.next();
    }
}
