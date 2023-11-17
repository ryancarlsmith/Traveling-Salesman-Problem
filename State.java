public class State {

    private final String name;
    private final String code;
    private City capital;

    public State(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String name() { return this.name; }
    public String code() { return this.code; }
    public City capital() { return this.capital; }

    public void capital(City capital) {
        this.capital = capital;
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(State other) {
        return this.name.equals(other.name);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof State && this.equals((State) other);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
