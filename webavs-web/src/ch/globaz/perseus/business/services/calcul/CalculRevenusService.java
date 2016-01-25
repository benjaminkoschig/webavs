package ch.globaz.perseus.business.services.calcul;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;

/**
 * @author DDE
 * 
 */
public interface CalculRevenusService extends JadeApplicationService {

    /**
     * Aditionne les informations pour le revenu brut pour l'imp�t � la source. ATTENTION !!! le calcul du revenu
     * d�terminant doit avoir �t� lanc� avant
     * 
     * @param inputCalcul
     * @param outputCalcul
     * @return
     * @throws CalculException
     */
    public OutputCalcul calculerRevenusBrutImpotSource(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException;

    public OutputCalcul calculerRevenusDeterminants(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException;

}
