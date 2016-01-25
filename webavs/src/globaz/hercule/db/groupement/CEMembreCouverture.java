package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hercule.db.couverture.CECouverture;

/**
 * 
 * @author SCO
 * @since 04 aout 2011
 */
public class CEMembreCouverture extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String idAffiliation = "";
    private String numAffilie = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffiliation = statement.dbReadNumeric(CEMembre.FIELD_IDAFFILIATION);
        numAffilie = statement.dbReadString(CECouverture.FIELD_NUMAFFILIE);
        annee = statement.dbReadNumeric(CECouverture.FIELD_ANNEE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

}
