# Work planning service
Serverless REST API designed to facilitate management of a shift schedule at work. Project based on AWS architecture.

### How serverless computing works?
Serverless computing is an execution model where the cloud provider (AWS in this case) is responsible for executing a piece of code by dynamically allocating the resources and only charging for the amount of resources used to run the code. The code is typically run inside stateless containers that can be triggered by a variety of events like http requests or database events. The code that is sent to the cloud provider for execution is usually in the form of a function. Hence serverless is sometimes referred to as Functions as a Service. FaaS is the compute layer of a serverless architecture, which is AWS Lambda.

<p align="center">
  <img src="resources/aws.png"/>
</p>
<br>

## Configuration
If you want to run the API with your own AWS instance, you need to configure services first.
- Set up [DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SettingUp.DynamoWebService.html), create 2 tables - EMPLOYEES and WORK_SCHEDULE. Make sure you have assigned the correct partition keys, employeeID (N), and primaryKey (N).
- Configure  [API Gateway](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-http.html) based on endpoints in the github repository. Try to import it with generated API documentation, you can find it in the resources directory.
- Create [AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java.html) for each endpoint and assign it the appropriate roles in the IAM console.
- Make sure each role is linked to the tables by the Amazon Resource Name (ARN).

## Usage
Test the application with Swagger
   ```
   URL is currently hidden. It can be accessed privately.
   ```
## References
* [serverless-stack.com](https://serverless-stack.com/chapters/what-is-serverless.html)

