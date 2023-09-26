package com.example.jettycontinuations;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet implementation class ContinuationsExampleServlet
 */
//@WebServlet("/ContinuationsExampleServlet")
public class NonContinuationsExampleServlet extends HttpServlet {
        private static final String URL = "https://example.com/";
//    private static final String URL = "http://localhost:8081/";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public NonContinuationsExampleServlet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestId = request.getParameter("id");

        response.setContentType("text/plain");
        response.getWriter().println("Request id is : " + requestId + " start: " + new Date());

        makeApacheHttpRequest();

        response.getWriter().println("Request id is : " + requestId + " end: " + new Date());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    protected void makeApacheHttpRequest() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpClient client = httpClientBuilder.build();
        HttpGet request = new HttpGet(URL);
//        try(CloseableHttpClient client = httpClientBuilder.build();) {
//        return client;
        try {
            client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
