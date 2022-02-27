package org.tlesis.squakefabric.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import org.tlesis.squakefabric.Reference;
import org.tlesis.squakefabric.SquakeFabric;
import org.tlesis.squakefabric.gui.GuiConfigs.ConfigGuiTab;
import org.tlesis.squakefabric.scheduler.TaskScheduler;

public class DataManager {

    private static final Map<String, File> LAST_DIRECTORIES = new HashMap<>();

    private static ConfigGuiTab configGuiTab = ConfigGuiTab.FEATURE_TOGGLE;
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
                    configGuiTab = ConfigGuiTab.FEATURE_TOGGLE;
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

    public static final ArrayList<Item> VANILLA_FOOD = new ArrayList<>(Arrays.asList(
            Items.APPLE, Items.BAKED_POTATO, Items.BEEF, Items.BEETROOT, Items.CARROT,
            Items.CHICKEN, Items.BREAD, Items.CHORUS_FRUIT, Items.COD, Items.COOKED_BEEF,
            Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT,
            Items.COOKED_SALMON, Items.COOKIE, Items.DRIED_KELP, Items.GLOW_BERRIES, Items.GOLDEN_APPLE, Items.GOLDEN_CARROT,
            Items.HONEY_BOTTLE, Items.MELON_SLICE, Items.MILK_BUCKET, Items.MUSHROOM_STEW, Items.MUTTON, Items.POISONOUS_POTATO,
            Items.PORKCHOP, Items.POTATO, Items.PUMPKIN_PIE, Items.RABBIT, Items.RABBIT_STEW, Items.BEETROOT_SOUP,
            Items.ROTTEN_FLESH, Items.SALMON, Items.SPIDER_EYE, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.TROPICAL_FISH,
            Items.ENCHANTED_GOLDEN_APPLE
    ));
}
