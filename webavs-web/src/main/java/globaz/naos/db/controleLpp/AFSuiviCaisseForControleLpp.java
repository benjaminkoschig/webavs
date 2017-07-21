/*
 * Globaz SA.
 */
package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFSuiviCaisseForControleLpp extends BEntity {

    private static final long serialVersionUID = 1L;

    private String idAffilie;
    private String dateDebut;
    private String dateFin;
    private String motifSuivi;

    public AFSuiviCaisseForControleLpp() {
        super();
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffilie = statement.dbReadNumeric("MAIAFF");
        dateDebut = statement.dbReadDateAMJ("MYDDEB");
        dateFin = statement.dbReadDateAMJ("MYDFIN");
        motifSuivi = statement.dbReadNumeric("MYTMOT");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not implemented
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotifSuivi() {
        return motifSuivi;
    }

    public void setMotifSuivi(String motifSuivi) {
        this.motifSuivi = motifSuivi;
    }

}
