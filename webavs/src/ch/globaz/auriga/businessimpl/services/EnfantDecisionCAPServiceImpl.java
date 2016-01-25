package ch.globaz.auriga.businessimpl.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCAPTiers;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCAPTiersSearchModel;
import ch.globaz.auriga.business.models.EnfantDecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleEnfantDecisionCAP;
import ch.globaz.auriga.business.services.EnfantDecisionCAPService;

public class EnfantDecisionCAPServiceImpl implements EnfantDecisionCAPService {
    @Override
    public SimpleEnfantDecisionCAP create(SimpleEnfantDecisionCAP enfantDecisionCap) throws JadePersistenceException {
        return (SimpleEnfantDecisionCAP) JadePersistenceManager.add(enfantDecisionCap);
    }

    @Override
    public SimpleEnfantDecisionCAP delete(SimpleEnfantDecisionCAP enfantDecisionCap) throws JadePersistenceException {
        return (SimpleEnfantDecisionCAP) JadePersistenceManager.delete(enfantDecisionCap);
    }

    @Override
    public List<ComplexEnfantDecisionCAPTiers> getListComplexEnfantDecisionCapTiers(String idDecision)
            throws JadePersistenceException {
        List<ComplexEnfantDecisionCAPTiers> listEnfantDecisionCapTiers = new ArrayList<ComplexEnfantDecisionCAPTiers>();

        ComplexEnfantDecisionCAPTiersSearchModel complexEnfantDecisionCAPTiersSearchModel = new ComplexEnfantDecisionCAPTiersSearchModel();
        complexEnfantDecisionCAPTiersSearchModel.setForIdDecision(idDecision);
        complexEnfantDecisionCAPTiersSearchModel = this.search(complexEnfantDecisionCAPTiersSearchModel);

        if (complexEnfantDecisionCAPTiersSearchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : complexEnfantDecisionCAPTiersSearchModel.getSearchResults()) {
                ComplexEnfantDecisionCAPTiers complexEnfantDecisionCapTiers = (ComplexEnfantDecisionCAPTiers) abstractModel;
                listEnfantDecisionCapTiers.add(complexEnfantDecisionCapTiers);
            }
        }

        return listEnfantDecisionCapTiers;
    }

    @Override
    public List<SimpleEnfantDecisionCAP> getListEnfantDecisionCap(String idDecision) throws JadePersistenceException {
        List<SimpleEnfantDecisionCAP> listEnfantDecisionCap = new ArrayList<SimpleEnfantDecisionCAP>();

        EnfantDecisionCAPSearchModel enfantDecisionCAPSearchModel = new EnfantDecisionCAPSearchModel();
        enfantDecisionCAPSearchModel.setForIdDecision(idDecision);
        enfantDecisionCAPSearchModel = this.search(enfantDecisionCAPSearchModel);

        if (enfantDecisionCAPSearchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : enfantDecisionCAPSearchModel.getSearchResults()) {
                SimpleEnfantDecisionCAP enfantDecisionCap = (SimpleEnfantDecisionCAP) abstractModel;
                listEnfantDecisionCap.add(enfantDecisionCap);
            }
        }

        return listEnfantDecisionCap;
    }

    @Override
    public List<String> getListIdEnfantDecisionCap(String idDecision) throws JadePersistenceException {

        List<SimpleEnfantDecisionCAP> listEnfantDecisionCap = getListEnfantDecisionCap(idDecision);

        List<String> listIdEnfantDecisionCap = new ArrayList<String>();

        for (SimpleEnfantDecisionCAP aEnfantDecisionCap : listEnfantDecisionCap) {
            listIdEnfantDecisionCap.add(aEnfantDecisionCap.getIdTiers());
        }

        return listIdEnfantDecisionCap;

    }

    @Override
    public SimpleEnfantDecisionCAP read(String idEnfantDecisionCap) throws JadePersistenceException {
        SimpleEnfantDecisionCAP enfantDecisionCap = new SimpleEnfantDecisionCAP();
        enfantDecisionCap.setIdDecision(idEnfantDecisionCap);
        return (SimpleEnfantDecisionCAP) JadePersistenceManager.read(enfantDecisionCap);
    }

    @Override
    public ComplexEnfantDecisionCAPTiersSearchModel search(ComplexEnfantDecisionCAPTiersSearchModel complexSearchModel)
            throws JadePersistenceException {
        return (ComplexEnfantDecisionCAPTiersSearchModel) JadePersistenceManager.search(complexSearchModel);
    }

    @Override
    public EnfantDecisionCAPSearchModel search(EnfantDecisionCAPSearchModel searchModel)
            throws JadePersistenceException {
        return (EnfantDecisionCAPSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleEnfantDecisionCAP update(SimpleEnfantDecisionCAP enfantDecisionCap) throws JadePersistenceException {
        return (SimpleEnfantDecisionCAP) JadePersistenceManager.update(enfantDecisionCap);
    }

}
