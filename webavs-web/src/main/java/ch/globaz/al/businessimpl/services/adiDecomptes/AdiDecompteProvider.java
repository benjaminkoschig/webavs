package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALConstAdiDecomptes;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Sur le plan m�tier, d�fini quel service va remplir les donn�es en fonction du type de d�compte global � remplir (pour
 * un dossier direct ou indirect)
 * 
 * @author PTA
 * 
 */
public class AdiDecompteProvider {

    /**
     * 
     * @param decompteGlobal
     *            d�compte ADI DossierComplexModel
     * @param typeDecompte
     *            type de d�compte Adi
     * @return AdiDecomptesService
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static AdiDecompteService getAdiDecompteService(AdiDecompteComplexModel decompteGlobal, String typeDecompte)
            throws JadeApplicationException {
        // contr�le les param�tres
        if (decompteGlobal == null) {
            throw new ALAdiDecomptesException("AdiDecompteProvider#getAdiDecomptesService: dossier is null");
        }

        if (!JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE, typeDecompte, false)
                && (!JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL, typeDecompte, false))) {
            throw new ALAdiDecomptesException("AdiDecompteProvider#getAdiDecomptesServices: " + typeDecompte
                    + " is not a valid type of account ");
        }

        // cas d'un decompte d�taill�e pour un dossier � paiement indirect
        if (JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE, typeDecompte, false)) {
            return ALImplServiceLocator.getAdiDecompteDetailleService();
            // cas d'un decompte global pour un dossier � paiement indirect
        } else if (JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL, typeDecompte, false)) {
            return ALImplServiceLocator.getAdiDecompteGlobalService();
        } else {

            throw new ALAdiDecomptesException(
                    "AdiDecompteProvider#getAdiDecomptesService : Impossible de d�terminer le type de decompteAdi");
        }
    }
}
