import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Prg02HttpWebClient {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("## HTTP client started.");
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // GET request & print response
        System.out.println("## GET request for http://localhost:8080/temp/");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/temp/"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("## GET response [start]");
        System.out.println(httpResponse.body());
        System.out.println("## GET response [end]");

        // GET request & print response
        System.out.println("## GET request for http://localhost:8080/?var1=9&var2=9");
        httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/?var1=9&var2=9"))
                .GET()
                .build();
        httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("## GET response [start]");
        System.out.println(httpResponse.body());
        System.out.println("## GET response [end]");

        // POST request & print response
        System.out.println("## POST request for http://localhost:8080/ with var1 is 9 and var2 is 9");
        String postData = "var1=9&var2=9" ;

        httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/"))
                .header("Content-Type", "application/x-www-form-urlencoded") // Form data-type
                .POST(HttpRequest.BodyPublishers.ofString(postData)) // Setting POST data
                .build();
        httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()); // same

        System.out.println("## POST response [start]");
        System.out.println(httpResponse.body());
        System.out.println("## POST response [end]");

        System.out.println("## HTTP client completed.");
    }
}
