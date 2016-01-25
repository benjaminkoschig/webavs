/*
 * Créé le 25 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class PRDemandeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtatDemande = "";
    private String forIdTiers = "";
    private String forTypeDemande = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isBlank(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_IDTIERS + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdTiers);
        }

        if (!JadeStringUtil.isBlank(forTypeDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_TYPE_DEMANDE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypeDemande);
        }

        if (!JadeStringUtil.isBlank(forEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forEtatDemande);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new PRDemande();
    }

    /**
     * @return
     */
    public String getForEtatDemande() {
        return forEtatDemande;
    }

    /**
     * getter pour l'attribut for id tiers
     * 
     * @return la valeur courante de l'attribut for id tiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * getter pour l'attribut for type demande
     * 
     * @return la valeur courante de l'attribut for type demande
     */
    public String getForTypeDemande() {
        return forTypeDemande;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return PRDemande.FIELDNAME_IDDEMANDE;
    }

    /**
     * @param string
     */
    public void setForEtatDemande(String string) {
        forEtatDemande = string;
    }

    /**
     * setter pour l'attribut for id tiers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    /**
     * setter pour l'attribut for type demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeDemande(String string) {
        forTypeDemande = string;
    }

}
