# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

### TCC vs LCC

As it's said in the course notes, "TCC is defined as the ratio of directly connected pairs of node in the graph to the number or all pairs of nodes. On its side, LCC is the number of pairs of connected (directly or indirectly) nodes to all pairs of node". 

That's why LCC can't be lower than TCC, because it includes both directly and indirectly connected nodes, where TCC include only directed connected nodes.

TCC and LCC can have the same value where there is no indirectly connected nodes and only directly connected nodes, or if there is no connection between methods. 


```java
public final class Point {
    private float x;
    private float y;

    public Point(x,y) {
        this.x = x;
        this.y = y;
    }

    public float get_X() {
        return x;
    }

    public float get_Y() {
        return y;
    }
}
```

