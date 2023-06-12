# Reddit
The Reddit application is a platform that allows users to engage in discussions, share content, and discover interesting topics. Users can create accounts, and participate in various discussions by posting,  and voting on content.


## Postgres Instance Configuration
In order to use your instance without docker-compose you shoud install postgres localy and update the [ Database Configuration Section ] section for your requirement in ```src/main/resources/application.yml```

```yaml
#
# [ Database Configuration Section ]
#
spring:
  application:
    name: reddit-service
  datasource:
    url: jdbc:postgresql://localhost:5432/reddit
    username: your-username
    password: your-password
    driver-class-name: org.postgresql.Driver
    name: reddit
  
#
# [ Other Configuration Attributes ]
#
```

## Swagger UI
Open your browser at the following URL for Swagger UI (giving REST interface details):
http://localhost:8080/swagger-ui/index.html

## initial data
There is a seed data file located at  ```src/main/resources/data.sql``` .
You can modify this file according to your specific requirements and data specifications.

## weather
To integrate weather data into the Reddit application, we utilize the WeatherAPI service, which provides real-time weather information based on user-specified locations.
You can modify this file according to your specific requirements in ```src/main/resources/application.yml```

```yaml
#
# [ Weather Configuration Section ]
#
weather:
  api:
    key: 5cbe6ca8ac66447bbc9173923230806
    url: https://api.weatherapi.com/v1/current.json
    location: London
  
#
# [ Other Configuration Attributes ]
#
```

## How to run the application using Docker
Run docker-compose up -d to start the applications.

## How to run the application without Docker

### Building the project

Clone the project and use Maven to build the server

	$ mvn clean package
  

### Testing the project

Clone the project and use Maven to test the server

	$ mvn clean test
	
