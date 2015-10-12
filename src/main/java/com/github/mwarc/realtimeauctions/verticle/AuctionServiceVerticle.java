package com.github.mwarc.realtimeauctions.verticle;


import com.github.mwarc.realtimeauctions.handler.AuctionHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class AuctionServiceVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route(HttpMethod.PATCH, "/api/*").handler(jwtAuthHandler());
        router.mountSubRouter("/api", auctionApiRouter());

        vertx.createHttpServer().requestHandler(router::accept).listen(8081);
    }

    private JWTAuthHandler jwtAuthHandler() {
        JsonObject authConfig = new JsonObject().put("keyStore", new JsonObject()
            .put("type", "jceks")
            .put("path", "keystore.jceks")
            .put("password", "secret"));
        JWTAuth authProvider = JWTAuth.create(vertx, authConfig);

        return JWTAuthHandler.create(authProvider);
    }

    private Router auctionApiRouter() {
        Injector injector = Guice.createInjector(new AuctionServiceModule(vertx));
        AuctionHandler auctionHandler = injector.getInstance(AuctionHandler.class);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route().consumes("application/json");
        router.route().produces("application/json");

        router.route("/auctions/:id").handler(auctionHandler::initAuctionInSharedData);
        router.get("/auctions/:id").handler(auctionHandler::handleGetAuction);
        router.patch("/auctions/:id").handler(auctionHandler::handleChangeAuction);

        return router;
    }
}
