package globaz.corvus.vb.ordresversements;

import java.math.BigDecimal;

/**
 * <p>
 * Conteneur utilis� dans l'�cran des ordres de versement pour encapsuler une d�cision sur laquelle on peut cr�er une
 * compensation inter-d�cision.
 * </p>
 * <p>
 * Le solde de cette d�cision sera donc obligatoirement positif, car sinon la compensation en ponctionnant cette
 * d�cision ne serait pas possible
 * </p>
 */
public class REDecisionPourCompensationInterDecision {

    private final Long idDecision;
    private final String nomBeneficiaire;
    private final String prenomBeneficiaire;
    private final BigDecimal solde;

    /**
     * Cette objet est immuable, de ce fait tous les param�tres doivent �tre consistants (non null)
     * 
     * @param idDecision
     *            l'id de la d�cision sur laquelle on peut ponctionner un montant
     * @param solde
     *            le solde (obligatoirement positif) de la d�cision
     * @param nomBeneficiaire
     *            le nom du b�n�ficiaire principal de la d�cision
     * @param prenomBeneficiaire
     *            le pr�nom du b�n�ficiaire principal de la d�cision
     */
    public REDecisionPourCompensationInterDecision(Long idDecision, BigDecimal solde, String nomBeneficiaire,
            String prenomBeneficiaire) {
        checkNotNull(idDecision, "[idDecision] can't be null");
        checkNotNull(solde, "[solde] can't be null");
        checkNotNull(nomBeneficiaire, "[nomBeneficiaire] can't be null");
        checkNotNull(prenomBeneficiaire, "[prenomBeneficiaire] can't be null");

        if (solde.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("[solde] must be above zero");
        }

        this.idDecision = idDecision;
        this.solde = solde;
        this.nomBeneficiaire = nomBeneficiaire;
        this.prenomBeneficiaire = prenomBeneficiaire;
    }

    private void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @return l'id de la d�cision sur laquelle il est possible de compenser
     */
    public final Long getIdDecision() {
        return idDecision;
    }

    /**
     * @return le nom du b�n�ficiaire principal de cette d�cision
     */
    public final String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    /**
     * @return le pr�nom du b�n�ficiaire principal de cette d�cision
     */
    public final String getPrenomBeneficiaire() {
        return prenomBeneficiaire;
    }

    /**
     * @return le solde (toujours positif) de cette d�cision
     */
    public final BigDecimal getSolde() {
        return new BigDecimal(solde.toString());
    }
}
