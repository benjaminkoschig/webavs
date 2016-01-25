package ch.globaz.al.businessimpl.services.copies;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import ch.globaz.al.business.exceptions.copies.ALCopieBusinessException;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;
import ch.globaz.al.business.models.dossier.CopieSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.copies.CopiesBusinessService;
import ch.globaz.al.businessimpl.copies.libelles.ContextLibellesCopiesLoader;
import ch.globaz.al.businessimpl.copies.libelles.LibelleCopieLoaderFactory;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service de gestion de copies
 * 
 * @author jts
 * 
 */
public class CopiesBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements CopiesBusinessService {

    @Override
    public void createDefaultCopies(DossierComplexModel dossier, String typeCopie) throws JadeApplicationException,
            JadePersistenceException {

        // Recherche
        CopieComplexSearchModel search = new CopieComplexSearchModel();
        search.setForIdDossier(dossier.getId());
        search.setForTypeCopie(typeCopie);

        // Si aucune copie n'a été trouvée chargement et enregistrement des
        // copies par défaut
        if (ALServiceLocator.getCopieComplexModelService().count(search) == 0) {

            ArrayList<CopieComplexModel> liste = ALImplServiceLocator.getCopiesService().loadDefaultCopies(dossier,
                    typeCopie);

            for (int i = 0; i < liste.size(); i++) {
                ALServiceLocator.getCopieComplexModelService().create(liste.get(i));

            }
        }
    }

    @Override
    public void deleteForDossier(String idDossier, String type) throws JadePersistenceException {

        CopieSearchModel search = new CopieSearchModel();
        search.setForIdDossier(idDossier);
        search.setForTypeCopie(type);

        JadePersistenceManager.delete(search);
    }

    @Override
    public String getLibelleCopie(DossierComplexModel dossier, String idTiersDestinataire, String typeCopie)
            throws JadePersistenceException, JadeApplicationException {

        if (dossier == null) {
            throw new ALCopieBusinessException(
                    "CopieBusinessServiceImpl#getLibelleCopie: unable to get libelle - dossier is null");
        }

        if (JadeStringUtil.isEmpty(idTiersDestinataire)) {
            throw new ALCopieBusinessException(
                    "CopieBusinessServiceImpl#getLibelleCopie: unable to get libelle - idTiersDestinataire is empty");
        }

        if (JadeStringUtil.isEmpty(typeCopie)) {
            throw new ALCopieBusinessException(
                    "CopieBusinessServiceImpl#getLibelleCopie: unable to get libelle - typeCopie is empty");
        }

        return LibelleCopieLoaderFactory.getLibelleCopieLoader(
                ContextLibellesCopiesLoader.getInstance(dossier, typeCopie, idTiersDestinataire)).getLibelle();
    }

    @Override
    public CopieSearchModel searchForDossier(String idDossier, String type) throws JadePersistenceException {
        CopieSearchModel search = new CopieSearchModel();
        search.setForIdDossier(idDossier);
        search.setForTypeCopie(type);

        return (CopieSearchModel) JadePersistenceManager.search(search);
    }
}
