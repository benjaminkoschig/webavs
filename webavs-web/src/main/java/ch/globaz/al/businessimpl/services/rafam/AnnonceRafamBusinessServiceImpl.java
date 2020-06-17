package ch.globaz.al.businessimpl.services.rafam;

import ch.globaz.al.business.constantes.enumerations.*;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pyxis.util.CommonNSSFormater;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service métier des annonces RAFAM
 * 
 * @author jts
 * 
 */
public class AnnonceRafamBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#canDelete(ch.globaz.al.business.models.rafam
     * .AnnonceRafamModel)
     */
    @Override
    public boolean canDelete(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        if ((RafamEtatAnnonce.A_TRANSMETTRE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat()))
                || RafamEtatAnnonce.ENREGISTRE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat())))
                && !RafamFamilyAllowanceType.ADI.getCode().equals(annonce.getGenrePrestation())) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#canValidate(ch.globaz.al.business.models.rafam
     * .AnnonceRafamModel)
     */
    @Override
    public boolean canValidate(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {

        // si l'annonce est déjà validée
        if (RafamEtatAnnonce.VALIDE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat()))) {
            return false;
            // si c'est une annonce de synchro UPI
        } else if (RafamTypeAnnonce._69B_SYNCHRO_UPI.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce
                .getTypeAnnonce()))) {
            return true;
            // si l'annonce a une erreur 212 mais pas d'autre type d'erreur
        } else if (ALServiceLocator.getAnnoncesRafamErrorBusinessService().hasError(annonce.getIdAnnonce(),
                RafamError._212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE.getCode())
                && !ALServiceLocator.getAnnoncesRafamErrorBusinessService().hasErrorOfOtherType(annonce.getIdAnnonce(),
                        RafamError._212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE.getCode())) {
            return true;
            // tous les autres cas
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#deleteIntermediateForRecordNumber(java.lang.
     * String)
     */
    @Override
    public void deleteAnnoncesForRecordNumber(String recordNumber, RafamTypeAnnonce typeAnnonce, boolean excludeLast)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteIntermediateForRecordNumber : recordNumber is empty");
        }

        if (typeAnnonce == null) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteIntermediateForRecordNumber : typeAnnonce is null");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        search.setForTypeAnnonce(typeAnnonce.getCode());
        search.setForEtatAnnonce(RafamEtatAnnonce.VALIDE.getCS());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderKey("idAnnonce");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        for (int i = (excludeLast ? 1 : 0); i < search.getSize(); i++) {
            ALServiceLocator.getAnnonceRafamModelService().delete((AnnonceRafamModel) search.getSearchResults()[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#deleteForEtat(java.lang.String,
     * ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce)
     */
    @Override
    public int deleteForEtat(String idDroit, RafamEtatAnnonce etatToDelete) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#deleteNotSent : idDroit is not defined");
        }

        if (!RafamEtatAnnonce.ENREGISTRE.equals(etatToDelete) && !RafamEtatAnnonce.A_TRANSMETTRE.equals(etatToDelete)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteNotSent : etatToDelete is not valid");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        List<String> etats = new ArrayList<String>();
        etats.add(etatToDelete.getCS());

        search.setInEtatAnnonce(etats);
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#deleteForEtat(java.lang.String,
     * ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce)
     */
    @Override
    public int deleteForEtatYear(String idDroit, RafamEtatAnnonce etatToDelete, String years) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#deleteNotSent : idDroit is not defined");
        }

        if (!RafamEtatAnnonce.ENREGISTRE.equals(etatToDelete) && !RafamEtatAnnonce.A_TRANSMETTRE.equals(etatToDelete)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteNotSent : etatToDelete is not valid");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        List<String> etats = new ArrayList<String>();
        etats.add(etatToDelete.getCS());

        search.setInEtatAnnonce(etats);
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setBetweenDateDebut("01.01."+years);
        search.setBetweenDateFin("31.12."+years);

        return JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#deleteNotSent(java.lang.String)
     */
    @Override
    public int deleteNotSent(String idDroit) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#deleteNotSent : idDroit is not defined");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        List<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.ENREGISTRE.getCS());
        etats.add(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
        search.setInEtatAnnonce(etats);
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return JadePersistenceManager.delete(search);
    }

    @Override
    public int deleteNotSentForRecordNumber(String recordNumber) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteNotSentForRecordNumber : recordNumber is not defined");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        List<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.ENREGISTRE.getCS());
        etats.add(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
        search.setInEtatAnnonce(etats);
        search.setForRecordNumber(recordNumber);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#deleteRefusees(java.lang.String)
     */
    @Override
    public int deleteRefusees(String recordNumber) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteRefusees : recordNumber is not defined");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        search.setForCodeRetour(RafamReturnCode.REJETEE.getCode());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#deleteRefuseesForDroit(java.lang.String)
     */
    @Override
    public int deleteRefuseesForDroit(String idDroit) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#deleteRefuseesForDroit : idDroit is not defined");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForIdDroit(idDroit);
        search.setForCodeRetour(RafamReturnCode.REJETEE.getCode());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#getSexeCS(java.lang.String)
     */
    @Override
    public String getSexeCS(String sexeEnfantRafam) throws JadeApplicationException {

        if (JadeStringUtil.isBlank(sexeEnfantRafam)) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#getSexeCS : sexeEnfantRafam is empty");
        }
        // FIXME vérifier la deuxième valeur
        if (sexeEnfantRafam.equals("1")) {
            return ALCSTiers.SEXE_HOMME;
        } else {
            return ALCSTiers.SEXE_FEMME;
        }
    }

    @Override
    public boolean isNaissanceHorlogere(ContextAnnonceRafam context) throws JadeApplicationException,
            JadePersistenceException {

        // cas d'un allocataire dans un canton ne versant pas de prestation de naissance
        if (ALImplServiceLocator.getHorlogerBusinessService().isNaissanceHorlogere(context.getDossier(),
                context.getDroit().getEnfantComplexModel())) {
            return true;
            // cas d'un allocataire résidant à l'étranger
        } else if (!ALCSPays.PAYS_SUISSE.equals(context.getDossier().getAllocataireComplexModel().getAllocataireModel()
                .getIdPaysResidence())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public AnnonceRafamModel setNSS(AnnonceRafamModel annonce, String nssAllocataire, String nssEnfant)
            throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();

        if ((annonce == null) || annonce.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#setNSS : annonce is null or is new");
        }

        try {
            if (!JadeStringUtil.isBlankOrZero(nssAllocataire)) {
                nssf.checkNss(nssf.unformat(nssAllocataire));
                annonce.setNssAllocataire(nssAllocataire);
            }
        } catch (Exception e) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#setNSS : Le NSS " + nssAllocataire
                    + "n'est pas valide");
        }

        try {
            if (!JadeStringUtil.isBlankOrZero(nssEnfant)) {
                nssf.checkNss(nssf.unformat(nssEnfant));
                annonce.setNssEnfant(nssEnfant);
            }
        } catch (Exception e) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#setNSS : Le NSS " + nssEnfant
                    + "n'est pas valide");
        }

        annonce = ALServiceLocator.getAnnonceRafamModelService().update(annonce);

        return annonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#setError(ch.globaz.al.business.models.rafam.
     * AnnonceRafamModel, java.lang.String)
     */
    @Override
    public AnnonceRafamModel setError(AnnonceRafamModel annonce, String message) throws JadeApplicationException,
            JadePersistenceException {

        if ((annonce == null) || annonce.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#setError : annonce is null or is new");
        }

        annonce.setInternalError(true);
        annonce.setInternalErrorMessage(message);
        return ALServiceLocator.getAnnonceRafamModelService().update(annonce);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#setEtat(ch.globaz.al.business.models.rafam.
     * AnnonceRafamModel, ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce)
     */
    @Override
    public AnnonceRafamModel setEtat(AnnonceRafamModel annonce, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException {

        if ((annonce == null) || annonce.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#setEtat : annonce is null or is new");
        }

        if (etat == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#setEtat : etat is null");
        }

        annonce.setEtat(etat.getCS());
        return ALServiceLocator.getAnnonceRafamModelService().update(annonce);
    }

    @Override
    public void validateEnAttenteForRecordNumber(String recordNumber, boolean excludeLast)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#validateForRecordNumber : recordNumber is empty");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        search.setForEtatAnnonce(RafamEtatAnnonce.VALIDE.getCS());

        List<String> codesRetour = new ArrayList<String>();
        codesRetour.add(RafamReturnCode.EN_ATTENTE.getCode());
        codesRetour.add(RafamReturnCode.NON_TRAITE.getCode());
        search.setInCodeRetour(codesRetour);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderKey("idAnnonce");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        for (int i = (excludeLast ? 1 : 0); i < search.getSize(); i++) {
            AnnonceRafamModel annonce = (AnnonceRafamModel) search.getSearchResults()[i];
            annonce.setCodeRetour(RafamReturnCode.TRAITE.getCode());
            ALServiceLocator.getAnnonceRafamModelService().update(annonce);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService#validerAnnoncesForRecordNumber(java.lang.String,
     * boolean)
     */
    @Override
    public void validateForRecordNumber(String recordNumber, boolean excludeLast) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#validateForRecordNumber : recordNumber is empty");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);

        List<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.RECU.getCS());
        etats.add(RafamEtatAnnonce.TRANSMIS.getCS());
        etats.add(RafamEtatAnnonce.SUSPENDU.getCS());
        search.setInEtatAnnonce(etats);

        List<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderKey("idAnnonce");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        for (int i = (excludeLast ? 1 : 0); i < search.getSize(); i++) {
            AnnonceRafamModel annonce = (AnnonceRafamModel) search.getSearchResults()[i];

            annonce.setEtat(RafamEtatAnnonce.VALIDE.getCS());

            if (RafamReturnCode.NON_TRAITE.equals(RafamReturnCode.getRafamReturnCode(annonce.getCodeRetour()))) {
                annonce.setCodeRetour(RafamReturnCode.TRAITE.getCode());
            }

            annonce = ALServiceLocator.getAnnonceRafamModelService().update(annonce);

            ALServiceLocator.getErreurAnnonceRafamModelService().deleteForIdAnnonce(annonce.getIdAnnonce());
        }
    }
}
