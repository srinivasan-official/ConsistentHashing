package com.localproj.nodes.impl;

import com.localproj.nodes.Node;

public class ServerNode implements Node {

    private String hostname;

    private String ip;

    private Integer port;

    private Integer replicationFactor;

    public ServerNode(String hostname, String ip, Integer port, Integer replicationFactor) {
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.replicationFactor = replicationFactor;
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

    public Integer getReplicationFactor() {
        return replicationFactor;
    }

    @Override
    public String getKey() {
        return this.getHostname().toUpperCase().concat("_")
                .concat(this.getIp()).concat(":")
                .concat(this.getPort().toString().toUpperCase());
    }
}
