package ch.globaz.prestation.business.services.models.echeance;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;

public interface EcheanceService extends JadeApplicationService {

    /**
     * Permet de chercher les échéances en fonction d'un idExterne et un domaine Un traitement métier est déjà appliqué
     * sur l'état de l'échéance si la date de l'échéance est échue
     * 
     * @param idExterne
     * @param domaine
     * @return La liste des échéance
     */
    List<Echeance> findByIdExterne(String idExterne, EcheanceDomaine domaine);

    List<Echeance> findByIdExterneAndType(String idExterne, EcheanceType valueOf, EcheanceDomaine valueOf2);

    List<Echeance> findToTreatByIdExterneAndType(String idExterne, EcheanceType type, EcheanceDomaine domaine);

    List<Echeance> findByIdExterneAndIdTiers(String idExterne, String idTiers, EcheanceDomaine domaine);

    List<Echeance> findByIdExterneAndIdTiersAndType(String idExterne, String idTiers, EcheanceType type,
            EcheanceDomaine domaine);

    /**
     * Permet de trouver toute les échéances à traiter.
     * 
     * @param idExterne
     * @param domaine
     * @return La liste des échéances à traiter
     */
    List<Echeance> findToTreatByIdExterne(String idExterne, EcheanceDomaine domaine);

    /**
     * Change l'état de l'échéance à traitée et ajoute un date de traitement
     * 
     * @param id
     * @return l'échance modifiée
     * @throws PrestationCommonException
     */
    Echeance changeEtatToTraitee(String id) throws PrestationCommonException;

    /**
     * Recherche l'échance en fonction de l'id Modifi l'état de l'échéance à Annulé Ajoute la date de traitement avec la
     * date du jour.
     * 
     * @param id
     * @return EcheanceJson modifié
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
     * @return l'échance qui a l'échéance la plus proche
     */
    Echeance findNearestTerm(String idExterne, String idTiers, EcheanceType type, EcheanceDomaine domaine);

    List<Echeance> findToTreatByIdExterneAndIdTiersAndType(String idExterne, String idTiers, EcheanceType type,
            EcheanceDomaine domaine);

}
