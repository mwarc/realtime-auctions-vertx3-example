package com.github.mwarc.realtimeauctions.handler;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class ProxyHandlerImpl implements ProxyHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProxyHandlerImpl.class);

    private final String host;
    private final int port;

    public ProxyHandlerImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void handle(RoutingContext context) {
        logger.info("Proxying request: " + context.request().uri());

        HttpClient client = context.vertx().createHttpClient();
        HttpClientRequest clientRequest = client.request(context.request().method(), port, host, context.request().uri(), clientResult -> {
            context.request().response().setStatusCode(clientResult.statusCode());
            context.request().response().headers().setAll(clientResult.headers());
            clientResult.handler(data -> context.request().response().write(data));
            clientResult.endHandler((v) -> context.request().response().end());
        });
        clientRequest.headers().setAll(context.request().headers());
        clientRequest.putHeader("Authorization", "Bearer " + context.session().get("token"));
        context.request().handler(clientRequest::write);
        context.request().endHandler((v) -> clientRequest.end());
    }
}
