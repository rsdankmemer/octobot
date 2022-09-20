package org.octobot.script.collection;

/**
 * Filter
 *
 * @author Pat-ji
 */
public interface Filter<T> {

    /**
     * This method is used to check acceptation for the {@link Filter}
     *
     * @param t the type to use as the filter
     * @return <code>true</code> if the {@link Filter} accepts the type
     */
    public boolean accept(final T t);

}
