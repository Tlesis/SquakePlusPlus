package org.tlesis.squakeplusplus.util;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum ScreenPositions implements IConfigOptionListEntry {
    
    TOP_RIGHT   ("Top Right",    "squake.label.screenpositions.topright"),
    TOP_LEFT    ("Top Left",     "squake.label.screenpositions.topleft"),
    BOTTOM_RIGHT("Bottom Right", "squake.label.screenpositions.bottomright"),
    BOTTOM_LEFT ("Bottom Left",  "squake.label.screenpositions.bottomleft"),
    CENTER      ("Center",       "squake.label.screenpositions.center");

    private final String configString;
    private final String unlocName;

    private ScreenPositions(String configString, String unlocName) {
        this.configString = configString;
        this.unlocName = unlocName;
    }

    @Override
    public String getStringValue() {
        return this.configString;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(this.unlocName);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();

        if (forward) {
            if (++id >= values().length) {
                id = 0;
            }
        } else {
            if (--id < 0) {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public ScreenPositions fromString(String name)
    {
        return fromStringStatic(name);
    }

    public static ScreenPositions fromStringStatic(String name)
    {
        for (ScreenPositions aligment : ScreenPositions.values())
        {
            if (aligment.configString.equalsIgnoreCase(name))
            {
                return aligment;
            }
        }

        return ScreenPositions.TOP_LEFT;
    }
}
