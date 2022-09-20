package org.octobot.bot.game.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ScriptManifest
 *
 * @author Pat-ji
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

    /**
     * This method is used to get the {@link ScriptManifest}s name
     *
     * @return the {@link ScriptManifest}s name
     */
    public String name();

    /**
     * This method is used to get the {@link ScriptManifest}s description
     *
     * @return the {@link ScriptManifest}s description
     */
    public String description();

    /**
     * This method is used to get the {@link ScriptManifest}s version
     *
     * @return the {@link ScriptManifest}s version
     */
    public double version();

    /**
     * This method is used to get the {@link ScriptManifest}s authors
     *
     * @return the {@link ScriptManifest}s authors
     */
    public String authors();

    /**
     * This method is used to get the {@link ScriptManifest}s {@link ScriptCategory}
     *
     * @return the {@link ScriptManifest}s {@link ScriptCategory}
     */
    public ScriptCategory category() default ScriptCategory.OTHER;

    /**
     * This method is used to get the max instances of the {@link GameScript} linked to this {@link ScriptManifest}
     *
     * @return the max instances of the {@link GameScript} linked to this {@link ScriptManifest}
     */
    public int maxInstances() default Integer.MAX_VALUE;

    /**
     * This method is used to check if the {@link GameScript} linked to this {@link ScriptManifest} is vip-only
     *
     * @return <code>true</code> if the {@link GameScript} linked to this {@link ScriptManifest} is vip-only
     */
    public boolean vip() default false;

}