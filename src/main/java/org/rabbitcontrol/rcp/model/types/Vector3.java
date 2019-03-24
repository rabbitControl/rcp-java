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
}

