import clojure.lang.Keyword;
import clojure.lang.PersistentVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Structure {
    private Node start;
    private ArrayList<Node> end;

    private static final HashMap<String, BiConsumer<Structure, PersistentVector>> CONSUMERS;

    static {
        CONSUMERS = new HashMap<>();
        CONSUMERS.put("Pn", (s,v) -> s.structPn(v));
        CONSUMERS.put("P1", (s,v) -> s.structP1(v));
        CONSUMERS.put("D0", (s,v) -> s.structD0(v));
        CONSUMERS.put("D1", (s,v) -> s.structD1(v));
        CONSUMERS.put("D2", (s,v) -> s.structD2(v));
        CONSUMERS.put("D3", (s,v) -> s.structD3(v));
    }

    public Structure(PersistentVector struct) {
        end = new ArrayList<>();

        // determine type
        assert struct.size() > 0 && struct.get(0).getClass() == Keyword.class;

        Keyword tag = (Keyword)(struct.get(0));

        assert CONSUMERS.containsKey(tag.getName());

        CONSUMERS.get(tag.getName()).accept(this, struct);
    }

    private void structPn(PersistentVector struct) {
        System.out.println("Pn not finished");

        for (Object o: struct) {
            if (o.getClass() == PersistentVector.class) {
                new Structure((PersistentVector)o);
            }
        }
    }

    private void structP1(PersistentVector struct) {
        start = new Node(struct);
        end.add(start);

        System.out.println(start.code);
    }

    private void structD0(PersistentVector struct) {
        System.out.println("DO not implemented");
    }

    private void structD1(PersistentVector struct) {
        System.out.println("D1 not implemented");
    }

    private void structD2(PersistentVector struct) {
        System.out.println("D2 not implemented");
    }

    private void structD3(PersistentVector struct) {
        System.out.println("D3 not implemented");
    }
}
