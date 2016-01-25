/**
 * 
 */
package ch.globaz.perseus.business.services.calcul;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;

/**
 * @author DDE
 * 
 */
public interface CalculDepensesReconnuesService extends JadeApplicationService {

    public OutputCalcul calculerDepensesReconnues(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException;

}
