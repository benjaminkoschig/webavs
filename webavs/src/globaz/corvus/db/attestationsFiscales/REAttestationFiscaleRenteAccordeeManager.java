/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.attestationsFiscales;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.RESituationFamiliale;
import globaz.corvus.db.demandes.RESituationFamilialeManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRDateFormater;
import java.util.Iterator;

/**
 * @author scr
 * 
 * 
 * 
 */

public class REAttestationFiscaleRenteAccordeeManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    private String forCsEtatIn = "";

    private String forCsEtatNotIn = "";
    private String forIdTiersBaseCalcul = "";
    private String forOrderBy = "";

    private String fromDateDebutDroit = "";
    private String idTiersRechercheFamilleWhere = "";

    private boolean isRechercheFamille = false;

    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private int nbTiersFamille = 0;
    private String toDateFinDroit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isRechercheFamille()) {

            // Début de la création de la String
            idTiersRechercheFamilleWhere += "(";

            // Selon les paramètres saisis, on recherche dans la situation
            // familiale
            // tous les membres qui correspondent à ces critères (tel que SF
            // (écran d'accueil))
            RESituationFamilialeManager sfManager = new RESituationFamilialeManager();
            sfManager.setSession(getSession());
            sfManager.setLikeNoAvs(likeNumeroAVS);
            sfManager.setlikeNoAvsNNSS(likeNumeroAVSNNSS);
            sfManager.setForCsDomainesIn(ISFSituationFamiliale.CS_DOMAINE_RENTES + ", "
                    + ISFSituationFamiliale.CS_DOMAINE_STANDARD);

            sfManager.find(BManager.SIZE_NOLIMIT);

            boolean isPremierPassage = true;

            // La méthode getParentsParMbrFamille est indépendant du domaine, on
            // prend donc la SF pour le domaine rente.
            // En passant null comme idTiersRequerant, aucun test n'est fait, et
            // on est certain d'en obtenir une.
            globaz.hera.api.ISFSituationFamiliale sitFam = SFSituationFamilialeFactory.getSituationFamiliale(
                    getSession(), ISFSituationFamiliale.CS_DOMAINE_RENTES, null);

            if (sitFam != null) {
                for (Iterator<RESituationFamiliale> iter = sfManager.iterator(); iter.hasNext();) {
                    RESituationFamiliale sf = iter.next();

                    // Rechercher les parents
                    ISFMembreFamille[] membresFamille = sitFam.getParentsParMbrFamille(sf.getIdMembreFamille());

                    if (membresFamille != null) {
                        for (int i = 0; i < membresFamille.length; i++) {
                            ISFMembreFamille membreFamille = membresFamille[i];

                            if (membreFamille != null) {

                                System.out.println("PARENT idTiers = " + membreFamille.getIdTiers());

                                // Pas d'idTiers, pas de rentes
                                if (!JadeStringUtil.isEmpty(membreFamille.getIdTiers())) {

                                    nbTiersFamille++;

                                    if (isPremierPassage) {
                                        idTiersRechercheFamilleWhere += membreFamille.getIdTiers();
                                        isPremierPassage = false;
                                    } else {
                                        idTiersRechercheFamilleWhere += ", " + membreFamille.getIdTiers();
                                    }

                                }

                            }

                        }
                    }

                    // Rechercher les autres membres de la famille
                    ISFMembreFamilleRequerant[] membresFamille1 = sitFam.getMembresFamilleRequerantParMbrFamille(sf
                            .getIdMembreFamille());

                    if (membresFamille1 != null) {
                        for (int i = 0; i < membresFamille1.length; i++) {
                            ISFMembreFamilleRequerant membreFamille1 = membresFamille1[i];

                            if (membreFamille1 != null) {
                                // Pas d'idTiers, pas de rentes
                                if (!JadeStringUtil.isEmpty(membreFamille1.getIdTiers())) {

                                    nbTiersFamille++;

                                    if (isPremierPassage) {
                                        idTiersRechercheFamilleWhere += membreFamille1.getIdTiers();
                                        isPremierPassage = false;
                                    } else {
                                        idTiersRechercheFamilleWhere += ", " + membreFamille1.getIdTiers();
                                    }

                                }

                            }

                        }
                    }

                }
            }

            // finir la string
            idTiersRechercheFamilleWhere += ")";

        }

    }

    /**
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(BStatement statement) {

        String INNER_JOIN = " INNER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String POINT = ".";
        String WHERE = " WHERE ";
        String IN = " IN ";

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");

        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(", ");
        sql.append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA).append(POINT)
                .append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleRenteAccordeeManager.TABLE_AVS).append(POINT)
                .append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_NUM_AVS).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleRenteAccordeeManager.TABLE_TIERS).append(POINT)
                .append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_NOM).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleRenteAccordeeManager.TABLE_PERSONNE).append(POINT)
                .append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_SEXE).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleRenteAccordeeManager.TABLE_TIERS).append(POINT)
                .append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_PRENOM).append(", ");
        sql.append(_getCollection()).append(PRDemande.TABLE_NAME).append(POINT).append(PRDemande.FIELDNAME_IDTIERS)
                .append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA).append(", ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append(" ");

        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et des rentes
        // accordées
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(POINT);
        sql.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(POINT);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordées et informations
        // comptabilités
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(POINT);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        sql.append(POINT);
        sql.append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        // jointure entre table des prestations accordes et table des numeros
        // AVS
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_AVS);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(POINT);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_AVS);
        sql.append(POINT);
        sql.append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des prestation et table des tiers
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_TIERS);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(POINT);
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_TIERS);
        sql.append(POINT);
        sql.append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_PERSONNE);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_AVS);
        sql.append(POINT);
        sql.append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_ID_TIERS_TI);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REAttestationFiscaleRenteAccordeeManager.TABLE_PERSONNE);
        sql.append(POINT);
        sql.append(REAttestationFiscaleRenteAccordeeManager.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des bases de calculs et rentes accordee
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(POINT);
        sql.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(POINT);
        sql.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        sql.append(POINT);
        sql.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(POINT);
        sql.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(POINT);
        sql.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        sql.append(POINT);
        sql.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table PRDEMAP et table des demandes de rentes
        // INNER JOIN CVCIWEB.PRDEMAP ON
        // CVCIWEB.PRDEMAP.WAIDEM=CVCIWEB.REDEREN.YAIMDO
        sql.append(INNER_JOIN);
        sql.append(_getCollection());
        sql.append(PRDemande.TABLE_NAME);
        sql.append(ON);
        sql.append(_getCollection());
        sql.append(PRDemande.TABLE_NAME);
        sql.append(POINT);
        sql.append(PRDemande.FIELDNAME_IDDEMANDE);
        sql.append(EGAL);
        sql.append(_getCollection());
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(POINT);
        sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))
                && (!isRechercheFamille() && (nbTiersFamille == 0))) {
            sql.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS " + IPRTiers.TABLE_AVS_HIST
                    + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "." + IPRTiers.FIELD_TI_IDTIERS + " = "
                    + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS + ")");
        }

        StringBuffer whereClause = new StringBuffer();

        if (isRechercheFamille() && (nbTiersFamille > 0)) {

            whereClause.append(WHERE);
            // TODO voir si jointure necessaire sur le table PRDemande !!!
            whereClause.append(_getCollection()).append(PRDemande.TABLE_NAME).append(POINT)
                    .append(PRDemande.FIELDNAME_IDTIERS);
            whereClause.append(IN).append(idTiersRechercheFamilleWhere);
        }

        if (!JadeStringUtil.isEmpty(getForCsEtatNotIn())) {
            if (whereClause.length() != 0) {
                whereClause.append(AND);
            } else {
                whereClause.append(WHERE);
            }
            whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            whereClause.append(" NOT IN (" + getForCsEtatNotIn() + ")");
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBaseCalcul)) {
            if (whereClause.length() != 0) {
                whereClause.append(AND);
            } else {
                whereClause.append(WHERE);
            }
            whereClause.append(RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL);
            whereClause.append(EGAL).append(this._dbWriteNumeric(statement.getTransaction(), forIdTiersBaseCalcul));
        }

        if (!JadeStringUtil.isEmpty(fromDateDebutDroit)) {
            if (whereClause.length() != 0) {
                whereClause.append(AND);
            } else {
                whereClause.append(WHERE);
            }
            whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            whereClause.append(" <="
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(fromDateDebutDroit)));
        }

        if (!JadeStringUtil.isBlankOrZero(toDateFinDroit)) {

            if (whereClause.length() != 0) {
                whereClause.append(AND);
            } else {
                whereClause.append(WHERE);
            }

            // si pas de date de fin ou si date de fin > date donnee
            whereClause.append(" (").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(">=")
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(toDateFinDroit));
            whereClause.append(OR).append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" = 0");
            whereClause.append(OR).append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL )");
        }

        if (!JadeStringUtil.isEmpty(forCsEtatIn)) {
            if (whereClause.length() != 0) {
                whereClause.append(AND);
            } else {
                whereClause.append(WHERE);
            }

            whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            whereClause.append(" IN (" + forCsEtatIn + ")");
        }

        sql.append(whereClause.toString());

        sql.append(" ORDER BY ");

        if (JadeStringUtil.isEmpty(forOrderBy)) {
            sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        } else {
            sql.append(forOrderBy);
        }

        return sql.toString();

    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {

        return "";
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAttestationFiscaleRenteAccordee();
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsEtatNotIn() {
        return forCsEtatNotIn;
    }

    public String getForIdTiersBaseCalcul() {
        return forIdTiersBaseCalcul;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromDateDebutDroit() {
        return fromDateDebutDroit;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getToDateFinDroit() {
        return toDateFinDroit;
    }

    public boolean isRechercheFamille() {
        return isRechercheFamille;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsEtatNotIn(String forCsEtatNotIn) {
        this.forCsEtatNotIn = forCsEtatNotIn;
    }

    public void setForIdTiersBaseCalcul(String forIdTiersBaseCalcul) {
        this.forIdTiersBaseCalcul = forIdTiersBaseCalcul;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromDateDebutDroit(String fromDateDebutDroit) {
        this.fromDateDebutDroit = fromDateDebutDroit;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setRechercheFamille(boolean isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
    }

    public void setToDateFinDroit(String toDateFinDroit) {
        this.toDateFinDroit = toDateFinDroit;
    }
}
