package com.localproj.algorithm.loadbalancer;

import com.localproj.nodes.Node;
import com.localproj.nodes.impl.ServerNode;
import com.localproj.nodes.impl.VirtualNode;

import java.util.List;

public interface LoadBalancer {
    void addNode(ServerNode serverNode, int replicaCount);

    void removeNode(ServerNode serverNode);

    ServerNode route(String key);
}
