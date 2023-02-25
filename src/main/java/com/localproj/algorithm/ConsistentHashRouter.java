package com.localproj.algorithm;

import com.localproj.hash.HashFunction;
import com.localproj.nodes.Node;
import com.localproj.nodes.impl.ServerNode;
import com.localproj.nodes.impl.VirtualNode;

import java.util.*;

public class ConsistentHashRouter implements LoadBalancer {
    private TreeMap<Long, VirtualNode<ServerNode>> hashRing;
    private HashFunction hashFunction;
    private Map<ServerNode, List<VirtualNode<ServerNode>>> serverNodeToVirtualNodeMap;

    public ConsistentHashRouter(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
        this.hashRing = new TreeMap<>();
        this.serverNodeToVirtualNodeMap = new HashMap<>();
    }

    @Override
    public void addNode(ServerNode serverNode, int replicaCount) {
        List<VirtualNode<ServerNode>> vNodes = serverNodeToVirtualNodeMap.getOrDefault(serverNode,
                new ArrayList<>());
        int size = vNodes.size();
        for(int i=size; i<size+replicaCount; i++) {
            String key = serverNode.getKey().concat("_vNode").concat(String.valueOf(i));
            Long hash = hashFunction.hash(key);
            VirtualNode<ServerNode> virtualNode = new VirtualNode<>(serverNode, i, key, hash);
            hashRing.put(hash, virtualNode);
            vNodes.add(virtualNode);
        }
        serverNodeToVirtualNodeMap.put(serverNode, vNodes);
    }

    @Override
    public void removeNode(ServerNode serverNode) {
        if(!serverNodeToVirtualNodeMap.containsKey(serverNode))
            throw new RuntimeException("Server Doesn't exists");
        List<VirtualNode<ServerNode>> vNodes = serverNodeToVirtualNodeMap.remove(serverNode);
        vNodes.stream().map(VirtualNode::getHash).forEach(hash -> hashRing.remove(hash));
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
