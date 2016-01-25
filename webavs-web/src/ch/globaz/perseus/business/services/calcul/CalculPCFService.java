/**
 * 
 */
package ch.globaz.perseus.business.services.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.models.demande.Demande;

/**
 * Service permettant de calculer une Prestation Complémentaire pour les PC Familles
 * 
 * @author DDE
 * 
 */
public interface CalculPCFService extends JadeApplicationService {

    /**
     * Permet le calcul d'une prestation complémntaire famille sur la base d'une demande
     * 
     * @param demande
     *            Demande pour laqueelle effecture le calcul
     * @return OutputCalcul Structure contenant le calcul effectué
     * @throws JadePersistenceException
     * @throws CalculException
     */
    public OutputCalcul calculerPCF(Demande demande) throws JadePersistenceException, CalculException;

    /**
     * Permet le calcul d'une prestation complémntaire famille sur la base d'une demande
     * 
     * @param IdDemande
     *            Id de la demande pour laquelle effectuer le calcul
     * @return OutputCalcul Structure contenant le calcul effectué
     * @throws JadePersistenceException
     * @throws CalculException
     */
    public OutputCalcul calculerPCF(String idDemande) throws JadePersistenceException, CalculException;

    /**
     * Permet de contrôler si le calcul est possible sur une demande (si les données financières obligatoires sont
     * renseignées)
     * 
     * @param demande
     * @return Chaine vide si le calcul est possible, ou message concernant les données manquantes
     * @throws JadePersistenceException
     * @throws CalculException
     */
    public String checkCalcul(Demande demande) throws JadePersistenceException, CalculException;

}
