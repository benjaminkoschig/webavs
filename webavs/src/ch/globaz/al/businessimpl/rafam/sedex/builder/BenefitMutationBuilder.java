package ch.globaz.al.businessimpl.rafam.sedex.builder;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.util.CommonNSSFormater;
import java.math.BigInteger;
import java.util.Date;
import ch.ech.xmlns.ech_0007._4.CantonAbbreviationType;
import ch.ech.xmlns.ech_0104._3.Contact;
import ch.ech.xmlns.ech_0104._3.LegalBasis;
import ch.ech.xmlns.ech_0104._3.ValidityPeriod;
import ch.ech.xmlns.ech_0104_68._3.BenefitMutationType;
import ch.ech.xmlns.ech_0104_68._3.NewBenefitType.Beneficiary;
import ch.ech.xmlns.ech_0104_68._3.NewBenefitType.Child;
import ch.ech.xmlns.ech_0104_68._3.ObjectFactory;
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

/**
 * Builder gérant la préparation d'annonce de mutation (68b)
 * 
 * @author jts
 * 
 */
public class BenefitMutationBuilder extends MessageBuilderAbstract {

    /**
     * Constructeur
     * 
     * @param annonce
     *            L'annonce à préparer
     */
    public BenefitMutationBuilder(AnnonceRafamComplexModel annonce) {
        this.annonce = annonce;
    }

    @Override
    public Object build() throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamBusinessService s = ALImplServiceLocator.getAnnonceRafamBusinessService();

        if (annonceIsValid()) {
            BenefitMutationType message = buildMessage();
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
    private BenefitMutationType buildMessage() throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        ObjectFactory of = new ObjectFactory();
        BenefitMutationType message = of.createBenefitMutationType();

        message.setDelegated(annonce.getAnnonceRafamModel().getDelegated());

        message.setDeliveryOffice(buildDeliveryOfficeObject(annonce.getAnnonceRafamModel()));
        message.setLegalOffice(buildLegalOfficeObject(annonce.getAnnonceRafamModel()));
        message.setRecordNumber(new BigInteger(annonce.getAnnonceRafamModel().getRecordNumber()));

        if (!JadeStringUtil.isBlank(annonce.getAnnonceRafamModel().getInternalOfficeReference())) {
            message.setInternalOfficeReference(annonce.getAnnonceRafamModel().getInternalOfficeReference());
        }
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
        } catch (Exception e) {
            throw new ALRafamSedexException("NewBenefitBuilder#buildMessage : unable to format NSS");
        }

        message.setChild(child);

        message.setFamilyAllowanceType(RafamFamilyAllowanceType.getFamilyAllowanceType(
                annonce.getAnnonceRafamModel().getGenrePrestation()).getCodeCentrale());

        LegalBasis legalBasis = new LegalBasis();
        legalBasis.setCanton(CantonAbbreviationType.valueOf(annonce.getAnnonceRafamModel().getCanton()));
        legalBasis.setLaw(RafamLegalBasis.getLegalBasis(annonce.getAnnonceRafamModel().getBaseLegale())
                .getCodeCentrale());
        message.setLegalBasis(legalBasis);

        if (!RafamFamilyAllowanceType.ADOPTION.equals(RafamFamilyAllowanceType.getFamilyAllowanceType(annonce
                .getAnnonceRafamModel().getGenrePrestation()))
                && !RafamFamilyAllowanceType.NAISSANCE.equals(RafamFamilyAllowanceType.getFamilyAllowanceType(annonce
                        .getAnnonceRafamModel().getGenrePrestation()))
                && !RafamFamilyAllowanceType.DIFFERENCE_ADOPTION.equals(RafamFamilyAllowanceType
                        .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()))
                && !RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE.equals(RafamFamilyAllowanceType
                        .getFamilyAllowanceType(annonce.getAnnonceRafamModel().getGenrePrestation()))) {

            ValidityPeriod period = new ValidityPeriod();
            period.setStart(ALDateUtils
                    .globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel().getDebutDroit()));
            period.setEnd(ALDateUtils.globazDateToXMLGregorianCalendar(annonce.getAnnonceRafamModel()
                    .getEcheanceDroit()));
            message.setValidityPeriod(period);
        }

        Beneficiary beneficiary = of.createNewBenefitTypeBeneficiary();
        try {
            beneficiary.setVn(Long.parseLong(nssf.unformat(annonce.getAnnonceRafamModel().getNssAllocataire())));
        } catch (Exception e) {
            throw new ALRafamSedexException("NewBenefitBuilder#buildMessage : unable to format NSS");
        }
        beneficiary.setOccupationStatus(RafamOccupationStatus.getOccupationStatus(
                annonce.getAnnonceRafamModel().getCodeTypeActivite()).getCodeCentrale());
        beneficiary.setFamilialStatus(RafamFamilyStatus.getFamilyStatus(
                annonce.getAnnonceRafamModel().getCodeStatutFamilial()).getCodeCentrale());
        message.setBeneficiary(beneficiary);

        return message;
    }

}
