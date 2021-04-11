package com.mw.getshift.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mw.commonlib.format.Parser;
import com.mw.commonlib.response.Response;
import com.mw.getshift.service.Schedule;

import java.util.Optional;

import static com.mw.commonlib.util.Constants.SHIFT_ID;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Response response = new Response();
        String shiftPath = input.getPathParameters().get(SHIFT_ID);
        processRequest(shiftPath, response);
        return response.sendResponse();
    }

    private void processRequest(String shiftPath, Response response) {
        Parser parser = new Parser();
        Optional<Long> optionalShiftId = parser.parsePathParameter(shiftPath, response);
        optionalShiftId.ifPresent(id -> new Schedule(response).fetchShift(id));
    }
}


