import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Prg03JsonExample {

    public static void main(String[] args) {

        try {

            // Open file in read mode
            String jsonContent = Files.readString(Paths.get("src/Prg03JsonExample.json"));

            // Load JSON format
            JSONObject superHeroes = new JSONObject(jsonContent);   // JSON Deserialization

            System.out.println(superHeroes.getString("homeTown"));
            System.out.println(superHeroes.getBoolean("active"));

            JSONArray members = superHeroes.getJSONArray("members");
            JSONObject member = members.getJSONObject(1);
            JSONArray powers = member.getJSONArray("powers");
            System.out.println(powers.get(2));

        } catch (IOException e) {
            System.err.println("File reading error: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
    }
}
