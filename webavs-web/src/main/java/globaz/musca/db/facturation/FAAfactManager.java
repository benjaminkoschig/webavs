package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPrintManageDoc;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import java.sql.ResultSet;

public class FAAfactManager extends globaz.globall.db.BManager implements java.io.Serializable, IFAPrintManageDoc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeCotisation = new String();
    private Boolean forAQuittancer = null;
    private java.lang.String forDebutPeriode = new String();
    private java.lang.String forIdAfact = new String();

    private java.lang.String forIdEnteteFacture = new String();

    private java.lang.String forIdExterneDebiteurCompensation = new String();
    private java.lang.String forIdExterneFactureCompensation = new String();
    private Boolean forIdExterneFactureCompensationNotEmpty = Boolean.FALSE;
    private java.lang.String forIdModuleFacturation = new String();
    private java.lang.String forIdPassage = new String();
    private java.lang.String forIdRemarque = new String();
    private java.lang.String forIdRoleDebiteurCompensation = new String();
    private String forIdRoleDebCom = new String();

    private java.lang.String forIdRubrique = new String();

    private java.lang.String forIdTiers = new String();
    private java.lang.String forIdTiersDebiteurCompensation = new String();
    private java.lang.String forIdTypeAfact = new String();
    private java.lang.String forIdTypeFactureCompensation = new String();
    private boolean forIsAfactCompensation = false;
    private java.lang.String forMontant = new String();
    private boolean forMontantSuppZero = false;
    private java.lang.String forNonComptabilisable = new String();
    private String forReferenceExterne = new String();
    private java.lang.String fromDebutPeriode = new String();
    private java.lang.String fromIdExterneRole = new String();
    private java.lang.String fromLibelle = new String();
    private String fromMontant = new String();
    private String groupBy = new String();
    private Boolean impression = new Boolean(false);
    private boolean isAfacForBulletinsSoldes = false;
    private java.lang.String orderBy = new String();
    private Boolean wantOnlyFileAFact = Boolean.FALSE;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (isAfacForBulletinsSoldes()) {
            return " IDTIEDEBCOM,IDROLDEBCOM, IDEXTDEBCOM, IDEXTFACCOM, IDTYPFACCOM, IDEXTERNEROLE, SUM(MONTANTFACTURE) AS MONTANTFACTURE";
        } else {
            return FAAfact.TABLE_FIELDS
                    + ", TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP.IDEXTERNEROLE, FAENTFP.IDEXTERNEFACTURE, "
                    + "PMTRADP.LIBELLE AS LIBELLERUB, CARUBRP.IDEXTERNE, FAREMAP.TEXTE ";
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        if (getWantOnlyFileAFact().equals(Boolean.TRUE)) {
            return _getCollection() + "FAAFACP AS FAAFACP";
        }
        if (isAfacForBulletinsSoldes()) {
            return _getCollection() + "FAAFACP AS FAAFACP INNER JOIN " + _getCollection()
                    + "FAENTFP AS FAENTFP ON (FAAFACP.IDENTETEFACTURE=FAENTFP.IDENTETEFACTURE) ";
        } else {
            return _getCollection() + "FAAFACP AS FAAFACP " + "LEFT JOIN " + _getCollection()
                    + "CARUBRP AS CARUBRP ON (CARUBRP.IDRUBRIQUE=FAAFACP.IDRUBRIQUE) " + "INNER JOIN "
                    + _getCollection() + "PMTRADP AS PMTRADP ON (PMTRADP.IDTRADUCTION= CARUBRP.IDTRADUCTION "
                    + "AND PMTRADP.codeisolangue="
                    + this._dbWriteString(statement.getTransaction(), getSession().getIdLangueISO()).toUpperCase()
                    + ") " + "LEFT JOIN " + _getCollection()
                    + "FAREMAP AS FAREMAP ON (FAAFACP.IDREMARQUE=FAREMAP.IDREMARQUE) " + "LEFT JOIN "
                    + _getCollection() + "FAENTFP AS FAENTFP ON (FAAFACP.IDENTETEFACTURE=FAENTFP.IDENTETEFACTURE) "
                    + "LEFT JOIN " + _getCollection() + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE)";
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getGroupBy(BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        String result = " GROUP BY ";
        if (isAfacForBulletinsSoldes()) {
            return result + "IDEXTDEBCOM, IDROLDEBCOM, IDEXTFACCOM, IDTIEDEBCOM, IDEXTERNEROLE, IDTYPFACCOM";
        } else if (!JadeStringUtil.isEmpty(getGroupBy())) {
            return result + getGroupBy();
        } else {
            return "";
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy().length() != 0) {
            if (getOrderBy().equals(FAEnteteFacture.CS_TRI_DEBITEUR)) {
                return "IDEXTERNEROLE";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_NUMERO_DECOMTPE)) {
                return "IDEXTERNEFACTURE";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_NOM)) {
                return "HTLDE1, HTLDE2";
            } else {
                return getOrderBy();
            }
        } else {
            // Tri par défaut
            return "";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdAfact().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDAFACT=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAfact());
        }

        if (getForIdEnteteFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDENTETEFACTURE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEnteteFacture());
        }

        if (getForIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDMODFAC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleFacturation());
        }

        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDPASSAGE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDREMARQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        if (getForIdTypeAfact().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDTYPEAFACT=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeAfact());
        }

        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDRUBRIQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        if (getForAnneeCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.ANNEECOTISATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnneeCotisation());
        }

        if (getForDebutPeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.DATEDEBUTPERIODE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForDebutPeriode());
        }

        if (getForIdRoleDebiteurCompensation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDROLEDEBITEURCOMPENSATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRoleDebiteurCompensation());
        }

        if (getForIdRoleDebCom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDROLDEBCOM=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRoleDebCom());
        }

        if (getForIdExterneDebiteurCompensation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDEXTDEBCOM="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneDebiteurCompensation());
        }

        if (getForIdTiersDebiteurCompensation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDTIERSDEBITEURCOMPENSATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiersDebiteurCompensation());
        }

        if (getForIdTypeFactureCompensation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDTYPFACCOM="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeFactureCompensation());
        }

        if (getForIdExterneFactureCompensation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDEXTFACCOM="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneFactureCompensation());
        }

        if (getForIdExterneFactureCompensationNotEmpty().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.IDEXTFACCOM<>" + this._dbWriteString(statement.getTransaction(), "");
        }

        if (getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.LIBELLE>=" + this._dbWriteString(statement.getTransaction(), getFromLibelle());
        }

        if (getFromDebutPeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.DATEDEBUTPERIODE>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromDebutPeriode());
        }

        if (getForNonComptabilisable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.NONCOMPTABILISABLE="
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(getForNonComptabilisable()),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (isForMontantSuppZero()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.MONTANTFACTURE>0";
        }

        if (isForIsAfactCompensation()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(FAAFACP.IDTYPEAFACT=" + FAAfact.CS_AFACT_COMPENSATION + " OR " + "FAAFACP.IDTYPEAFACT="
                    + FAAfact.CS_AFACT_COMPENSATION_INTERNE + ")";
        }

        if (getFromMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.MONTANTFACTURE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromMontant());
        }

        if (getForMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.MONTANTFACTURE=" + this._dbWriteNumeric(statement.getTransaction(), getForMontant());
        }

        if (getFromIdExterneRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEROLE >="
                    + this._dbWriteString(statement.getTransaction(), getFromIdExterneRole());
        }

        if (forAQuittancer != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.AQUITTANCER ="
                    + this._dbWriteBoolean(statement.getTransaction(), getForAQuittancer(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "forAQuittancer");
        }

        if (getForReferenceExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.REFERENCEEXTERNE = "
                    + this._dbWriteString(statement.getTransaction(), getForReferenceExterne());
        }

        if (getImpression().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAAFACP.AQUITTANCER = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "forAQuittancer");
        }

        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDTIERS =" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        sqlWhere += _getGroupBy(statement);
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAAfact();
    }

    public java.lang.String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    /**
     * @return
     */
    public Boolean getForAQuittancer() {
        return forAQuittancer;
    }

    public java.lang.String getForDebutPeriode() {
        return forDebutPeriode;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdAfact() {
        return forIdAfact;
    }

    public java.lang.String getForIdEnteteFacture() {
        return forIdEnteteFacture;
    }

    public java.lang.String getForIdExterneDebiteurCompensation() {
        return forIdExterneDebiteurCompensation;
    }

    public java.lang.String getForIdExterneFactureCompensation() {
        return forIdExterneFactureCompensation;
    }

    /**
     * @return
     */
    public Boolean getForIdExterneFactureCompensationNotEmpty() {
        return forIdExterneFactureCompensationNotEmpty;
    }

    public java.lang.String getForIdModuleFacturation() {
        return forIdModuleFacturation;
    }

    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public java.lang.String getForIdRemarque() {
        return forIdRemarque;
    }

    public java.lang.String getForIdRoleDebiteurCompensation() {
        return forIdRoleDebiteurCompensation;
    }

    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public java.lang.String getForIdTiersDebiteurCompensation() {
        return forIdTiersDebiteurCompensation;
    }

    public java.lang.String getForIdTypeAfact() {
        return forIdTypeAfact;
    }

    public java.lang.String getForIdTypeFactureCompensation() {
        return forIdTypeFactureCompensation;
    }

    public java.lang.String getForMontant() {
        return forMontant;
    }

    /**
     * Returns the forNonComptabilisable.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.String getForNonComptabilisable() {
        return forNonComptabilisable;
    }

    /**
     * @return
     */
    public String getForReferenceExterne() {
        return forReferenceExterne;
    }

    public java.lang.String getFromDebutPeriode() {
        return fromDebutPeriode;
    }

    /**
     * @return
     */
    public java.lang.String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public java.lang.String getFromLibelle() {
        return fromLibelle;
    }

    /**
     * @return
     */
    public String getFromMontant() {
        return fromMontant;
    }

    /**
     * Returns the groudBy.
     * 
     * @return String
     */
    public String getGroupBy() {
        return groupBy;
    }

    /**
     * @return
     */
    public Boolean getImpression() {
        return impression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:21:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    public String getSumPassage(String idPassage, String idModuleFac, BTransaction transaction) throws Exception {
        String sum = "";
        double entier;
        BStatement statement = new BStatement(transaction);
        statement.createStatement();
        ResultSet res = statement.executeQuery("SELECT SUM (FAAFACP.MONTANTFACTURE) FROM " + _getCollection()
                + "FAAFACP AS FAAFACP WHERE FAAFACP.IDPASSAGE = " + idPassage + " AND FAAFACP.IDMODFAC = "
                + idModuleFac);
        res.next();
        entier = JadeStringUtil.toDouble(res.getObject(1).toString()) * -1;
        sum = String.valueOf(entier);
        statement.closeStatement();
        return JANumberFormatter.deQuote(sum);
    }

    public Boolean getWantOnlyFileAFact() {
        return wantOnlyFileAFact;
    }

    /**
     * Returns the isAfacForBulletinsSoldes.
     * 
     * @return boolean
     */
    public boolean isAfacForBulletinsSoldes() {
        return isAfacForBulletinsSoldes;
    }

    public boolean isForIsAfactCompensation() {
        return forIsAfactCompensation;
    }

    /**
     * Returns the forMontantSuppZero.
     * 
     * @return boolean
     */
    public boolean isForMontantSuppZero() {
        return forMontantSuppZero;
    }

    public void setForAnneeCotisation(java.lang.String newForAnneeCotisation) {
        forAnneeCotisation = newForAnneeCotisation;
    }

    /**
     * @param boolean1
     */
    public void setForAQuittancer(Boolean boolean1) {
        forAQuittancer = boolean1;
    }

    /**
     * Method setForCSRubrique.
     * 
     * @param string
     */
    public void setForCSRubrique(String CSIdExterne) throws Exception {
        // TO DO ALD
        // on va rechercher dans Osiris l'id mémorisée correspondant au CS passé
        // en paramètre
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdExterne(CSIdExterne);
        rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
        rubrique.retrieve();
        if (rubrique.isNew()) {
            setMessage("FAAfactManager : setForCSRubrique(" + CSIdExterne + ") : la rubrique " + CSIdExterne
                    + "CSIdExterne n'existe pas !!");
            setMsgType(FWViewBeanInterface.ERROR);
        } else {
            setForIdRubrique(rubrique.getIdRubrique());
        }
    }

    public void setForDebutPeriode(java.lang.String newForDebutPeriode) {
        forDebutPeriode = newForDebutPeriode;
    }

    /**
     * Setter
     */
    public void setForIdAfact(java.lang.String newForIdAfact) {
        forIdAfact = newForIdAfact;
    }

    public void setForIdEnteteFacture(java.lang.String newForIdEnteteFacture) {
        forIdEnteteFacture = newForIdEnteteFacture;
    }

    public void setForIdExterneDebiteurCompensation(java.lang.String newForIdExterneDebiteurCompensation) {
        forIdExterneDebiteurCompensation = newForIdExterneDebiteurCompensation;
    }

    public void setForIdExterneFactureCompensation(java.lang.String newForIdExterneFactureCompensation) {
        forIdExterneFactureCompensation = newForIdExterneFactureCompensation;
    }

    /**
     * @param boolean1
     */
    public void setForIdExterneFactureCompensationNotEmpty(Boolean boolean1) {
        forIdExterneFactureCompensationNotEmpty = boolean1;
    }

    public void setForIdModuleFacturation(java.lang.String newForIdModuleFacturation) {
        forIdModuleFacturation = newForIdModuleFacturation;
    }

    public void setForIdPassage(java.lang.String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    public void setForIdRemarque(java.lang.String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    public void setForIdRoleDebiteurCompensation(java.lang.String newForIdRoleDebiteurCompensation) {
        forIdRoleDebiteurCompensation = newForIdRoleDebiteurCompensation;
    }

    public void setForIdRubrique(java.lang.String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    public void setForIdTiers(java.lang.String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTiersDebiteurCompensation(java.lang.String newForIdTiersDebiteurCompensation) {
        forIdTiersDebiteurCompensation = newForIdTiersDebiteurCompensation;
    }

    public void setForIdTypeAfact(java.lang.String newForIdTypeAfact) {
        forIdTypeAfact = newForIdTypeAfact;
    }

    public void setForIdTypeFactureCompensation(java.lang.String newForIdTypeFactureCompensation) {
        forIdTypeFactureCompensation = newForIdTypeFactureCompensation;
    }

    public void setForIsAfactCompensation(boolean forIsAfactCompensation) {
        this.forIsAfactCompensation = forIsAfactCompensation;
    }

    public void setForMontant(java.lang.String forMontant) {
        this.forMontant = forMontant;
    }

    public String getForIdRoleDebCom() {
        return forIdRoleDebCom;
    }

    public void setForIdRoleDebCom(String forIdRoleDebCom) {
        this.forIdRoleDebCom = forIdRoleDebCom;
    }

    /**
     * Sets the forMontantSuppZero.
     * 
     * @param forMontantSuppZero
     *            The forMontantSuppZero to set
     */
    public void setForMontantSuppZero(boolean forMontantSuppZero) {
        this.forMontantSuppZero = forMontantSuppZero;
    }

    /**
     * Sets the forNonComptabilisable.
     * 
     * @param forNonComptabilisable
     *            The forNonComptabilisable to set
     */
    public void setForNonComptabilisable(java.lang.String forNonComptabilisable) {
        this.forNonComptabilisable = forNonComptabilisable;
    }

    /**
     * @param string
     */
    public void setForReferenceExterne(String string) {
        forReferenceExterne = string;
    }

    public void setFromDebutPeriode(java.lang.String newFromDebutPeriode) {
        fromDebutPeriode = newFromDebutPeriode;
    }

    /**
     * @param string
     */
    public void setFromIdExterneRole(java.lang.String string) {
        fromIdExterneRole = string;
    }

    public void setFromLibelle(java.lang.String newFromLibelle) {
        fromLibelle = newFromLibelle;
    }

    /**
     * @param string
     */
    public void setFromMontant(String string) {
        fromMontant = string;
    }

    /**
     * Sets the groudBy.
     * 
     * @param groudBy
     *            The groudBy to set
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    /**
     * @param boolean1
     */
    public void setImpression(Boolean boolean1) {
        impression = boolean1;
    }

    /**
     * Sets the isAfacForBulletinsSoldes.
     * 
     * @param isAfacForBulletinsSoldes
     *            The isAfacForBulletinsSoldes to set
     */
    public void setIsAfacForBulletinsSoldes(boolean isAfacForBulletinsSoldes) {
        this.isAfacForBulletinsSoldes = isAfacForBulletinsSoldes;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.04.2003 14:21:31)
     * 
     * @param newOrderBy
     *            java.lang.String
     */
    public void setOrderBy(java.lang.String newOrderBy) {
        orderBy = newOrderBy;
    }

    public void setWantOnlyFileAFact(Boolean wantOnlyFileAFact) {
        this.wantOnlyFileAFact = wantOnlyFileAFact;
    }

}
