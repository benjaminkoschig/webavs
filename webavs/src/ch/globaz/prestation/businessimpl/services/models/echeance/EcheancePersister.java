package ch.globaz.prestation.businessimpl.services.models.echeance;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceEtat;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.echance.EcheanceModel;
import ch.globaz.prestation.business.models.echance.EcheanceSearch;
import ch.globaz.prestation.business.models.echance.SimpleEcheance;
import ch.globaz.prestation.business.models.echance.SimpleEcheanceSearch;
import ch.globaz.prestation.businessimpl.services.PrestationCommonImplServiceLocator;

public class EcheancePersister extends EcheanceConverter {

    private List<Echeance> find(SimpleEcheanceSearch search) {
        try {
            search = (SimpleEcheanceSearch) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<Echeance> list = new ArrayList<Echeance>(search.getSize());
        if (search.getSize() > 0) {
            for (JadeAbstractModel model : search.getSearchResults()) {
                SimpleEcheance entity = (SimpleEcheance) model;
                list.add(this.fromPersistence(entity, null));
            }
        } else {
            return new ArrayList<Echeance>();
        }
        return list;
    }

    private List<Echeance> find(EcheanceSearch search) {
        try {
            search = (EcheanceSearch) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<Echeance> list = new ArrayList<Echeance>(search.getSize());
        if (search.getSize() > 0) {
            for (JadeAbstractModel model : search.getSearchResults()) {
                EcheanceModel entity = (EcheanceModel) model;
                list.add(this.fromPersistence(entity.getSimpleEcheance(), entity.getSimpleTiers()));
            }
        } else {
            return new ArrayList<Echeance>();
        }
        return list;
    }

    private Echeance read(Echeance echeance) {
        try {
            EcheanceModel model = new EcheanceModel();
            model.setId(echeance.getId());
            model = (EcheanceModel) JadePersistenceManager.read(model);
            echeance = this.fromPersistence(model.getSimpleEcheance(), model.getSimpleTiers());
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return echeance;
    }

    public Echeance read(String id) {
        Echeance echeance = null;
        try {
            EcheanceModel model = new EcheanceModel();
            model.setId(id);
            model = (EcheanceModel) JadePersistenceManager.read(model);
            echeance = this.fromPersistence(model.getSimpleEcheance(), model.getSimpleTiers());
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return echeance;
    }

    public List<Echeance> findByIdExterne(String idExterne, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForCsDomaine(csDomaine);
        return this.find(search);
    }

    public List<Echeance> findToTreatByIdExterne(String idExterne, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForCsDomaine(csDomaine);
        search.setForCsEtat(EcheanceEtat.PLANIFIEE.getCodeSysteme());
        search.setWhereKey(EcheanceSearch.FOR_A_TRAITER);
        return this.find(search);
    }

    public List<Echeance> findToTreatByIdExterneAndIdTiersAndType(String idExterne, String idTiers, String csType,
            String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForCsDomaine(csDomaine);
        search.setForIdTiers(idTiers);
        search.setForCsTypeEcheance(csType);
        search.setForCsEtat(EcheanceEtat.PLANIFIEE.getCodeSysteme());
        search.setWhereKey(EcheanceSearch.FOR_A_TRAITER);
        return this.find(search);
    }

    public List<Echeance> findByIdExternAndIdTiers(String idExterne, String idTiers, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForIdTiers(idTiers);
        search.setForCsDomaine(csDomaine);
        return this.find(search);
    }

    public Echeance add(Echeance echeance) throws PrestationCommonException {
        SimpleEcheance simpleEcheance = toPersistence(echeance);

        try {
            PrestationCommonImplServiceLocator.getSimpleEcheanceService().create(simpleEcheance);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrestationCommonException("The service for SimpelEcheance is not available echeance", e);
        } catch (JadeApplicationException e) {
            throw new PrestationCommonException("Unable to add the model echeance", e);
        } catch (JadePersistenceException e) {
            throw new PrestationCommonException("Unable to add the model echeance", e);
        }
        return this.fromPersistence(simpleEcheance);
    }

    public boolean remove(Echeance echeance) throws PrestationCommonException {
        SimpleEcheance simpleEcheance = toPersistence(echeance);
        try {
            simpleEcheance = (SimpleEcheance) JadePersistenceManager.delete(simpleEcheance);
        } catch (JadePersistenceException e) {
            throw new PrestationCommonException("Unable to delete the model echeance -> " + e, e);
        }
        return true;
    }

    public boolean remove(String id) throws PrestationCommonException {
        try {
            SimpleEcheance simpleEcheance = new SimpleEcheance();
            simpleEcheance.setId(id);
            simpleEcheance = (SimpleEcheance) JadePersistenceManager.delete(simpleEcheance);
        } catch (JadePersistenceException e) {
            throw new PrestationCommonException("Unable to delete the model echeance -> " + e, e);
        }
        return true;
    }

    public Echeance update(Echeance echeance) throws PrestationCommonException {
        SimpleEcheance simpleEcheance = toPersistence(echeance);
        try {
            PrestationCommonImplServiceLocator.getSimpleEcheanceService().update(simpleEcheance);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrestationCommonException("The service for SimpelEcheance is not available echeance", e);
        } catch (JadeApplicationException e) {
            throw new PrestationCommonException("Unable to update the model echeance", e);
        } catch (JadePersistenceException e) {
            throw new PrestationCommonException("Unable to update the model echeance", e);
        }
        return this.fromPersistence(simpleEcheance);
    }

    public List<Echeance> findByIdExterneAndIdTiers(String idExterne, String idTiers, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForIdTiers(idTiers);
        search.setForCsDomaine(csDomaine);
        return this.find(search);
    }

    public List<Echeance> findByIdExterneAndIdTiersAndType(String idExterne, String idTiers, String csType,
            String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForIdTiers(idTiers);
        search.setForCsTypeEcheance(csType);
        search.setForCsDomaine(csDomaine);
        return this.find(search);
    }

    public Echeance findNearestTerm(String idExterne, String idTiers, String csType, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForIdTiers(idTiers);
        search.setForCsTypeEcheance(csType);
        search.setForCsDomaine(csDomaine);
        search.setForCsEtat(EcheanceEtat.PLANIFIEE.getCodeSysteme());
        search.setWhereKey(EcheanceSearch.FOR_NEAREST_TERM);
        List<Echeance> list = this.find(search);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Echeance> findToTreatByIdExterneAndType(String idExterne, String csType, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForCsTypeEcheance(csType);
        search.setForCsEtat(EcheanceEtat.PLANIFIEE.getCodeSysteme());
        search.setForCsDomaine(csDomaine);
        search.setWhereKey(EcheanceSearch.FOR_A_TRAITER);
        return this.find(search);
    }

    public List<Echeance> findByIdExterneAndType(String idExterne, String csType, String csDomaine) {
        EcheanceSearch search = new EcheanceSearch();
        search.setForIdExterne(idExterne);
        search.setForCsTypeEcheance(csType);
        search.setForCsDomaine(csDomaine);
        return this.find(search);
    }
}
