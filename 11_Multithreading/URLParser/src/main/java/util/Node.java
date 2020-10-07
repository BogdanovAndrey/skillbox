package util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Node {
    private boolean isRoot;
    private int level;
    private Node parent;
    private ArrayList<Node> nextLevelNodes;
    private String text;

    public Node(int level, Node parent, boolean isRoot, String text) {
        this.level = level;
        this.isRoot = isRoot;
        nextLevelNodes = new ArrayList<Node>();
        this.text = text;
    }

    public Node(int level, Node parent, String text) {
        this(level, parent, false, text);
    }

    public void listUnion(Node brotherNode) {
        this.nextLevelNodes.addAll(brotherNode.getNextLevelNodes());
    }
}
