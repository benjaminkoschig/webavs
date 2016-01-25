package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * @author
 * @revision SCO 11 mars 2010
 */
public class CAApercuInteretMoratoireManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOMAINE_CA = "CA";
    public static final String DOMAINE_FA = "FA";

    private String forDateCalcul = "";
    private String forDateCalculDebut = "";
    private String forDateCalculFin = "";
    private String forDomaine = CAApercuInteretMoratoireManager.DOMAINE_CA; // le domaine des IM par défaut est
    // osiris
    private String forIdCompteAnnexe = "";
    private String forIdEnteteFacture = "";
    private String forIdExterneRole = "";
    private String forIdGenreInteret = "";
    private String forIdInteretMoratoire = "";
    private String forIdJournalCalcul = "";

    private String forIdJournalFacturation = "";
    private String forIdMotifCalcul = "";
    private String forIdRole = "";
    private String forMotifCalcul;
    private String forSelectionTri = "";

    private String fromTotalMontantInteret = "";

    private boolean statsParGenreInteret = false;

    /**
     * Constructeur de CAApercuInteretMoratoireManager
     */
    public CAApercuInteretMoratoireManager() {
        super();
    }

    /*
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String sqlFields = "";

        if (isStatsParGenreInteret()) {
            // récupération de la somme des totaux (regroupés par genre
            // d'intérêt
            sqlFields = CAInteretMoratoire.FIELD_IDGENREINTERET + ", " + CAInteretMoratoire.FIELD_MOTIFCALCUL
                    + ", SUM(TOTALMONTANTINT) AS TOTALMONTANTINT, COUNT(TOTALMONTANTINT) AS NOMBRELIGNES ";
        } else {
            if (isDomaineCA()) {
                sqlFields = CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDINTERETMORATOIRE + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDJOUFAC + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDJOURNALCALCUL + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDSECTIONFACTURE + ", ";
                sqlFields += CAInteretMoratoire.FIELD_DATECALCUL + ", ";
                sqlFields += CAInteretMoratoire.FIELD_DATEFACTURATION + ", ";
                sqlFields += CASection.TABLE_CASECTP + "." + CASection.FIELD_IDCOMPTEANNEXE + " AS "
                        + CASection.FIELD_IDCOMPTEANNEXE + ", ";
                sqlFields += CASection.TABLE_CASECTP + "." + CASection.FIELD_IDEXTERNE + " AS "
                        + CASection.FIELD_IDEXTERNE + ", ";
                sqlFields += CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE + " AS "
                        + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", ";
                sqlFields += CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_DESCRIPTION + " AS "
                        + CACompteAnnexe.FIELD_DESCRIPTION + ", ";
                sqlFields += CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDSECTION + ", ";
                sqlFields += "IDTYPESECTION, ";
                sqlFields += CASection.FIELD_DATESECTION
                        + " AS SECTIONDATE, IDGENREINTERET, TOTALMONTANTINT, MOTIFCALCUL ";
            } else {
                sqlFields = CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDINTERETMORATOIRE + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDJOUFAC + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDJOURNALCALCUL + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDSECTIONFACTURE + ", ";
                sqlFields += CAInteretMoratoire.FIELD_DATECALCUL + ", ";
                sqlFields += CAInteretMoratoire.FIELD_DATEFACTURATION + ", ";
                sqlFields += CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDSECTION + ", ";
                sqlFields += CAInteretMoratoire.FIELD_IDGENREINTERET + ", ";
                sqlFields += CAInteretMoratoire.FIELD_MOTIFCALCUL + ", ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDTIERS + ", ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDEXTERNEFACTURE
                        + " AS IDEXTERNE, ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDTYPEFACTURE
                        + " AS IDTYPESECTION, ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDPASSAGE + ", ";
                sqlFields += "TITIERP.HTLDE1 CONCAT ' ' CONCAT TITIERP.HTLDE2 AS DESCRIPTION, ";
                sqlFields += "TOTALMONTANTINT, ";
                sqlFields += "'' AS SECTIONDATE, ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDEXTERNEROLE
                        + " CONCAT ' ' CONCAT " + FAEnteteFacture.TABLE_FAENTFP + "."
                        + FAEnteteFacture.FIELD_IDEXTERNEFACTURE + " AS IDCOMPTEANNEXE, ";
                sqlFields += FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDENTETEFACTURE;
            }
        }

        return sqlFields;
    }

    /*
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = "";

        if (isDomaineCA()) {
            sqlFrom = " " + _getCollection() + CAInteretMoratoire.TABLE_CAIMDCP + " "
                    + CAInteretMoratoire.TABLE_CAIMDCP;
            sqlFrom += " INNER JOIN " + _getCollection() + CASection.TABLE_CASECTP + " " + CASection.TABLE_CASECTP
                    + " ON " + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION + " = "
                    + CAInteretMoratoire.TABLE_CAIMDCP + ".IDSECTION";
            sqlFrom += " INNER JOIN " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " "
                    + CACompteAnnexe.TABLE_CACPTAP + " ON " + CACompteAnnexe.TABLE_CACPTAP + ".IDCOMPTEANNEXE = "
                    + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDCOMPTEANNEXE;
            sqlFrom += " LEFT JOIN (SELECT IDINTERETMORATOIRE, SUM(MONTANTINTERET) AS TOTALMONTANTINT FROM  "
                    + _getCollection() + CADetailInteretMoratoire.TABLE_CAIMDEP
                    + " GROUP BY IDINTERETMORATOIRE) TEMP ON " + CAInteretMoratoire.TABLE_CAIMDCP
                    + ".IDINTERETMORATOIRE = TEMP.IDINTERETMORATOIRE ";
        } else {
            sqlFrom = " " + _getCollection() + CAInteretMoratoire.TABLE_CAIMDCP + " "
                    + CAInteretMoratoire.TABLE_CAIMDCP;
            sqlFrom += " INNER JOIN " + _getCollection() + FAEnteteFacture.TABLE_FAENTFP + " "
                    + FAEnteteFacture.TABLE_FAENTFP + " ON (" + FAEnteteFacture.TABLE_FAENTFP + "."
                    + FAEnteteFacture.FIELD_IDPASSAGE + " = " + CAInteretMoratoire.TABLE_CAIMDCP + ".IDJOUFAC AND "
                    + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDENTETEFACTURE + " = "
                    + CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDSECTIONFACTURE + ")";
            sqlFrom += " INNER JOIN "
                    + _getCollection()
                    + "TITIERP AS TITIERP ON ("
                    + FAEnteteFacture.TABLE_FAENTFP
                    + "."
                    + FAEnteteFacture.FIELD_IDTIERS
                    + "=TITIERP.HTITIE) LEFT JOIN (SELECT IDINTERETMORATOIRE, SUM(MONTANTINTERET) AS TOTALMONTANTINT FROM "
                    + _getCollection() + CADetailInteretMoratoire.TABLE_CAIMDEP
                    + " GROUP BY IDINTERETMORATOIRE) TEMP ON " + CAInteretMoratoire.TABLE_CAIMDCP + "."
                    + CAInteretMoratoire.FIELD_IDINTERETMORATOIRE + " = TEMP.IDINTERETMORATOIRE ";
        }

        return sqlFrom;
    }

    /*
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        if (isStatsParGenreInteret()) {
            return "MOTIFCALCUL, IDGENREINTERET ";
        } else {
            return null;
        }
    }

    /*
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        String _order = "";

        String columDescription = isDomaineCA() ? CACompteAnnexe.FIELD_DESCUPCASE : CACompteAnnexe.FIELD_DESCRIPTION;

        if (isStatsParGenreInteret()) {
            _order = "IDGENREINTERET";
            // date calcul, nom
        } else if (getForSelectionTri().equalsIgnoreCase("datenom")) {
            _order = "DATECALCUL, " + columDescription;
            // Date de calcul, numéro
        } else if (getForSelectionTri().equalsIgnoreCase("datenumero")) {
            _order = "DATECALCUL, IDEXTERNEROLE";
            // description
        } else if (getForSelectionTri().equalsIgnoreCase("nom")) {
            _order = columDescription + ", DATECALCUL desc, IDEXTERNEROLE";
            // numéro
        } else if (getForSelectionTri().equalsIgnoreCase("numero")) {
            _order = "IDEXTERNEROLE";
            // numéro, description
        } else if (getForSelectionTri().equalsIgnoreCase("rolenumeronom")) {
            _order = "" + CACompteAnnexe.TABLE_CACPTAP + ".IDROLE, IDEXTERNEROLE, " + columDescription;
        }

        return _order;
    }

    /*
     * Méthode surchargée Ajout du group by
     */
    @Override
    protected java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }

            String sqlGroupBy = _getGroupBy(statement);
            if ((sqlGroupBy != null) && (sqlGroupBy.trim().length() != 0)) {
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(sqlGroupBy);
            }

            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(sqlOrder);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /*
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (getForIdGenreInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDGENREINTERET=" + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreInteret());
        }
        if (getForIdJournalCalcul().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDJOURNALCALCUL=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalCalcul());
        }
        if (getFromTotalMontantInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TOTALMONTANTINT = "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromTotalMontantInteret());
        }

        if (getForIdCompteAnnexe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CASection.TABLE_CASECTP + ".IDCOMPTEANNEXE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        if (getForIdExterneRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "IDEXTERNEROLE LIKE "
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneRole() + "%");
        }

        // TODO sch voir si c'est juste qu'on ne prenne pas en compte les droits
        // sur les rôles dans la facturation
        if ((getForIdRole().length() != 0) && isDomaineCA()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForIdRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForIdRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "" + CACompteAnnexe.TABLE_CACPTAP + ".IDROLE IN (";

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "" + CACompteAnnexe.TABLE_CACPTAP + ".IDROLE = "
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdRole());
            }
        }

        if (getForIdInteretMoratoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".IDINTERETMORATOIRE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdInteretMoratoire());
        }

        // si date de début définie : date >=
        if ((getForDateCalculDebut().length() != 0) && (getForDateCalculFin().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".DATECALCUL >= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateCalculDebut());
            // si date de fin définie : date <=
        } else if ((getForDateCalculDebut().length() == 0) && (getForDateCalculFin().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".DATECALCUL <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateCalculFin());
        }
        // si les deux dates sont définie : between
        else if ((getForDateCalculDebut().length() != 0) && (getForDateCalculFin().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".DATECALCUL BETWEEN "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateCalculDebut()) + " AND "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateCalculFin());
        }

        // pour une date exacte
        if (getForDateCalcul().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".DATECALCUL = "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateCalcul());
        }

        if (getForIdJournalFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".IDJOUFAC = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalFacturation());

            if (getForIdJournalFacturation().equalsIgnoreCase("0")) {
                sqlWhere += " AND MOTIFCALCUL != "
                        + this._dbWriteNumeric(statement.getTransaction(), CAInteretMoratoire.CS_EXEMPTE);
            }
        }

        if (getForIdMotifCalcul().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".MOTIFCALCUL = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdMotifCalcul());
        }

        if (getForIdEnteteFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "" + CAInteretMoratoire.TABLE_CAIMDCP + ".IDSECTIONFACTURE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEnteteFacture());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMotifCalcul())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_MOTIFCALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForMotifCalcul());
        }

        if (isDomaineCA()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDSECTION + " <> "
                    + CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDSECTIONFACTURE;
        }

        return sqlWhere;
    }

    /*
     * @see globaz.osiris.db.interets.CAInteretMoratoireManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAApercuInteretMoratoire();
    }

    /**
     * @return
     */
    public String getForDateCalcul() {
        return forDateCalcul;
    }

    /**
     * @return
     */
    public String getForDateCalculDebut() {
        return forDateCalculDebut;
    }

    /**
     * @return
     */
    public String getForDateCalculFin() {
        return forDateCalculFin;
    }

    /**
     * @return
     */
    public String getForDomaine() {
        return forDomaine;
    }

    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return
     */
    public String getForIdEnteteFacture() {
        return forIdEnteteFacture;
    }

    /**
     * @return
     */
    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public String getForIdGenreInteret() {
        return forIdGenreInteret;
    }

    public String getForIdInteretMoratoire() {
        return forIdInteretMoratoire;
    }

    public String getForIdJournalCalcul() {
        return forIdJournalCalcul;
    }

    public String getForIdJournalFacturation() {
        return forIdJournalFacturation;
    }

    /**
     * @return
     */
    public String getForIdMotifCalcul() {
        return forIdMotifCalcul;
    }

    /**
     * peut contenir des ','.
     * 
     * @see #setForIdRole(String)
     * @return
     */
    public String getForIdRole() {
        return forIdRole;
    }

    public String getForMotifCalcul() {
        return forMotifCalcul;
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * @return
     */
    public String getFromTotalMontantInteret() {
        return fromTotalMontantInteret;
    }

    /**
     * Returns true is the viewbean applies for "Comptabilité Auxiliaire", false for "Facturation"
     * 
     * @return
     */
    public boolean isDomaineCA() {
        return CAApercuInteretMoratoireManager.DOMAINE_CA.equals(forDomaine);
    }

    /**
     * @return
     */
    public boolean isStatsParGenreInteret() {
        return statsParGenreInteret;
    }

    /**
     * @param string
     */
    public void setForDateCalcul(String string) {
        forDateCalcul = string;
    }

    /**
     * @param string
     */
    public void setForDateCalculDebut(String string) {
        forDateCalculDebut = string;
    }

    /**
     * @param string
     */
    public void setForDateCalculFin(String string) {
        forDateCalculFin = string;
    }

    /**
     * @param boolean1
     */
    public void setForDomaine(String val) {
        forDomaine = val;
    }

    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    /**
     * @param string
     */
    public void setForIdEnteteFacture(String string) {
        forIdEnteteFacture = string;
    }

    /**
     * @param string
     */
    public void setForIdExterneRole(String string) {
        forIdExterneRole = string;
    }

    public void setForIdGenreInteret(String forIdGenreInteret) {
        this.forIdGenreInteret = forIdGenreInteret;
    }

    public void setForIdInteretMoratoire(String forIdInteretMoratoire) {
        this.forIdInteretMoratoire = forIdInteretMoratoire;
    }

    public void setForIdJournalCalcul(String forIdJournalCalcul) {
        this.forIdJournalCalcul = forIdJournalCalcul;
    }

    public void setForIdJournalFacturation(String forIdJournalFacturation) {
        this.forIdJournalFacturation = forIdJournalFacturation;
    }

    /**
     * @param string
     */
    public void setForIdMotifCalcul(String string) {
        forIdMotifCalcul = string;
    }

    /**
     * string peut contenir des ',' auquel cas string est considéré comme une liste d'id roles séparés par des virgules,
     * les interets retournes concernent alors les comptes annexes dont le role et l'un de ceux contenus dans cette
     * liste.
     * 
     * @param string
     */
    public void setForIdRole(String string) {
        forIdRole = string;
    }

    public void setForMotifCalcul(String forMotifCalcul) {
        this.forMotifCalcul = forMotifCalcul;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

    /**
     * @param string
     */
    public void setFromTotalMontantInteret(String string) {
        fromTotalMontantInteret = string;
    }

    /**
     * @param b
     */
    public void setStatsParGenreInteret(boolean b) {
        statsParGenreInteret = b;
    }

}
