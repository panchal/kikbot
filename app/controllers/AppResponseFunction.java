package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.ws.WSResponse;

import java.util.function.Function;

/**
 * @author <a href="mailto:panchal@yahoo-inc.com">Deven Panchal</a>
 */
public class AppResponseFunction implements Function<WSResponse, JsonNode> {

    @Override
    public JsonNode apply(WSResponse wsResponse) {
        return wsResponse.asJson();
    }

}
