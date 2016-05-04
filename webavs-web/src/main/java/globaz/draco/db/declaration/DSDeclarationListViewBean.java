package globaz.draco.db.declaration;

import globaz.draco.application.DSApplication;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

public class DSDeclarationListViewBean extends BManager {

    private static final long serialVersionUID = 4342524597579156133L;
    /** (MAIAFF) */
    private String forAffiliationId = new String();
    /** (TAANNE) */
    private String forAnnee = new String();
    private String forDateFlag = new String();
    private String forDateFlagDecompteImpotLtn = new String();
    private Boolean forDateFlagIsNullOrZero = new Boolean(false);
    private Boolean forDateImpressionDecompteLtnIsNullOrZero = new Boolean(false);

    /** (TADREF) */
    private String forDateRetourEffGreaterOrEquals = new String();
    private String forDateRetourEffLowerOrEquals = new String();
    /**
     * Sélection de l'état de la déclaration CS_OUVERT CS_AFACTURER CS_COMPTABILISE
     */
    private String forEtat = new String();
    private String forEtatNotLike = new String();
    /** TAICTR */
    private String forIdControlEmployeur = new String();
    /** Fichier DSDECLP */
    /** (TAIDDE) */
    private String forIdDeclaration = new String();
    private String forIdDeclarationDistante = new String();
    private String forIdJournal = new String();

    /** (EAID) */
    private String forIdPassageFac = new String();
    private Boolean forIdPucsFileNotEmpty = new Boolean(false);
    /** (TAICTR) */
    private Boolean forIsIdRapportVide = new Boolean(false);
    /** (TANDEC) */
    private String forNoDecompte = new String();
    private String forNotTypeDeclaration = "";
    private String forProvenance = "";
    private String forSelectionTri = new String();
    /** (TATTYP) */
    private String forTypeDeclaration = new String();
    /**
     * selection du tri 1 = Par année et numéro d'affilié 2 = Par année et nom d'affilié 3 = Par numéro d'affilié 4 =
     * Par nom d'affilié
     */
    private String fromAffilie = new String();
    /** (TAANNE) */
    private String fromAnnee = new String();
    /** (TAIDDE) */
    private String fromIdDeclaration = new String();
    /** (TANDEC) */
    private String fromNoDecompte = new String();
    /** (TATTYP) */
    private String fromTypeDeclaration = new String();
    private List<String> inTypeDeclaration = new ArrayList<String>();
    private String likeNumeroAffilie = new String();
    private java.lang.String order = new String();
    private String toAffilie = new String();
    private String notForIdDeclaration = new String();

    @Override
    protected String _getFrom(BStatement statement) {

        if (!JadeStringUtil.isBlank(getForSelectionTri())) {
            return _getCollection() + "DSDECLP INNER JOIN " + _getCollection() + "AFAFFIP ON (" + _getCollection()
                    + "DSDECLP.MAIAFF=" + _getCollection() + "AFAFFIP.MAIAFF)" + " INNER JOIN " + _getCollection()
                    + "TITIERP ON (" + _getCollection() + "AFAFFIP.HTITIE=" + _getCollection() + "TITIERP.HTITIE)";
        } else {
            return _getCollection() + "DSDECLP";
        }
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (getForSelectionTri().equals("1")) {
            // d'affilié
            return "TAANNE DESC, MALNAF";
        } else if (getForSelectionTri().equals("2")) {
            // d'affilié
            return "TAANNE DESC, HTLDE1";
        } else if (getForSelectionTri().equals("3")) {
            // d'affilié
            return "MALNAF, TAANNE";
        } else if (getForSelectionTri().equals("4")) {
            return "HTLDE1";
        }

        if (!JadeStringUtil.isBlankOrZero(getOrder())) {
            return getOrder();
        }

        return "";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDeclaration());
        }

        // traitement du positionnement
        if (getNotForIdDeclaration().isEmpty()) {
            if (!sqlWhere.isEmpty()) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE <> " + this._dbWriteNumeric(statement.getTransaction(), getNotForIdDeclaration());
        }

        // traitement du positionnement
        if (getForAffiliationId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

        if (getForDateRetourEffGreaterOrEquals().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TADREF>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateRetourEffGreaterOrEquals());
        }

        // traitement du positionnement
        if (getForIdPassageFac().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EAID=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassageFac());
        }
        // traitement du positionnement
        if (getForNoDecompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TANDEC=" + this._dbWriteString(statement.getTransaction(), getForNoDecompte());
        }
        // traitement du positionnement
        if (getForTypeDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TATTYP=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeDeclaration());
        }
        // Pour exclure un type de déclaration
        if (getForNotTypeDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TATTYP<>" + this._dbWriteNumeric(statement.getTransaction(), getForNotTypeDeclaration());
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAANNE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // traitement du positionnement
        if (getFromIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdDeclaration());
        }
        // traitement du positionnement
        if (getFromNoDecompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TANDEC>=" + this._dbWriteString(statement.getTransaction(), getFromNoDecompte());
        }
        // traitement du positionnement
        if (getFromTypeDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TATTYP>=" + this._dbWriteNumeric(statement.getTransaction(), getFromTypeDeclaration());
        }
        // traitement du positionnement
        if (getFromAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAANNE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromAnnee());
        }
        // traitement du positionnement
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TATETA=" + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }
        // traitement du positionnement
        if (getForEtatNotLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TATETA<>" + this._dbWriteNumeric(statement.getTransaction(), getForEtatNotLike());
        }
        // traitement du positionnement
        if (getFromAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF>=" + this._dbWriteString(statement.getTransaction(), getFromAffilie());
        }
        if (getToAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF<=" + this._dbWriteString(statement.getTransaction(), getToAffilie());
        }
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KCID=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        // traitement du positionnement
        if (getForDateFlag().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TADATT=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFlag());
        }
        // traitement du positionnement
        if (getForDateFlagDecompteImpot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TADANT=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFlagDecompteImpot());
        }
        // traitement du positionnement
        if (getForDateFlagIsNullOrZero().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(TADATT IS NULL OR TADATT=0) ";
        }
        if (getForDateImpressionDecompteLtnIsNullOrZero().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(TADANT IS NULL OR TADANT=0 OR TADANT=11111111) ";
        }
        if (getLikeNumeroAffilie().length() != 0) {
            DSApplication dsApp = null;
            String like = "";
            try {
                dsApp = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                        DSApplication.DEFAULT_APPLICATION_DRACO);
                IFormatData affilieFormater = dsApp.getAffileFormater();
                like = affilieFormater.format(getLikeNumeroAffilie());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // composant de la requete initialises avec les options par defaut
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF like '" + like + "%' ";
        }
        if (getForIdDeclarationDistante().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDIS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDeclarationDistante());
        }
        if (getForIsIdRapportVide().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(TAICTR IS NULL OR TAICTR=0)";
        }
        if (getForDateRetourEffLowerOrEquals().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TADREF<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateRetourEffLowerOrEquals());
        }

        if ((getInTypeDeclaration() != null) && (getInTypeDeclaration().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += (" AND ");
            }
            sqlWhere += "TATTYP IN(";
            boolean isFirstElement = true;
            for (String type : getInTypeDeclaration()) {
                if (isFirstElement) {
                    isFirstElement = false;
                } else {
                    sqlWhere += ",";
                }
                sqlWhere += type;
            }
            sqlWhere += ")";
        }

        if (getForIdPucsFileNotEmpty().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(TAIDPU IS NOT NULL AND TAIDPU <> '0')";
        }

        if (!JadeStringUtil.isEmpty(getForProvenance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAPROV = '" + getForProvenance() + "'";
        }

        if (getForIdControlEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAICTR=" + this._dbWriteNumeric(statement.getTransaction(), getForIdControlEmployeur());
        }

        // traitement du positionnement
        // Numéro d'affilié
        /*
         * if (getForSelectionTri().equals("1") || getForSelectionTri().equals("3")) { if (getFromAffilie().length() !=
         * 0) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere += "MALNAF>=" + _dbWriteString(
         * statement.getTransaction(), getFromAffilie()); } } // Nom de l'affilié /*if (getForSelectionTri().equals("2")
         * || getForSelectionTri().equals("4")) { if (getFromAffilie().length() != 0) { if (sqlWhere.length() != 0) {
         * sqlWhere += " AND "; } sqlWhere += "HTLDE1>=" + _dbWriteString( statement.getTransaction(),
         * getFromAffilie()); } }
         */
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSDeclarationViewBean();
    }

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDateFlag() {
        return forDateFlag;
    }

    public String getForDateFlagDecompteImpot() {
        return forDateFlagDecompteImpotLtn;
    }

    public Boolean getForDateFlagIsNullOrZero() {
        return forDateFlagIsNullOrZero;
    }

    public Boolean getForDateImpressionDecompteLtnIsNullOrZero() {
        return forDateImpressionDecompteLtnIsNullOrZero;
    }

    public String getForDateRetourEffGreaterOrEquals() {
        return forDateRetourEffGreaterOrEquals;
    }

    public String getForDateRetourEffLowerOrEquals() {
        return forDateRetourEffLowerOrEquals;
    }

    /**
     * Returns the forEtat.
     * 
     * @return String
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * Returns the forEtatNotLike.
     * 
     * @return String
     */
    public String getForEtatNotLike() {
        return forEtatNotLike;
    }

    public String getForIdControlEmployeur() {
        return forIdControlEmployeur;
    }

    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    public String getForIdDeclarationDistante() {
        return forIdDeclarationDistante;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdPassageFac() {
        return forIdPassageFac;
    }

    public Boolean getForIdPucsFileNotEmpty() {
        return forIdPucsFileNotEmpty;
    }

    public Boolean getForIsIdRapportVide() {
        return forIsIdRapportVide;
    }

    public String getForNoDecompte() {
        return forNoDecompte;
    }

    public String getForNotTypeDeclaration() {
        return forNotTypeDeclaration;
    }

    public String getForProvenance() {
        return forProvenance;
    }

    /**
     * Gets the forSelectionTri
     * 
     * @return Returns a String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    public String getForTypeDeclaration() {
        return forTypeDeclaration;
    }

    /**
     * Returns the fromAffilie.
     * 
     * @return String
     */
    public String getFromAffilie() {
        return fromAffilie;
    }

    public String getFromAnnee() {
        return fromAnnee;
    }

    public String getFromIdDeclaration() {
        return fromIdDeclaration;
    }

    public String getFromNoDecompte() {
        return fromNoDecompte;
    }

    public String getFromTypeDeclaration() {
        return fromTypeDeclaration;
    }

    public List<String> getInTypeDeclaration() {
        return inTypeDeclaration;
    }

    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public String getToAffilie() {
        return toAffilie;
    }

    public void setForAffiliationId(String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setForDateFlag(String forDateFlag) {
        this.forDateFlag = forDateFlag;
    }

    public void setForDateFlagDecompteImpot(String forDateFlagDecompteImpotLtn) {
        this.forDateFlagDecompteImpotLtn = forDateFlagDecompteImpotLtn;
    }

    public void setForDateFlagIsNullOrZero(Boolean forDateFlagIsNullOrZero) {
        this.forDateFlagIsNullOrZero = forDateFlagIsNullOrZero;
    }

    public void setForDateImpressionDecompteLtnIsNullOrZero(Boolean forDateImpressionDecompteLtnIsNullOrZero) {
        this.forDateImpressionDecompteLtnIsNullOrZero = forDateImpressionDecompteLtnIsNullOrZero;
    }

    public void setForDateRetourEffGreaterOrEquals(String forDateRetourEffGreaterOrEquals) {
        this.forDateRetourEffGreaterOrEquals = forDateRetourEffGreaterOrEquals;
    }

    public void setForDateRetourEffLowerOrEquals(String forDateRetourEffLowerOrEquals) {
        this.forDateRetourEffLowerOrEquals = forDateRetourEffLowerOrEquals;
    }

    /**
     * Sets the forEtat.
     * 
     * @param forEtat
     *            The forEtat to set
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * Sets the forEtatNotLike.
     * 
     * @param forEtatNotLike
     *            The forEtatNotLike to set
     */
    public void setForEtatNotLike(String forEtatNotLike) {
        this.forEtatNotLike = forEtatNotLike;
    }

    public void setForIdControlEmployeur(String forIdControlEmployeur) {
        this.forIdControlEmployeur = forIdControlEmployeur;
    }

    public void setForIdDeclaration(String newForIdDeclaration) {
        forIdDeclaration = newForIdDeclaration;
    }

    public void setForIdDeclarationDistante(String forIdDeclarationDistante) {
        this.forIdDeclarationDistante = forIdDeclarationDistante;
    }

    public void setForIdJournal(String string) {
        forIdJournal = string;
    }

    public void setForIdPassageFac(String newForIdPassageFac) {
        forIdPassageFac = newForIdPassageFac;
    }

    public void setForIdPucsFileNotEmpty(Boolean forIdPucsFileNotEmpty) {
        this.forIdPucsFileNotEmpty = forIdPucsFileNotEmpty;
    }

    public void setForIsIdRapportVide(Boolean forIsIdRapportVide) {
        this.forIsIdRapportVide = forIsIdRapportVide;
    }

    public void setForNoDecompte(String newForNoDecompte) {
        forNoDecompte = newForNoDecompte;
    }

    public void setForNotTypeDeclaration(String forNotTypeDeclaration) {
        this.forNotTypeDeclaration = forNotTypeDeclaration;
    }

    public void setForProvenance(String forProvenance) {
        this.forProvenance = forProvenance;
    }

    /**
     * Sets the forSelectionTri
     * 
     * @param forSelectionTri
     *            The forSelectionTri to set
     */
    public void setForSelectionTri(String forSelectionTri) {
        this.forSelectionTri = forSelectionTri;
    }

    public void setForTypeDeclaration(String newForTypeDeclaration) {
        forTypeDeclaration = newForTypeDeclaration;
    }

    /**
     * Sets the fromAffilie.
     * 
     * @param fromAffilie
     *            The fromAffilie to set
     */
    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setFromAnnee(String newFromAnnee) {
        fromAnnee = newFromAnnee;
    }

    public void setFromIdDeclaration(String newFromIdDeclaration) {
        fromIdDeclaration = newFromIdDeclaration;
    }

    public void setFromNoDecompte(String newFromNoDecompte) {
        fromNoDecompte = newFromNoDecompte;
    }

    public void setFromTypeDeclaration(String newFromTypeDeclaration) {
        fromTypeDeclaration = newFromTypeDeclaration;
    }

    public void setInTypeDeclaration(List<String> inTypeDeclaration) {
        this.inTypeDeclaration = inTypeDeclaration;
    }

    public void setLikeNumeroAffilie(String string) {
        likeNumeroAffilie = string;
    }

    public void setOrder(java.lang.String order) {
        this.order = order;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }

    public String getNotForIdDeclaration() {
        return notForIdDeclaration;
    }

    public void setNotForIdDeclaration(String notForIdDeclaration) {
        this.notForIdDeclaration = notForIdDeclaration;
    }

}