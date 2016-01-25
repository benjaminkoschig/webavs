/*
 * Created on 25 nov. 05
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;

/**
 * @author mar
 */
public class CPCommunicationFiscaleAffichageManager extends BManager {

    private static final long serialVersionUID = -7046284911329491378L;

    private String dateEdition = "";

    private Boolean dateEnvoiNonVide = new Boolean(false);

    private Boolean dateEnvoiVide = new Boolean(false);

    private Boolean demandeAnnulee = new Boolean(false);

    private Boolean demandeAnnuleeOuPas = new Boolean(false);

    private Boolean exceptComptabilise = new Boolean(false);

    private String exceptGenreAffilie = "";

    private Boolean exceptNumContibuableVide = new Boolean(false);

    private Boolean exceptRetour = new Boolean(false);

    private String field = "";

    private String forAnneeDecision = "";

    private String forCanton = "";
    private String forDateEnvoi = "";
    private String forDateEnvoiAnnulation = "";
    private String forGenreAffilie = "";
    private String forIdCommunication = "";
    private String forIdIfd = "";
    private String forIdTiers = "";
    private boolean forListerCantons = false;
    private String forNumAffilie = "";
    private String forNumIfd = "";

    private java.lang.String inGenreAffilie = "";

    private Boolean isEmptyOrEtranger = Boolean.FALSE;
    private String likeNomPrenom = "";

    private String likeNumAffilie = "";

    private String likeNumContri = "";
    private ArrayList<String> listeCas = new ArrayList<String>();
    private Boolean nonRecu = new Boolean(false);
    private java.lang.String notInGenreAffilie = "";
    private String orderBy = "";
    private String reportType = "";
    private Boolean withAnneeEnCours = new Boolean(false);

    /**
	 * 
	 */
    public CPCommunicationFiscaleAffichageManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        if (isForListerCantons()) {
            return " IBTCAN ";
        }
        if (JadeStringUtil.isEmpty(field)) {
            return " IBAPRI , IBCANO , IBDENV , IBDRET , IBTGAF , IBDCPT, IBTCAN, IBICAI , " + _getCollection()
                    + "AFAFFIP.MAIAFF , " + _getCollection() + "CPCOFIP.IBIDCF , " + _getCollection()
                    + "CPCOFIP.ICIIFD , " + _getCollection() + "CPPEFIP.ICNIFD," + _getCollection() + "CPPEFIP.ICANRD,"
                    + _getCollection() + "CPPEFIP.ICANRF," + _getCollection() + "CPCOFIP.IBDANN," + _getCollection()
                    + "CPCOFIP.HTITIE ,IBCSUS, "
                    + "HTLDE1 , HTLDE2, MALNAF, HXNCON, HXNAVS, ICANDD, HPDNAI, HPTSEX, HNIPAY";

        } else {
            return field;
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPCOFIP" + " INNER JOIN " + _getCollection() + "CPPEFIP ON (" + _getCollection()
                + "CPCOFIP.ICIIFD=" + _getCollection() + "CPPEFIP.ICIIFD) " + "INNER JOIN " + _getCollection()
                + "TITIERP ON (" + _getCollection() + "CPCOFIP.HTITIE=" + _getCollection() + "TITIERP.HTITIE) "
                + "INNER JOIN " + _getCollection() + "TIPAVSP ON (" + _getCollection() + "TIPAVSP.HTITIE="
                + _getCollection() + "TITIERP.HTITIE) " + "INNER JOIN " + _getCollection() + "TIPERSP ON ("
                + _getCollection() + "TIPERSP.HTITIE=" + _getCollection() + "TITIERP.HTITIE) " + "INNER JOIN "
                + _getCollection() + "AFAFFIP ON (" + _getCollection() + "CPCOFIP.MAIAFF=" + _getCollection()
                + "AFAFFIP.MAIAFF)";
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (!isForListerCantons()) {
            if (JadeStringUtil.isEmpty(getOrderBy())) {
                return "HXNCON, ICANDD";
            } else {
                return getOrderBy();
            }
        } else {
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer();
        // _getCollection()
        // + "CPDECIP.IAIDEC=(SELECT MAX(T1.IAIDEC) FROM "
        // + _getCollection()
        // + "CPDECIP T1 WHERE T1.IBIDCF = "
        // + _getCollection()
        // + "CPCOFIP.IBIDCF";

        // Il est très important de mettre dans le sous-select
        // tout ce qui concerne les champs de la décision
        // traitement du positionnement
        if (getLikeNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("MALNAF LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%"));
        }
        if (getForNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }
        if (getLikeNumContri().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("HXNCON LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContri() + "%"));
        }
        if (getForAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("ICANDD=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDecision()));
        }
        // traitement du positionnement
        if (getLikeNomPrenom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(HTLDE1 LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNomPrenom() + "%"));
            sqlWhere.append(" OR HTLDE2 LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNomPrenom() + "%") + ")");
        }

        // traitement du positionnement
        if (isEmptyOrEtranger()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBTCAN in (0, " + IConstantes.CS_LOCALITE_ETRANGER + ")");
        }

        // traitement du positionnement
        if (getForCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBTCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForCanton()));
        }
        //
        if (getDateEnvoiVide().equals(Boolean.TRUE) && Boolean.FALSE.equals(getDemandeAnnulee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("((IBDENV = 0) OR (IBDENV IS NULL))");
        }
        if (getDateEnvoiVide().equals(Boolean.TRUE) && Boolean.TRUE.equals(getDemandeAnnulee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("((IBDANN = 0) OR (IBDANN IS NULL))");
        }
        //
        if (getDateEnvoiNonVide().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("((IBDENV != 0) AND (IBDENV IS NOT NULL))");
        }
        // traitement du positionnement
        if (getNonRecu().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("((IBDRET = 0) OR (IBDRET IS NULL))");
        }
        // traitement du positionnement
        if (getExceptNumContibuableVide().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("HXNCON<> ' '");
        }

        // traitement du positionnement
        if (getWithAnneeEnCours().equals(Boolean.FALSE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("ICANDD <> "
                    + this._dbWriteNumeric(statement.getTransaction(), Integer.toString(JACalendar.today().getYear())));
        }
        if (getForIdIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection() + "CPCOFIP.ICIIFD="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdIfd()));
        }

        // traitement du positionnement
        if (getForNumIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("ICNIFD=" + this._dbWriteNumeric(statement.getTransaction(), getForNumIfd()));
        }

        if (getForIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBIDCF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCommunication()));
        }
        if (getForDateEnvoi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBDENV=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEnvoi()));
        }
        if (getForDateEnvoiAnnulation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBDANN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEnvoiAnnulation()));
        }
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBTGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie()));
        }
        if (getExceptGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBTGAF<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptGenreAffilie()));
        }
        if (getExceptRetour().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBDRET=0");
        }
        if (getExceptComptabilise().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBDCPT=0");
        }
        // Compris dans une sélection
        if (getInGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBTGAF in (" + getInGenreAffilie() + ")");
        }
        // Non inclus dans une sélection
        if (getNotInGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBTGAF not in (" + getNotInGenreAffilie() + ")");
        }
        // IdTiers
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection() + "CPCOFIP.HTITIE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }
        // traitement du GROUP BY
        // if (_getGroupBy(statement).length() != 0) {
        // sqlWhere.append( " GROUP BY " + _getGroupBy(statement);
        // }

        // Demande annulee ou pas
        if (!Boolean.TRUE.equals(getDemandeAnnuleeOuPas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("IBCSUS = "
                    + this._dbWriteBoolean(statement.getTransaction(), getDemandeAnnulee(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (!JadeStringUtil.isBlank(getReportType())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" IKRETY = " + getReportType());
        }

        if (listeCas.size() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" ibidcf in (");
            for (int i = 0; i < listeCas.size(); i++) {
                sqlWhere.append(listeCas.get(i));
                if (i < listeCas.size() - 1) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(")");
        }

        if (isForListerCantons()) {
            sqlWhere.append(" GROUP BY IBTCAN ");
        }
        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleAffichage();
    }

    /**
     * @return
     */
    public String getDateEdition() {
        return dateEdition;
    }

    public Boolean getDateEnvoiNonVide() {
        return dateEnvoiNonVide;
    }

    public Boolean getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    /**
     * @return
     */
    public Boolean getDemandeAnnulee() {
        return demandeAnnulee;
    }

    public Boolean getDemandeAnnuleeOuPas() {
        return demandeAnnuleeOuPas;
    }

    public Boolean getExceptComptabilise() {
        return exceptComptabilise;
    }

    /**
     * @return
     */
    public String getExceptGenreAffilie() {
        return exceptGenreAffilie;
    }

    /**
     * @return
     */
    public Boolean getExceptNumContibuableVide() {
        return exceptNumContibuableVide;
    }

    /**
     * @return
     */
    public Boolean getExceptRetour() {
        return exceptRetour;
    }

    /**
     * @return
     */
    public String getForAnneeDecision() {
        return forAnneeDecision;
    }

    /**
     * @return
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * @return
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    public String getForDateEnvoiAnnulation() {
        return forDateEnvoiAnnulation;
    }

    /**
     * @return
     */
    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * @return
     */
    public String getForIdCommunication() {
        return forIdCommunication;
    }

    public String getForIdIfd() {
        return forIdIfd;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    /**
     * @return
     */
    public String getForNumIfd() {
        return forNumIfd;
    }

    public java.lang.String getInGenreAffilie() {
        return inGenreAffilie;
    }

    /**
     * @return
     */
    public String getLikeNomPrenom() {
        return likeNomPrenom;
    }

    /**
     * @return
     */
    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    /**
     * @return
     */
    public String getLikeNumContri() {
        return likeNumContri;
    }

    public ArrayList<String> getListeCantons() {
        ArrayList<String> cantons = new ArrayList<String>();
        setForListerCantons(true);
        try {
            this.find();
            setForListerCantons(false);
            for (int i = 0; i < size(); i++) {
                CPCommunicationFiscaleAffichage entity = (CPCommunicationFiscaleAffichage) get(i);
                cantons.add(entity.getCanton());
            }
            return cantons;
        } catch (Exception e) {
            return null;
        }

    }

    public ArrayList<String> getListeCas() {
        return listeCas;
    }

    /**
     * @return
     */
    public Boolean getNonRecu() {
        return nonRecu;
    }

    public java.lang.String getNotInGenreAffilie() {
        return notInGenreAffilie;
    }

    /**
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    public String getReportType() {
        return reportType;
    }

    public Boolean getWithAnneeEnCours() {
        return withAnneeEnCours;
    }

    public Boolean isEmptyOrEtranger() {
        return isEmptyOrEtranger;
    }

    public boolean isForListerCantons() {
        return forListerCantons;
    }

    /**
     * Tri par anne Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnnee() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("ICANDD ASC");
        } else {
            setOrderBy(getOrderBy() + ", ICANDD ASC");
        }
    }

    /**
     * Tri par canton Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByCanton() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("IBTCAN ASC");
        } else {
            setOrderBy(getOrderBy() + ", IBTCAN ASC");
        }
    }

    /**
     * Tri par id communication Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdCommunication() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("IBIDCF ASC");
        } else {
            setOrderBy(getOrderBy() + ", IBIDCF ASC");
        }
    }

    public void orderByIdCommunicationDesc() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("IBIDCF DESC");
        } else {
            setOrderBy(getOrderBy() + ", IBIDCF DESC");
        }
    }

    /**
     * Tri par nom Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNom() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("HTLDE1 ASC");
        } else {
            setOrderBy(getOrderBy() + ", HTLDE1 ASC");
        }
    }

    /**
     * Tri par n° affilié Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumAffilie() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("MALNAF ASC");
        } else {
            setOrderBy(getOrderBy() + ", MALNAF ASC");
        }
    }

    /**
     * Tri par n° avs Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumAVS() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("HXNAVS ASC");
        } else {
            setOrderBy(getOrderBy() + ", HXNAVS ASC");
        }
    }

    /**
     * Tri par n° de contribuable Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumContribuable() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("HXNCON ASC");
        } else {
            setOrderBy(getOrderBy() + ", HXNCON ASC");
        }
    }

    /**
     * Tri par prénom Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByPrenom() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("HTLDE2 ASC");
        } else {
            setOrderBy(getOrderBy() + ", HTLDE2 ASC");
        }
    }

    /**
     * Tri par nom Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByTypeAffilie() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("IBTGAF ASC");
        } else {
            setOrderBy(getOrderBy() + ", IBTGAF ASC");
        }
    }

    /**
     * @param string
     */
    public void setDateEdition(String string) {
        dateEdition = string;
    }

    public void setDateEnvoiNonVide(Boolean dateEnvoiNonVide) {
        this.dateEnvoiNonVide = dateEnvoiNonVide;
    }

    public void setDateEnvoiVide(Boolean dateEnvoiVide) {
        this.dateEnvoiVide = dateEnvoiVide;
    }

    /**
     * @param boolean1
     */
    public void setDemandeAnnulee(Boolean boolean1) {
        demandeAnnulee = boolean1;
    }

    public void setDemandeAnnuleeOuPas(Boolean demandeAnnuleeOuPas) {
        this.demandeAnnuleeOuPas = demandeAnnuleeOuPas;
    }

    public void setEmptyOrEtranger(Boolean isEmptyOrEtranger) {
        this.isEmptyOrEtranger = isEmptyOrEtranger;
    }

    public void setExceptComptabilise(Boolean exceptComptabilise) {
        this.exceptComptabilise = exceptComptabilise;
    }

    /**
     * @param string
     */
    public void setExceptGenreAffilie(String string) {
        exceptGenreAffilie = string;
    }

    /**
     * @param boolean1
     */
    public void setExceptNumContibuableVide(Boolean boolean1) {
        exceptNumContibuableVide = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setExceptRetour(Boolean boolean1) {
        exceptRetour = boolean1;
    }

    /**
     * @param string
     */
    public void setForAnneeDecision(String string) {
        forAnneeDecision = string;
    }

    /**
     * @param string
     */
    public void setForCanton(String string) {
        forCanton = string;
    }

    /**
     * @param string
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    public void setForDateEnvoiAnnulation(String forDateEnvoiAnnulation) {
        this.forDateEnvoiAnnulation = forDateEnvoiAnnulation;
    }

    /**
     * @param string
     */
    public void setForGenreAffilie(String string) {
        forGenreAffilie = string;
    }

    /**
     * @param string
     */
    public void setForIdCommunication(String string) {
        forIdCommunication = string;
    }

    public void setForIdIfd(String forIdIfd) {
        this.forIdIfd = forIdIfd;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForListerCantons(boolean forListerCantons) {
        this.forListerCantons = forListerCantons;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    /**
     * @param string
     */
    public void setForNumIfd(String string) {
        forNumIfd = string;
    }

    public void setInGenreAffilie(java.lang.String inGenreAffilie) {
        this.inGenreAffilie = inGenreAffilie;
    }

    /**
     * @param string
     */
    public void setLikeNomPrenom(String string) {
        likeNomPrenom = string;
    }

    /**
     * @param string
     */
    public void setLikeNumAffilie(String string) {
        likeNumAffilie = string;
    }

    /**
     * @param string
     */
    public void setLikeNumContri(String string) {
        likeNumContri = string;
    }

    public void setListeCas(ArrayList<String> listeCas) {
        this.listeCas = listeCas;
    }

    /**
     * @param boolean1
     */
    public void setNonRecu(Boolean boolean1) {
        nonRecu = boolean1;
    }

    public void setNotInGenreAffilie(java.lang.String notInGenreAffilie) {
        this.notInGenreAffilie = notInGenreAffilie;
    }

    /**
     * @param string
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setWithAnneeEnCours(Boolean withAnneeEnCours) {
        this.withAnneeEnCours = withAnneeEnCours;
    }

}
