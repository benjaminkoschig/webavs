package ch.globaz.pyxis.loader;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
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

    private Map<String, Pays> mapPaysByid = new HashMap<String, Pays>();
    private Map<String, Pays> mapPaysByCodeCentrale = new HashMap<String, Pays>();

    private AdresseService adresseService;
    private BSession session;
    private boolean isContextextInitlisied = false;

    PaysLoader(AdresseService adresseService) {
        this.adresseService = adresseService;
        loadPays();
    }

    public PaysLoader() {
        try {
            initContext();
            adresseService = TIBusinessServiceLocator.getAdresseService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } finally {
            if (isContextextInitlisied) {
                stopContext();
            }
        }
        loadPays();
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public Pays resolveById(String idPays) {
        Pays pays = new Pays();
        if (mapPaysByid.containsKey(idPays)) {
            pays = mapPaysByid.get(idPays);
        }
        return pays;
    }

    public Map<String, Pays> getMapPaysByid() {
        return mapPaysByid;
    }

    public Map<String, Pays> getMapPaysByCodeCentrale() {
        return mapPaysByCodeCentrale;
    }

    public Pays resolveByCodeCentrale(String code) {
        Pays pays = new Pays();
        if (mapPaysByCodeCentrale.containsKey(code)) {
            pays = mapPaysByCodeCentrale.get(code);
        }
        return pays;
    }

    void group(List<PaysSimpleModel> listPays) {
        Map<String, Pays> map = new HashMap<String, Pays>();
        for (PaysSimpleModel paysSimpleModel : listPays) {
            Pays pays = convert(paysSimpleModel);
            mapPaysByid.put(paysSimpleModel.getId(), pays);
            mapPaysByCodeCentrale.put(paysSimpleModel.getCodeCentrale(), pays);
        }
    }

    static Pays convert(PaysSimpleModel paysSimpleModel) {
        Pays pays = new Pays();
        pays.setId(Long.valueOf(paysSimpleModel.getId()));

        // on fait se test car on ne veut pas tout arrêter si le code iso n'est pas juste.
        if (paysSimpleModel.getCodeIso() != null && paysSimpleModel.getCodeIso().trim().length() == 2) {
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

    private void stopContext() {
        if (session != null && adresseService != null) {
            BSessionUtil.stopUsingContext(this);
        }
    }

    private void initContext() {
        if (session != null && adresseService != null) {
            isContextextInitlisied = true;
            try {
                BSessionUtil.initContext(session, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadPays() {
        if (mapPaysByid.isEmpty()) {
            List<PaysSimpleModel> listPays = findAllPays();
            group(listPays);
        }
    }
}
