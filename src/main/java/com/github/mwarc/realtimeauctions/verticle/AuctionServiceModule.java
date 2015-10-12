package com.github.mwarc.realtimeauctions.verticle;

import com.github.mwarc.realtimeauctions.validation.AuctionValidator;
import com.github.mwarc.realtimeauctions.validation.Validator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.SharedData;

public class AuctionServiceModule extends AbstractModule {

    private final Vertx vertx;

    public AuctionServiceModule(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(this.vertx);
        bind(Validator.class).to(AuctionValidator.class);
    }

    @Provides
    SharedData provideSharedData(Vertx vertx) {
        return vertx.sharedData();
    }
}
