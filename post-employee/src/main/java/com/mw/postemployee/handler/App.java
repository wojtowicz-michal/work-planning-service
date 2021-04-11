package com.mw.postemployee.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mw.commonlib.entity.Employee;
import com.mw.postemployee.format.Parser;
import com.mw.commonlib.response.Response;
import com.mw.postemployee.service.EmployeeService;


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
        Optional<Employee> optionalEmployee = new Parser().parseRequestBody(requestBody, response);
        optionalEmployee.ifPresent(employee -> new EmployeeService(response).saveEmployee(employee));
    }
}


