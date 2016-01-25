/*
 * Cr�� le 10 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author mmu
 * 
 *         Entit� representant un journal de r�c�ption
 */
public class CPLienCommunicationsPlausi extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCommunication = "";
    private String idLien = "";

    private String idPlausibilite = "";

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        setIdLien(this._incCounter(transaction, idLien));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CPLCRPP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCommunication = statement.dbReadNumeric("IBIDCF");
        idPlausibilite = statement.dbReadNumeric("IXIDPA");
        idLien = statement.dbReadNumeric("ILCRPP");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IBIDCF", this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), ""));
        statement.writeKey("IXIDPA", this._dbWriteNumeric(statement.getTransaction(), getIdPlausibilite(), ""));
        statement.writeKey("ILCRPP", this._dbWriteNumeric(statement.getTransaction(), getIdLien(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IBIDCF",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), "IdJournalRetour"));
        statement.writeField("IXIDPA",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlausibilite(), "IdJournalRetour"));
        statement
                .writeField("ILCRPP", this._dbWriteNumeric(statement.getTransaction(), getIdLien(), "IdJournalRetour"));
    }

    public String getIdCommunication() {
        return idCommunication;
    }

    public String getIdLien() {
        return idLien;
    }

    public String getIdPlausibilite() {
        return idPlausibilite;
    }

    public void setIdCommunication(String idCommunication) {
        this.idCommunication = idCommunication;
    }

    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    public void setIdPlausibilite(String idPlausibilite) {
        this.idPlausibilite = idPlausibilite;
    }

}
