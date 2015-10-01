package com.github.mwarc.realtimeauctions.handler;

import io.vertx.core.Handler;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;


public interface FormLoginWithTokenHandler extends Handler<RoutingContext> {

    static FormLoginWithTokenHandler create(AuthProvider authProvider) {
        return new FormLoginWithTokenHandlerImpl(authProvider);
    }
}
