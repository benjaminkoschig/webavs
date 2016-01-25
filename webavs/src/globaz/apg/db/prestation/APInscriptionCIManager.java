/*
 * Créé le 18 juil. 05
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APInscriptionCIManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNoPassage = "";
    private String forStatut = "";
    private String toAnnee = "";
    private String toMoisAnnee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forStatut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APInscriptionCI.FIELDNAME_STATUT + "=" + _dbWriteNumeric(statement.getTransaction(), forStatut);
        }

        if (!JadeStringUtil.isIntegerEmpty(toAnnee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APInscriptionCI.FIELDNAME_ANNEE + "<=" + _dbWriteNumeric(statement.getTransaction(), toAnnee);
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoPassage)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APInscriptionCI.FIELDNAME_NOPASSAGE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forNoPassage);
        }

        try {
            if (!JadeStringUtil.isIntegerEmpty(toMoisAnnee)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                JADate date = new JADate(toMoisAnnee);

                sqlWhere += "(" + APInscriptionCI.FIELDNAME_ANNEE + "<"
                        + _dbWriteNumeric(statement.getTransaction(), Integer.toString(date.getYear())) + " OR ("
                        + APInscriptionCI.FIELDNAME_ANNEE + "="
                        + _dbWriteNumeric(statement.getTransaction(), Integer.toString(date.getYear())) + " AND "
                        + APInscriptionCI.FIELDNAME_MOISFIN + "<=" + Integer.toString(date.getMonth()) + "))";
            }
        } catch (JAException e) {
            statement.getTransaction().addErrors("FORMAT_DATES_INCORRECT");
        }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APInscriptionCI();
    }

    /**
     * @return
     */
    public String getForNoPassage() {
        return forNoPassage;
    }

    /**
     * getter pour l'attribut for statut
     * 
     * @return la valeur courante de l'attribut for statut
     */
    public String getForStatut() {
        return forStatut;
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
        return APInscriptionCI.FIELDNAME_NOAVS + ", " + APInscriptionCI.FIELDNAME_ANNEE + ", "
                + APInscriptionCI.FIELDNAME_MOISDEBUT + ", " + APInscriptionCI.FIELDNAME_MOISFIN;
    }

    /**
     * getter pour l'attribut to annee
     * 
     * @return la valeur courante de l'attribut to annee
     */
    public String getToAnnee() {
        return toAnnee;
    }

    /**
     * getter pour l'attribut to mois annee
     * 
     * @return la valeur courante de l'attribut to mois annee
     */
    public String getToMoisAnnee() {
        return toMoisAnnee;
    }

    /**
     * @param string
     */
    public void setForNoPassage(String string) {
        forNoPassage = string;
    }

    /**
     * setter pour l'attribut for statut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForStatut(String string) {
        forStatut = string;
    }

    /**
     * setter pour l'attribut to annee
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setToAnnee(String string) {
        toAnnee = string;
    }

    /**
     * setter pour l'attribut to mois annee
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setToMoisAnnee(String string) {
        toMoisAnnee = string;
    }

}
