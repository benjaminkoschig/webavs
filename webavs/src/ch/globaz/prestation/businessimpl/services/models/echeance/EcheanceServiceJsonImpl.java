package ch.globaz.prestation.businessimpl.services.models.echeance;

import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.ajax.EcheanceJson;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.services.models.echeance.EcheanceService;
import ch.globaz.prestation.business.services.models.echeance.EcheanceServiceJson;
import ch.globaz.prestation.businessimpl.services.PrestationCommonImplServiceLocator;

public class EcheanceServiceJsonImpl implements EcheanceServiceJson {

    @Override
    public List<EcheanceJson> findByIdExterne(String idExterne, String domaine) {
        List<Echeance> list = service().findByIdExterne(idExterne, EcheanceDomaine.valueOf(domaine));
        List<EcheanceJson> listJson = genreateList(list);
        return listJson;
    }

    @Override
    public List<EcheanceJson> findByIdExterneAndType(String idExterne, String type, String domaine) {
        List<Echeance> list = service().findByIdExterneAndType(idExterne, EcheanceType.valueOf(type),
                EcheanceDomaine.valueOf(domaine));
        List<EcheanceJson> listJson = genreateList(list);
        return listJson;
    }

    @Override
    public List<EcheanceJson> findToTreatByIdExterneAndType(String idExterne, String type, String domaine) {
        List<Echeance> list = service().findToTreatByIdExterneAndType(idExterne, EcheanceType.valueOf(type),
                EcheanceDomaine.valueOf(domaine));
        List<EcheanceJson> listJson = genreateList(list);
        return listJson;
    }

    @Override
    public List<EcheanceJson> findByIdExterneAndIdTiersAndType(String idExterne, String idTiers, String type,
            String domaine) {
        List<Echeance> list = service().findByIdExterneAndIdTiersAndType(idExterne, idTiers,
                EcheanceType.valueOf(type), EcheanceDomaine.valueOf(domaine));
        List<EcheanceJson> listJson = genreateList(list);
        return listJson;
    }

    @Override
    public List<EcheanceJson> findToTreatByIdExterne(String idExterne, String domaine) {
        List<Echeance> list = service().findToTreatByIdExterne(idExterne, EcheanceDomaine.valueOf(domaine));
        List<EcheanceJson> listJson = genreateList(list);
        return listJson;
    }

    @Override
    public List<EcheanceJson> findToTreatByIdExterneAndIdTiersAndType(String idExterne, String idTiers, String type,
            String domaine) {
        List<Echeance> list = service().findToTreatByIdExterneAndIdTiersAndType(idExterne, idTiers,
                EcheanceType.valueOf(type), EcheanceDomaine.valueOf(domaine));
        List<EcheanceJson> listJson = genreateList(list);
        return listJson;
    }

    @Override
    public EcheanceJson findNearestTerm(String idExterne, String idTiers, String type, String domaine) {
        Echeance echeance = service().findNearestTerm(idExterne, idTiers, EcheanceType.valueOf(type),
                EcheanceDomaine.valueOf(domaine));
        if (echeance != null) {
            return EcheanceJsonConverter.convert(echeance);
        }
        return null;
    }

    @Override
    public EcheanceJson add(EcheanceJson echeanceJson) throws PrestationCommonException {
        Echeance echeance = EcheanceJsonConverter.convert(echeanceJson);
        echeanceJson = EcheanceJsonConverter.convert(service().add(echeance));
        return echeanceJson;
    }

    public boolean remove(EcheanceJson echeanceJson) throws PrestationCommonException {
        Echeance echeance = EcheanceJsonConverter.convert(echeanceJson);
        return service().delete(echeance);
    }

    @Override
    public EcheanceJson update(EcheanceJson echeanceJson) throws PrestationCommonException {
        Echeance echeance = EcheanceJsonConverter.convert(echeanceJson);
        echeanceJson = EcheanceJsonConverter.convert(service().update(echeance));
        return echeanceJson;
    }

    @Override
    public EcheanceJson read(String id) throws PrestationCommonException {
        EcheanceJson echeanceJson = EcheanceJsonConverter.convert(service().read(id));
        return echeanceJson;
    }

    private EcheanceService service() {
        try {
            return PrestationCommonImplServiceLocator.getEcheanceService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException("JadeApplicationServiceNotAvailableException ->EcheanceService", e);
        }
    }

    @Override
    public EcheanceJson delete(String id) throws PrestationCommonException {
        Echeance echeance = service().changeEtatToAnnule(id);
        return EcheanceJsonConverter.convert(echeance);
    }

    @Override
    public EcheanceJson traitee(String id) throws PrestationCommonException {
        Echeance echeance = service().changeEtatToTraitee(id);
        return EcheanceJsonConverter.convert(echeance);
    }

    @Override
    public EcheanceJson toggleEtatToTraiteeOrPlanifiee(String id) throws PrestationCommonException {
        Echeance echeance = service().toggleEtatToTraiteeOrPlanifiee(id);
        return EcheanceJsonConverter.convert(echeance);
    }

    private List<EcheanceJson> genreateList(List<Echeance> list) {
        List<EcheanceJson> listJson = new ArrayList<EcheanceJson>(list.size());
        for (Echeance echeance : list) {
            listJson.add(EcheanceJsonConverter.convert(echeance));
        }
        return listJson;
    }
}
