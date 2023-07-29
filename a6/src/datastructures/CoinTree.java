package datastructures;
import game.*;
import java.util.ArrayList;
import java.util.List;

public class CoinTree {

    private final Node node;
    private final int value;
    private final List<CoinTree> children;

    public CoinTree(Node node, int value) {
        this.node = node;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public Node getNode() {
        return node;
    }

    public int getValue() {
        return value;
    }

    public List<CoinTree> getChildren() {
        return children;
    }

    public void addChild(CoinTree child) {
        children.add(child);
    }

    public int getTotalValue() {
        int totalValue = value;
        for (CoinTree child : children) {
            totalValue += child.getNode().getTile().coins();
        }
        return totalValue;
    }

    public boolean hasChild(Node node) {
        for (CoinTree child : children) {
            if (child.getNode().equals(node)) {
                return true;
            }
        }
        return false;
    }
}
