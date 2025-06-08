import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Prg01HttpWebServer {

    public static void main(String[] args) throws IOException {

        String serverName = "localhost";
        int serverPort = 8080;

        HttpServer webServer = HttpServer.create(new InetSocketAddress(serverName, serverPort), 0);
        webServer.createContext("/", new MyHttpHandler());
        webServer.setExecutor(null);

        System.out.println("## HTTP server started at http://" + serverName + ":" + serverPort + ".");
        webServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server....");
            webServer.stop(0);
            System.out.println("HTTP server stopped.");
        }));

        try {
            Thread.currentThread().join();  // 서버가 계속 실행되도록 대기

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Server interrupted.");
        }
        webServer.stop(0);
        System.out.println("HTTP server stopped.");
    }

    static class MyHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();    // "GET" or "POST

            if ("GET".equals(method)) {
                doGet(exchange);

            } else if ("POST".equals(method)) {
                doPost(exchange);
            }
        }

        private void printHttpRequestDetail(HttpExchange exchange) {

            System.out.println("::Client address    : " + exchange.getRemoteAddress().getAddress().getHostAddress());
            System.out.println("::Client port       : " + exchange.getRemoteAddress().getPort());
            System.out.println("::Request command   : " + exchange.getRequestMethod());
            System.out.println("::Request line      : " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
            System.out.println("::Request path      : " + exchange.getRequestURI().getPath());
            System.out.println("::Request version   : HTTP/1.1");   // Java's HttpExchange doesn't provide direct access to HTTP version
            // HttpServer uses HTTP/1.1 by default
        }

        private void sendHttpResponseHeader(HttpExchange exchange) throws IOException {

            exchange.getResponseHeaders().set("Content-Type", "text/html");

            exchange.sendResponseHeaders(200, 0);
        }

        private void doGet(HttpExchange exchange) throws IOException {

            System.out.println("## doGet() activated.");

            printHttpRequestDetail(exchange);
            sendHttpResponseHeader(exchange);

            URI uri = exchange.getRequestURI();
            String path = uri.getPath();
            String query = uri.getQuery();

            if (query != null) {

                List<Integer> parameter = parameterRetrieval(query);
                int result = simpleCalc(parameter.get(0), parameter.get(1)); // Calculation

                OutputStream responseBody = exchange.getResponseBody(); // GET response generation

                responseBody.write("<html>".getBytes(StandardCharsets.UTF_8));
                String getResponse = String.format("GET request for calculation => %d x %d = %d",
                        parameter.get(0), parameter.get(1), result);
                responseBody.write(getResponse.getBytes(StandardCharsets.UTF_8));
                responseBody.write("</html>".getBytes(StandardCharsets.UTF_8));
                responseBody.close();

                System.out.printf("## GET request for calculation => " + parameter.get(0) + " x " + parameter.get(1) + " = " + result + ".%n");

            } else {

                OutputStream responseBody = exchange.getResponseBody();

                responseBody.write("<html>".getBytes(StandardCharsets.UTF_8));

                String pathResponse = String.format("<p>HTTP Request GET for Path: %s</p>", path);
                responseBody.write(pathResponse.getBytes(StandardCharsets.UTF_8));

                responseBody.write("</html>".getBytes(StandardCharsets.UTF_8));
                responseBody.close();

                System.out.println("## GET request for directory => " + path + ".");
            }

        }

        private void doPost(HttpExchange exchange) throws IOException {

            System.out.println("## doPost() activated.");

            printHttpRequestDetail(exchange);
            sendHttpResponseHeader(exchange);

            String contentLengthHeader = exchange.getRequestHeaders().getFirst("Content-Length");
            int contentLength = Integer.parseInt(contentLengthHeader);

            InputStream requestBody = exchange.getRequestBody();
            byte[] postDataBytes = requestBody.readNBytes(contentLength);
            String postData = new String(postDataBytes, StandardCharsets.UTF_8);

            List<Integer> parameter = parameterRetrieval(postData);
            int result = simpleCalc(parameter.get(0), parameter.get(1));

            String postResponse = String.format("POST request for calculation => %d x %d = %d",
                    parameter.get(0), parameter.get(1), result);

            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(postResponse.getBytes(StandardCharsets.UTF_8));
            responseBody.close();

            System.out.println("## POST request data => " + postData + ".");

            System.out.printf("## POST request for calculation => %d x %d = %d.%n",
                    parameter.get(0), parameter.get(1), result);
        }

        private int simpleCalc(int para1, int para2) {
            return para1 * para2;
        }

        private List<Integer> parameterRetrieval(String msg) {

            List<Integer> result = new ArrayList<>();
            String[] fields = msg.split("&");
            result.add(Integer.parseInt(fields[0].split("=")[1]));
            result.add(Integer.parseInt(fields[1].split("=")[1]));

            return result;
        }

    }
}
