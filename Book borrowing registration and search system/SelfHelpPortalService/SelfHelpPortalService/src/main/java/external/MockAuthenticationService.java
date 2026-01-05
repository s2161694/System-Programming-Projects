package external;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A mock Authentication Service Provider implementation for testing.
 * This does not communicate with any real authentication API, just uses local user data to simulate one.
 *
 * To better understand code using the json-simple library, see the following links:
 * https://attacomsian.com/blog/what-is-json
 * https://www.digitalocean.com/community/tutorials/json-simple-example
 * https://stackoverflow.com/questions/17351043/how-to-get-absolute-path-to-file-in-resources-folder-of-your-project
 * https://stackoverflow.com/questions/10926353/how-to-read-json-file-into-java-with-simple-json-library
 */
public class MockAuthenticationService implements AuthenticationService {
    /**
     * Mapping of user emails to user data (as JSON objects)
     */
    private final Map<String, JSONObject> users = new HashMap<>();

    /**
     * Load data about all users from a JSON file into memory
     * @throws URISyntaxException occurs if the file URI is invalid
     * @throws IOException occurs if the file cannot be opened
     * @throws ParseException occurs if the file contains invalid JSON
     * @throws NullPointerException occurs if the file does not exist
     */
    public MockAuthenticationService() throws URISyntaxException, IOException, ParseException, NullPointerException {
        URL dataPath = getClass().getResource("/MockUserDataGroups4.json");
        Objects.requireNonNull(dataPath);
        File dataFile = Paths.get(dataPath.toURI()).toFile();

        JSONParser parser = new JSONParser();
        JSONArray userDataArray = (JSONArray) parser.parse(new FileReader(dataFile));
        for (Object userData: userDataArray) {
            JSONObject user = (JSONObject) userData;
            String username = (String) user.get("username");
            users.put(username, user);
        }
    }

    @Override
    public String login(String email, String password) {
        JSONObject user = users.get(email);
        if (user == null) {
            // This means the email address is not registered, but for security,
            // Error message deliberately does not say it's the email that's wrong
            HashMap<String, String> errorObj = new HashMap<>();
            errorObj.put("error", "Wrong username or password");
            return new JSONObject(errorObj).toJSONString();
        }

        String registeredPassword = (String) user.get("password");
        if (!password.equals(registeredPassword)) {
            // For security, passwords should never be transferred or stored in plaintext
            // Instead, they should be hashed using a secure algorithm.
            // For this exercise, we use plaintext passwords for simplicity, but don't ever do this in production code!
            HashMap<String, String> errorObj = new HashMap<>();
            errorObj.put("error", "Wrong username or password");
            return new JSONObject(errorObj).toJSONString();
        }

        return user.toJSONString();
    }
}
