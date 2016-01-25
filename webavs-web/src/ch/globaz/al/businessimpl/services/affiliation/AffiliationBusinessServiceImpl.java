package ch.globaz.al.businessimpl.services.affiliation;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.affiliations.ALAffiliationException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierComplexModelException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.affiliation.AffiliationBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * Implémentation du service métier lié aux affiliation
 * 
 * @author jts
 * 
 */
public class AffiliationBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements AffiliationBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.affiliation.AffiliationService#
     * getAssuranceInfo(ch.globaz.al.business.models.dossier.DossierModel, java.lang.String)
     */
    @Override
    public AssuranceInfo getAssuranceInfo(DossierModel model, String date) throws JadePersistenceException,
            JadeApplicationException {

        if (model == null) {
            throw new ALAffiliationException(
                    "AffiliationServiceImpl#getAssuranceInfo : Unable to retrieve model (AssuranceInfo) - the model passed is null!");
        }

        if (JadeStringUtil.isEmpty(date)) {
            date = JACalendar.todayJJsMMsAAAA();
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDossierComplexModelException("AffiliationServiceImpl#getAssuranceInfo :" + date
                    + "is not a valid date");
        }

        // TODO (lot 2) on ne doit pas passer l'activite de l'allocataire
        // selon genre affiliation et/ou branche economique, on détermine les
        // coti à avoir pour être actif
        // TODO (lot 2) gérer les codes dans service affiliation
        String activite = "";
        if (ALCSDossier.ACTIVITE_SALARIE.equals(model.getActiviteAllocataire())) {
            activite = "S";
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(model.getActiviteAllocataire())) {
            activite = "I";
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(model.getActiviteAllocataire())) {
            activite = "N";
        } else if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(model.getActiviteAllocataire())) {
            activite = "A";
        } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(model.getActiviteAllocataire())) {
            activite = "T";
        } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(model.getActiviteAllocataire())) {
            activite = "C";
        } else if (ALCSDossier.ACTIVITE_PECHEUR.equals(model.getActiviteAllocataire())) {
            activite = "P";
        } else if (ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE.equals(model.getActiviteAllocataire())) {
            activite = "A";
        } else if (ALCSDossier.ACTIVITE_TSE.equals(model.getActiviteAllocataire())) {
            activite = "";
        } else if (ALCSDossier.ACTIVITE_VIGNERON.equals(model.getActiviteAllocataire())) {
            activite = "";
        }

        else {
            throw new ALAffiliationException("AffiliationServiceImpl#getAssuranceInfo : the activity type "
                    + model.getActiviteAllocataire() + " is not supported");
        }

        return AFBusinessServiceLocator.getAffiliationService().getAssuranceInfoAF(model.getNumeroAffilie(), date,
                activite);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.affiliation.AffiliationService#
     * getAssuranceInfo(ch.globaz.al.business.models.dossier.DossierModel, java.lang.String)
     */
    @Override
    public AssuranceInfo getAssuranceInfo(String numAffilie, String activite, String date)
            throws JadePersistenceException, JadeApplicationException {

        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALAffiliationException("AffiliationServiceImpl#getAssuranceInfo :  numAffilie passed is empty!");
        }
        if (JadeStringUtil.isEmpty(activite)) {
            throw new ALAffiliationException("AffiliationServiceImpl#getAssuranceInfo :  activite passed is empty!");
        }

        if (JadeStringUtil.isEmpty(date)) {
            date = JACalendar.todayJJsMMsAAAA();
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDossierComplexModelException("AffiliationServiceImpl#getAssuranceInfo :" + date
                    + "is not a valid date");
        }

        // TODO (lot 2) on ne doit pas passer l'activite de l'allocataire
        // selon genre affiliation et/ou branche economique, on détermine les
        // coti à avoir pour être actif
        // TODO (lot 2) gérer les codes dans service affiliation
        if (ALCSDossier.ACTIVITE_SALARIE.equals(activite)) {
            activite = "S";
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(activite)) {
            activite = "I";
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(activite)) {
            activite = "N";
        } else if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(activite)) {
            activite = "A";
        } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(activite)) {
            activite = "T";
        } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(activite)) {
            activite = "C";
        } else if (ALCSDossier.ACTIVITE_PECHEUR.equals(activite)) {
            activite = "P";
        } else if (ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE.equals(activite)) {
            activite = "A";
        } else if (ALCSDossier.ACTIVITE_TSE.equals(activite)) {
            activite = "";
        } else {
            throw new ALAffiliationException("AffiliationServiceImpl#getAssuranceInfo : the activity type " + activite
                    + " is not supported");
        }

        return AFBusinessServiceLocator.getAffiliationService().getAssuranceInfoAF(numAffilie, date, activite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.affiliation.AffiliationBusinessService#requireDocumentLienAgenceCommunale()
     */
    @Override
    public boolean requireDocumentLienAgenceCommunale() throws JadePersistenceException, JadeApplicationException {
        String nomCaisse = ALServiceLocator.getParametersServices().getNomCaisse();
        if ("CCJU".equals(nomCaisse)) {
            return true;
        } else {
            return false;
        }
    }
}
