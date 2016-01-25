/*
 * Créé le 13 oct. 06
 */

package globaz.lyra.db.parametres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author hpe
 * 
 *         Manager de la classe LYParametres
 */

public class LYParametresManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 231007857112891543L;

    private String forIdEcheances = "";
    private String forIdParametres = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFPeriodeManager.
     */
    public LYParametresManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getOrder(BStatement statement) {
        return LYParametres.FIELDNAME_IDPARAMETRES;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdParametres)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYParametres.FIELDNAME_IDPARAMETRES);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdParametres));

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdEcheances)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYParametres.FIELDNAME_IDECHEANCES);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdEcheances));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LYEcheances)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LYParametres();
    }

    /**
     * getter pour l'attribut forIdEcheances
     * 
     * @return la valeur courante de l'attribut forIdEcheances
     */
    public String getForIdEcheances() {
        return forIdEcheances;
    }

    /**
     * getter pour l'attribut forIdParametres
     * 
     * @return la valeur courante de l'attribut forIdParametres
     */
    public String getForIdParametres() {
        return forIdParametres;
    }

    /**
     * setter pour l'attribut forIdEcheances
     * 
     * @param forIdEcheances
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdEcheances(String forIdEcheances) {
        this.forIdEcheances = forIdEcheances;
    }

    /**
     * setter pour l'attribut forIdParametres
     * 
     * @param forIdParametres
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdParametres(String forIdParametres) {
        this.forIdParametres = forIdParametres;
    }

}
