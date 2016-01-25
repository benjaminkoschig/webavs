package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpression;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionAncienneSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionSearch;
import ch.globaz.pegasus.business.vo.pcaccordee.Regimes02RFMVo;

/**
 * @author DMA
 * 
 */
public interface PCAccordeeAdaptationImpressionService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(PCAccordeeAdaptationImpressionSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * Charge l'entit� en base de donn�es
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    public PCAccordeeAdaptationImpression read(String idDecisionHeader) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException;

    public PCAccordeeAdaptationImpressionAncienneSearch search(
            PCAccordeeAdaptationImpressionAncienneSearch pcAccordeeSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de chercher des pc accordees selon un mod�le de crit�res.
     * 
     * @param pcAccordeeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PCAccordeeAdaptationImpressionSearch search(PCAccordeeAdaptationImpressionSearch pcAccordeeSearch)
            throws JadePersistenceException, PCAccordeeException;

    public PCAccordeeAdaptationImpression search(String idDecisionHeader) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException;

    /**
     * Recherche des regimes RFM de type alimenraire et diab�tique pour une date donn�e Essentiellement pr�vu pour la
     * recherche des r�gimes RFM pour l'impression des d�cisions light d'adaptation
     * 
     * @param idTiersBeneficiaires
     * @param dateAdaptation
     * @return
     * @throws Exception
     */
    public ArrayList<Regimes02RFMVo> searchRegimeRFM(ArrayList<String> idTiersBeneficiaires, String dateAdaptation)
            throws Exception;

}
