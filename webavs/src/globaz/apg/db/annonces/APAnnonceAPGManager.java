/*
 * Créé le 10 juin 05
 */
package globaz.apg.db.annonces;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 * @author mbo : correction pour BZ_8135, modification du 06.11.2013
 * @author mbo : la recherche d'annonces APG ne doit pas prendre en compte les annonces qui ont un idParent
 * @author mbo : la recherche d'annonces a partir du 01.09.2012 va pointer sur la methode getWhereRAPG, car les annonces
 *         dès cette période sont des annonces RAPG.
 */
public class APAnnonceAPGManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** Date de mise en application des RAPG **/
    public static final String DATE_PASSAGE_RAPG = "01.09.2012";

    /** Critére de recherche pour un etat envoyé ou erroné */
    public static final String FOR_ETAT_ENVOYE_OU_ERRONE = "envoyeOuErrone";

    /** Critère de recherche pour lister toutes les annonces APG (rev99 et 05) */
    public static final String FOR_TYPE_APG = "forapg";

    /** Critère de recherche pour lister tous les types d'annonces */
    public static final String FOR_TYPE_TOUS = "tous";

    /**
	 * 
	 */
    private static final long serialVersionUID = 6322890536024036401L;

    private String forBPID = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forContenuAnnonce = "";
    private String forContenuAnnonceDifferentDe = "";
    private String forDateEnvoi = "";
    private String forEnvelopeMessageId = "";
    private String forEtat = "";
    private String forEtatDifferentDe = "";
    private List<String> forGenreServiceIn = new ArrayList<String>();
    private String forIdParent = "";
    private String forMoisAnneeComptable = "";
    private String forMoisAnneeComptableDifferentDe = "";
    private String forNss = "";
    private String forPeriodeAu;
    private String forPeriodeDe;
    private String forTimeStamp = "";
    private String forType = "";
    private String forTypeAnnonce = "";
    private String fromMoisAnneeComptable = "";
    private Boolean isAnnoncesWithoutIdParent = false;
    private String forSubMessageType;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        return super._getFields(statement);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
     * Cette méthode va être redirigée selon la période des anonnces souhaitées, reçue en paramètre. Si période < que
     * 09.2012, retourne le WHERE des APG , sinon retourne le WHERE des RAPG
     */
    @Override
    protected String _getWhere(BStatement statement) {

        if (isAnnoncesAPG()) {
            return getWhereAPG(statement).toString();
        } else {
            return getWhereRAPG(statement).toString();
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APAnnonceAPG();
    }

    public final String getForBPID() {
        return forBPID;
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
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * @return the forEnvelopeMessageId
     */
    public String getForEnvelopeMessageId() {
        return forEnvelopeMessageId;
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
     * @return
     */
    public String getForEtatDifferentDe() {
        return forEtatDifferentDe;
    }

    public final List<String> getForGenreServiceIn() {
        return forGenreServiceIn;
    }

    public String getForIdParent() {
        return forIdParent;
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
     * @return the forNss
     */
    public String getForNss() {
        return forNss;
    }

    public final String getForPeriodeAu() {
        return forPeriodeAu;
    }

    public final String getForPeriodeDe() {
        return forPeriodeDe;
    }

    public final String getForTimeStamp() {
        return forTimeStamp;
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
     * @return the forTypeAnnonce
     */
    public String getForTypeAnnonce() {
        return forTypeAnnonce;
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
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APAnnonceAPG.FIELDNAME_IDANNONCE;
    }

    /**
     * Cette méthode est appelé pour les annonces d'une période inférieur au 01.09.2012 (APG)
     * 
     * @param statement
     * @return StringBuffer
     */
    private StringBuffer getWhereAPG(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        try {
            if (!JAUtil.isDateEmpty(fromMoisAnneeComptable)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
                sqlWhere.append(">=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                        JACalendar.format(new JADate(fromMoisAnneeComptable), JACalendar.FORMAT_YYYYMM)));
            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORECT"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtat) || forEtat.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (forEtat.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
                sqlWhere.append("(" + APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ENVOYE));
                sqlWhere.append(" OR ");
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ERRONE));
                sqlWhere.append(")");
            } else {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forEtat));
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtatDifferentDe)
                || forEtatDifferentDe.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (forEtat.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
                sqlWhere.append("(");
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ENVOYE));
                sqlWhere.append(" AND ");
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ERRONE));
                sqlWhere.append(")");
            } else {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forEtatDifferentDe));
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forType) && !forType.equals(APAnnonceAPGManager.FOR_TYPE_TOUS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (forType.equals(APAnnonceAPGManager.FOR_TYPE_APG)) {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_GENRE);
                sqlWhere.append(" NOT = '90'");
            } else {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_GENRE);
                sqlWhere.append(" = '90'");
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            try {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                        JACalendar.format(new JADate(forMoisAnneeComptable), JACalendar.FORMAT_YYYYMM)));
            } catch (JAException e1) {
                _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
            }
        }

        if (!JadeStringUtil.isEmpty(forNss)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_NUMEROASSURE);
            sqlWhere.append(" LIKE '");
            sqlWhere.append(forNss);
            sqlWhere.append("%'");
        }

        if (!JAUtil.isDateEmpty(forDateEnvoi)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_DATEENVOI);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateEnvoi));
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forContenuAnnonce));
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonceDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
            sqlWhere.append("<>");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forContenuAnnonceDifferentDe));
        }

        try {
            if (!JAUtil.isDateEmpty(forMoisAnneeComptableDifferentDe)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                        JACalendar.format(new JADate(forMoisAnneeComptableDifferentDe), JACalendar.FORMAT_YYYYMM)));

            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_IDPARENT);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdParent));
        } else {
            if (isAnnoncesWithoutIdParent) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(APAnnonceAPG.FIELDNAME_IDPARENT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), String.valueOf(0)));
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypeAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_TYPEANNONCE);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forTypeAnnonce));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEnvelopeMessageId)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_ENVELOPEMESSAGEID);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forEnvelopeMessageId));
        }

        // BPID
        if (!JadeStringUtil.isIntegerEmpty(forBPID)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forBPID));
        }

        // TimeStamp
        if (!JadeStringUtil.isIntegerEmpty(forTimeStamp)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_TIME_STAMP);
            sqlWhere.append("=");
            sqlWhere.append(forTimeStamp);
        }

        if (JadeDateUtil.isGlobazDate(forPeriodeDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_PERIODEDE);
            sqlWhere.append(">=");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forPeriodeDe));
        }
        if (JadeDateUtil.isGlobazDate(forPeriodeAu)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_PERIODEA);
            sqlWhere.append("<=");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forPeriodeAu));
        }
        if ((forGenreServiceIn != null) && (forGenreServiceIn.size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_GENRE);
            sqlWhere.append(" IN (");
            for (int ctr = 0; ctr < forGenreServiceIn.size(); ctr++) {
                sqlWhere.append("'" + forGenreServiceIn.get(ctr));
                sqlWhere.append("'");
                if ((ctr + 1) < forGenreServiceIn.size()) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(")");
        }

        return sqlWhere;
    }

    /**
     * Cette méthode est appelé pour les annonces d'une période égal ou supérieur au 01.09.2012 (RAPG)
     * 
     * @param statement
     * @return StringBuffer
     */
    private StringBuffer getWhereRAPG(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        try {
            if (!JAUtil.isDateEmpty(fromMoisAnneeComptable)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
                sqlWhere.append(">=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                        JACalendar.format(new JADate(fromMoisAnneeComptable), JACalendar.FORMAT_YYYYMM)));
            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORECT"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtat) || forEtat.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (forEtat.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
                sqlWhere.append("(" + APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ENVOYE));
                sqlWhere.append(" OR ");
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ERRONE));
                sqlWhere.append(")");
            } else {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forEtat));
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtatDifferentDe)
                || forEtatDifferentDe.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (forEtat.equals(APAnnonceAPGManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
                sqlWhere.append("(");
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ENVOYE));
                sqlWhere.append(" AND ");
                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ERRONE));
                sqlWhere.append(")");
            } else {

                sqlWhere.append(APAnnonceAPG.FIELDNAME_ETAT);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forEtatDifferentDe));

            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forType) && !forType.equals(APAnnonceAPGManager.FOR_TYPE_TOUS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (forType.equals(APAnnonceAPGManager.FOR_TYPE_APG)) {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_GENRE);
                sqlWhere.append(" NOT = '90'");
            } else {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_GENRE);
                sqlWhere.append(" = '90'");
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            try {
                sqlWhere.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
                sqlWhere.append("=");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                        JACalendar.format(new JADate(forMoisAnneeComptable), JACalendar.FORMAT_YYYYMM)));
            } catch (JAException e1) {
                _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
            }
        }

        // BPID
        if (!JadeStringUtil.isIntegerEmpty(forBPID)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forBPID));
        }

        if (!JadeStringUtil.isEmpty(forNss)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_NUMEROASSURE);
            sqlWhere.append(" LIKE '");
            sqlWhere.append(forNss);
            sqlWhere.append("%'");
        }

        if (!JAUtil.isDateEmpty(forDateEnvoi)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_DATEENVOI);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateEnvoi));
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
            sqlWhere.append("=");
            sqlWhere.append("'" + APAnnoncesRapgService.subMessageType4 + "'");
            sqlWhere.append(" AND ");
            sqlWhere.append(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forContenuAnnonce));
        }

        if (!JadeStringUtil.isIntegerEmpty(forContenuAnnonceDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
            sqlWhere.append("=");
            sqlWhere.append("'");
            sqlWhere.append(APAnnoncesRapgService.subMessageType3);
            sqlWhere.append("'");
            sqlWhere.append(" OR ");
            sqlWhere.append(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
            sqlWhere.append("=");
            sqlWhere.append("'");
            sqlWhere.append(APAnnoncesRapgService.subMessageType1);
            sqlWhere.append("'");
            sqlWhere.append(")");

        }

        try {
            if (!JAUtil.isDateEmpty(forMoisAnneeComptableDifferentDe)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
                sqlWhere.append("<>");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                        JACalendar.format(new JADate(forMoisAnneeComptableDifferentDe), JACalendar.FORMAT_YYYYMM)));

            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_IDPARENT);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdParent));
        } else {
            if (isAnnoncesWithoutIdParent) {
                if (isAnnoncesWithoutIdParent) {
                    if (sqlWhere.length() != 0) {
                        sqlWhere.append(" AND ");
                    }

                    sqlWhere.append(APAnnonceAPG.FIELDNAME_IDPARENT);
                    sqlWhere.append("=");
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), String.valueOf(0)));
                }
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypeAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_TYPEANNONCE);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forTypeAnnonce));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEnvelopeMessageId)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(APAnnonceAPG.FIELDNAME_ENVELOPEMESSAGEID);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forEnvelopeMessageId));
        }

        // BPID
        if (!JadeStringUtil.isIntegerEmpty(forBPID)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forBPID));
        }

        // SubMessageType (annonce type 1, 3 ou 4)
        if (!JadeStringUtil.isIntegerEmpty(forSubMessageType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forSubMessageType));
        }

        // TimeStamp
        if (!JadeStringUtil.isIntegerEmpty(forTimeStamp)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_TIME_STAMP);
            sqlWhere.append("=");
            sqlWhere.append(forTimeStamp);
        }

        if (JadeDateUtil.isGlobazDate(forPeriodeDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_PERIODEDE);
            sqlWhere.append(">=");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forPeriodeDe));
        }
        if (JadeDateUtil.isGlobazDate(forPeriodeAu)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_PERIODEA);
            sqlWhere.append("<=");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forPeriodeAu));
        }
        if ((forGenreServiceIn != null) && (forGenreServiceIn.size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(APAnnonceAPG.FIELDNAME_GENRE);
            sqlWhere.append(" IN ");
            sqlWhere.append("(");
            for (int ctr = 0; ctr < forGenreServiceIn.size(); ctr++) {
                sqlWhere.append("'");
                sqlWhere.append(forGenreServiceIn.get(ctr));
                sqlWhere.append("'");
                if ((ctr + 1) < forGenreServiceIn.size()) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(")");
        }

        return sqlWhere;

    }

    /**
     * Methode pour vérifier si la période reçue en paramètre est inférieur au 01.09.2013, date à laquelle les annonces
     * RAPG sont rentrées en vigueur.
     * 
     * @return
     */
    private boolean isAnnoncesAPG() {

        return JadeDateUtil.isDateBefore("01." + forMoisAnneeComptable, APAnnonceAPGManager.DATE_PASSAGE_RAPG);
    }

    public final void setForBPID(String forBPID) {
        this.forBPID = forBPID;
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
     * setter pour l'attribut for date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * @param forEnvelopeMessageId
     *            the forEnvelopeMessageId to set
     */
    public void setForEnvelopeMessageId(String forEnvelopeMessageId) {
        this.forEnvelopeMessageId = forEnvelopeMessageId;
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
     * @param string
     */
    public void setForEtatDifferentDe(String string) {
        forEtatDifferentDe = string;
    }

    public final void setForGenreServiceIn(List<String> forGenreServiceIn) {
        this.forGenreServiceIn = forGenreServiceIn;
    }

    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    /**
     * setter pour l'attribut for mois annee comptable.
     * 
     * @exception : Si date ne respecte pas le format mm.aaaa
     * @param string
     */
    public void setForMoisAnneeComptable(String date) {
        if (!JadeDateUtil.isGlobazDateMonthYear(date)) {
            throw new IllegalArgumentException("La date n'est pas correcte: " + date);
        }
        forMoisAnneeComptable = date;
    }

    /**
     * setter pour l'attribut for mois annee comptable different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptableDifferentDe(String string) {
        forMoisAnneeComptableDifferentDe = string;
    }

    /**
     * @param forNss
     *            the forNss to set
     */
    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    public final void setForPeriodeAu(String forPeriodeAu) {
        this.forPeriodeAu = forPeriodeAu;
    }

    public final void setForPeriodeDe(String forPeriodeDe) {
        this.forPeriodeDe = forPeriodeDe;
    }

    public final void setForTimeStamp(String forTimeStamp) {
        this.forTimeStamp = forTimeStamp;
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
     * @param forTypeAnnonce
     *            the forTypeAnnonce to set
     */
    public void setForTypeAnnonce(String forTypeAnnonce) {
        this.forTypeAnnonce = forTypeAnnonce;
    }

    /**
     * setter pour l'attribut from mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromMoisAnneeComptable(String string) {
        fromMoisAnneeComptable = string;
    }

    /**
     * @param isAnnoncesWithoutIdParent
     *            the isAnnoncesWithoutIdParent to set
     */
    public void setIsAnnoncesWithoutIdParent(Boolean isAnnoncesWithoutIdParent) {
        this.isAnnoncesWithoutIdParent = isAnnoncesWithoutIdParent;
    }

    public void setForSubMessageType(String subMessageType) {
        forSubMessageType = subMessageType;
    }

    public final String getForSubMessageType() {
        return forSubMessageType;
    }

}
