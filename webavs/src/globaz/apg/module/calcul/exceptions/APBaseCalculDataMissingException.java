/*
 * Créé le 27 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul.exceptions;

import globaz.apg.module.calcul.APCalculException;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class APBaseCalculDataMissingException extends APCalculException {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String extraMessage = "";
    private String label = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APRulesException.
     */
    public APBaseCalculDataMissingException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APRulesException.
     * 
     * @param arg0
     */
    public APBaseCalculDataMissingException(String arg0) {
        super(arg0);
    }

    /**
     * Constructor for PRFieldNotFoundException.
     * 
     * @param label
     * @param extraMessage
     *            DOCUMENT ME!
     */
    public APBaseCalculDataMissingException(String label, String extraMessage) {
        super(extraMessage);
        this.label = label;
        this.extraMessage = extraMessage;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
