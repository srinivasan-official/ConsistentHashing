import com.localproj.algorithm.loadbalancer.impl.ConsistentHashRouter;
import com.localproj.algorithm.loadbalancer.LoadBalancer;
import com.localproj.hash.impl.MD5Hash;
import com.localproj.nodes.impl.ServerNode;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsistentHashingExecutor {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        List<ServerNode> serverNodeList = Arrays.asList(
                new ServerNode("Server1", "10.0.0.1", 6080, 20),
                new ServerNode("Server2", "10.0.0.2", 7080, 25),
                new ServerNode("Server3", "10.0.0.3", 8080, 15),
                new ServerNode("Server4", "10.0.0.4", 9080, 30));

        System.out.println("Initializing Consistent Hash based Load Balancer");
        LoadBalancer lb = new ConsistentHashRouter(new MD5Hash());

        System.out.println("Adding Servers to Load Balancer");
        serverNodeList.forEach(serverNode -> lb.addNode(serverNode));
        System.out.println("Servers added Successfully");

        System.out.println("Generating 100000 IP Address for load test");
        List<String> requestIps = new ArrayList<>();
        for(int i = 0; i < 100000; i++) {
            requestIps.add(getRandomIp());
        }

        routeIps(requestIps, lb);

        ServerNode serverNode = serverNodeList.get(3);
        System.out.println("Removing Server: " + serverNode.getKey());
        lb.removeNode(serverNode);

        routeIps(requestIps, lb);
    }

    private static void routeIps(List<String> requestIps, LoadBalancer lb) {
        Map<ServerNode, AtomicInteger> map = new HashMap<>();
        requestIps.forEach(ip -> {
            ServerNode serverNode = lb.route(ip);
            AtomicInteger atomicInteger = map.getOrDefault(serverNode, new AtomicInteger());
            atomicInteger.incrementAndGet();
            map.put(serverNode, atomicInteger);
        });
        System.out.println("Server Load ratio :: ");
        map.forEach((k,v)->System.out.println(k.getKey() + " Request Handled: " + v));
    }

    private static String getRandomIp() {
        int[][] range = {{607649792, 608174079},// 36.56.0.0-36.63.255.255
                {1038614528, 1039007743},// 61.232.0.0-61.237.255.255
                {1783627776, 1784676351},// 106.80.0.0-106.95.255.255
                {2035023872, 2035154943},// 121.76.0.0-121.77.255.255
                {2078801920, 2079064063},// 123.232.0.0-123.235.255.255
                {-1950089216, -1948778497},// 139.196.0.0-139.215.255.255
                {-1425539072, -1425014785},// 171.8.0.0-171.15.255.255
                {-1236271104, -1235419137},// 182.80.0.0-182.92.255.255
                {-770113536, -768606209},// 210.25.0.0-210.47.255.255
                {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
        };

        Random random = new Random();
        int index = random.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    private static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";

        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }

}
