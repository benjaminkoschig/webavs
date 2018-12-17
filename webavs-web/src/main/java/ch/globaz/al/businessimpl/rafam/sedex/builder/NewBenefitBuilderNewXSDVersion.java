package ch.globaz.al.businessimpl.rafam.sedex.builder;

import java.math.BigInteger;
import java.util.Date;
import al.ch.ech.xmlns.ech_0007._6.CantonAbbreviationType;
import al.ch.ech.xmlns.ech_0104._4.Contact;
import al.ch.ech.xmlns.ech_0104._4.LegalBasis;
import al.ch.ech.xmlns.ech_0104._4.ValidityPeriod;
import al.ch.ech.xmlns.ech_0104_68._4.NewBenefitType;
import al.ch.ech.xmlns.ech_0104_68._4.NewBenefitType.Beneficiary;
import al.ch.ech.xmlns.ech_0104_68._4.NewBenefitType.Child;
import al.ch.ech.xmlns.ech_0104_68._4.ObjectFactory;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.util.CommonNSSFormater;

/**
 * Builder gérant la préparation d'annonce de création (68a)
 *
 * @author jts
 *
 */
public class NewBenefitBuilderNewXSDVersion extends MessageBuilderAbstractNewXSDVersion {

    /**
     * Constructeur
     *
     * @param annonce
     *            L'annonce à préparer
     */
    public NewBenefitBuilderNewXSDVersion(AnnonceRafamComplexModel annonce) {
        this.annonce = annonce;
    }

    @Override
    public Object build() throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamBusinessService s = ALImplServiceLocator.getAnnonceRafamBusinessService();

        if (annonceIsValid()) {
            NewBenefitType message = buildMessage();
            s.setEtat(annonce.getAnnonceRafamModel(), RafamEtatAnnonce.TRANSMIS);
            return message;
        } else {
            s.setError(annonce.getAnnonceRafamModel(), "al.rafam.send.error.format");
            return null;
        }
    }

    /**
     * Prépare l'annonce
     *
     * @return l'annonce préparée
     *
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private NewBenefitType buildMessage() throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        ObjectFactory of = new ObjectFactory();
        NewBenefitType message = of.createNewBenefitType();

        message.setDelegated(annonce.getAnnonceRafamModel().getDelegated());

        message.setDeliveryOffice(buildDeliveryOfficeObject(annonce.getAnnonceRafamModel()));
        message.setLegalOffice(buildLegalOfficeObject(annonce.getAnnonceRafamModel()));
        message.setRecordNumber(new BigInteger(annonce.getAnnonceRafamModel().getRecordNumber()));

        if (!JadeStringUtil.isBlank(annonce.getAnnonceRafamModel().getInternalOfficeReference())) {
            message.setInternalOfficeReference(annonce.getAnnonceRafamModel().getInternalOfficeReference());
        }
        // pas de dossier pour les annonces délégués, on ne met pas cette info
        if (!annonce.getAnnonceRafamModel().getDelegated()) {
            Contact contact = getContact(annonce.getDossierModel().getNumeroAffilie(),
                    JadeDateUtil.getGlobazFormattedDate(new Date()));
            if (contact != null) {
                message.setContact(contact);
            }
        }

        Child child = of.createNewBenefitTypeChild();
        try {
            child.setVn(Long.parseLong(nssf.unformat(annonce.getAnnonceRafamModel().getNssEnfant())));
            if(!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getCodeCentralePaysEnfant())) {
                child.setCountryId(
                    Integer.valueOf(CODE_PAYS_OFS + annonce.getAnnonceRafamModel().getCodeCentralePaysEnfant()));
            }
        } catch (Exception e) {
            throw new ALRafamSedexException("NewBenefitBuilder#buildMessage : unable to format NSS");
        }

        message.setChild(child);

        message.setFamilyAllowanceType(RafamFamilyAllowanceType
                .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()).getCodeCentrale());

        LegalBasis legalBasis = new LegalBasis();
        legalBasis.setCanton(CantonAbbreviationType.valueOf(annonce.getAnnonceRafamModel().getCanton()));
        legalBasis.setLaw(
                RafamLegalBasis.getLegalBasis(annonce.getAnnonceRafamModel().getBaseLegale()).getCodeCentrale());
        message.setLegalBasis(legalBasis);

        if (!RafamFamilyAllowanceType.ADOPTION.equals(
                RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()))
                && !RafamFamilyAllowanceType.NAISSANCE.equals(RafamFamilyAllowanceType
                        .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()))
                && !RafamFamilyAllowanceType.DIFFERENCE_ADOPTION.equals(RafamFamilyAllowanceType
                        .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()))
                && !RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE.equals(RafamFamilyAllowanceType
                        .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()))) {
            ValidityPeriod period = new ValidityPeriod();
            period.setStart(
                    ALDateUtils.globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getDebutDroit()));
            period.setEnd(
                    ALDateUtils.globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getEcheanceDroit()));
            message.setValidityPeriod(period);
        }

        Beneficiary beneficiary = of.createNewBenefitTypeBeneficiary();
        try {
            beneficiary.setVn(Long.parseLong(nssf.unformat(annonce.getAnnonceRafamModel().getNssAllocataire())));
        } catch (Exception e) {
            throw new ALRafamSedexException("NewBenefitBuilder#buildMessage : unable to format NSS");
        }
        beneficiary.setOccupationStatus(RafamOccupationStatus
                .getOccupationStatus(annonce.getAnnonceRafamModel().getCodeTypeActivite()).getCodeCentrale());
        beneficiary.setFamilialStatus(RafamFamilyStatus
                .getFamilyStatus(annonce.getAnnonceRafamModel().getCodeStatutFamilial()).getCodeCentrale());
        message.setBeneficiary(beneficiary);

        return message;
    }
}
