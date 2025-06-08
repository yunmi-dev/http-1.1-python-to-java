import org.json.JSONObject;

public class Prg05JsonExample {

    public static void main(String[] args) {

        // JSON Serialization

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

        String superHeroesToString = superHeroes.toString(4);
        System.out.println(superHeroesToString);
    }
}
