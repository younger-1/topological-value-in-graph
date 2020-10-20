
package younger.vrp.topological;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Route {
    private int id;
    private List<Node> nodes;
    private static int static_id = 0;

    private Route(int id) {
        this.nodes = new ArrayList<>();
        this.id = id;
    }

    public static Route of() {
        static_id += 1;
        return new Route(static_id);
    }

    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.nodes.size();
    }

    public List<Node> getAllNodes() {
        return this.nodes;
    }

    public int getIdOfNode(int index) {
        return this.nodes.get(index).getId();
    }

    @Override
    public String toString() {
        String res = "Route: id=" + this.id + " [";
        for (Node node : this.nodes) {
            res += "\n\t\t" + node;
        }
        return res + "\n\t]";
    }

    public Optional<Integer> getIndexOfNode(int id) {
        // return IntStream.range(0, this.getSize()).filter(i -> this.getNode(i).getId() == id)
        //         .mapToObj(i -> Integer.valueOf(i)).findFirst();
        return Stream.iterate(0, i -> i < this.getSize(), i -> i + 1).filter(i -> this.getNode(i).getId() == id)
                .findFirst();
    }

    public Node getNode(int index) {
        return this.nodes.get(index);
    }

    public Optional<Node> getNodeWithId(int id) {
        return this.getIndexOfNode(id).map(i -> this.getNode(i));
    }

    public Node removeNode(int index) {
        return this.nodes.remove(index);
    }

    public Optional<Node> removeNodeWithId(int id) {
        // Optional<Integer> rm = Stream.iterate(0, i -> i < this.getSize(), i -> i + 1).filter(i -> this.getNode(i).getId() == node_id).findFirst();
        // return rm.isPresent() ? Optional.of(this.removeNode(rm.get())) : Optional.empty();

        return this.getIndexOfNode(id).map(i -> this.removeNode(i));
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void addNode(int index, Node node) {
        this.nodes.add(index, node);
    }

    public void addBefore(int node_id, Node node) {
        this.getIndexOfNode(node_id).stream().forEach(i -> this.addNode(i, node));
    }

    public void addAfter(int node_id, Node node) {
        this.getIndexOfNode(node_id).stream().forEach(i -> this.addNode(i + 1, node));
    }

    public int routeLenX() {
        return IntStream.range(1, this.getSize())
                .map(i -> Math.abs(this.getNode(i - 1).getX() - this.getNode(i).getX())).sum();
    }

    public int routeLenY() {
        return IntStream.range(1, this.getSize())
                .map(i -> Math.abs(this.getNode(i - 1).getY() - this.getNode(i).getY())).sum();
    }

    public double routeTopoX() {
        double topo = IntStream.range(1, this.getSize()).mapToDouble(i -> this.getNode(i - 1).getX()
                * this.getNode(i).getX() * (this.getNode(i - 1).getX() - this.getNode(i).getX())).sum();
        return topo;
    }

    public double routeTopoY() {
        double topo = IntStream.range(1, this.getSize()).mapToDouble(i -> this.getNode(i - 1).getY()
                * this.getNode(i).getY() * (this.getNode(i - 1).getY() - this.getNode(i).getY())).sum();
        return topo;
    }
}
