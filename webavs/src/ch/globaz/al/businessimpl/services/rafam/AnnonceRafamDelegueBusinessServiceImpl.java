package ch.globaz.al.businessimpl.services.rafam;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnonceRafamDelegueBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class AnnonceRafamDelegueBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamDelegueBusinessService {

    @Override
    public String[] getListeEmployeursDelegue() throws JadeApplicationException, JadePersistenceException {
        String listeDelegueProp = JadePropertiesService.getInstance().getProperty("al.rafam.delegue.index");
        String[] listeDelegueTab;
        String delimiter = ",";

        if (listeDelegueProp != null) {
            listeDelegueTab = listeDelegueProp.split(delimiter);
            return listeDelegueTab;
        } else {
            return null;
        }
    }

    @Override
    public boolean isAnnoncesInDb() throws JadeApplicationException, JadePersistenceException {
        // FIXME : pas d'autre solution que de charger les annonces pour avoir cette info ?
        AnnonceRafamSearchModel searchAnnoncesDelegue = new AnnonceRafamSearchModel();
        searchAnnoncesDelegue.setForDelegated(new Boolean(true));
        searchAnnoncesDelegue.setWhereKey("afDelegue");
        searchAnnoncesDelegue = ALServiceLocator.getAnnonceRafamModelService().search(searchAnnoncesDelegue);
        return searchAnnoncesDelegue.getSize() > 0 ? true : false;
    }

    @Override
    public void validationCafAnnonce(String idAnnonce) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idAnnonce)) {
            throw new ALRafamException("AnnonceRafamDelegueBusinessServiceImpl#validationCafAnnonce:idAnnonce is empty");
        }

        AnnonceRafamModel annonce = ALServiceLocator.getAnnonceRafamModelService().read(idAnnonce);
        annonce.setEtat(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
        ALServiceLocator.getAnnonceRafamModelService().update(annonce);

    }

    @Override
    public void validationCafAnnonces(String prefixEmployeur) throws JadeApplicationException, JadePersistenceException {

        if (prefixEmployeur.indexOf(ALConstRafam.PREFIX_INTERNAL_OFFICE_REFERENCE_ED) < 0) {
            throw new ALRafamException(
                    "AnnonceRafamDelegueBusinessServiceImpl#validationCafAnnonces: invalid parameter prefixEmployeur"
                            + prefixEmployeur);
        }

        AnnonceRafamSearchModel searchAnnoncesDelegue = new AnnonceRafamSearchModel();
        searchAnnoncesDelegue.setForDelegated(new Boolean(true));
        searchAnnoncesDelegue.setForEtatAnnonce(RafamEtatAnnonce.ENREGISTRE.getCS());
        searchAnnoncesDelegue.setLikeInternalOffice(prefixEmployeur);
        searchAnnoncesDelegue.setDefinedSearchSize(0);
        searchAnnoncesDelegue.setWhereKey("afDelegue");
        searchAnnoncesDelegue = ALServiceLocator.getAnnonceRafamModelService().search(searchAnnoncesDelegue);
        for (int i = 0; i < searchAnnoncesDelegue.getSize(); i++) {
            AnnonceRafamModel current = (AnnonceRafamModel) searchAnnoncesDelegue.getSearchResults()[i];
            current.setEtat(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
            ALServiceLocator.getAnnonceRafamModelService().update(current);

        }

    }

}
