package org.tlesis.squakefabric.gui;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.BooleanHotkeyGuiWrapper;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import org.tlesis.squakefabric.Reference;
import org.tlesis.squakefabric.config.Configs;
import org.tlesis.squakefabric.config.FeatureToggle;
import org.tlesis.squakefabric.data.DataManager;

public class GuiConfigs extends GuiConfigsBase {

    public static ImmutableList<FeatureToggle> TOGGLE_LIST = FeatureToggle.VALUES;

    public GuiConfigs() {
        super(10, 50, Reference.MOD_ID, null, "squake.gui.title.configs", String.format("%s", Reference.MOD_VERSION));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        x += this.createButton(x, y, -1, ConfigGuiTab.FEATURE_TOGGLE);
        x += this.createButton(x, y, -1, ConfigGuiTab.OPTIONS);
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(DataManager.getConfigGuiTab() != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth() {
        ConfigGuiTab tab = DataManager.getConfigGuiTab();

        if (tab == ConfigGuiTab.OPTIONS) {
            return 140;
        }

        return 260;
    }

    @Override
    protected boolean useKeybindSearch() {
        return DataManager.getConfigGuiTab() == ConfigGuiTab.FEATURE_TOGGLE || 
               DataManager.getConfigGuiTab() == ConfigGuiTab.OPTIONS;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = DataManager.getConfigGuiTab();

        if (tab == ConfigGuiTab.OPTIONS) {
            configs = Configs.Options.OPTIONS;

        } else if(tab == ConfigGuiTab.FEATURE_TOGGLE) {
            return ConfigOptionWrapper.createFor(TOGGLE_LIST.stream().map(this::wrapConfig).toList());
 
        } else {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    protected BooleanHotkeyGuiWrapper wrapConfig(FeatureToggle config)
    {
        return new BooleanHotkeyGuiWrapper(config.getName(), config, config.getKeybind());
    }

    @Override
    protected void onSettingsChanged() {
        super.onSettingsChanged();

        System.out.println("Settings changed");
    }

    private static class ButtonListener implements IButtonActionListener {
        private final GuiConfigs parent;
        private final ConfigGuiTab tab;

        public ButtonListener(ConfigGuiTab tab, GuiConfigs parent) {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            DataManager.setConfigGuiTab(this.tab);

            this.parent.initGui();
        }
    }

    public enum ConfigGuiTab {
        OPTIONS         ("squake.gui.button.config_gui.options"),
        FEATURE_TOGGLE  ("squake.gui.button.config_gui.feature_toggle");

        private final String translationKey;

        private ConfigGuiTab(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getDisplayName() {
            return StringUtils.translate(this.translationKey);
        }
    }
}
