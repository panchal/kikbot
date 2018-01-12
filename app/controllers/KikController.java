package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.ws.WSClient;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class KikController extends Controller {

    private static final String GREETINGS_FILE = "resources/greetings.txt";

    private List<String> greetings;

    @Inject
    WSClient ws;

    public KikController() {
        ClassLoader classLoader = getClass().getClassLoader();
        greetings = loadResourceAsList(classLoader, GREETINGS_FILE);
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result incoming() {
        JsonNode json = request().body().asJson();
        JsonNode messages = json.findPath("messages");
        String from = messages.get(0).findPath("from").asText();
        String body = messages.get(0).findPath("body").asText();
        String chatId = messages.get(0).findPath("chatId").asText();
        Logger.info("Incoming Request: " + request().host(), request().uri());
        Logger.info(from + ": " + body);
        KikClient.postTextMessage(ws, from, chatId, greetings);
        return ok("Kikbot entry portal.");
    }

    public static List<String> loadResourceAsList(ClassLoader classLoader, String resourceName) {
        List<String> list = new ArrayList<>();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Error loading resource.");
        }
    }

}
