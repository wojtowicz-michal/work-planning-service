package com.mw.commonlib.response;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.Getter;
import lombok.Setter;

import static com.mw.commonlib.response.ResponseMessage.*;

@Getter
@Setter
public class Response {
    private int status;
    private String body;

    public APIGatewayProxyResponseEvent sendResponse() {
        if (!isStatusSet()) setStatusCode();
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withBody(body);
    }

    public void appendBody(String body) {
        if (this.body == null) {
            this.body = body;
        } else {
            this.body += "\n" + body;
        }
    }

    private void setStatusCode() {
        switch (body) {
            case EMPLOYEE_SAVED:
            case EMPLOYEE_DELETED:
            case SHIFT_SAVED:
            case SHIFT_UPDATED:
            case SHIFT_DELETED:
            case SAVED_SHIFTS:
            case FAILED_SAVES:
                status = 200;
                break;

            case INVALID_REQUEST_BODY:
            case NULL_REQUEST_BODY:
                status = 400;
                break;

            default:
                status = 500;
        }
    }

    private boolean isStatusSet() {
        return status != 0;
    }
}
