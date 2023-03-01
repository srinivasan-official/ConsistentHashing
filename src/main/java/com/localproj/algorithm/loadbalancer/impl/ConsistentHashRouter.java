package com.localproj.algorithm.loadbalancer.impl;

import com.localproj.algorithm.loadbalancer.LoadBalancer;
import com.localproj.hash.HashFunction;
import com.localproj.nodes.impl.ServerNode;
import com.localproj.nodes.impl.VirtualNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsistentHashRouter implements LoadBalancer {
    private TreeMap<Long, VirtualNode<ServerNode>> hashRing;
    private HashFunction hashFunction;

    public ConsistentHashRouter(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
        this.hashRing = new TreeMap<>();
    }

    @Override
    public void addNode(ServerNode serverNode) {
        if(serverNode.getVirtualNodeCount() == 0)
            throw new RuntimeException("Invalid vNode count");
        int idx = serverNode.getLastNodeIndex().get();
        while(idx < serverNode.getVirtualNodeCount()) {
            String key = serverNode.getKey().concat("_vNode").concat(
                    String.valueOf(idx));
            Long hash = hashFunction.hash(key);
            VirtualNode<ServerNode> virtualNode = new VirtualNode<>(serverNode, idx, key, hash);
            hashRing.put(hash, virtualNode);
            idx++;
        }
        serverNode.getLastNodeIndex().set(idx);
    }

    @Override
    public void removeNode(ServerNode serverNode) {
        for(int i=0; i<serverNode.getLastNodeIndex().intValue(); i++) {
            String key = serverNode.getKey().concat("_vNode").concat(String.valueOf(i));
            Long hash = hashFunction.hash(key);
            hashRing.remove(hash);
        }
        serverNode.getLastNodeIndex().set(0);
    }

    @Override
    public ServerNode route(String key) {
        if(key == null)
            throw new RuntimeException("Invalid Key");
        if(hashRing.isEmpty())
            throw new RuntimeException("All Servers Down");
        Long hash = hashFunction.hash(key);
        Map.Entry<Long, VirtualNode<ServerNode>> entry = hashRing.ceilingEntry(hash);
        return (entry != null)
                ? entry.getValue().getPhysicalNode()
                : hashRing.firstEntry().getValue().getPhysicalNode();
    }
}
