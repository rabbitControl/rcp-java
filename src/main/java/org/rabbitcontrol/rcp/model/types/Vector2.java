package org.rabbitcontrol.rcp.model.types;



public class Vector2<T extends Number> extends VectorBase {

    private T x;

    private T y;

    public Vector2() {
    }

    public Vector2(T x, T y) {

        this.x = x;
        this.y = y;
    }

    public T getX() {

        return x;
    }

    public void setX(final T _x) {

        x = _x;
    }

    public T getY() {

        return y;
    }

    public void setY(final T _y) {

        y = _y;
    }


    @Override
    public String toString() {

        return x + ", " + y;
    }

    @Override
    public boolean equals(final Object obj) {

        if ((obj instanceof Vector2)) {
            final Vector2<T> o = (Vector2<T>)obj;
            return (x.equals(o.getX())) && (y.equals(o.getY()));
        }

        return false;
    }
}

