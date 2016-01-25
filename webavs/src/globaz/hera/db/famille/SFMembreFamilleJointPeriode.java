package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class SFMembreFamilleJointPeriode extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String createFromClause(String schema) {

        String createFrom = schema + SFMembreFamille.TABLE_NAME + " LEFT JOIN " + schema + SFPeriode.TABLE_NAME
                + " ON " + schema + SFPeriode.TABLE_NAME + "." + SFPeriode.FIELD_IDMEMBREFAMILLE + " = " + schema
                + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDMEMBREFAMILLE;

        return createFrom;
    }

    private String dateDebut = "";
    private String dateFin = "";
    private String idMembreFamille = "";
    private String idPeriode = "";

    private String type = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPeriode = statement.dbReadNumeric(SFPeriode.FIELD_IDPERIODE);
        idMembreFamille = statement.dbReadNumeric(SFPeriode.FIELD_IDMEMBREFAMILLE);
        type = statement.dbReadNumeric(SFPeriode.FIELD_TYPE);
        dateDebut = statement.dbReadDateAMJ(SFPeriode.FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(SFPeriode.FIELD_DATEFIN);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    public String getType() {
        return type;
    }

}
