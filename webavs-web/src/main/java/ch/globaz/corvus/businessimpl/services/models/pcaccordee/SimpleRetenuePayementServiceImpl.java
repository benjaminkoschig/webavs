package ch.globaz.corvus.businessimpl.services.models.pcaccordee;

import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.common.persistence.OldPersistenceToNew;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayementSearch;
import ch.globaz.corvus.business.services.models.pcaccordee.SimpleRetenuePayementService;
import ch.globaz.pyxis.common.Messages;

public class SimpleRetenuePayementServiceImpl extends
        OldPersistenceToNew<SimpleRetenuePayement, RERetenuesPaiement, SimpleRetenuePayementException> implements
        SimpleRetenuePayementService {

    @Override
    protected SimpleRetenuePayement _adapt(RERetenuesPaiement bean) {
        SimpleRetenuePayement model = new SimpleRetenuePayement(); // destination

        // Préconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new IllegalArgumentException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        model.setId(bean.getId());

        model.setMontantRetenuMensuel(bean.getMontantRetenuMensuel());
        model.setMontantDejaRetenu(bean.getMontantDejaRetenu());
        model.setMontantTotalARetenir(bean.getMontantTotalARetenir());
        model.setDateDebutRetenue(bean.getDateDebutRetenue());
        model.setDateFinRetenue(bean.getDateFinRetenue());
        model.setCsTypeRetenue(bean.getCsTypeRetenue());
        model.setIdExterne(bean.getIdExterne());
        model.setIdTiersAdressePmt(bean.getIdTiersAdressePmt());
        model.setIdTypeSection(bean.getIdTypeSection());
        model.setIdRenteAccordee(bean.getIdRenteAccordee());
        model.setNoFacture(bean.getNoFacture());
        model.setIdParentRetenue(bean.getIdParentRetenue());
        model.setRole(bean.getRole());

        // TODO check if spy from BEntity to jademodel spy
        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    @Override
    protected RERetenuesPaiement _adapt(SimpleRetenuePayement model) throws IllegalArgumentException {
        RERetenuesPaiement bean = new RERetenuesPaiement(); // destination

        // Préconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new IllegalArgumentException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());
        bean.setMontantRetenuMensuel(model.getMontantRetenuMensuel());
        bean.setMontantDejaRetenu(model.getMontantDejaRetenu());
        bean.setMontantTotalARetenir(model.getMontantTotalARetenir());
        bean.setDateDebutRetenue(model.getDateDebutRetenue());
        bean.setDateFinRetenue(model.getDateFinRetenue());
        bean.setReferenceInterne(model.getReferenceInterne());
        bean.setCsTypeRetenue(model.getCsTypeRetenue());
        bean.setIdExterne(model.getIdExterne());
        bean.setIdTiersAdressePmt(model.getIdTiersAdressePmt());
        bean.setIdTypeSection(model.getIdTypeSection());
        bean.setIdRenteAccordee(model.getIdRenteAccordee());
        bean.setNoFacture(model.getNoFacture());
        bean.setIdParentRetenue(model.getIdParentRetenue());
        bean.setRole(model.getRole());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    @Override
    public int count(SimpleRetenuePayementSearch search) throws SimpleRetenuePayementException,
            JadePersistenceException {
        if (search == null) {
            throw new SimpleRetenuePayementException("Unable to count simpleLot, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public void deleteByIdPrestationAccordee(String idPrestationAccordee) throws SimpleRetenuePayementException,
            JadePersistenceException {
        SimpleRetenuePayementSearch simpleRetenuePayementSearch = new SimpleRetenuePayementSearch();
        simpleRetenuePayementSearch.setForIdRente(idPrestationAccordee);
        simpleRetenuePayementSearch = search(simpleRetenuePayementSearch);
        for (JadeAbstractModel simpleRetenuePayement : simpleRetenuePayementSearch.getSearchResults()) {
            delete((SimpleRetenuePayement) simpleRetenuePayement);
        }
    }

    @Override
    public SimpleRetenuePayementSearch search(SimpleRetenuePayementSearch search) throws JadePersistenceException,
            SimpleRetenuePayementException {
        if (search == null) {
            throw new SimpleRetenuePayementException("Unable to search simpleLot, the search model passed is null!");
        }
        return (SimpleRetenuePayementSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleRetenuePayement read(String idSimpleRetenuePayement) throws JadePersistenceException,
            SimpleRetenuePayementException {
        if (JadeStringUtil.isEmpty(idSimpleRetenuePayement)) {
            throw new SimpleRetenuePayementException("Unable to read simpleLot, the id passed is null!");
        }
        SimpleRetenuePayement simpleRetenuePayement = new SimpleRetenuePayement();
        simpleRetenuePayement.setId(idSimpleRetenuePayement);
        return (SimpleRetenuePayement) JadePersistenceManager.read(simpleRetenuePayement);
    }

    @Override
    protected SimpleRetenuePayementException getException(String message, Exception e) {
        return new SimpleRetenuePayementException(message, e);
    }
}
