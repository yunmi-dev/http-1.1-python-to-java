import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Prg04JsonExample {

    public static void main(String[] args) {

        String superHeroesString = """
                {
                  "squadName": "Super hero squad",
                  "homeTown": "Metro City",
                  "formed": 2016,
                  "secretBase": "Super tower",
                  "active": True,
                  "members": [
                    {
                      "name": "Molecule Man",
                      "age": 29,
                      "secretIdentity": "Dan Jukes",
                      "powers": [
                        "Radiation resistance",
                        "Turning tiny",
                        "Radiation blast"
                      ]
                    },
                    {
                      "name": "Madame Uppercut",
                      "age": 39,
                      "secretIdentity": "Jane Wilson",
                      "powers": [
                        "Million tonne punch",
                        "Damage resistance",
                        "Superhuman reflexes"
                      ]
                    },
                    {
                      "name": "Eternal Flame",
                      "age": 1000000,
                      "secretIdentity": "Unknown",
                      "powers": [
                        "Immortality",
                        "Heat Immunity",
                        "Inferno",
                        "Teleportation",
                        "Interdimensional travel"
                      ]
                    }
                  ]
                }
                """;

        JSONObject superHeroes = new JSONObject(superHeroesString);

        System.out.println(superHeroes.getString("homeTown"));
        System.out.println(superHeroes.getBoolean("active"));

        System.out.println(superHeroes.getJSONArray("members")
                .optJSONObject(1)
                .getJSONArray("powers")
                .getString(2));

        // Write to JSON file with tab indentation
        try (FileWriter fileWriter = new FileWriter("src/Prg04JsonExample.json")) {

            String jsonWithSpaces = superHeroes.toString(1);
            String[] lines = jsonWithSpaces.split("\n");
            StringBuilder result = new StringBuilder();

            for (String line : lines) {
                int leadingSpaces = 0;
                for (char c : line.toCharArray()) {
                    if (c == ' ') leadingSpaces++;
                    else break;
                }

                String tabs = "\t".repeat(leadingSpaces);
                String content = line.substring(leadingSpaces);
                result.append(tabs).append(content).append("\n");
            }

            fileWriter.write(result.toString());
            System.out.println("JSON file created successfully!");

        } catch (IOException e) {
            System.err.println("Writing JSON file error: " + e.getMessage());
        }
    }
}
