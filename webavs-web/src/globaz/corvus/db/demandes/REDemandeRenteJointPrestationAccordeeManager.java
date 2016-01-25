package globaz.corvus.db.demandes;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager dédié aux {@link REDemandeRenteJointPrestationAccordee} <br/>
 * Il a été nécessaire de spécialiser {@link REDemandeRenteJointDemandeManager} <br/>
 * afin de pouvoir afficher les codes de prestations, au dessous du genre de prestation, de manière optimisée <br/>
 * (pour éviter une requête par ligne, synonyme de gros ralentissements)
 * 
 * @author PBA
 * 
 */
public class REDemandeRenteJointPrestationAccordeeManager extends REDemandeRenteJointDemandeManager implements
        BIGenericManager<REDemandeRenteJointPrestationAccordee> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatPrestation = "";
    private String forCsEtatPrestationIn = "";
    private String forCsEtatPrestationNotIn = "";
    private String forIdTiersBeneficiaire = "";

    public REDemandeRenteJointPrestationAccordeeManager() {
    }

    /**
     * {@inheritDoc} <br/>
     * <br/>
     * Ajout des champs "Genre de prestation accordée", "Code de prestation" et "Date de fin de droit"
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();

        String superFields = super._getFields(statement);
        if (!"*".equals(superFields)) {
            fields.append(superFields).append(",");
        }
        fields.append(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(",");

        // BZ 5432
        fields.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE).append(",");
        fields.append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(",");
        fields.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(",");

        fields.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(",");
        fields.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(",");
        fields.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(",");
        fields.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(",");
        fields.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);

        return fields.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String schema = _getCollection();
        String prestationAccorde = schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;

        String superWhere = super._getWhere(statement);
        if (!JadeStringUtil.isBlank(superWhere)) {
            sql.append(superWhere);
        }

        if (!JadeStringUtil.isBlank(forIdTiersBeneficiaire)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(prestationAccorde).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sql.append("=");
            sql.append(forIdTiersBeneficiaire);
        }

        if (!JadeStringUtil.isBlank(forCsEtatPrestation)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(prestationAccorde).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append("=");
            sql.append(forCsEtatPrestation);
        }

        if (!JadeStringUtil.isBlank(forCsEtatPrestationIn)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(prestationAccorde).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append(" IN(").append(forCsEtatPrestationIn).append(")");
        }

        if (!JadeStringUtil.isBlank(forCsEtatPrestationNotIn)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(prestationAccorde).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append(" NOT IN(").append(forCsEtatPrestationNotIn).append(")");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteJointPrestationAccordee();
    }

    @Override
    public List<REDemandeRenteJointPrestationAccordee> getContainerAsList() {
        List<REDemandeRenteJointPrestationAccordee> list = new ArrayList<REDemandeRenteJointPrestationAccordee>();

        for (int i = 0; i < size(); i++) {
            list.add((REDemandeRenteJointPrestationAccordee) get(i));
        }

        return list;
    }

    public String getForCsEtatPrestation() {
        return forCsEtatPrestation;
    }

    public String getForCsEtatPrestationIn() {
        return forCsEtatPrestationIn;
    }

    public String getForCsEtatPrestationNotIn() {
        return forCsEtatPrestationNotIn;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    @Override
    protected String getFromClauseFromEntity(String schema) {
        return REDemandeRenteJointPrestationAccordee.createFromClause(schema);
    }

    @Override
    public String getOrderByDefaut() {
        StringBuilder orderBy = new StringBuilder();

        orderBy.append(REDemandeRenteJointDemande.FIELDNAME_NOM).append(",");
        orderBy.append(REDemandeRenteJointDemande.FIELDNAME_PRENOM).append(",");
        orderBy.append(REDemandeRenteJointDemandeManager.ALIAS_DATE_FIN).append(" DESC,");
        orderBy.append(REDemandeRente.FIELDNAME_DATE_DEBUT).append(" DESC");

        return orderBy.toString();
    }

    public void setForCsEtatPrestation(String forCsEtatPrestation) {
        this.forCsEtatPrestation = forCsEtatPrestation;
    }

    public void setForCsEtatPrestationIn(String forCsEtatPrestationIn) {
        this.forCsEtatPrestationIn = forCsEtatPrestationIn;
    }

    public void setForCsEtatPrestationNotIn(String forCsEtatPrestationNotIn) {
        this.forCsEtatPrestationNotIn = forCsEtatPrestationNotIn;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }
}
