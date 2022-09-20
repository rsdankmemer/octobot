package org.octobot.bot.game.script.loader;

import org.octobot.bot.game.script.*;

/**
 * LocalScript
 *
 * @author Pat-ji
 */
public class LocalScript implements ScriptSource {
    private final Class<? extends GameScript> script;

    public LocalScript(final Class<? extends GameScript> script) {
        this.script = script;
    }

    @Override
    public GameScript getScript() {
        try {
            return script.asSubclass(GameScript.class).newInstance();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String name() {
        final ScriptManifest manifest;
        return script != null && (manifest = script.getAnnotation(ScriptManifest.class)) != null ? manifest.name() : null;
    }

    @Override
    public String description() {
        final ScriptManifest manifest;
        return script != null && (manifest = script.getAnnotation(ScriptManifest.class)) != null ? manifest.description() : null;
    }

    @Override
    public double version() {
        final ScriptManifest manifest;
        return script != null && (manifest = script.getAnnotation(ScriptManifest.class)) != null ? manifest.version() : -1;
    }

    @Override
    public String authors() {
        final ScriptManifest manifest;
        return script != null && (manifest = script.getAnnotation(ScriptManifest.class)) != null ? manifest.authors() : null;
    }

    @Override
    public ScriptCategory category() {
        final ScriptManifest manifest;
        return script != null && (manifest = script.getAnnotation(ScriptManifest.class)) != null ? manifest.category() : ScriptCategory.OTHER;
    }

}
