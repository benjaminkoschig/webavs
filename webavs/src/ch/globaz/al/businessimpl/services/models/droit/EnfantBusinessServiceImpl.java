package ch.globaz.al.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.business.ALEnfantBusinessException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.droit.EnfantBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services liés à l'enfant
 * 
 * @author GMO
 * 
 */
public class EnfantBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements EnfantBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantBusinessService#isActif (java.lang.String)
     */
    @Override
    public int getNombreDroitsActifs(String idEnfant) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnfant)) {
            throw new ALEnfantBusinessException(
                    "EnfantBusinessServiceImpl#getNombreDroitsActifs : idEnfant is null or empty");
        }

        int nbDroitsActifs = 0;
        // on recherche tous les droits existants liés à l'enfant
        DroitComplexSearchModel droitComplexSearch = new DroitComplexSearchModel();
        droitComplexSearch.setForIdEnfant(idEnfant);
        droitComplexSearch = ALServiceLocator.getDroitComplexModelService().search(droitComplexSearch);
        // pour chaque droit lié à l'enfant, on teste si ils sont actifs, si oui
        // on les compte
        for (int i = 0; i < droitComplexSearch.getSearchResults().length; i++) {
            // on prend en compte que les droits enfant et formation pour voir
            // si enfant utilisé "à double"

            DossierModel dossier = ALServiceLocator.getDossierModelService().read(
                    ((DroitComplexModel) droitComplexSearch.getSearchResults()[i]).getDroitModel().getIdDossier());
            // on prend en compte que les droits dans les dossiers actifs
            if (ALCSDossier.ETAT_ACTIF.equals(dossier.getEtatDossier())) {
                if ((ALCSDroit.TYPE_ENF.equals(((DroitComplexModel) droitComplexSearch.getSearchResults()[i])
                        .getDroitModel().getTypeDroit()) || ALCSDroit.TYPE_FORM
                        .equals(((DroitComplexModel) droitComplexSearch.getSearchResults()[i]).getDroitModel()
                                .getTypeDroit()))
                        && ALServiceLocator.getDroitBusinessService().isDroitActif(
                                ((DroitComplexModel) droitComplexSearch.getSearchResults()[i]).getDroitModel(),
                                JadeDateUtil.getGlobazFormattedDate(new Date()))) {
                    nbDroitsActifs++;
                }
            }

        }
        return nbDroitsActifs;

    }

    /**
     * cette méthode permet de retrouver le nombre de droits de type formations qui sont actifs
     */
    @Override
    public int getNombreDroitsFormationActifs(String idTiersEnfant) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idTiersEnfant)) {
            throw new ALEnfantBusinessException(
                    "EnfantBusinessServiceImpl#getNombreDroitsActifs : idEnfant is null or empty");
        }

        int nbDroitsActifs = 0;
        // on recherche tous les droits existants liés à l'enfant
        DroitComplexSearchModel droitComplexSearch = new DroitComplexSearchModel();
        droitComplexSearch.setForIdTiersEnfant(idTiersEnfant);
        droitComplexSearch.setForTypeDroit(ALCSDroit.TYPE_FORM);
        droitComplexSearch = ALServiceLocator.getDroitComplexModelService().search(droitComplexSearch);
        // pour chaque droit lié à l'enfant, on teste si ils sont actifs, si oui
        // on les compte
        for (int i = 0; i < droitComplexSearch.getSearchResults().length; i++) {
            // on prend en compte que les droits enfant et formation pour voir
            // si enfant utilisé "à double"

            DossierModel dossier = ALServiceLocator.getDossierModelService().read(
                    ((DroitComplexModel) droitComplexSearch.getSearchResults()[i]).getDroitModel().getIdDossier());
            // on prend en compte que les droits dans les dossiers actifs
            if (ALCSDossier.ETAT_ACTIF.equals(dossier.getEtatDossier())) {
                if (ALServiceLocator.getDroitBusinessService().isDroitActif(
                        ((DroitComplexModel) droitComplexSearch.getSearchResults()[i]).getDroitModel(),
                        JadeDateUtil.getGlobazFormattedDate(new Date()))) {
                    nbDroitsActifs++;
                }
            }

        }
        return nbDroitsActifs;

    }
}
