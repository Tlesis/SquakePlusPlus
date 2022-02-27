package org.tlesis.squakeplusplus.event;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;

import org.tlesis.squakeplusplus.Reference;
import org.tlesis.squakeplusplus.config.FeatureToggle;
import org.tlesis.squakeplusplus.config.Configs.Options;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler {
    
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler() {
        super();
    }

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {

        for (FeatureToggle toggle : FeatureToggle.values()) {
            manager.addKeybindToMap(toggle.getKeybind());
        }

        for (IHotkey hotkey : Options.HOTKEY_LIST) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(Reference.MOD_NAME, "squake.hotkeys.category.generic_hotkeys", Options.HOTKEY_LIST);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "squake.hotkeys.category.feature_toggle_hotkeys", ImmutableList.copyOf(FeatureToggle.values()));
    }
}
