package algorithms;

import algorithms.outputs.Test;
import langs.eventb.Event;
import langs.eventb.substitutions.Skip;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 16:59
 */
public final class TestsComputer extends AComputer<List<Test>> {

    private static final int NONE = -1;
    private final List<ConcreteState> vertices;
    private final ConcreteState initialVertex;
    private final int initialVertexIndex;
    private final List<ConcreteTransition> edges;
    private int[] neg;
    private int N;
    private int delta[];
    private int pos[];
    private int arcs[][];
    private Vector label[][];
    private int f[][];
    private float c[][];
    private String cheapestLabel[][];
    private boolean defined[][];
    private int path[][];
    private List<Test> tests;

    public TestsComputer(ConcreteState initialVertex, LinkedHashSet<ConcreteState> vertices, List<ConcreteTransition> edges) {
        if ((N = vertices.size()) <= 0) throw new Error("Graph is empty");
        delta = new int[N];
        defined = new boolean[N][N];
        label = new Vector[N][N];
        c = new float[N][N];
        f = new int[N][N];
        arcs = new int[N][N];
        cheapestLabel = new String[N][N];
        path = new int[N][N];
        tests = new ArrayList<>();
        this.initialVertex = initialVertex;
        this.vertices = new ArrayList<>(vertices);
        this.initialVertexIndex = this.vertices.indexOf(initialVertex);
        this.edges = edges;
    }

    @Override
    List<Test> run() {
        for (ConcreteTransition edge : edges) {
            addArc(edge.getEvent().getName(), vertices.indexOf(edge.getSource()), vertices.indexOf(edge.getTarget()), 1);
        }
        solve();
        return computeCPT(initialVertexIndex);
    }

    /**
     * Class for finding and printing the optimal Chinese Postman tour of multidigraphs.
     * For more details, read <a href="http:
     *
     * @author Harold Thimbleby, 2001, 2, 3
     */

    private void solve() {
        leastCostPaths();
        checkValid();
        findUnbalanced();
        findFeasible();
        boolean moreImprovements = improvements();
        while (moreImprovements) {
            moreImprovements = improvements();
        }
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private void addArc(String lab, int u, int v, float cost) {
        if (!defined[u][v]) label[u][v] = new Vector();
        label[u][v].addElement(lab);
        if (!defined[u][v] || c[u][v] > cost) {
            c[u][v] = cost;
            cheapestLabel[u][v] = lab;
            defined[u][v] = true;
            path[u][v] = v;
        }
        arcs[u][v]++;
        delta[u]++;
        delta[v]--;
    }

    /**
     * Floyd-Warshall algorithm
     * Assumes no negative self-cycles.
     * Finds least cost tests or terminates on finding any non-trivial negative cycle.
     */

    private void leastCostPaths() {
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                if (defined[i][k]) {
                    for (int j = 0; j < N; j++) {
                        if (defined[k][j] && (!defined[i][j] || c[i][j] > c[i][k] + c[k][j])) {
                            path[i][j] = path[i][k];
                            c[i][j] = c[i][k] + c[k][j];
                            defined[i][j] = true;
                            if (i == j && c[i][j] < 0) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkValid() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!defined[i][j]) {
                    throw new Error("Graph is not strongly connected");
                }
            }
            if (c[i][i] < 0) {
                throw new Error("Graph has a negative cycle");
            }
        }
    }

    /*private float cost() {
        return basicCost + phi();
    }

    private float phi() {
        float phi = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                phi += c[i][j] * f[i][j];
            }
        }
        return phi;
    }*/

    private void findUnbalanced() {
        int nn = 0, np = 0;
        for (int i = 0; i < N; i++) {
            if (delta[i] < 0) {
                nn++;
            } else if (delta[i] > 0) {
                np++;
            }
        }
        neg = new int[nn];
        pos = new int[np];
        nn = np = 0;
        for (int i = 0; i < N; i++) {
            if (delta[i] < 0) {
                neg[nn++] = i;
            } else if (delta[i] > 0) {
                pos[np++] = i;
            }
        }
    }

    private void findFeasible() {
        int delta[] = new int[N];
        System.arraycopy(this.delta, 0, delta, 0, N);
        for (int i : neg) {
            for (int j : pos) {
                f[i][j] = -delta[i] < delta[j] ? -delta[i] : delta[j];
                delta[i] += f[i][j];
                delta[j] -= f[i][j];
            }
        }
    }

    private boolean improvements() {
        TestsComputer residual = new TestsComputer(initialVertex, new LinkedHashSet<>(vertices), edges);
        for (int i : neg) {
            for (int j : pos) {
                residual.addArc(null, i, j, c[i][j]);
                if (f[i][j] != 0) {
                    residual.addArc(null, j, i, -c[i][j]);
                }
            }
        }
        residual.leastCostPaths();
        for (int i = 0; i < N; i++) {
            if (residual.c[i][i] < 0) {
                int k = 0, u, v;
                boolean kunset = true;
                u = i;
                do {
                    v = residual.path[u][i];
                    if (residual.c[u][v] < 0 && (kunset || k > f[v][u])) {
                        k = f[v][u];
                        kunset = false;
                    }
                } while ((u = v) != i);
                u = i;
                do {
                    v = residual.path[u][i];
                    if (residual.c[u][v] < 0) {
                        f[v][u] -= k;
                    } else {
                        f[u][v] += k;
                    }
                } while ((u = v) != i);
                return true;
            }
        }
        return false;
    }

    private int findPath(int from, int f[][]) {
        for (int i = 0; i < N; i++) {
            if (f[from][i] > 0) {
                return i;
            }
        }
        return NONE;
    }

    private List<Test> computeCPT(int startVertex) {
        int v = startVertex;
        int arcs[][] = new int[N][N];
        int f[][] = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                arcs[i][j] = this.arcs[i][j];
                f[i][j] = this.f[i][j];
            }
        }
        Test fullPath = new Test();
        while (true) {
            int u = v;
            if ((v = findPath(u, f)) != NONE) {
                f[u][v]--;
                for (int p; u != v; u = p) {
                    p = path[u][v];
                    fullPath.add(new ConcreteTransition(vertices.get(u), new Event(cheapestLabel[u][p], new Skip()), vertices.get(p)));
                    if (p == startVertex) {
                        tests.add(fullPath);
                        fullPath = new Test();
                    }
                }
            } else {
                int bridgeVertex = path[u][startVertex];
                if (arcs[u][bridgeVertex] == 0)
                    break;
                v = bridgeVertex;
                for (int i = 0; i < N; i++) {
                    if (i != bridgeVertex && arcs[u][i] > 0) {
                        v = i;
                        break;
                    }
                }
                arcs[u][v]--;
                fullPath.add(new ConcreteTransition(vertices.get(u), new Event((String) label[u][v].elementAt(arcs[u][v]), new Skip()), vertices.get(v)));
                if (v == startVertex) {
                    tests.add(fullPath);
                    fullPath = new Test();
                }
            }
        }
        return tests;
    }

}
