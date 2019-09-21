package org.rabbitcontrol.rcp.model.types;



public class Vector4<T extends Number> extends VectorBase {

    private T x;

    private T y;

    private T z;

    private T t;


    public Vector4() {
    }

    public Vector4(T x, T y, T z, T t) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
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

    public T getT() {

        return t;
    }

    public void setT(final T _t) {

        t = _t;
    }

    @Override
    public String toString() {

        return x + ", " + y + ", " + z + ", " + t;
    }

    @Override
    public boolean equals(final Object obj) {

        if ((obj instanceof Vector4)) {
            final Vector4<T> o = (Vector4<T>)obj;
            return (x.equals(o.getX())) && (y.equals(o.getY())) && (z.equals(o.getZ())) && (t.equals(o.getT()));
        }

        return false;
    }
}

