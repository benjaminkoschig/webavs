package globaz.corvus.vb.ordresversements;

import java.math.BigDecimal;

/**
 * <p>
 * Conteneur utilisé dans l'écran des ordres de versement pour encapsuler une décision sur laquelle on peut créer une
 * compensation inter-décision.
 * </p>
 * <p>
 * Le solde de cette décision sera donc obligatoirement positif, car sinon la compensation en ponctionnant cette
 * décision ne serait pas possible
 * </p>
 */
public class REDecisionPourCompensationInterDecision {

    private final Long idDecision;
    private final String nomBeneficiaire;
    private final String prenomBeneficiaire;
    private final BigDecimal solde;

    /**
     * Cette objet est immuable, de ce fait tous les paramètres doivent être consistants (non null)
     * 
     * @param idDecision
     *            l'id de la décision sur laquelle on peut ponctionner un montant
     * @param solde
     *            le solde (obligatoirement positif) de la décision
     * @param nomBeneficiaire
     *            le nom du bénéficiaire principal de la décision
     * @param prenomBeneficiaire
     *            le prénom du bénéficiaire principal de la décision
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
     * @return l'id de la décision sur laquelle il est possible de compenser
     */
    public final Long getIdDecision() {
        return idDecision;
    }

    /**
     * @return le nom du bénéficiaire principal de cette décision
     */
    public final String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    /**
     * @return le prénom du bénéficiaire principal de cette décision
     */
    public final String getPrenomBeneficiaire() {
        return prenomBeneficiaire;
    }

    /**
     * @return le solde (toujours positif) de cette décision
     */
    public final BigDecimal getSolde() {
        return new BigDecimal(solde.toString());
    }
}
