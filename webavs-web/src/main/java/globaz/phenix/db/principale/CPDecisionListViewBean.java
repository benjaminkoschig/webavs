package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.application.CPApplication;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPDecisionListViewBean extends BManager implements FWViewBeanInterface {

    private static final long serialVersionUID = 817472420485991599L;
    private String action;
    private String colonneSelection = "";
    private final String fieldTiers = "AFAFFIP.MAIAFF, MALNAF, HTLDE1, HTLDE2";
    private java.lang.String forAnnee = "";
    private java.lang.String forCotisation = "";
    private java.lang.String forEtat = "";
    private java.lang.String forProvenance = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forIdAffiliation = "";
    private java.lang.String forIdDecision = "";
    private java.lang.String forIdPassage = "";
    private java.lang.String forIdTiers = "";
    private java.lang.String forRevenuFortune = "";
    private java.lang.String forTypeDecision = "";
    private java.lang.String likeNom = "";
    private java.lang.String likeNumAffilie = "";
    private java.lang.String likePrenom = "";
    private java.lang.String order = "";
    private int totalSelection = 0;
    private Boolean useTiers = Boolean.FALSE;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        String champIdDecision = "CPDECIP.IAIDEC";
        String champDernierEtat = "IATETA";
        String champTypeDecision = "IATTDE";
        String champGenreAffilie = "IATGAF";
        String champDebutAffiliation = "MADDEB";
        String champFinAffiliation = "MADFIN";
        String champDateInformation = "IADINF";
        String champAnneeDecision = "IAANNE";
        String champDebutDecision = "IADDEB";
        String champFinDecision = "IADFIN";
        String champSpecification = "IATSPE";
        String champCotisationAnnuelle = "ISMCAN";
        String champRevenuFortune = "IHMDCA";
        String champMiseEnCompte = "IDCOT1";
        String champIfdDefinitif = "ICIIFD";
        String champIdTiers = "CPDECIP.HTITIE";
        String champDateFacturation = "IADFAC";
        String champActive = "COALESCE(IAACTI, '1') IAACTI";
        String champIdPassage = "EBIPAS";
        String champIDDemandePortail = "EBIDDP";
        // String champModifiable = "UBBMOD";
        String field = champIdDecision + " , " + champDernierEtat + " , " + champTypeDecision + " , "
                + champGenreAffilie + " , " + champDebutAffiliation + " , " + champFinAffiliation + " , "
                + champDateInformation + " , " + champDateFacturation + " , " + champAnneeDecision + " , "
                + champDebutDecision + " , " + champFinDecision + " , " + champSpecification + " , "
                + champCotisationAnnuelle + " , " + champRevenuFortune + " , " + champMiseEnCompte + " , "
                + champIfdDefinitif + " , " + champIdTiers + " , " + champIdPassage + " , " + champActive + ", " + champIDDemandePortail;
        // Ajout des champs du tiers pour écran de recherche (mandat 194)
        if (getUseTiers().equals(Boolean.TRUE)) {
            field = field + ", " + fieldTiers;
        }
        field = field + " ";
        return field;

    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = _getCollection() + "CPDECIP CPDECIP";
        String table3 = _getCollection() + "CPDOCAP CPDOCAP";
        String table4 = _getCollection() + "CPDOENP CPDOENP";
        String table5 = _getCollection() + "AFAFFIP AFAFFIP";
        String table8 = _getCollection() + "CPCOTIP CPCOTIP";
        String table9 = _getCollection() + "TIPAVSP TIPAVSP";
        String table10 = _getCollection() + "TITIERP TIERS";

        String fromTable = table1 + " INNER JOIN " + table4 + " ON CPDECIP.IAIDEC=CPDOENP.IAIDEC " + " INNER JOIN "
                + table5 + " ON CPDECIP.MAIAFF=AFAFFIP.MAIAFF" + " INNER JOIN " + table9
                + " ON CPDECIP.HTITIE=TIPAVSP.HTITIE" + " LEFT OUTER JOIN " + table3
                + " ON CPDECIP.IAIDEC=CPDOCAP.IAIDEC AND (IHIDCA=" + CPDonneesCalcul.CS_FORTUNE_TOTALE + " OR IHIDCA="
                + CPDonneesCalcul.CS_REV_NET + ")" + " LEFT OUTER JOIN " + table8
                + " ON CPDECIP.IAIDEC=CPCOTIP.IAIDEC and ISTGCO=812001";
        // Ajout des champs du tiers pour écran de recherche (mandat 194)
        if (getUseTiers().equals(Boolean.TRUE)) {
            fromTable = fromTable + " INNER JOIN " + table10 + " ON CPDECIP.HTITIE=TIERS.HTITIE";
        }
        return fromTable;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // Pour une décision
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPDECIP.IAIDEC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }

        // Pour un tiers
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPDECIP.HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        // Pour une affiliation
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPDECIP.MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        // Positionnement selon le nom
        if (!JadeStringUtil.isBlank(getLikeNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " HTLDE1 like " + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%");
        }
        // Positionnement selon le nom
        if (!JadeStringUtil.isBlank(getLikePrenom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " HTLDE2 like " + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%");
        }
        // Positionnement selon le n° affilié
        if (!JadeStringUtil.isBlank(getLikeNumAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MALNAF like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
        }
        // Pour un etat
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA=" + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }
        // Pour savoir si la cotisation personnelle provient du portail
        if(CPDecision.CS_PROVENANCE_PORTAIL.equals(getForProvenance())){
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EBIDDP > 0";
        }else if(CPDecision.CS_PROVENANCE_NON_PORTAIL.equals(getForProvenance())){
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(EBIDDP=0 OR EBIDDP is null)";
        }
        // Pour un type de décision
        if (getForTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeDecision());
        }
        // Pour un genre de décision
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }
        // Pour une année
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // Pour un revenu ou une fortune déterminant(e)
        if (getForRevenuFortune().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IHMDCA=" + this._dbWriteNumeric(statement.getTransaction(), getForRevenuFortune());
        }
        // Pour un montant de cotisation annuel avs
        if (getForCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ISMCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForCotisation());
        }
        // Pour un passage de facturation
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EBIPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionListerViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    public java.lang.String getForAnnee() {
        return forAnnee;
    }

    public java.lang.String getForCotisation() {
        return forCotisation;
    }

    public java.lang.String getForEtat() {
        return forEtat;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:21:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdDecision() {
        return forIdDecision;
    }

    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public java.lang.String getForRevenuFortune() {
        return forRevenuFortune;
    }

    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    public java.lang.String getLikeNom() {
        return likeNom;
    }

    public java.lang.String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public java.lang.String getLikePrenom() {
        return likePrenom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.08.2003 09:54:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    public int getTotalSelection() {
        return totalSelection;
    }

    public Boolean getUseTiers() {
        return useTiers;
    }

    /**
     * Détermine si on doit prendre pour la date de décision la date d'information ou la date de facturation Date de
     * création : (7.10.2004 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAffichageDateFacturation() throws Exception {
        try {
            return ((CPApplication) getSession().getApplication()).isAffichageDateFacturation();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return true;
        }
    }

    /**
     * Détermine si on doit afficher la colonne mise en compte Date de création : (7.10.2004 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAffichageMiseEnCompte() throws Exception {
        try {
            return ((CPApplication) getSession().getApplication()).isAffichageMiseEnCompte();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return true;
        }
    }

    /**
     * Détermine si on doit afficher la colonne spécialité Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAffichageSpecification() throws Exception {
        try {
            return ((CPApplication) getSession().getApplication()).isAffichageSpecification();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return false;
        }
    }

    /**
     * Tri par année de décision. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDecision() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("CPDECIP.IAANNE DESC");
        } else {
            setOrder(getOrder() + ", CPDECIP.IAANNE DESC");
        }
    }

    public void orderByDateComptabilisation() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("CPDECIP.IADFAC DESC");
        } else {
            setOrder(getOrder() + ", CPDECIP.IADFAC DESC");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateDecision() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("CPDECIP.IADINF DESC");
        } else {
            setOrder(getOrder() + ", CPDECIP.IADINF DESC");
        }
    }

    public void orderByEtatDecision() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("CPDECIP.IATETA");
        } else {
            setOrder(getOrder() + ", CPDECIP.IATETA");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdDecision() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("CPDECIP.IAIDEC DESC");
        } else {
            setOrder(getOrder() + ", CPDECIP.IAIDEC DESC");
        }
    }

    /**
     * Tri par idTiers. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdTiers() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("CPDECIP.HTITIE");
        } else {
            setOrder(getOrder() + ", CPDECIP.HTITIE");
        }
    }

    /**
     * Trie par le nom Date de création : (21.08.2007 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNom() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("HTLDE1");
        } else {
            setOrder(getOrder() + ", HTLDE1");
        }
    }

    /**
     * Trie par n° d'afiilié Date de création : (21.08.2007 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumAffilie() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("MALNAF");
        } else {
            setOrder(getOrder() + ", MALNAF");
        }
    }

    /**
     * Trie par le prénom Date de création : (21.08.2007 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByPrenom() {
        if (JadeStringUtil.isBlank(getOrder())) {
            setOrder("HTLDE2");
        } else {
            setOrder(getOrder() + ", HTLDE2");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }

    public void setForAnnee(java.lang.String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCotisation(java.lang.String forCotisation) {
        this.forCotisation = forCotisation;
    }

    public void setForEtat(java.lang.String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:21:11)
     * 
     * @param newForIdAffiliation
     *            java.lang.String
     */
    public void setForIdAffiliation(java.lang.String newForIdAffiliation) {
        forIdAffiliation = newForIdAffiliation;
    }

    /**
     * Setter
     */
    public void setForIdDecision(java.lang.String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    public void setForIdPassage(java.lang.String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForRevenuFortune(java.lang.String forRevenuFortune) {
        this.forRevenuFortune = forRevenuFortune;
    }

    public void setForTypeDecision(java.lang.String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    public void setLikeNom(java.lang.String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumAffilie(java.lang.String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void setLikePrenom(java.lang.String likePrenom) {
        this.likePrenom = likePrenom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.08.2003 09:54:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    public void setTotalSelection(int totalSelection) {
        this.totalSelection = totalSelection;
    }

    public void setUseTiers(Boolean useTiers) {
        this.useTiers = useTiers;
    }

    public String getForProvenance() {
        return forProvenance;
    }

    public void setForProvenance(String forProvenance) {
        this.forProvenance = forProvenance;
    }
}
