package ch.globaz.al.business.compensation;

import java.math.BigDecimal;

/**
 * Mod�le m�tier contenant les informations n�cessaire � l'impression d'une ligne sur le protocole de simulation de
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
     * Num�ro de la r�cap
     */
    private String idRecap = null;

    /**
     * Montant total de des prestation de la r�cap
     */
    private BigDecimal montant = null;
    /**
     * Nom de l'affili� auquel est li� la r�cap
     */
    private String nomAffilie = null;
    /**
     * Num�ro de l'affili�
     */
    private String numeroAffilie = null;
    /**
     * Num�ro de rubrique comptable
     */
    private String numeroCompte = null;
    /**
     * Num�ro de facture de la r�cap
     */
    private String numeroFacture = null;
    /**
     * Fin de la p�riode de la r�cap
     */
    private String periodeRecapA = null;
    /**
     * D�but de la p�riode de la r�cap
     */
    private String periodeRecapDe = null;

    /**
     * Constructeur
     * 
     * @param idRecap
     *            Num�ro de la r�cap
     * @param nomAffilie
     *            Nom de l'affili� auquel est li� la r�cap
     * @param numeroAffilie
     *            Num�ro de l'affili�
     * @param numeroFacture
     *            Num�ro de facture
     * @param numeroCompte
     *            Num�ro de rubrique comptable
     * @param periodeRecapDe
     *            D�but de la p�riode de la r�cap
     * @param periodeRecapA
     *            Fin de la p�riode de la r�cap
     * @param genreAssurance
     *            genre d'assurance (ind�pendant ou paritaire)
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
     * Additionne le <code>montant</code> pass� en param�tre au montant contenu dans l'objet
     * 
     * @param montant
     *            le montant � ajouter
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
     * @return Montant total de des prestation de la r�cap
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return Nom de l'affili� auquel est li� la r�cap
     */
    public String getNomAffilie() {
        return nomAffilie;
    }

    /**
     * @return Num�ro de l'affili�
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
