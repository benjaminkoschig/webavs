package ch.globaz.al.businessimpl.services.copies;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.exceptions.copies.ALCopieBusinessException;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.copies.CopiesService;
import ch.globaz.al.businessimpl.copies.adresse.ContextIdTiersAdresseCopiesLoader;
import ch.globaz.al.businessimpl.copies.adresse.IdTiersAdresseLoaderFactory;
import ch.globaz.al.businessimpl.copies.defaut.ContextDefaultCopiesLoader;
import ch.globaz.al.businessimpl.copies.defaut.DefaultCopiesLoaderFactory;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de copies
 * 
 * @author jts
 * 
 */
public class CopiesServiceImpl extends ALAbstractBusinessServiceImpl implements CopiesService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.copies.CopiesService#getIdTiersAdresse
     * (ch.globaz.al.business.models.dossier.DossierComplexModel, java.lang.String, java.lang.String)
     */
    @Override
    public String getIdTiersAdresse(DossierComplexModel dossier, String typeCopie, String idTiers)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCopieBusinessException("CopiesBusinessServiceImpl#getIdTiersAdresse : dossier is null");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSCopie.GROUP_COPIE_TYPE, typeCopie)) {
                throw new ALCopieBusinessException("CopiesBusinessServiceImpl#getIdTiersAdresse : '" + typeCopie
                        + "' is not a valid type of copie");
            }
        } catch (Exception e) {
            throw new ALCopieBusinessException(
                    "CopiesBusinessServiceImpl#getIdTiersAdresse : unable to check the type of copie", e);
        }

        if (JadeNumericUtil.isEmptyOrZero(idTiers)) {
            throw new ALCopieBusinessException("CopiesBusinessServiceImpl#getIdTiersAdresse : idTiers ('" + idTiers
                    + "') is not a valid number");
        }

        return IdTiersAdresseLoaderFactory.getIdTiersAdresseLoader(
                ContextIdTiersAdresseCopiesLoader.getInstance(dossier, typeCopie, idTiers)).getIdTiersAdresse();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.copies.CopiesService#loadDefaultCopies
     * (ch.globaz.al.business.models.dossier.DossierComplexModel, java.lang.String)
     */
    @Override
    public ArrayList<CopieComplexModel> loadDefaultCopies(DossierComplexModel dossier, String typeCopie)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCopieBusinessException("CopiesBusinessServiceImpl#loadDefaultCopies : dossier is null");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSCopie.GROUP_COPIE_TYPE, typeCopie)) {
                throw new ALCopieBusinessException("CopiesBusinessServiceImpl#loadDefaultCopies : '" + typeCopie
                        + "' is not a valid type of copie");
            }
        } catch (Exception e) {
            throw new ALCopieBusinessException(
                    "CopiesBusinessServiceImpl#loadDefaultCopies : unable to check the type of copie", e);
        }

        return DefaultCopiesLoaderFactory.getDefaultCopiesLoader(
                ContextDefaultCopiesLoader.getInstance(dossier, typeCopie)).getListCopies();
    }
}
