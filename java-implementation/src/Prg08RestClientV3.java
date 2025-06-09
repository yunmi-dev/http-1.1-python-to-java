import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Prg08RestClientV3 {

    public static void main(String[] args) {
        System.out.println("## REST Client started.");

        try {
            // 1. Read a non registed member (error case)
            Response r1 = sendRequest("GET", "http://127.0.0.1:5000/membership_api/0001", null);
            System.out.println("#1 Code: " + r1.statusCode + " >> JSON: " + r1.json + " >> JSON Result: " + r1.json.getString("0001"));

            // 2. Create new registered member (non-error)
            Response r2 = sendRequest("POST", "http://127.0.0.1:5000/membership_api/0001", "0001=apple");
            System.out.println("#2 Code: " + r2.statusCode + " >> JSON: " + r2.json + " >> JSON Result: " + r2.json.getString("0001"));

            // 3. Read a registered member (non-error)
            Response r3 = sendRequest("GET", "http://127.0.0.1:5000/membership_api/0001", null);
            System.out.println("#3 Code: " + r3.statusCode + " >> JSON: " + r3.json + " >> JSON Result: " + r3.json.getString("0001"));

            // 4. Create already registered member (error case)
            Response r4 = sendRequest("POST", "http://127.0.0.1:5000/membership_api/0001", "0001=xpple");
            System.out.println("#4 Code: " + r4.statusCode + " >> JSON: " + r4.json + " >> JSON Result: " + r4.json.getString("0001"));

            // 5. Update a non registered member (error case)
            Response r5 = sendRequest("PUT", "http://127.0.0.1:5000/membership_api/0002", "0002=xrange");
            System.out.println("#5 Code: " + r5.statusCode + " >> JSON: " + r5.json + " >> JSON Result: " + r5.json.getString("0002"));

            // 6. Update a registered member (non-error)
            // First create member 0002
            sendRequest("POST", "http://127.0.0.1:5000/membership_api/0002", "0002=xrange");
            // Then update it
            Response r6 = sendRequest("PUT", "http://127.0.0.1:5000/membership_api/0002", "0002=orange");
            System.out.println("#6 Code: " + r6.statusCode + " >> JSON: " + r6.json + " >> JSON Result: " + r6.json.getString("0002"));

            // 7. Delete a registered member (non-error)
            Response r7 = sendRequest("DELETE", "http://127.0.0.1:5000/membership_api/0001", null);
            System.out.println("#7 Code: " + r7.statusCode + " >> JSON: " + r7.json + " >> JSON Result: " + r7.json.getString("0001"));

            // 8. Delete a non registered member (non-error)
            Response r8 = sendRequest("DELETE", "http://127.0.0.1:5000/membership_api/0001", null);
            System.out.println("#8 Code: " + r8.statusCode + " >> JSON: " + r8.json + " >> JSON Result: " + r8.json.getString("0001"));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("## REST Client completed.");
    }

    private static Response sendRequest(String method, String urlString, String data) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // If provided, send POST/PUT data
        if (data != null && (method.equals("POST") || method.equals("PUT"))) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        // Get response
        int statusCode = connection.getResponseCode();

        // Read response body
        Scanner scanner;
        if (statusCode >= 200 && statusCode < 300) {
            scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        } else {
            scanner = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8);
        }

        StringBuilder responseBody = new StringBuilder();
        while (scanner.hasNextLine()) {
            responseBody.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject json = new JSONObject(responseBody.toString()); // Parse JSON response

        connection.disconnect();
        return new Response(statusCode, json);
    }

    // to hold response data
    static class Response {
        int statusCode;
        JSONObject json;

        Response(int statusCode, JSONObject json) {
            this.statusCode = statusCode;
            this.json = json;
        }
    }
}
