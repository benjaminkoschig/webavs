package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALConstAdiDecomptes;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Sur le plan métier, défini quel service va remplir les données en fonction du type de décompte global à remplir (pour
 * un dossier direct ou indirect)
 * 
 * @author PTA
 * 
 */
public class AdiDecompteProvider {

    /**
     * 
     * @param decompteGlobal
     *            décompte ADI DossierComplexModel
     * @param typeDecompte
     *            type de décompte Adi
     * @return AdiDecomptesService
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static AdiDecompteService getAdiDecompteService(AdiDecompteComplexModel decompteGlobal, String typeDecompte)
            throws JadeApplicationException {
        // contrôle les paramètres
        if (decompteGlobal == null) {
            throw new ALAdiDecomptesException("AdiDecompteProvider#getAdiDecomptesService: dossier is null");
        }

        if (!JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE, typeDecompte, false)
                && (!JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL, typeDecompte, false))) {
            throw new ALAdiDecomptesException("AdiDecompteProvider#getAdiDecomptesServices: " + typeDecompte
                    + " is not a valid type of account ");
        }

        // cas d'un decompte détaillée pour un dossier à paiement indirect
        if (JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE, typeDecompte, false)) {
            return ALImplServiceLocator.getAdiDecompteDetailleService();
            // cas d'un decompte global pour un dossier à paiement indirect
        } else if (JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL, typeDecompte, false)) {
            return ALImplServiceLocator.getAdiDecompteGlobalService();
        } else {

            throw new ALAdiDecomptesException(
                    "AdiDecompteProvider#getAdiDecomptesService : Impossible de déterminer le type de decompteAdi");
        }
    }
}
