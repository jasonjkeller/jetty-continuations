# Jetty Continuations Servlet Example
Modified from here: https://examples.javacodegeeks.com/java-development/enterprise-java/jetty/jetty-continuations-example/

## Requirements
Java 8 to build

## Build
`./gradlew build` 

This will generate a WAR file here: `jetty-continuations/build/libs/jetty-continuations-1.0-SNAPSHOT.war`

## Deploy to Jetty
Copy the WAR file to your Jetty `webapps` directory for deployment

Run Jetty
```commandline
java -javaagent:/path/to/newrelic/newrelic.jar -jar start.jar
```

## Usage
The Jetty `ContinuationsExampleServlet` can be accessed here: http://localhost:8080/jetty-continuations-1.0-SNAPSHOT/ContinuationsExampleServlet

It shows basic usage of the Jetty Continuations API.

The `AsyncServletAPI` can be accessed here: http://localhost:8080/jetty-continuations-1.0-SNAPSHOT/async-test

It shows basic usage of the async servlet API that replaced Jetty Continuations.

Example of driving load using `wrk2`:
```commandline
wrk -t80 -c3000 -d120s http://localhost:8080/jetty-continuations-1.0-SNAPSHOT/ContinuationsExampleServlet
```

## Related Info

https://sberyozkin.blogspot.com/2008/12/continuations-in-cxf.html
https://cwiki.apache.org/confluence/display/CXF20DOC/Servlet+Transport