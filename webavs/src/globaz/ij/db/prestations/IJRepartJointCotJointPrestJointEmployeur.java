/*
 * Créé le 31 août 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.af.PRAffiliationHelper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartJointCotJointPrestJointEmployeur extends IJRepartitionPaiements {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

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

    /**
     */
    public static final String FRAIS_ADMINISTRATION = "FRAIS";

    /**
     */
    public static final String IMPOT_SOURCE = "IMPOTSOURCE";

    /**
     */
    public static final String LFA = "LFA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

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
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);

        // jointure entre table des repartitions et cotisations
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJCotisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJCotisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJCotisation.FIELDNAME_IDREPARTITIONPAIEMENTS);

        // jointure entre table des repartitions et prestations
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJRepartitionPaiements.FIELDNAME_IDPRESTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrestation.FIELDNAME_IDPRESTATION);

        // Jointure entre table prestation et bases d'indemnisation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);

        // Jointure entre table bases d'indemnisation et prononce
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);

        // Jointure entre table prononce et demande prestation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);

        // jointure avec Employeur
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJRepartitionPaiements.FIELDNAME_IDSITUATIONPROFESSIONNELLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJSituationProfessionnelle.FIELDNAME_ID_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJEmployeur.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJSituationProfessionnelle.FIELDNAME_ID_EMPLOYEUR);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJEmployeur.FIELDNAME_ID_EMPLOYEUR);

        return fromClauseBuffer.toString();
    }

    private String contenuAnnonce = "";
    private String dateDebutPrestation = "";
    private String fromClause = null;
    private String idDepartement = "";
    private String idExterne = "";
    private Boolean isImpotSource = null;

    private Boolean isIndependant = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String montantCotisation = "";

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
            fromClause = createFromClause(_getCollection());
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
        montantCotisation = statement.dbReadNumeric(IJCotisation.FIELDNAME_MONTANT);
        idExterne = statement.dbReadNumeric(IJCotisation.FIELDNAME_IDEXTERNE);
        contenuAnnonce = statement.dbReadNumeric(IJPrestation.FIELDNAME_CS_TYPE);
        dateDebutPrestation = statement.dbReadDateAMJ(IJPrestation.FIELDNAME_DATEDEBUT);
        idDepartement = statement.dbReadNumeric(IJEmployeur.FIELDNAME_ID_PARTICULARITE);
        isImpotSource = statement.dbReadBoolean(IJCotisation.FIELDNAME_ISIMPOTSOURCE);

        String idAssure = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        String statutProfessionnel = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_STATUT_PROFESSIONNEL);
        isIndependant = Boolean.FALSE;

        // On set le flag indépendant si : la statut de la situation
        // professionnel est independant ou non actif
        // et que le bénéficiaire de la prestation est l'assuré lui-même (même
        // id tiers)
        if (IIJPrononce.CS_INDEPENDANT.equals(statutProfessionnel)
                || IIJPrononce.CS_NON_ACTIF.equals(statutProfessionnel)) {

            if (idAssure != null && idAssure.equals(getIdTiers())) {
                isIndependant = Boolean.TRUE;
            }
        }
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
        if (isImpotSource.booleanValue()) {
            return IMPOT_SOURCE;
        } else {
            if (PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return AC;
            } else if (PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return AVS;
            } else if (PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return FRAIS_ADMINISTRATION;
            } else if (PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                    PRAffiliationHelper.TYPE_PARITAIRE).equals(idExterne)
                    || PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL).equals(idExterne)) {
                return LFA;
            } else {
                return "";
            }
        }
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

    /**
     * getter pour l'attribut montantCotisation
     * 
     * @return la valeur courante de l'attribut montantCotisation
     */
    public String getMontantCotisation() {
        return montantCotisation;
    }
}
