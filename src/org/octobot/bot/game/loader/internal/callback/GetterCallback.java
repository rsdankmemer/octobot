package org.octobot.bot.game.loader.internal.callback;

/**
 * GetterCallback
 *
 * @author Pat-ji
 */
public class GetterCallback {
    public String owner, fieldName, methodName, methodDesc, desc;
    public Object multiplier;

    public GetterCallback(final String owner, final String fieldName, final String methodName, final String desc, final String methodDesc) {
        this.owner = owner;
        this.fieldName = fieldName;
        this.methodName = methodName;
        this.desc = desc;
        this.methodDesc = methodDesc;
    }

}
