package org.octobot.bot.internal.ui.component;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.Map;

/**
 * ClientStub
 *
 * @author Pat-ji
 */
public class ClientStub implements AppletStub {
    private final URL base;
    private final Map<String, String> parameters;

    public ClientStub(final URL base, final Map<String, String> parameters) {
        this.base = base;
        this.parameters = parameters;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return base;
    }

    @Override
    public URL getCodeBase() {
        return base;
    }

    @Override
    public String getParameter(final String name) {
        return parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(final int width, final int height) {
    }

}
