/**
 * class CPDecisionsAvecMiseEnCompteManager écrit le 19/01/05 par JPA
 * 
 * class entité pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPLienSedexCommunicationFiscale extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCommunication = "";
    private String idMessageSedex = "";

    @Override
    protected String _getTableName() {
        return "CPLSECOP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMessageSedex = statement.dbReadNumeric("IBMEID");
        idCommunication = statement.dbReadString("IBIDCF");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IBIDCF",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), "idCommunication"));
        statement.writeField("IBMEID",
                this._dbWriteString(statement.getTransaction(), getIdMessageSedex(), "idMessageSedex"));
    }

    public String getIdCommunication() {
        return idCommunication;
    }

    public String getIdMessageSedex() {
        return idMessageSedex;
    }

    public void setIdCommunication(String idCommentaire) {
        idCommunication = idCommentaire;
    }

    public void setIdMessageSedex(String idLienCommentaireRemarque) {
        idMessageSedex = idLienCommentaireRemarque;
    }
}
