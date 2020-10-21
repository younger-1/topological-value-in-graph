
package younger.vrp.topological;

public class Node {

    private static int static_id = 0;
    private int id;
    private int x;
    private int y;

    private Node(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public static Node of(int x, int y) {
        static_id += 1;
        return new Node(x, y, static_id);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return String.format("Node:(%2d, %2d) id: %2d", this.x, this.y, this.id);
    }
}
