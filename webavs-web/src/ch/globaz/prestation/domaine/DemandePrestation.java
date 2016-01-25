package ch.globaz.prestation.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * <p>
 * Une demande de prestation
 * </p>
 * <p>
 * Base commune à tous les modules des prestations pour les demandes (doit être étendue)
 * </p>
 */
public abstract class DemandePrestation extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DossierPrestation dossier;
    private InformationsComplementaires informationsComplementaires;

    public DemandePrestation() {
        super();

        dossier = new DossierPrestation();
        informationsComplementaires = new InformationsComplementaires();
    }

    /**
     * @return le dossier sur lequel est liée cette demande
     */
    public final DossierPrestation getDossier() {
        return dossier;
    }

    /**
     * @return les informations complémentaires de cette demande
     */
    public final InformationsComplementaires getInformationsComplementaires() {
        return informationsComplementaires;
    }

    /**
     * (re-)défini le dossier sur lequel est liée cette demande
     * 
     * @param dossier
     *            un dossier contenant, entre autre, le requérant
     * @throws NullPointerException
     *             si le dossier passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le dossier passé en paramètre n'est pas initialisé
     */
    public final void setDossier(final DossierPrestation dossier) {
        Checkers.checkNotNull(dossier, "demande.dossier");
        Checkers.checkHasID(dossier, "demande.dossier");
        this.dossier = dossier;
    }

    /**
     * (re-)défini les informations complémentaires de cette demande
     * 
     * @param informationsComplementaires
     *            des informations complémentaires
     * @throws NullPointerException
     *             si les informations complémentaires passées en paramètre sont null
     * @throws IllegalArgumentException
     *             si les informations complémentaires passées en paramètre ne sont pas initialisées
     */
    public final void setInformationsComplementaires(final InformationsComplementaires informationsComplementaires) {
        Checkers.checkNotNull(informationsComplementaires, "demande.informationsComplementaires");
        Checkers.checkHasID(informationsComplementaires, "demande.informationsComplementaires");
        this.informationsComplementaires = informationsComplementaires;
    }
}
