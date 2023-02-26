import com.localproj.algorithm.datapartitioner.DataPartitioner;
import com.localproj.algorithm.loadbalancer.LoadBalancer;
import com.localproj.algorithm.loadbalancer.impl.ConsistentHashRouter;
import com.localproj.hash.impl.MD5Hash;
import com.localproj.nodes.impl.ServerNode;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataPartitionExecutor {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        LoadBalancer lb = new ConsistentHashRouter(new MD5Hash());
        DataPartitioner dataPartitioner = new DataPartitioner(lb);

        List<ServerNode> serverNodeList = Arrays.asList(
                new ServerNode("Server1", "10.0.0.1", 6080, 20),
                new ServerNode("Server2", "10.0.0.2", 7080, 25),
                new ServerNode("Server3", "10.0.0.3", 8080, 15),
                new ServerNode("Server4", "10.0.0.4", 9080, 30));

        System.out.println("Adding Servers to Load Balancer");
        serverNodeList.forEach(serverNode -> lb.addNode(serverNode, serverNode.getReplicationFactor()));
        System.out.println("Servers added Successfully");

        System.out.println("Generating 100000 Data for partition test");
        List<String> dataList = new ArrayList<>();
        for(int i=0; i<100000; i++) {
            dataList.add(String.valueOf("Data_").concat(Integer.toString(i)));
        }

        routeData(dataPartitioner, dataList);

        ServerNode serverNode = serverNodeList.get(3);
        System.out.println("Removing Server: " + serverNode.getKey());
        lb.removeNode(serverNode);

        routeData(dataPartitioner, dataList);
    }

    private static void routeData(DataPartitioner dataPartitioner, List<String> dataList) {
        Map<ServerNode, AtomicInteger> map = new HashMap<>();
        dataList.forEach(data -> {
            ServerNode serverNode = dataPartitioner.routeData(data, data);
            AtomicInteger atomicInteger = map.getOrDefault(serverNode, new AtomicInteger());
            atomicInteger.incrementAndGet();
            map.put(serverNode, atomicInteger);
        });
        System.out.println("Data Partition ratio :: ");
        map.forEach((k,v)->System.out.println(k.getKey() + " Request Handled: " + v));
    }

}
