package ch.globaz.prestation.businessimpl.services.models.echeance;

import java.util.Date;
import java.util.List;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceEtat;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.services.models.echeance.EcheanceService;

public class EcheanceServiceImpl implements EcheanceService {

    EcheancePersister echeancePersister = new EcheancePersister();

    @Override
    public List<Echeance> findToTreatByIdExterne(String idExterne, EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        List<Echeance> list = echeancePersister.findToTreatByIdExterne(idExterne, domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public List<Echeance> findToTreatByIdExterneAndType(String idExterne, EcheanceType type, EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(type, "echeanceType");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        List<Echeance> list = echeancePersister.findToTreatByIdExterneAndType(idExterne, type.getCodeSysteme(),
                domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public List<Echeance> findToTreatByIdExterneAndIdTiersAndType(String idExterne, String idTiers, EcheanceType type,
            EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(type, "type");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        List<Echeance> list = echeancePersister.findToTreatByIdExterneAndIdTiersAndType(idExterne, idTiers,
                type.getCodeSysteme(), domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public List<Echeance> findByIdExterne(String idExterne, EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        List<Echeance> list = echeancePersister.findByIdExterne(idExterne, domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public List<Echeance> findByIdExterneAndType(String idExterne, EcheanceType type, EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        Checkers.checkNotNull(type, "echeanceType");
        List<Echeance> list = echeancePersister.findByIdExterneAndType(idExterne, type.getCodeSysteme(),
                domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public List<Echeance> findByIdExterneAndIdTiers(String idExterne, String idTiers, EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        Checkers.checkNotNull(idTiers, "idTiers");
        List<Echeance> list = echeancePersister.findByIdExterneAndIdTiers(idExterne, idTiers, domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public List<Echeance> findByIdExterneAndIdTiersAndType(String idExterne, String idTiers, EcheanceType type,
            EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(type, "type");
        List<Echeance> list = echeancePersister.findByIdExterneAndIdTiersAndType(idExterne, idTiers,
                type.getCodeSysteme(), domaine.getCodeSysteme());
        resolveEtat(list);
        return list;
    }

    @Override
    public Echeance changeEtatToAnnule(String id) throws PrestationCommonException {
        Checkers.checkNotNull(id, "idEcheance");
        Echeance echeance = updateEtatAndAddDateTraitement(id, EcheanceEtat.ANNULEE);
        return echeance;
    }

    @Override
    public Echeance toggleEtatToTraiteeOrPlanifiee(String id) throws PrestationCommonException {
        Checkers.checkNotNull(id, "idEcheance");
        Echeance echeance = read(id);
        toggleEtat(echeance);
        echeance = update(echeance);
        echeance.setEtat(EcheanceEtatResolver.resolve(echeance));

        return echeance;
    }

    @Override
    public Echeance findNearestTerm(String idExterne, String idTiers, EcheanceType type, EcheanceDomaine domaine) {
        Checkers.checkNotNull(idExterne, "idExterne");
        Checkers.checkNotNull(domaine, "echeanceDomaine");
        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(type, "type");
        Echeance echeance = echeancePersister.findNearestTerm(idExterne, idTiers, type.getCodeSysteme(),
                domaine.getCodeSysteme());
        if (echeance != null) {
            echeance.setEtat(EcheanceEtatResolver.resolve(echeance));
        }
        return echeance;
    }

    void toggleEtat(Echeance echeance) {
        if (echeance.getEtat().equals(EcheanceEtat.TRAITEE)) {
            echeance.setEtat(EcheanceEtat.PLANIFIEE);
            echeance.setDateTraitement(null);
        } else {
            echeance.setEtat(EcheanceEtat.TRAITEE);
            echeance.setDateTraitement(new Date());
        }
    }

    @Override
    public Echeance changeEtatToTraitee(String id) throws PrestationCommonException {
        Checkers.checkNotNull(id, "idEcheance");
        Echeance echeance = updateEtatAndAddDateTraitement(id, EcheanceEtat.TRAITEE);
        return echeance;
    }

    private void resolveEtat(List<Echeance> list) {
        for (Echeance echeance : list) {
            echeance.setEtat(EcheanceEtatResolver.resolve(echeance));
        }
    }

    private Echeance updateEtatAndAddDateTraitement(String id, EcheanceEtat echeanceEtat)
            throws PrestationCommonException {
        Checkers.checkNotNull(id, "idEcheance");
        Checkers.checkNotNull(echeanceEtat, "echeanceEtat");
        Echeance echeance = read(id);
        echeance.setEtat(echeanceEtat);
        echeance.setDateTraitement(new Date());
        echeance = update(echeance);
        return echeance;
    }

    @Override
    public Echeance add(Echeance echeance) throws PrestationCommonException {
        return echeancePersister.add(echeance);
    }

    @Override
    public boolean delete(Echeance echeance) throws PrestationCommonException {
        return echeancePersister.remove(echeance);
    }

    @Override
    public boolean deleteById(Integer id) throws PrestationCommonException {
        Checkers.checkNotNull(id, "idEcheance");
        if ((id == null)) {
            return false;
        }
        return echeancePersister.remove(String.valueOf(id));
    }

    @Override
    public Echeance read(String id) throws PrestationCommonException {
        return echeancePersister.read(id);
    }

    @Override
    public Echeance update(Echeance echeance) throws PrestationCommonException {
        return echeancePersister.update(echeance);
    }

}
