package ch.globaz.prestation.business.services.models.echeance;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.common.ajax.EcheanceJson;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;

public interface EcheanceServiceJson extends JadeApplicationService {
    /**
     * Permet de chercher les échéances en fonction d'un idExterne et un domaine Un traitement métier est déjà appliqué
     * sur l'état de l'échéance si la date de l'échéance est échue
     * 
     * @param idExterne
     * @param domaine
     * @return La liste des échéance
     */
    List<EcheanceJson> findByIdExterne(String idExterne, String domaine);

    List<EcheanceJson> findByIdExterneAndType(String idExterne, String type, String domaine);

    List<EcheanceJson> findByIdExterneAndIdTiersAndType(String idExterne, String idTiers, String type, String domaine);

    /**
     * Permet de rechercher les échances à traiter. Filtre les échéances anullée
     * 
     * @param idExterne
     * @param domaine
     * @return
     */
    List<EcheanceJson> findToTreatByIdExterne(String idExterne, String domaine);

    List<EcheanceJson> findToTreatByIdExterneAndType(String idExterne, String type, String domaine);

    List<EcheanceJson> findToTreatByIdExterneAndIdTiersAndType(String idExterne, String idTiers, String type,
            String domaine);

    EcheanceJson add(EcheanceJson echeanceJson) throws PrestationCommonException;

    EcheanceJson update(EcheanceJson echeanceJson) throws PrestationCommonException;

    EcheanceJson read(String id) throws PrestationCommonException;

    EcheanceJson delete(String id) throws PrestationCommonException;

    EcheanceJson traitee(String id) throws PrestationCommonException;

    EcheanceJson findNearestTerm(String idExterne, String idTiers, String type, String domaine);

    EcheanceJson toggleEtatToTraiteeOrPlanifiee(String id) throws PrestationCommonException;

}
