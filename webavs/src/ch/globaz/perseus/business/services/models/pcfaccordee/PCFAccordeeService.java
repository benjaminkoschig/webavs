/**
 * 
 */
package ch.globaz.perseus.business.services.models.pcfaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeForDetailsCalculSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;

/**
 * @author DDE
 * 
 */
public interface PCFAccordeeService extends JadeApplicationService {

    /**
     * Permet de calculer et enretgistrer une PCF Accordée
     * 
     * @param idDemande
     *            Id de la demande pour laquelle faire le calcul
     * @return la nouvelle PCFAccordee
     * @throws JadePersistenceException
     * @throws PCFAccordeeException
     * @throws Exception
     */
    public PCFAccordee calculer(String idDemande) throws JadePersistenceException, PCFAccordeeException;

    public int count(PCFAccordeeSearchModel search) throws PCFAccordeeException, JadePersistenceException;

    public PCFAccordee create(PCFAccordee pCFAccordee) throws JadePersistenceException, PCFAccordeeException;

    public PCFAccordee delete(PCFAccordee pCFAccordee) throws JadePersistenceException, PCFAccordeeException;

    public PCFAccordee deleteNoUpdateEtatDemande(PCFAccordee pCFAccordee) throws JadePersistenceException,
            PCFAccordeeException;

    public OutputCalcul deserializeCalcul(byte[] outputCalcul);

    public PCFAccordee read(String idPCFAccordee) throws JadePersistenceException, PCFAccordeeException;

    /**
     * Permet de récupérer la PCFAccordée d'une demande
     * 
     * @param idDemande
     * @return
     * @throws JadePersistenceException
     * @throws PCFAccordeeException
     */
    public PCFAccordee readForDemande(String idDemande) throws JadePersistenceException, PCFAccordeeException;

    public PCFAccordeeSearchModel search(PCFAccordeeSearchModel searchModel) throws JadePersistenceException,
            PCFAccordeeException;

    public PCFAccordeeForDetailsCalculSearchModel searchWithBlobs(PCFAccordeeForDetailsCalculSearchModel searchModel)
            throws JadePersistenceException, PCFAccordeeException;

    public PCFAccordeeSearchModel searchWithBlobs(PCFAccordeeSearchModel searchModel) throws JadePersistenceException,
            PCFAccordeeException;

    public PCFAccordee update(PCFAccordee pCFAccordee) throws JadePersistenceException, PCFAccordeeException;

    public PCFAccordee updateWithoutCalcul(PCFAccordee pCFAccordee) throws JadePersistenceException,
            PCFAccordeeException;

    public PCFAccordee updateValidationDecision(PCFAccordee pCFAccordee) throws JadePersistenceException,
            PCFAccordeeException;

    /**
     * Permet de modifier une pcfAccordee en appliquant une mesure d'encouragement et la mesure de coaching
     * 
     * @param pCFAccordee
     * @param mesureEncouragement
     *            Montant de la mesure d'encouragement qui sera ajouter à la PCF Accordée
     * @param mesureChargesLoyer
     *            Montant de la mesure pour les charges de loyer
     * @param mesureCoaching
     *            Montant de la mesure de coaching
     * @return
     * @throws JadePersistenceException
     * @throws PCFAccordeeException
     */
    public PCFAccordee update(PCFAccordee pCFAccordee, String mesureEncouragement, String mesureChargesLoyer,
            String mesureCoaching) throws JadePersistenceException, PCFAccordeeException;

}
