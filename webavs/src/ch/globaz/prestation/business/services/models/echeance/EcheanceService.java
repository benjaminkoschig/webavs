package ch.globaz.prestation.business.services.models.echeance;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;

public interface EcheanceService extends JadeApplicationService {

    /**
     * Permet de chercher les �ch�ances en fonction d'un idExterne et un domaine Un traitement m�tier est d�j� appliqu�
     * sur l'�tat de l'�ch�ance si la date de l'�ch�ance est �chue
     * 
     * @param idExterne
     * @param domaine
     * @return La liste des �ch�ance
     */
    List<Echeance> findByIdExterne(String idExterne, EcheanceDomaine domaine);

    List<Echeance> findByIdExterneAndType(String idExterne, EcheanceType valueOf, EcheanceDomaine valueOf2);

    List<Echeance> findToTreatByIdExterneAndType(String idExterne, EcheanceType type, EcheanceDomaine domaine);

    List<Echeance> findByIdExterneAndIdTiers(String idExterne, String idTiers, EcheanceDomaine domaine);

    List<Echeance> findByIdExterneAndIdTiersAndType(String idExterne, String idTiers, EcheanceType type,
            EcheanceDomaine domaine);

    /**
     * Permet de trouver toute les �ch�ances � traiter.
     * 
     * @param idExterne
     * @param domaine
     * @return La liste des �ch�ances � traiter
     */
    List<Echeance> findToTreatByIdExterne(String idExterne, EcheanceDomaine domaine);

    /**
     * Change l'�tat de l'�ch�ance � trait�e et ajoute un date de traitement
     * 
     * @param id
     * @return l'�chance modifi�e
     * @throws PrestationCommonException
     */
    Echeance changeEtatToTraitee(String id) throws PrestationCommonException;

    /**
     * Recherche l'�chance en fonction de l'id Modifi l'�tat de l'�ch�ance � Annul� Ajoute la date de traitement avec la
     * date du jour.
     * 
     * @param id
     * @return EcheanceJson modifi�
     * @throws PrestationCommonException
     */
    Echeance changeEtatToAnnule(String id) throws PrestationCommonException;

    Echeance read(String id) throws PrestationCommonException;

    boolean deleteById(Integer id) throws PrestationCommonException;

    boolean delete(Echeance echeance) throws PrestationCommonException;

    Echeance update(Echeance echeance) throws PrestationCommonException;

    Echeance add(Echeance echeance) throws PrestationCommonException;

    Echeance toggleEtatToTraiteeOrPlanifiee(String id) throws PrestationCommonException;

    /**
     * @param idExterne
     * @param idTiers
     * @param type
     * @param domaine
     * @return l'�chance qui a l'�ch�ance la plus proche
     */
    Echeance findNearestTerm(String idExterne, String idTiers, EcheanceType type, EcheanceDomaine domaine);

    List<Echeance> findToTreatByIdExterneAndIdTiersAndType(String idExterne, String idTiers, EcheanceType type,
            EcheanceDomaine domaine);

}
