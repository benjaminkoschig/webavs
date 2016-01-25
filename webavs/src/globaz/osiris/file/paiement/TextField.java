package globaz.osiris.file.paiement;

/**
 * Classe : type_conteneur Description : Date de création: 2 mars 04
 * 
 * @author scr
 */
public class TextField {

    private int beginPos = -1;
    private int endPos = -1;
    private String name = null;

    /**
     * Constructor for TextField.
     * 
     * @param name
     * @param beginPos
     * @param endPos
     */
    public TextField(String name, int beginPos, int endPos) {
        this.name = name;
        this.beginPos = beginPos;
        this.endPos = endPos;
    }

    /**
     * Returns the beginPos.
     * 
     * @return int
     */
    public int getBeginPos() {
        return beginPos;
    }

    /**
     * Returns the endPos.
     * 
     * @return int
     */
    public int getEndPos() {
        return endPos;
    }

    /**
     * Returns the name.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

}
