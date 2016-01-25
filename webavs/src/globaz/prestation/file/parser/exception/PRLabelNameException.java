package globaz.prestation.file.parser.exception;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 2 mars 04
 * 
 * @author scr
 * 
 */
public class PRLabelNameException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String extraMessage = "";
    private String label = null;

    /**
     * Constructor for PRFieldNotFoundException.
     * 
     * @param s
     */
    public PRLabelNameException(String label, String extraMessage) {
        super(extraMessage);
        this.label = label;
        this.extraMessage = extraMessage;
    }

    /**
     * Returns the extraMessage.
     * 
     * @return String
     */
    public String getExtraMessage() {
        return extraMessage;
    }

    /**
     * Returns the label.
     * 
     * @return String
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the extraMessage.
     * 
     * @param extraMessage
     *            The extraMessage to set
     */
    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            The label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

}
