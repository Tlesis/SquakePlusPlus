package org.tlesis.squakeplusplus.event;

import org.tlesis.squakeplusplus.config.FeatureToggle;

import fi.dy.masa.malilib.interfaces.IRenderer;

public class RenderHandler implements IRenderer {

    void foo() {
        if (FeatureToggle.SPEEDOMETER.getBooleanValue()) {

        }
    }
}