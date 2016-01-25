/*
 * Créé le 2 août 07
 */
package globaz.corvus.db.annonces;

import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * Utilise pour la generation de la liste des annonces
 * 
 * @author BSC
 * 
 */
public class REAnnoncesGroupePrestations extends REAnnonceHeader {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String FIELDNAME_GROUPE_PRESTATION = " GRPRST ";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeMutation = "";
    private String debutDroit = "";
    private String finDroit = "";
    private String genrePrestation = "";
    private String groupePrestation = "";
    private String idTiers = "";
    private String mensualitePrestationsFrancs = "";

    private String moisRapport = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += super._getFrom(statement);

        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A;

        return getFrom;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A;
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

        idTiers = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_ID_TIERS);
        moisRapport = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT));
        finDroit = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_FIN_DROIT);
        genrePrestation = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION);
        debutDroit = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_DEBUT_DROIT);
        mensualitePrestationsFrancs = statement
                .dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_MENSUALITE_PRESTATIONS_FR);
        codeMutation = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION);
        groupePrestation = statement.dbReadNumeric(FIELDNAME_GROUPE_PRESTATION);
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
    }

    /**
     * @return
     */
    public String getCodeMutation() {
        return codeMutation;
    }

    /**
     * @return
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * @return
     */
    public String getFinDroit() {
        return finDroit;
    }

    /**
     * @return
     */
    public String getGenrePrestation() {
        return genrePrestation;
    }

    /**
     * @return
     */
    public String getGroupePrestation() {
        return groupePrestation;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getMensualitePrestationsFrancs() {
        return mensualitePrestationsFrancs;
    }

    /**
     * @return
     */
    public String getMoisRapport() {
        return moisRapport;
    }

}
