package younger.vrp.topological;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Graph {

    private List<Route> routes;
    /**
     * @implNote
     * this.removeNodes is treated as a Stack
     */
    private Deque<Node> removeNodes;
    private Deque<Route> removeRoutes;

    private Graph() {
        this.routes = new ArrayList<>();
        this.removeNodes = new LinkedList<>();
        this.removeRoutes = new LinkedList<>();
    }

    public static Graph ofRandom(int route_num, int node_num) {
        Random r = new Random(-1);
        Graph g = new Graph();
        for (int i = 0; i < route_num; i++) {
            g.addRoute(Route.of());
        }

        for (int i = 0; i < node_num; i++) {
            g.getRouteWithIndex(r.nextInt(g.getSize())).addNode(Node.of(r.nextInt(node_num), r.nextInt(node_num)));
        }

        return g;
    }

    /**
     * 
     * @return
     * The number of routes in this graph
     */
    public int getSize() {
        return this.routes.size();
    }

    public List<Route> getAllRoutes() {
        return this.routes;
    }

    public Route getRouteWithIndex(int index) {
        return this.routes.get(index);
    }

    public Route getRoute(int id) {
        return this.routes.get(IntStream.range(0, this.getSize()).filter(i -> this.routes.get(i).getId() == id)
                .findFirst().getAsInt());
    }

    public void removeRoute(int id) {
        this.removeRoutes.add(this.routes.remove(IntStream.range(0, this.getSize())
                .filter(i -> this.routes.get(i).getId() == id).findFirst().getAsInt()));
    }

    public void addRoute(Route r) {
        this.routes.add(r);
    }

    @Override
    public String toString() {
        String result = "Graph: {";
        for (Route r : this.routes) {
            result += "\n\t" + r;
        }
        return result + "\n}";
    }

    public Node getNode(int id) {
        return this.routes.stream().map(r -> r.getNodeWithId(id)).filter(Optional::isPresent).findFirst().get().get();
    }

    /**
     * Remove nodes with node_ids from route and push them to <code>this.removeNodes</code>
     * @apiNote
     * usually be used before <code>joinAfter(int... node_ids)</code> and <code>joinBefore(int... node_ids)</code>
     * @implNote
     * Push nodes to <code>this.removeNodes</code> with opposite order of node_ids
     */
    public Graph pickNode(int... node_ids) {
        // for (int _k = node_ids.length - 1; _k >= 0; _k--) {
        //     int k = _k;
        //     Node rm = this.routes.stream().map(r -> r.removeNodeWithId(node_ids[k]))
        //             .reduce((a, b) -> a.isPresent() ? a : b).get().get();
        //     this.removeNodes.push(rm);
        // }
        Stream.iterate(node_ids.length - 1, i -> i >= 0, i -> i - 1).map(i -> this.routes.stream()
                .map(r -> r.removeNodeWithId(node_ids[i])).filter(Optional::isPresent).findFirst().get())
                .forEach(option_node -> this.removeNodes.push(option_node.get()));
        return this;
    }

    /**
     * @deprecated
     */
    public Graph addBefore(int route_id, int node_id, Node node) {
        this.routes.stream().filter(r -> r.getId() == route_id).forEach(r -> r.addBefore(node_id, node));
        return this;
    }

    public Graph addNodeBefore(int node_id, Node node) {
        this.routes.stream().forEach(r -> r.addBefore(node_id, node));
        return this;
    }

    /**
    * Pop nodes from <code>this.removeNodes</code> and add them before node_ids
    * @implNote
    * usually be used after <code>pickNode(int... node_ids)</code>
    */
    public Graph joinBefore(int... node_ids) {
        Arrays.stream(node_ids).forEach(id -> this.addNodeBefore(id, this.removeNodes.pop()));
        return this;
    }

    /**
     * @deprecated
     */
    public Graph addAfter(int route_id, int node_id, Node node) {
        this.routes.stream().filter(r -> r.getId() == route_id).forEach(r -> r.addAfter(node_id, node));
        return this;
    }

    public Graph addNodeAfter(int node_id, Node node) {
        this.routes.stream().forEach(r -> r.addAfter(node_id, node));
        return this;
    }

    /**
    * Pop nodes from <code>this.removeNodes</code> and add them after node_ids
    * @implNote
    * usually be used after <code>pickNode(int... node_ids)</code>
    */
    public Graph joinAfter(int... node_ids) {
        Arrays.stream(node_ids).forEach(id -> this.addNodeAfter(id, this.removeNodes.pop()));
        return this;
    }

    public Graph moveBefore(int rm_node_id, int node_id) {
        return this.pickNode(rm_node_id).addNodeBefore(node_id, this.removeNodes.pop());
    }

    public Graph moveAfter(int rm_node_id, int node_id) {
        return this.pickNode(rm_node_id).addNodeAfter(node_id, this.removeNodes.pop());
    }

    public double topoX() {

        int[] routeLenX = new int[this.getSize()];
        double[] routeTopoX = new double[this.getSize()];

        IntStream.range(0, this.getSize()).forEach(i -> routeLenX[i] = this.routes.get(i).routeLenX());

        IntStream.range(0, this.getSize()).forEach(i -> routeTopoX[i] = this.routes.get(i).routeTopoX());

        double topoX = IntStream.range(0, this.getSize()).mapToDouble(i -> routeTopoX[i] * routeLenX[i]).sum();
        return topoX;
    }
}
