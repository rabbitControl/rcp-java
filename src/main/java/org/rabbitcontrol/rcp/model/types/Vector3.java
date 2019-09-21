package org.rabbitcontrol.rcp.model.types;



public class Vector3<T extends Number> extends VectorBase {

    private T x;

    private T y;

    private T z;


    public Vector3() {
    }

    public Vector3(T x, T y, T z) {

        this.x = x;
        this.y = y;
        this.z = z;
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

    public T getZ() {

        return z;
    }

    public void setZ(final T _z) {

        z = _z;
    }

    @Override
    public String toString() {

        return x + ", " + y + ", " + z;
    }

    @Override
    public boolean equals(final Object obj) {

        if ((obj instanceof Vector3)) {
            final Vector3<T> o = (Vector3<T>)obj;
            return (x.equals(o.getX())) && (y.equals(o.getY())) && (z.equals(o.getZ()));
        }

        return false;
    }
}

