package ch.globaz.al.businessimpl.services.models.allocataire;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.business.ALAllocataireBusinessException;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.business.services.models.allocataire.AllocataireBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe d'implémentation des service de AllocataireBusinessService
 * 
 * @author GMO/PTA/JTS
 * 
 */
public class AllocataireBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements AllocataireBusinessService {

    @Override
    public String getTypeResident(AllocataireModel allocataire) throws JadeApplicationException {
        if (allocataire == null) {
            throw new ALAllocataireBusinessException(
                    "AllocataireBusinessServiceImpl#getTypeResident : allocataire is null");
        }

        String permis = allocataire.getPermis();
        String paysResidence = allocataire.getIdPaysResidence();

        // si le permis n'est pas vide et est de type frontalier
        if (!JadeStringUtil.isEmpty(permis) && ALCSAllocataire.PERMIS_G.equals(permis)) {
            return ALCSTarif.RESIDENT_CH;
            // si le pays de résidence est la Suisse
        } else if (ALCSPays.PAYS_SUISSE.equals(paysResidence)) {
            return ALCSTarif.RESIDENT_CH;
        } else if (JadeStringUtil.isEmpty(paysResidence)) {
            throw new ALAllocataireBusinessException(
                    "AllocataireBusinessServiceImpl#getTypeResident : unable to get the type, 'paysResidence' is empty");
        } else {
            return ALCSTarif.RESIDENT_ETR;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireBusinessService #isActif(java.lang.String)
     */
    @Override
    public int isActif(String idAllocataire) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idAllocataire)) {
            throw new ALAllocataireBusinessException(
                    "AllocataireBusinessServiceImpl#isActif : idAllocataire is null or empty");
        }

        DossierFkSearchModel dossierFkSearch = new DossierFkSearchModel();
        dossierFkSearch.setForIdAllocataire(idAllocataire);
        dossierFkSearch.setForEtatDossier(ALCSDossier.ETAT_ACTIF);
        return ALImplServiceLocator.getDossierFkModelService().count(dossierFkSearch);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireBusinessService #isAgricole(java.lang.String)
     */
    @Override
    public boolean isAgricole(String idAllocataire) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idAllocataire)) {
            throw new ALAllocataireBusinessException(
                    "AllocataireBusinessServiceImpl#isAgricole : idAllocataire is null or empty");
        }

        AgricoleSearchModel agricoleSearch = new AgricoleSearchModel();
        agricoleSearch.setForIdAllocataire(idAllocataire);
        return (ALImplServiceLocator.getAgricoleModelService().count(agricoleSearch) > 0) ? true : false;
    }
}