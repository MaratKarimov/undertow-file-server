package ru.maratk.undertow.file.server.handlers;

import io.undertow.io.IoCallback;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.Deque;

public class CustomHttpHandler implements HttpHandler {

    private final ResourceManager rm;

    public CustomHttpHandler(final ResourceManager rm) {
        this.rm = rm;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        final Deque<String> nameParameters = exchange.getQueryParameters().get("name");
        if(nameParameters != null){
            final String name = nameParameters.getFirst();
            if(name != null && (!name.isEmpty())){
                final Resource r = rm.getResource(name);
                if(r == null){
                    exchange.setStatusCode(StatusCodes.NOT_FOUND);
                    exchange.endExchange();
                } else r.serve(exchange.getResponseSender(), exchange, IoCallback.END_EXCHANGE);
            } else {
                sendExchangeBadRequestTextError(exchange, "You should send 'name' request parameter!");
                return;
            }
        } else {
            sendExchangeBadRequestTextError(exchange, "You should send 'name' request parameter!");
            return;
        }
    }

    private final void sendExchangeBadRequestTextError(final HttpServerExchange exchange, final String errorMessage){
        exchange.setStatusCode(StatusCodes.BAD_REQUEST);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(errorMessage);
        exchange.endExchange();
    }
}