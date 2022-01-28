package omen44.omens_economy.utils;

import omen44.omens_economy.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonSaver {
    private FileWriter file;
    public Plugin plugin = Main.getPlugin(Main.class);


    public void init(String fileName) {
        try {
            file = new FileWriter(plugin.getDataFolder().getAbsolutePath() + File.separator + fileName + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *
     * @param obj the Json object to save
     * @param fileName the file name to add without the file extension
     */
    public void saveData(JSONObject obj, String fileName) {
        try {
            file = new FileWriter(plugin.getDataFolder().getAbsolutePath() + File.separator + fileName + ".json");
            file.write(obj.toJSONString());
        } catch (IOException e) {
            Bukkit.getLogger().warning("Unable to save data to JSON File");
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Unable to save data to JSON File");
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void setData(Object key, Object value, String fileName) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        saveData(obj, fileName);
    }

    public String getString(String key, String fileName) {
        JSONParser jsonParser = new JSONParser();

        try {
            FileReader reader = new FileReader(
                    plugin.getDataFolder().getAbsolutePath() + File.separator + fileName + ".json");

            //Read JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            if (jsonObject.containsKey(key)) {
                return jsonObject.get(key).toString();
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileWriter getFile() {
        return file;
    }
}
