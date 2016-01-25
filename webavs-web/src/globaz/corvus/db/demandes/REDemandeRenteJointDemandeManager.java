/*
 * Créé le 10 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRelationFamilialeRequerant;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author hpe
 */
public class REDemandeRenteJointDemandeManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_DATE_FIN = "DATE_DE_FIN";
    public static final String ALIAS_IS_EN_COURS = "IS_EN_COURS";
    public static final String WHERE_NSS_DEBUT = "(TIHAVSP.HVNAVS like ";
    public static final String WHERE_NSS_FIN = ")";

    private Boolean enCours = null;
    private String forCsEtatDemande = "";
    private String forCsEtatDemandeIn = "";
    private String forCSEtatDemandeNotIn = "";
    private String forCsSexe = "";
    private String forCsTypeCalcul = "";
    private String forCsTypeDemande = "";
    private String forCsTypeDemandeIn = "";
    private String forDateDebut = "";
    private String forDateNaissance = "";
    private String forDroitAu = "";
    private String forDroitDu = "";
    private String forIdDemandeRente = "";
    private String forIdGestionnaire = "";
    private String forIdTiersRequ = "";
    private transient String fromClause = null;
    private String idTiersRechercheFamilleWhere = "";
    private boolean isRechercheFamille = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";
    private int nbTiersFamille = 0;

    public REDemandeRenteJointDemandeManager() {
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isRechercheFamille) {

            // Début de la création de la String
            idTiersRechercheFamilleWhere += "(";

            // on chercher le tiers avec le nss
            PRTiersWrapper tw = PRTiersHelper.getTiers(getSession(), likeNumeroAVS);

            if (tw != null) {

                // on cherche le membreFamille pour le tiers
                SFMembreFamille mf = new SFMembreFamille();
                mf.setSession(getSession());
                mf.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                mf.setId(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                mf.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
                mf.retrieve(transaction);

                if (mf.isNew()) {
                    mf.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                    mf.retrieve(transaction);
                }

                if (!mf.isNew()) {

                    boolean isPremierPassage = true;

                    if (!JadeStringUtil.isIntegerEmpty(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                        idTiersRechercheFamilleWhere += tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                        nbTiersFamille++;
                        isPremierPassage = false;
                    }

                    // La méthode getParentsParMbrFamille est indépendant du
                    // domaine, on prend donc la SF pour le domaine rente,
                    // en passant null comme idTiersRequerant, aucun test n'est
                    // fait, et on est certain d'en obtenir une.
                    ISFSituationFamiliale sitFam = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                            ISFSituationFamiliale.CS_DOMAINE_RENTES, null);
                    ISFMembreFamille[] membresFamille = sitFam.getMembresFamilleEtendue(mf.getIdMembreFamille(),
                            Boolean.TRUE);

                    for (int i = 0; i < membresFamille.length; i++) {
                        ISFMembreFamille membreFamille = membresFamille[i];

                        if (membreFamille != null) {

                            // Pas d'idTiers, pas de rentes
                            if (!JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())
                                    && (idTiersRechercheFamilleWhere.indexOf(membreFamille.getIdTiers()) == -1)) {
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
            }
            // finir la string
            idTiersRechercheFamilleWhere += ")";
        }
    }

    @Override
    protected String _getFields(BStatement statement) {
        String schema = _getCollection();
        StringBuilder fields = new StringBuilder();
        fields.append(PRDemande.FIELDNAME_IDTIERS).append(", ");
        fields.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE).append(", ");
        fields.append(REDemandeRente.FIELDNAME_DATE_RECEPTION).append(", ");
        fields.append(REDemandeRente.FIELDNAME_DATE_DEBUT).append(", ");
        fields.append(REDemandeRente.FIELDNAME_DATE_FIN).append(", ");
        fields.append(REDemandeRente.FIELDNAME_DATE_DEPOT).append(", ");
        fields.append(REDemandeRente.FIELDNAME_DATE_TRAITEMENT).append(", ");
        fields.append(REDemandeRente.FIELDNAME_CS_ETAT).append(", ");
        fields.append(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE).append(", ");
        fields.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION).append(", ");
        fields.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(", ");
        fields.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append(", ");
        fields.append(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE).append(", ");
        fields.append(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_NUM_AVS).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_NOM).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_PRENOM).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_DATEDECES).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_SEXE).append(", ");
        fields.append(REDemandeRenteJointDemande.FIELDNAME_NATIONALITE).append(", ");
        fields.append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL).append(",");
        fields.append(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL).append(",");

        fields.append(" CASE  WHEN  ( ");
        fields.append(REDemandeRente.FIELDNAME_DATE_FIN).append("=0");
        fields.append(" OR ");
        fields.append(REDemandeRente.FIELDNAME_DATE_FIN).append(" IS NULL) THEN 99999999 ELSE ");
        fields.append(REDemandeRente.FIELDNAME_DATE_FIN).append(" END AS ")
                .append(REDemandeRenteJointDemandeManager.ALIAS_DATE_FIN);

        if ((isEnCours() != null)) {
            fields.append(",(");
            if (isEnCours().booleanValue()) {
                fields.append("1");
            } else {
                fields.append(getIsEnCoursCountRequest(schema));
            }
            fields.append(") AS ").append(REDemandeRenteJointDemandeManager.ALIAS_IS_EN_COURS);
        }

        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuilder from = new StringBuilder();

            from.append(getFromClauseFromEntity(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))
                    && !(isRechercheFamille && (nbTiersFamille > 0))) {

                from.append(" LEFT JOIN ");
                from.append(_getCollection());
                from.append(IPRTiers.TABLE_AVS_HIST);
                from.append(" AS ");
                from.append(IPRTiers.TABLE_AVS_HIST);
                from.append(" ON (");
                from.append(_getCollection());
                from.append(IPRTiers.TABLE_AVS);
                from.append(".");
                from.append(IPRTiers.FIELD_TI_IDTIERS);
                from.append(" = ");
                from.append(IPRTiers.TABLE_AVS_HIST);
                from.append(".");
                from.append(IPRTiers.FIELD_TI_IDTIERS);
                from.append(")");
            }
            fromClause = from.toString();
        }
        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();
        String schema = _getCollection();

        if (isEnCours() != null) {
            if (isEnCours().booleanValue()) {
                sql.append("(").append(getIsEnCoursCountRequest(schema)).append(")");
                sql.append(" > 0");
            }
        }
        if (isRechercheFamille && (nbTiersFamille > 0)) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(PRDemande.TABLE_NAME);
            sql.append(".");
            sql.append(PRDemande.FIELDNAME_IDTIERS);
            sql.append(" IN ");
            sql.append(idTiersRechercheFamilleWhere);

        } else {
            if ((!JadeStringUtil.isBlank(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
            }
            if (!JadeStringUtil.isEmpty(likeNom)) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(schema);
                sql.append(REDemandeRenteJointDemande.TABLE_TIERS);
                sql.append(".");
                sql.append(REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH);
                sql.append(" LIKE ");
                sql.append(this._dbWriteString(statement.getTransaction(),
                        PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
            }
            if (!JadeStringUtil.isEmpty(likePrenom)) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(schema);
                sql.append(REDemandeRenteJointDemande.TABLE_TIERS);
                sql.append(".");
                sql.append(REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH);
                sql.append(" LIKE ");
                sql.append(this._dbWriteString(statement.getTransaction(),
                        PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
            }
            if (!JadeStringUtil.isEmpty(forDateNaissance)) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(schema);
                sql.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
                sql.append(".");
                sql.append(REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE);
                sql.append("=");
                sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
            }
            if (!JadeStringUtil.isEmpty(forCsSexe)) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(schema);
                sql.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
                sql.append(".");
                sql.append(REDemandeRenteJointDemande.FIELDNAME_SEXE);
                sql.append("=");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
            }
        }
        if (!JadeStringUtil.isEmpty(forIdTiersRequ)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(PRDemande.TABLE_NAME);
            sql.append(".");
            sql.append(PRDemande.FIELDNAME_IDTIERS);
            sql.append(" = ");
            sql.append(forIdTiersRequ);
        }
        if (!JadeStringUtil.isEmpty(forIdDemandeRente)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
            sql.append(" = ");
            sql.append(forIdDemandeRente);
        }
        if (!JadeStringUtil.isEmpty(forIdGestionnaire)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE);
            sql.append("=");
            sql.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
        }
        if (!JadeStringUtil.isEmpty(forCsTypeDemande)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeDemande));
        }
        if (!JadeStringUtil.isEmpty(forCsTypeCalcul)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeCalcul));
        }
        if (!JadeStringUtil.isEmpty(forCsTypeDemandeIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
            sql.append(" IN (");
            sql.append(getForCsTypeDemandeIn());
            sql.append(" )");
        }
        if (!JadeStringUtil.isEmpty(forCsEtatDemande)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            if (!(forCsEtatDemande.equals(REDemandeRenteJointDemande.LABEL_NON_VALIDE))) {
                sql.append(schema);
                sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
                sql.append(".");
                sql.append(REDemandeRente.FIELDNAME_CS_ETAT);
                sql.append("=");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatDemande));
            } else {
                sql.append(schema);
                sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
                sql.append(".");
                sql.append(REDemandeRente.FIELDNAME_CS_ETAT);
                sql.append(" NOT IN (");
                sql.append(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
                sql.append(",");
                sql.append(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
                sql.append(",");
                sql.append(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
                sql.append(" )");
            }
        }
        if (!JadeStringUtil.isEmpty(forCsEtatDemandeIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_CS_ETAT);
            sql.append(" IN ( ");
            sql.append(forCsEtatDemandeIn);
            sql.append(" )");
        }
        if (!JadeStringUtil.isEmpty(forCSEtatDemandeNotIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_CS_ETAT);
            sql.append("  NOT IN ( ");
            sql.append(forCSEtatDemandeNotIn);
            sql.append(" )");
        }
        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_DATE_DEBUT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
        }
        if (!JadeStringUtil.isBlankOrZero(forDroitDu)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandeRente.FIELDNAME_DATE_DEBUT);
            sql.append("=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitDu));
        }
        if (!JadeStringUtil.isBlankOrZero(forDroitAu)) {
            try {
                JADate df = new JADate(forDroitAu);
                JACalendar cal = new JACalendarGregorian();
                df.setDay(cal.daysInMonth(df.getMonth(), df.getYear()));

                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(schema);
                sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
                sql.append(".");
                sql.append(REDemandeRente.FIELDNAME_DATE_FIN);
                sql.append("<=");
                sql.append(df.toStrAMJ());
                sql.append(" AND ");
                sql.append(schema);
                sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
                sql.append(".");
                sql.append(REDemandeRente.FIELDNAME_DATE_FIN);
                sql.append(" IS NOT NULL AND ");
                sql.append(schema);
                sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
                sql.append(".");
                sql.append(REDemandeRente.FIELDNAME_DATE_FIN);
                sql.append("<> 0");
            } catch (Exception e) {
            }
        }
        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteJointDemande();
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsEtatDemandeIn() {
        return forCsEtatDemandeIn;
    }

    public String getForCSEtatDemandeNotIn() {
        return forCSEtatDemandeNotIn;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsType() {
        return forCsTypeDemande;
    }

    public String getForCsTypeCalcul() {
        return forCsTypeCalcul;
    }

    public String getForCsTypeDemande() {
        return forCsTypeDemande;
    }

    public String getForCsTypeDemandeIn() {
        return forCsTypeDemandeIn;
    }

    /**
     * @deprecated remplacé par {@link #getForCsTypeDemandeIn()}
     * @return
     */
    @Deprecated
    public String getForCsTypeIn() {
        return forCsTypeDemandeIn;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDroitAu() {
        return forDroitAu;
    }

    public String getForDroitDu() {
        return forDroitDu;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdTiersRequ() {
        return forIdTiersRequ;
    }

    protected String getFromClauseFromEntity(String schema) {
        return REDemandeRenteJointDemande.createFromClause(_getCollection());
    }

    private String getIsEnCoursCountRequest(String schema) {

        String date = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(getSession()));

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT count (*) FROM ");
        sql.append(schema).append(SFMembreFamille.TABLE_NAME);

        // Jointure avec le membre de famille requérant
        sql.append(" MFR INNER JOIN ");
        sql.append(schema).append(SFRequerant.TABLE_NAME);
        sql.append(" ON ");
        sql.append(schema).append(SFRequerant.TABLE_NAME);
        sql.append(".").append(SFRequerant.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append("MFR.").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

        // pour chaque demande trouvée!!!
        sql.append(" LEFT OUTER JOIN ");
        sql.append(schema).append(SFRelationFamilialeRequerant.TABLE_NAME);
        sql.append(" ON ");
        sql.append(schema).append(SFRelationFamilialeRequerant.TABLE_NAME);
        sql.append(".").append(SFRelationFamilialeRequerant.FIELD_IDREQUERANT);
        sql.append("=");
        sql.append(schema).append(SFRequerant.TABLE_NAME);
        sql.append(".").append(SFRequerant.FIELD_IDREQUERANT);

        // - les membres de famille du requérant
        sql.append(" LEFT OUTER JOIN ");
        sql.append(schema).append(SFMembreFamille.TABLE_NAME);
        sql.append(" AS MF ON ");
        sql.append("MF.").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(" = ");
        sql.append(schema).append(SFRelationFamilialeRequerant.TABLE_NAME);
        sql.append(".").append(SFRelationFamilialeRequerant.FIELD_IDMEMBREFAMILLE);

        // Membres de la famille (requérant non compris)
        sql.append(" INNER JOIN ");
        sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(" ON (");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(" = ");
        sql.append(PRDemande.FIELDNAME_IDTIERS);
        sql.append(" OR ");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(" = ");
        sql.append("MF.").append(SFMembreFamille.FIELD_IDTIERS).append(")");

        sql.append(" WHERE ");
        sql.append("MFR.").append(SFMembreFamille.FIELD_IDTIERS);
        sql.append(" = ");
        sql.append(schema).append(PRDemande.TABLE_NAME);
        sql.append(".").append(PRDemande.FIELDNAME_IDTIERS);
        sql.append(" AND ");
        sql.append(SFRequerant.FIELD_IDDOMAINEAPPLICATION);
        sql.append(" = ");
        sql.append("MFR.").append(SFMembreFamille.FIELD_DOMAINE_APPLICATION);
        sql.append(" AND (");
        sql.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL");
        sql.append(" OR ");
        sql.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" = 0");
        sql.append(" OR ");
        sql.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(" >= ");
        sql.append(date).append(")");

        return sql.toString();
    }

    public boolean getIsRechercheFamille() {
        return isRechercheFamille;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    @Override
    public String getOrderByDefaut() {
        return PRDemande.FIELDNAME_IDDEMANDE;
    }

    public Boolean isEnCours() {
        return enCours;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false ou vide)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getLikeNumeroAVS())) {
            return "";
        }

        if (getLikeNumeroAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public void setEnCours(Boolean b) {
        enCours = b;
    }

    public void setForCsEtatDemande(String string) {
        forCsEtatDemande = string;
    }

    public void setForCsEtatDemandeIn(String forCsEtatDemandeIn) {
        this.forCsEtatDemandeIn = forCsEtatDemandeIn;
    }

    public void setForCSEtatDemandeNotIn(String forCSEtatDemandeNotIn) {
        this.forCSEtatDemandeNotIn = forCSEtatDemandeNotIn;
    }

    public void setForCsSexe(String string) {
        forCsSexe = string;
    }

    /**
     * @deprecated remplacé par {@link #setForCsTypeDemande(String)}
     * @param string
     */
    @Deprecated
    public void setForCsType(String string) {
        forCsTypeDemande = string;
    }

    public void setForCsTypeCalcul(String forCsTypeCalcul) {
        this.forCsTypeCalcul = forCsTypeCalcul;
    }

    public void setForCsTypeDemande(String forCsTypeDemande) {
        this.forCsTypeDemande = forCsTypeDemande;
    }

    public void setForCsTypeDemandeIn(String forCsTypeDemandeIn) {
        this.forCsTypeDemandeIn = forCsTypeDemandeIn;
    }

    /**
     * @deprecated remplacé par {@link #setForCsTypeDemandeIn(String)}
     * @param forCsTypeIn
     */
    @Deprecated
    public void setForCsTypeIn(String forCsTypeIn) {
        forCsTypeDemandeIn = forCsTypeIn;
    }

    public void setForDateDebut(String string) {
        forDateDebut = string;
    }

    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    public void setForDroitAu(String forDroitAu) {
        this.forDroitAu = forDroitAu;
    }

    public void setForDroitDu(String forDroitDu) {
        this.forDroitDu = forDroitDu;
    }

    public void setForIdDemandeRente(String forIdDemandeRente) {
        this.forIdDemandeRente = forIdDemandeRente;
    }

    public void setForIdGestionnaire(String string) {
        forIdGestionnaire = string;
    }

    public void setForIdTiersRequ(String forIdTiersRequ) {
        this.forIdTiersRequ = forIdTiersRequ;
    }

    public void setIsRechercheFamille(boolean boolean1) {
        isRechercheFamille = boolean1;
    }

    public void setLikeNom(String string) {
        likeNom = string;
    }

    public void setLikeNumeroAVS(String string) {
        likeNumeroAVS = string;
    }

    public void setLikeNumeroAVSNNSS(String string) {
        likeNumeroAVSNNSS = string;
    }

    public void setLikePrenom(String string) {
        likePrenom = string;
    }
}
