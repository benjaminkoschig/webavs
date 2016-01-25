package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;
import ch.globaz.prestation.domaine.PrestationAccordee;

/**
 * Données propres aux rentes concernant les prestations accordées
 */
public class RenteAccordee extends PrestationAccordee {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BaseCalcul baseCalcul;
    private Set<CodeCasSpecialRente> codesCasSpeciaux;
    private Set<PrestationDue> prestationsDues;

    public RenteAccordee() {
        super();

        baseCalcul = new BaseCalcul();
        codesCasSpeciaux = new HashSet<CodeCasSpecialRente>(5);
        prestationsDues = new HashSet<PrestationDue>();
    }

    /**
     * Ajoute un code cas spécial à cette rente accordée
     * 
     * @param codeCasSpecial
     *            un code cas spécial
     * @throws NullPointerException
     *             si le code cas spécial passé en paramètre est null
     * @throws IllegalArgumentException
     *             si la rente comporte déjà 5 codes cas spéciaux
     */
    public final void ajouterCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "codeCasSpecial");
        if (codesCasSpeciaux.size() == 5) {
            throw new IllegalArgumentException("[renteAccordee.codesCasSpeciaux] can't have more thant 5 elements");
        }
        codesCasSpeciaux.add(codeCasSpecial);
    }

    /**
     * <p>
     * Ajoute une prestation due à cette rente accordée
     * </p>
     * <p>
     * Les règles suivantes doivent être respectées :
     * <ul>
     * <li>Une seule prestation due de type {@link TypePrestationDue#MONTANT_RETROACTIF_TOTAL montant total} est
     * possible par rente accordée
     * <li>Il ne peut pas y avoir plusieurs prestation due de type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement
     * mensuel} dans le même mois
     * <li>Il ne peut pas y avoir plusieurs prestation due de type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement
     * mensuel} sans mois de fin
     * </ul>
     * </p>
     * 
     * @param unePrestationDue
     *            une prestation due
     * @throws NullPointerException
     *             si la prestation due passé en paramètre est null
     * @throws IllegalArgumentException
     *             si une règle métier citée plus haut n'est pas respectée
     */
    public void ajouterPrestationDue(final PrestationDue unePrestationDue) {
        Checkers.checkNotNull(unePrestationDue, "unePrestationDue");
        Checkers.checkPeriodMonthYear(unePrestationDue.getPeriode(), "prestationDue.periode");
        prestationsDues.add(unePrestationDue);
    }

    /**
     * @param codeCasSpecial
     *            un code cas spécial
     * @return vrai si le code cas spécial est présent dans cette rente accordée
     * @throws NullPointerException
     *             si le code cas spécial passé en paramètre est null
     */
    public final boolean comporteCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "codeCasSpecial");
        return codesCasSpeciaux.contains(codeCasSpecial);
    }

    /**
     * @param uneDemande
     *            une demande de rente
     * @return vrai si le type de la demande (vieillesse, survivant, AI, API) correspond au genre de cette rente
     * @throws NullPointerException
     *             si la demande passée en paramètre est null
     */
    public boolean correspondAuTypeDeLaDemande(final DemandeRente uneDemande) {
        Checkers.checkNotNull(uneDemande, "uneDemande");

        boolean match = false;

        switch (uneDemande.getTypeDemandeRente()) {
            case DEMANDE_API:
                match = getCodePrestation().isAPI();
                break;

            case DEMANDE_INVALIDITE:
                match = getCodePrestation().isAI();
                break;

            case DEMANDE_SURVIVANT:
                match = getCodePrestation().isSurvivant();
                break;

            case DEMANDE_VIEILLESSE:
                match = getCodePrestation().isVieillesse();
                break;

        }
        return match;
    }

    /**
     * @return vrai si cette rente accordée est une rente complémentaire pour la famille du requérant de la demande de
     *         rente
     */
    public final boolean estUneRenteComplementaire() {
        return getCodePrestation().isRenteComplementaire();
    }

    /**
     * @return vrai si cette rente accordée est une rente complémentaire pour le conjoint du requérant de la demande de
     *         rente
     */
    public final boolean estUneRenteComplementairePourConjoint() {
        return getCodePrestation().isRenteComplementairePourConjoint();
    }

    /**
     * @return vrai si cette rente accordée est une rente complémentaire pour un enfant du requérant de la demande de
     *         rente
     */
    public final boolean estUneRenteComplementairePourEnfant() {
        return getCodePrestation().isRenteComplementairePourConjoint();
    }

    /**
     * @return vrai si cette rente accordée est une rente principale
     */
    public final boolean estUneRentePrincipale() {
        return getCodePrestation().isRentePrincipale();
    }

    /**
     * <p>
     * Permet de définir si la rente passée en paramètre fait parti du même groupe de prestation (AVS/AI ou API), et
     * dans le cas d'une complémentaire pour enfant si le donneur de droit est le même, afin de determiner si la rente
     * doit être prise en comte pour différent traitement métier (calcul de rente versée à tort, définition des rentes à
     * diminuer lors de la validation d'une demande de rente, etc...)
     * </p>
     * 
     * @param uneAutreRente
     * @return
     */
    public boolean estDeLaMemeFamilleDePrestationQue(RenteAccordee uneAutreRente) {
        Checkers.checkNotNull(uneAutreRente, "uneAutreRente");
        return getCodePrestation().estDeLaMemeFamilleDePrestationQue(uneAutreRente.getCodePrestation());
    }

    /**
     * @param uneDemande
     *            une demande
     * @return vrai si la prestation accordée fait partie de la demande passée en paramètre
     * @throws NullPointerException
     *             si la demande passée en paramètre est null
     */
    public boolean faitPartieDeCetteDemande(final DemandeRente uneDemande) {
        Checkers.checkNotNull(uneDemande, "uneDemande");
        for (RenteAccordee uneRenteAccordee : uneDemande.getRentesAccordees()) {
            if (equals(uneRenteAccordee)) {
                return true;
            }
        }
        return false;
    }

    public BaseCalcul getBaseCalcul() {
        return baseCalcul;
    }

    /**
     * @return les codes cas spéciaux de cette rente accordée, ou une liste vide s'il n'y en a pas. Cette liste est
     *         immuable.
     */
    public final Set<CodeCasSpecialRente> getCodesCasSpeciaux() {
        return Collections.unmodifiableSet(codesCasSpeciaux);
    }

    /**
     * @return le montant rétroactif qui a été payé lors de la validation de cette rente accordée (tiré des prestations
     *         dues)
     */
    public BigDecimal getMontantRetroactif() {
        BigDecimal montantRetroactif = BigDecimal.ZERO;

        for (PrestationDue prestationDue : prestationsDues) {
            if (prestationDue.getType() == TypePrestationDue.MONTANT_RETROACTIF_TOTAL) {
                montantRetroactif = montantRetroactif.add(prestationDue.getMontant());
            }
        }

        return montantRetroactif;
    }

    /**
     * @return les prestations dues de cette rente dans un conteneur invariable
     */
    public Set<PrestationDue> getPrestationsDues() {
        return Collections.unmodifiableSet(prestationsDues);
    }

    /**
     * (re-)défini la base de calcul ayant permis de définir cette rente accordée
     * 
     * @param baseCalcul
     *            une base de calcul
     * @throws NullPointerException
     *             si la base de calcul passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la base de calcul passée en paramètre n'est pas initialisée
     */
    public void setBaseCalcul(final BaseCalcul baseCalcul) {
        Checkers.checkNotNull(baseCalcul, "renteAccordee.baseCalcul");
        Checkers.checkHasID(baseCalcul, "renteAccordee.baseCalcul");
        this.baseCalcul = baseCalcul;
    }

    /**
     * (re-)défini tous les codes cas spéciaux de cette rente
     * 
     * @param codesCasSpeciaux
     *            une liste non null, avec maximum 5 code cas spéciaux
     * @throws NullPointerException
     *             si la liste passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la liste passée en paramètre a plus de 5 éléments
     */
    public final void setCodesCasSpeciaux(final Set<CodeCasSpecialRente> codesCasSpeciaux) {
        Checkers.checkNotNull(codesCasSpeciaux, "renteAccordee.codesCasSpeciaux");
        if (codesCasSpeciaux.size() > 5) {
            throw new IllegalArgumentException("[renteAccordee.codesCasSpeciaux] can't have more thant 5 elements");
        }
        this.codesCasSpeciaux = new HashSet<CodeCasSpecialRente>(codesCasSpeciaux);
    }

    /**
     * (re-)défini les prestations dues de cette rente accordée
     * 
     * @param prestationsDues
     *            une liste de prestation due
     * @throws NullPointerException
     *             si la liste passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la liste passée en paramètre est vide
     */
    public void setPrestationsDues(final Set<PrestationDue> prestationsDues) {
        Checkers.checkNotNull(prestationsDues, "renteAccordee.prestationsDues");
        Checkers.checkNotEmpty(prestationsDues, "renteAccordee.prestationsDues");
        this.prestationsDues = prestationsDues;
    }

    /**
     * Supprime un code cas spécial à cette rente accordée. Si le code cas spécial passé en paramètre n'est pas présent,
     * rien ne sera fait.
     * 
     * @param codeCasSpecial
     *            un code cas spécial
     * @throws NullPointerException
     *             si le code cas spécial passé en paramètre est null
     */
    public final void supprimerCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "renteAccordee.codeCasSpecial");
        codesCasSpeciaux.remove(codeCasSpecial);
    }
}
