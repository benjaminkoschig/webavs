package ch.globaz.corvus.domaine;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.constantes.Atteinte;
import ch.globaz.corvus.domaine.constantes.Infirmite;
import ch.globaz.corvus.domaine.constantes.OfficeAI;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Une demande de rente API
 */
public final class DemandeRenteAPI extends DemandeRente {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Atteinte atteinte;
    private boolean avecMotivation;
    private String dateSuvenanceEvenementAssure;
    private Infirmite infirmite;
    private OfficeAI officeAI;

    public DemandeRenteAPI() {
        super();

        atteinte = Atteinte.ATTEINTE_00;
        avecMotivation = false;
        dateSuvenanceEvenementAssure = "";
        infirmite = Infirmite.INFIRMITE_101;
        officeAI = OfficeAI.ASSURES_A_L_ETRANGER;

        setTypeDemandeRente(TypeDemandeRente.DEMANDE_API);
    }

    @Override
    public Set<CodePrestation> codesPrestationsAcceptesPourCeTypeDeDemande() {
        Set<CodePrestation> codesPrestationsAcceptes = new HashSet<CodePrestation>();

        for (CodePrestation unCodePrestation : CodePrestation.values()) {
            if (unCodePrestation.isAPI()) {
                codesPrestationsAcceptes.add(unCodePrestation);
            }
        }

        return codesPrestationsAcceptes;
    }

    /**
     * @return l'atteinte de l'assuré l'ayant conduit à avoir droit à une API
     */
    public final Atteinte getAtteinte() {
        return atteinte;
    }

    /**
     * @return la date à laquelle la personne est devenue impotente (le début de son droit à une API)
     */
    public final String getDateSuvenanceEvenementAssure() {
        return dateSuvenanceEvenementAssure;
    }

    /**
     * @return l'infirmitié de l'assuré
     */
    public final Infirmite getInfirmite() {
        return infirmite;
    }

    /**
     * @return l'office AI compétent pour cette API
     */
    public final OfficeAI getOfficeAI() {
        return officeAI;
    }

    /**
     * @return TODO : à clarifier ce que c'est exactement
     */
    public final boolean isAvecMotivation() {
        return avecMotivation;
    }

    /**
     * (re-)défini l'atteinte de l'assuré
     * 
     * @param atteinte
     *            une atteinte
     * @throws NullPointerException
     *             si l'atteinte est null
     */
    public final void setAtteinte(final Atteinte atteinte) {
        Checkers.checkNotNull(atteinte, "demandeAPI.atteinte");
        this.atteinte = atteinte;
    }

    /**
     * @param avecMotivation
     *            TODO : à définir plus exactement
     */
    public final void setAvecMotivation(final boolean avecMotivation) {
        this.avecMotivation = avecMotivation;
    }

    /**
     * (re-)défini la date à laquelle l'assuré est devenu impotent (la date à laquelle son droit à une API commence)
     * 
     * @param dateSuvenanceEvenementAssure
     *            une date au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée en paramètre n'est pas au bon format, ou est vide
     */
    public final void setDateSuvenanceEvenementAssure(final String dateSuvenanceEvenementAssure) {
        Checkers.checkNotNull(dateSuvenanceEvenementAssure, "demandeAPI.dateSuvenanceEvenementAssure");
        Checkers.checkFullDate(dateSuvenanceEvenementAssure, "demandeAPI.dateSuvenanceEvenementAssure", true);
        this.dateSuvenanceEvenementAssure = dateSuvenanceEvenementAssure;
    }

    /**
     * (re-)défini l'infirmité de l'assuré
     * 
     * @param infirmite
     *            une infirmité
     * @throws NullPointerException
     *             si l'infirmité est null
     */
    public final void setInfirmite(final Infirmite infirmite) {
        Checkers.checkNotNull(infirmite, "infirmite");
        this.infirmite = infirmite;
    }

    /**
     * (re-)défini l'office AI compétent pour cette API
     * 
     * @param officeAI
     *            un office AI
     * @throws NullPointerException
     *             si l'office AI est null
     */
    public final void setOfficeAI(final OfficeAI officeAI) {
        Checkers.checkNotNull(officeAI, "officeAI");
        this.officeAI = officeAI;
    }
}
