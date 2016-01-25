package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsJointTiersManager;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class REDecisionJointDemandeRenteListViewBean extends REDecisionJointDemandeRenteManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // BZ 5181 - Juste pour l'écran de validation des décisions
    private int nbDecisionInvalidables = 0;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");

        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_LOT).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE).append(", ");
        sql.append(_getCollection()).append(REPrestations.TABLE_NAME_PRESTATION).append(".")
                .append(REPrestations.FIELDNAME_MONTANT_PRESTATION).append(", ");

        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_ID_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_TYPE_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DATE_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_ETAT).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_PREPARE_PAR).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DATE_PREPARATION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DATE_VALIDATION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_VALIDE_PAR).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_ID_TIER_ADR_COURRIER).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_REMARQUE_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_REM_ANNULE_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_REM_RED_PLAFOND).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_REM_SUPP_VEUF).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_GENRE_DECISION).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_TRAITER_PAR).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_TEXTE_OBLIG_PAYER_COTI).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_AVEC_BONNE_FOI).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_SANS_BONNE_FOI).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DATE_FIN_RETRO).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DATE_DEBUT_RETRO).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_EMAIL_ADRESSE).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_DECISION_DEPUIS).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_UID).append(", ");
        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                .append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);

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
        return new REDecisionJointDemandeRenteViewBean();
    }

    public int getNbDecisionInvalidables() {
        return nbDecisionInvalidables;
    }

    public void setNbDecisionInvalidables(int nbDecisionInvalidables) {
        this.nbDecisionInvalidables = nbDecisionInvalidables;
    }

}
