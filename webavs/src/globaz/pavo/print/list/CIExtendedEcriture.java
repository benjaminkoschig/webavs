package globaz.pavo.print.list;

import globaz.globall.db.BStatement;

/**
 * Insert the type's description here. Creation date: (09.07.2003 17:03:35)
 * 
 * @author: Administrator
 */
public class CIExtendedEcriture extends globaz.pavo.db.compte.CIEcriture {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String dateInscriptionJournal;
    private boolean hidden = false;
    private java.lang.String nomPrenomCompteIndividuel;
    private java.lang.String numeroAvsCompteIndividuel;

    /**
     * CIExtendedEcriture constructor comment.
     */
    public CIExtendedEcriture() {
        super();
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        dateInscriptionJournal = statement.dbReadDateAMJ("KCDINS");
        nomPrenomCompteIndividuel = statement.dbReadString("KALNOM");
        numeroAvsCompteIndividuel = statement.dbReadString("KANAVS");
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:10:57)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateInscriptionJournal() {
        return dateInscriptionJournal;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:09:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNomPrenomCompteIndividuel() {
        return nomPrenomCompteIndividuel;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:07:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroAvsCompteIndividuel() {
        return numeroAvsCompteIndividuel;
    }

    /**
     * Returns the hidden.
     * 
     * @return boolean
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:10:57)
     * 
     * @param newDateInscriptionJournal
     *            java.lang.String
     */
    public void setDateInscriptionJournal(java.lang.String newDateInscriptionJournal) {
        dateInscriptionJournal = newDateInscriptionJournal;
    }

    /**
     * Sets the hidden.
     * 
     * @param hidden
     *            The hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:09:11)
     * 
     * @param newNomPrenomCompteIndividuel
     *            java.lang.String
     */
    public void setNomPrenomCompteIndividuel(java.lang.String newNomPrenomCompteIndividuel) {
        nomPrenomCompteIndividuel = newNomPrenomCompteIndividuel;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:07:53)
     * 
     * @param newNumeroAvsCompteIndividuel
     *            java.lang.String
     */
    public void setNumeroAvsCompteIndividuel(java.lang.String newNumeroAvsCompteIndividuel) {
        numeroAvsCompteIndividuel = newNumeroAvsCompteIndividuel;
    }

}
