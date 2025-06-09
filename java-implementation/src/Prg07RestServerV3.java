import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class MembershipHandler {

    private final Map<String, String> database = new HashMap<>();

    // TODO: create, read, update, delete 메서드
    public JSONObject create(String id, String value) {
        JSONObject response = new JSONObject();
        if (database.containsKey(id)) {
            response.put(id, "None");
        } else {
            database.put(id, value);
            response.put(id, database.get(id));
        }
        return response;
    }

    public JSONObject read(String id) {
        JSONObject response = new JSONObject();
//        if (database.containsKey(id)) {
//            response.put(id, database.get(id));
//        } else {
//            response.put(id, "None");
//        }
        response.put(id, database.getOrDefault(id, "None"));
        return response;
    }

    public JSONObject update(String id, String value) {
        JSONObject response = new JSONObject();
        if (database.containsKey(id)) {
            database.put(id, value);
            response.put(id, database.get(id));
        } else {
            response.put(id, "None");
        }
        return response;
    }

    public JSONObject delete(String id) {
        JSONObject response = new JSONObject();
        if (database.containsKey(id)) {
            database.remove(id);
            response.put(id, "Removed");
        } else {
            response.put(id, "None");
        }
        return response;
    }
}

public class Prg07RestServerV3 {

    public static void main(String[] args) throws IOException {

        // TODO: HTTP 서버 생성 & 시작
        String serverName = "localhost";
        int serverPort = 5000;  // Flask default port

        HttpServer app = HttpServer.create(new InetSocketAddress(serverName, serverPort), 0);
        app.createContext("/", new MembershipApiHandler());
        app.setExecutor(null);

        System.out.println("## REST server started at http://" + serverName + ":" + serverPort);
        app.start(); // REST Server start

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop(0);
            System.out.println("REST server stopped.");
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static class MembershipApiHandler implements HttpHandler {

        private final MembershipHandler myManager = new MembershipHandler();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            URI uri = exchange.getRequestURI();
            String path = uri.getPath();

            if (path.startsWith("/membership_api/") && path.length() > "/membership_api/".length()) {
                String memberId = extractMemberId(path);
                handleMembershipRequest(exchange, method, memberId);

            } else {
                sendErrorResponse(exchange, 404, "Not Found");
            }
        }

        private String extractMemberId(String path) {
            return path.substring("/membership_api/".length());
        }

        private void handleMembershipRequest(HttpExchange exchange, String method, String memberId) throws IOException {
            JSONObject result;

            try {
                switch (method) {
                    case "POST":
                        String postValue = getFormData(exchange, memberId); // Extract data from POST req
                        result = myManager.create(memberId, postValue);     // real CRUD
                        break;

                    case "GET":
                        result = myManager.read(memberId);
                        break;

                    case "PUT":
                        String putValue = getFormData(exchange, memberId);
                        result = myManager.update(memberId, putValue);
                        break;

                    case "DELETE":
                        result = myManager.delete(memberId);
                        break;

                    default:
                        sendErrorResponse(exchange, 405, "Method Not Allowed");
                        return;
                }

                sendJsonResponse(exchange, 200, result);

            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Internal Server Error");
            }
        }

        // Get form data from request body
        private String getFormData(HttpExchange exchange, String key) throws IOException {
            String contentLengthHeader = exchange.getRequestHeaders().getFirst("Content-Length");
            if (contentLengthHeader == null || "0".equals(contentLengthHeader)) {
                return key; // default fallback
            }

            int contentLength = Integer.parseInt(contentLengthHeader);
            InputStream requestBody = exchange.getRequestBody();
            byte[] postDataBytes = requestBody.readNBytes(contentLength);
            String postData = new String(postDataBytes, StandardCharsets.UTF_8);

            String[] parts = postData.split("=", 2); // Parse form data
            if (parts.length >= 2) {
                return parts[1];
            }
            return key; // fallback
        }

        // Send JSON response
        private void sendJsonResponse(HttpExchange exchange, int statusCode, JSONObject jsonResponse) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            byte[] responseBytes = jsonResponse.toString().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);

            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseBytes);
            responseBody.close();
        }

        // Send error res
        private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            byte[] responseBytes = message.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);

            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseBytes);
            responseBody.close();
        }
    }
}