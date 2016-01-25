/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr Retourne toutes les rentes actives pour le mois donné, avec les membres de la famille du requérant.
 *         Utilisé pour optimiser la liste des rentes en erreur.
 */

public class REListeRentesActivesJoinMembresFamilleForListeEnErreurManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_INACTIF = "HTINAC";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NSS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // Format : mm.aaaa
    private String forDatePaiement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * selectionne en une seule requete toutes les infos necessaires pour le traitement de la liste des rentes en
     * erreurs
     * <p>
     * redefini car la requete est un peu compliquee.
     * </p>
     * SELECT ZTLCPR, ZTLFRR, YINDIN, YLLCS1, YLLCS2, YLLCS3, YLLCS4, YLLCS5, HPTSEX, WAITIE, ZTITBE, HXNAVS, HTLDE1,
     * HTLDE2, HPDNAI, HPDDEC, HPTSEX, HNIPAY, INACTIF membre_b.wgitie AS amb_idti1, membre_b.wgimef AS amb_idmf1,
     * membre_b.wgtdoa AS amb_domaine, conjoints.wjicon AS aconj_id, conjoints.wjico1 AS aconj_idmf1, conjoints.wjico2
     * AS aconj_idmf2, relations.wkirec AS arel_id, relations.wkddeb as arel_dd, relations.wkdfin as arel_df,
     * relations.wkttyp as arel_typ FROM cvciweb.rereacc INNER JOIN cvciweb.repracc ON cvciweb.rereacc.ylirac =
     * cvciweb.repracc.ztipra INNER JOIN cvciweb.titierp ON cvciweb.repracc.ztitbe = cvciweb.titierp.htitie INNER JOIN
     * cvciweb.tipavsp ON cvciweb.repracc.ztitbe = cvciweb.tipavsp.htitie INNER JOIN cvciweb.tipersp ON
     * cvciweb.repracc.ztitbe = cvciweb.tipersp.htitie INNER JOIN cvciweb.rebacal ON cvciweb.rebacal.yiibca =
     * cvciweb.rereacc.ylibac INNER JOIN cvciweb.sfmbrfam AS membre_b ON membre_b.wgitie = cvciweb.repracc.ztitbe INNER
     * JOIN cvciweb.sfconjoi AS conjoints ON (membre_b.wgimef = conjoints.wjico1 OR membre_b.wgimef = conjoints.wjico2)
     * LEFT JOIN cvciweb.sfrelcon AS relations ON relations.wkicon = conjoints.wjicon WHERE ((cvciweb.repracc.ztdfdr >=
     * 200905) OR (cvciweb.repracc.ztdfdr = 0) OR (cvciweb.repracc.ztdfdr IS NULL)) AND (cvciweb.repracc.ztdddr <=
     * 200905) AND cvciweb.repracc.ztteta IN (52820002,52820003) ORDER BY htlde1, htlde2, ztitbe, ylirac, AMB_DOMAINE,
     * AREL_DD;
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(BStatement statement) {

        String INNER_JOIN = " INNER JOIN ";
        String LEFT_JOIN = " LEFT OUTER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String POINT = ".";

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(", ");
        sql.append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE).append(", ");
        sql.append(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE).append(", ");
        sql.append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append(", ");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(", ");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(", ");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(", ");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(", ");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_SEXE).append(", ");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_NSS).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_NOM).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_PRENOM).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_DATENAISSANCE).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_DATEDECES).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_SEXE).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_NATIONALITE).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_INACTIF).append(", ");
        sql.append("membre_b.wgitie AS amb_idti1").append(", ");
        sql.append("membre_b.wgimef AS amb_idmf1").append(", ");
        sql.append("membre_b.wgtdoa AS amb_domaine").append(", ");
        sql.append("conjoints.wjicon AS aconj_id").append(", ");
        sql.append("conjoints.wjico1 AS aconj_idmf1").append(", ");
        sql.append("conjoints.wjico2 AS aconj_idmf2").append(", ");
        sql.append("relations.wkirec AS arel_id").append(", ");
        sql.append("relations.wkddeb AS arel_dd").append(", ");
        sql.append("relations.wkdfin AS arel_df").append(", ");
        sql.append("relations.wkttyp AS arel_typ");
        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

        // join entre RenteAccordee et Prestations accordées
        sql.append(LEFT_JOIN).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(ON).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection())
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_TIERS);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection())
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_TIERS).append(POINT)
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_ID_TIERS_TI);

        sql.append(INNER_JOIN).append(_getCollection())
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_AVS);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection())
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_AVS).append(POINT)
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_ID_TIERS_TI);

        sql.append(INNER_JOIN).append(_getCollection())
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_PERSONNE);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection())
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_PERSONNE).append(POINT)
                .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_ID_TIERS_TI);

        sql.append(LEFT_JOIN).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(ON).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        sql.append(INNER_JOIN).append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(" AS membre_b");
        sql.append(ON).append("membre_b.").append(SFMembreFamille.FIELD_IDTIERS);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        sql.append(LEFT_JOIN).append(_getCollection()).append(SFConjoint.TABLE_NAME).append(" AS conjoints");
        sql.append(ON).append("(membre_b.").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(EGAL).append("conjoints.").append(SFConjoint.FIELD_IDCONJOINT1).append(OR);
        sql.append("membre_b.").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(EGAL).append("conjoints.")
                .append(SFConjoint.FIELD_IDCONJOINT2).append(")");

        sql.append(LEFT_JOIN).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(" AS relations");
        sql.append(ON).append("relations.").append(SFRelationConjoint.FIELD_IDCONJOINTS);
        sql.append(EGAL).append("conjoints.").append(SFConjoint.FIELD_IDCONJOINTS);

        // Les RA dans l'état validé ou partiel inclusent dans la période
        sql.append(" WHERE ((");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" >= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement())).append(")").append(OR)
                .append("(").append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(EGAL).append(" 0)")
                .append(OR).append("(").append(_getCollection())
                .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL))");

        sql.append(AND).append("(").append(_getCollection())
                .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" <= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement())).append(")");
        sql.append(AND)
                .append(_getCollection())
                .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT)
                .append(" IN (")
                .append(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                        + IREPrestationAccordee.CS_ETAT_DIMINUE).append(")");

        sql.append(" ORDER BY ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_NOM).append(", ");
        sql.append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_PRENOM).append(", ");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(", ");
        sql.append("amb_domaine, ");
        sql.append("arel_dd ");

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {
        try {
            String INNER_JOIN = " INNER JOIN ";
            String LEFT_JOIN = " LEFT OUTER JOIN ";
            String ON = " ON ";
            String OR = " OR ";
            String AND = " AND ";
            String EGAL = " = ";
            String POINT = ".";

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT COUNT(").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(") ");

            sql.append(" FROM ");
            sql.append(_getCollection());
            sql.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

            // join entre RenteAccordee et Prestations accordées
            sql.append(INNER_JOIN).append(_getCollection())
                    .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
            sql.append(ON).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                    .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
            sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

            sql.append(INNER_JOIN).append(_getCollection())
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_TIERS);
            sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sql.append(EGAL).append(_getCollection())
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_TIERS).append(POINT)
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_ID_TIERS_TI);

            sql.append(INNER_JOIN).append(_getCollection())
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_AVS);
            sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sql.append(EGAL).append(_getCollection())
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_AVS).append(POINT)
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_ID_TIERS_TI);

            sql.append(INNER_JOIN).append(_getCollection())
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_PERSONNE);
            sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sql.append(EGAL).append(_getCollection())
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.TABLE_PERSONNE).append(POINT)
                    .append(REListeRentesActivesJoinMembresFamilleForListeEnErreurManager.FIELDNAME_ID_TIERS_TI);

            sql.append(INNER_JOIN).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
            sql.append(ON).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                    .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
            sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                    .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

            sql.append(INNER_JOIN).append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(" AS membre_b");
            sql.append(ON).append("membre_b.").append(SFMembreFamille.FIELD_IDTIERS);
            sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

            sql.append(LEFT_JOIN).append(_getCollection()).append(SFConjoint.TABLE_NAME).append(" AS conjoints");
            sql.append(ON).append("(membre_b.").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
            sql.append(EGAL).append("conjoints.").append(SFConjoint.FIELD_IDCONJOINT1).append(OR);
            sql.append("membre_b.").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(EGAL).append("conjoints.")
                    .append(SFConjoint.FIELD_IDCONJOINT2).append(")");

            sql.append(LEFT_JOIN).append(_getCollection()).append(SFRelationConjoint.TABLE_NAME)
                    .append(" AS relations");
            sql.append(ON).append("relations.").append(SFRelationConjoint.FIELD_IDCONJOINTS);
            sql.append(EGAL).append("conjoints.").append(SFConjoint.FIELD_IDCONJOINTS);

            // Les RA dans l'état validé ou partiel inclusent dans la période
            sql.append(" WHERE ((");
            sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" >= ")
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement())).append(")").append(OR)
                    .append("(").append(_getCollection())
                    .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(EGAL).append(" 0)").append(OR)
                    .append("(").append(_getCollection())
                    .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL))");

            sql.append(AND).append("(").append(_getCollection())
                    .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                    .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" <= ")
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement())).append(")");
            sql.append(AND)
                    .append(_getCollection())
                    .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT)
                    .append(REPrestationsAccordees.FIELDNAME_CS_ETAT)
                    .append(" IN (")
                    .append(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                            + IREPrestationAccordee.CS_ETAT_DIMINUE).append(")");

            return sql.toString();

        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSqlCount() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteActiveJoinMembresFamille();
    }

    public String getForDatePaiement() {
        return forDatePaiement;
    }

    /**
     * @param forDatePaiement
     *            format : mm.aaaa
     */
    public void setForDatePaiement(String forDatePaiement) {
        this.forDatePaiement = forDatePaiement;
    }

}
