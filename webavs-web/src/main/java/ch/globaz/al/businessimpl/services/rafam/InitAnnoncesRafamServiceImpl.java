package ch.globaz.al.businessimpl.services.rafam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.AllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.BeneficiaryType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildType;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.InitAnnoncesRafamService;
import ch.globaz.al.business.services.rafam.sedex.AnnonceRafamSedexService;
import ch.globaz.al.businessimpl.rafam.office.Office;
import ch.globaz.al.businessimpl.rafam.office.OfficeFactory;
import ch.globaz.al.businessimpl.rafam.office.UniqueCaisseOffice;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALRafamUtils;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.util.CommonNSSFormater;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jts
 *
 */
public class InitAnnoncesRafamServiceImpl extends ALAbstractBusinessServiceImpl implements InitAnnoncesRafamService {

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#getBaseLegale(ch.globaz.al.business.models.dossier
     * .DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public RafamLegalBasis getBaseLegale(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationException {

        if (ALServiceLocator.getDossierBusinessService()
                .isAgricole(dossier.getDossierModel().getActiviteAllocataire())) {

            AgricoleSearchModel search = new AgricoleSearchModel();
            search.setForIdAllocataire(dossier.getDossierModel().getIdAllocataire());
            search = ALImplServiceLocator.getAgricoleModelService().search(search);

            if (search.getSize() == 1) {

                if (((AgricoleModel) search.getSearchResults()[0]).getDomaineMontagne()) {
                    return RafamLegalBasis.LFA_MONTAGNE;
                } else {
                    return RafamLegalBasis.LFA_PLAINE;
                }

            } else {
                throw new ALAnnonceRafamException(
                        "InitAnnoncesRafamServiceImpl#getBaseLegale : Impossible d'identifier le domaine agricole");
            }
        } else {
            return RafamLegalBasis.LAFAM;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#getCantonBaseLegale(ch.globaz.al.business.models
     * .dossier.DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public String getCantonBaseLegale(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {

        AssuranceInfo info = ALServiceLocator.getAffiliationBusinessService()
                .getAssuranceInfo(dossier.getDossierModel(), JadeDateUtil.getGlobazFormattedDate(new Date()));

        return JadeCodesSystemsUtil.getCode(info.getCanton());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#getInternalOfficeReference(ch.globaz.al.business
     * .models.dossier.DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public String getInternalOfficeReference(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException {

        StringBuffer sb = new StringBuffer(ALConstRafam.PREFIX_INTERNAL_OFFICE_REFERENCE_CAISSE)
                .append(ALConstRafam.INTERNAL_OFFICE_REFERENCE_SEPARATOR);
        sb.append(dossier.getId()).append(ALConstRafam.INTERNAL_OFFICE_REFERENCE_SEPARATOR);
        sb.append(droit.getId());

        if (sb.length() > ALConstRafam.INTERNAL_OFFICE_REFERENCE_MAX_LENGH) {
            throw new ALRafamException(
                    "InitAnnoncesRafamServiceImpl#getInternalOfficeReference : generated Internal Office Reference is too long");
        }

        return sb.toString();
    }

    /**
     * Méthode ajoutée en vue de la gestion des record numbers pour la FPV
     *
     * @return <code>null</code>
     */
    private String getRecordNumber() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68a(ch.globaz.al.business.models.dossier
     * .DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType)
     */
    @Override
    public AnnonceRafamModel initAnnonce68a(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type) throws JadeApplicationException, JadePersistenceException {

        return this.initAnnonce68a(dossier, droit, type, RafamEtatAnnonce.A_TRANSMETTRE);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68a(ch.globaz.al.business.models.dossier
     * .DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType,
     * ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce)
     */
    @Override
    public AnnonceRafamModel initAnnonce68a(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEtatAnnonce etat)
            throws JadeApplicationException, JadePersistenceException {

        if (!RafamEtatAnnonce.A_TRANSMETTRE.equals(etat) && !RafamEtatAnnonce.ENREGISTRE.equals(etat)) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce68a : Impossible d'initialiser une annonce avec cet etat:"
                            + etat.toString());
        }

        AnnonceRafamModel annonce = new AnnonceRafamModel();
        annonce.setTypeAnnonce(RafamTypeAnnonce._68A_CREATION.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.CREATION.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);

        annonce.setEtat(etat.getCS());
        annonce.setCodeRetour(RafamReturnCode.NON_TRAITE.getCode());

        annonce.setIdAllocataire(dossier.getDossierModel().getIdAllocataire());
        annonce.setIdDroit(droit.getId());

        annonce.setRecordNumber(getRecordNumber());
        annonce.setInternalOfficeReference(getInternalOfficeReference(dossier, droit));

        annonce.setNssEnfant(
                droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
        annonce.setGenrePrestation(type.getCodeCentrale());
        annonce.setBaseLegale(getBaseLegale(dossier, droit).getCodeCentrale());
        annonce.setCanton(getCantonBaseLegaleCascade(dossier, droit, annonce));

        if (!type.equals(RafamFamilyAllowanceType.ADOPTION) && !type.equals(RafamFamilyAllowanceType.NAISSANCE)
                && !type.equals(RafamFamilyAllowanceType.DIFFERENCE_ADOPTION)
                && !type.equals(RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE)) {

            annonce.setDebutDroit(droit.getDroitModel().getDebutDroit());
            annonce.setEcheanceDroit(droit.getDroitModel().getFinDroitForcee());
        }

        annonce.setNssAllocataire(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel());
        annonce.setCodeStatutFamilial(
                RafamFamilyStatus.getFamilyStatusCS(droit.getDroitModel().getStatutFamilial()).getCodeCentrale());
        annonce.setCodeTypeActivite(RafamOccupationStatus
                .getOccupationStatusCS(dossier.getDossierModel().getActiviteAllocataire()).getCodeCentrale());

        annonce.setDelegated(false);

        String numAffilie = dossier.getDossierModel().getNumeroAffilie();
        String activite = dossier.getDossierModel().getActiviteAllocataire();
        Office officeProvider = OfficeFactory.instanciate(numAffilie, activite);

        annonce.setOfficeIdentifier(officeProvider.getOfficeIdentifier());
        annonce.setOfficeBranch(officeProvider.getOfficeBranch());
        annonce.setLegalOffice(officeProvider.getLegalOffice(droit.getDroitModel().getDebutDroit()));

        annonce.setNumeroIDE(getNumeroIDEAffilie(numAffilie));
        annonce.setCodeCentralePaysEnfant(
                getCodeCentralePays(droit.getEnfantComplexModel().getEnfantModel().getIdPaysResidence()));

        return annonce;
    }

    /**
     * Récupère le code de la centrale du pays
     *
     * @param idPays
     * @return codeCentralePays
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private String getCodeCentralePays(String idPays) throws JadePersistenceException, JadeApplicationException {
        PaysSearchSimpleModel paysSearchModel = new PaysSearchSimpleModel();
        paysSearchModel.setForIdPays(idPays);
        paysSearchModel = TIBusinessServiceLocator.getAdresseService().findPays(paysSearchModel);
        if (paysSearchModel.getSize() == 1) {
            PaysSimpleModel paysSimpleModel = (PaysSimpleModel) paysSearchModel.getSearchResults()[0];
            return paysSimpleModel.getCodeCentrale();
        }
        return "";
    }

    /**
     * Récupère le numéro IDE de l'affilié d'après son numéro d'affilié
     *
     * @param numAffilie
     * @return numeroIDE de l'affilié
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private String getNumeroIDEAffilie(String numAffilie) throws JadePersistenceException, JadeApplicationException {
        AffiliationSimpleModel affil = ALServiceLocator.getAffiliationBusinessService().getAffiliation(numAffilie);
        return affil.getNumeroIDE();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68b(ch.globaz.al.business.models.dossier
     * .DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType,
     * ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce,
     * ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur,
     * ch.globaz.al.business.models.rafam.AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel initAnnonce68b(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEtatAnnonce etat, RafamEvDeclencheur evDeclencheur,
            AnnonceRafamModel lastAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (!RafamEtatAnnonce.A_TRANSMETTRE.equals(etat) && !RafamEtatAnnonce.ENREGISTRE.equals(etat)) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce68b : Impossible d'initialiser une annonce avec cet etat:"
                            + etat.toString());
        }

        AnnonceRafamModel annonce = new AnnonceRafamModel();
        annonce.setTypeAnnonce(RafamTypeAnnonce._68B_MUTATION.getCode());
        annonce.setEvDeclencheur(evDeclencheur.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);

        annonce.setEtat(etat.getCS());
        annonce.setCodeRetour(RafamReturnCode.NON_TRAITE.getCode());

        annonce.setIdAllocataire(dossier.getDossierModel().getIdAllocataire());
        annonce.setIdDroit(droit.getId());

        annonce.setRecordNumber(lastAnnonce.getRecordNumber());
        annonce.setInternalOfficeReference(lastAnnonce.getInternalOfficeReference());

        annonce.setNssEnfant(
                droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
        annonce.setGenrePrestation(type.getCodeCentrale());
        annonce.setBaseLegale(getBaseLegale(dossier, droit).getCodeCentrale());
        annonce.setCanton(getCantonBaseLegaleCascade(dossier, droit, annonce));

        if (!type.equals(RafamFamilyAllowanceType.ADOPTION) && !type.equals(RafamFamilyAllowanceType.NAISSANCE)
                && !type.equals(RafamFamilyAllowanceType.DIFFERENCE_ADOPTION)
                && !type.equals(RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE)) {

            annonce.setDebutDroit(droit.getDroitModel().getDebutDroit());
            annonce.setEcheanceDroit(droit.getDroitModel().getFinDroitForcee());
        }

        annonce.setNssAllocataire(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel());
        annonce.setCodeStatutFamilial(
                RafamFamilyStatus.getFamilyStatusCS(droit.getDroitModel().getStatutFamilial()).getCodeCentrale());
        annonce.setCodeTypeActivite(RafamOccupationStatus
                .getOccupationStatusCS(dossier.getDossierModel().getActiviteAllocataire()).getCodeCentrale());

        annonce.setDelegated(false);

        String numAffilie = dossier.getDossierModel().getNumeroAffilie();
        String activite = dossier.getDossierModel().getActiviteAllocataire();
        Office officeProvider = OfficeFactory.instanciate(numAffilie, activite);

        annonce.setOfficeIdentifier(officeProvider.getOfficeIdentifier());
        annonce.setOfficeBranch(officeProvider.getOfficeBranch());
        annonce.setLegalOffice(officeProvider.getLegalOffice(droit.getDroitModel().getDebutDroit()));

        annonce.setNumeroIDE(getNumeroIDEAffilie(numAffilie));
        annonce.setCodeCentralePaysEnfant(
                getCodeCentralePays(droit.getEnfantComplexModel().getEnfantModel().getIdPaysResidence()));

        return annonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68b(ch.globaz.al.business.models.dossier
     * .DossierComplexModel, ch.globaz.al.business.models.droit.DroitComplexModel,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType,
     * ch.globaz.al.business.models.rafam.AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel initAnnonce68b(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEvDeclencheur evDeclencheur, AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException {

        return this.initAnnonce68b(dossier, droit, type, RafamEtatAnnonce.A_TRANSMETTRE, evDeclencheur, lastAnnonce);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68c(ch.globaz.al.business.models.rafam
     * .AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel initAnnonce68c(AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException {
        return this.initAnnonce68c(lastAnnonce, RafamEtatAnnonce.A_TRANSMETTRE);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68c(ch.globaz.al.business.models.rafam
     * .AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel initAnnonce68c(AnnonceRafamModel lastAnnonce, RafamEtatAnnonce etat)
            throws JadeApplicationException, JadePersistenceException {
        if (!RafamEtatAnnonce.A_TRANSMETTRE.equals(etat) && !RafamEtatAnnonce.ENREGISTRE.equals(etat)) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce68b : Impossible d'initialiser une annonce avec cet etat:"
                            + etat.toString());
        }

        AnnonceRafamModel annonce = new AnnonceRafamModel();
        annonce.setTypeAnnonce(RafamTypeAnnonce._68C_ANNULATION.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.ANNULATION.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(true);

        annonce.setEtat(etat.getCS());
        annonce.setCodeRetour(RafamReturnCode.NON_TRAITE.getCode());

        annonce.setIdAllocataire(lastAnnonce.getIdAllocataire());
        annonce.setIdDroit(lastAnnonce.getIdDroit());

        annonce.setRecordNumber(lastAnnonce.getRecordNumber());
        annonce.setInternalOfficeReference(lastAnnonce.getInternalOfficeReference());

        annonce.setNssEnfant(lastAnnonce.getNssEnfant());
        annonce.setGenrePrestation(lastAnnonce.getGenrePrestation());

        annonce.setDelegated(false);

        annonce.setOfficeIdentifier(lastAnnonce.getOfficeIdentifier());
        annonce.setOfficeBranch(lastAnnonce.getOfficeBranch());
        annonce.setLegalOffice(lastAnnonce.getLegalOffice());

        annonce.setNumeroIDE(lastAnnonce.getNumeroIDE());

        if (!JadeStringUtil.isBlankOrZero(lastAnnonce.getCodeCentralePaysEnfant())) {
            Integer countryID = Integer.valueOf(lastAnnonce.getCodeCentralePaysEnfant());
            annonce.setCodeCentralePaysEnfant(String.valueOf(ALRafamUtils.formatCountryIDToThreePositions(countryID)));
        }

        return annonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce69b(ch.ech.xmlns.ech_0104_69._2.
     * UPISynchronizationRecordType)
     */
    @Override
    public AnnonceRafamModel initAnnonce69b(ch.ech.xmlns.ech_0104_69._3.UPISynchronizationRecordType message)
            throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        AnnonceRafamModel annonce = new AnnonceRafamModel();
        AnnonceRafamModel last = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .getLastAnnonceForRecordNumber(message.getRecordNumber().toString());

        if (last.isNew()) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69b : aucune annonce n'a été trouvée pour le record number "
                            + message.getRecordNumber().toString());
        }

        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));

        annonce.setTypeAnnonce(RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.UPDATE_UPI.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);
        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce.setIdAllocataire(last.getIdAllocataire());
        annonce.setIdDroit(last.getIdDroit());

        annonce.setRecordNumber(message.getRecordNumber().toString());
        // selon le recordNumber, on set attribut delegated
        AnnonceRafamSedexService s = ALImplServiceLocator.getAnnonceRafamSedexService();

        if (s.isAnnonceEmployeurDelegue(null, message.getRecordNumber().toString())) {
            annonce.setDelegated(true);
        } else {
            annonce.setDelegated(false);
        }

        annonce.setGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(message.getFamilyAllowanceType()).getCodeCentrale());

        annonce.setDateCreation(
                JadeDateUtil.getGlobazFormattedDate(message.getCreationDate().toGregorianCalendar().getTime()));
        if (message.getMutationDate() != null) {
            annonce.setDateMutation(
                    JadeDateUtil.getGlobazFormattedDate(message.getMutationDate().toGregorianCalendar().getTime()));
        }

        annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(Long.toString(message.getReturnCode())).getCode());

        // NSS
        try {
            if (message.getChild().getVn() != null) {
                annonce.setNssEnfant(nssf.format(Long.toString(message.getChild().getVn())));
            }
            if (message.getChild().getNewVn() != null) {
                annonce.setNewNssEnfant(nssf.format(Long.toString(message.getChild().getNewVn())));
            }
            if (message.getBeneficiary().getVn() != null) {
                annonce.setNssAllocataire(nssf.format(Long.toString(message.getBeneficiary().getVn())));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69b : unable to check NSS " + e.getMessage());
        }

        // enfant
        if (!JadeStringUtil.isBlank(message.getChild().getOfficialName())) {
            annonce.setNomEnfant(message.getChild().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getFirstName())) {
            annonce.setPrenomEnfant(message.getChild().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getSex())) {
            annonce.setSexeEnfant(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getChild().getSex()));
        }
        if (message.getChild().getDateOfBirth() != null) {
            annonce.setDateNaissanceEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getChild().getDateOfDeath() != null) {
            annonce.setDateMortEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        // allocataire
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getOfficialName())) {
            annonce.setNomAllocataire(message.getBeneficiary().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getFirstName())) {
            annonce.setPrenomAllocataire(message.getBeneficiary().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getSex())) {
            annonce.setSexeAllocataire(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getBeneficiary().getSex()));
        }
        if (message.getBeneficiary().getDateOfBirth() != null) {
            annonce.setDateNaissanceAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getBeneficiary().getDateOfDeath() != null) {
            annonce.setDateMortAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        return annonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce69c(ch.ech.xmlns.ech_0104_69._2.
     * RegisterStatusRecordType)
     */
    @Override
    public AnnonceRafamModel initAnnonce69cDelegue(ch.ech.xmlns.ech_0104_69._3.RegisterStatusRecordType message,
            boolean initial) throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        AnnonceRafamModel annonce = new AnnonceRafamModel();

        annonce.setRecordNumber(message.getRecordNumber().toString());

        annonce.setGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(message.getFamilyAllowanceType()).getCodeCentrale());

        // NSS
        try {
            if (message.getChild().getVn() != null) {
                annonce.setNssEnfant(nssf.format(Long.toString(message.getChild().getVn())));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69c : unable to check child's NSS " + e.getMessage());
        }

        // enfant
        if (!JadeStringUtil.isBlank(message.getChild().getOfficialName())) {
            annonce.setNomEnfant(message.getChild().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getFirstName())) {
            annonce.setPrenomEnfant(message.getChild().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getSex())) {
            annonce.setSexeEnfant(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getChild().getSex()));
        }
        if (message.getChild().getDateOfBirth() != null) {
            annonce.setDateNaissanceEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getChild().getDateOfDeath() != null) {
            annonce.setDateMortEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        annonce.setBaseLegale(RafamLegalBasis.getLegalBasis(message.getLegalBasis().getLaw()).getCodeCentrale());
        if (message.getLegalBasis().getCanton() != null) {
            annonce.setCanton(message.getLegalBasis().getCanton().value());
        }

        if (message.getValidityPeriod() != null) {
            annonce.setDebutDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod().getStart()));
            annonce.setEcheanceDroit(
                    ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod().getEnd()));
        }

        // allocataire
        try {
            if (message.getBeneficiary().getVn() != null) {
                annonce.setNssAllocataire(nssf.format(Long.toString(message.getBeneficiary().getVn())));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69c : unable to check beneficiary's NSS "
                            + e.getMessage());
        }

        if (!JadeStringUtil.isBlank(message.getBeneficiary().getOfficialName())) {
            annonce.setNomAllocataire(message.getBeneficiary().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getFirstName())) {
            annonce.setPrenomAllocataire(message.getBeneficiary().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getSex())) {
            annonce.setSexeAllocataire(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getBeneficiary().getSex()));
        }
        if (message.getBeneficiary().getDateOfBirth() != null) {
            annonce.setDateNaissanceAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getBeneficiary().getDateOfDeath() != null) {
            annonce.setDateMortAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        annonce.setCodeStatutFamilial(
                RafamFamilyStatus.getFamilyStatus(message.getBeneficiary().getFamilialStatus()).getCodeCentrale());

        annonce.setCodeTypeActivite(RafamOccupationStatus
                .getOccupationStatus(message.getBeneficiary().getOccupationStatus()).getCodeCentrale());

        annonce.setDateCreation(
                JadeDateUtil.getGlobazFormattedDate(message.getCreationDate().toGregorianCalendar().getTime()));

        if (message.getMutationDate() != null) {
            annonce.setDateMutation(
                    JadeDateUtil.getGlobazFormattedDate(message.getMutationDate().toGregorianCalendar().getTime()));
        }

        if (message.getCanceled() == null) {
            annonce.setCanceled(false);
        } else {
            annonce.setCanceled(0 == message.getCanceled() ? true : false);
        }

        if (message.getReturnCode() != null) {
            annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(Long.toString(message.getReturnCode())).getCode());
        } else {

            if ((message.getError() != null) && !message.getError().isEmpty()) {
                annonce.setCodeRetour(RafamReturnCode.EN_ERREUR.getCode());
            } else if (annonce.getCanceled()) {
                annonce.setCodeRetour(RafamReturnCode.ANNULEE.getCode());
            } else {
                annonce.setCodeRetour(RafamReturnCode.TRAITE.getCode());
            }
        }

        // ////////////////////////////////////////////////////////////////
        // Informations internes
        if (initial) {
            annonce.setTypeAnnonce(RafamTypeAnnonce._68A_CREATION.getCode());
            annonce.setEvDeclencheur(RafamEvDeclencheur.CREATION.getCS());
        } else {
            annonce.setTypeAnnonce(RafamTypeAnnonce._69C_REGISTER_STATUS.getCode());
            annonce.setEvDeclencheur(RafamEvDeclencheur.ETAT_REGISTRE.getCS());
        }

        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
        annonce.setInternalOfficeReference("ED-" + message.getRecordNumber().toString().substring(0, 2) + "-"
                + ALRafamUtils.translateInRefNumber(message.getRecordNumber().toString()));
        annonce.setInternalError(false);
        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce.setIdAllocataire("0");
        annonce.setIdDroit("0");
        annonce.setDelegated(new Boolean(true));
        return annonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce69d(ch.ech.xmlns.ech_0104_69._2.
     * NoticeType
     * )
     */
    @Override
    public AnnonceRafamModel initAnnonce69d(ch.ech.xmlns.ech_0104_69._3.NoticeType notice)
            throws JadeApplicationException, JadePersistenceException {
        CommonNSSFormater nssf = new CommonNSSFormater();
        AnnonceRafamModel annonce = new AnnonceRafamModel();
        AnnonceRafamModel last = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .getLastAnnonceForRecordNumber(notice.getRecordNumber().toString());

        if (last.isNew()) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69d : aucune annonce n'a été trouvée pour le record number "
                            + notice.getRecordNumber().toString());
        }

        annonce.setTypeAnnonce(RafamTypeAnnonce._69D_NOTICE.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.NOTICE.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);
        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce.setIdAllocataire(last.getIdAllocataire());
        annonce.setIdDroit(last.getIdDroit());

        annonce.setRecordNumber(notice.getRecordNumber().toString());
        annonce.setGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(notice.getFamilyAllowanceType()).getCodeCentrale());

        annonce.setDateCreation(
                JadeDateUtil.getGlobazFormattedDate(notice.getCreationDate().toGregorianCalendar().getTime()));
        if (notice.getMutationDate() != null) {
            annonce.setDateMutation(
                    JadeDateUtil.getGlobazFormattedDate(notice.getMutationDate().toGregorianCalendar().getTime()));
        }

        annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(Long.toString(notice.getReturnCode())).getCode());

        // NSS
        try {
            annonce.setNssEnfant(nssf.format(Long.toString(notice.getChild().getVn())));
            annonce.setNssAllocataire(nssf.format(Long.toString(notice.getBeneficiary().getVn())));

        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69d : unable to check NSS " + e.getMessage());
        }

        return annonce;
    }

    @Override
    public AnnonceRafamModel initAnnonceDelegue68a(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance) throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamModel annonce = new AnnonceRafamModel();
        annonce.setTypeAnnonce(RafamTypeAnnonce._68A_CREATION.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.CREATION.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);

        annonce.setEtat(RafamEtatAnnonce.ENREGISTRE.getCS());
        annonce.setCodeRetour(RafamReturnCode.NON_TRAITE.getCode());

        annonce.setIdAllocataire(null);
        annonce.setIdDroit(null);
        annonce.setRecordNumber(null);

        annonce.setNssEnfant(child.getChildAHVN13());
        annonce.setGenrePrestation(allowance.getAllowanceType());
        annonce.setBaseLegale(allowance.getAllowanceApplicableLegislation());
        annonce.setCanton(allowance.getAllowanceBenefitCanton());
        if (allowance.getAllowanceChildCountryResidence() != null) {
            annonce.setCodeCentralePaysEnfant(String.valueOf(
                    ALRafamUtils.formatCountryIDToThreePositions(allowance.getAllowanceChildCountryResidence())));
        }

        annonce.setDebutDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(allowance.getAllowanceDateFrom()));
        annonce.setEcheanceDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(allowance.getAllowanceDateTo()));

        annonce.setNssAllocataire(beneficiary.getBeneficiaryAHVN13());
        annonce.setCodeStatutFamilial(child.getChildFamilyRelation());
        annonce.setCodeTypeActivite(beneficiary.getBeneficiaryStatus());

        annonce.setDelegated(true);

        Office officeProvider = new UniqueCaisseOffice();
        annonce.setOfficeIdentifier(officeProvider.getOfficeIdentifier());
        annonce.setOfficeBranch(officeProvider.getOfficeBranch());
        annonce.setLegalOffice(officeProvider.getLegalOffice());

        return annonce;
    }

    @Override
    public AnnonceRafamModel initAnnonceDelegue68b(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance, AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamModel annonce = new AnnonceRafamModel();
        annonce.setTypeAnnonce(RafamTypeAnnonce._68B_MUTATION.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.MODIF_DROIT.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);

        annonce.setEtat(RafamEtatAnnonce.ENREGISTRE.getCS());
        annonce.setCodeRetour(RafamReturnCode.NON_TRAITE.getCode());

        annonce.setIdAllocataire(null);
        annonce.setIdDroit(null);

        annonce.setRecordNumber(lastAnnonce.getRecordNumber());
        annonce.setInternalOfficeReference(lastAnnonce.getInternalOfficeReference());

        annonce.setNssEnfant(child.getChildAHVN13());
        annonce.setGenrePrestation(allowance.getAllowanceType());
        annonce.setBaseLegale(allowance.getAllowanceApplicableLegislation());
        annonce.setCanton(allowance.getAllowanceBenefitCanton());
        if (allowance.getAllowanceChildCountryResidence() != null) {
            annonce.setCodeCentralePaysEnfant(String.valueOf(
                    ALRafamUtils.formatCountryIDToThreePositions(allowance.getAllowanceChildCountryResidence())));
        }

        if (!allowance.getAllowanceType().equals(RafamFamilyAllowanceType.ADOPTION.getCodeCentrale())
                && !allowance.getAllowanceType().equals(RafamFamilyAllowanceType.NAISSANCE.getCodeCentrale())
                && !allowance.getAllowanceType().equals(RafamFamilyAllowanceType.DIFFERENCE_ADOPTION.getCodeCentrale())
                && !allowance.getAllowanceType()
                        .equals(RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE.getCodeCentrale())) {
            annonce.setDebutDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(allowance.getAllowanceDateFrom()));
            annonce.setEcheanceDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(allowance.getAllowanceDateTo()));
        }

        annonce.setNssAllocataire(beneficiary.getBeneficiaryAHVN13());
        annonce.setCodeStatutFamilial(child.getChildFamilyRelation());
        annonce.setCodeTypeActivite(beneficiary.getBeneficiaryStatus());

        annonce.setDelegated(true);

        Office officeProvider = new UniqueCaisseOffice();
        annonce.setOfficeIdentifier(officeProvider.getOfficeIdentifier());
        annonce.setOfficeBranch(officeProvider.getOfficeBranch());
        annonce.setLegalOffice(officeProvider.getLegalOffice());

        return annonce;
    }

    @Override
    public AnnonceRafamModel initAnnonceDelegue68c(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance, AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException {
        AnnonceRafamModel annonce = new AnnonceRafamModel();
        annonce.setTypeAnnonce(RafamTypeAnnonce._68C_ANNULATION.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.ANNULATION.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(true);

        annonce.setEtat(RafamEtatAnnonce.ENREGISTRE.getCS());
        annonce.setCodeRetour(RafamReturnCode.NON_TRAITE.getCode());

        annonce.setIdAllocataire(null);
        annonce.setIdDroit(null);

        annonce.setRecordNumber(lastAnnonce.getRecordNumber());
        annonce.setInternalOfficeReference(lastAnnonce.getInternalOfficeReference());

        annonce.setNssEnfant(lastAnnonce.getNssEnfant());
        annonce.setGenrePrestation(lastAnnonce.getGenrePrestation());

        annonce.setDelegated(true);

        annonce.setOfficeIdentifier(lastAnnonce.getOfficeIdentifier());
        annonce.setOfficeBranch(lastAnnonce.getOfficeBranch());
        annonce.setLegalOffice(lastAnnonce.getLegalOffice());

        return annonce;
    }

    @Override
    public AnnonceRafamModel initAnnonce69b(al.ch.ech.xmlns.ech_0104_69._4.UPISynchronizationRecordType message)
            throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        AnnonceRafamModel annonce = new AnnonceRafamModel();
        AnnonceRafamModel last = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .getLastAnnonceForRecordNumber(message.getRecordNumber().toString());

        if (last.isNew()) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69b : aucune annonce n'a été trouvée pour le record number "
                            + message.getRecordNumber().toString());
        }

        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));

        annonce.setTypeAnnonce(RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.UPDATE_UPI.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);
        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce.setIdAllocataire(last.getIdAllocataire());
        annonce.setIdDroit(last.getIdDroit());

        annonce.setRecordNumber(message.getRecordNumber().toString());
        // selon le recordNumber, on set attribut delegated
        AnnonceRafamSedexService s = ALImplServiceLocator.getAnnonceRafamSedexService();

        if (s.isAnnonceEmployeurDelegue(null, message.getRecordNumber().toString())) {
            annonce.setDelegated(true);
        } else {
            annonce.setDelegated(false);
        }

        annonce.setGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(message.getFamilyAllowanceType()).getCodeCentrale());

        annonce.setDateCreation(
                JadeDateUtil.getGlobazFormattedDate(message.getCreationDate().toGregorianCalendar().getTime()));
        if (message.getMutationDate() != null) {
            annonce.setDateMutation(
                    JadeDateUtil.getGlobazFormattedDate(message.getMutationDate().toGregorianCalendar().getTime()));
        }

        annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(Long.toString(message.getReturnCode())).getCode());

        // NSS
        try {
            if (message.getChild().getVn() != null) {
                annonce.setNssEnfant(nssf.format(Long.toString(message.getChild().getVn())));
            }
            if (message.getChild().getNewVn() != null) {
                annonce.setNewNssEnfant(nssf.format(Long.toString(message.getChild().getNewVn())));
            }
            if (message.getBeneficiary().getVn() != null) {
                annonce.setNssAllocataire(nssf.format(Long.toString(message.getBeneficiary().getVn())));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69b : unable to check NSS " + e.getMessage());
        }

        // enfant
        if (!JadeStringUtil.isBlank(message.getChild().getOfficialName())) {
            annonce.setNomEnfant(message.getChild().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getFirstName())) {
            annonce.setPrenomEnfant(message.getChild().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getSex())) {
            annonce.setSexeEnfant(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getChild().getSex()));
        }
        if (message.getChild().getDateOfBirth() != null) {
            annonce.setDateNaissanceEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getChild().getDateOfDeath() != null) {
            annonce.setDateMortEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        // allocataire
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getOfficialName())) {
            annonce.setNomAllocataire(message.getBeneficiary().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getFirstName())) {
            annonce.setPrenomAllocataire(message.getBeneficiary().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getSex())) {
            annonce.setSexeAllocataire(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getBeneficiary().getSex()));
        }
        if (message.getBeneficiary().getDateOfBirth() != null) {
            annonce.setDateNaissanceAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getBeneficiary().getDateOfDeath() != null) {
            annonce.setDateMortAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        return annonce;
    }

    @Override
    public AnnonceRafamModel initAnnonce69cDelegue(al.ch.ech.xmlns.ech_0104_69._4.RegisterStatusRecordType message,
            boolean initial) throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        AnnonceRafamModel annonce = new AnnonceRafamModel();

        annonce.setRecordNumber(message.getRecordNumber().toString());

        annonce.setGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(message.getFamilyAllowanceType()).getCodeCentrale());

        // NSS
        try {
            if (message.getChild().getVn() != null) {
                annonce.setNssEnfant(nssf.format(Long.toString(message.getChild().getVn())));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69c : unable to check child's NSS " + e.getMessage());
        }

        if (message.getChild().getCountryId() != null) {
            annonce.setCodeCentralePaysEnfant(
                    String.valueOf(ALRafamUtils.formatCountryIDToThreePositions(message.getChild().getCountryId())));
        }

        // enfant
        if (!JadeStringUtil.isBlank(message.getChild().getOfficialName())) {
            annonce.setNomEnfant(message.getChild().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getFirstName())) {
            annonce.setPrenomEnfant(message.getChild().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getChild().getSex())) {
            annonce.setSexeEnfant(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getChild().getSex()));
        }
        if (message.getChild().getDateOfBirth() != null) {
            annonce.setDateNaissanceEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getChild().getDateOfDeath() != null) {
            annonce.setDateMortEnfant(JadeDateUtil.getGlobazFormattedDate(
                    message.getChild().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        annonce.setBaseLegale(RafamLegalBasis.getLegalBasis(message.getLegalBasis().getLaw()).getCodeCentrale());
        if (message.getLegalBasis().getCanton() != null) {
            annonce.setCanton(message.getLegalBasis().getCanton().value());
        }

        if (message.getValidityPeriod() != null) {
            annonce.setDebutDroit(ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod().getStart()));
            annonce.setEcheanceDroit(
                    ALDateUtils.XMLGregorianCalendarToGlobazDate(message.getValidityPeriod().getEnd()));
        }

        // allocataire
        try {
            if (message.getBeneficiary().getVn() != null) {
                annonce.setNssAllocataire(nssf.format(Long.toString(message.getBeneficiary().getVn())));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69c : unable to check beneficiary's NSS "
                            + e.getMessage());
        }

        if (!JadeStringUtil.isBlank(message.getBeneficiary().getOfficialName())) {
            annonce.setNomAllocataire(message.getBeneficiary().getOfficialName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getFirstName())) {
            annonce.setPrenomAllocataire(message.getBeneficiary().getFirstName());
        }
        if (!JadeStringUtil.isBlank(message.getBeneficiary().getSex())) {
            annonce.setSexeAllocataire(
                    ALImplServiceLocator.getAnnonceRafamBusinessService().getSexeCS(message.getBeneficiary().getSex()));
        }
        if (message.getBeneficiary().getDateOfBirth() != null) {
            annonce.setDateNaissanceAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime()));
        }
        if (message.getBeneficiary().getDateOfDeath() != null) {
            annonce.setDateMortAllocataire(JadeDateUtil.getGlobazFormattedDate(
                    message.getBeneficiary().getDateOfDeath().getYearMonthDay().toGregorianCalendar().getTime()));
        }

        annonce.setCodeStatutFamilial(
                RafamFamilyStatus.getFamilyStatus(message.getBeneficiary().getFamilialStatus()).getCodeCentrale());

        annonce.setCodeTypeActivite(RafamOccupationStatus
                .getOccupationStatus(message.getBeneficiary().getOccupationStatus()).getCodeCentrale());

        annonce.setDateCreation(
                JadeDateUtil.getGlobazFormattedDate(message.getCreationDate().toGregorianCalendar().getTime()));

        if (message.getMutationDate() != null) {
            annonce.setDateMutation(
                    JadeDateUtil.getGlobazFormattedDate(message.getMutationDate().toGregorianCalendar().getTime()));
        }

        if (message.getCanceled() == null) {
            annonce.setCanceled(false);
        } else {
            annonce.setCanceled(0 == message.getCanceled() ? true : false);
        }

        if (message.getReturnCode() != null) {
            annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(Long.toString(message.getReturnCode())).getCode());
        } else {

            if ((message.getError() != null) && !message.getError().isEmpty()) {
                annonce.setCodeRetour(RafamReturnCode.EN_ERREUR.getCode());
            } else if (annonce.getCanceled()) {
                annonce.setCodeRetour(RafamReturnCode.ANNULEE.getCode());
            } else {
                annonce.setCodeRetour(RafamReturnCode.TRAITE.getCode());
            }
        }

        // ////////////////////////////////////////////////////////////////
        // Informations internes
        if (initial) {
            annonce.setTypeAnnonce(RafamTypeAnnonce._68A_CREATION.getCode());
            annonce.setEvDeclencheur(RafamEvDeclencheur.CREATION.getCS());
        } else {
            annonce.setTypeAnnonce(RafamTypeAnnonce._69C_REGISTER_STATUS.getCode());
            annonce.setEvDeclencheur(RafamEvDeclencheur.ETAT_REGISTRE.getCS());
        }

        annonce.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
        annonce.setInternalOfficeReference("ED-" + message.getRecordNumber().toString().substring(0, 2) + "-"
                + ALRafamUtils.translateInRefNumber(message.getRecordNumber().toString()));
        annonce.setInternalError(false);
        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce.setIdAllocataire("0");
        annonce.setIdDroit("0");
        annonce.setDelegated(new Boolean(true));
        return annonce;
    }

    @Override
    public AnnonceRafamModel initAnnonce69d(al.ch.ech.xmlns.ech_0104_69._4.NoticeType notice)
            throws JadeApplicationException, JadePersistenceException {
        CommonNSSFormater nssf = new CommonNSSFormater();
        AnnonceRafamModel annonce = new AnnonceRafamModel();
        AnnonceRafamModel last = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .getLastAnnonceForRecordNumber(notice.getRecordNumber().toString());

        if (last.isNew()) {
            throw new ALAnnonceRafamException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69d : aucune annonce n'a été trouvée pour le record number "
                            + notice.getRecordNumber().toString());
        }

        annonce.setTypeAnnonce(RafamTypeAnnonce._69D_NOTICE.getCode());
        annonce.setEvDeclencheur(RafamEvDeclencheur.NOTICE.getCS());

        annonce.setInternalError(false);
        annonce.setCanceled(false);
        annonce.setEtat(RafamEtatAnnonce.RECU.getCS());
        annonce.setIdAllocataire(last.getIdAllocataire());
        annonce.setIdDroit(last.getIdDroit());

        annonce.setRecordNumber(notice.getRecordNumber().toString());
        annonce.setGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(notice.getFamilyAllowanceType()).getCodeCentrale());

        annonce.setDateCreation(
                JadeDateUtil.getGlobazFormattedDate(notice.getCreationDate().toGregorianCalendar().getTime()));
        if (notice.getMutationDate() != null) {
            annonce.setDateMutation(
                    JadeDateUtil.getGlobazFormattedDate(notice.getMutationDate().toGregorianCalendar().getTime()));
        }

        annonce.setCodeRetour(RafamReturnCode.getRafamReturnCode(Long.toString(notice.getReturnCode())).getCode());

        // NSS
        try {
            annonce.setNssEnfant(nssf.format(Long.toString(notice.getChild().getVn())));
            annonce.setNssAllocataire(nssf.format(Long.toString(notice.getBeneficiary().getVn())));

        } catch (Exception e) {
            throw new ALRafamSedexException(
                    "InitAnnoncesRafamServiceImpl#initAnnonce69d : unable to check NSS " + e.getMessage());
        }

        return annonce;
    }

    /**
     * Retourne le canton base legale pour un dossier allocation familiale
     * selon la cascade 1) canton Droit 2) canton Validité 3) canton Affilié
     *
     * @param dossier : le dossier.
     * @param droit   : le droit.
     * @param annonce : l'annonce.
     * @return String du canton
     */
    private String getCantonBaseLegaleCascade(DossierComplexModel dossier, DroitComplexModel droit, AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException {
        String dateDebut =  droit.getDroitModel().getDebutDroit();
        String dateFin =  droit.getDroitModel().getFinDroitForcee();
        Date dateDuJour = new Date();
        String dateCalcul = JadeDateUtil.getGlobazFormattedDate(dateDuJour);
        if(!JadeStringUtil.isEmpty(dateDebut)
            && JadeDateUtil.getGlobazDate(dateDebut).after(dateDuJour)) {
            dateCalcul = dateDebut;
        } else if(!JadeStringUtil.isEmpty(dateFin)
            && JadeDateUtil.getGlobazDate(dateFin).before(dateDuJour)) {
            dateCalcul = dateFin;
        }
        List<CalculBusinessModel> droitsList = new ArrayList<>();

        droitsList = ALServiceLocator.getCalculBusinessService().getCalcul(dossier, dateCalcul);

        CalculBusinessModel droitTrouve = null;
        for (CalculBusinessModel droitIteration : droitsList) {
           if (annonce.getIdDroit().equals(droitIteration.getDroit().getDroitModel().getIdDroit())) {
                droitTrouve = droitIteration;
           }
        }

        String canton;
        if (droitTrouve != null && !JadeStringUtil.isEmpty((droitTrouve).getTarif())) {
            canton = JadeCodesSystemsUtil.getCode((droitTrouve).getTarif());
        } else if (StringUtils.isNotEmpty(dossier.getDossierModel().getTarifForce())
                && !StringUtils.equals("0", dossier.getDossierModel().getTarifForce())
                && StringUtils.isNotEmpty(dossier.getDossierModel().getCantonImposition())) {
            canton = JadeCodesSystemsUtil.getCode(dossier.getDossierModel().getCantonImposition());
        } else {
            canton = getCantonBaseLegale(dossier, droit);
        }

        // Force le canton à VD sur les annonces rafam pour les tarifs spéciaux FPV
        if (isTarifSpecifiqueFPV(canton)) {
            canton = "VD";
        }

        // Si ce n'est pas un canton, se trouve dans un tarif spécifique forcer au canton de l'affiliation
        if(!isCantonSuisse(canton)){
            canton = getCantonBaseLegale(dossier, droit);
        }

        return canton;
    }

    private boolean isCantonSuisse(String canton){
        return ALCSCantons.CANTONS.contains(canton);
    }

    /** Permet de savoir si l'on se trouve dans un tarif spécifique FPV et non un canton
     * @param canton
     * @return true si le canton est un tarif spécifique FPV et non un canton
     */
    private boolean isTarifSpecifiqueFPV(String canton) {
        return StringUtils.equals(canton, "FPV_IAV")
                    || StringUtils.equals(canton, "FPV_GAR")
                    || StringUtils.equals(canton, "FPV_GEOM")
                    || StringUtils.equals(canton, "FPV_LIBR")
                    || StringUtils.equals(canton, "FPV_AT")
                    || StringUtils.equals(canton, "FPV_NOT")
                    || StringUtils.equals(canton, "FPV_AGA")
                    || StringUtils.equals(canton, "FPV_AFIT");
    }
}