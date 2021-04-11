package com.mw.deleteshiftemployee.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mw.commonlib.format.Parser;
import com.mw.commonlib.response.Response;
import com.mw.deleteshiftemployee.service.Schedule;

import java.util.Optional;

import static com.mw.commonlib.util.Constants.EMPLOYEE_ID;
import static com.mw.commonlib.util.Constants.SHIFT_ID;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Response response = new Response();
        String shiftPath = input.getPathParameters().get(SHIFT_ID);
        String employeePath = input.getPathParameters().get(EMPLOYEE_ID);
        processRequest(shiftPath, employeePath, response);
        return response.sendResponse();
    }

    private void processRequest(String shiftPath, String employeePath, Response response) {
        Parser parser = new Parser();
        Optional<Long> optionalShiftId = parser.parsePathParameter(shiftPath, response);
        Optional<Long> optionalEmployeeId = parser.parsePathParameter(employeePath, response);
        if (optionalShiftId.isPresent() && optionalEmployeeId.isPresent()) {
            long shiftId = optionalShiftId.get();
            long employeeId = optionalEmployeeId.get();
            Schedule schedule = new Schedule(response);
            schedule.deleteShift(shiftId, employeeId);
        }
    }
}


