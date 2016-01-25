package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * Descpription
 * 
 * @author scr Date de création 24 mai 05
 */
public class APDroitLAPGJoinPrestationManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        // jointure entre table des demandes et table des droits
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDDROIT);

        return fromClauseBuffer.toString();
    }

    private String forContenuAnnonce = null;
    private String forEtat = null;

    private String forIdDroit = null;

    // Recherche par droit groupe.
    // C'est-à-dire, tous les groupe groupé entre eux par la notion de droit
    // parent et enfants.
    // 1 seul niveau hiérarchique.
    private String forIdDroitGroupe = "";

    private String forIdLot = null;
    private String forIdRestitution = null;
    private boolean forPrestationAvecDroitAcquis = false;
    private String forTypeDifferentDe = null;
    private transient String fromClause = null;

    private String fromDateDebut = null;

    private String fromDateFin = null;

    private String toDateDebut = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String toDateFin = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitLAPGJoinPrestationManager.
     */
    public APDroitLAPGJoinPrestationManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdDroitGroupe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_IDDROIT_LAPG
                    + " = " + _dbWriteNumeric(statement.getTransaction(), forIdDroitGroupe) + " OR " + _getCollection()
                    + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT + " = "
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroitGroupe) + " ) ";
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDroit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDDROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDroit());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdRestitution())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDRESTITUTION + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdRestitution());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForContenuAnnonce())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_CONTENUANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForContenuAnnonce());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForEtat());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdLot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDLOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }

        if (!JAUtil.isDateEmpty(getFromDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEFIN + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateFin());
        }

        if (!JAUtil.isDateEmpty(getFromDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEDEBUT + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }

        if (!JAUtil.isDateEmpty(getToDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEDEBUT + "<="
                    + _dbWriteDateAMJ(statement.getTransaction(), getToDateDebut());
        }

        if (forPrestationAvecDroitAcquis) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DROITACQUIS
                    + "='' OR " + _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DROITACQUIS
                    + " IS NULL";
        }

        if (!JAUtil.isDateEmpty(getToDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEFIN + "<="
                    + _dbWriteDateAMJ(statement.getTransaction(), getToDateFin());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForTypeDifferentDe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_TYPE + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), getForTypeDifferentDe());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
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
     * getter pour l'attribut for id droit
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for id droit groupe
     * 
     * @return la valeur courante de l'attribut for id droit groupe
     */
    public String getForIdDroitGroupe() {
        return forIdDroitGroupe;
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
     * getter pour l'attribut for type different de
     * 
     * @return la valeur courante de l'attribut for type different de
     */
    public String getForTypeDifferentDe() {
        return forTypeDifferentDe;
    }

    /**
     * getter pour l'attribut from clause
     * 
     * @return la valeur courante de l'attribut from clause
     */
    public String getFromClause() {
        return fromClause;
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
     * getter pour l'attribut from date fin
     * 
     * @return la valeur courante de l'attribut from date fin
     */
    public String getFromDateFin() {
        return fromDateFin;
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
     * getter pour l'attribut to date fin
     * 
     * @return la valeur courante de l'attribut to date fin
     */
    public String getToDateFin() {
        return toDateFin;
    }

    /**
     * getter pour l'attribut for prestation avec droit acquis
     * 
     * @return la valeur courante de l'attribut for prestation avec droit acquis
     */
    public boolean isForPrestationAvecDroitAcquis() {
        return forPrestationAvecDroitAcquis;
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
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }

    /**
     * setter pour l'attribut for id droit groupe
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroitGroupe(String string) {
        forIdDroitGroupe = string;
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
     * setter pour l'attribut for prestation avec droit acquis
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPrestationAvecDroitAcquis(boolean b) {
        forPrestationAvecDroitAcquis = b;
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
     * setter pour l'attribut from clause
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromClause(String string) {
        fromClause = string;
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
     * setter pour l'attribut from date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateFin(String string) {
        fromDateFin = string;
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
     * setter pour l'attribut to date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setToDateFin(String string) {
        toDateFin = string;
    }
}
