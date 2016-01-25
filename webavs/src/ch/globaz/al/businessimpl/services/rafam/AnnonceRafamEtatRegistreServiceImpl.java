package ch.globaz.al.businessimpl.services.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamEtatRegistreModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnonceRafamEtatRegistreService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService;
import ch.globaz.al.businessimpl.checker.model.rafam.AnnonceRafamModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service de gestion de l'état du registre du RAFam
 * 
 * @author jts
 * @deprecated A partir de la version 1-11-00 l'état du registre est traité à l'importation
 */
@Deprecated
public class AnnonceRafamEtatRegistreServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamEtatRegistreService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamEtatRegistreService#processEtatRegistre(java.lang.String)
     */
    @Override
    public void cleanWithEtatRegistre(String recordNumber) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALRafamException("AnnonceRafamBusinessServiceImpl#processEtatRegistre : recordNumber is empty");
        }

        AnnoncesRafamSearchService s = ALImplServiceLocator.getAnnoncesRafamSearchService();

        AnnonceRafamModel annonceEtatReg = s.getAnnonceRegisterStatus(recordNumber);

        if ((annonceEtatReg != null) && !annonceEtatReg.isNew()) {

            AnnonceRafamModel annonce = s.getLastAnnonceForRecordNumber(annonceEtatReg.getRecordNumber());

            if ((annonce != null) && !annonce.isNew()) {
                if (!annonce.getRecordNumber().equals(annonceEtatReg.getRecordNumber())) {
                    throw new ALRafamException(
                            "AnnonceRafamEtatRegistreServiceImpl#cleanWithEtatRegistre : Les records numbers ne correspondent pas");
                } else {

                    // si annonce annulée à la centrale
                    if (annonceEtatReg.getCanceled()) {
                        // on vérifie que la dernière annonce est bien une annulation
                        if (RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce
                                .getTypeAnnonce()))) {
                            ALServiceLocator.getAnnonceRafamModelService().update(
                                    updateAnnonce(annonceEtatReg, annonce));
                            // si l'annonce WebAF n'est pas une annulation, création de l'annonce manquante
                        } else {
                            createMissing68c(annonceEtatReg, annonce);
                        }
                    } else {
                        ALServiceLocator.getAnnonceRafamModelService().update(updateAnnonce(annonceEtatReg, annonce));
                    }
                }
                // si aucune annonce de création ou de modification n'a été trouvée
            } else {
                createMissing68a(annonceEtatReg, annonce);
            }

            validate(annonceEtatReg);
        }
    }

    /**
     * 
     * @param annonceEtatReg
     * @param annonce
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @deprecated à usage temporaire pour la remise en état des annonces
     */
    @Deprecated
    public void createMissing68a(AnnonceRafamModel annonceEtatReg, AnnonceRafamModel annonce)
            throws JadeApplicationException, JadePersistenceException {

        DroitComplexModel droit = ALServiceLocator.getDroitComplexModelService().read(annonceEtatReg.getIdDroit());
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(
                droit.getDroitModel().getIdDossier());
        AnnonceRafamModel annonce68a = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68a(dossier,
                droit, RafamFamilyAllowanceType.getFamilyAllowanceType(annonceEtatReg.getGenrePrestation()));

        updateAnnonce(annonceEtatReg, annonce68a);
        annonce68a.setId(annonceEtatReg.getRecordNumber());
        AnnonceRafamModelChecker.validate(annonce68a);

        AnnonceRafamEtatRegistreModel annonceNewCreation = new AnnonceRafamEtatRegistreModel();
        annonceNewCreation.setBaseLegale(annonce68a.getBaseLegale());
        annonceNewCreation.setCanceled(annonce68a.getCanceled());
        annonceNewCreation.setCanton(annonce68a.getCanton());
        annonceNewCreation.setCodeRemarque(annonce68a.getCodeRemarque());
        annonceNewCreation.setCodeRetour(annonce68a.getCodeRetour());
        annonceNewCreation.setCodeStatutFamilial(annonce68a.getCodeStatutFamilial());
        annonceNewCreation.setCodeTypeActivite(annonce68a.getCodeTypeActivite());
        annonceNewCreation.setDateCreation(annonce68a.getDateCreation());
        annonceNewCreation.setDateMortAllocataire(annonce68a.getDateMortAllocataire());
        annonceNewCreation.setDateMortEnfant(annonce68a.getDateMortEnfant());
        annonceNewCreation.setDateMutation(annonce68a.getDateMutation());
        annonceNewCreation.setDateNaissanceAllocataire(annonce68a.getDateNaissanceAllocataire());
        annonceNewCreation.setDateNaissanceEnfant(annonce68a.getDateNaissanceEnfant());
        annonceNewCreation.setDebutDroit(annonce68a.getDebutDroit());
        annonceNewCreation.setEcheanceDroit(annonce68a.getEcheanceDroit());
        annonceNewCreation.setEtat(annonce68a.getEtat());
        annonceNewCreation.setEvDeclencheur(annonce68a.getEvDeclencheur());
        annonceNewCreation.setGenrePrestation(annonce68a.getGenrePrestation());
        annonceNewCreation.setId(annonce68a.getRecordNumber());
        annonceNewCreation.setIdAllocataire(annonce68a.getIdAllocataire());
        annonceNewCreation.setIdDroit(annonce68a.getIdDroit());
        annonceNewCreation.setInternalError(annonce68a.getInternalError());
        annonceNewCreation.setInternalErrorMessage("updateReg " + annonce68a.getInternalErrorMessage());
        annonceNewCreation.setInternalOfficeReference(annonce68a.getInternalOfficeReference());
        annonceNewCreation.setNewNssEnfant(annonce68a.getNewNssEnfant());
        annonceNewCreation.setNomAllocataire(annonce68a.getNomAllocataire());
        annonceNewCreation.setNomEnfant(annonce68a.getNomEnfant());
        annonceNewCreation.setNssAllocataire(annonce68a.getNssAllocataire());
        annonceNewCreation.setNssEnfant(annonce68a.getNssEnfant());
        annonceNewCreation.setPrenomAllocataire(annonce68a.getPrenomAllocataire());
        annonceNewCreation.setPrenomEnfant(annonce68a.getPrenomEnfant());
        annonceNewCreation.setRecordNumber(annonce68a.getRecordNumber());
        annonceNewCreation.setSexeAllocataire(annonce68a.getSexeAllocataire());
        annonceNewCreation.setSexeEnfant(annonce68a.getSexeEnfant());
        annonceNewCreation.setTypeAnnonce(annonce68a.getTypeAnnonce());
        JadePersistenceManager.add(annonceNewCreation);
    }

    /**
     * Crée une annonce d'annulation contenant les informations de l'état du registre
     * 
     * @param annonceEtatReg
     *            Annonce d'état du registre
     * @param annonce
     *            Annonce provenant de la base de données
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void createMissing68c(AnnonceRafamModel annonceEtatReg, AnnonceRafamModel annonce)
            throws JadeApplicationException, JadePersistenceException {
        AnnonceRafamModel annonce68c = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68c(annonce);
        annonce68c = updateAnnonce(annonceEtatReg, annonce68c);
        annonce68c.setInternalErrorMessage("updateReg " + annonce68c.getInternalErrorMessage());
        ALServiceLocator.getAnnonceRafamModelService().create(annonce68c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamEtatRegistreService#deleteAnnonceEtatRegistre(java.lang.String)
     */
    @Override
    public void deleteAnnonceEtatRegistre(String recordNumber) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteAnnonceEtatRegistre : recordNumber is empty");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        search.setForTypeAnnonce(RafamTypeAnnonce._69C_REGISTER_STATUS.getCode());
        JadePersistenceManager.delete(search);
    }

    /**
     * Met à jour l'annonce passée en paramètre avec les informations contenues dans une annonce d'état du registre
     * 
     * @param annonceEtatReg
     *            Annonce d'état du registre
     * @param annonce
     *            l'annonce à mettre à jour
     * @return L'annonce mise à jour
     * 
     * @throws JadeApplicationException
     */
    private AnnonceRafamModel updateAnnonce(AnnonceRafamModel annonceEtatReg, AnnonceRafamModel annonce)
            throws JadeApplicationException {

        annonce.setRecordNumber(annonceEtatReg.getRecordNumber());
        annonce.setGenrePrestation(annonceEtatReg.getGenrePrestation());

        if (!JadeStringUtil.isBlank(annonceEtatReg.getNssEnfant())) {
            annonce.setNssEnfant(annonceEtatReg.getNssEnfant());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getNomEnfant())) {
            annonce.setNomEnfant(annonceEtatReg.getNomEnfant());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getPrenomEnfant())) {
            annonce.setPrenomEnfant(annonceEtatReg.getPrenomEnfant());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getSexeEnfant())) {
            annonce.setSexeEnfant(annonceEtatReg.getSexeEnfant());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getDateNaissanceEnfant())) {
            annonce.setDateNaissanceEnfant(annonceEtatReg.getDateNaissanceEnfant());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getDateMortEnfant())) {
            annonce.setDateMortEnfant(annonceEtatReg.getDateMortEnfant());
        }

        annonce.setBaseLegale(annonceEtatReg.getBaseLegale());
        if (!JadeStringUtil.isBlank(annonceEtatReg.getCanton())) {
            annonce.setCanton(annonceEtatReg.getCanton());
        }

        if (!JadeStringUtil.isBlank(annonceEtatReg.getDebutDroit())) {
            annonce.setDebutDroit(annonceEtatReg.getDebutDroit());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getEcheanceDroit())) {
            annonce.setEcheanceDroit(annonceEtatReg.getEcheanceDroit());
        }

        if (!JadeStringUtil.isBlank(annonceEtatReg.getNomAllocataire())) {
            annonce.setNomAllocataire(annonceEtatReg.getNomAllocataire());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getPrenomAllocataire())) {
            annonce.setPrenomAllocataire(annonceEtatReg.getPrenomAllocataire());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getSexeAllocataire())) {
            annonce.setSexeAllocataire(annonceEtatReg.getSexeAllocataire());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getDateNaissanceAllocataire())) {
            annonce.setDateNaissanceAllocataire(annonceEtatReg.getDateNaissanceAllocataire());
        }
        if (!JadeStringUtil.isBlank(annonceEtatReg.getDateMortAllocataire())) {
            annonce.setDateMortAllocataire(annonceEtatReg.getDateMortAllocataire());
        }
        annonce.setCodeStatutFamilial(annonceEtatReg.getCodeStatutFamilial());
        annonce.setCodeTypeActivite(annonceEtatReg.getCodeTypeActivite());

        annonce.setDateCreation(annonceEtatReg.getDateCreation());
        if (!JadeStringUtil.isBlank(annonceEtatReg.getDateMutation())) {
            annonce.setDateMutation(annonceEtatReg.getDateMutation());
        }

        annonce.setCanceled(annonceEtatReg.getCanceled());

        if (JadeStringUtil.isBlankOrZero(annonce.getEvDeclencheur())) {
            annonce.setEvDeclencheur(RafamEvDeclencheur.ETAT_REGISTRE.getCS());
        }

        return annonce;
    }

    /**
     * Met à jour l'état des annonces (validation ou mise en erreur) en fonction de l'état du registre
     * 
     * @param annonceEtatReg
     *            Annonce d'état du registre
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void validate(AnnonceRafamModel annonceEtatReg) throws JadeApplicationException, JadePersistenceException {

        deleteAnnonceEtatRegistre(annonceEtatReg.getRecordNumber());

        switch (RafamReturnCode.getRafamReturnCode(annonceEtatReg.getCodeRetour())) {

            case EN_ERREUR:
            case REJETEE:
            case RAPPEL:
                ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                        annonceEtatReg.getRecordNumber(), true);
                break;

            case TRAITE:
            case ANNULEE:
            case EN_ATTENTE:
                ALImplServiceLocator.getAnnonceRafamBusinessService().validateForRecordNumber(
                        annonceEtatReg.getRecordNumber(), false);
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                        annonceEtatReg.getRecordNumber(), RafamTypeAnnonce._68B_MUTATION, true);
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteAnnoncesForRecordNumber(
                        annonceEtatReg.getRecordNumber(), RafamTypeAnnonce._68C_ANNULATION, true);
                break;
            default:
                throw new ALRafamException(
                        "AnnonceRafamBusinessServiceImpl#validate : this return code is not supported");
        }
    }
}
