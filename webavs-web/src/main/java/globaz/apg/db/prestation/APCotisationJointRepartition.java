/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCotisationJointRepartition extends APCotisation {

    private static final long serialVersionUID = -1703821807679179390L;

    private static final String FIELDNAME_TI_IDTIERS = "HTITIE";

    private static final String FIELDNAME_TI_SEXE = "HPTSEX";
    private static final String TABLE_TIERS_PERSONNE = "TIPERSP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFieldsClause(final String schema) {
        StringBuffer fields = new StringBuffer(APCotisation.createFieldsClause(schema));

        fields.append(",");
        fields.append(APRepartitionPaiements.FIELDNAME_NOM);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_MONTANTBRUT);
        fields.append(",");
        fields.append(APRepartitionPaiements.FIELDNAME_MONTANTBRUT);
        fields.append(",");
        fields.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);

        fields.append(",");
        fields.append(FIELDNAME_TI_SEXE);

        fields.append(",");
        fields.append(APDroitLAPG.FIELDNAME_GENRESERVICE);

        fields.append(",");
        fields.append(APDroitLAPG.FIELDNAME_ETAT);

        return fields.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(final String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(APCotisation.TABLE_NAME);

        // jointure avec la table des repartitions de paiement
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(APRepartitionPaiements.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        fromClause.append("=");
        fromClause.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);

        // jointure avec la table des prestations
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(APPrestation.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        fromClause.append("=");
        fromClause.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);

        // jointure avec la table des droits
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(" ON ");
        fromClause.append(APPrestation.FIELDNAME_IDDROIT);
        fromClause.append("=");
        fromClause.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // jointure avec la table des tiers personnes
        fromClause.append(" LEFT JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_TIERS_PERSONNE);
        fromClause.append(" ON ");
        fromClause.append(APRepartitionPaiements.FIELDNAME_IDTIERS);
        fromClause.append("=");
        fromClause.append(FIELDNAME_TI_IDTIERS);

        return fromClause.toString();
    }

    private String genreService = "";
    private String idPrestationApg = "";
    private String montantBrut = "";
    private String montantBrutCotisation = "";
    private String etatDroit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nom = "";

    private String sexe = "";

    /**
     * retourne faux
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return createFieldsClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        sexe = statement.dbReadNumeric(FIELDNAME_TI_SEXE);
        nom = statement.dbReadString(APRepartitionPaiements.FIELDNAME_NOM);
        montantBrutCotisation = statement.dbReadNumeric(APCotisation.FIELDNAME_MONTANTBRUT, 2);
        montantBrut = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTBRUT, 2);
        idPrestationApg = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        etatDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_ETAT);
    }

    public String getEtatDroit() {
        return etatDroit;
    }

    /**
     * getter pour l'attribut genre service
     * 
     * @return la valeur courante de l'attribut genre service
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * getter pour l'attribut id prestation apg
     * 
     * @return la valeur courante de l'attribut id prestation apg
     */
    public String getIdPrestationApg() {
        return idPrestationApg;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    @Override
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * @return
     */
    public String getMontantBrutCotisation() {
        return montantBrutCotisation;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut sexe
     * 
     * @return la valeur courante de l'attribut sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * setter pour l'attribut genre service
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String string) {
        genreService = string;
    }

    /**
     * setter pour l'attribut id prestation apg
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestationApg(String string) {
        idPrestationApg = string;
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantBrut(String string) {
        montantBrut = string;
    }

    /**
     * @param string
     */
    public void setMontantBrutCotisation(String string) {
        montantBrutCotisation = string;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut sexe
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSexe(String string) {
        sexe = string;
    }

}
