package com.mw.postshift.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mw.postshift.format.Data;
import com.mw.postshift.format.Parser;
import com.mw.commonlib.response.Response;
import com.mw.postshift.service.Schedule;

import java.util.Optional;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Response response = new Response();
        String requestBody = input.getBody();
        processRequest(requestBody, response);
        return response.sendResponse();
    }

    private void processRequest(String requestBody, Response response) {
        Parser parser = new Parser();
        Optional<Data> optionalData = parser.parseRequestBody(requestBody, response);
        optionalData.ifPresent(data -> new Schedule(response).saveShift(data));
    }
}


