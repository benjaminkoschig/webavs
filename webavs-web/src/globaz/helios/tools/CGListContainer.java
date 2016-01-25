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
public class CGListContainer {

    private List data = null;
    private String id = null;

    /**
     * Constructor for CGListContainer.
     */
    public CGListContainer() {
        data = new ArrayList();
    }

    public void add(Object object) {
        data.add(object);
    }

    public List getData() {
        return data;
    }

    /**
     * Returns the id.
     * 
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            The id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
