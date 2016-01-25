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
	 * Activer des validations des d�cisions
	 * 
	 * @throws PaiementException
	 */
	public void activerValidationDecision() throws PaiementException;

	/**
	 * Activer des validations des d�cisions
	 * 
	 * @throws PaiementException
	 */
	public void desactiverValidationDecision() throws PaiementException;

	/**
	 * Permet d'ex�cuter le paiement mensuel des PCF Accord�es
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
	 * Retourne la liste des d�cisions d'octroi complet pour la liste de pcfa
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
	 * @return true si la validation des d�cisions est autorisee, sinon false.
	 */
	public boolean isValidationDecisionAuthorise() throws PaiementException, JadePersistenceException;

	/**
	 * Pour utiliser lorsqu'il n'y a pas de contexte
	 * 
	 * @return true si la validation des d�cisions est autorisee, sinon false.
	 */
	public boolean isValidationDecisionAuthorise(BSession session) throws PaiementException, JadePersistenceException;

	/**
	 * Liste des PCFAccord��es qui ne sont plus pay�es � partir du mois en cours
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFAdiminuees(String mois) throws PaiementException, JadePersistenceException;

	/**
	 * Liste des PCFAccord��es en cours pour le mois
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFAencours(String mois) throws PaiementException, JadePersistenceException;

	/**
	 * Liste des PCFAccord��es en cours pour le mois
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
	 * Liste des PCFAccord��es qui sont pay�es � partir du mois en cours
	 * 
	 * @param mois
	 *            au format MM.YYYY
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<PCFAccordee> listPCFaugmentees(String mois) throws PaiementException, JadePersistenceException;

	/**
	 * Permet de savoir si une PCFAccord�e sera pay�e le mois pass� en param�tre
	 * 
	 * @param pcfAccordee
	 * @param mois
	 *            au foramt MM.YYYY
	 * @return true si la pcfa est pay� au mois pass� en param�tre
	 */
	public boolean willBePaid(PCFAccordee pcfAccordee, String mois);

}
