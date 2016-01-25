/**
 * 
 */
package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciereType;

/**
 * Service permettant de manipuler plusierus types de donn�es financi�res en m�me temps
 * 
 * @author DDE
 * 
 */
public interface DonneeFinanciereService extends JadeApplicationService {

    /**
     * Permet de supprimer toutes les donn�es financi�res d'une demande
     * 
     * @param idDemande
     * @throws JadePersistenceException
     * @throws DonneesFinancieresException
     */
    public int deleteForDemande(String idDemande) throws JadePersistenceException, DonneesFinancieresException;

    /**
     * Permet de supprimer toutes les donn�es financi�res d'un membre famille pour une demande
     * 
     * @param idDemande
     * @param idMembreFamille
     * @throws JadePersistenceException
     * @throws DonneesFinancieresException
     */
    public int deleteForDemandeAndMembreFamille(String idDemande, String idMembreFamille)
            throws JadePersistenceException, DonneesFinancieresException;

    /**
     * Permet de retrouver une donn�e finacni�re sp�cifique pour un membre famille
     * 
     * @param type
     * @param idMembreFamille
     * @param idDemande
     * @return Retourne null si la donn�e n'existe pas
     * @throws JadePersistenceException
     * @throws DonneesFinancieresException
     */
    public DonneeFinanciere search(DonneeFinanciereType type, String idMembreFamille, String idDemande)
            throws JadePersistenceException, DonneesFinancieresException;

}
