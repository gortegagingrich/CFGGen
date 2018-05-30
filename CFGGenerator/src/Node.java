import clojure.lang.PersistentVector;

import java.util.ArrayList;

public class Node {
    protected ArrayList<Node> next;
    protected String code;

    public Node(PersistentVector vec) {
        code = "";
        next = new ArrayList<>();

        for (Object o: vec) {
            if (o.getClass() == String.class) {
                code = String.format("%s%s ",code,(String)o);
            }
        }
    }
}
