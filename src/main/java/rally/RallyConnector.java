package rally;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.*;
import com.rallydev.rest.response.*;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Brian on 1/26/2017.
 */
public class RallyConnector {

    public static RallyRestApi createConnection (String url, String key){
        RallyRestApi rallyRestApi = null;
        try {
            URI uri = new URI(url);
            rallyRestApi = new RallyRestApi(uri, key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rallyRestApi;
    }

    public static void main(String[] args) {

    }
}
