package org.octobot.script.collection;

import org.octobot.script.ScriptContext;

import java.util.*;

/**
 * Query
 *
 * @author Pat-ji
 */
@SuppressWarnings("unchecked")
public abstract class Query<T, E extends Query> implements Iterable<T> {
    private final ScriptContext context;

    protected final List<Filter<T>> filters;
    protected final List<Comparator<T>> comparators;

    public Query(final ScriptContext context) {
        this.context = context;

        filters = new ArrayList<Filter<T>>();
        comparators = new ArrayList<Comparator<T>>();
    }

    /**
     * This method is used by the {@link Query} to get required methods
     *
     * @return the {@link ScriptContext} for the {@link Query}
     */
    public ScriptContext context() {
        return context;
    }

    /**
     * This method is used to refresh the {@link Query}, removing all the {@link Filter}s and {@link java.util.Comparator}s
     *
     * @return the same {@link Query} with an empty {@link Filter} and {@link java.util.Comparator} {@link java.util.List}
     */
    public E refresh() {
        filters.clear();
        comparators.clear();
        return (E) this;
    }

    /**
     * This method is used to add a {@link Filter} to the {@link Query}
     *
     * @param filter the {@link Filter} to add
     * @return the same {@link Query} with an extra {@link Filter}
     */
    public E filter(final Filter<T> filter) {
        filters.add(filter);
        return (E) this;
    }

    /**
     * This method is used to add a {@link java.util.Comparator} to the {@link Query}
     *
     * @param comparator the {@link java.util.Comparator} to add
     * @return the same {@link Query} with an extra {@link java.util.Comparator}
     */
    public E compare(final Comparator<T> comparator) {
        comparators.add(comparator);
        return (E) this;
    }

    /**
     * This method is used to get all the types in this {@link Query}
     *
     * @return all the types in this {@link Query}
     */
    public abstract T[] all();

    /**
     * This method is used to get the first type in the {@link Query}
     *
     * @return the first type in the {@link Query}
     */
    public T first() {
        final T[] all = all();
        return all != null && all.length > 0 ? all[0] : null;
    }

    /**
     * This method is used to get a limited amount of types in the {@link Query}
     *
     * @param limit the max amount of types to get
     * @return a limited amount of types in the {@link Query}
     */
    public T[] limit(final int limit) {
        return limit(0, limit);
    }

    /**
     * This method is used to get a limited amount of types in the {@link Query}
     *
     * @param start the lowest index of the limitation
     * @param limit the highest index of the limitation
     * @return a limited amount of types in the {@link Query}
     */
    public T[] limit(final int start, final int limit) {
        final T[] all = all();
        if (all == null || start >= all.length) return null;

        final int max = Math.min(limit, all.length);
        final T[] result = Arrays.copyOf(all, max - start);
        System.arraycopy(all, start, result, start, max - start);
        return result;
    }

    /**
     * This method is used create a new {@link java.util.Iterator} for this {@link Query}
     *
     * @return a new {@link java.util.Iterator} for this {@link Query}
     */
    public Iterator<T> iterator() {
        final List<T> list = new ArrayList<T>();
        Collections.addAll(list, all());
        return list.iterator();
    }

}
