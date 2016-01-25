package ch.globaz.perseus.business.services.paiement;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.HashMap;
import java.util.List;

import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;

public interface PmtMensuelService extends JadeApplicationService {

	/**
	 * Activer des validations des décisions
	 * 
	 * @throws PaiementException
	 */
	public void activerValidationDecision() throws PaiementException;

	/**
	 * Activer des validations des décisions
	 * 
	 * @throws PaiementException
	 */
	public void desactiverValidationDecision() throws PaiementException;

	/**
	 * Permet d'exécuter le paiement mensuel des PCF Accordées
	 * 
	 * @param session
	 * @param logSession
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public void executerPaiementMensuel(BSession session, JadeBusinessLogSession logSession) throws PaiementException,
			JadePersistenceException;

	/**
	 * Donne la date du dernier paiement mensuel
	 * 
	 * @return date au format mm.aaaa
	 */
	public String getDateDernierPmt() throws PaiementException, JadePersistenceException;

	/**
	 * Donne la date du prochain paiement mensuel
	 * 
	 * @return date au format mm.aaaa
	 */
	public String getDateProchainPmt() throws PaiementException, JadePersistenceException;

	/**
	 * Retourne la liste des décisions d'octroi complet pour la liste de pcfa
	 * 
	 * @param listPcfa
	 * @return
	 * @throws DecisionException
	 * @throws JadeApplicationServiceNotAvailableException
	 * @throws JadePersistenceException
	 */
	public HashMap<String, Decision> getDecisionForPcfa(List<PCFAccordee> listPcfa) throws DecisionException,
			JadeApplicationServiceNotAvailableException, JadePersistenceException;

	/**
	 * 
	 * @return true si la validation des décisions est autorisee, sinon false.
	 */
	public boolean isValidationDecisionAuthorise() throws PaiementException, JadePersistenceException;

	/**
	 * Pour utiliser lorsqu'il n'y a pas de contexte
	 * 
	 * @return true si la validation des décisions est autorisee, sinon false.
	 */
	public boolean isValidationDecisionAuthorise(BSession session) throws PaiementException, JadePersistenceException;

	/**
	 * Liste des PCFAccordéées qui ne sont plus payées à partir du mois en cours
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFAdiminuees(String mois) throws PaiementException, JadePersistenceException;

	/**
	 * Liste des PCFAccordéées en cours pour le mois
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFAencours(String mois) throws PaiementException, JadePersistenceException;

	/**
	 * Liste des PCFAccordéées en cours pour le mois
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @param onlyOnError
	 *            si on veut que les PCFA en erreur (pour le paiement partiel)
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFAencours(String mois, Boolean onlyOnError) throws PaiementException,
			JadePersistenceException;

	/**
	 * Liste des PCFAccordéées qui sont payées à partir du mois en cours
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFaugmentees(String mois) throws PaiementException, JadePersistenceException;

	/**
	 * Permet de savoir si une PCFAccordée sera payée le mois passé en paramètre
	 * 
	 * @param pcfAccordee
	 * @param mois
	 *            au foramt MM.YYYY
	 * @return true si la pcfa est payé au mois passé en paramètre
	 */
	public boolean willBePaid(PCFAccordee pcfAccordee, String mois);

}
