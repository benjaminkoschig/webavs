package globaz.draco.db.inscriptions;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.util.CIUtil;

public class DSInscriptionsIndividuellesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean aTraiter = new Boolean(false);
    private String forAnnee = "";
    private String forAnneeChomage = "";
    private String forAnneeInscription = "";
    private String forCategoriePersonnel = "";
    private String forCodeCanton = "";
    private String forCompteIndividuelId = "";
    private String forIdAffiliation = "";
    private String forIdDeclaration = new String();
    private String forIdEcrtirureCI = "";
    private String forJourFin = "";
    private String forMoisFin = "";
    private String forMontantSigne = "";
    private String forMontantSigneValue = "";
    private String forNotId = "";
    private String fromDateFemme = "";
    private String fromDateHomme = "";
    private String fromNomPrenom = "";
    private String fromNumeroAvs = "";
    private Boolean isAvertissement = new Boolean(false);
    private boolean isDateProdNNSS = false;
    private String likeNumeroAvs = "";
    private String likeNumeroAvsNNSS = "";
    private String notForInCatPerso = "";
    private String tri = "";
    private String untilDateSexeFemme = "";
    private String untilDateSexeHomme = "";
    // Si faux, on prend tout le monde, sinon on ne prend que les actif=> rattrapage FFPP
    private Boolean wantIsSorti = new Boolean(false);

    public DSInscriptionsIndividuellesManager() {
        super();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isBlankOrZero(getTri())) {
            if (isDateProdNNSS()) {
                return "KALNOM, TENANN";
            } else {
                return "KANAVS, TENANN";
            }
        } else if ("nom".equals(getTri())) {
            return "KALNOM, TENANN";
        } else if ("nss".equals(getTri())) {
            return "KANAVS, TENANN";
        } else if ("ordre".equals(getTri())) {
            return "TEID, TENANN";
        } else if ("montant".equals(getTri())) {
            return "KBMMON, TENANN";
        } else if ("canton".equals(getTri())) {
            return "TETCAN, TENANN";
        }
        return "KALNOM, TENANN";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.TAIDDE ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdDeclaration());
        }
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " TAANNE =" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        if (getForCompteIndividuelId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.KAIIND ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelId());
        }
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSDECLP.MAIAFF ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        if (getForCodeCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.TETCAN ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForCodeCanton());
        }
        if (getForNotId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TEID  <>" + this._dbWriteNumeric(statement.getTransaction(), getForNotId());

        }
        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KANAVS  like '" + CIUtil.unFormatAVS(getLikeNumeroAvs()) + "%' ";

        }
        if (getLikeNumeroAvs().length() != 0) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "KABNNS ='1'";
            } else if ("false".equalsIgnoreCase(likeNumeroAvsNNSS)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "KABNNS ='2'";
            }
        }
        if (getFromNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KANAVS  >='" + CIUtil.unFormatAVS(getFromNumeroAvs()) + "' ";

        }

        if (aTraiter.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KBMMON IS NULL AND TEMAI = 0 AND TEMAF = 0";

        }
        if (getForIdEcrtirureCI().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.KBIECR ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEcrtirureCI());

        }
        if (getNotForInCatPerso().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.TETCPE NOT IN (" + getNotForInCatPerso() + ")";
        }
        if (getForCategoriePersonnel().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.TETCPE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCategoriePersonnel());

        }
        if (getForAnneeInscription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.TENANN = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnneeInscription());

        }
        if (getForAnneeChomage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (TAANNE = " + this._dbWriteNumeric(statement.getTransaction(), getForAnneeChomage()) + " OR "
                    + _getCollection() + "DSINDP.TENANN = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnneeChomage()) + ")";
        }
        if (getForMoisFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNMOF = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMoisFin());

        }
        if (getForJourFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "DSINDP.TENJOF = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForJourFin());

        }
        if (wantIsSorti.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBBATT = '2' ";
        }
        if (!JadeStringUtil.isBlankOrZero(fromNomPrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KALNOM >= '" + getFromNomPrenom() + "' ";
        }
        if (!JadeStringUtil.isBlankOrZero(getForMontantSigne())
                && !JadeStringUtil.isBlankOrZero(getForMontantSigneValue())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBMMON " + getForMontantSigne() + " "
                    + JANumberFormatter.deQuote(getForMontantSigneValue()) + " ";
        }
        if (isAvertissement.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " TEBCAS = "
                    + this._dbWriteBoolean(statement.getTransaction(), isAvertissement,
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "casSpecial") + " ";

        }
        // until date différencié homme / femme
        if (!JadeStringUtil.isBlankOrZero(untilDateSexeFemme) && !JadeStringUtil.isBlankOrZero(untilDateSexeHomme)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ((KADNAI >="
                    + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + untilDateSexeFemme)
                    + " AND KATSEX = " + CICompteIndividuel.CS_FEMME + ") " + "OR (KADNAI >= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + untilDateSexeHomme)
                    + " AND KATSEX = " + CICompteIndividuel.CS_HOMME + ")) ";
        }
        if (!JadeStringUtil.isBlankOrZero(fromDateFemme) && !JadeStringUtil.isBlankOrZero(fromDateHomme)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ((KADNAI <=" + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + fromDateFemme)
                    + " AND KATSEX = " + CICompteIndividuel.CS_FEMME + ") " + "OR (KADNAI <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + fromDateHomme) + " AND KATSEX = "
                    + CICompteIndividuel.CS_HOMME + ")) ";
        }
        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSInscriptionsIndividuelles();
    }

    /**
     * @return
     */
    public Boolean getATraiter() {
        return aTraiter;
    }

    /**
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    public String getForAnneeChomage() {
        return forAnneeChomage;
    }

    public String getForAnneeInscription() {
        return forAnneeInscription;
    }

    public String getForCategoriePersonnel() {
        return forCategoriePersonnel;
    }

    /**
     * @return
     */
    public String getForCodeCanton() {
        return forCodeCanton;
    }

    /**
     * @return
     */
    public String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * @return
     */
    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * @return
     */
    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    /**
     * @return
     */
    public String getForIdEcrtirureCI() {
        return forIdEcrtirureCI;
    }

    public String getForJourFin() {
        return forJourFin;
    }

    public String getForMoisFin() {
        return forMoisFin;
    }

    public String getForMontantSigne() {
        return forMontantSigne;
    }

    public String getForMontantSigneValue() {
        return forMontantSigneValue;
    }

    /**
     * @return
     */
    public String getForNotId() {
        return forNotId;
    }

    public String getFromDateFemme() {
        return fromDateFemme;
    }

    public String getFromDateHomme() {
        return fromDateHomme;
    }

    public String getFromNomPrenom() {
        return fromNomPrenom;
    }

    public String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    public Boolean getIsAvertissement() {
        return isAvertissement;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    public String getNotForInCatPerso() {
        return notForInCatPerso;
    }

    public String getTri() {
        return tri;
    }

    public String getUntilDateSexeFemme() {
        return untilDateSexeFemme;
    }

    public String getUntilDateSexeHomme() {
        return untilDateSexeHomme;
    }

    public Boolean getWantIsSorti() {
        return wantIsSorti;
    }

    public boolean isDateProdNNSS() {
        return isDateProdNNSS;
    }

    /**
     * @param boolean1
     */
    public void setATraiter(Boolean boolean1) {
        aTraiter = boolean1;
    }

    public void setDateProdNNSS(boolean isDateProdNNSS) {
        this.isDateProdNNSS = isDateProdNNSS;
    }

    /**
     * @param string
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }

    public void setForAnneeChomage(String forAnneeChomage) {
        this.forAnneeChomage = forAnneeChomage;
    }

    public void setForAnneeInscription(String forAnneeInscription) {
        this.forAnneeInscription = forAnneeInscription;
    }

    public void setForCategoriePersonnel(String forCategoriePersonnel) {
        this.forCategoriePersonnel = forCategoriePersonnel;
    }

    /**
     * @param string
     */
    public void setForCodeCanton(String string) {
        forCodeCanton = string;
    }

    /**
     * @param string
     */
    public void setForCompteIndividuelId(String string) {
        forCompteIndividuelId = string;
    }

    /**
     * @param string
     */
    public void setForIdAffiliation(String string) {
        forIdAffiliation = string;
    }

    /**
     * @param string
     */
    public void setForIdDeclaration(String string) {
        forIdDeclaration = string;
    }

    /**
     * @param string
     */
    public void setForIdEcrtirureCI(String string) {
        forIdEcrtirureCI = string;
    }

    public void setForJourFin(String forJourFin) {
        this.forJourFin = forJourFin;
    }

    public void setForMoisFin(String forMoisFin) {
        this.forMoisFin = forMoisFin;
    }

    public void setForMontantSigne(String forMontantSigne) {
        this.forMontantSigne = forMontantSigne;
    }

    public void setForMontantSigneValue(String forMontantSigneValue) {
        this.forMontantSigneValue = forMontantSigneValue;
    }

    /**
     * @param string
     */
    public void setForNotId(String string) {
        forNotId = string;
    }

    public void setFromDateFemme(String fromDateFemme) {
        this.fromDateFemme = fromDateFemme;
    }

    public void setFromDateHomme(String fromDateHomme) {
        this.fromDateHomme = fromDateHomme;
    }

    public void setFromNomPrenom(String fromNomPrenom) {
        this.fromNomPrenom = fromNomPrenom;
    }

    public void setFromNumeroAvs(String fromNumeroAvs) {
        this.fromNumeroAvs = fromNumeroAvs;
    }

    public void setIsAvertissement(Boolean isAvertissement) {
        this.isAvertissement = isAvertissement;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = string;
    }

    public void setNotForInCatPerso(String notForInCatPerso) {
        this.notForInCatPerso = notForInCatPerso;
    }

    public void setTri(String tri) {
        this.tri = tri;
    }

    public void setUntilDateSexeFemme(String untilDateSexeFemme) {
        this.untilDateSexeFemme = untilDateSexeFemme;
    }

    public void setUntilDateSexeHomme(String untilDateSexeHomme) {
        this.untilDateSexeHomme = untilDateSexeHomme;
    }

    public void setWantIsSorti(Boolean wantIsSorti) {
        this.wantIsSorti = wantIsSorti;
    }

}
