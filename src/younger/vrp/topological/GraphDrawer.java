
package younger.vrp.topological;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class GraphDrawer extends JPanel {
    private static final long serialVersionUID = 1L;

    public Graph gg;
    private double[][] x_routes;
    private double[][] y_routes;
    private int[] route_id;
    private int[][] node_id;

    double x_min;
    double y_min;
    double x_diff;
    double y_diff;

    private final Random r = new Random(1004);
    private Color[] routeColor;

    public static void draw_graph(Graph gg, int screenPositionIndex) {
        int colNum = 2;
        // int rowNum = 2;
        // int xPosition = (screenPositionIndex - 1) % colNum;
        // int yPosition = (screenPositionIndex - 1) / colNum;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = (int) screenSize.getWidth() / colNum;
        // int frameHeight = (int) screenSize.getHeight() / rowNum;

        GraphDrawer graph = new GraphDrawer(gg);
        // Border myBorder = BorderFactory.createTitledBorder(graph_kind);
        Border myBorder = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.CYAN);
        graph.setBorder(myBorder);
        graph.setPreferredSize(new Dimension(frameWidth, frameWidth));

        JPanel legend = draw_legend(graph, 60, frameWidth);
        JPanel control = draw_control(graph, 120, frameWidth);
        JPanel main_pane = draw_mainPane(graph, legend, control);

        draw_frame(screenPositionIndex, main_pane);
    }

    private static JPanel draw_legend(GraphDrawer graph, int legend_width, int legend_height) {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (int i = 0; i < graph.route_id.length; i++) {
            int _i = i;
            JPanel child_legend = new JPanel() {
                private static final long serialVersionUID = -3370717016527965812L;

                @Override
                public void paintComponent(Graphics g) {
                    Dimension d = this.getSize();
                    g.setColor(graph.routeColor[_i]);
                    g.fillRect(0, d.height / 4, d.width / 2, d.height / 2);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("JetBrainsMono NF", Font.BOLD, 12));
                    g.drawString(graph.route_id[_i] + "", d.width * 2 / 3, d.height * 2 / 3);
                }
            };
            child_legend.setPreferredSize(new Dimension(legend_width, 15));
            legend.add(child_legend);
        }
        legend.setBackground(Color.LIGHT_GRAY);
        legend.setPreferredSize(new Dimension(legend_width, legend_height));
        return legend;
    }

    private static JPanel draw_control(GraphDrawer graph, int legend_width, int legend_height) {
        JPanel control = new JPanel(new GridLayout(2, 1));

        JTextField result = new JTextField("" + graph.gg.topoX());
        result.setEditable(false);
        result.setHorizontalAlignment(JTextField.CENTER);
        result.setPreferredSize(new Dimension(110, 25));

        JTextField node_id1 = new JTextField("", 5);
        node_id1.setPreferredSize(new Dimension(55, 25));
        JTextField node_id2 = new JTextField("", 5);
        node_id2.setPreferredSize(new Dimension(55, 25));

        JButton ma = new JButton("Move After");
        ma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id1 = Integer.parseInt(node_id1.getText());
                int id2 = Integer.parseInt(node_id2.getText());
                graph.gg.moveAfter(id1, id2);
                result.setText("" + graph.gg.topoX());
            }
        });
        ma.setPreferredSize(new Dimension(110, 22));

        JButton mb = new JButton("Move Before");
        mb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.setText("" + graph.gg.topoX());
            }
        });
        mb.setPreferredSize(new Dimension(110, 22));

        JPanel front = new JPanel();
        front.add(node_id1);
        front.add(node_id2);
        front.add(ma);
        front.add(mb);

        JPanel back = new JPanel();
        back.add(result);

        control.add(front);
        control.add(back);
        control.setBackground(Color.LIGHT_GRAY);
        control.setPreferredSize(new Dimension(legend_width, legend_height));
        return control;
    }

    private static JPanel draw_mainPane(GraphDrawer graph, JPanel legend, JPanel control) {
        JPanel main_pane = new JPanel();
        main_pane.setLayout(new BoxLayout(main_pane, BoxLayout.X_AXIS));
        main_pane.setOpaque(true);
        main_pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        main_pane.add(legend);
        main_pane.add(graph);
        main_pane.add(control);
        return main_pane;
    }

    private static void draw_frame(int screenPositionIndex, JPanel main_pane) {
        String graph_kind = null;
        if (screenPositionIndex == 1) {
            graph_kind = "g1";
        } else if (screenPositionIndex == 2) {
            graph_kind = "g2";
        } else if (screenPositionIndex == 3) {
            graph_kind = "g3";
        } else if (screenPositionIndex == 4) {
            graph_kind = "g4";
        }
        JFrame myFrame = new JFrame("Graph -- " + graph_kind);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // myFrame.setBounds(xPosition * frameWidth, yPosition * frameHeight, frameWidth + 60, frameWidth);
        myFrame.getContentPane().add(main_pane);
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }

    public GraphDrawer(Graph myGraph) {
        this.gg = myGraph;
        List<Route> routes = this.gg.getAllRoutes();
        this.route_id = new int[routes.size()];
        this.node_id = new int[routes.size()][];

        this.routeColor = new Color[routes.size()];
        for (int i = 0; i < routeColor.length; i++) {
            this.routeColor[i] = new Color((float) r.nextDouble(), (float) r.nextDouble(), (float) r.nextDouble());
            this.route_id[i] = routes.get(i).getId();
        }

        this.x_routes = new double[routes.size()][];
        this.y_routes = new double[routes.size()][];
        int m = 0;
        for (Route route : routes) {
            int n = 0;
            List<Node> nodes = route.getAllNodes();
            this.x_routes[m] = new double[nodes.size()];
            this.y_routes[m] = new double[nodes.size()];
            this.node_id[m] = new int[nodes.size()];
            for (Node node : nodes) {
                double xCord = node.getX();
                double yCord = node.getY();
                this.x_routes[m][n] = xCord;
                this.y_routes[m][n] = yCord;

                this.node_id[m][n] = node.getId();
                n += 1;
            }
            m += 1;
        }

        this.x_min = Arrays.stream(x_routes).map(route -> Arrays.stream(route).reduce(Double::min).orElse(0.0))
                .reduce(Double::min).get();
        this.x_diff = Arrays.stream(x_routes).map(route -> Arrays.stream(route).reduce(0, Double::max)).reduce(0D,
                Double::max) - x_min;
        this.y_min = Arrays.stream(y_routes).map(route -> Arrays.stream(route).reduce(Double::min).orElse(0.0))
                .reduce(Double::min).orElse(0.0);
        this.y_diff = Arrays.stream(y_routes).map(route -> Arrays.stream(route).reduce(0, Double::max)).reduce(0D,
                Double::max) - y_min;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = this.getWidth();
        int height = this.getHeight();
        // int radius = width / 110;
        int radius = (int) (Math.log(width + 1) / 1.0);

        for (int i = 0; i < this.x_routes.length; i++) {
            int[] x_coord = Arrays.stream(this.x_routes[i])
                    .map((num) -> 1 / 40.0 * width + 38 / 40.0 * width * (num - this.x_min) / this.x_diff)
                    .mapToInt(num -> (int) num).toArray();
            int[] y_coord = Arrays.stream(this.y_routes[i])
                    .map((num) -> 1 / 40.0 * height + 38 / 40.0 * height * (num - this.y_min) / this.y_diff)
                    .map(num -> height - num).mapToInt(num -> (int) num).toArray();

            g.setColor(this.routeColor[i]);
            g.drawPolyline(x_coord, y_coord, x_coord.length);

            for (int j = 0; j < x_coord.length; j++) {
                g.setColor(this.routeColor[i]);
                g.fillOval(x_coord[j] - radius, y_coord[j] - radius, 2 * radius, 2 * radius);
                // node id
                g.setColor(Color.BLACK);
                g.setFont(new Font("DejaVuSansMono NF", Font.PLAIN, 13));
                g.drawString(Integer.toString(this.node_id[i][j]), x_coord[j], y_coord[j]);
            }

            // route id
            // g.setColor(Color.RED);
            // g.drawString(Integer.toString(this.route_id[i]), x_coord[0], y_coord[0]);

        }
    }
}
