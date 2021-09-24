/*
 * CrÈÈ le 20 juin 05
 * 
 * Pour changer le modËle de ce fichier gÈnÈrÈ, allez ‡ : FenÍtre&gt;PrÈfÈrences&gt;Java&gt;GÈnÈration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.prestation;

import ch.globaz.common.util.Dates;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.utils.APGUtils;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APRepartitionJointPrestation extends APRepartitionPaiements implements Comparable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String PAIEMENT_ALLOC_FRAIS_GARDE = "AssurÈ (alloc. frais de garde)";
    public static final String PAIEMENT_ASSURE = "AssurÈ";
    public static final String PAIEMENT_ASSURE_IMDEPENDANT = "AssurÈ (indÈpendant)";
    public static final String PAIEMENT_EMPLOYEUR = "Employeur";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String createFromClause(final String schema) {
        final StringBuffer buffer = new StringBuffer();

        buffer.append(schema);
        buffer.append(APRepartitionPaiements.TABLE_NAME);

        // jointure avec la table des prestations
        buffer.append(" INNER JOIN ");
        buffer.append(schema);
        buffer.append(APPrestation.TABLE_NAME);
        buffer.append(" ON ");
        buffer.append(schema);
        buffer.append(APRepartitionPaiements.TABLE_NAME);
        buffer.append(".");
        buffer.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        buffer.append("=");
        buffer.append(schema);
        buffer.append(APPrestation.TABLE_NAME);
        buffer.append(".");
        buffer.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);

        // jointure avec la table des droits
        buffer.append(" INNER JOIN ");
        buffer.append(schema);
        buffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        buffer.append(" ON ");
        buffer.append(schema);
        buffer.append(APPrestation.TABLE_NAME);
        buffer.append(".");
        buffer.append(APPrestation.FIELDNAME_IDDROIT);
        buffer.append("=");
        buffer.append(schema);
        buffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        buffer.append(".");
        buffer.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        return buffer.toString();
    }

    private String cleTri = "";
    private String dateDebut = "";
    private String dateFin = "";
    private transient String fromClause = null;
    private String genrePrestationPrestation = "";
    private String genreService = "";
    private String idDroit = "";
    private String montantJournalier = "";
    private String nbJoursSoldes = "";
    private String remarque = "";
    private String contenuAnnonce = "";
    private String revenuMoyenDeterminant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typePrestationPrestation = "";

    /**
     * faux
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(final BStatement statement) {
        if (fromClause == null) {
            fromClause = APRepartitionJointPrestation.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return super._getTableName();
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        super._readProperties(statement);
        dateDebut = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEFIN);
        idDroit = statement.dbReadNumeric(APPrestation.FIELDNAME_IDDROIT);
        nbJoursSoldes = statement.dbReadNumeric(APPrestation.FIELDNAME_NOMBREJOURSSOLDES);
        montantJournalier = statement.dbReadNumeric(APPrestation.FIELDNAME_MONTANT_JOURNALIER);
        genreService = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_GENRESERVICE);
        remarque = statement.dbReadString(APPrestation.FIELDNAME_REMARQUE);
        genrePrestationPrestation = statement.dbReadNumeric(APPrestation.FIELDNAME_GENRE_PRESTATION);
        typePrestationPrestation = statement.dbReadNumeric(APPrestation.FIELDNAME_TYPE);
        contenuAnnonce = statement.dbReadNumeric(APPrestation.FIELDNAME_CONTENUANNONCE);
        revenuMoyenDeterminant = statement.dbReadNumeric(APPrestation.FIELDNAME_REVENUMOYENDETERMINANT);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(final BStatement statement) throws Exception {
        super._validate(statement);
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT, this._dbWriteNumeric(
                statement.getTransaction(), getIdRepartitionBeneficiairePaiement(), "idRepartition"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        super._writeProperties(statement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object arg0) {
        try {
            return getCleTri().compareTo(((APRepartitionJointPrestation) arg0).getCleTri());
        } catch (final Exception e) {
            return -1;
        }
    }

    /**
     * @return
     */
    public String getCleTri() throws Exception {
        if (JadeStringUtil.isEmpty(cleTri)) {

            final APDroitLAPG droit = APGUtils.loadDroit(getSession(), getIdDroit(), getGenreService());
            final PRDemande demande = droit.loadDemande();
            final PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), demande.getIdTiers());

            cleTri = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

            final String accent = "¿¡¬√ƒ≈‡·‚„‰Â“”‘’÷ÿÚÛÙıˆ¯»… ÀËÈÍÎÃÕŒœÏÌÓÔŸ⁄€‹˘˙˚¸ˇ—Ò«Á";
            final String sansAccent = "AAAAAAaaaaaaOOOOOOooooooEEEEeeeeIIIIiiiiUUUUuuuuyNnCc";

            // Conversion des chaines en tableaux de caractËres
            final char[] tableauSansAccent = sansAccent.toCharArray();
            final char[] tableauAccent = accent.toCharArray();

            // Pour chaque accent
            for (int i = 0; i < accent.length(); i++) {
                // Remplacement de l'accent par son Èquivalent sans accent dans
                // la chaÓne de caractËres
                cleTri = cleTri.replace(tableauAccent[i], tableauSansAccent[i]);
            }

            cleTri += getIdRepartitionBeneficiairePaiement();
        }
        return cleTri;

    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut genre prestation
     * 
     * @return la valeur courante de l'attribut genre prestation
     */
    public String getGenrePrestationPrestation() {
        return genrePrestationPrestation;
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
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    @Override
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    @Override
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut montant journalier
     * 
     * @return la valeur courante de l'attribut montant journalier
     */
    public String getMontantJournalier() {
        return montantJournalier;
    }

    /**
     * getter pour l'attribut nb jours soldes
     * retourne le nb jour soldÈ de la rÈpartition sinon celui de la prestation
     * 
     * @return la valeur courante de l'attribut nb jours soldes
     */
    public String getNbJoursSoldes() {
        return !JadeStringUtil.isBlankOrZero(getNombreJoursSoldes()) ? getNombreJoursSoldes() : nbJoursSoldes;
    }

    public String getPaiementPourLibelle() {

        // c'est un paiement pour les alloc. frais de garde
        if (IAPRepartitionPaiements.CS_FRAIS_GARDE.equals(getTypePrestation())) {
            return APRepartitionJointPrestation.PAIEMENT_ALLOC_FRAIS_GARDE;
        } else {

            if (IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR.equals(getTypePaiement())) {
                // un paiement employeur
                return APRepartitionJointPrestation.PAIEMENT_EMPLOYEUR;
            } else {

                // si un sit. prof. c'est un paiement a un independant
                if (!JadeStringUtil.isIntegerEmpty(getIdSituationProfessionnelle())) {
                    return APRepartitionJointPrestation.PAIEMENT_ASSURE_IMDEPENDANT;
                } else {
                    return APRepartitionJointPrestation.PAIEMENT_ASSURE;
                }
            }
        }
    }

    /**
     * @return
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * getter pour l'attribut type prestation prestation
     * 
     * @return la valeur courante de l'attribut type prestation prestation
     */
    public String getTypePrestationPrestation() {
        return typePrestationPrestation;
    }

    /**
     * vrai si la repartition est pour une prestation d'un droit mat.
     * 
     * @return la valeur courante de l'attribut maternite
     */
    public boolean isMaternite() {
        return IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService);
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(final String string) {
        dateDebut = string;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(final String string) {
        dateFin = string;
    }

    /**
     * setter pour l'attribut genre prestation
     * 
     * @param genrePrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenrePrestationPrestation(final String genrePrestation) {
        genrePrestationPrestation = genrePrestation;
    }

    /**
     * setter pour l'attribut genre service
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(final String string) {
        genreService = string;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param idAffilie
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setIdAffilie(final String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(final String string) {
        idDroit = string;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut montant journalier
     * 
     * @param montantJournalier
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalier(final String montantJournalier) {
        this.montantJournalier = montantJournalier;
    }

    /**
     * setter pour l'attribut nb jours soldes
     * 
     * @param nbJoursSoldes
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbJoursSoldes(final String nbJoursSoldes) {
        this.nbJoursSoldes = nbJoursSoldes;
    }

    /**
     * @param string
     */
    public void setRemarque(final String string) {
        remarque = string;
    }

    /**
     * setter pour l'attribut type prestation prestation
     * 
     * @param typePrestationPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePrestationPrestation(final String typePrestationPrestation) {
        this.typePrestationPrestation = typePrestationPrestation;
    }

    /**
     * @return the contenuAnnonce
     */
    public final String getContenuAnnonce() {
        return contenuAnnonce;
    }

    public static Comparator<APRepartitionJointPrestation> comparatorByDateDebut() {
        return Comparator.comparing(o1 -> Dates.toDate(o1.getDateDebut()));
    }

    public String getRevenuMoyenDeterminant() {
        return revenuMoyenDeterminant;
    }

    public void setRevenuMoyenDeterminant(String revenuMoyenDeterminant) {
        this.revenuMoyenDeterminant = revenuMoyenDeterminant;
    }
}
