package ch.globaz.al.business.compensation;

import java.math.BigDecimal;

/**
 * Modèle métier contenant les informations nécessaire à l'impression d'une ligne sur le protocole de simulation de
 * compensation
 * 
 * @author jts
 * 
 */
public class CompensationBusinessModel implements Comparable<Object> {

    /**
     * Genre d'assurance
     * 
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    private String genreAssurance = null;

    /**
     * Numéro de la récap
     */
    private String idRecap = null;

    /**
     * Montant total de des prestation de la récap
     */
    private BigDecimal montant = null;
    /**
     * Nom de l'affilié auquel est lié la récap
     */
    private String nomAffilie = null;
    /**
     * Numéro de l'affilié
     */
    private String numeroAffilie = null;
    /**
     * Numéro de rubrique comptable
     */
    private String numeroCompte = null;
    /**
     * Numéro de facture de la récap
     */
    private String numeroFacture = null;
    /**
     * Fin de la période de la récap
     */
    private String periodeRecapA = null;
    /**
     * Début de la période de la récap
     */
    private String periodeRecapDe = null;

    /**
     * Constructeur
     * 
     * @param idRecap
     *            Numéro de la récap
     * @param nomAffilie
     *            Nom de l'affilié auquel est lié la récap
     * @param numeroAffilie
     *            Numéro de l'affilié
     * @param numeroFacture
     *            Numéro de facture
     * @param numeroCompte
     *            Numéro de rubrique comptable
     * @param periodeRecapDe
     *            Début de la période de la récap
     * @param periodeRecapA
     *            Fin de la période de la récap
     * @param genreAssurance
     *            genre d'assurance (indépendant ou paritaire)
     */
    public CompensationBusinessModel(String idRecap, String nomAffilie, String numeroAffilie, String numeroFacture,
            String numeroCompte, String periodeRecapDe, String periodeRecapA, String genreAssurance) {
        montant = new BigDecimal("0");
        this.idRecap = idRecap;
        this.nomAffilie = nomAffilie;
        this.numeroAffilie = numeroAffilie;
        this.numeroFacture = numeroFacture;
        this.numeroCompte = numeroCompte;
        this.periodeRecapDe = periodeRecapDe;
        this.periodeRecapA = periodeRecapA;
        this.genreAssurance = genreAssurance;
    }

    /**
     * Additionne le <code>montant</code> passé en paramètre au montant contenu dans l'objet
     * 
     * @param montant
     *            le montant à ajouter
     */
    public void addMontant(String montant) {
        this.montant = this.montant.add(new BigDecimal(montant));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {

        if (!(o instanceof CompensationBusinessModel)) {
            throw new IllegalArgumentException(
                    "CalculBusinessModel#compareTo : o is not an instance of CompensationBusinessModel");
        }

        return getNumeroAffilie().compareTo(((CompensationBusinessModel) o).getNumeroAffilie());
    }

    /**
     * @return the genreAssurance
     */
    public String getGenreAssurance() {
        return genreAssurance;
    }

    /**
     * @return the numeroRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return Montant total de des prestation de la récap
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return Nom de l'affilié auquel est lié la récap
     */
    public String getNomAffilie() {
        return nomAffilie;
    }

    /**
     * @return Numéro de l'affilié
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return the numeroCompte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * @return the numeroFacture
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * @return the periodeRecapA
     */
    public String getPeriodeRecapA() {
        return periodeRecapA;
    }

    /**
     * @return the periodeRecapDe
     */
    public String getPeriodeRecapDe() {
        return periodeRecapDe;
    }
}
