package com.example.jettycontinuations;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "AsyncClientHttpExchangeFutureCallback", urlPatterns = { "/async-http-client" }, asyncSupported = true)
public class AsyncClientHttpExchangeFutureCallback extends HttpServlet {
    private static final String URL = "https://example.com/";
//    private static final String URL = "http://localhost:8081/";

    @Override
    protected void doGet(HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        doAsyncExternal();
        PrintWriter writer = resp.getWriter();
    }

    public void doAsyncExternal() throws IOException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000).build();
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            httpclient.start();
            final HttpGet[] requests = new HttpGet[] {
                    new HttpGet("https://httpbin.org/ip"),
                    new HttpGet("https://httpbin.org/ip"),
                    new HttpGet("https://httpbin.org/headers")
            };
            final CountDownLatch latch = new CountDownLatch(requests.length);
            for (final HttpGet request : requests) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {

                    @Override
                    public void completed(final HttpResponse response) {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + " cancelled");
                    }

                });
            }
            latch.await();
            System.out.println("Shutting down");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done");
    }

}