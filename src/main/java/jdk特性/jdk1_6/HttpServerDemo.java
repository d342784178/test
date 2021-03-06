package jdk特性.jdk1_6;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerDemo {
    //HttpServer http协议简单实现 (不需要tomcat)
    private static int count = 0;

    public static void main(String[] args) {
        try {
            HttpServer hs = HttpServer.create(new InetSocketAddress(8888), 0);
            hs.createContext("/", new MyHandler());
            hs.createContext("/java", new MyHandler());
            hs.setExecutor(null);
            hs.start();
            System.out.println("---begin---");
            System.out.println("Listening on " + hs.getAddress());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange he) throws IOException {
            System.out.println("Request " + count++);
            System.out.println(he.getHttpContext().getPath());

            InputStream is       = he.getRequestBody();
            String      response = "<font color='blue'>Happy Spring Festerval</font>";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}