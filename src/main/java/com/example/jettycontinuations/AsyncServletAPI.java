package com.example.jettycontinuations;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "AsyncServletAPI", urlPatterns = { "/async-test" }, asyncSupported = true)
public class AsyncServletAPI extends HttpServlet {
    private static final String URL = "https://example.com/";
//    private static final String URL = "http://localhost:8081/";

    @Override
    protected void doGet(HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new MyAsyncListener());

        makeApacheHttpRequest();
        asyncContext.start(() -> {
            String msg = task();
            writer.println(msg);
            asyncContext.complete();
        });
    }

    private String task() {
        long start = System.currentTimeMillis();
        try {
            int i = ThreadLocalRandom.current()
                    .nextInt(1, 5);
            Thread.sleep(i * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "time to complete long task " + (System.currentTimeMillis() - start);
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