package com.localproj.nodes;

public class DataNode<T> {
    private String key;
    private T value;

    public DataNode(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}
