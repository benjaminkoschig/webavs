/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.RESituationFamiliale;
import globaz.corvus.db.demandes.RESituationFamilialeManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.webavs.common.BIGenericManager;

/**
 * @author SCR
 */
public class RERenteAccJoinTblTiersJoinDemRenteManager extends PRAbstractManager
        implements BIGenericManager<RERenteAccJoinTblTiersJoinDemandeRente> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean escapeString = true;
    private String forCodeCasSpecial = "";
    private String forCsEtat = "";
    private String forCsEtatDifferentDe = "";
    private String forCsEtatIn = "";
    private String forCsEtatNotIn = "";
    private String forCsSexe = "";
    private String forCsTypeDemande = "";
    private String forDateNaissance = "";
    private String forDroitAu = "";
    private String forDroitDu = "";
    private String forEnCoursAtMois = "";
    private String forGenrePrestation = "";
    private String forGenrePrestationIn = "";
    private String forGenrePrestationNotIn = "";
    private String forIdTiersBaseCalcul = "";
    private String forIdTiersBeneficiaire = "";
    private String forMoisFinRANotEmptyAndHigherOrEgal = "";
    private String forNoBaseCalcul = "";
    private String forNoDemandeRente = "";
    private String forNoRenteAccordee = "";
    private transient String fromClause = null;
    private String fromDateDebutDroit = "";
    private String idTiersRechercheFamilleWhere = "";
    private boolean isRechercheFamille = false;
    private boolean isRechercheRenteEnCours = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";
    private int nbTiersFamille = 0;
    private String toDateFinDroit = "";
    private boolean isEtatCalculeForDateFinDroitNotEmpty = false;
    private String forCodesPrestationsIn = "";

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isRechercheFamille) {

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
            sfManager.setForCsDomainesIn(
                    ISFSituationFamiliale.CS_DOMAINE_RENTES + ", " + ISFSituationFamiliale.CS_DOMAINE_STANDARD);

            sfManager.find(BManager.SIZE_NOLIMIT);

            boolean isPremierPassage = true;

            // La méthode getParentsParMbrFamille est indépendant du domaine, on
            // prend donc la SF pour le domaine rente.
            // En passant null comme idTiersRequerant, aucun test n'est fait, et
            // on est certain d'en obtenir une.
            globaz.hera.api.ISFSituationFamiliale sitFam = SFSituationFamilialeFactory
                    .getSituationFamiliale(getSession(), ISFSituationFamiliale.CS_DOMAINE_RENTES, null);

            for (Iterator iter = sfManager.iterator(); iter.hasNext();) {
                RESituationFamiliale sf = (RESituationFamiliale) iter.next();

                // Rechercher les parents
                ISFMembreFamille[] membresFamille = sitFam.getParentsParMbrFamille(sf.getIdMembreFamille());

                for (int i = 0; i < membresFamille.length; i++) {
                    ISFMembreFamille membreFamille = membresFamille[i];

                    if (membreFamille != null) {

                        // System.out.println("PARENT idTiers = "+membreFamille.getIdTiers());

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

                // Rechercher les autres membres de la famille
                ISFMembreFamilleRequerant[] membresFamille1 = sitFam
                        .getMembresFamilleRequerantParMbrFamille(sf.getIdMembreFamille());

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

            // finir la string
            idTiersRechercheFamilleWhere += ")";

        }

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RERenteAccJoinTblTiersJoinDemandeRente.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))
                    && !(isRechercheFamille && (nbTiersFamille > 0))) {
                from.append(
                        " LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS " + IPRTiers.TABLE_AVS_HIST
                                + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "." + IPRTiers.FIELD_TI_IDTIERS
                                + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Renvoie la clause WHERE de la requête SQL
     *
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";
        String schema = _getCollection();

        // Ce manager est fait ainsi... si recherche par nss, il s'agit du nss
        // de l'assuré, celui ayant ouvert
        // la demande de rente -> dans ce cas, le no de demande de rente doit
        // obligatoirement être renseigné
        // faute de quoi, le manager ne retournera pas tous les résultat.

        if (isRechercheFamille && (nbTiersFamille > 0)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_IDTIERS + " IN "
                    + idTiersRechercheFamilleWhere;

        } else {

            if ((!JadeStringUtil.isBlank(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
            }

            if (!JadeStringUtil.isEmpty(likeNom)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += schema + REDemandeRenteJointDemande.TABLE_TIERS + "."
                        + REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH + " like " + this._dbWriteString(
                                statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%");
            }

            if (!JadeStringUtil.isEmpty(likePrenom)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += schema + REDemandeRenteJointDemande.TABLE_TIERS + "."
                        + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH + " like "
                        + this._dbWriteString(statement.getTransaction(),
                                PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%");
            }

            if (!JadeStringUtil.isEmpty(forDateNaissance)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += schema + REDemandeRenteJointDemande.TABLE_PERSONNE + "."
                        + REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE + "="
                        + this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);

            }

            if (!JadeStringUtil.isEmpty(forCsSexe)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += schema + REDemandeRenteJointDemande.TABLE_PERSONNE + "."
                        + REDemandeRenteJointDemande.FIELDNAME_SEXE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), forCsSexe);
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBeneficiaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiersBeneficiaire);

        }

        if (!JadeStringUtil.isEmpty(forGenrePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + "="
                    + this._dbWriteString(statement.getTransaction(), forGenrePrestation);
        }

        if (!JadeStringUtil.isBlank(forGenrePrestationIn)) {
            StringBuilder sql = new StringBuilder();
            if (sqlWhere.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            sql.append(" IN (");
            if (escapeString) {
                sql.append(this._dbWriteString(statement.getTransaction(), forGenrePrestationIn));
            } else {
                sql.append(forGenrePrestationIn);
            }
            sql.append(")");

            sqlWhere += sql.toString();
        }

        if (!JadeStringUtil.isBlank(forGenrePrestationNotIn)) {
            StringBuilder sql = new StringBuilder();
            if (sqlWhere.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            sql.append(" NOT IN (");
            if (escapeString) {
                sql.append(this._dbWriteString(statement.getTransaction(), forGenrePrestationNotIn));
            } else {
                sql.append(forGenrePrestationNotIn);
            }
            sql.append(")");

            sqlWhere += sql.toString();
        }

        if (!JadeStringUtil.isEmpty(forCsTypeDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "."
                    + REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsTypeDemande);
        }

        if (!JadeStringUtil.isEmpty(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtat);

        }

        if (!JadeStringUtil.isEmpty(forCsEtatDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDifferentDe);

        }

        if (!JadeStringUtil.isIntegerEmpty(forNoDemandeRente)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "."
                    + REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoDemandeRente);
        }

        if (!JadeStringUtil.isBlank(forNoBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REBasesCalcul.TABLE_NAME_BASES_CALCUL + "."
                    + REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoBaseCalcul);
        }

        if (!JadeStringUtil.isBlank(forNoRenteAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "."
                    + RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoRenteAccordee);
        }

        if (!JadeStringUtil.isEmpty(forMoisFinRANotEmptyAndHigherOrEgal)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "((" + _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisFinRANotEmptyAndHigherOrEgal))
                    + ")";

            sqlWhere += " OR ";

            sqlWhere += "(" + _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0 )";

            sqlWhere += " OR ";

            sqlWhere += "(" + _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " is NULL )";

            if (isEtatCalculeForDateFinDroitNotEmpty) {

                // ou si date de fin ET etat = calcule
                sqlWhere += " OR " + "(" + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " <> 0 " + " AND "
                        + REPrestationsAccordees.FIELDNAME_CS_ETAT + "=" + IREPrestationAccordee.CS_ETAT_CALCULE + ") ";
            }

            sqlWhere += ")";

            sqlWhere += " AND ";
            sqlWhere += "(" + _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " <= "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisFinRANotEmptyAndHigherOrEgal))
                    + ")";

        }

        if (!JadeStringUtil.isEmpty(fromDateDebutDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " <= "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(fromDateDebutDroit));
        }

        if (!JadeStringUtil.isBlankOrZero(toDateFinDroit)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere += " (" + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + ">="
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(toDateFinDroit) + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL ) ";
        }

        if (isRechercheRenteEnCours) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (" + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL ) ";
        }

        if (!JadeStringUtil.isEmpty(forDroitDu)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " = " + this._dbWriteNumeric(
                            statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDroitDu));
        }

        if (!JadeStringUtil.isBlankOrZero(forDroitAu)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere += " " + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + "<="
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDroitAu) + " AND "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " <> 0" + " AND "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NOT NULL  ";
        }

        if (!JadeStringUtil.isIntegerEmpty(forEnCoursAtMois)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + "<="
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois) + " AND ( "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL " + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " >= "
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois) + ")";
        }

        if (!JadeStringUtil.isEmpty(forCsEtatIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + " IN (" + forCsEtatIn + " )";
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiersBaseCalcul);
        }

        if (!JadeStringUtil.isEmpty(forCsEtatNotIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + " NOT IN (" + forCsEtatNotIn + " )";
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeCasSpecial)) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }

            String codeCasSpecial = null;
            if (forCodeCasSpecial.length() > 2) {
                codeCasSpecial = forCodeCasSpecial.substring(0, 1);
            } else if (forCodeCasSpecial.length() == 1) {
                codeCasSpecial = "0" + forCodeCasSpecial;
            } else if (forCodeCasSpecial.length() == 2) {
                codeCasSpecial = forCodeCasSpecial;
            }

            codeCasSpecial = "'" + codeCasSpecial + "'";

            sqlWhere += "(";
            sqlWhere += _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "."
                    + RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1 + "=" + codeCasSpecial;
            sqlWhere += " OR ";
            sqlWhere += _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "."
                    + RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2 + "=" + codeCasSpecial;
            sqlWhere += " OR ";
            sqlWhere += _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "."
                    + RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3 + "=" + codeCasSpecial;
            sqlWhere += " OR ";
            sqlWhere += _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "."
                    + RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4 + "=" + codeCasSpecial;
            sqlWhere += " OR ";
            sqlWhere += _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "."
                    + RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5 + "=" + codeCasSpecial;
            sqlWhere += ")";
        }

        if (!JadeStringUtil.isEmpty(forCodesPrestationsIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES
                    + "."+REPrestationsAccordees.FIELDNAME_CODE_PRESTATION+" IN ("
                    + forCodesPrestationsIn+" )";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteAccJoinTblTiersJoinDemandeRente();
    }

    @Override
    public List<RERenteAccJoinTblTiersJoinDemandeRente> getContainerAsList() {
        List<RERenteAccJoinTblTiersJoinDemandeRente> list = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
        for (int i = 0; i < size(); i++) {
            list.add((RERenteAccJoinTblTiersJoinDemandeRente) get(i));
        }
        return list;
    }

    public String getForCodeCasSpecial() {
        return forCodeCasSpecial;
    }

    /**
     * @return
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forCsEtatDifferentDe
     */
    public String getForCsEtatDifferentDe() {
        return forCsEtatDifferentDe;
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsEtatNotIn() {
        return forCsEtatNotIn;
    }

    /**
     * @return
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return
     */
    public String getForCsTypeDemande() {
        return forCsTypeDemande;
    }

    /**
     * @return
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDroitAu() {
        return forDroitAu;
    }

    public String getForDroitDu() {
        return forDroitDu;
    }

    public String getForEnCoursAtMois() {
        return forEnCoursAtMois;
    }

    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    public String getForGenrePrestationIn() {
        return forGenrePrestationIn;
    }

    public String getForGenrePrestationNotIn() {
        return forGenrePrestationNotIn;
    }

    public String getForIdTiersBaseCalcul() {
        return forIdTiersBaseCalcul;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public String getForMoisFinRANotEmptyAndHigherOrEgal() {
        return forMoisFinRANotEmptyAndHigherOrEgal;
    }

    public String getForNoBaseCalcul() {
        return forNoBaseCalcul;
    }

    /**
     * @return
     */
    public String getForNoDemandeRente() {
        return forNoDemandeRente;
    }

    public String getForNoRenteAccordee() {
        return forNoRenteAccordee;
    }

    public String getFromDateDebutDroit() {
        return fromDateDebutDroit;
    }

    public boolean getIsRechercheRenteEnCours() {
        return isRechercheRenteEnCours;
    }

    /**
     * @return
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return
     */
    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    /**
     * @return
     */
    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    /**
     * @return
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        if (!JadeStringUtil.isBlankOrZero(likeNom)) {
            return REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH + ","
                    + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH + ", " + " CASE "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " WHEN 0 THEN 999999 ELSE "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " END DESC";
        } else {
            if (JadeStringUtil.isEmpty(forNoDemandeRente)) {
                return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
            } else {
                return REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT + ","
                        + REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
            }
        }
    }

    public String getToDateFinDroit() {
        return toDateFinDroit;
    }

    public boolean isEtatCalculeForDateFinDroitNotEmpty() {
        return isEtatCalculeForDateFinDroitNotEmpty;
    }

    public final boolean isEscapeString() {
        return escapeString;
    }

    public boolean isRechercheFamille() {
        return isRechercheFamille;
    }

    public final void setEscapeString(boolean escapeString) {
        this.escapeString = escapeString;
    }

    public void setForCodeCasSpecial(String forCodeCasSpecial) {
        this.forCodeCasSpecial = forCodeCasSpecial;
    }

    /**
     * @param string
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    /**
     * @param forCsEtatDifferentDe
     *            the forCsEtatDifferentDe to set
     */
    public void setForCsEtatDifferentDe(String forCsEtatDifferentDe) {
        this.forCsEtatDifferentDe = forCsEtatDifferentDe;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsEtatNotIn(String forCsEtatNotIn) {
        this.forCsEtatNotIn = forCsEtatNotIn;
    }

    /**
     * @param string
     */
    public void setForCsSexe(String string) {
        forCsSexe = string;
    }

    /**
     * @param string
     */
    public void setForCsTypeDemande(String string) {
        forCsTypeDemande = string;
    }

    /**
     * @param string
     */
    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    public void setForDroitAu(String s) {
        forDroitAu = s;
    }

    public void setForDroitDu(String s) {
        forDroitDu = s;
    }

    public void setForEnCoursAtMois(String forEnCoursAtMois) {
        this.forEnCoursAtMois = forEnCoursAtMois;
    }

    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    public void setForGenrePrestationIn(String forGenrePrestationIn) {
        this.forGenrePrestationIn = forGenrePrestationIn;
    }

    public void setForGenrePrestationNotIn(String forGenrePrestationNotIn) {
        this.forGenrePrestationNotIn = forGenrePrestationNotIn;
    }

    public void setForIdTiersBaseCalcul(String forIdTiersBaseCalcul) {
        this.forIdTiersBaseCalcul = forIdTiersBaseCalcul;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForMoisFinRANotEmptyAndHigherOrEgal(String forMoisFinRANotEmptyAndHigherOrEgal) {
        this.forMoisFinRANotEmptyAndHigherOrEgal = forMoisFinRANotEmptyAndHigherOrEgal;
    }

    public void setForNoBaseCalcul(String forNoBaseCalcul) {
        this.forNoBaseCalcul = forNoBaseCalcul;
    }

    /**
     * @param string
     */
    public void setForNoDemandeRente(String string) {
        forNoDemandeRente = string;
    }

    public void setForNoRenteAccordee(String forNoRenteAccordee) {
        this.forNoRenteAccordee = forNoRenteAccordee;
    }

    public void setFromDateDebutDroit(String fromDateDebutDroit) {
        this.fromDateDebutDroit = fromDateDebutDroit;
    }

    public void setIsRechercheRenteEnCours(boolean isRechercheRenteEnCours) {
        this.isRechercheRenteEnCours = isRechercheRenteEnCours;
    }

    /**
     * @param string
     */
    public void setLikeNom(String string) {
        likeNom = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAVS(String string) {
        likeNumeroAVS = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAVSNNSS(String string) {
        likeNumeroAVSNNSS = string;
    }

    /**
     * @param string
     */
    public void setLikePrenom(String string) {
        likePrenom = string;
    }

    public void setRechercheFamille(boolean isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
    }

    public void setToDateFinDroit(String toDateFinDroit) {
        this.toDateFinDroit = toDateFinDroit;
    }

    public void setEtatCalculeForDateFinDroitNotEmpty(boolean isEtatCalculeForDateFinDroitNotEmpty) {
        this.isEtatCalculeForDateFinDroitNotEmpty = isEtatCalculeForDateFinDroitNotEmpty;
    }

    public String getForCodesPrestationsIn() {
        return forCodesPrestationsIn;
    }

    public void setForCodesPrestationsIn(final String forCodesPrestationsIn) {
        this.forCodesPrestationsIn = forCodesPrestationsIn;
    }
}
