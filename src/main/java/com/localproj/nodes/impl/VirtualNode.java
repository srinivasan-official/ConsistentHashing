package com.localproj.nodes.impl;

import com.localproj.nodes.Node;

public class VirtualNode<T extends Node> implements Node {

    private String key;
    private T physicalNode;
    private Integer replicaIdx;
    private Long hash;

    public VirtualNode(T physicalNode, Integer replicaIdx, String key, Long hash) {
        this.physicalNode = physicalNode;
        this.replicaIdx = replicaIdx;
        this.key = key;
        this.hash = hash;
    }

    public T getPhysicalNode() {
        return physicalNode;
    }

    public Integer getReplicaIdx() {
        return replicaIdx;
    }

    public Long getHash() {
        return hash;
    }

    @Override
    public String getKey() {
        return key;
    }
}
