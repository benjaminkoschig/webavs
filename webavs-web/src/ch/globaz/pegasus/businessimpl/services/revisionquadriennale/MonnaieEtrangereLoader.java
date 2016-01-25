package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangereSearch;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangere;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class MonnaieEtrangereLoader {

    public MonnaiesEtrangere load() {
        List<ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere> list = search();
        MonnaiesEtrangere monnaiesEtrangere = convertAll(list);
        return monnaiesEtrangere;
    }

    private MonnaiesEtrangere convertAll(List<ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere> list) {
        MonnaiesEtrangere monnaiesEtrangere = new MonnaiesEtrangere();
        for (ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere monnaieEtrangere : list) {
            monnaiesEtrangere.add(convert(monnaieEtrangere.getSimpleMonnaieEtrangere()));
        }
        return monnaiesEtrangere;
    }

    private List<ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere> search() {
        MonnaieEtrangereSearch search = new MonnaieEtrangereSearch();
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            search = PegasusServiceLocator.getMonnaieEtrangereService().search(search);
        } catch (MonnaieEtrangereException e) {
            new RuntimeException(e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            new RuntimeException(e);
        } catch (JadePersistenceException e) {
            new RuntimeException(e);
        }
        return PersistenceUtil.typeSearch(search);
    }

    public MonnaieEtrangere convert(SimpleMonnaieEtrangere simpleMonnaieEtrangere) {
        MonnaieEtrangere monnaieEtrangere = new MonnaieEtrangere();
        monnaieEtrangere.setDateDebut(new Date(simpleMonnaieEtrangere.getDateDebut()));
        if (simpleMonnaieEtrangere.getDateFin() != null && !simpleMonnaieEtrangere.getDateFin().trim().isEmpty()
                && !simpleMonnaieEtrangere.getDateFin().equals("0")) {
            monnaieEtrangere.setDateFin(new Date(simpleMonnaieEtrangere.getDateFin()));
        }
        monnaieEtrangere.setId(simpleMonnaieEtrangere.getId());
        monnaieEtrangere.setTaux(new Taux(simpleMonnaieEtrangere.getTaux()));
        monnaieEtrangere.setType(MonnaieEtrangereType.fromValue(simpleMonnaieEtrangere.getCsTypeMonnaie()));
        return monnaieEtrangere;
    }

}
