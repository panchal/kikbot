package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

import java.util.concurrent.CompletionStage;

/**
 * @author <a href="mailto:panchal@yahoo-inc.com">Deven Panchal</a>
 */
public class QuotesClient {

    private static final String QUOTES_SCHEME = "http";
    private static final String QUOTES_SERVER = "quotesondesign.com";

    public static CompletionStage<JsonNode> getQuote(WSClient ws) {
        String quotesPath = "/wp-json/posts";
        String quotesUrl = getUrl(QUOTES_SCHEME, QUOTES_SERVER, quotesPath);
        WSRequest quotesRequest =  ws
                .url(quotesUrl)
                .setQueryString("filter[orderby]=rand&filter[posts_per_page]=1");
        CompletionStage<JsonNode> quotesPromise = quotesRequest.get().thenApply(new AppResponseFunction());
        return quotesPromise;
    }

    public static String getUrl(String scheme, String server, String path) {
        return scheme + "://" + server + path;
    }

}
