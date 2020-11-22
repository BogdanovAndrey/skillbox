package Model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DataGrabber {
    public static JsonElement getDataFromServer(String adr) throws IOException {
        URL url = new URL(adr);
        URLConnection request = url.openConnection();
        request.connect();
        Gson gson = new GsonBuilder().setLenient().create();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson

        return jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
    }
}
