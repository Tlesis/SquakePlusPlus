package org.tlesis.squakefabric.event;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;

import org.tlesis.squakefabric.Reference;
import org.tlesis.squakefabric.config.FeatureToggle;
import org.tlesis.squakefabric.config.Hotkeys;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler {
    
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler() {}

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {

        for (FeatureToggle toggle : FeatureToggle.VALUES) {
            manager.addKeybindToMap(toggle.getKeybind());
        }

        for (IHotkey hotkey : Hotkeys.HOTKEY_LIST) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(Reference.MOD_NAME, "squake.hotkeys.category.generic_hotkeys", Hotkeys.HOTKEY_LIST);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "squake.hotkeys.category.feature_toggle_hotkeys", FeatureToggle.VALUES);
    }
}
