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
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PCAccordeeAdaptationImpressionSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * Charge l'entité en base de données
     * 
     * @param idDecision
     * @return l'entité chargé
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
     * Permet de chercher des pc accordees selon un modèle de critères.
     * 
     * @param pcAccordeeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PCAccordeeAdaptationImpressionSearch search(PCAccordeeAdaptationImpressionSearch pcAccordeeSearch)
            throws JadePersistenceException, PCAccordeeException;

    public PCAccordeeAdaptationImpression search(String idDecisionHeader) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException;

    /**
     * Recherche des regimes RFM de type alimenraire et diabétique pour une date donnée Essentiellement prévu pour la
     * recherche des régimes RFM pour l'impression des décisions light d'adaptation
     * 
     * @param idTiersBeneficiaires
     * @param dateAdaptation
     * @return
     * @throws Exception
     */
    public ArrayList<Regimes02RFMVo> searchRegimeRFM(ArrayList<String> idTiersBeneficiaires, String dateAdaptation)
            throws Exception;

}
