public interface List<T> extends Iterable<T> {

    public int size();
    default public boolean isEmpty() {
        return this.size() == 0;
    }

    default public boolean contains(T item) {
        return occurrences(item) > 0;
    }
    public int occurrences(T item);

    public void prepend(T item);
    public void append(T item);
    public void removeAll(T item);

    public T head();
    public T tail();

    public T removeHead();
    public T removeTail();
}
