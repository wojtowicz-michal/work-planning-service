package com.mw.postshiftemployee.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.format.Parser;
import com.mw.commonlib.response.Response;
import com.mw.postshiftemployee.service.Schedule;

import java.util.Optional;

import static com.mw.commonlib.util.Constants.EMPLOYEE_ID;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Response response = new Response();
        String requestBody = input.getBody();
        String employeePath = input.getPathParameters().get(EMPLOYEE_ID);
        processRequest(employeePath, requestBody, response);
        return response.sendResponse();
    }

    private void processRequest(String employeePath, String requestBody, Response response) {
        Parser parser = new Parser();
        Optional<Long> optionalEmployeeId = parser.parsePathParameter(employeePath, response);
        if (optionalEmployeeId.isPresent()) {
            long employeeId = optionalEmployeeId.get();
            Optional<Shift> optionalShift = parser.parseRequestBody(requestBody, response);
            optionalShift.ifPresent(shift -> new Schedule(response).saveShift(shift, employeeId));
        }
    }
}


