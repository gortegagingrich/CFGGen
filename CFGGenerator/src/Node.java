import clojure.lang.PersistentVector;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> prev;
    private ArrayList<Node> next;
    private String code;

    public Node(PersistentVector vec) {
        code = "";
        next = new ArrayList<>();
        prev = new ArrayList<>();

        // All string type objects in vec should be parts of the line of code
        // this node represents.
        for (Object o: vec) {
            if (o.getClass() == String.class) {
                code = String.format("%s%s ",code,o);
            }
        }
    }

    public ArrayList<Node> getPrev() {
        return prev;
    }

    public ArrayList<Node> getNext() {
        return next;
    }

    public String getCode() {
        return code;
    }
}
