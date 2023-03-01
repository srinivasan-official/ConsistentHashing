package com.localproj.nodes.impl;

import com.localproj.nodes.Node;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerNode implements Node {

    private String hostname;

    private String ip;

    private Integer port;

    private Integer virtualNodeCount;

    private AtomicInteger lastNodeIndex;

    public ServerNode(String hostname, String ip, Integer port, Integer virtualNodeCount) {
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.virtualNodeCount = virtualNodeCount;
        this.lastNodeIndex = new AtomicInteger(0);
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getVirtualNodeCount() {
        return virtualNodeCount;
    }

    public AtomicInteger getLastNodeIndex() {
        return lastNodeIndex;
    }

    public void setVirtualNodeCount(Integer virtualNodeCount) {
        this.virtualNodeCount = virtualNodeCount;
    }

    public void setLastNodeIndex(AtomicInteger lastNodeIndex) {
        this.lastNodeIndex = lastNodeIndex;
    }

    @Override
    public String getKey() {
        return this.getHostname().toUpperCase().concat("_")
                .concat(this.getIp()).concat(":")
                .concat(this.getPort().toString().toUpperCase());
    }
}
