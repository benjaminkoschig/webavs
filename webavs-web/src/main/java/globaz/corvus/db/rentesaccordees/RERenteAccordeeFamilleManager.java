package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.hera.business.constantes.ISFRelationConjoint;
import ch.globaz.prestation.domaine.CodePrestation;

public class RERenteAccordeeFamilleManager extends BManager implements BIGenericManager<RERenteAccordeeFamille> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private enum OrderBy {
        CodePrestation;
    }

    private String forDateFinDroitPlusGrandeOuEgale;
    private String forIdTiersLiant;
    private boolean inclureRentesConjoints;
    private boolean inclureRentesConjointsDivorces;
    private boolean inclureRentesEnfants;
    private OrderBy orderBy;
    private boolean seulementRenteEnCours;
    private boolean exclureRenteAutreQueSurvivant;
    private boolean exclureRentesSurvivant;

    public RERenteAccordeeFamilleManager() {
        super();

        exclureRenteAutreQueSurvivant = false;
        exclureRentesSurvivant = false;

        forDateFinDroitPlusGrandeOuEgale = "";
        forIdTiersLiant = "";

        inclureRentesConjoints = true;
        inclureRentesConjointsDivorces = true;
        inclureRentesEnfants = true;

        orderBy = null;

        seulementRenteEnCours = true;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (orderBy != null) {
            StringBuilder sql = new StringBuilder();

            switch (orderBy) {
                case CodePrestation:
                    sql.append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
                    break;
            }

            if (sql.length() > 0) {
                return sql.toString();
            }
        }
        return null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;

        String tableMembreFamille = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableConjoint = _getCollection() + SFConjoint.TABLE_NAME;
        String tableEnfant = _getCollection() + SFEnfant.TABLE_NAME;
        String tableRelationConjugale = _getCollection() + SFRelationConjoint.TABLE_NAME;

        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        String aliasTableTiersEnfant = "enfant";
        String aliasTableTiersConjoint = "conjoint";
        String aliasTableMembreFamilleParent = "membreParent";
        String aliasTableMembreFamilleEnfant = "membreEnfant";
        String aliasTableMembreFamilleRequerant = "membreRequerant";
        String aliasTableMembreFamilleConjoint = "membreConjoint";

        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlankOrZero(forIdTiersLiant)) {

            sql.append("(");

            if (inclureRentesEnfants) {
                sql.append(tablePrestationAccordee).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
                sql.append(" IN (");

                sql.append("SELECT DISTINCT ");
                sql.append(aliasTableTiersEnfant).append(".").append(ITITiersDefTable.ID_TIERS);
                sql.append(" FROM ");
                sql.append(tableMembreFamille).append(" AS ").append(aliasTableMembreFamilleParent);

                sql.append(" INNER JOIN ");
                sql.append(tableConjoint);
                sql.append(" ON ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT1).append("=")
                        .append(aliasTableMembreFamilleParent).append(".")
                        .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
                sql.append(" OR ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT2).append("=")
                        .append(aliasTableMembreFamilleParent).append(".")
                        .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

                sql.append(" INNER JOIN ");
                sql.append(tableEnfant);
                sql.append(" ON ");
                sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDCONJOINT);
                sql.append("=");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINTS);

                if (!inclureRentesConjointsDivorces) {
                    sql.append(" INNER JOIN ");
                    sql.append(" ON ");
                    sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_IDCONJOINTS);
                    sql.append("=");
                    sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINTS);
                }

                sql.append(" INNER JOIN ");
                sql.append(tableMembreFamille).append(" AS ").append(aliasTableMembreFamilleEnfant);
                sql.append(" ON ");
                sql.append(aliasTableMembreFamilleEnfant).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
                sql.append("=");
                sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDMEMBREFAMILLE);

                sql.append(" INNER JOIN ");
                sql.append(tableTiers).append(" AS ").append(aliasTableTiersEnfant);
                sql.append(" ON ");
                sql.append(aliasTableTiersEnfant).append(".").append(ITITiersDefTable.ID_TIERS);
                sql.append("=");
                sql.append(aliasTableMembreFamilleEnfant).append(".").append(SFMembreFamille.FIELD_IDTIERS);

                sql.append(" WHERE ");
                if (!inclureRentesConjointsDivorces) {
                    sql.append("NOT ");
                    sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_TYPERELATION);
                    sql.append("=");
                    sql.append(ISFRelationConjoint.CS_REL_CONJ_DIVORCE);

                    sql.append(" AND ");
                }
                sql.append(aliasTableMembreFamilleParent).append(".").append(SFMembreFamille.FIELD_IDTIERS);
                sql.append("=");
                sql.append(forIdTiersLiant);

                sql.append(")");

                sql.append(" OR ");
            }

            if (inclureRentesConjoints) {
                sql.append(tablePrestationAccordee).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
                sql.append(" IN (");

                sql.append("SELECT DISTINCT ");
                sql.append(aliasTableTiersConjoint).append(".").append(ITITiersDefTable.ID_TIERS);
                sql.append(" FROM ");
                sql.append(tableMembreFamille).append(" AS ").append(aliasTableMembreFamilleRequerant);

                sql.append(" INNER JOIN ");
                sql.append(tableConjoint);
                sql.append(" ON ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT1).append("=")
                        .append(aliasTableMembreFamilleRequerant).append(".")
                        .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
                sql.append(" OR ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT2).append("=")
                        .append(aliasTableMembreFamilleRequerant).append(".")
                        .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

                if (!inclureRentesConjointsDivorces) {
                    sql.append(" INNER JOIN ");
                    sql.append(" ON ");
                    sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_IDCONJOINTS);
                    sql.append("=");
                    sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINTS);
                }

                sql.append(" INNER JOIN ");
                sql.append(tableMembreFamille).append(" AS ").append(aliasTableMembreFamilleConjoint);
                sql.append(" ON CASE WHEN ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT1).append("=")
                        .append(aliasTableMembreFamilleRequerant).append(".")
                        .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
                sql.append(" THEN ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT2);
                sql.append(" ELSE ");
                sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT1);
                sql.append(" END = ");
                sql.append(aliasTableMembreFamilleConjoint).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
                sql.append(" INNER JOIN ");
                sql.append(tableTiers).append(" AS ").append(aliasTableTiersConjoint);
                sql.append(" ON ");
                sql.append(aliasTableTiersConjoint).append(".").append(ITITiersDefTable.ID_TIERS);
                sql.append("=");
                sql.append(aliasTableMembreFamilleConjoint).append(".").append(SFMembreFamille.FIELD_IDTIERS);

                sql.append(" WHERE ");
                if (!inclureRentesConjointsDivorces) {
                    sql.append("NOT ");
                    sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_TYPERELATION);
                    sql.append("=");
                    sql.append(ISFRelationConjoint.CS_REL_CONJ_DIVORCE);

                    sql.append(" AND ");
                }
                sql.append(aliasTableMembreFamilleRequerant).append(".").append(SFMembreFamille.FIELD_IDTIERS);
                sql.append("=");
                sql.append(forIdTiersLiant);
                sql.append(")");

                sql.append(" OR ");
            }

            sql.append(tablePrestationAccordee).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sql.append("=");
            sql.append(forIdTiersLiant);

            sql.append(")");

            if (seulementRenteEnCours || !JadeStringUtil.isBlank(forDateFinDroitPlusGrandeOuEgale)) {
                sql.append(" AND (");

                if (seulementRenteEnCours) {
                    sql.append(tablePrestationAccordee).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
                    sql.append(" IS NULL");
                    sql.append(" OR ");
                    sql.append(tablePrestationAccordee).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
                    sql.append("=0");
                }

                if (!JadeStringUtil.isBlank(forDateFinDroitPlusGrandeOuEgale)) {
                    if (seulementRenteEnCours) {
                        sql.append(" OR ");
                    }
                    sql.append(tablePrestationAccordee).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
                    sql.append(">=");
                    sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDateFinDroitPlusGrandeOuEgale));
                }

                sql.append(")");
            }

            if (seulementRenteEnCours) {
                sql.append(" AND ");
                sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT)
                        .append(" IN(");
                sql.append(IREPrestationAccordee.CS_ETAT_DIMINUE).append(",");
                sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
                sql.append(IREPrestationAccordee.CS_ETAT_VALIDE);
                sql.append(")");
            }

            if (exclureRenteAutreQueSurvivant) {
                StringBuilder codePrestSurvivant = new StringBuilder();
                for (CodePrestation unCodePrestation : CodePrestation.values()) {
                    if (unCodePrestation.isSurvivant() || unCodePrestation.isAPIAVS()) {
                        if (codePrestSurvivant.length() > 0) {
                            codePrestSurvivant.append(",");
                        }
                        codePrestSurvivant.append("'").append(unCodePrestation.getCodePrestation()).append("'");
                    }
                }
                sql.append(" AND ");
                sql.append(tablePrestationAccordee).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" IN(")
                        .append(codePrestSurvivant.toString()).append(")");
            }
            if (exclureRentesSurvivant) {
                StringBuilder codePrestNonSurvivant = new StringBuilder();
                for (CodePrestation unCodePrestation : CodePrestation.values()) {
                    if (unCodePrestation.isVieillesse() || unCodePrestation.isAI() || unCodePrestation.isAPI()) {
                        if (codePrestNonSurvivant.length() > 0) {
                            codePrestNonSurvivant.append(",");
                        }
                        codePrestNonSurvivant.append("'").append(unCodePrestation.getCodePrestation()).append("'");
                    }
                }
                sql.append(" AND ");
                sql.append(tablePrestationAccordee).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" IN(")
                        .append(codePrestNonSurvivant.toString()).append(")");
            }
        }
        return sql.toString();
    };

    @Override
    protected RERenteAccordeeFamille _newEntity() throws Exception {
        return new RERenteAccordeeFamille();
    }

    @Override
    public List<RERenteAccordeeFamille> getContainerAsList() {
        List<RERenteAccordeeFamille> list = new ArrayList<RERenteAccordeeFamille>();
        for (int i = 0; i < size(); i++) {
            list.add((RERenteAccordeeFamille) get(i));
        }
        return list;
    }

    public String getForDateFinDroitPlusGrandeOuEgale() {
        return forDateFinDroitPlusGrandeOuEgale;
    }

    public String getForIdTiersLiant() {
        return forIdTiersLiant;
    }

    public boolean isInclureRentesConjoints() {
        return inclureRentesConjoints;
    }

    public boolean isInclureRentesConjointsDivorces() {
        return inclureRentesConjointsDivorces;
    }

    public boolean isInclureRentesEnfants() {
        return inclureRentesEnfants;
    }

    public boolean isSeulementRenteEnCours() {
        return seulementRenteEnCours;
    }

    public void setForDateFinDroitPlusGrandeOuEgale(String forDateFinDroitPlusGrandeOuEgale) {
        this.forDateFinDroitPlusGrandeOuEgale = forDateFinDroitPlusGrandeOuEgale;
    }

    public void setForIdTiersLiant(String forIdTiersLiant) {
        this.forIdTiersLiant = forIdTiersLiant;
    }

    public void setInclureRentesConjoints(boolean inclureRentesConjoints) {
        this.inclureRentesConjoints = inclureRentesConjoints;
    }

    public void setInclureRentesConjointsDivorces(boolean inclureRentesConjointsDivorces) {
        this.inclureRentesConjointsDivorces = inclureRentesConjointsDivorces;
    }

    public void setInclureRentesEnfants(boolean inclureRentesEnfants) {
        this.inclureRentesEnfants = inclureRentesEnfants;
    }

    public void setOrderByCodePrestation() {
        orderBy = OrderBy.CodePrestation;
    }

    public void setSeulementRenteEnCours(boolean seulementRenteEnCours) {
        this.seulementRenteEnCours = seulementRenteEnCours;
    }

    public void setExclureRentesAutreQueSurvivant(boolean exclure) {
        exclureRenteAutreQueSurvivant = exclure;
    }

    public boolean isExclureRenteAutreQueSurvivant() {
        return exclureRenteAutreQueSurvivant;
    }

    public void setExclureRentesSurvivant(boolean exclure) {
        exclureRentesSurvivant = exclure;
    }

    public boolean isExclureRentesSurvivant() {
        return exclureRentesSurvivant;
    }
}
