package com.github.mwarc.realtimeauctions.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;

public class FormLoginWithTokenHandlerImpl implements FormLoginWithTokenHandler {

    private final AuthProvider authProvider;

    public FormLoginWithTokenHandlerImpl(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public void handle(RoutingContext context) {
        JsonObject authInfo = new JsonObject()
            .put("username", context.request().getParam("username"))
            .put("password", context.request().getParam("password"));
        authProvider.authenticate(authInfo, res -> {
            if (res.succeeded()) {
                JWTAuth jwt = JWTAuth.create(context.vertx(), new JsonObject().put("keyStore", new JsonObject()
                    .put("type", "jceks")
                    .put("path", "keystore.jceks")
                    .put("password", "secret")));
                String token = jwt.generateToken(
                    new JsonObject().put("sub", context.request().getParam("username")),
                    new JWTOptions().setExpiresInMinutes(60L)
                );
                context.setUser(res.result());
                context.session().put("token", token);
                context.response()
                    .putHeader("location", (String) context.session().remove("return_url"))
                    .setStatusCode(302)
                    .end();
            } else {
                context.fail(401);
            }
        });
    }
}
