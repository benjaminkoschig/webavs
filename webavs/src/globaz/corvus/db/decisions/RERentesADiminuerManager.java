package globaz.corvus.db.decisions;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de rechercher les rentes à diminuer lors de la préparation d'une décision depuis une demande.
 */
public class RERentesADiminuerManager extends BManager implements BIGenericManager<RERentesADiminuer> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final String ALIAS_BASE_CALCUL_DEMANDE_EN_TRAITEMENT = "base_calcul_demande";
    static final String ALIAS_BASE_CALCUL_RENTE_FAMILLE = "base_calcul_prestation_famille";
    static final String ALIAS_DEMANDE_RENTE_EN_TRAITEMENT = "demande_en_traitement";
    static final String ALIAS_DEMANDE_RENTE_FAMILLE = "demande_prestation_famille";
    static final String ALIAS_MEMBRE_FAMILLE_CONJOINT = "membre_famille_conjoint";
    static final String ALIAS_MEMBRE_FAMILLE_ENFANT = "membre_famille_enfant";
    static final String ALIAS_MEMBRE_FAMILLE_REQUERANT = "membre_famille_requerant";
    static final String ALIAS_PRESTATION_ACCORDEE_DEMANDE_EN_RENTE = "prestation_demande";
    static final String ALIAS_PRESTATION_ACCORDEE_FAMILLE = "prestation_famille";
    static final String ALIAS_RENTE_ACCORDEE_DEMANDE_EN_TRAITEMENT = "rente_accorde_demande";
    static final String ALIAS_RENTE_ACCORDEE_FAMILLE = "rente_accorde_prestation_famille";
    static final String ALIAS_RENTE_CALCULEE_DEMANDE_EN_TRAITEMENT = "rente_calculee_demande";
    static final String ALIAS_RENTE_CALCULEE_FAMILLE = "rente_calculee_prestation_famille";

    private Long forIdDemandeRente;

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableRenteCalculee = _getCollection() + RERenteCalculee.TABLE_NAME_RENTE_CALCULEE;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableDemandePrestation = _getCollection() + PRDemande.TABLE_NAME;
        String tableMembreSituationFamiliale = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableConjointMembreFamille = _getCollection() + SFConjoint.TABLE_NAME;
        String tableEnfantMembreFamille = _getCollection() + SFEnfant.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDemandeRente).append(" AS ").append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_EN_TRAITEMENT);

        sql.append(" INNER JOIN ").append(tableRenteCalculee).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_RENTE_CALCULEE_DEMANDE_EN_TRAITEMENT);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_EN_TRAITEMENT).append(".")
                .append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append("=")
                .append(RERentesADiminuerManager.ALIAS_RENTE_CALCULEE_DEMANDE_EN_TRAITEMENT).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ").append(tableBaseCalcul).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_BASE_CALCUL_DEMANDE_EN_TRAITEMENT);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_RENTE_CALCULEE_DEMANDE_EN_TRAITEMENT).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE).append("=")
                .append(RERentesADiminuerManager.ALIAS_BASE_CALCUL_DEMANDE_EN_TRAITEMENT).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ").append(tableRenteAccordee).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_RENTE_ACCORDEE_DEMANDE_EN_TRAITEMENT);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_BASE_CALCUL_DEMANDE_EN_TRAITEMENT).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL).append("=")
                .append(RERentesADiminuerManager.ALIAS_RENTE_ACCORDEE_DEMANDE_EN_TRAITEMENT).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        sql.append(" INNER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_DEMANDE_EN_RENTE);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_RENTE_ACCORDEE_DEMANDE_EN_TRAITEMENT).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append("=")
                .append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_DEMANDE_EN_RENTE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableDemandePrestation);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_EN_TRAITEMENT).append(".")
                .append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION).append("=").append(tableDemandePrestation)
                .append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ").append(tableMembreSituationFamiliale).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT);
        sql.append(" ON ").append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append("=")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT).append(".")
                .append(SFMembreFamille.FIELD_IDTIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableConjointMembreFamille);
        sql.append(" ON (");
        sql.append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append("=").append(tableConjointMembreFamille)
                .append(".").append(SFConjoint.FIELD_IDCONJOINT1);
        sql.append(" OR ");
        sql.append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append("=").append(tableConjointMembreFamille)
                .append(".").append(SFConjoint.FIELD_IDCONJOINT2);
        sql.append(")");

        sql.append(" LEFT OUTER JOIN ").append(tableEnfantMembreFamille);
        sql.append(" ON ").append(tableConjointMembreFamille).append(".").append(SFConjoint.FIELD_IDCONJOINTS)
                .append("=").append(tableEnfantMembreFamille).append(".").append(SFEnfant.FIELD_IDCONJOINT);

        sql.append(" LEFT OUTER JOIN ").append(tableMembreSituationFamiliale).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_ENFANT);
        sql.append(" ON ").append(tableEnfantMembreFamille).append(".").append(SFEnfant.FIELD_IDMEMBREFAMILLE)
                .append("=").append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_ENFANT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

        sql.append(" LEFT OUTER JOIN ").append(tableMembreSituationFamiliale).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_CONJOINT);
        sql.append(" ON ((");
        sql.append(tableConjointMembreFamille).append(".").append(SFConjoint.FIELD_IDCONJOINT1).append("=")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_CONJOINT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(" AND NOT ").append(tableConjointMembreFamille).append(".").append(SFConjoint.FIELD_IDCONJOINT1)
                .append("=").append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(") OR (");
        sql.append(tableConjointMembreFamille).append(".").append(SFConjoint.FIELD_IDCONJOINT2).append("=")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_CONJOINT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(" AND NOT ").append(tableConjointMembreFamille).append(".").append(SFConjoint.FIELD_IDCONJOINT2)
                .append("=").append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append("))");

        sql.append(" INNER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE);
        sql.append(" ON (");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_REQUERANT).append(".")
                .append(SFMembreFamille.FIELD_IDTIERS);
        sql.append(" OR ").append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_CONJOINT).append(".")
                .append(SFMembreFamille.FIELD_IDTIERS);
        sql.append(" OR ").append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(RERentesADiminuerManager.ALIAS_MEMBRE_FAMILLE_ENFANT).append(".")
                .append(SFMembreFamille.FIELD_IDTIERS);
        sql.append(")");

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=").append(tableTiers)
                .append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tableRenteAccordee).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_RENTE_ACCORDEE_FAMILLE);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                .append(RERentesADiminuerManager.ALIAS_RENTE_ACCORDEE_FAMILLE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableBaseCalcul).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_BASE_CALCUL_RENTE_FAMILLE);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_RENTE_ACCORDEE_FAMILLE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL).append("=")
                .append(RERentesADiminuerManager.ALIAS_BASE_CALCUL_RENTE_FAMILLE).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        sql.append(" INNER JOIN ").append(tableRenteCalculee).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_RENTE_CALCULEE_FAMILLE);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_BASE_CALCUL_RENTE_FAMILLE).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE).append("=")
                .append(RERentesADiminuerManager.ALIAS_RENTE_CALCULEE_FAMILLE).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ").append(tableDemandeRente).append(" AS ")
                .append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_FAMILLE);
        sql.append(" ON ").append(RERentesADiminuerManager.ALIAS_RENTE_CALCULEE_FAMILLE).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE).append("=")
                .append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_FAMILLE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        if (getForIdDemandeRente() == null) {
            throw new RETechnicalException("[forIdDemandeRente] can't be null");
        }

        StringBuilder sql = new StringBuilder();

        sql.append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_EN_TRAITEMENT).append(".")
                .append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append("=").append(getForIdDemandeRente());
        sql.append(" AND NOT ").append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_EN_TRAITEMENT).append(".")
                .append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append("=")
                .append(RERentesADiminuerManager.ALIAS_DEMANDE_RENTE_FAMILLE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(" AND (");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append("=0");
        sql.append(" OR ");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL");
        sql.append(")");

        sql.append(" AND ").append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" IN(")
                .append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",").append(IREPrestationAccordee.CS_ETAT_PARTIEL)
                .append(")");

        sql.append(" AND NOT ").append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append("=0.0");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERentesADiminuer();
    }

    @Override
    public List<RERentesADiminuer> getContainerAsList() {
        List<RERentesADiminuer> list = new ArrayList<RERentesADiminuer>();
        for (int i = 0; i < size(); i++) {
            list.add((RERentesADiminuer) get(i));
        }
        return list;
    }

    public Long getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public void setForIdDemandeRente(Long forIdDemandeRente) {
        this.forIdDemandeRente = forIdDemandeRente;
    }
}
