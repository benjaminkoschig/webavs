package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.exceptions.copies.ALCopieBusinessException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * Contexte utilisé pour définir la liste des copies par défaut
 * 
 * @author jts
 * 
 */
public class ContextLibellesCopiesLoader {

    /**
     * Retourne une nouvelle instance du contexte
     * 
     * @param dossier
     *            Dossier pour lequel les copies par défaut devront être chargées
     * @param typeCopie
     *            Type de copie. Code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * @param idTiers
     *            id du tiers en copie
     * 
     * @return nouvelle instance du contexte
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètre n'est pas valide
     */
    public static ContextLibellesCopiesLoader getInstance(DossierComplexModel dossier, String typeCopie, String idTiers)
            throws JadeApplicationException {

        if (dossier == null) {
            throw new ALCopieBusinessException("ContextDefaultCopiesLoader#getInstance : dossier is null");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSCopie.GROUP_COPIE_TYPE, typeCopie)) {
                throw new ALCopieBusinessException("ContextDefaultCopiesLoader#getInstance : '" + typeCopie
                        + "' is not a valid type of copy");
            }
        } catch (Exception e) {
            throw new ALCopieBusinessException(
                    "ContextDefaultCopiesLoader#getInstance : unable to check the type of copy", e);
        }

        return new ContextLibellesCopiesLoader(dossier, typeCopie, idTiers);
    }

    /**
     * Dossier pour lequel les copies par défaut devront être chargées
     */
    private DossierComplexModel dossier = null;

    /**
     * id du tiers en copie
     */
    private String idTiers = null;

    /**
     * Type de copie. Code système du groupe {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     */
    private String typeCopie = null;

    /**
     * Constructeur
     * 
     * @param dossier
     *            Dossier pour lequel les copies par défaut devront être chargées
     * @param typeCopie
     *            Type de copie. Code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * @param idTiers
     *            id du tiers en copie
     */
    private ContextLibellesCopiesLoader(DossierComplexModel dossier, String typeCopie, String idTiers) {

        this.dossier = dossier;
        this.typeCopie = typeCopie;
        this.idTiers = idTiers;
    }

    /**
     * @return the dossier
     */
    public DossierComplexModel getDossier() {
        return dossier;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the typeCopie
     */
    public String getTypeCopie() {
        return typeCopie;
    }
}
