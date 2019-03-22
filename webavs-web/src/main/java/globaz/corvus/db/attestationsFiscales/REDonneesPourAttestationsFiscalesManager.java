package globaz.corvus.db.attestationsFiscales;

import ch.globaz.corvus.process.attestationsfiscales.REAgregateurDonneesPourAttestationsFiscales;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * Permet la recherche des données nécessaires à la génération des attestations fiscales des rentes. Ces données sont
 * brutes et non utilisables en l'état. Pour réorganiser ces données par attestation (par famille de la situation
 * familiale), utilisez l'utilitaire {@link REAgregateurDonneesPourAttestationsFiscales}
 *
 * @author PBA
 */
public class REDonneesPourAttestationsFiscalesManager extends BManager {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean filtrerParDateDeDecision;
    private String forAnnee;
    private String forIdTiersBaseCalcul;
    private boolean isTiersBeneficiaire;
    private String forNssA;
    private String forNssDe;

    public REDonneesPourAttestationsFiscalesManager() {
        super();

        filtrerParDateDeDecision = true;
        forAnnee = null;
        forIdTiersBaseCalcul = null;
        isTiersBeneficiaire = false;
        forNssA = null;
        forNssDe = null;
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "DISTINCT " + super._getFields(statement);
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.DESIGNATION_2);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;

        if (JadeStringUtil.isBlank(forAnnee)) {
            throw new NullPointerException("must have a year for this request");
        }

        // on ne prend que les rentes avec date de début du droit avant l'année fiscale (ou le 1er mois de l'année si
        // décision en décembre)
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        sql.append("<");
        sql.append(Integer.parseInt(forAnnee) + 1).append("00");

        sql.append(" AND ");
        // et qui n'ont pas de date de fin de droit antérieure à l'année fiscale
        sql.append("(");
        sql.append(getSqlForDateFinDeDroitVideOuPlusGrandeOuEgaleA(forAnnee));
        sql.append(")");

        if (isFiltrerParDateDeDecision()) {
            sql.append(" AND (");
            // et dont la date de la décision est antérieure ou égale à l'année fiscale
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_DECISION);
            sql.append("<=");
            sql.append(forAnnee).append("1231");
            sql.append(" OR ");
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_DECISION);
            sql.append(" is NULL )");

        }

        sql.append(" AND ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        sql.append(" IN(").append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",")
                .append(IREPrestationAccordee.CS_ETAT_DIMINUE).append(")");

        if (!JadeStringUtil.isBlank(forNssDe) || !JadeStringUtil.isBlank(forNssA)) {
            sql.append(" AND ");
            sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL).append(".")
                    .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" BETWEEN ");
        }

        if (!JadeStringUtil.isBlank(forNssDe) && !JadeStringUtil.isBlank(forNssA)) {
            sql.append("'").append(forNssDe).append("'");
            sql.append(" AND ");
            sql.append("'").append(forNssA).append("'");
        } else if (!JadeStringUtil.isBlank(forNssA)) {
            sql.append("'756.0000.0000.00'");
            sql.append(" AND ");
            sql.append("'").append(forNssA).append("'");
        } else if (!JadeStringUtil.isBlank(forNssDe)) {
            sql.append("'").append(forNssDe).append("'");
            sql.append(" AND ");
            sql.append("'756.9999.999.99'");
        }

        if (!JadeStringUtil.isBlankOrZero(forIdTiersBaseCalcul)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(" ( ");
            sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                    .append(ITITiersDefTable.ID_TIERS).append("=").append(forIdTiersBaseCalcul);
            if (isTiersBeneficiaire) {
                sql.append(" OR ");
                sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                        .append(ITITiersDefTable.ID_TIERS).append("=").append(forIdTiersBaseCalcul);
            }
            sql.append(" ) ");
        }

        return sql.toString();
    }

    @Override
    protected REDonneesPourAttestationsFiscales _newEntity() throws Exception {
        return new REDonneesPourAttestationsFiscales();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdTiersBaseCalcul() {
        return forIdTiersBaseCalcul;
    }

    public String getForNssA() {
        return forNssA;
    }

    public String getForNssDe() {
        return forNssDe;
    }

    private String getSqlForDateFinDeDroitVideOuPlusGrandeOuEgaleA(String annee) {
        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append("=0");
        sql.append(" OR ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(" IS NULL");
        sql.append(" OR ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(">=").append(annee).append("01");

        return sql.toString();
    }

    public boolean isFiltrerParDateDeDecision() {
        return filtrerParDateDeDecision;
    }

    public void setFiltrerParDateDeDecision(boolean filtrerParDateDeDecision) {
        this.filtrerParDateDeDecision = filtrerParDateDeDecision;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdTiersBaseCalcul(String idTiers) {
        forIdTiersBaseCalcul = idTiers;
    }

    public void setForNssA(String forNssA) {
        this.forNssA = forNssA;
    }

    public void setForNssDe(String forNssDe) {
        this.forNssDe = forNssDe;
    }

    public boolean getIsTiersBeneficiaire() {
        return isTiersBeneficiaire;
    }

    public void setIsTiersBeneficiaire(boolean isTiersBeneficiaire) {
        this.isTiersBeneficiaire = isTiersBeneficiaire;
    }
}
