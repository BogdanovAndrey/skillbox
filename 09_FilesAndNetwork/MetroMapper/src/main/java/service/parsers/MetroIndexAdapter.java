package service.parsers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import service.LineColor;
import service.MetroIndex;
import service.MetroLine;

import java.lang.reflect.Type;
import java.util.*;

public class MetroIndexAdapter implements JsonSerializer<MetroIndex>, JsonDeserializer<MetroIndex> {
    @Override
    public JsonElement serialize(MetroIndex metroIndex, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject finalObj = new JsonObject();

        finalObj.add("stations", serializeStations(metroIndex.getLines()));
        finalObj.add("lines", serializeLines(metroIndex.getLines()));
        finalObj.add("connections", serializeConnections(metroIndex.getConnections()));

        return finalObj;
    }

    private JsonElement serializeLines(TreeMap<String, MetroLine> lines) {
        JsonArray jsonLines = new JsonArray();
        lines.forEach((s, metroLine) -> {
            JsonObject entry = new JsonObject();
            entry.addProperty("number", s);
            entry.addProperty("name", metroLine.getName());
            entry.addProperty("color", metroLine.getColor());
            jsonLines.add(entry);
        });
        return jsonLines;
    }

    private JsonElement serializeStations(TreeMap<String, MetroLine> lines) {
        JsonObject jsonStations = new JsonObject();
        lines.forEach((s, metroLine) -> {
            JsonArray stations = new JsonArray();
            metroLine.getStations().forEach(stations::add);
            jsonStations.add(s, stations);
        });
        return jsonStations;
    }

    private JsonArray serializeConnections(HashSet<Map<String, String>> connections) {
        JsonArray jsonConnections = new JsonArray();
        connections.forEach(map -> {
            JsonArray curConnection = new JsonArray();
            map.forEach((key, value) -> {
                JsonObject entry = new JsonObject();
                entry.addProperty("line", key);
                entry.addProperty("station", value);
                curConnection.add(entry);
            });
            jsonConnections.add(curConnection);
        });
        return jsonConnections;
    }


    @Override
    public MetroIndex deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        MetroIndex index = new MetroIndex();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        index.setConnections(deserializeConnections(jsonObject.getAsJsonArray("connections")));
        index.addLines(deserializeLines(jsonObject.getAsJsonArray("lines")));
        Gson gson = new Gson();
        Type collectionType = new TypeToken<LinkedHashSet<String>>() {
        }.getType();

        jsonObject.getAsJsonObject("stations").entrySet().forEach(entry -> {
            LinkedHashSet<String> stations = gson.fromJson(entry.getValue(), collectionType);
            index.getLines().get(entry.getKey()).setStations(stations);
        });

        return index;
    }

    private HashSet<Map<String, String>> deserializeConnections(JsonArray jsonArray) {
        HashSet<Map<String, String>> set = new HashSet<>();
        jsonArray.forEach(array -> {
            JsonArray context = array.getAsJsonArray();
            HashMap<String, String> entry = new HashMap<>();
            context.forEach(jsonElement -> {
                JsonObject obj = jsonElement.getAsJsonObject();
                entry.put(obj.get("line").getAsString(), obj.get("station").getAsString());
            });
            set.add(entry);
        });
        return set;
    }

    private TreeMap<String, MetroLine> deserializeLines(JsonArray jsonLines) {
        TreeMap<String, MetroLine> lines = new TreeMap<>();
        jsonLines.forEach(jsonElement -> {
            JsonObject lineInfo = jsonElement.getAsJsonObject();
            lines.put(lineInfo.get("number").getAsString(),
                    new MetroLine(
                            lineInfo.get("name").getAsString(),
                            LineColor.getColorByLine(lineInfo.get("number").getAsString())
                    ));
        });
        return lines;
    }

}
