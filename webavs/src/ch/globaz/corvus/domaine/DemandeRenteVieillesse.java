package ch.globaz.corvus.domaine;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.constantes.Anticipation;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Demande de rente de vieillesse
 */
public class DemandeRenteVieillesse extends DemandeRente {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Anticipation anticipation;
    private boolean avecAjournement;
    private String dateRevocationAjournement;

    public DemandeRenteVieillesse() {
        super();

        anticipation = Anticipation.AGE_LEGAL;
        avecAjournement = false;
        dateRevocationAjournement = "";

        setTypeDemandeRente(TypeDemandeRente.DEMANDE_VIEILLESSE);
    }

    /**
     * @return vrai si un ajournement est en cours, ou a eu lieu
     */
    public final boolean avecAjournement() {
        return isAvecAjournement();
    }

    @Override
    public Set<CodePrestation> codesPrestationsAcceptesPourCeTypeDeDemande() {
        Set<CodePrestation> codesPrestationsAcceptes = new HashSet<CodePrestation>();

        for (CodePrestation unCodePrestation : CodePrestation.values()) {
            if (unCodePrestation.isVieillesse()) {
                codesPrestationsAcceptes.add(unCodePrestation);
            }
        }

        return codesPrestationsAcceptes;
    }

    /**
     * @return le type d'anticipation de cette demande de vieillesse. S'il n'y a pas eu d'anticipation, retournera
     *         {@link Anticipation#AGE_LEGAL}
     */
    public final Anticipation getAnticipation() {
        return anticipation;
    }

    /**
     * @return la date de révocation de l'ajournement de cette demande de vieillesse (format JJ.MM.AAAA, ou vide s'il
     *         n'y pas eu d'ajournement ou si la révocation de celui-ci n'a pas encore eue lieu)
     */
    public final String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    /**
     * @return vrai si un ajournement est en cours, ou a eu lieu
     */
    public final boolean isAvecAjournement() {
        return avecAjournement;
    }

    /**
     * (re-)défini le type d'anticipation de cette demande de vieillesse. S'il n'y a pas eu d'anticipation, retournera
     * {@link Anticipation#AGE_LEGAL}
     * 
     * @param anticipation
     *            un type d'anticipation
     * @throws NullPointerException
     *             si le type d'anticipation passé en paramètre est null
     */
    public final void setAnticipation(final Anticipation anticipation) {
        Checkers.checkNotNull(anticipation, "demandeVieillesse.anticipation");
        this.anticipation = anticipation;
    }

    /**
     * (re-)défini si un ajournement a lieu avec cette demande de vieillesse
     * 
     * @param avecAjournement
     */
    public final void setAvecAjournement(final boolean avecAjournement) {
        this.avecAjournement = avecAjournement;
    }

    /**
     * (re-)défini la date à laquelle l'ajournement a pris fin. Une chaîne vide représente une non valeur
     * 
     * @param dateRevocationAjournement
     *            une date au format JJ.MM.AAAA ou une chaîne vide
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée au paramètre n'est pas vide et n'est pas au bon format
     */
    public final void setDateRevocationAjournement(final String dateRevocationAjournement) {
        Checkers.checkNotNull(dateRevocationAjournement, "demandeVieillesse.dateRevocationAjournement");
        Checkers.checkFullDate(dateRevocationAjournement, "demandeVieillesse.dateRevocationAjournement", true);
        this.dateRevocationAjournement = dateRevocationAjournement;
    }
}
