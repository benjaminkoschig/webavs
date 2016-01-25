/**
 * 
 */
package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.models.qd.QDAnnuelleSearchModel;

/**
 * Service permettant de gréer les QD Annuelles
 * 
 * @author DDE
 * 
 */
public interface QDAnnuelleService extends JadeApplicationService {

    /**
     * Permet de créer une qdAnnuelle avec toutes les QDs nécessaires pour tous les membres familles d'une demande Si
     * elle existe déjà, celle-ci est retournée
     * 
     * @param demande
     * @param excedantRevenu
     * @param annee
     * @return QDAnnuelle
     * @throws QDException
     * @throws JadePersistenceException
     */
    public QDAnnuelle createOrRead(Demande demande, String excedantRevenu, String annee) throws QDException,
            JadePersistenceException;

    /**
     * Permet de créer une qdAnnuelle avec toutes les QDs nécessaires pour tous les membres familles d'une PCFAccordees
     * Si elle existe déjà, celle-ci est retournée
     * 
     * @param pcfAccordee
     * @param annee
     * @return QDAnnuelle
     * @throws QDException
     * @throws JadePersistenceException
     */
    public QDAnnuelle createOrRead(PCFAccordee pcfAccordee, String annee) throws QDException, JadePersistenceException;

    /**
     * Permet de créer un QD avec uniquement les frais de garde. Cette méthode est prévu pour l'ouverture des QD
     * spécifiques pour les Prestations complémentaires AVS/AI
     * 
     * @param demande
     * @param excedantRevenu
     * @param annee
     * @return
     * @throws QDException
     * @throws JadePersistenceException
     * @throws Exception
     */
    public QDAnnuelle createSpecificFraisGarde(Demande demande, String excedantRevenu, String annee)
            throws QDException, JadePersistenceException, Exception;

    public QDAnnuelle delete(QDAnnuelle qdAnnuelle) throws QDException, JadePersistenceException;

    /**
     * Retourne le montant disponbile pour le membre d'une famille pour un Type de QD
     * 
     * @param qdAnnuelle
     * @param idMembreFamille
     * @param typeQD
     * @return
     * @throws QDEXception
     * @throws JadePersistenceException
     */
    public float getMontantDisponible(QDAnnuelle qdAnnuelle, String idMembreFamille, CSTypeQD typeQD)
            throws QDException, JadePersistenceException;

    /**
     * Permet de retrouver la qd d'un dossier pour une annéee donnée
     * 
     * @param idDossier
     * @param annee
     * @return QDAnnuelle
     * @throws QDException
     * @throws JadePersistenceException
     */
    public QDAnnuelle read(String idDossier, String annee) throws QDException, JadePersistenceException;

    public QDAnnuelleSearchModel search(QDAnnuelleSearchModel searchModel) throws QDException, JadePersistenceException;

    public QDAnnuelle update(QDAnnuelle qdAnnuelle) throws QDException, JadePersistenceException;

}
