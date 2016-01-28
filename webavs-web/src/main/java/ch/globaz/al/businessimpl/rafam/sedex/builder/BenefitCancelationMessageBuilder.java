package ch.globaz.al.businessimpl.rafam.sedex.builder;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.util.CommonNSSFormater;
import java.math.BigInteger;
import ch.ech.xmlns.ech_0104_68._3.BenefitCancellationType;
import ch.ech.xmlns.ech_0104_68._3.BenefitCancellationType.Child;
import ch.ech.xmlns.ech_0104_68._3.ObjectFactory;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Builder gérant la préparation d'annonce d'annulation (68c)
 * 
 * @author jts
 * 
 */
public class BenefitCancelationMessageBuilder extends MessageBuilderAbstract {

    /**
     * Constructeur
     * 
     * @param annonce
     *            L'annonce à préparer
     */
    public BenefitCancelationMessageBuilder(AnnonceRafamComplexModel annonce) {
        this.annonce = annonce;
    }

    @Override
    public Object build() throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamBusinessService s = ALImplServiceLocator.getAnnonceRafamBusinessService();

        if (annonceIsValid()) {
            BenefitCancellationType message = buildMessage();
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
    private BenefitCancellationType buildMessage() throws JadeApplicationException, JadePersistenceException {

        CommonNSSFormater nssf = new CommonNSSFormater();
        ObjectFactory of = new ObjectFactory();
        BenefitCancellationType message = of.createBenefitCancellationType();

        message.setDeliveryOffice(buildDeliveryOfficeObject(annonce.getAnnonceRafamModel()));
        message.setRecordNumber(new BigInteger(annonce.getAnnonceRafamModel().getRecordNumber()));

        if (!JadeStringUtil.isBlank(annonce.getAnnonceRafamModel().getInternalOfficeReference())) {
            message.setInternalOfficeReference(annonce.getAnnonceRafamModel().getInternalOfficeReference());
        }

        Child child = of.createBenefitCancellationTypeChild();
        try {
            child.setVn(Long.parseLong(nssf.unformat(annonce.getAnnonceRafamModel().getNssEnfant())));
        } catch (Exception e) {
            throw new ALRafamSedexException("NewBenefitBuilder#buildMessage : unable to format NSS");
        }
        message.setChild(child);

        message.setFamilyAllowanceType(RafamFamilyAllowanceType.getFamilyAllowanceType(
                annonce.getAnnonceRafamModel().getGenrePrestation()).getCodeCentrale());

        return message;
    }
}