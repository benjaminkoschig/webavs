/*
 * Créé le 12 oct. 06
 */
package globaz.lyra.db.echeances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author hpe
 * 
 *         Manager de la classe LYEcheances
 */
public class LYEcheancesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = -5307622656649709561L;

    private String forDomaineApplicatif = "";
    private String forIdEcheances = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LYEcheancesManager.
     */
    public LYEcheancesManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdEcheances)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYEcheances.FIELDNAME_IDECHEANCES);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdEcheances));
        }

        if (!JadeStringUtil.isIntegerEmpty(forDomaineApplicatif)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYEcheances.FIELDNAME_DOMAINE_APPLI);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forDomaineApplicatif));

        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LYEcheances)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LYEcheances();
    }

    /**
     * getter pour l'attribut forDomaineApplicatif
     * 
     * @return la valeur courante de l'attribut forDomaineApplicatif
     */
    public String getForDomaineApplicatif() {
        return forDomaineApplicatif;
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
     * setter pour l'attribut forDomaineApplicatif
     * 
     * @param forDomaineApplicatif
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDomaineApplicatif(String forDomaineApplicatif) {
        this.forDomaineApplicatif = forDomaineApplicatif;
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

}
