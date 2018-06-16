package org.rabbitcontrol.rcp.model.types;

public class Range<T extends Number> {

    private T value1;
    private T value2;

    public Range(final T value1, final T value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T getValue1() {

        return value1;
    }

    public void setValue1(final T _value1) {

        value1 = _value1;
    }

    public T getValue2() {

        return value2;
    }

    public void setValue2(final T _value2) {

        value2 = _value2;
    }
}
