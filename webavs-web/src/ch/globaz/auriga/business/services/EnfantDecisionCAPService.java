package ch.globaz.auriga.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCAPTiers;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCAPTiersSearchModel;
import ch.globaz.auriga.business.models.EnfantDecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleEnfantDecisionCAP;

public interface EnfantDecisionCAPService extends JadeApplicationService {
    public SimpleEnfantDecisionCAP create(SimpleEnfantDecisionCAP enfantDecisionCap) throws JadePersistenceException;

    public SimpleEnfantDecisionCAP delete(SimpleEnfantDecisionCAP enfantDecisionCap) throws JadePersistenceException;

    public List<ComplexEnfantDecisionCAPTiers> getListComplexEnfantDecisionCapTiers(String idDecision)
            throws JadePersistenceException;

    public List<SimpleEnfantDecisionCAP> getListEnfantDecisionCap(String idDecision) throws JadePersistenceException;

    public List<String> getListIdEnfantDecisionCap(String idDecision) throws JadePersistenceException;

    public SimpleEnfantDecisionCAP read(String idEnfantDecisionCap) throws JadePersistenceException;

    public ComplexEnfantDecisionCAPTiersSearchModel search(ComplexEnfantDecisionCAPTiersSearchModel complexSearchModel)
            throws JadePersistenceException;

    public EnfantDecisionCAPSearchModel search(EnfantDecisionCAPSearchModel searchModel)
            throws JadePersistenceException;

    public SimpleEnfantDecisionCAP update(SimpleEnfantDecisionCAP enfantDecisionCap) throws JadePersistenceException;
}
