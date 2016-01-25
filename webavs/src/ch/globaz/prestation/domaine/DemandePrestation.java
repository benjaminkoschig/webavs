package ch.globaz.prestation.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * <p>
 * Une demande de prestation
 * </p>
 * <p>
 * Base commune � tous les modules des prestations pour les demandes (doit �tre �tendue)
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
     * @return le dossier sur lequel est li�e cette demande
     */
    public final DossierPrestation getDossier() {
        return dossier;
    }

    /**
     * @return les informations compl�mentaires de cette demande
     */
    public final InformationsComplementaires getInformationsComplementaires() {
        return informationsComplementaires;
    }

    /**
     * (re-)d�fini le dossier sur lequel est li�e cette demande
     * 
     * @param dossier
     *            un dossier contenant, entre autre, le requ�rant
     * @throws NullPointerException
     *             si le dossier pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le dossier pass� en param�tre n'est pas initialis�
     */
    public final void setDossier(final DossierPrestation dossier) {
        Checkers.checkNotNull(dossier, "demande.dossier");
        Checkers.checkHasID(dossier, "demande.dossier");
        this.dossier = dossier;
    }

    /**
     * (re-)d�fini les informations compl�mentaires de cette demande
     * 
     * @param informationsComplementaires
     *            des informations compl�mentaires
     * @throws NullPointerException
     *             si les informations compl�mentaires pass�es en param�tre sont null
     * @throws IllegalArgumentException
     *             si les informations compl�mentaires pass�es en param�tre ne sont pas initialis�es
     */
    public final void setInformationsComplementaires(final InformationsComplementaires informationsComplementaires) {
        Checkers.checkNotNull(informationsComplementaires, "demande.informationsComplementaires");
        Checkers.checkHasID(informationsComplementaires, "demande.informationsComplementaires");
        this.informationsComplementaires = informationsComplementaires;
    }
}
