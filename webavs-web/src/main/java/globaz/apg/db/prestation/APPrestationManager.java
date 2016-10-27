package globaz.apg.db.prestation;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Descpription
 * 
 * @author scr Date de création 17 mai 05
 */
public class APPrestationManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String ETAT_NON_DEFINITIF = "non définitif";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    private String forContenuAnnonce = null;
    private String forEtat = null;
    private String forGenre = "";
    private List<String> forInGenre = new ArrayList();
    private String forIdAnnonce = null;
    private String forIdDroit = null;
    private String forIdLot = null;
    private String forIdRestitution = null;
    private String forNoRevision = "";
    private boolean forPrestationsSansAnnonce = false;
    private String forType = null;
    private String forTypeDifferentDe = null;
    private String fromDateDebut = null;
    private String fromDateFin = null;
    private String fromIdPrestationApg = null;
    private String inDateDebut = null;
    private String inDateFin = null;
    private String notForContenuAnnonce = null;
    private String toDateDebut = null;
    private String toDateFin = null;

    private String toIdPrestationApg = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPrestationManager.
     */
    public APPrestationManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + APPrestation.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDDROIT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRestitution)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDRESTITUTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdRestitution);
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_CONTENUANNONCE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forContenuAnnonce);
        }

        if (!JadeStringUtil.isIntegerEmpty(notForContenuAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_CONTENUANNONCE + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), notForContenuAnnonce);
        }

        if (!JadeStringUtil.isEmpty(forEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (forEtat.equals(APPrestationManager.ETAT_NON_DEFINITIF)) {
                sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_ETAT + "<>"
                        + this._dbWriteNumeric(statement.getTransaction(), IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
            } else {
                sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_ETAT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), forEtat);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDLOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        if (!JAUtil.isDateEmpty(fromDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEFIN + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateFin);
        }

        if (!JAUtil.isDateEmpty(fromDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEDEBUT + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebut);
        }

        if (!JAUtil.isDateEmpty(toDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEDEBUT + "<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), toDateDebut);
        }

        if (!JAUtil.isDateEmpty(toDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEFIN + "<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), toDateFin);
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypeDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_TYPE + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), forTypeDifferentDe);
        }

        if (!JadeStringUtil.isIntegerEmpty(fromIdPrestationApg)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDPRESTATIONAPG
                    + ">=" + this._dbWriteNumeric(statement.getTransaction(), fromIdPrestationApg);
        }

        if (!JadeStringUtil.isIntegerEmpty(toIdPrestationApg)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDPRESTATIONAPG
                    + "<=" + this._dbWriteNumeric(statement.getTransaction(), toIdPrestationApg);
        }

        if (!JadeStringUtil.isIntegerEmpty(forType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_TYPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forType);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDANNONCE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdAnnonce);
        }

        if (forPrestationsSansAnnonce) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDANNONCE
                    + "=0 OR " + _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDANNONCE
                    + " is NULL)";
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoRevision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_NOREVISION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoRevision);
        }

        if (!JadeStringUtil.isIntegerEmpty(forGenre)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_GENRE_PRESTATION
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), forGenre);
        }

        if (!getForInGenre().isEmpty()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            StringBuilder valuesStr = new StringBuilder();
            for (String genre : getForInGenre()) {
                if (!JadeStringUtil.isEmpty(valuesStr.toString())) {
                    valuesStr.append(",");
                }
                valuesStr.append(this._dbWriteNumeric(statement.getTransaction(), genre));
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_GENRE_PRESTATION
                    + " IN (" + valuesStr.toString() + ")";
        }

        if (!JAUtil.isDateEmpty(inDateDebut) && !JAUtil.isDateEmpty(inDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEDEBUT + "<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), inDateDebut);

            sqlWhere += " AND ";

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEFIN + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), inDateFin);
        }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestation();
    }

    /**
     * getter pour l'attribut for contenu annonce
     * 
     * @return la valeur courante de l'attribut for contenu annonce
     */
    public String getForContenuAnnonce() {
        return forContenuAnnonce;
    }

    /**
     * getter pour l'attribut for etat
     * 
     * @return la valeur courante de l'attribut for etat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * getter pour l'attribut for genre
     * 
     * @return la valeur courante de l'attribut for genre
     */
    public String getForGenre() {
        return forGenre;
    }

    /**
     * getter pour l'attribut for id annonce
     * 
     * @return la valeur courante de l'attribut for id annonce
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * getter pour l'attribut for id droit
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * getter pour l'attribut for id restitution
     * 
     * @return la valeur courante de l'attribut for id restitution
     */
    public String getForIdRestitution() {
        return forIdRestitution;
    }

    /**
     * getter pour l'attribut for no revision
     * 
     * @return la valeur courante de l'attribut for no revision
     */
    public String getForNoRevision() {
        return forNoRevision;
    }

    /**
     * getter pour l'attribut for type
     * 
     * @return la valeur courante de l'attribut for type
     */
    public String getForType() {
        return forType;
    }

    /**
     * getter pour l'attribut for type different de
     * 
     * @return la valeur courante de l'attribut for type different de
     */
    public String getForTypeDifferentDe() {
        return forTypeDifferentDe;
    }

    /**
     * getter pour l'attribut from date debut
     * 
     * @return la valeur courante de l'attribut from date debut
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * getter pour l'attribut from data fin
     * 
     * @return la valeur courante de l'attribut from data fin
     */
    public String getFromDateFin() {
        return fromDateFin;
    }

    /**
     * getter pour l'attribut from id prestation
     * 
     * @return la valeur courante de l'attribut from id prestation
     */
    public String getFromIdPrestationApg() {
        return fromIdPrestationApg;
    }

    /**
     * @return
     */
    public String getInDateDebut() {
        return inDateDebut;
    }

    /**
     * @return
     */
    public String getInDateFin() {
        return inDateFin;
    }

    public String getNotForContenuAnnonce() {
        return notForContenuAnnonce;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APPrestation.FIELDNAME_IDPRESTATIONAPG;
    }

    /**
     * getter pour l'attribut to date debut
     * 
     * @return la valeur courante de l'attribut to date debut
     */
    public String getToDateDebut() {
        return toDateDebut;
    }

    /**
     * getter pour l'attribut to data fin
     * 
     * @return la valeur courante de l'attribut to data fin
     */
    public String getToDateFin() {
        return toDateFin;
    }

    /**
     * getter pour l'attribut to id prestation apg
     * 
     * @return la valeur courante de l'attribut to id prestation apg
     */
    public String getToIdPrestationApg() {
        return toIdPrestationApg;
    }

    /**
     * getter pour l'attribut for prestations sans annonce
     * 
     * @return la valeur courante de l'attribut for prestations sans annonce
     */
    public boolean isForPrestationsSansAnnonce() {
        return forPrestationsSansAnnonce;
    }

    /**
     * setter pour l'attribut for contenu annonce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForContenuAnnonce(String string) {
        forContenuAnnonce = string;
    }

    /**
     * setter pour l'attribut for etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String string) {
        forEtat = string;
    }

    /**
     * setter pour l'attribut for genre
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForGenre(String string) {
        forGenre = string;
    }

    /**
     * setter pour l'attribut for id annonce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAnnonce(String string) {
        forIdAnnonce = string;
    }

    /**
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }

    /**
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /**
     * setter pour l'attribut for id restitution
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdRestitution(String string) {
        forIdRestitution = string;
    }

    /**
     * setter pour l'attribut for no revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNoRevision(String string) {
        forNoRevision = string;
    }

    /**
     * setter pour l'attribut for prestations sans annonce
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPrestationsSansAnnonce(boolean b) {
        forPrestationsSansAnnonce = b;
    }

    /**
     * setter pour l'attribut for type
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForType(String string) {
        forType = string;
    }

    /**
     * setter pour l'attribut for type different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeDifferentDe(String string) {
        forTypeDifferentDe = string;
    }

    /**
     * setter pour l'attribut from date debut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateDebut(String string) {
        fromDateDebut = string;
    }

    /**
     * setter pour l'attribut from data fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateFin(String string) {
        fromDateFin = string;
    }

    /**
     * setter pour l'attribut from id prestation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromIdPrestationApg(String string) {
        fromIdPrestationApg = string;
    }

    /**
     * @param string
     */
    public void setInDateDebut(String string) {
        inDateDebut = string;
    }

    /**
     * @param string
     */
    public void setInDateFin(String string) {
        inDateFin = string;
    }

    public void setNotForContenuAnnonce(String notForContenuAnnonce) {
        this.notForContenuAnnonce = notForContenuAnnonce;
    }

    /**
     * setter pour l'attribut to date debut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setToDateDebut(String string) {
        toDateDebut = string;
    }

    /**
     * setter pour l'attribut toDateFin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setToDateFin(String string) {
        toDateFin = string;
    }

    /**
     * setter pour l'attribut to id prestation apg
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setToIdPrestationApg(String string) {
        toIdPrestationApg = string;
    }

    public void setForInGenre(List<String> forInGenre) {
        this.forInGenre = forInGenre;
    }

    public List<String> getForInGenre() {
        return forInGenre;
    }
}
