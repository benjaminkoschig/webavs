/*
 * Créé le 3 oct. 05
 */
package globaz.ij.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJAnnonceHierarchiqueManager extends PRAbstractManagerHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Critére de recherche pour un etat envoyé ou erroné */
    public static final String FOR_ETAT_ENVOYE_OU_ERRONE = "envoyeOuErrone";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsEtat = "";
    private String forDateEnvoi = "";
    private String forMoisAnneeComptable = "";
    private String forMoisAnneeComptableDifferentDe = "";
    private String fromMoisAnneeComptable = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        return "parent." + IJAnnonce.FIELDNAME_IDANNONCE + ", parent." + IJAnnonce.FIELDNAME_CODEAPPLICATION
                + ", parent." + IJAnnonce.FIELDNAME_CODEENREGISTREMENT + ", parent." + IJAnnonce.FIELDNAME_NOCAISSE
                + ", parent." + IJAnnonce.FIELDNAME_NOAGENCE + ", parent." + IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE
                + ", parent." + IJAnnonce.FIELDNAME_CONTENUANNONCE + ", parent." + IJAnnonce.FIELDNAME_PETITEIJ
                + ", parent." + IJAnnonce.FIELDNAME_NOASSURE + ", parent." + IJAnnonce.FIELDNAME_NOASSURECONJOINT
                + ", parent." + IJAnnonce.FIELDNAME_CODEETATCIVIL + ", parent." + IJAnnonce.FIELDNAME_NOMBREENFANTS
                + ", parent." + IJAnnonce.FIELDNAME_REVENUJOURNALIERDETERMINANT + ", parent."
                + IJAnnonce.FIELDNAME_OFFICEAI + ", parent." + IJAnnonce.FIELDNAME_CS_GENREREADAPTATION + ", parent."
                + IJAnnonce.FIELDNAME_GARANTIEAA + ", parent." + IJAnnonce.FIELDNAME_IJREDUITE + ", parent."
                + IJAnnonce.FIELDNAME_DROITACQUIS4EMEREVISION + ", parent."
                + IJAnnonce.FIELDNAME_CODEENREGISTREMENTSUIVANTPERIODEIJ2ET3 + ", parent."
                + IJAnnonce.FIELDNAME_CS_ETAT + ", parent." + IJAnnonce.FIELDNAME_IDPARENT + ", parent."
                + IJAnnonce.FIELDNAME_PARAMSPECIFIQUE3EMEREVISIONSUR5POSITIONS + ", parent."
                + IJAnnonce.fieldname_DATEENVOI;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        return " " + _getDbSchema() + "." + IJAnnonce.TABLE_NAME + " as parent left outer join " + _getDbSchema() + "."
                + IJAnnonce.TABLE_NAME + " as enfant on enfant." + IJAnnonce.FIELDNAME_IDPARENT + " = parent."
                + IJAnnonce.FIELDNAME_IDANNONCE;
    }

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
        BTransaction transaction = statement.getTransaction();

        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (forCsEtat.equals(IIJAnnonce.ETATS_NON_ENVOYE)) {
                sqlWhere += " (parent." + IJAnnonce.FIELDNAME_CS_ETAT + " <> " + IIJAnnonce.CS_ENVOYEE + " or(parent."
                        + IJAnnonce.FIELDNAME_CS_ETAT + " = " + IIJAnnonce.CS_ENVOYEE + " and enfant."
                        + IJAnnonce.FIELDNAME_CS_ETAT + " = " + IIJAnnonce.CS_ERRONEE + ")) ";
            } else {
                sqlWhere += "parent." + IJAnnonce.FIELDNAME_CS_ETAT + "=" + _dbWriteNumeric(transaction, forCsEtat);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(fromMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "parent." + IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE + ">="
                    + _dbWriteNumeric(transaction, formatMoisAnneeComptableToDB(fromMoisAnneeComptable));
        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "parent." + IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE + "="
                    + _dbWriteNumeric(transaction, formatMoisAnneeComptableToDB(forMoisAnneeComptable));
        }

        if (!JAUtil.isDateEmpty(forDateEnvoi)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "parent." + IJAnnonce.fieldname_DATEENVOI + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDateEnvoi);
        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            try {
                sqlWhere += "parent."
                        + IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE
                        + "="
                        + _dbWriteNumeric(statement.getTransaction(),
                                JACalendar.format(new JADate(forMoisAnneeComptable), JACalendar.FORMAT_YYYYMM));
            } catch (JAException e1) {
                _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
            }
        }

        try {
            if (!JAUtil.isDateEmpty(forMoisAnneeComptableDifferentDe)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "parent."
                        + IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE
                        + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), JACalendar.format(new JADate(
                                forMoisAnneeComptableDifferentDe), JACalendar.FORMAT_YYYYMM));
            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
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
        return new IJAnnonce();
    }

    private String formatMoisAnneeComptableToDB(String date) {
        try {
            if (date.length() == 6) {
                return date.substring(2) + "0" + date.substring(0, 1);
            } else {
                return date.substring(3) + date.substring(0, 2);
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * getter pour l'attribut for cs etat
     * 
     * @return la valeur courante de l'attribut for cs etat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * getter pour l'attribut for mois annee comptable
     * 
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    /**
     * getter pour l'attribut for mois annee comptable different de
     * 
     * @return la valeur courante de l'attribut for mois annee comptable different de
     */
    public String getForMoisAnneeComptableDifferentDe() {
        return forMoisAnneeComptableDifferentDe;
    }

    /**
     * getter pour l'attribut from mois annee comptable
     * 
     * @return la valeur courante de l'attribut from mois annee comptable
     */
    public String getFromMoisAnneeComptable() {
        return fromMoisAnneeComptable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRAbstractManagerHierarchique#getHierarchicalOrderBy ()
     */
    @Override
    public String getHierarchicalOrderBy() {
        String result = "";
        result = " CASE WHEN parent." + IJAnnonce.FIELDNAME_IDPARENT + "=0" + " THEN " + " char(parent."
                + IJAnnonce.FIELDNAME_IDANNONCE + ") " + " ELSE " + " char(parent." + IJAnnonce.FIELDNAME_IDPARENT
                + ")" + " END " + "|| '-' || char(parent." + IJAnnonce.FIELDNAME_IDANNONCE + ") ";

        return result;
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
        return " parent." + IJAnnonce.FIELDNAME_IDANNONCE;
    }

    /**
     * setter pour l'attribut for cs etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    /**
     * setter pour l'attribut for date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut (MM.AAAA)
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut (MM.AAAA)
     */
    public void setForMoisAnneeComptableDifferentDe(String string) {
        forMoisAnneeComptableDifferentDe = string;
    }

    /**
     * setter pour l'attribut from mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut (au format MM.AAAA)
     */
    public void setFromMoisAnneeComptable(String string) {
        fromMoisAnneeComptable = string;
    }

}
