package com.example.jettycontinuations;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

/**
 * Servlet implementation class ContinuationsExampleServlet
 */
//@WebServlet("/ContinuationsExampleServlet")
public class ContinuationsExampleServlet extends HttpServlet {
    private static final String URL = "https://example.com/";
//    private static final String URL = "http://localhost:8081/";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ContinuationsExampleServlet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestId = request.getParameter("id");

        Continuation cont = ContinuationSupport.getContinuation(request);

        response.setContentType("text/plain");
        response.getWriter().println("Request id is : " + requestId + " start: " + new Date());

        cont.setTimeout(3000);

        makeApacheHttpRequest();

        // SUSPEND
        cont.suspend();

        response.getWriter().println("Request id is : " + requestId + " end: " + new Date());

        if (!cont.isInitial()) {
            cont.complete();
        }
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
