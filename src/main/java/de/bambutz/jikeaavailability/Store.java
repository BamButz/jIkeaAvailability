package de.bambutz.jikeaavailability;

import java.util.Objects;

public final class Store {
    private final String buCode;
    private final String name;
    private final Coordinate coordinates;
    private final String countryCode;

    public Store(String buCode, String name, Coordinate coordinates, String countryCode) {
        this.buCode = buCode;
        this.name = name;
        this.coordinates = coordinates;
        this.countryCode = countryCode;
    }

    public String buCode() {
        return buCode;
    }

    public String name() {
        return name;
    }

    public Coordinate coordinates() {
        return coordinates;
    }

    public String countryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Store) obj;
        return Objects.equals(this.buCode, that.buCode) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.coordinates, that.coordinates) &&
                Objects.equals(this.countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buCode, name, coordinates, countryCode);
    }

    @Override
    public String toString() {
        return "Store[" +
                "buCode=" + buCode + ", " +
                "name=" + name + ", " +
                "coordinates=" + coordinates + ", " +
                "countryCode=" + countryCode + ']';
    }

}
