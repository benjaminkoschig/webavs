package ch.globaz.perseus.business.services.paiement;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.rentepont.RentePont;

public interface PmtMensuelRentePontService extends JadeApplicationService {

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
	 * Retourne la liste des rentes ponts valides à payer pour un mois
	 * 
	 * @param mois
	 * @param annee
	 * @return
	 * @throws PaiementException
	 * @throws JadePersistenceException
	 */
	public List<RentePont> listRentePontEnCours(String moisAnnee, Boolean onError) throws PaiementException,
			RentePontException, JadePersistenceException;

}
