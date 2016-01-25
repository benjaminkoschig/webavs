package globaz.helios.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 23 nov. 04
 * 
 * @author scr
 * 
 */
public class CGListParser {

    private List data = null;
    private int index = -1;

    /**
     * Constructor for CGListContainer.
     */
    public CGListParser() {
        data = new ArrayList();
    }

    public void add(Object object) {
        data.add(object);
    }

    public void clear() {
        data.clear();
        index = -1;
    }

    public List getData() {
        return data;
    }

    public Object getFirst() {
        index = 0;
        return data.get(index);
    }

    public Object getLast() {
        index = data.size() - 1;
        return data.get(index);
    }

    public Object getNext() throws IndexOutOfBoundsException {
        index++;
        return data.get(index);
    }

    public Object getPrevious() throws IndexOutOfBoundsException {
        index--;
        return data.get(index);
    }

}
