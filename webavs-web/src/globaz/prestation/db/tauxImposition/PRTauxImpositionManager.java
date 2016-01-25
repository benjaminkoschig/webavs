/*
 * Créé le 23 juin 05
 */
package globaz.prestation.db.tauxImposition;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class PRTauxImpositionManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String afterDateFin = "";
    private String beforeDateFin = "";
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String forCsCanton = "";
    private String forIdTauxImposition = "";
    private String forIdTauxImpositionDifferentFrom = "";
    private boolean forPeriodeNotClose = false;

    private String forTypeImpot = "";
    private String fromDateDebut = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdTauxImposition)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_IDTAUXIMPOSITION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdTauxImposition));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTauxImpositionDifferentFrom)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_IDTAUXIMPOSITION);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdTauxImpositionDifferentFrom));
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypeImpot)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_TYPEIMPOTSOURCE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forTypeImpot));
        }

        if (!JAUtil.isDateEmpty(fromDateDebut)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_DATEDEBUT);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), fromDateDebut));
        }

        if (!JAUtil.isDateEmpty(beforeDateFin)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_DATEFIN);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), beforeDateFin));
        }

        if (!JAUtil.isDateEmpty(afterDateFin)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_DATEFIN);
            whereClause.append(">");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), afterDateFin));
        }

        if (!JAUtil.isDateEmpty(dateDebutPeriode)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_DATEDEBUT);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), dateFinPeriode));
            whereClause.append(" AND (");
            whereClause.append(PRTauxImposition.FIELDNAME_DATEFIN);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(" OR ");
            whereClause.append(PRTauxImposition.FIELDNAME_DATEFIN);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), dateDebutPeriode));
            whereClause.append(")");
        }

        // id canton
        if (!JadeStringUtil.isIntegerEmpty(forCsCanton)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_CS_CANTON);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forCsCanton));
        }

        if (forPeriodeNotClose) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(PRTauxImposition.FIELDNAME_DATEFIN);
            whereClause.append("=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), ""));
        }

        return whereClause.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new PRTauxImposition();
    }

    /**
     * @return
     */
    public String getAfterDateFin() {
        return afterDateFin;
    }

    /**
     * getter pour l'attribut before date fin
     * 
     * @return la valeur courante de l'attribut before date fin
     */
    public String getBeforeDateFin() {
        return beforeDateFin;
    }

    /**
     * getter pour l'attribut for id canton
     * 
     * @return la valeur courante de l'attribut for id canton
     */
    public String getForCsCanton() {
        return forCsCanton;
    }

    /**
     * getter pour l'attribut for id taux imposition
     * 
     * @return la valeur courante de l'attribut for id taux imposition
     */
    public String getForIdTauxImposition() {
        return forIdTauxImposition;
    }

    /**
     * @return
     */
    public String getForIdTauxImpositionDifferentFrom() {
        return forIdTauxImpositionDifferentFrom;
    }

    /**
     * getter pour l'attribut for type impot
     * 
     * @return la valeur courante de l'attribut for type impot
     */
    public String getForTypeImpot() {
        return forTypeImpot;
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
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return PRTauxImposition.FIELDNAME_IDTAUXIMPOSITION;
    }

    /**
     * @return
     */
    public boolean isForPeriodeNotClose() {
        return forPeriodeNotClose;
    }

    /**
     * @param string
     */
    public void setAfterDateFin(String string) {
        afterDateFin = string;
    }

    /**
     * setter pour l'attribut before date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setBeforeDateFin(String string) {
        beforeDateFin = string;
    }

    /**
     * setter pour l'attribut for id canton
     * 
     * @param forIdCanton
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsCanton(String forIdCanton) {
        forCsCanton = forIdCanton;
    }

    /**
     * setter pour l'attribut for id taux imposition
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTauxImposition(String string) {
        forIdTauxImposition = string;
    }

    /**
     * @param string
     */
    public void setForIdTauxImpositionDifferentFrom(String string) {
        forIdTauxImpositionDifferentFrom = string;
    }

    /**
     * setter pour l'attribut for periode
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPeriode(String dateDebut, String dateFin) {
        dateDebutPeriode = dateDebut;
        dateFinPeriode = dateFin;
    }

    /**
     * @param b
     */
    public void setForPeriodeNotClose(boolean b) {
        forPeriodeNotClose = b;
    }

    /**
     * setter pour l'attribut for type impot
     * 
     * @param forTypeImpot
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeImpot(String forTypeImpot) {
        this.forTypeImpot = forTypeImpot;
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

}
