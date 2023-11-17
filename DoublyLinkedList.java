import java.util.Iterator;

public class DoublyLinkedList<T> implements List<T> {

    public static class EmptyListException extends RuntimeException {
        public EmptyListException() {
            super();
        }
    }

    private class Node {

        private Node next;
        private Node prev;
        private T    data;

        private Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public DoublyLinkedList(T item) {
        this();
        this.append(item);
    }

    public DoublyLinkedList(T[] items) {
        this();
        for (T item : items) {
            this.append(item);
        }
    }

    public DoublyLinkedList(List<T> items) {
        this();
        for (T item : items) {
            this.append(item);
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean contains(T item) {
        Node rover = this.head;
        while (rover != null) {
            if (equal(rover.data, item)) return true;
            rover = rover.next;
        }
        return false;
    }

    public int occurrences(T item) {
        int count = 0;
        Node rover = this.head;
        while (rover != null) {
            if (equal(rover.data, item)) count++;
            rover = rover.next;
        }
        return count;
    }

    public Node locate(T item) {
        Node rover = this.head;
        while (rover != null) {
            if (equal(rover.data, item)) return rover;
            rover = rover.next;
        }
        return null;
    }

    private void checkIndexBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    public T get(int index) {
        checkIndexBounds(index);
        Node rover = this.head;
        while (index-- > 0) {
            rover = rover.next;
        }
        return rover.data;
    }

    public void set(int index, T value) {
        checkIndexBounds(index);
        Node rover = this.head;
        while (index-- > 0) {
            rover = rover.next;
        }
        rover.data = value;
    }

    public void prepend(T item) {
        Node node = new Node(item);
        node.next = this.head;
        node.prev = null;
        if (this.head != null) {
            this.head.prev = node;
        } else {
            this.tail = node;
        }
        this.head = node;
        this.size++;
    }

    public void append(T item) {
        Node node = new Node(item);
        node.next = null;
        node.prev = this.tail;
        if (this.tail != null) {
            this.tail.next = node;
        } else {
            this.head = node;
        }
        this.tail = node;
        this.size++;
    }

    private Node predecessor(Node node) {
        Node rover = this.head;
        while (rover != null) {
            if (rover.next == node) return rover;
            rover = rover.next;
        }
        return null;
    }

    public void insertAfter(Node position, T item) {
        Node node = new Node(item);
        Node predecessor = position;
        Node successor;

        if (position != null) {
            successor = position.next;
        } else {
            successor = this.head;
        }

        if (successor != null) {
            successor.prev = node;
        } else {
            this.tail = node;
        }

        if (predecessor != null) {
            predecessor.next = node;
        } else {
            this.head = node;
        }

        node.next = successor;
        node.prev = predecessor;
        this.size++;
    }

    public void insertBefore(Node position, T item) {
        Node node = new Node(item);
        Node successor = position;
        Node predecessor;

        if (position != null) {
            predecessor = position.prev;
        } else {
            predecessor = this.tail;
        }

        if (successor != null) {
            successor.prev = node;
        } else {
            this.tail = node;
        }

        if (predecessor != null) {
            predecessor.next = node;
        } else {
            this.head = node;
        }

        node.next = successor;
        node.prev = predecessor;
        this.size++;
    }

    public T remove(Node node) {
        Node successor = node.next;
        Node predecessor = node.prev;
        T result = node.data;

        if (predecessor != null) {
            predecessor.next = node.next;
        } else {
            this.head = node.next;
        }

        if (successor != null) {
            successor.prev = node.prev;
        } else {
            this.tail = node.prev;
        }

        node.next = null;
        node.prev = null;
        this.size--;
        return result;
    }

    public void removeAll(T item) {
        Node rover = this.head;
        Node previous = null;
        while (rover != null) {
            Node next = rover.next;
            if (equal(rover.data, item)) {
                if (previous != null) {
                    previous.next = next;
                } else {
                    this.head = next;
                }
                if (next != null) {
                    next.prev = previous;
                } else {
                    this.tail = previous;
                }
                rover.next = null;
                this.size--;
            } else {
                previous = rover;
            }
            rover = next;
        }
    }

    public T head() {
        if (this.size > 0) {
            return this.head.data;
        } else {
            throw new EmptyListException();
        }
    }

    public T tail() {
        if (this.size > 0) {
            return this.tail.data;
        } else {
            throw new EmptyListException();
        }
    }

    public T removeHead() {
        if (this.size == 0) {
            throw new EmptyListException();
        }

        T result = this.head.data;
        this.head = this.head.next;
        this.size--;
        if (this.size == 0) {
            this.tail = null;
        }
        return result;
    }

    public T removeTail() {
        if (this.size == 0) {
            throw new EmptyListException();
        }

        T result = this.tail.data;
        this.tail = this.tail.prev;
        if (this.tail != null) {
            this.tail.next = null;
        } else {
            this.head = null;
        }
        this.size--;
        return result;
    }

    public void reverse() {
        Node rover = this.head;
        this.head = this.tail;
        this.tail = rover;

        while (rover != null) {
            Node next = rover.next;
            rover.next = rover.prev;
            rover.prev = next;
            rover = next;
        }
    }

    private boolean equal(T x, T y) {
        if (x == null) {
            return y == null;
        } else {
            return x.equals(y);
        }
    }

    public boolean equals(DoublyLinkedList other) {
        if (other == null) return false;
        if (this.size != other.size) return false;

        Node rover1 = this.head;
        Node rover2 = other.head;

        while (rover1 != null && rover2 != null) {
            if ( ! equal(rover1.data, rover2.data)) return false;
            rover1 = rover1.next;
            rover2 = rover2.next;
        }
        return rover1 == rover2; // same length?
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DoublyLinkedList && this.equals((DoublyLinkedList) other);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        Node rover = this.head;
        while (rover != null) {
            if (rover.data != null) {
                hash = 751 * hash + rover.data.hashCode();
            }
            rover = rover.next;
        }
        return hash;
    }

    @Override
    public String toString() {
        String separator = "";
        String result = "[";
        Node rover = this.head;
        while (rover != null) {
            result += separator;
            result += rover.data;
            separator = ", ";
            rover = rover.next;
        }
        result += "]";
        return result;
    }


    public static enum Direction {FORWARD, REVERSE};

    public Iterator<T> iterator(Direction direction) {
        return this.new ListIterator(direction);
    }

    public Iterator<T> iterator() {
        return this.new ListIterator(Direction.FORWARD);
    }

    public Iterator<T> reverseIterator() {
        return this.new ListIterator(Direction.REVERSE);
    }

    public Iterable<T> reversal() {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return DoublyLinkedList.this.reverseIterator();
            }
        };
    }


    private class ListIterator implements Iterator<T> {

        private Node rover;
        private boolean forward;

        public ListIterator(Direction direction) {
            switch (direction) {
                case FORWARD:
                    this.rover = DoublyLinkedList.this.head;
                    this.forward = true;
                    break;

                case REVERSE:
                    this.rover = DoublyLinkedList.this.tail;
                    this.forward = false;
                    break;

                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public boolean hasNext() {
            return this.rover != null;
        }

        @Override
        public T next() {
            T result = this.rover.data;
            this.rover = this.forward ? this.rover.next : this.rover.prev;
            return result;
        }
    }
}
