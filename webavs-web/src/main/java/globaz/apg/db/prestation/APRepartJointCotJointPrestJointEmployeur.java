/*
 * Créé le 31 août 05
 */
package globaz.apg.db.prestation;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.properties.APProperties;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.PRAffiliationHelper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APRepartJointCotJointPrestJointEmployeur extends APRepartitionPaiements {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     */
    public static final String AC = "AC";
    /**
     */
    public static final String AVS = "AVS";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String COT_FNE = "COT_FNE";

    /**
     */
    public static final String FRAIS_ADMINISTRATION = "FRAIS";

    /**
     */
    public static final String IMPOT_SOURCE = "IMPOTSOURCE";

    /**
     */
    public static final String LFA = "LFA";

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);

        // jointure entre table des repartitions et cotisations
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APCotisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APCotisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);

        // jointure entre table des repartitions et prestations
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);

        // jointure avec Employeur
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDSITUATIONPROFESSIONNELLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF);
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APSituationProfessionnelle.FIELDNAME_IDEMPLOYEUR);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEmployeur.FIELDNAME_ID_EMPLOYEUR);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String contenuAnnonce = "";
    private String dateDebutPrestation = "";
    private String fromClause = null;
    private String genrePrestation = "";
    private String idDepartement = "";
    private String idExterne = "";
    private Boolean isIndependant = Boolean.FALSE;
    private String montantBrutCotisation = "";
    private String montantCotisation = "";
    private String noRevision = "";
    private String tauxCotisation = "";

    private String typeCotisation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(BStatement statement) {
        // TODO prendre que ce qu'on a besoin
        return super._getFields(statement);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = APRepartJointCotJointPrestJointEmployeur.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        montantCotisation = statement.dbReadNumeric(APCotisation.FIELDNAME_MONTANT);
        typeCotisation = statement.dbReadString(APCotisation.FIELDNAME_TYPE);
        idExterne = statement.dbReadNumeric(APCotisation.FIELDNAME_ID_EXTERNE);
        contenuAnnonce = statement.dbReadNumeric(APPrestation.FIELDNAME_CONTENUANNONCE);
        noRevision = statement.dbReadNumeric(APPrestation.FIELDNAME_NOREVISION);
        dateDebutPrestation = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEDEBUT);
        genrePrestation = statement.dbReadNumeric(APPrestation.FIELDNAME_GENRE_PRESTATION);
        idDepartement = statement.dbReadNumeric(APEmployeur.FIELDNAME_ID_PARTICULARITE);
        isIndependant = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISINDEPENDANT);
        tauxCotisation = statement.dbReadNumeric(APCotisation.FIELDNAME_TAUX);
        montantBrutCotisation = statement.dbReadNumeric(APCotisation.FIELDNAME_MONTANTBRUT);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * getter pour l'attribut contenu annonce
     * 
     * @return la valeur courante de l'attribut contenu annonce
     */
    public String getContenuAnnonce() {
        return contenuAnnonce;
    }

    /**
     * getter pour l'attribut date debut prestation
     * 
     * @return la valeur courante de l'attribut date debut prestation
     */
    public String getDateDebutPrestation() {
        return dateDebutPrestation;
    }

    /**
     * getter pour l'attribut genre cotisation
     * 
     * @return la valeur courante de l'attribut genre cotisation
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String getGenreCotisation() throws Exception {
        if (typeCotisation.equals(APCotisation.TYPE_IMPOT)) {
            return APRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE;
        } else {
            if (PRAffiliationHelper.GENRE_AC.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_AC.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return APRepartJointCotJointPrestJointEmployeur.AC;
            } else if (PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return APRepartJointCotJointPrestJointEmployeur.AVS;
            } else if (PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return APRepartJointCotJointPrestJointEmployeur.FRAIS_ADMINISTRATION;
            } else if (PRAffiliationHelper.GENRE_LFA.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_LFA.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return APRepartJointCotJointPrestJointEmployeur.LFA;
            } else if (!JadeStringUtil.isBlankOrZero(idExterne)
                    && GlobazSystem.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                            .getProperty(APProperties.ASSURANCE_FNE_ID.getPropertyName(), "")
                            .equalsIgnoreCase(idExterne)) {
                return APRepartJointCotJointPrestJointEmployeur.COT_FNE;
            }

            else {
                return "";
            }
        }
    }

    /**
     * getter pour l'attribut genre prestation
     * 
     * @return la valeur courante de l'attribut genre prestation
     */
    public String getGenrePrestation() {
        return genrePrestation;
    }

    /**
     * getter pour l'attribut id departement
     * 
     * @return la valeur courante de l'attribut id departement
     */
    public String getIdDepartement() {
        return idDepartement;
    }

    /**
     * getter pour l'attribut id externe
     * 
     * @return la valeur courante de l'attribut id externe
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * getter pour l'attribut is independant
     * 
     * @return la valeur courante de l'attribut is independant
     */
    public Boolean getIsIndependant() {
        return isIndependant;
    }

    public String getMontantBrutCotisation() {
        return montantBrutCotisation;
    }

    /**
     * getter pour l'attribut montantCotisation
     * 
     * @return la valeur courante de l'attribut montantCotisation
     */
    public String getMontantCotisation() {
        return montantCotisation;
    }

    /**
     * getter pour l'attribut no revision
     * 
     * @return la valeur courante de l'attribut no revision
     */
    public String getNoRevision() {
        return noRevision;
    }

    public String getTauxCotisation() {
        return tauxCotisation;
    }

    /**
     * getter pour l'attribut type cotisation
     * 
     * @return la valeur courante de l'attribut type cotisation
     */
    public String getTypeCotisation() {
        return typeCotisation;
    }

}
