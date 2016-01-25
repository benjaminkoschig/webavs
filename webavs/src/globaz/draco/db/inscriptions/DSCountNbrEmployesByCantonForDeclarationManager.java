package globaz.draco.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager qui permet de compter le nombre d'employé par canton
 * pour une déclaration donnée
 */
public class DSCountNbrEmployesByCantonForDeclarationManager extends BManager {

    private static final long serialVersionUID = 4876393641270453628L;
    private String forIdDeclaration = "";
    private String forCodeCanton = "";

    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    @Override
    protected String _getSqlCount(BStatement statement) {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT COUNT(*) FROM ").append(_getCollection()).append("CIINDIP");
        sqlBuilder.append(" WHERE KAIIND IN(");
        sqlBuilder.append(" SELECT DISTINCT EC.KAIIND FROM ").append(_getCollection()).append("CIECRIP EC");
        sqlBuilder.append(" INNER JOIN ").append(_getCollection()).append("DSINDP IND ON IND.KBIECR = EC.KBIECR");
        sqlBuilder.append(" WHERE EC.KBNMOF = 12");
        sqlBuilder.append(" AND IND.TETCAN = ").append(forCodeCanton);
        sqlBuilder.append(" AND IND.TAIDDE = ").append(forIdDeclaration);
        sqlBuilder.append(" GROUP BY EC.KAIIND)");

        return sqlBuilder.toString();
    }

    /**
     * @return the forIdDeclaration
     */
    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    /**
     * @param forIdDeclaration the forIdDeclaration to set
     */
    public void setForIdDeclaration(String forIdDeclaration) {
        this.forIdDeclaration = forIdDeclaration;
    }

    /**
     * @return the forCodeCanton
     */
    public String getForCodeCanton() {
        return forCodeCanton;
    }

    /**
     * @param forCodeCanton the forCodeCanton to set
     */
    public void setForCodeCanton(String forCodeCanton) {
        this.forCodeCanton = forCodeCanton;
    }

}
