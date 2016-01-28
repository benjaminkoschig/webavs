package globaz.draco.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class DSInscAnneeMaxMinManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDeclaration = "";
    boolean isMin = false;

    @Override
    protected String _getFields(BStatement statement) {
        return "MIN(TENANN) AS VALMIN, MAX(TENANN) AS VALMAX";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        // TODO Auto-generated method stub
        return super._getFrom(statement);
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE =" + _dbWriteNumeric(statement.getTransaction(), getForIdDeclaration());
        }
        sqlWhere += " AND TENANN > 0 ";
        System.out.println("New Class Used");
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new DSInscAnneeMaxMinEntity();
    }

    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    public void setForIdDeclaration(String forIdDeclaration) {
        this.forIdDeclaration = forIdDeclaration;
    }

}
