package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPSortieManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IAANNE) */
    private String forAnnee = "";
    /**
     * Fichier CPSORTP
     */
    /** (IZBCHK) */
    private Boolean forChecked = null;
    /** (MAIAFF) */
    private String forIdAffiliation = "";
    /** (IAIDEC) */
    private String forIdDecision = "";

    /** (EBIPAS) */
    private String forIdPassage = "";

    private String forIdTiers = "";
    private Boolean forIsComptabilise = null;
    /** (MALNAF) */
    private String forNoAffilie = "";
    private String fromAnnee = "";
    /** (MALNAF) */
    private String fromNoAffilie = "";
    private java.lang.String order = "";
    private String untilAnnee = "";

    public CPSortieManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPSORTP";
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
        return getOrder();
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
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        // Pour un tiers
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        // Pour une décision
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }
        // traitement du positionnement
        if (getForNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNoAffilie());
        }
        // Depuis le n° d'affilié
        if (getFromNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getFromNoAffilie());
        }
        // traitement du positionnement
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EBIPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());

        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());

        }
        // traitement du positionnement
        if (getUntilAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE<=" + this._dbWriteNumeric(statement.getTransaction(), getUntilAnnee());
        }

        // traitement du positionnement
        if (getFromAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromAnnee());
        }

        if (getForChecked() != null) {
            // Si la case est cochée, on affiche tous les résultats
            if (!getForChecked().booleanValue()) {
                // Sinon on affiche que les sorties non comptabilisées
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "IZBCHK='1'";
            }
        }

        if (getForIsComptabilise() != null) {
            // Si la case est cochée, on affiche tous les résultats
            if (getForIsComptabilise().booleanValue()) {
                // Sinon on affiche que les sorties non comptabilisées
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "IZBCHK='1'";
            }
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
        return new CPSortie();
    }

    /**
     * Returns the forAnnee.
     * 
     * @return String
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Returns the forChecked.
     * 
     * @return Boolean
     */
    public Boolean getForChecked() {
        return forChecked;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * Returns the forIdPassage.
     * 
     * @return String
     */
    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public Boolean getForIsComptabilise() {
        return forIsComptabilise;
    }

    public String getForNoAffilie() {
        return forNoAffilie;
    }

    public String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * @return
     */
    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    /**
     * Returns the order.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * @return
     */
    public String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * Tri par idPassage Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnnee() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAANNE ASC");
        } else {
            setOrder(getOrder() + ", IAANNE ASC");
        }
    }

    /**
     * Tri par annee desc Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDESC() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAANNE DESC");
        } else {
            setOrder(getOrder() + ", IAANNE DESC");
        }
    }

    /**
     * Tri par id décision Date de création : (29.07.2005 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAIDEC");
        } else {
            setOrder(getOrder() + ", IAIDEC");
        }
    }

    /**
     * Tri par idPassage Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdPassageDESC() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("EBIPAS DESC");
        } else {
            setOrder(getOrder() + ", EBIPAS DESC");
        }
    }

    /**
     * Tri par id sortie Date de création : (29.07.2005 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdSortie() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IZISOR");
        } else {
            setOrder(getOrder() + ", IZISOR");
        }
    }

    public void orderByMontantCiDESC() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IZMMCI DESC");
        } else {
            setOrder(getOrder() + ", IZMMCI DESC");
        }
    }

    /**
     * Tri par No Affiliés Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNoAffilie() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("MALNAF ASC");
        } else {
            setOrder(getOrder() + ", MALNAF ASC");
        }
    }

    /**
     * Tri par No Affiliés Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNoAffilieDESC() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("MALNAF DESC");
        } else {
            setOrder(getOrder() + ", MALNAF DESC");
        }
    }

    /**
     * Sets the forAnnee.
     * 
     * @param forAnnee
     *            The forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * Sets the forChecked.
     * 
     * @param forChecked
     *            The forChecked to set
     */
    public void setForChecked(Boolean checked) {
        forChecked = checked;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdAffiliation(String newForIdAffiliation) {
        forIdAffiliation = newForIdAffiliation;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    /**
     * Sets the forIdPassage.
     * 
     * @param forIdPassage
     *            The forIdPassage to set
     */
    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIsComptabilise(Boolean forIsComptabilise) {
        this.forIsComptabilise = forIsComptabilise;
    }

    public void setForNoAffilie(String newForNoAffilie) {
        forNoAffilie = newForNoAffilie;
    }

    public void setFromAnnee(String fromAnnee) {
        this.fromAnnee = fromAnnee;
    }

    /**
     * @param string
     */
    public void setFromNoAffilie(String string) {
        fromNoAffilie = string;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(java.lang.String order) {
        this.order = order;
    }

    /**
     * @param string
     */
    public void setUntilAnnee(String string) {
        untilAnnee = string;
    }

}