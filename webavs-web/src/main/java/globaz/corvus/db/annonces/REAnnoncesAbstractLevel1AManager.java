package globaz.corvus.db.annonces;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.tiers.ITIHistoriqueAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class REAnnoncesAbstractLevel1AManager extends PRAbstractManager implements
        BIGenericManager<REAnnoncesAbstractLevel1A> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeEnregistrement = "";
    private String forCsCodeTraitement = "";
    private String forCsEtat = "";
    private String forDebutDroit = "";
    private String forFinDroit = "";
    private String forGenrePrestation = "";
    private String forMoisRapport = "";
    private String forNoRenteAccordee = "";
    private String forNssAyantDroit = "";
    private String forNumeroAnnonce = "";
    private boolean hasAvsHistory = true;
    private Set<Long> idsRenteAccordeeIn;
    private String idTiersRechercheFamilleWhere = "";
    private boolean isForAnnoncesSubsequentes = false;
    private String isPRE0070 = "";
    private boolean isRechercheFamille = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";
    private int nbTiersFamille = 0;

    public REAnnoncesAbstractLevel1AManager() {
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isRechercheFamille) {
            nbTiersFamille = 0;

            StringBuilder sql = new StringBuilder();
            sql.append("(");

            PRTiersWrapper tw = null;
            if (!JadeStringUtil.isBlank(likeNumeroAVS)) {
                tw = PRTiersHelper.getTiers(getSession(), likeNumeroAVS);
            } else if (!JadeStringUtil.isBlank(forNoRenteAccordee)) {
                REPrestationsAccordees prestationAccordee = new REPrestationsAccordees();
                prestationAccordee.setSession(getSession());
                prestationAccordee.setIdPrestationAccordee(forNoRenteAccordee);
                prestationAccordee.retrieve();

                tw = PRTiersHelper.getTiersParId(getSession(), prestationAccordee.getIdTiersBeneficiaire());
            }

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
                        sql.append(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
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
                                    isPremierPassage = false;
                                } else {
                                    sql.append(", ");
                                }
                                sql.append(membreFamille.getIdTiers());
                            }
                        }
                    }
                }
            }
            sql.append(")");

            idTiersRechercheFamilleWhere = sql.toString();
        }
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tableEnteteAnnonce = _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER;
        String tableAnnonceLevel1A = _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A;
        String tableAnnonceRente = _getCollection() + REAnnonceRente.TABLE_NAME_ANNONCE_RENTE;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableHistoriqueAvs = _getCollection() + ITIHistoriqueAvsDefTable.TABLE_NAME;

        sql.append(tableEnteteAnnonce);

        sql.append(" INNER JOIN ").append(tableAnnonceLevel1A);
        sql.append(" ON ").append(tableEnteteAnnonce).append(".").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE)
                .append("=").append(tableAnnonceLevel1A).append(".")
                .append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A);

        sql.append(" INNER JOIN ").append(tableAnnonceRente);
        sql.append(" ON ").append(tableEnteteAnnonce).append(".").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE)
                .append("=").append(tableAnnonceRente).append(".").append(REAnnonceRente.FIELDNAME_ID_ANNONCE_HEADER);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_TIERS)
                .append("=").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        if (hasAvsHistory) {
            sql.append(" INNER JOIN ").append(tableHistoriqueAvs);
            sql.append(" ON ").append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS).append("=")
                    .append(tableHistoriqueAvs).append(".").append(ITIHistoriqueAvsDefTable.ID_TIERS);
        }

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tableEnteteAnnonce = _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER;
        String tableAnnonceLevel1A = _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A;
        String tableAnnonceRente = _getCollection() + REAnnonceRente.TABLE_NAME_ANNONCE_RENTE;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        if (isRechercheFamille && (nbTiersFamille > 0)) {
            sql.append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_TIERS)
                    .append(" IN ").append(idTiersRechercheFamilleWhere);
        } else {
            if ((!JadeStringUtil.isBlank(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                // BZ 9792 : le schéma n'était pas inclus
                sql.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS(), true));
            }

            if (!JadeStringUtil.isEmpty(likeNom)) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }

                sql.append(tableTiers)
                        .append(".")
                        .append(ITITiersDefTable.DESIGNATION_1_MAJ)
                        .append(" LIKE ")
                        .append(this._dbWriteString(statement.getTransaction(),
                                PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
            }

            if (!JadeStringUtil.isEmpty(likePrenom)) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }

                sql.append(tableTiers)
                        .append(".")
                        .append(ITITiersDefTable.DESIGNATION_2_MAJ)
                        .append(" LIKE ")
                        .append(this._dbWriteString(statement.getTransaction(),
                                PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
            }

            if (!JadeStringUtil.isIntegerEmpty(forMoisRapport)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT)
                        .append("=");
                if (forMoisRapport.length() == 7) {
                    sql.append(this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisRapport)));
                } else {
                    sql.append(this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMAA_to_AAAAMM(forMoisRapport)));
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(forNoRenteAccordee)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceRente).append(".").append(REAnnonceRente.FIELDNAME_ID_RENTE_ACCORDEE)
                        .append("=").append(this._dbWriteNumeric(statement.getTransaction(), forNoRenteAccordee));
            }

            if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableEnteteAnnonce).append(".").append(REAnnonceHeader.FIELDNAME_ETAT).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forCsEtat));
            }

            if (!JadeStringUtil.isIntegerEmpty(forCsCodeTraitement)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceRente).append(".").append(REAnnonceRente.FIELDNAME_CS_TRAITEMENT).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forCsCodeTraitement));
            }

            if (!JadeStringUtil.isIntegerEmpty(forCodeEnregistrement)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableEnteteAnnonce).append(".").append(REAnnonceHeader.FIELDNAME_CODE_ENREGISTREMENT_01)
                        .append("=").append(this._dbWriteString(statement.getTransaction(), forCodeEnregistrement));
            }

            if (!JadeStringUtil.isIntegerEmpty(forNumeroAnnonce)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_NUMERO_ANNONCE)
                        .append("=").append(this._dbWriteNumeric(statement.getTransaction(), forNumeroAnnonce));
            }

            if (!JadeStringUtil.isIntegerEmpty(forGenrePrestation)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceLevel1A).append(".")
                        .append(REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION).append("=")
                        .append(this._dbWriteString(statement.getTransaction(), forGenrePrestation));
            }

            if (!JadeStringUtil.isIntegerEmpty(forNssAyantDroit)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceLevel1A).append(".")
                        .append(REAnnoncesAbstractLevel1A.FIELDNAME_NO_ASS_AYANT_DROIT).append("=")
                        .append(this._dbWriteString(statement.getTransaction(), forNssAyantDroit));
            }

            if (!JadeStringUtil.isIntegerEmpty(forDebutDroit)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_DEBUT_DROIT)
                        .append("=").append(this._dbWriteString(statement.getTransaction(), forDebutDroit));
            }

            if (!JadeStringUtil.isIntegerEmpty(forFinDroit)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }

                sql.append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_FIN_DROIT)
                        .append("=").append(this._dbWriteString(statement.getTransaction(), forFinDroit));
            }

            if (isForAnnoncesSubsequentes) {
                if (sql.length() != 0) {
                    sql.append(" AND ");
                }
                sql.append(tableEnteteAnnonce).append(".").append(REAnnonceHeader.FIELDNAME_CODE_APPLICATION)
                        .append(" IN ('43', '46')");
                sql.append(" AND ");
                // BZ 9725 : mauvaise table pour le champ YXLCOM
                sql.append(tableAnnonceLevel1A).append(".").append(REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION)
                        .append(" IN ('77', '78')");

            } else {

                if (!isPRE0070.equals("true")) {
                    if (sql.length() != 0) {
                        sql.append(" AND ");
                    }
                    // BZ 4866 = Les annonces ponctuelles (89 / 99) doivent être envoyées
                    sql.append(tableAnnonceLevel1A).append(".")
                            .append(REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION).append(" NOT IN ('77', '78')");
                }
            }

            if ((idsRenteAccordeeIn != null) && !idsRenteAccordeeIn.isEmpty()) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableAnnonceRente).append(".").append(REAnnonceRente.FIELDNAME_ID_RENTE_ACCORDEE)
                        .append(" IN(");
                for (Iterator<Long> iterator = idsRenteAccordeeIn.iterator(); iterator.hasNext();) {
                    sql.append(iterator.next());
                    if (iterator.hasNext()) {
                        sql.append(",");
                    }
                }
                sql.append(")");
            }
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnoncesAbstractLevel1A();
    }

    @Override
    public List<REAnnoncesAbstractLevel1A> getContainerAsList() {
        List<REAnnoncesAbstractLevel1A> list = new ArrayList<REAnnoncesAbstractLevel1A>();
        for (int i = 0; i < size(); i++) {
            list.add((REAnnoncesAbstractLevel1A) get(i));
        }
        return list;
    }

    public String getForCodeEnregistrement() {
        return forCodeEnregistrement;
    }

    public String getForCsCodeTraitement() {
        return forCsCodeTraitement;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForDebutDroit() {
        return forDebutDroit;
    }

    public String getForFinDroit() {
        return forFinDroit;
    }

    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    public String getForMoisRapport() {
        return forMoisRapport;
    }

    public String getForNoRenteAccordee() {
        return forNoRenteAccordee;
    }

    public String getForNssAyantDroit() {
        return forNssAyantDroit;
    }

    public String getForNumeroAnnonce() {
        return forNumeroAnnonce;
    }

    public Set<Long> getIdsRenteAccordeeIn() {
        return idsRenteAccordeeIn;
    }

    public String getIsPRE0070() {
        return isPRE0070;
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

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REAnnonceHeader.FIELDNAME_ID_ANNONCE;
    }

    public boolean isForAnnoncesSubsequentes() {
        return isForAnnoncesSubsequentes;
    }

    public boolean isHasAvsHistory() {
        return hasAvsHistory;
    }

    public void setForAnnoncesSubsequentes(boolean isForAnnoncesSubsequentes) {
        this.isForAnnoncesSubsequentes = isForAnnoncesSubsequentes;
    }

    public void setForCodeEnregistrement(String forCodeEnregistrement) {
        this.forCodeEnregistrement = forCodeEnregistrement;
    }

    public void setForCsCodeTraitement(String forCsCodeTraitement) {
        this.forCsCodeTraitement = forCsCodeTraitement;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForDebutDroit(String forDebutDroit) {
        this.forDebutDroit = forDebutDroit;
    }

    public void setForFinDroit(String forFinDroit) {
        this.forFinDroit = forFinDroit;
    }

    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    public void setForIdsRenteAccordeeIn(Set<Long> idsRenteAccordeeIn) {
        this.idsRenteAccordeeIn = idsRenteAccordeeIn;
    }

    public void setForMoisRapport(String forMoisRapport) {
        this.forMoisRapport = forMoisRapport;
    }

    public void setForNoRenteAccordee(String forNoRenteAccordee) {
        this.forNoRenteAccordee = forNoRenteAccordee;
    }

    public void setForNssAyantDroit(String forNssAyantDroit) {
        this.forNssAyantDroit = forNssAyantDroit;
    }

    public void setForNumeroAnnonce(String forNumeroAnnonce) {
        this.forNumeroAnnonce = forNumeroAnnonce;
    }

    public void setHasAvsHistory(boolean hasAvsHistory) {
        this.hasAvsHistory = hasAvsHistory;
    }

    public void setIsPRE0070(String isPRE0070) {
        this.isPRE0070 = isPRE0070;
    }

    public void setIsRechercheFamille(boolean isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public void setRechercheFamille(String isRechercheFamille) {
        this.isRechercheFamille = Boolean.valueOf(isRechercheFamille);
    }
}
