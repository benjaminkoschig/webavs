package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Objet de domaine représentant une compensation inter-décision dans les rentes
 */
public final class CompensationInterDecision extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneAVS beneficiaireCompensationInterDecision;
    private BigDecimal montantCompense;
    private OrdreVersement ordreVersementDecisionCompensee;
    private OrdreVersement ordreVersementDecisionPonctionnee;

    public CompensationInterDecision() {
        super();

        beneficiaireCompensationInterDecision = new PersonneAVS();
        montantCompense = BigDecimal.ZERO;
        ordreVersementDecisionCompensee = new OrdreVersement();
        ordreVersementDecisionPonctionnee = new OrdreVersement();
    }

    /**
     * @return le bénéficiaire de cette compensation inter-décision (à qui on versera le montant)
     */
    public PersonneAVS getBeneficiaireCompensationInterDecision() {
        return beneficiaireCompensationInterDecision;
    }

    /**
     * @return le montant transitant entre les décisions (pris sur la décision lié à l'ordre de versement
     *         {@link #getOrdreVersementDecisionPonctionnee()} et ajouté à la décision liée à l'ordre de versement
     *         {@link #getOrdreVersementDecisionCompensee()})
     */
    public BigDecimal getMontantCompense() {
        return montantCompense;
    }

    /**
     * @return l'ordre de versement lié à la décision qui recevra l'argent de la compensation
     */
    public OrdreVersement getOrdreVersementDecisionCompensee() {
        return ordreVersementDecisionCompensee;
    }

    /**
     * @return l'ordre de versement lié à la décision sur laquelle on retirera le montant pour la compensation
     */
    public OrdreVersement getOrdreVersementDecisionPonctionnee() {
        return ordreVersementDecisionPonctionnee;
    }

    /**
     * (re-)défini le bénéficiaire de cette compensation inter-décision (celui à qui on versera le montant)
     * 
     * @param beneficiaireCompensationInterDecision
     *            une personne ayant un numéro AVS (généralement le bénéficiaire principal de la décision sur laquelle
     *            sera versée à l'argent)
     */
    public void setBeneficiaireCompensationInterDecision(final PersonneAVS beneficiaireCompensationInterDecision) {
        Checkers.checkNotNull(beneficiaireCompensationInterDecision, "cid.beneficiaireCompensationInterDecision");
        this.beneficiaireCompensationInterDecision = beneficiaireCompensationInterDecision;
    }

    /**
     * (re-)défini le montant qui transitera entre les décisions ainsi que le montant de l'ordre de versement lié dans
     * la décision ponctionnée
     * 
     * @param montantCompense
     *            un montant positif
     * @throws NullPointerException
     *             si le montant est null
     * @throws IllegalArgumentException
     *             si le montant est négatif
     */
    public void setMontantCompense(final BigDecimal montantCompense) {
        Checkers.checkNotNull(montantCompense, "cid.montantCompense");
        Checkers.checkCantBeNegative(montantCompense, "cid.montantCompense");
        this.montantCompense = montantCompense;
        ordreVersementDecisionPonctionnee.setMontantCompense(montantCompense);
    }

    /**
     * <p>
     * (re-)défini l'ordre de versement lié à la décision qui recevra le montant de la compensation inter-décision
     * </p>
     * <p>
     * <strong>Attention!</strong> : cet ordre de versement ne doit pas avoir
     * {@link OrdreVersement#isCompensationInterDecision() le flag compensation inter-décision} à <code>true</code> et
     * ne pas être de type {@link TypeOrdreVersement#DETTE} pour que la validation de la décision se fasse correctement.
     * <p>
     * 
     * @param ordreVersementDecisionCompensee
     *            un ordre de versement (n'importe lequel) lié à la décision qui recevra le montant.
     * @throws NullPointerException
     *             si l'ordre de versement passé en paramètre est null
     * @throws IllegalArgumentException
     *             si l'ordre de versement est de type {@link TypeOrdreVersement#DETTE} ou s'il a le flag
     *             {@link OrdreVersement#isCompensationInterDecision()}
     */
    public void setOrdreVersementDecisionCompensee(final OrdreVersement ordreVersementDecisionCompensee) {
        Checkers.checkNotNull(ordreVersementDecisionCompensee, "ordreVersementDecisionCompense");
        if (ordreVersementDecisionCompensee.isCompensationInterDecision()) {
            throw new IllegalArgumentException(
                    "[cid.ordreVersementDecisionCompense] must not have the flag [isCompensationInterDecision()]");
        }
        if (!(ordreVersementDecisionCompensee.getType() == TypeOrdreVersement.DETTE)) {
            throw new IllegalArgumentException("[cid.ordreVersementDecisionCompense] must be a debt");
        }
        this.ordreVersementDecisionCompensee = ordreVersementDecisionCompensee;
    }

    /**
     * <p>
     * (re-)défini l'ordre de versement lié à la décision sur laquelle on ponctionne le montant de cette CID
     * </p>
     * <p>
     * <strong>Attention!</strong> : cet ordre de versement doit avoir le flag de compensation inter-décision à
     * <code>true</code> et doit avoir le type {@link TypeOrdreVersement#DETTE}, sinon la validation de la décision sera
     * erronée
     * </p>
     * 
     * @param ordreVersementDecisionPonctionnee
     *            un ordre de versement (n'importe lequel) lié la décision qui sera ponctionnée
     * @throws NullPointerException
     *             si l'ordre de versement passé en paramètre est null
     * @throws IllegalArgumentException
     *             si l'ordre de versement passé en paramètre n'a pas le flag
     *             {@link OrdreVersement#isCompensationInterDecision()} ou s'il n'est pas de type
     *             {@link TypeOrdreVersement#DETTE}
     */
    public void setOrdreVersementDecisionPonctionnee(final OrdreVersement ordreVersementDecisionPonctionnee) {
        Checkers.checkNotNull(ordreVersementDecisionPonctionnee, "ordreVersementDecisionPonctionnee");
        if (!ordreVersementDecisionPonctionnee.isCompensationInterDecision()) {
            throw new IllegalArgumentException(
                    "[cid.ordreVersementDecisionPonctionnee] must have the flag [isCompensationInterDecision()]");
        }
        if (!(ordreVersementDecisionPonctionnee.getType() == TypeOrdreVersement.DETTE)) {
            throw new IllegalArgumentException("[cid.ordreVersementDecisionPonctionnee] must be a debt");
        }
        this.ordreVersementDecisionPonctionnee = ordreVersementDecisionPonctionnee;
    }
}
