package ch.globaz.al.businessimpl.copies.defaut;

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
public class ContextDefaultCopiesLoader {

    /**
     * Retourne une nouvelle instance du contexte
     * 
     * @param dossier
     *            Dossier pour lequel les copies par défaut devront être chargées
     * @param typeCopie
     *            Type de copie. Code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * 
     * @return nouvelle instance du contexte
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètre n'est pas valide
     */
    public static ContextDefaultCopiesLoader getInstance(DossierComplexModel dossier, String typeCopie)
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

        return new ContextDefaultCopiesLoader(dossier, typeCopie);
    }

    /**
     * Dossier pour lequel les copies par défaut devront être chargées
     */
    private DossierComplexModel dossier = null;

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
     */
    private ContextDefaultCopiesLoader(DossierComplexModel dossier, String typeCopie) {

        this.dossier = dossier;
        this.typeCopie = typeCopie;
    }

    /**
     * @return the dossier
     */
    public DossierComplexModel getDossier() {
        return dossier;
    }

    /**
     * @return the typeCopie
     */
    public String getTypeCopie() {
        return typeCopie;
    }
}
