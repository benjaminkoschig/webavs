package ch.globaz.al.utils;

import java.util.ArrayList;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.AllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.BeneficiaryType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildType;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.smtp.JadeSmtpClient;

/**
 *
 * @author jts,gmo
 *
 */

public class ALRafamUtils {
    /**
     * @deprecated à usage temporaire en attendant le passage à Web@AF des caisses FVE, CVCI, CCVD
     */
    @Deprecated
    public static boolean modeAlfaGest = false;

    public static AnnonceRafamModel getLastActive68(AnnonceRafamDelegueComplexModel annonceImported) {
        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(annonceImported.getAnnonceRafamModel().getRecordNumber());
        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        search.setInTypeAnnonce(types);
        search.setOrderKey("lastActive");
        try {
            search = ALServiceLocator.getAnnonceRafamModelService().search(search);
        } catch (Exception e) {
            JadeLogger.warn(ALRafamUtils.class, "Unable to find a 68 type annonce according to "
                    + annonceImported.getAnnonceRafamModel().getTypeAnnonce() + " to getting of childFamilyRelation");
        }

        AnnonceRafamModel lastActive = ((AnnonceRafamModel) search.getSearchResults()[0]);
        return lastActive;

    }

    public static void sendMail(StringBuffer title, StringBuffer description, String[] files) throws Exception {

        String email = JadePropertiesService.getInstance().getProperty(ALConstRafam.RAFAM_CONTACT_EMAIL);

        JadeSmtpClient.getInstance().sendMail(email, title.toString(), description.toString(), files);

    }

    /**
     *
     * @param annonce
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public static AllowanceType toAllowanceAfDelegueFormat(AnnonceRafamDelegueComplexModel annonce)
            throws JadeApplicationException, JadePersistenceException {
        AllowanceType newAllowance = new AllowanceType();

        AnnonceRafamModel last68 = null;
        if (RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())
                || RafamTypeAnnonce._69C_REGISTER_STATUS.getCode()
                        .equals(annonce.getAnnonceRafamModel().getTypeAnnonce())
                || RafamTypeAnnonce._68C_ANNULATION.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())) {
            last68 = ALRafamUtils.getLastActive68(annonce);
        }

        if (!JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getDebutDroit()) && (last68 != null)) {
            if (!JadeStringUtil.isEmpty(last68.getDebutDroit())) {
                newAllowance.setAllowanceDateFrom(ALDateUtils.globazDateToXMLGregorianCalendar(last68.getDebutDroit()));
            } else {
                newAllowance.setAllowanceDateFrom(
                        ALDateUtils.globazDateToXMLGregorianCalendar(ALConstRafam.DATE_DEBUT_MINIMUM));
            }
        } else {
            newAllowance.setAllowanceDateFrom(
                    ALDateUtils.globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getDebutDroit()));
        }

        if (!JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getEcheanceDroit()) && (last68 != null)) {
            if (!JadeStringUtil.isEmpty(last68.getEcheanceDroit())) {
                newAllowance
                        .setAllowanceDateTo(ALDateUtils.globazDateToXMLGregorianCalendar(last68.getEcheanceDroit()));
            } else {
                newAllowance.setAllowanceDateTo(
                        ALDateUtils.globazDateToXMLGregorianCalendar(ALConstRafam.DATE_DEBUT_MINIMUM));
            }
        } else {
            newAllowance.setAllowanceDateTo(
                    ALDateUtils.globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getEcheanceDroit()));
        }

        if ((annonce.getAnnonceRafamModel().getInternalOfficeReference() != null)
                && annonce.getAnnonceRafamModel().getInternalOfficeReference().startsWith("ED-")) {
            newAllowance.setAllowanceRefNumber(annonce.getAnnonceRafamModel().getInternalOfficeReference()
                    .substring(annonce.getAnnonceRafamModel().getInternalOfficeReference().lastIndexOf("-") + 1));
        } else {
            if (last68 != null) {
                newAllowance.setAllowanceRefNumber(last68.getInternalOfficeReference()
                        .substring(last68.getInternalOfficeReference().lastIndexOf("-") + 1));
            }
        }

        if (JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getBaseLegale())) {
            if (last68 != null) {
                newAllowance.setAllowanceApplicableLegislation(
                        RafamLegalBasis.getLegalBasis(last68.getBaseLegale()).getCodeCentrale());
            } else {
                newAllowance.setAllowanceApplicableLegislation(RafamLegalBasis.getLegalBasis("1").getCodeCentrale());
            }

        } else {
            newAllowance.setAllowanceApplicableLegislation(
                    RafamLegalBasis.getLegalBasis(annonce.getAnnonceRafamModel().getBaseLegale()).getCodeCentrale());
        }

        if (JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getCanton())) {
            if (last68 != null) {
                newAllowance.setAllowanceBenefitCanton(last68.getCanton());
            } else {
                newAllowance.setAllowanceBenefitCanton("VD");
            }
        } else {
            newAllowance.setAllowanceBenefitCanton(annonce.getAnnonceRafamModel().getCanton());
        }
        if (RafamTypeAnnonce._68C_ANNULATION.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())) {
            newAllowance.setAllowanceCompleteStorno("01");
        } else {
            newAllowance.setAllowanceCompleteStorno("02");
        }

        newAllowance.setAllowanceType(RafamFamilyAllowanceType
                .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()).getCodeCentrale());

        if (RafamTypeAnnonce._69C_REGISTER_STATUS.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())) {
            newAllowance.setAllowanceErrorMessage(
                    JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getInternalErrorMessage())
                            ? ALConstRafam.MESSAGE_ED_NOERROR_CODE
                            : annonce.getAnnonceRafamModel().getInternalErrorMessage());
        } else {

            ErreurAnnonceRafamSearchModel searchErreurs = new ErreurAnnonceRafamSearchModel();
            searchErreurs.setForIdAnnonce(annonce.getAnnonceRafamModel().getIdAnnonce());
            searchErreurs = ALServiceLocator.getErreurAnnonceRafamModelService().search(searchErreurs);

            if (searchErreurs.getSize() > 0) {
                // TODO v2 mettre la liste des erreurs car ce champ est libre dans le xsd employeur délégué
                newAllowance.setAllowanceErrorMessage(
                        ((ErreurAnnonceRafamModel) searchErreurs.getSearchResults()[0]).getCode());
                //
            } else {
                newAllowance.setAllowanceErrorMessage(ALConstRafam.MESSAGE_ED_NOERROR_CODE);
            }
        }
        // NON GERE, mais pas obligatoire
        // newAllowance.setAllowancePeriodeAmount(value)
        // newAllowance.setAllowancePeriodeFrom(value)
        // newAllowance.setAllowancePeriodeTo(value)
        // newAllowance.setAllowanceDateErrorFrom(value)
        // newAllowance.setAllowanceDateErrorTo(value)
        // newAllowance.setAllowanceErrorCode(value)
        if (annonce.getComplementDelegueModel() != null) {
            newAllowance.setAllowanceAmount(annonce.getComplementDelegueModel().getAllowanceAmount());
        }

        if (JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getCodeCentralePaysEnfant())) {
            if (last68 != null && !JadeStringUtil.isBlankOrZero(last68.getCodeCentralePaysEnfant())) {
                newAllowance.setAllowanceChildCountryResidence(Integer.valueOf(last68.getCodeCentralePaysEnfant()));
            } else {
                newAllowance.setAllowanceChildCountryResidence(0);
            }
        } else {
            newAllowance.setAllowanceChildCountryResidence(
                    Integer.valueOf(annonce.getAnnonceRafamModel().getCodeCentralePaysEnfant()));
        }

        ALRafamUtils.validateAllowanceType(newAllowance);
        return newAllowance;
    }

    /**
     *
     * @param beneficiaire
     *            ch.ech.xmlns.ech_0104_69._3.ReceiptType.Beneficiary
     * @return
     * @throws JadeApplicationException
     */
    public static BeneficiaryType toBeneficiaryAfDelegueFormat(AnnonceRafamDelegueComplexModel annonce)
            throws JadeApplicationException {

        AnnonceRafamModel last68 = null;
        if (RafamTypeAnnonce._68C_ANNULATION.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())) {
            last68 = ALRafamUtils.getLastActive68(annonce);
        }

        BeneficiaryType newBeneficiary = new BeneficiaryType();

        if (JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getNssAllocataire()) && (last68 != null)) {
            newBeneficiary.setBeneficiaryAHVN13(last68.getNssAllocataire());
        } else {
            newBeneficiary.setBeneficiaryAHVN13(annonce.getAnnonceRafamModel().getNssAllocataire());
        }

        if (JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getDateNaissanceAllocataire())) {
            newBeneficiary.setBeneficiaryDateOfBirth(ALDateUtils
                    .globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getDateNaissanceAllocataire()));
        } else if ((last68 != null) && JadeDateUtil.isGlobazDate(last68.getDateNaissanceAllocataire())) {
            newBeneficiary.setBeneficiaryDateOfBirth(
                    ALDateUtils.globazDateToXMLGregorianCalendar(last68.getDateNaissanceAllocataire()));
        }

        if (JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getDateMortAllocataire())) {
            newBeneficiary.setBeneficiaryDateOfDeath(ALDateUtils
                    .globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getDateMortAllocataire()));
        }

        if ((annonce.getComplementDelegueModel() != null)
                && JadeDateUtil.isGlobazDate(annonce.getComplementDelegueModel().getBeneficiaryEndDate())) {
            newBeneficiary.setBeneficiaryEndDateEmployment(ALDateUtils
                    .globazDateToXMLGregorianCalendar(annonce.getComplementDelegueModel().getBeneficiaryEndDate()));
        }
        if ((annonce.getComplementDelegueModel() != null)
                && JadeDateUtil.isGlobazDate(annonce.getComplementDelegueModel().getBeneficiaryStartDate())) {
            newBeneficiary.setBeneficiaryStartDateEmployment(ALDateUtils
                    .globazDateToXMLGregorianCalendar(annonce.getComplementDelegueModel().getBeneficiaryStartDate()));
        }
        if (!JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getPrenomAllocataire())) {
            newBeneficiary.setBeneficiaryFirstName(annonce.getAnnonceRafamModel().getPrenomAllocataire()
                    .length() > ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH
                            ? JadeStringUtil.leftJustify(annonce.getAnnonceRafamModel().getPrenomAllocataire(),
                                    ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH)
                            : annonce.getAnnonceRafamModel().getPrenomAllocataire());
        } else {
            newBeneficiary.setBeneficiaryFirstName(ALConstRafam.MESSAGE_ED_DATA_BLANK);
        }
        // les upi sont créées en base mais ne contient pas l'info code type activité
        if (JadeNumericUtil.isEmptyOrZero(annonce.getAnnonceRafamModel().getCodeTypeActivite())) {
            newBeneficiary.setBeneficiaryStatus(RafamOccupationStatus.SALARIE.getCodeCentrale());
        } else {
            newBeneficiary.setBeneficiaryStatus(RafamOccupationStatus
                    .getOccupationStatus(annonce.getAnnonceRafamModel().getCodeTypeActivite()).getCodeCentrale());
        }

        if (ALCSTiers.SEXE_FEMME.equals(annonce.getAnnonceRafamModel().getSexeAllocataire())) {
            newBeneficiary.setBeneficiaryGender("1");
        } else {
            newBeneficiary.setBeneficiaryGender("0");
        }

        if (!JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getNomAllocataire())) {
            newBeneficiary.setBeneficiarySurname(annonce.getAnnonceRafamModel().getNomAllocataire()
                    .length() > ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH
                            ? JadeStringUtil.leftJustify(annonce.getAnnonceRafamModel().getNomAllocataire(),
                                    ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH)
                            : annonce.getAnnonceRafamModel().getNomAllocataire());
        } else {
            newBeneficiary.setBeneficiarySurname(ALConstRafam.MESSAGE_ED_DATA_BLANK);
        }

        ALRafamUtils.validateBeneficiaryType(newBeneficiary);
        return newBeneficiary;
    }

    /**
     *
     * @return
     * @throws JadeApplicationException
     */
    public static ChildType toChildAfDelegueFormat(AnnonceRafamDelegueComplexModel annonce)
            throws JadeApplicationException {
        ChildType newChild = new ChildType();

        AnnonceRafamModel last68 = null;

        if (RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())
                || RafamTypeAnnonce._69C_REGISTER_STATUS.getCode()
                        .equals(annonce.getAnnonceRafamModel().getTypeAnnonce())
                || RafamTypeAnnonce._68C_ANNULATION.getCode().equals(annonce.getAnnonceRafamModel().getTypeAnnonce())) {
            last68 = ALRafamUtils.getLastActive68(annonce);
            if (!JadeStringUtil.isBlankOrZero(last68.getCodeStatutFamilial())) {
                newChild.setChildFamilyRelation(last68.getCodeStatutFamilial());
            }
        }

        // si vraiment on pas trouvé dans les annonces existantes || que l'annonce n'est pas une UPI, un état registre
        // ou une 68c
        if (JadeStringUtil.isBlankOrZero(newChild.getChildFamilyRelation())
                && !JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getCodeStatutFamilial())) {
            newChild.setChildFamilyRelation(annonce.getAnnonceRafamModel().getCodeStatutFamilial());
        }

        newChild.setChildAHVN13(annonce.getAnnonceRafamModel().getNssEnfant());
        if (JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getDateNaissanceEnfant())) {
            newChild.setChildDateOfBirth(ALDateUtils
                    .globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getDateNaissanceEnfant()));
        } else if ((last68 != null) && JadeDateUtil.isGlobazDate(last68.getDateNaissanceEnfant())) {
            newChild.setChildDateOfBirth(ALDateUtils.globazDateToXMLGregorianCalendar(last68.getDateNaissanceEnfant()));
        }

        if (!JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getPrenomEnfant())) {
            newChild.setChildFirstName(
                    annonce.getAnnonceRafamModel().getPrenomEnfant().length() > ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH
                            ? JadeStringUtil.leftJustify(annonce.getAnnonceRafamModel().getPrenomEnfant(),
                                    ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH)
                            : annonce.getAnnonceRafamModel().getPrenomEnfant());
        } else {
            newChild.setChildFirstName(ALConstRafam.MESSAGE_ED_DATA_BLANK);
        }

        if (ALCSTiers.SEXE_FEMME.equals(annonce.getAnnonceRafamModel().getSexeAllocataire())) {
            newChild.setChildGender("1");
        } else {

            newChild.setChildGender("0");
        }

        if (!JadeStringUtil.isEmpty(annonce.getAnnonceRafamModel().getNomEnfant())) {
            newChild.setChildSurname(
                    annonce.getAnnonceRafamModel().getNomEnfant().length() > ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH
                            ? JadeStringUtil.leftJustify(annonce.getAnnonceRafamModel().getNomEnfant(),
                                    ALConstRafam.MESSAGE_ED_NAME_MAX_LENGTH)
                            : annonce.getAnnonceRafamModel().getNomEnfant());
        } else {
            newChild.setChildSurname(ALConstRafam.MESSAGE_ED_DATA_BLANK);
        }
        ALRafamUtils.validateChildType(newChild);
        return newChild;
    }

    /**
     * Traduit un recordNumber employeur délégué en refNumber employeur délégué (n° utilisé par l'employeur)
     *
     * @param recordNumber
     * @return
     * @throws JadeApplicationException
     */
    public static String translateInRefNumber(String recordNumber) throws JadeApplicationException {

        String prefixEmployeur = recordNumber.substring(0, 2);
        String prefixInReferenceNumber = JadePropertiesService.getInstance()
                .getProperty("al.rafam.delegue." + prefixEmployeur + ".refTranslator");

        if (prefixInReferenceNumber == null) {
            throw new ALAnnonceRafamException(
                    "ALRafamUtils#translateInRefNumber : prefixInReferenceNumber is null:" + recordNumber);
        }

        return prefixInReferenceNumber.concat(recordNumber.substring(2));

    }

    public static boolean validateAllowanceType(AllowanceType allowance) throws JadeApplicationException {

        if (!allowance.getAllowanceRefNumber().matches("[0-9]{16}")) {
            throw new ALRafamException("validateAllowanceType#allowanceRefNumber n'est pas valide");
        }
        if (!allowance.getAllowanceAmount().matches("-?[0-9]+\\.[0-9]{2}")) {
            throw new ALRafamException("validateAllowanceType#allowancAmount n'est pas valide");
        }

        if ((allowance.getAllowanceType() != "01") && (allowance.getAllowanceType() != "02")) {
            if (!allowance.getAllowanceDateFrom().isValid()) {
                throw new ALRafamException("validateAllowanceType#allowanceDateFrom n'est pas valide");
            }

            if (!allowance.getAllowanceDateTo().isValid()) {
                throw new ALRafamException("validateAllowanceType#allowanceDatTo n'est pas valide");
            }
        }
        if (!allowance.getAllowanceApplicableLegislation().matches("[0-9]{2}")) {
            throw new ALRafamException("validateAllowanceType#allowanceApplicableLegislation n'est pas valide");
        }

        if (!allowance.getAllowanceBenefitCanton().matches("[A-Z]{2}")) {
            throw new ALRafamException("validateAllowanceType#sllowanceBenefitCanton n'est pas valide");
        }

        if (!allowance.getAllowanceType().matches("[0-9]{2}")) {
            throw new ALRafamException("validateAllowanceType#allowanceType n'est pas valide");
        }

        if (!String.valueOf(allowance.getAllowanceChildCountryResidence()).matches("[0-9]{4}")) {
            throw new ALRafamException("validateAllowanceType#allowanceType n'est pas valide");
        }

        return true;
    }

    public static boolean validateBeneficiaryType(BeneficiaryType beneficiary) throws JadeApplicationException {
        if (!beneficiary.getBeneficiaryAHVN13().matches("[0-9]{3}\\.[0-9]{4}\\.[0-9]{4}\\.[0-9]{2}")) {
            throw new ALRafamException("validateBeneficiaryType#nss n'est pas valide");
        }

        if (!beneficiary.getBeneficiaryStatus().matches("[0-9]{2}")) {
            throw new ALRafamException("validateBeneficiaryType#beneficiaryStatus n'est pas valide");
        }

        if (!beneficiary.getBeneficiaryStartDateEmployment().isValid()) {
            throw new ALRafamException("validateBeneficiaryType#startDateEmployment n'est pas valide");
        }

        return true;
    }

    public static boolean validateChildType(ChildType child) throws JadeApplicationException {
        if (!child.getChildAHVN13().matches("[0-9]{3}\\.[0-9]{4}\\.[0-9]{4}\\.[0-9]{2}")) {
            throw new ALRafamException("validateChildType#nss n'est pas valide");
        }

        if (!child.getChildFamilyRelation().matches("[0-9]{2}")) {
            throw new ALRafamException("validateChildType#childFamilyRelation n'est pas valide");
        }
        return true;
    }

}
