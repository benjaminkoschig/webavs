/*
 * Créé le 29 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;

/**
 * @author mmu Object contenannt une facture ainsi que toutes les sections ouvertes associées à l'affilié
 */
public class CASectionEnteteFacture extends CASection {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEnteteFacture = new String();
    private String idPassage = new String();
    private String totalFacture = new String();

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement) + ", FAENTFP.TOTALFACTURE, FAENTFP.IDENTETEFACTURE ";
    }

    /**
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        totalFacture = statement.dbReadNumeric("TOTALFACTURE", 2);
        idEnteteFacture = statement.dbReadNumeric("IDENTETEFACTURE");

    }

    /**
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDPASSAGE", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), ""));
    }

    /**
     * @return
     */
    public String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    /**
     * @return
     */
    public String getIdPassage() {
        return idPassage;
    }

    /**
     * @return
     */
    public String getTotalFacture() {
        return totalFacture;
    }

    /**
     * @param string
     */
    public void setIdEnteteFacture(String string) {
        idEnteteFacture = string;
    }

    /**
     * @param string
     */
    public void setIdPassage(String string) {
        idPassage = string;
    }

    /**
     * @param string
     */
    public void setTotalFacture(String string) {
        totalFacture = string;
    }

}
