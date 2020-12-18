package com.av.gltrans.gles.graphics;

import com.av.gltrans.gles.annotation.GlContext;

public abstract class InputValue<T> {

    protected String name;

    public InputValue(String name) {
        this.name = name;
    }

    protected T cachedValue = null;
    protected int program = -1;
    protected int _location = -1;

    protected abstract int loadLocation();

    @GlContext
    public void initialize(int p) throws RuntimeException {
        if (p == -1)
            throw new RuntimeException("Invalid program");
        this.program = p;

        _location = loadLocation();
        rationalChecks();
    }

    protected abstract void rationalChecks() throws RuntimeException;

    @GlContext
    public abstract void setValue(T value);

    @GlContext
    public abstract T getValue();

    public int getLocation() throws RuntimeException {
        rationalChecks();
        return _location;
    }

    public T cachedValue() {
        if (cachedValue != null) {
            return cachedValue;
        } else {
            throw new RuntimeException("cachedValue was null");
        }
    }
}
