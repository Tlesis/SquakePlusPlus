package tlesis.squakefabric.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import tlesis.squakefabric.Reference;
import tlesis.squakefabric.SquakeFabric;
import tlesis.squakefabric.gui.GuiConfigs.ConfigGuiTab;
import tlesis.squakefabric.scheduler.TaskScheduler;

public class DataManager {

    private static final Map<String, File> LAST_DIRECTORIES = new HashMap<>();

    private static ConfigGuiTab configGuiTab = ConfigGuiTab.GENERIC;
    private static boolean canSave;
    private static boolean isCarpetServer;

    private DataManager() {}

    public static File getCurrentConfigDirectory() {
        return new File(FileUtils.getConfigDirectory(), Reference.MOD_ID);
    }
    
    public static void load() {

        File file = getCurrentStorageFile(true);
        JsonElement element = JsonUtils.parseJsonFile(file);

        if (element != null && element.isJsonObject()) {
            LAST_DIRECTORIES.clear();

            JsonObject root = element.getAsJsonObject();

            if (JsonUtils.hasObject(root, "last_directories")) {
                JsonObject obj = root.get("last_directories").getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    String name = entry.getKey();
                    JsonElement el = entry.getValue();

                    if (el.isJsonPrimitive()) {
                        File dir = new File(el.getAsString());

                        if (dir.exists() && dir.isDirectory()) {
                            LAST_DIRECTORIES.put(name, dir);
                        }
                    }
                }
            }

            if (JsonUtils.hasString(root, "config_gui_tab")) {
                try {
                    configGuiTab = ConfigGuiTab.valueOf(root.get("config_gui_tab").getAsString());
                } catch (Exception e) {}

                if (configGuiTab == null) {
                    configGuiTab = ConfigGuiTab.GENERIC;
                }
            }
        }

        canSave = true;
    }

    public static void save() {
        save(false);
    }

    public static void save(boolean forceSave) {
        if (canSave == false && forceSave == false) {
            return;
        }

        JsonObject root = new JsonObject();
        JsonObject objDirs = new JsonObject();

        for (Map.Entry<String, File> entry : LAST_DIRECTORIES.entrySet()) {
            objDirs.add(entry.getKey(), new JsonPrimitive(entry.getValue().getAbsolutePath()));
        }

        root.add("last_directories", objDirs);
        
        root.add("config_gui_tab", new JsonPrimitive(configGuiTab.name()));

        File file = getCurrentStorageFile(true);
        JsonUtils.writeJsonToFile(root, file);

        canSave = false;
    }

    public static void clear() {
        TaskScheduler.getInstanceClient().clearTasks();

        setIsCarpetServer(false);
    }

    private static File getCurrentStorageFile(boolean globalData) {
        File dir = getCurrentConfigDirectory();

        if (dir.exists() == false && dir.mkdirs() == false) {
            SquakeFabric.logger.warn("Failed to create the config directory '{}'", dir.getAbsolutePath());
        }

        return new File(dir, StringUtils.getStorageFileName(globalData, Reference.MOD_ID + "_", ".json", "default"));
    }

    public static void setIsCarpetServer(boolean isCarpetServer) {
        DataManager.isCarpetServer = isCarpetServer;
    }

    public static boolean isCarpetServer() {
        return isCarpetServer;
    }

    public static ConfigGuiTab getConfigGuiTab() {
        return configGuiTab;
    }

    public static void setConfigGuiTab(ConfigGuiTab tab) {
        configGuiTab = tab;
    }
}
