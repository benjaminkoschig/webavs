package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPCommunicationFiscaleManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCanton = "";
    private boolean forComFisSansDec = false;
    private java.lang.String forDateComptabilisation = "";
    private Boolean forDateComptabilisationVide = Boolean.FALSE;
    private String forDateEnvoiAnnulation = "";
    private java.lang.String forDateRetour = "";
    private Boolean forDateRetourVide = Boolean.FALSE;
    private Boolean forDemandeActive = Boolean.FALSE;
    private Boolean forDemandeAnnulee = Boolean.FALSE;
    private Boolean forDemandeEnvoyee = Boolean.FALSE;
    private java.lang.String forExceptIdCommunication = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forIdAffiliation = "";
    private java.lang.String forIdCaisse = "";
    private java.lang.String forIdCommunication = "";
    private java.lang.String forIdIfd = "";
    private String forIdMessageSedex = "";
    private java.lang.String forIdTiers = "";
    private String[] forMalnafIdIfd = null;
    private String orderBy = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (isForComFisSansDec()) {
            return _getCollection() + "CPCOFIP LEFT OUTER JOIN " + _getCollection() + "CPDECIP ON " + _getCollection()
                    + "CPCOFIP.IBIDCF=" + _getCollection() + "CPDECIP.IBIDCF";
        } else {
            return _getCollection() + "CPCOFIP";
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            return "HTITIE, IBIDCF";
        } else {
            return getOrderBy();
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBTGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }
        // Les demandes annulées ont généralement été anvoyées => si on demande
        // les annulations ça évite ainsi
        // de devoir décocher la cas sauf celles déjà envoyées...
        if (getForDemandeEnvoyee().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "((IBDENV IS NOT NULL) AND (IBDENV <> 0))";
        }
        // traitement du positionnement
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        // Que les demandes annulées
        if (Boolean.TRUE.equals(getForDemandeAnnulee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IBCSUS = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // Que les demandes actives
        if (Boolean.TRUE.equals(getForDemandeActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IBCSUS = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (getForIdCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBICAI=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCaisse());
        }

        // traitement du positionnement
        if (getForIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBIDCF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCommunication());
        }

        // traitement du positionnement
        if (getForExceptIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBIDCF<>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdCommunication());
        }

        // traitement du positionnement
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        // traitement du positionnement
        if (getForCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBTCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForCanton());
        }

        // traitement du positionnement
        if (getForIdIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD=" + this._dbWriteNumeric(statement.getTransaction(), getForIdIfd());
        }
        if (isForComFisSansDec()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IBIDCF IS NULL ";
        }
        if (getForDateRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBDRET=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateRetour());
        }
        if (getForDateEnvoiAnnulation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBDANN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEnvoiAnnulation());
        }
        // Que les demandes non retournées
        if (Boolean.TRUE.equals(getForDateRetourVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBDRET=0";
        }
        // Que les demandes non coptabilisées
        if (Boolean.TRUE.equals(getForDateComptabilisationVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBDCPT=0";
        }
        if (getForDateComptabilisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBDCPT=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateComptabilisation());
        }
        if (getForIdMessageSedex().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBMEID=" + this._dbWriteString(statement.getTransaction(), getForIdMessageSedex());
        }
        if (getForMalnafIdIfd() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD = " + forMalnafIdIfd[1] + " AND HTITIE = " + "(select htitie from " + _getCollection()
                    + "AFAFFIP where malnaf = '" + forMalnafIdIfd[0] + "' group by htitie)";
        }
        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscale();
    }

    public String getCollection() {
        return _getCollection();
    }

    public String getForCanton() {
        return forCanton;
    }

    public java.lang.String getForDateComptabilisation() {
        return forDateComptabilisation;
    }

    public Boolean getForDateComptabilisationVide() {
        return forDateComptabilisationVide;
    }

    public String getForDateEnvoiAnnulation() {
        return forDateEnvoiAnnulation;
    }

    public java.lang.String getForDateRetour() {
        return forDateRetour;
    }

    public Boolean getForDateRetourVide() {
        return forDateRetourVide;
    }

    public Boolean getForDemandeActive() {
        return forDemandeActive;
    }

    public Boolean getForDemandeAnnulee() {
        return forDemandeAnnulee;
    }

    public Boolean getForDemandeEnvoyee() {
        return forDemandeEnvoyee;
    }

    public java.lang.String getForExceptIdCommunication() {
        return forExceptIdCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 09:13:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public java.lang.String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:04:06)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCaisse() {
        return forIdCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:02:42)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCommunication() {
        return forIdCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:03:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdIfd() {
        return forIdIfd;
    }

    public String getForIdMessageSedex() {
        return forIdMessageSedex;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:01:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public String[] getForMalnafIdIfd() {
        return forMalnafIdIfd;
    }

    /**
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @return
     */
    public boolean isForComFisSansDec() {
        return forComFisSansDec;
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

    /**
     * Tri par id communication Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdCommunicationDesc() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("IBIDCF DESC");
        } else {
            setOrderBy(getOrderBy() + ", IBIDCF DESC");
        }
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    /**
     * @param b
     */
    public void setForComFisSansDec(boolean b) {
        forComFisSansDec = b;
    }

    public void setForDateComptabilisation(java.lang.String forDateComptabilisation) {
        this.forDateComptabilisation = forDateComptabilisation;
    }

    public void setForDateComptabilisationVide(Boolean forDateComptabilisationVide) {
        this.forDateComptabilisationVide = forDateComptabilisationVide;
    }

    public void setForDateEnvoiAnnulation(String forDateEnvoiAnnulation) {
        this.forDateEnvoiAnnulation = forDateEnvoiAnnulation;
    }

    public void setForDateRetour(java.lang.String forDateRetour) {
        this.forDateRetour = forDateRetour;
    }

    public void setForDateRetourVide(Boolean forDateRetourVide) {
        this.forDateRetourVide = forDateRetourVide;
    }

    public void setForDemandeActive(Boolean isDemandeActive) {
        forDemandeActive = isDemandeActive;
    }

    public void setForDemandeAnnulee(Boolean isDemandeAnnulee) {
        forDemandeAnnulee = isDemandeAnnulee;
    }

    public void setForDemandeEnvoyee(Boolean forDemandeEnvoyee) {
        this.forDemandeEnvoyee = forDemandeEnvoyee;
    }

    public void setForExceptIdCommunication(java.lang.String forExceptIdCommunication) {
        this.forExceptIdCommunication = forExceptIdCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.03.2003 09:13:36)
     * 
     * @param newForGenreAffilie
     *            java.lang.String
     */
    public void setForGenreAffilie(java.lang.String newForGenreAffilie) {
        forGenreAffilie = newForGenreAffilie;
    }

    public void setForIdAffiliation(java.lang.String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:04:06)
     * 
     * @param newForIdCaisse
     *            java.lang.String
     */
    public void setForIdCaisse(java.lang.String newForIdCaisse) {
        forIdCaisse = newForIdCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:02:42)
     * 
     * @param newForIdCommunication
     *            java.lang.String
     */
    public void setForIdCommunication(java.lang.String newForIdCommunication) {
        forIdCommunication = newForIdCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:03:17)
     * 
     * @param newForIdIfd
     *            java.lang.String
     */
    public void setForIdIfd(java.lang.String newForIdIfd) {
        forIdIfd = newForIdIfd;
    }

    public void setForIdMessageSedex(String forIdMessageSedex) {
        this.forIdMessageSedex = forIdMessageSedex;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 15:01:34)
     * 
     * @param newForIdTiers
     *            java.lang.String
     */
    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForMalnafIdIfd(String[] forMalnafIdIfd) {
        this.forMalnafIdIfd = forMalnafIdIfd;
    }

    /**
     * @param string
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

}
