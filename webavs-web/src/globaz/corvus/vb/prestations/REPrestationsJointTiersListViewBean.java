/*
 * Créé le 30 juil. 07
 */
package globaz.corvus.vb.prestations;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsJointTiersManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCR
 * 
 */
public class REPrestationsJointTiersListViewBean extends REPrestationsJointTiersManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        // Prévu que pour l'écran de recherche -> n'impacte pas l'écran de
        // détail ou le retrieve de l'entité

        StringBuffer sql = new StringBuffer();

        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_TIERS).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_ID_TIERS_TI).append(", ");
        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_TIERS).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_NOM).append(", ");
        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_TIERS).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_PRENOM).append(", ");
        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_TIERS).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_NATIONALITE).append(", ");

        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_PERSONNE).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_SEXE).append(", ");
        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_PERSONNE).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_DATENAISSANCE).append(", ");
        sql.append(_getCollection()).append(REPrestationsJointTiersManager.TABLE_AVS).append(".")
                .append(REPrestationsJointTiersManager.FIELDNAME_NUM_AVS).append(", ");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(", ");

        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_TYPE_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DATE_FIN_RETRO).append(", ");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");

        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_LOT).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ETAT).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_MOIS_ANNEE).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_TYPE).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_MONTANT_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_DECISION);

        return sql.toString();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationsJointTiersViewBean();
    }

}
