package com.mw.putshiftemployee.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.format.Parser;
import com.mw.commonlib.response.Response;
import com.mw.putshiftemployee.service.Schedule;

import java.util.Optional;

import static com.mw.commonlib.util.Constants.EMPLOYEE_ID;
import static com.mw.commonlib.util.Constants.SHIFT_ID;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Response response = new Response();
        String requestBody = input.getBody();
        String shiftPath = input.getPathParameters().get(SHIFT_ID);
        String employeePath = input.getPathParameters().get(EMPLOYEE_ID);
        processRequest(shiftPath, employeePath, requestBody, response);
        return response.sendResponse();
    }

    private void processRequest(String shiftPath, String employeePath, String requestBody, Response response) {
        Parser parser = new Parser();
        Optional<Long> optionalShiftId = parser.parsePathParameter(shiftPath, response);
        Optional<Long> optionalEmployeeId = parser.parsePathParameter(employeePath, response);
        if (optionalShiftId.isPresent() && optionalEmployeeId.isPresent()) {
            long shiftId = optionalShiftId.get();
            long employeeId = optionalEmployeeId.get();
            Optional<Shift> optionalShift = parser.parseRequestBody(requestBody, response);
            optionalShift.ifPresent(shift -> new Schedule(response).updateShift(shift, shiftId, employeeId));
        }
    }
}


