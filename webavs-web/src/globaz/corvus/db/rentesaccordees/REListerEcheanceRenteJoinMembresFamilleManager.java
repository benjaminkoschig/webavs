package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import ch.globaz.hera.business.constantes.ISFPeriode;

/**
 * Retourne des données nécessaires pour le processus REListerEcheanceProcess<br/>
 * Utilisé pour optimiser le processus des échéances des rentes.
 * 
 * @author PCA
 */
public class REListerEcheanceRenteJoinMembresFamilleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_CONJOINT_MEMBRE_FAMILLE = "leconjoint_mf";
    public static final String ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_NAISSANCE = "dateNaissanceConjoint2";

    public static final String ALIAS_CONJOINT_TIERS = "leconjoint_ti";
    public static final String ALIAS_CONJOINT_TIERS_CS_SEXE = "sexeConjoint";
    public static final String ALIAS_CONJOINT_TIERS_DATE_NAISSANCE = "dateNaissanceConjoint1";
    public static final String ALIAS_CONJOINT_TIERS_ID_TIERS = "idTiersConjoint";

    public static final String ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE = "membre_b";
    public static final String ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_DOMAINE_APPLICATION = "amb_domaine";
    public static final String ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_ID_MEMBRE_FAMILLE = "amb_idmf1";
    public static final String ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_ID_TIERS = "amb_idti1";

    public static final String ALIAS_TABLE_TIERS_PERSONNE_BENEFICIAIRE = "tipersp_benef";

    // Format : mm.aaaa
    private String forDateTraitement = "";

    @Override
    protected String _getFields(BStatement statement) {
        String POINT = ".";
        String VIRGULE = ", ";
        String AS = " AS ";

        StringBuilder sql = new StringBuilder();

        sql.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(VIRGULE);
        sql.append(RERenteAccordee.FIELDNAME_DATE_REVOCATION_AJOURNEMENT).append(VIRGULE);
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(VIRGULE);
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(VIRGULE);
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(VIRGULE);
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(VIRGULE);
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(VIRGULE);

        sql.append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(VIRGULE);

        sql.append(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE).append(VIRGULE);

        sql.append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(VIRGULE);

        sql.append(ITITiersDefTable.DESIGNATION_1).append(VIRGULE);
        sql.append(ITITiersDefTable.DESIGNATION_2).append(VIRGULE);
        sql.append(ITITiersDefTable.ID_PAYS).append(VIRGULE);

        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_TABLE_TIERS_PERSONNE_BENEFICIAIRE)
                .append(POINT).append(ITIPersonneDefTable.DATE_NAISSANCE).append(VIRGULE);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_TABLE_TIERS_PERSONNE_BENEFICIAIRE)
                .append(POINT).append(ITIPersonneDefTable.DATE_DECES).append(VIRGULE);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_TABLE_TIERS_PERSONNE_BENEFICIAIRE)
                .append(POINT).append(ITIPersonneDefTable.CS_SEXE).append(VIRGULE);

        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE).append(POINT)
                .append(SFMembreFamille.FIELD_IDTIERS).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_ID_TIERS)
                .append(VIRGULE);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE)
                .append(POINT)
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE)
                .append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_ID_MEMBRE_FAMILLE)
                .append(VIRGULE);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE)
                .append(POINT)
                .append(SFMembreFamille.FIELD_DOMAINE_APPLICATION)
                .append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_DOMAINE_APPLICATION)
                .append(VIRGULE);

        sql.append(SFPeriode.FIELD_TYPE).append(VIRGULE);
        sql.append(SFPeriode.FIELD_DATEDEBUT).append(VIRGULE);
        sql.append(SFPeriode.FIELD_DATEFIN).append(VIRGULE);

        sql.append(SFConjoint.FIELD_IDCONJOINTS).append(VIRGULE);
        sql.append(SFConjoint.FIELD_IDCONJOINT1).append(VIRGULE);
        sql.append(SFConjoint.FIELD_IDCONJOINT2).append(VIRGULE);

        sql.append(SFRelationConjoint.FIELD_IDRELATIONCONJOINT).append(VIRGULE);
        sql.append(SFRelationConjoint.FIELD_DATEDEBUT).append(VIRGULE);
        sql.append(SFRelationConjoint.FIELD_DATEFIN).append(VIRGULE);
        sql.append(SFRelationConjoint.FIELD_TYPERELATION).append(VIRGULE);

        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS).append(POINT)
                .append(ITITiersDefTable.ID_TIERS).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS_ID_TIERS).append(VIRGULE);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS).append(POINT)
                .append(ITIPersonneDefTable.CS_SEXE).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS_CS_SEXE).append(VIRGULE);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS).append(POINT)
                .append(ITIPersonneDefTable.DATE_NAISSANCE).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS_DATE_NAISSANCE)
                .append(VIRGULE);

        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(POINT)
                .append(SFMembreFamille.FIELD_DATENAISSANCE).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_NAISSANCE)
                .append(VIRGULE);

        sql.append(REDemandeRente.FIELDNAME_CS_ETAT).append(VIRGULE);

        sql.append(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String INNER_JOIN = " INNER JOIN ";
        String LEFT_JOIN = " LEFT OUTER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String POINT = ".";
        String IN = " IN ";
        String AS = " AS ";

        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection());
        sql.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(ON).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection()).append(ITITiersDefTable.TABLE_NAME);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(POINT)
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(INNER_JOIN).append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME).append(POINT)
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(INNER_JOIN).append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_TABLE_TIERS_PERSONNE_BENEFICIAIRE);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_TABLE_TIERS_PERSONNE_BENEFICIAIRE)
                .append(POINT).append(ITITiersDefTable.ID_TIERS);

        sql.append(INNER_JOIN).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(ON).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // BZ 5492
        sql.append(INNER_JOIN);
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(ON);
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append(EGAL);
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(LEFT_JOIN);
        sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME);
        sql.append(ON);
        sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME).append(".")
                .append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL);
        sql.append(EGAL);
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE);

        sql.append(INNER_JOIN).append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE);
        sql.append(ON).append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE)
                .append(POINT).append(SFMembreFamille.FIELD_IDTIERS);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        sql.append(LEFT_JOIN).append(_getCollection()).append(SFPeriode.TABLE_NAME);
        sql.append(ON).append("(")
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE).append(POINT)
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(EGAL).append(_getCollection()).append(SFPeriode.TABLE_NAME).append(POINT)
                .append(SFPeriode.FIELD_IDMEMBREFAMILLE);
        sql.append(AND).append(_getCollection()).append(SFPeriode.TABLE_NAME).append(POINT)
                .append(SFPeriode.FIELD_TYPE);
        // BZ 5195, ajout du code système pour le certificat de vie
        sql.append(IN).append("(").append(ISFPeriode.CS_TYPE_PERIODE_ETUDE).append(", ")
                .append(ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE).append("))");

        sql.append(LEFT_JOIN).append(_getCollection()).append(SFConjoint.TABLE_NAME);
        sql.append(ON).append("(")
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE).append(POINT)
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(EGAL).append(_getCollection()).append(SFConjoint.TABLE_NAME).append(POINT)
                .append(SFConjoint.FIELD_IDCONJOINT1).append(OR);
        sql.append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE).append(POINT)
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(EGAL).append(_getCollection())
                .append(SFConjoint.TABLE_NAME).append(POINT).append(SFConjoint.FIELD_IDCONJOINT2).append(")");

        String dateMariage;
        try {
            JACalendarGregorian jaCalGre = new JACalendarGregorian();

            JADate dateTraitement = new JADate(getForDateTraitement());

            dateMariage = JACalendar.format(
                    jaCalGre.addDays(
                            jaCalGre.addMonths(
                                    new JADate("01."
                                            + (Integer.toString(dateTraitement.getMonth()).length() < 2 ? "0"
                                                    + Integer.toString(dateTraitement.getMonth()) + "."
                                                    + Integer.toString(dateTraitement.getYear()) : Integer
                                                    .toString(dateTraitement.getMonth())
                                                    + "."
                                                    + Integer.toString(dateTraitement.getYear()))), 1), -1),
                    JACalendar.FORMAT_YYYYMMDD);
        } catch (Exception ex) {
            dateMariage = getForDateTraitement();
        }

        sql.append(LEFT_JOIN).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME);
        sql.append(ON).append("(").append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(POINT)
                .append(SFRelationConjoint.FIELD_IDCONJOINTS);
        sql.append(EGAL).append(_getCollection()).append(SFConjoint.TABLE_NAME).append(POINT)
                .append(SFConjoint.FIELD_IDCONJOINTS);
        sql.append(AND).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(POINT)
                .append(SFRelationConjoint.FIELD_TYPERELATION).append(EGAL).append(36001001);
        sql.append(AND).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(POINT)
                .append(SFRelationConjoint.FIELD_DATEDEBUT).append("<=").append(dateMariage);
        sql.append(AND).append("(").append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(POINT)
                .append(SFRelationConjoint.FIELD_DATEFIN).append(" > ").append(dateMariage);
        sql.append(OR).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(POINT)
                .append(SFRelationConjoint.FIELD_DATEFIN).append(" is NULL ");
        sql.append(OR).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(POINT)
                .append(SFRelationConjoint.FIELD_DATEFIN).append(EGAL).append(0).append("))");

        sql.append(LEFT_JOIN).append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE);
        sql.append(ON).append("(").append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE)
                .append(POINT).append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(EGAL).append(_getCollection()).append(SFConjoint.TABLE_NAME).append(POINT)
                .append(SFConjoint.FIELD_IDCONJOINT2);
        sql.append(OR).append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE)
                .append(POINT).append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(EGAL).append(_getCollection()).append(SFConjoint.TABLE_NAME).append(POINT)
                .append(SFConjoint.FIELD_IDCONJOINT1);
        sql.append(AND).append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE)
                .append(POINT).append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append("<>")
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE).append(POINT)
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(")");

        sql.append(LEFT_JOIN).append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(AS)
                .append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS);
        sql.append(ON).append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS).append(POINT)
                .append(ITITiersDefTable.ID_TIERS);
        sql.append(EGAL).append(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE)
                .append(POINT).append(SFMembreFamille.FIELD_IDTIERS);

        return sql.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        String VIRGULE = ",";

        StringBuilder sql = new StringBuilder();

        sql.append(ITITiersDefTable.DESIGNATION_1).append(VIRGULE);
        sql.append(ITITiersDefTable.DESIGNATION_2).append(VIRGULE);
        sql.append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(VIRGULE);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(VIRGULE);
        sql.append(SFConjoint.FIELD_IDCONJOINTS).append(VIRGULE);
        sql.append(SFRelationConjoint.FIELD_IDRELATIONCONJOINT).append(VIRGULE);
        sql.append(SFPeriode.FIELD_IDPERIODE);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String POINT = ".";
        String IN = " IN ";
        String VIRGULE = ", ";

        StringBuilder sql = new StringBuilder();

        sql.append("((");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(">=");
        sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDateTraitement()));

        sql.append(")");
        sql.append(OR);
        sql.append("(");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(EGAL);
        sql.append("0");

        sql.append(")");
        sql.append(OR);
        sql.append("(");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(" IS NULL");

        sql.append("))");
        sql.append(AND);
        sql.append("(");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        sql.append("<=");
        sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDateTraitement()));

        sql.append(")");
        sql.append(AND);

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        sql.append(IN);
        sql.append("(");
        sql.append(IREPrestationAccordee.CS_ETAT_VALIDE).append(VIRGULE);
        sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(VIRGULE);
        sql.append(IREPrestationAccordee.CS_ETAT_AJOURNE);
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REListerEcheanceRenteJoinMembresFamille();
    }

    public String getForDateTraitement() {
        return forDateTraitement;
    }

    public void setForDateTraitement(String forDateTraitement) {
        this.forDateTraitement = forDateTraitement;
    }
}
