package globaz.helios.process.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 14 sept. 04
 * 
 * @author scr
 * 
 */
public class CGMetaAnnonceAttributs {

    private Map attributs = new HashMap();
    private String codeAnnonce = null;

    /**
     * Constructor for CGMetaAnnonceAttributs.
     */
    public CGMetaAnnonceAttributs() {
        super();
    }

    /**
     * Returns the attributs.
     * 
     * @return Map
     */
    public Map getAttributs() {
        return attributs;
    }

    /**
     * Returns the codeAnnonce.
     * 
     * @return String
     */
    public String getCodeAnnonce() {
        return codeAnnonce;
    }

    /**
     * Sets the attributs.
     * 
     * @param attributs
     *            The attributs to set
     */
    public void setAttributs(Map attributs) {
        this.attributs.putAll(attributs);
    }

    /**
     * Sets the codeAnnonce.
     * 
     * @param codeAnnonce
     *            The codeAnnonce to set
     */
    public void setCodeAnnonce(String codeAnnonce) {
        this.codeAnnonce = codeAnnonce;
    }

}
