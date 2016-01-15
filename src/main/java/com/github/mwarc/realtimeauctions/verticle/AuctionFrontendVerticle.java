package com.github.mwarc.realtimeauctions.verticle;

import com.github.mwarc.realtimeauctions.handler.FormLoginWithTokenHandler;
import com.github.mwarc.realtimeauctions.handler.ProxyHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class AuctionFrontendVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        AuthProvider authProvider = ShiroAuth.create(
            vertx,
            new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(new JsonObject())
        );
        router.route().handler(UserSessionHandler.create(authProvider));
        router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/login.html"));

        router.mountSubRouter("/tokens", tokenRouter(authProvider));
        router.mountSubRouter("/api", auctionApiRouter());

        router.route().failureHandler(ErrorHandler.create(true));
        router.route("/private/*").handler(staticHandler("private"));
        router.route().handler(staticHandler("public"));

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private Router tokenRouter(AuthProvider authProvider) {
        Router router = Router.router(vertx);

        router.post().handler(BodyHandler.create());
        router.post().handler(FormLoginWithTokenHandler.create(authProvider));

        return router;
    }

    private Router auctionApiRouter() {
        Router router = Router.router(vertx);

        router.route().consumes("application/json");
        router.route().produces("application/json");

        router.get("/auctions/:id").handler(ProxyHandler.create("localhost", 8081));
        router.patch("/auctions/:id").handler(ProxyHandler.create("localhost", 8081));

        return router;
    }

    private StaticHandler staticHandler(String webRoot) {
        return StaticHandler.create()
            .setCachingEnabled(false)
            .setWebRoot(webRoot);
    }
}
