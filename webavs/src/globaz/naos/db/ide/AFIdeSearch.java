package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;
import org.apache.commons.codec.binary.Base64;

public class AFIdeSearch extends BEntity implements Serializable {

    private static final long serialVersionUID = 8755637531995554164L;

    private String numeroIDE = "";
    private String statut = "";
    private String raisonSociale = "";

    public String getNumeroIDE() {
        return numeroIDE;
    }

    public String getStatut() {
        return statut;
    }

    public void setNumeroIDE(String numeroIDE) {
        this.numeroIDE = numeroIDE;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * conversion Base64 : Une raison sociale peut contenir des caractères non autorisé en get
     * 
     * @return
     */
    public String getRaisonSocialeb64() {
        return new String(Base64.encodeBase64(raisonSociale.getBytes()));
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setRaisonSocialeb64(String raisonSocialeb64) {
        raisonSociale = new String(Base64.decodeBase64(raisonSociale.getBytes()));
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public AFIdeSearch() {
        super();
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        // numeroIDE = statement.dbReadNumeric(AFIdeSearch.FIELD_IDREVISEUR);
        // raisonSociale = statement.dbReadString(AFIdeSearch.FIELD_VISA);

    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
        // if (JadeStringUtil.isEmpty(getVisa())) {
        // _addError(statement.getTransaction(), getSession().getLabel("VAL_VISA_OBLIGATOIRE"));
        // }
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        // statement.writeKey(AFIdeSearch.FIELD_IDREVISEUR,
        // this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), ""));
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        // statement.writeField(AFIdeSearch.FIELD_IDREVISEUR,
        // this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), "idReviseur"));
        // statement
        // .writeField(AFIdeSearch.FIELD_VISA, this._dbWriteString(statement.getTransaction(), getVisa(), "visa"));

    }

}
