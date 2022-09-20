package org.octobot.script;

/**
 * ContextProvider
 *
 * @author Pat-ji
 */
public class ContextProvider {
    private final ScriptContext context;

    public ContextProvider(final ScriptContext context) {
        this.context = context;
    }

    /**
     * This method is used to get the {@link ScriptContext} from the {@link ContextProvider}
     *
     * @return the {@link ScriptContext}
     */
    public ScriptContext context() {
        return context;
    }

}
