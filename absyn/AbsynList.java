package absyn;

import java.util.*;

public abstract class AbsynList<T extends Absyn> extends Absyn {

    private List<T> flattened;

    public abstract T getHead();

    public abstract AbsynList<T> getTail();

    public List<T> getFlattened() {
        if (flattened != null) {
            return flattened;
        }

        flattened = new ArrayList<>();

        var _tail = this;

        while (_tail != null) {
            var head = _tail.getHead();

            if (head != null) {
                flattened.add(head);
            }

            _tail = _tail.getTail();
        }

        return flattened;
    }
}
