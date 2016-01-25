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
 * Donn�es propres aux rentes concernant les prestations accord�es
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
     * Ajoute un code cas sp�cial � cette rente accord�e
     * 
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @throws NullPointerException
     *             si le code cas sp�cial pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si la rente comporte d�j� 5 codes cas sp�ciaux
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
     * Ajoute une prestation due � cette rente accord�e
     * </p>
     * <p>
     * Les r�gles suivantes doivent �tre respect�es :
     * <ul>
     * <li>Une seule prestation due de type {@link TypePrestationDue#MONTANT_RETROACTIF_TOTAL montant total} est
     * possible par rente accord�e
     * <li>Il ne peut pas y avoir plusieurs prestation due de type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement
     * mensuel} dans le m�me mois
     * <li>Il ne peut pas y avoir plusieurs prestation due de type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement
     * mensuel} sans mois de fin
     * </ul>
     * </p>
     * 
     * @param unePrestationDue
     *            une prestation due
     * @throws NullPointerException
     *             si la prestation due pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si une r�gle m�tier cit�e plus haut n'est pas respect�e
     */
    public void ajouterPrestationDue(final PrestationDue unePrestationDue) {
        Checkers.checkNotNull(unePrestationDue, "unePrestationDue");
        Checkers.checkPeriodMonthYear(unePrestationDue.getPeriode(), "prestationDue.periode");
        prestationsDues.add(unePrestationDue);
    }

    /**
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @return vrai si le code cas sp�cial est pr�sent dans cette rente accord�e
     * @throws NullPointerException
     *             si le code cas sp�cial pass� en param�tre est null
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
     *             si la demande pass�e en param�tre est null
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
     * @return vrai si cette rente accord�e est une rente compl�mentaire pour la famille du requ�rant de la demande de
     *         rente
     */
    public final boolean estUneRenteComplementaire() {
        return getCodePrestation().isRenteComplementaire();
    }

    /**
     * @return vrai si cette rente accord�e est une rente compl�mentaire pour le conjoint du requ�rant de la demande de
     *         rente
     */
    public final boolean estUneRenteComplementairePourConjoint() {
        return getCodePrestation().isRenteComplementairePourConjoint();
    }

    /**
     * @return vrai si cette rente accord�e est une rente compl�mentaire pour un enfant du requ�rant de la demande de
     *         rente
     */
    public final boolean estUneRenteComplementairePourEnfant() {
        return getCodePrestation().isRenteComplementairePourConjoint();
    }

    /**
     * @return vrai si cette rente accord�e est une rente principale
     */
    public final boolean estUneRentePrincipale() {
        return getCodePrestation().isRentePrincipale();
    }

    /**
     * <p>
     * Permet de d�finir si la rente pass�e en param�tre fait parti du m�me groupe de prestation (AVS/AI ou API), et
     * dans le cas d'une compl�mentaire pour enfant si le donneur de droit est le m�me, afin de determiner si la rente
     * doit �tre prise en comte pour diff�rent traitement m�tier (calcul de rente vers�e � tort, d�finition des rentes �
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
     * @return vrai si la prestation accord�e fait partie de la demande pass�e en param�tre
     * @throws NullPointerException
     *             si la demande pass�e en param�tre est null
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
     * @return les codes cas sp�ciaux de cette rente accord�e, ou une liste vide s'il n'y en a pas. Cette liste est
     *         immuable.
     */
    public final Set<CodeCasSpecialRente> getCodesCasSpeciaux() {
        return Collections.unmodifiableSet(codesCasSpeciaux);
    }

    /**
     * @return le montant r�troactif qui a �t� pay� lors de la validation de cette rente accord�e (tir� des prestations
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
     * (re-)d�fini la base de calcul ayant permis de d�finir cette rente accord�e
     * 
     * @param baseCalcul
     *            une base de calcul
     * @throws NullPointerException
     *             si la base de calcul pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la base de calcul pass�e en param�tre n'est pas initialis�e
     */
    public void setBaseCalcul(final BaseCalcul baseCalcul) {
        Checkers.checkNotNull(baseCalcul, "renteAccordee.baseCalcul");
        Checkers.checkHasID(baseCalcul, "renteAccordee.baseCalcul");
        this.baseCalcul = baseCalcul;
    }

    /**
     * (re-)d�fini tous les codes cas sp�ciaux de cette rente
     * 
     * @param codesCasSpeciaux
     *            une liste non null, avec maximum 5 code cas sp�ciaux
     * @throws NullPointerException
     *             si la liste pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la liste pass�e en param�tre a plus de 5 �l�ments
     */
    public final void setCodesCasSpeciaux(final Set<CodeCasSpecialRente> codesCasSpeciaux) {
        Checkers.checkNotNull(codesCasSpeciaux, "renteAccordee.codesCasSpeciaux");
        if (codesCasSpeciaux.size() > 5) {
            throw new IllegalArgumentException("[renteAccordee.codesCasSpeciaux] can't have more thant 5 elements");
        }
        this.codesCasSpeciaux = new HashSet<CodeCasSpecialRente>(codesCasSpeciaux);
    }

    /**
     * (re-)d�fini les prestations dues de cette rente accord�e
     * 
     * @param prestationsDues
     *            une liste de prestation due
     * @throws NullPointerException
     *             si la liste pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la liste pass�e en param�tre est vide
     */
    public void setPrestationsDues(final Set<PrestationDue> prestationsDues) {
        Checkers.checkNotNull(prestationsDues, "renteAccordee.prestationsDues");
        Checkers.checkNotEmpty(prestationsDues, "renteAccordee.prestationsDues");
        this.prestationsDues = prestationsDues;
    }

    /**
     * Supprime un code cas sp�cial � cette rente accord�e. Si le code cas sp�cial pass� en param�tre n'est pas pr�sent,
     * rien ne sera fait.
     * 
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @throws NullPointerException
     *             si le code cas sp�cial pass� en param�tre est null
     */
    public final void supprimerCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "renteAccordee.codeCasSpecial");
        codesCasSpeciaux.remove(codeCasSpecial);
    }
}
