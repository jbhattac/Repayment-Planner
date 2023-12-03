# Repayment Planner Application

##  Features

* The application is dockerized, and also cloud ready.
* To create create a repayment plan we need to send a POST to http://localhost:8080/generate-plan
  ```
  curl -X 'POST' \
  'http://localhost:8080/generate-plan' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
    "loanAmount": "5000",
    "nominalRate": "5.0",
    "duration": 24,
    "startDate": "01-01-2024"
    }'
 
  ```
  * The response will be
    ```
    {
    "borrowerPayments": [
      {
        "borrowerPaymentAmount": 219.37,
        "date": "01-01-2024",
        "initialOutstandingPrincipal": 5000,
        "interest": 20.55,
        "principal": 198.82,
        "remainingOutstandingPrincipal": 4801.18
      },
      {
        "borrowerPaymentAmount": 219.37,
        "date": "01-02-2024",
        "initialOutstandingPrincipal": 4801.18,
        "interest": 19.73,
        "principal": 199.64,
        "remainingOutstandingPrincipal": 4601.54
      }, 
    ...
      {
        "borrowerPaymentAmount": 219.37,
        "date": "01-12-2025",
        "initialOutstandingPrincipal": 214.3,
        "interest": 0.88,
        "principal": 214.3,
        "remainingOutstandingPrincipal": 0
      }
    ]
    }

* Swagger document can be found in

``` 
  http://localhost:8080/swagger-ui/index.html
``` 

* Actuator Prometheus endpoint is exposed via the following url

``` 
  http://localhost:8080/actuator/prometheus
  
  (http_server_requests_seconds_count{uri="/api/v1/shortUrl/url/**"})
``` 


### Stack

* Java 18
* Spring Boot 3
* JUnit
* Mockito
* maven
* Docker
* Open API Swagger document
* Prometheus and Actuator for monitoring

### Setup and Run

* Build and install
  ```
    mvn clean install
  ./mvnw spring-boot:run
  ```

*  Some Class and their purpose
- LoanResource - Controller class providing apis to create loan repayment plan.
- LoanRequest, RepaymentDetails - Used as dto to trnasfer data also provides type saftey and are immutable.
- LoanPaymentPlanService - Holds the business logic to actually create the repayment plan. (From SOLID - SRS)
- ErrorHandlingControllerAdvice - handles all the error flows.
- LoanPaymentPlanServiceTest - Unit test for service layer.
