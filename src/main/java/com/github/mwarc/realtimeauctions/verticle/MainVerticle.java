package com.github.mwarc.realtimeauctions.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start() {
        vertx.deployVerticle("com.github.mwarc.realtimeauctions.verticle.SockJSBridgeVerticle", res -> {
            if (res.succeeded()) {
                logger.info("SockJSBridgeVerticle deployment id is: " + res.result());
            } else {
                logger.error("SockJSBridgeVerticle deployment failed!");
            }
        });

        vertx.deployVerticle("com.github.mwarc.realtimeauctions.verticle.AuctionServiceVerticle", res -> {
            if (res.succeeded()) {
                logger.info("AuctionServiceVerticle deployment id is: " + res.result());
            } else {
                logger.error("AuctionServiceVerticle deployment failed!");
            }
        });

        vertx.deployVerticle("com.github.mwarc.realtimeauctions.verticle.AuctionFrontendVerticle", res -> {
            if (res.succeeded()) {
                logger.info("AuctionFrontendVerticle deployment id is: " + res.result());
            } else {
                logger.error("AuctionFrontendVerticle deployment failed!");
            }
        });
    }
}
