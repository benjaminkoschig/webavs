package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;

/**
 * Service relatif aux calculs du revenu déterminant et déductions sourcier
 * 
 * @author dhi
 * 
 */
public interface RevenuCalculService extends JadeApplicationService {
    /**
     * Call by ajax with enough parameters (anneeTaxation, revenuPrisEnCompte, ...) to compute deductions for sourcier
     * 
     * @param values
     * @return
     * @throws RevenuException
     */
    public RevenuFullComplex calculDeductionSourcier(HashMap<?, ?> values) throws RevenuException;

    /**
     * Call by ajax with enough parameters (anneeTaxation, revenuPrisEnCompte, ...) to compute deductions for
     * contribuable standard
     * 
     * @param values
     * @return
     * @throws RevenuException
     */
    public RevenuFullComplex calculDeductionStandard(HashMap<?, ?> values) throws RevenuException;

    /**
     * Call by ajax with enough parameters (anneeHistorique, id Taxation) to compute the revenu determinant
     * 
     * @param values
     * @return
     * @throws RevenuException
     */
    public RevenuHistoriqueComplex calculRDet(HashMap<?, ?> values) throws RevenuException;
}