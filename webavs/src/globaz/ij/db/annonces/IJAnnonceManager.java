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
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJAnnonceManager extends PRAbstractManager {

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

    private String forContenuAnnonce = "";
    private String forContenuAnnonceDifferentDe = "";
    private String forCsEtat = "";
    private String forCsEtatDifferentDe = "";
    private String forDateEnvoi = "";
    private String forIdParent = "";
    private Boolean forIsExclureAnnonceEnfant = null;
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

        return super._getFields(statement);

        // return IJAnnonce.FIELDNAME_IDANNONCE + ", " +
        // IJAnnonce.FIELDNAME_CODEAPPLICATION + ", " +
        // IJAnnonce.FIELDNAME_CODEENREGISTREMENT + ", " +
        // IJAnnonce.FIELDNAME_NOCAISSE + ", " +
        // IJAnnonce.FIELDNAME_NOAGENCE + ", " +
        // IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE + ", " +
        // IJAnnonce.FIELDNAME_CONTENUANNONCE + ", " +
        // IJAnnonce.FIELDNAME_PETITEIJ + ", " +
        // IJAnnonce.FIELDNAME_NOASSURE + ", " +
        // IJAnnonce.FIELDNAME_NOASSURECONJOINT + ", " +
        // IJAnnonce.FIELDNAME_CODEETATCIVIL + ", " +
        // IJAnnonce.FIELDNAME_NOMBREENFANTS + ", " +
        // IJAnnonce.FIELDNAME_REVENUJOURNALIERDETERMINANT + ", " +
        // IJAnnonce.FIELDNAME_OFFICEAI + ", " +
        // IJAnnonce.FIELDNAME_CS_GENREREADAPTATION + ", " +
        // IJAnnonce.FIELDNAME_GARANTIEAA + ", " +
        // IJAnnonce.FIELDNAME_IJREDUITE + ", " +
        // IJAnnonce.FIELDNAME_DROITACQUIS4EMEREVISION + ", " +
        // IJAnnonce.FIELDNAME_CODEENREGISTREMENTSUIVANTPERIODEIJ2ET3 + ", " +
        // IJAnnonce.FIELDNAME_CS_ETAT + ", " +
        // IJAnnonce.FIELDNAME_IDPARENT + ", " +
        // IJAnnonce.FIELDNAME_PARAMSPECIFIQUE3EMEREVISIONSUR5POSITIONS + ", " +
        // IJAnnonce.fieldname_DATEENVOI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
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

            sqlWhere += IJAnnonce.FIELDNAME_CS_ETAT + "=" + _dbWriteNumeric(transaction, forCsEtat);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtatDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.FIELDNAME_CS_ETAT + "<>" + _dbWriteNumeric(transaction, forCsEtatDifferentDe);
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.FIELDNAME_CONTENUANNONCE + "="
                    + _dbWriteString(statement.getTransaction(), forContenuAnnonce);
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonceDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.FIELDNAME_CONTENUANNONCE + "<>"
                    + _dbWriteString(statement.getTransaction(), forContenuAnnonceDifferentDe);
        }

        if (!JadeStringUtil.isIntegerEmpty(fromMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE + ">="
                    + _dbWriteNumeric(transaction, formatMoisAnneeComptableToDB(fromMoisAnneeComptable));
        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE + "="
                    + _dbWriteNumeric(transaction, formatMoisAnneeComptableToDB(forMoisAnneeComptable));
        }

        if (!JAUtil.isDateEmpty(forDateEnvoi)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.fieldname_DATEENVOI + "=" + _dbWriteDateAMJ(statement.getTransaction(), forDateEnvoi);
        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            try {
                sqlWhere += IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE
                        + "="
                        + _dbWriteNumeric(statement.getTransaction(),
                                JACalendar.format(new JADate(forMoisAnneeComptable), JACalendar.FORMAT_YYYYMM));
            } catch (JAException e1) {
                _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
            }
        }

        if (getForIsExclureAnnonceEnfant() != null && getForIsExclureAnnonceEnfant().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ( " + IJAnnonce.FIELDNAME_IDPARENT + " IS NULL OR " + IJAnnonce.FIELDNAME_IDPARENT
                    + " = 0 ) ";
        }

        try {
            if (!JAUtil.isDateEmpty(forMoisAnneeComptableDifferentDe)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE
                        + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), JACalendar.format(new JADate(
                                forMoisAnneeComptableDifferentDe), JACalendar.FORMAT_YYYYMM));
            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJAnnonce.FIELDNAME_IDPARENT + "=" + _dbWriteNumeric(transaction, forIdParent);
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
     * @return
     */
    public String getForContenuAnnonce() {
        return forContenuAnnonce;
    }

    /**
     * @return
     */
    public String getForContenuAnnonceDifferentDe() {
        return forContenuAnnonceDifferentDe;
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
     * @return
     */
    public String getForCsEtatDifferentDe() {
        return forCsEtatDifferentDe;
    }

    /**
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    public String getForIdParent() {
        return forIdParent;
    }

    public Boolean getForIsExclureAnnonceEnfant() {
        return forIsExclureAnnonceEnfant;
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

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJAnnonce.FIELDNAME_IDANNONCE;
    }

    /**
     * @param string
     */
    public void setForContenuAnnonce(String string) {
        forContenuAnnonce = string;
    }

    /**
     * @param string
     */
    public void setForContenuAnnonceDifferentDe(String string) {
        forContenuAnnonceDifferentDe = string;
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
     * @param string
     */
    public void setForCsEtatDifferentDe(String string) {
        forCsEtatDifferentDe = string;
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

    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    public void setForIsExclureAnnonceEnfant(Boolean forIsExclureAnnonceEnfant) {
        this.forIsExclureAnnonceEnfant = forIsExclureAnnonceEnfant;
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
