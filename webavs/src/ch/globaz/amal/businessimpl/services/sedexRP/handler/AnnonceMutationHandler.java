package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import ch.gdk_cds.xmlns.pv_5211_000301._3.Message;
import ch.gdk_cds.xmlns.pv_5211_000301._3.MutationType;
import ch.gdk_cds.xmlns.pv_5211_000301._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_common._3.InsuranceDataType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Gestionnaire de réception pour les annonces 'Mutation du rapport d'assurance'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceMutationHandler extends AnnonceHandlerAbstract {
    private Message mutation;

    public AnnonceMutationHandler(Message mutation) {
        this.mutation = mutation;
    }

    private boolean bigDecimalValuesNullAreDifferent(BigDecimal b1, BigDecimal b2) {
        boolean b1Null = false;
        boolean b2Null = false;

        if (b1 == null) {
            b1 = new BigDecimal("0");
            b1Null = true;
        }

        if (b2 == null) {
            b2 = new BigDecimal("0");
            b2Null = true;
        }

        if (b1Null && b2Null) {
            return false;
        }

        if (b1.compareTo(b2) != 0) {
            return true;
        }

        return false;
    }

    @Override
    public SimpleAnnonceSedex execute() {
        String idTiersCM = null;
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(mutation.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(mutation.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(mutation.getHeader().getRecipientId());
            annonceSedex.setMessageId(mutation.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(mutation).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(mutation).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.MUTATION_RAPPORT_ASSURANCE.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.MUTATION_RAPPORT_ASSURANCE.getValue());
            annonceSedex.setNumeroCourant(mutation.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(mutation.getContent().getMutation().getDecreeId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.A_TRAITER.getValue());

            // Récupération de l'annonce parente
            SimpleAnnonceSedex parentAnnonceSedex = getRelatedDecree(mutation.getContent().getMutation().getDecreeId(),
                    null);

            annonceSedex.setIdDetailFamille(parentAnnonceSedex.getIdDetailFamille());
            annonceSedex.setIdContribuable(parentAnnonceSedex.getIdContribuable());

            // Set de l'idTiers caisse maladie
            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(mutation.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);
            // Création de l'annonce
            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

            try {
                SimpleDetailFamille sdf = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                        annonceSedex.getIdDetailFamille());
                BigDecimal contrib = new BigDecimal(sdf.getMontantContributionAvecSupplExtra());
                BigDecimal prime = mutation.getContent().getMutation().getInsuranceData().getPremium();
                BigDecimal primeExact = contrib.subtract(prime);

                sdf.setMontantPrimeAssurance(prime.setScale(2).toString());
                sdf.setMontantExact(primeExact.setScale(2).toString());

                sdf = AmalImplServiceLocator.getSimpleDetailFamilleService().update(sdf);
            } catch (Exception e) {
                // On ne remonte pas d'erreur mais la prime n'est pas enregistrée
            }
        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, mutation);
        }

        return annonceSedex;
    }

    /**
     * @param insuranceData
     * @param insuranceDataOriginal
     * @return
     */
    private StringBuffer findDifference(InsuranceDataType insuranceData, InsuranceDataType insuranceDataOriginal) {
        ArrayList<String> diff = new ArrayList<String>();
        ArrayList<String> orig = new ArrayList<String>();
        ArrayList<String> lib = new ArrayList<String>();

        if (insuranceData.getPremium().compareTo(insuranceDataOriginal.getPremium()) != 0) {
            lib.add("Prime");
            orig.add(insuranceDataOriginal.getPremium().toString());
            diff.add(insuranceData.getPremium().setScale(2).toString());
        }

        if (insuranceData.isAccident() != insuranceDataOriginal.isAccident()) {
            lib.add("Accident");
            orig.add(insuranceDataOriginal.isAccident() ? "Oui" : "Non");
            diff.add(insuranceData.isAccident() ? "Oui" : "Non");
        }

        String startDate = AMSedexRPUtil.getDateXMLToString(insuranceData.getContractStartDate());
        String startDateOriginal = AMSedexRPUtil.getDateXMLToString(insuranceDataOriginal.getContractStartDate());
        if (stringValuesNullAreDifferent(startDate, startDateOriginal)) {
            lib.add("Début rapport assurance");
            orig.add(startDateOriginal);
            diff.add(startDate);
        }

        String endDate = AMSedexRPUtil.getDateXMLToString(insuranceData.getContractEndDate());
        String endDateOriginal = AMSedexRPUtil.getDateXMLToString(insuranceDataOriginal.getContractEndDate());
        if (stringValuesNullAreDifferent(endDate, endDateOriginal)) {
            lib.add("Fin rapport assurance");
            orig.add(endDateOriginal);
            diff.add(endDate);
        }

        if (bigDecimalValuesNullAreDifferent(insuranceData.getFranchise(), insuranceDataOriginal.getFranchise())) {
            lib.add("Franchise annuelle");
            orig.add(insuranceDataOriginal.getFranchise().toString());
            diff.add(insuranceData.getFranchise().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getPremiumGroup(), insuranceDataOriginal.getPremiumGroup())) {
            lib.add("Groupe tarifaire");
            orig.add(insuranceDataOriginal.getPremiumGroup().toString());
            diff.add(insuranceData.getPremiumGroup().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getProductName(), insuranceDataOriginal.getProductName())) {
            lib.add("Nom du produit");
            orig.add(insuranceDataOriginal.getProductName().toString());
            diff.add(insuranceData.getProductName().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getBonus(), insuranceDataOriginal.getBonus())) {
            lib.add("Produit bonus");
            orig.add(insuranceDataOriginal.getBonus().toString());
            diff.add(insuranceData.getBonus().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getPremiumAge(), insuranceDataOriginal.getPremiumAge())) {
            lib.add("Groupe d'âge tarifaire");
            orig.add(insuranceDataOriginal.getPremiumAge().toString());
            diff.add(insuranceData.getPremiumAge().toString());
        }

        String pc = "0";
        String pc_o = "0";
        if (insuranceData.getPremiumCountry() == null) {
            pc = "0";
        } else {
            pc = insuranceData.getPremiumCountry().toString();
        }
        if (insuranceDataOriginal.getPremiumCountry() == null) {
            pc_o = "0";
        } else {
            pc_o = insuranceDataOriginal.getPremiumCountry().toString();
        }
        if (stringValuesNullAreDifferent(pc, pc_o)) {
            lib.add("Pays tarifaire");
            orig.add(insuranceDataOriginal.getPremiumCountry().toString());
            diff.add(insuranceData.getPremiumCountry().toString());
        }

        String pzch = "";
        String pzch_o = "";
        if (insuranceData.getPremiumZoneCH() == null) {
            pzch = "";
        } else {
            pzch = insuranceData.getPremiumZoneCH().toString();
        }
        if (insuranceDataOriginal.getPremiumZoneCH() == null) {
            pzch_o = "";
        } else {
            pzch_o = insuranceDataOriginal.getPremiumZoneCH().toString();
        }
        if (stringValuesNullAreDifferent(pzch, pzch_o)) {
            lib.add("Zone tarifaire CH");
            orig.add(insuranceDataOriginal.getPremiumZoneCH().toString());
            diff.add(insuranceData.getPremiumZoneCH().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getPremiumZoneForeign(),
                insuranceDataOriginal.getPremiumZoneForeign())) {
            lib.add("Zone tarifaire ETR");
            orig.add(insuranceDataOriginal.getPremiumZoneForeign().toString());
            diff.add(insuranceData.getPremiumZoneForeign().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getPremiumRegionCH(), insuranceDataOriginal.getPremiumRegionCH())) {
            lib.add("Région tarifaire CH");
            orig.add(insuranceDataOriginal.getPremiumRegionCH().toString());
            diff.add(insuranceData.getPremiumRegionCH().toString());
        }

        if (stringValuesNullAreDifferent(insuranceData.getPremiumRegionForeign(),
                insuranceDataOriginal.getPremiumRegionForeign())) {
            lib.add("Région tarifaire ETR");
            orig.add(insuranceDataOriginal.getPremiumRegionForeign().toString());
            diff.add(insuranceData.getPremiumRegionForeign().toString());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < lib.size(); i++) {
            sb.append(lib.get(i) + " : " + orig.get(i) + " --> " + diff.get(i) + "</br>");
        }

        return sb;
    }

    public String getContentMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();
        StringBuffer diff = new StringBuffer();

        MutationType mutationType = mutation.getContent().getMutation();
        stringBufferInfos.append("<strong>Données de la modification du rapport d'assurance :</strong></br>");
        stringBufferInfos.append("DecreeId : " + mutationType.getDecreeId() + "</br>");
        stringBufferInfos.append("</br>");
        try {
            String mutationDate = AMSedexRPUtil.getDateXMLToString(mutationType.getMutationDate());
            stringBufferInfos.append("Entrée en vigueur : " + mutationDate + "</br>");
            stringBufferInfos.append("</br>");
        } catch (Exception e) {
            e.printStackTrace();
            // On ne fait rien
        }

        try {
            String referenceMessageId = mutation.getContent().getMutation().getDecreeId();
            SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();
            simpleAnnonceSedexSearch.setForNumeroDecision(referenceMessageId);

            // simpleAnnonceSedexSearch.setForMessageSubType(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION
            // .getValue());

            simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(
                    simpleAnnonceSedexSearch);

            SimpleAnnonceSedex parentAnnonceSedex = new SimpleAnnonceSedex();
            if (simpleAnnonceSedexSearch.getSize() > 0) {
                parentAnnonceSedex = (SimpleAnnonceSedex) simpleAnnonceSedexSearch.getSearchResults()[0];

            } else {
                throw new AnnonceSedexException("Erreur retrieve Parent annonce with referenceMessageId :"
                        + referenceMessageId);
            }

            // Récupération de l'annonce parente. Il nous faut l'annonce de confirmation pour comparer ce qui a changé
            // SimpleAnnonceSedex parentAnnonceSedex = this.getRelatedDecree(this.mutation.getContent().getMutation()
            // .getDecreeId(), AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());

            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5211_000102._3.Message.class };
            ch.gdk_cds.xmlns.pv_5211_000102._3.Message mutationTypeOriginal = (ch.gdk_cds.xmlns.pv_5211_000102._3.Message) unMarshallFromText(
                    parentAnnonceSedex.getMessageContent(), addClasses);

            InsuranceDataType idt = mutationTypeOriginal.getContent().getDecreeConfirmation().getInsuranceData();

            diff = findDifference(mutationType.getInsuranceData(), idt);
        } catch (Exception e) {
            stringBufferInfos.append("<strong>Erreur lors de la récupération de l'annonce parente : </br>"
                    + e.getMessage() + "</strong></br>");

            return stringBufferInfos;
        }

        if (!JadeStringUtil.isBlankOrZero(diff.toString())) {
            stringBufferInfos.append("<strong>Différences données de l'assureur :</strong></br>");
            stringBufferInfos.append(diff);
            stringBufferInfos.append("</br>");
        }

        if (!JadeStringUtil.isBlankOrZero(mutationType.getMutationReason())) {
            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            String motif = currentSession.getLabel("SEDEXRP_MUTATION_REASON_" + mutationType.getMutationReason());
            stringBufferInfos.append("Motif de mutation : " + motif + "</br>");
        }
        stringBufferInfos.append("</br>");

        if (!JadeStringUtil.isBlankOrZero(mutationType.getOtherInsurance())) {
            AdministrationSearchComplexModel administrationSearchComplexModel = new AdministrationSearchComplexModel();
            administrationSearchComplexModel.setForCodeAdministrationLike(mutationType.getOtherInsurance());
            try {
                administrationSearchComplexModel = TIBusinessServiceLocator.getAdministrationService().find(
                        administrationSearchComplexModel);

                if (administrationSearchComplexModel.getSearchResults().length == 1) {
                    AdministrationComplexModel administrationComplexModel = (AdministrationComplexModel) administrationSearchComplexModel
                            .getSearchResults()[0];
                    String otherAssurance = administrationComplexModel.getTiers().getDesignation1();
                    stringBufferInfos.append("Autre assurance : " + otherAssurance + "</br>");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // On ne fait rien
            }
        }

        return stringBufferInfos;
    }

    public String getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

    private boolean stringValuesNullAreDifferent(String s1, String s2) {
        boolean s1Null = false;
        boolean s2Null = false;

        if (s1 == null) {
            s1Null = true;
            s1 = "";
        }

        if (s2 == null) {
            s2Null = true;
            s2 = "";
        }

        if (s1Null && s2Null) {
            return false;
        }

        if (!s1.equals(s2)) {
            return true;
        }

        return false;
    }

}
