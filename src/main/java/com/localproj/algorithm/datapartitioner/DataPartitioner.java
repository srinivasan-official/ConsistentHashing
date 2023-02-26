package com.localproj.algorithm.datapartitioner;

import com.localproj.algorithm.loadbalancer.LoadBalancer;
import com.localproj.hash.HashFunction;
import com.localproj.nodes.DataNode;
import com.localproj.nodes.impl.ServerNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPartitioner {
    private LoadBalancer lb;
    private Map<String, List<String>> serverToDataMap;

    public DataPartitioner(LoadBalancer lb) {
        this.lb = lb;
        this.serverToDataMap = new HashMap<>();
    }

    public void addNode(ServerNode serverNode, int replicaCount) {
        lb.addNode(serverNode, replicaCount);
    }

    public void removeNode(ServerNode serverNode) {
        lb.removeNode(serverNode);
        List<String> dataKey = serverToDataMap.remove(serverNode.getKey());
        migrateData(dataKey);
    }

    private void migrateData(List<String> dataKey) {
        if(dataKey==null || dataKey.isEmpty()) return;
        dataKey.forEach(key -> route(key));
    }

    public <T> ServerNode routeData(String key, T value) {
        DataNode<T> dataNode = new DataNode<>(key, value);
        return route(key);
    }

    private ServerNode route(String key) {
        ServerNode serverNode = lb.route(key);
        serverToDataMap.getOrDefault(serverNode.getKey(), new ArrayList<>()).add(key);
        return serverNode;
    }
}
