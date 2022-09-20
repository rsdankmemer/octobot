package org.octobot.script.wrappers;

import java.lang.ref.WeakReference;

/**
 * Wrapper
 *
 * @author Pat-ji
 */
public abstract class Wrapper<T> {
    private final WeakReference<T> reference;

    public Wrapper(final T accessor) {
        this.reference = new WeakReference<T>(accessor);
    }

    /**
     * This method is used to get {@link Wrapper}s accessor
     *
     * @return the {@link Wrapper}s accesor
     */
    public T getAccessor() {
        return reference.get();
    }

}
