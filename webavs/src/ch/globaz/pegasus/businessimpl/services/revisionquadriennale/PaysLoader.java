package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;

public class PaysLoader {

    Map<String, Pays> mapPays = new HashMap<String, Pays>();;

    AdresseService adresseService;

    public PaysLoader() {
        try {
            adresseService = TIBusinessServiceLocator.getAdresseService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            e.printStackTrace();
        }
    }

    PaysLoader(AdresseService adresseService) {
        this.adresseService = adresseService;
    }

    public Pays loadAndGetPaysById(String idPays) {
        Pays pays = new Pays();

        if (mapPays.isEmpty()) {
            mapPays = loadAndGroupByIdPays();
        }

        if (mapPays.containsKey(idPays)) {
            pays = mapPays.get(idPays);
        } else {
            pays = new Pays();
        }
        return pays;

    }

    public Map<String, Pays> loadAndGroupByIdPays() {
        Map<String, Pays> map = new HashMap<String, Pays>();
        List<PaysSimpleModel> findAllPays = findAllPays();
        for (PaysSimpleModel paysSimpleModel : findAllPays) {
            map.put(paysSimpleModel.getId(), convert(paysSimpleModel));
        }
        return map;
    }

    Pays convert(PaysSimpleModel paysSimpleModel) {
        Pays pays = new Pays();
        pays.setId(Long.valueOf(paysSimpleModel.getId()));
        if (paysSimpleModel.getCodeIso() != null && paysSimpleModel.getCodeIso().trim().length() > 0) {
            pays.setCodeIso(CodeIsoPays.parse(paysSimpleModel.getCodeIso()));
        } else {
            pays.setCodeIso(CodeIsoPays.INCONNU);
        }
        Map<Langues, String> traductions = new HashMap<Langues, String>();

        if (paysSimpleModel.getLibelleAl() != null) {
            traductions.put(Langues.Allemand, paysSimpleModel.getLibelleAl());
        }
        if (paysSimpleModel.getLibelleFr() != null) {
            traductions.put(Langues.Francais, paysSimpleModel.getLibelleFr());
        }
        if (paysSimpleModel.getLibelleIt() != null) {
            traductions.put(Langues.Italien, paysSimpleModel.getLibelleIt());
        }

        pays.setTraductionParLangue(traductions);
        return pays;
    }

    List<PaysSimpleModel> findAllPays() {
        try {
            PaysSearchSimpleModel search = new PaysSearchSimpleModel();
            search.setForLibelleAlUpperLike("%%");
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = adresseService.findPays(search);
            if (search.getSearchResults() != null || search.getSize() > 0) {
                return PersistenceUtil.typeSearch(search);
            } else {
                return new ArrayList<PaysSimpleModel>();
            }
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        } catch (JadeApplicationException e) {
            throw new RuntimeException(e);
        }
    }
}
