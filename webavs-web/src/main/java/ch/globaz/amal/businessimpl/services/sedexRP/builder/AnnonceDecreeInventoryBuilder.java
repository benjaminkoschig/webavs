package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.gdk_cds.xmlns.pv_5203_000501._3.ContentType;
import ch.gdk_cds.xmlns.pv_5203_000501._3.DecreeInventoryElementType;
import ch.gdk_cds.xmlns.pv_5203_000501._3.DecreeInventoryType;
import ch.gdk_cds.xmlns.pv_5203_000501._3.HeaderType;
import ch.gdk_cds.xmlns.pv_5203_000501._3.Message;
import ch.gdk_cds.xmlns.pv_5203_000501._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_common._3.PersonType;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Fabrique pour les annonces 'Etat des décisions'
 * 
 * @author cbu
 * @version XSD _1
 * 
 */
public class AnnonceDecreeInventoryBuilder extends AnnonceBuilderAbstract {
    private AnnonceInfosContainer annonceInfos = null;

    public AnnonceDecreeInventoryBuilder(SimpleAnnonceSedex simpleAnnonceSedex) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
    }

    public AnnonceDecreeInventoryBuilder(SimpleAnnonceSedex simpleAnnonceSedex, AnnonceInfosContainer annonceInfos) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
        this.annonceInfos = annonceInfos;
    }

    @Override
    public Object createMessage(Object annonceHeader, Object annonceContent) {
        if (annonceHeader instanceof Message) {
            annonceHeader = ((Message) annonceHeader).getHeader();
        }

        if (annonceContent instanceof Message) {
            annonceContent = ((Message) annonceContent).getContent();
        }

        ObjectFactory of = new ObjectFactory();
        Message message = of.createMessage();

        message.setMinorVersion(new BigInteger("0"));

        message.setContent((ContentType) annonceContent);
        message.setHeader((HeaderType) annonceHeader);

        return message;
    }

    @Override
    public Object generateContent() throws AnnonceSedexException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        ObjectFactory of = new ObjectFactory();

        ContentType contentType = of.createContentType();
        DecreeInventoryType decreeInventoryType = of.createDecreeInventoryType();

        // Récupération des infos depuis le container
        decreeInventoryType.setInventoryDate(toXmlDate(annonceInfos.getReferenceDay(), false));
        decreeInventoryType.setBeginDate(toXmlDate(annonceInfos.getPeriodeFrom(), false));
        decreeInventoryType.setEndDate(toXmlDate(annonceInfos.getPeriodeTo(), false));

        // Sélection des subsides de la caisse pour la période définie (année choisie)
        ComplexAnnonceSedexSearch complexAnnonceSedexSearch = new ComplexAnnonceSedexSearch();
        String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(annonceInfos.getRecipientId());
        complexAnnonceSedexSearch.setForCMIdTiersCaisse(idTiers);
        complexAnnonceSedexSearch.setForSUBAnneeHistorique(annonceInfos.getPeriodeFrom().substring(
                annonceInfos.getPeriodeFrom().length() - 4));
        ArrayList<String> arrayInSubType = new ArrayList<String>();
        arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());
        arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue());
        arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.REJET_DECISION.getValue());
        arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.REJET_INTERRUPTION.getValue());

        complexAnnonceSedexSearch.setInSDXMessageSubType(arrayInSubType);
        complexAnnonceSedexSearch.setOrderKey("orderByIdSedexMsg");
        complexAnnonceSedexSearch.setDefinedSearchSize(0);
        complexAnnonceSedexSearch = AmalServiceLocator.getComplexAnnonceSedexService()
                .search(complexAnnonceSedexSearch);

        Map<String, ComplexAnnonceSedex> mapAnnonces = new HashMap<String, ComplexAnnonceSedex>();
        String idSimpleDetailFamille = "";
        for (JadeAbstractModel model : complexAnnonceSedexSearch.getSearchResults()) {
            ComplexAnnonceSedex cas = (ComplexAnnonceSedex) model;

            if (!idSimpleDetailFamille.equals(cas.getSimpleDetailFamille().getIdDetailFamille())) {

                if (AMMessagesSubTypesAnnonceSedex.REJET_DECISION.getValue().equals(
                        cas.getSimpleAnnonceSedex().getMessageSubType())) {
                    idSimpleDetailFamille = cas.getSimpleDetailFamille().getIdDetailFamille();
                    continue;
                }

                if (!mapAnnonces.containsKey(cas.getSimpleDetailFamille().getIdDetailFamille())) {
                    mapAnnonces.put(cas.getSimpleDetailFamille().getIdDetailFamille(), cas);
                }
            }
        }

        // Itération sur les subsides qu'on a trouvé
        for (String id : mapAnnonces.keySet()) {
            ComplexAnnonceSedex sas = mapAnnonces.get(id);
            simpleDetailFamille = sas.getSimpleDetailFamille();
            try {
                // Construction de l'annonce
                DecreeInventoryElementType decreeInventoryElementType = of.createDecreeInventoryElementType();
                decreeInventoryElementType.setDecreeId(sas.getSimpleAnnonceSedex().getNumeroDecision());
                BigDecimal bd_amount = new BigDecimal(sas.getSimpleDetailFamille()
                        .getMontantContributionAvecSupplExtra());
                decreeInventoryElementType.setAmount(bd_amount.setScale(2));

                XMLGregorianCalendar beginMonth = toXmlDate(sas.getSimpleDetailFamille().getDebutDroit(), true);
                String endMonthString = sas.getSimpleDetailFamille().getFinDroit();
                if (JadeStringUtil.isBlankOrZero(endMonthString)) {
                    endMonthString = "12." + sas.getSimpleDetailFamille().getAnneeHistorique();
                }
                XMLGregorianCalendar endMonth = toXmlDate(endMonthString, true);
                decreeInventoryElementType.setBeginMonth(beginMonth);
                decreeInventoryElementType.setEndMonth(endMonth);

                int anneeHistoriqueSubside = 0;
                try {
                    anneeHistoriqueSubside = Integer.valueOf(simpleDetailFamille.getAnneeHistorique());
                } catch (Exception e) {
                    // On laisse anneeHistorique à 0
                }
                // Si le subside est un cas PC année historique > 2011 alors limitation = FALSE sinon TRUE
                if (IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(simpleDetailFamille.getTypeDemande())
                        && (anneeHistoriqueSubside > 2011)) {
                    decreeInventoryElementType.setLimitation(false);
                } else {
                    decreeInventoryElementType.setLimitation(true);
                }
                // decreeInventoryElementType.setDecreeId(sas.getSimpleAnnonceSedex().getNumeroDecision());
                decreeInventoryElementType.setLastBusinessProcessId(new BigInteger(sas.getSimpleAnnonceSedex()
                        .getNumeroCourant()));
                simpleAnnonceSedex = sas.getSimpleAnnonceSedex();
                PersonType newPerson = getPersonType();
                decreeInventoryElementType.setPerson(newPerson);
                decreeInventoryType.getDecreeInventoryElement().add(decreeInventoryElementType);
            } catch (Exception e) {
                String msg = "";
                if (e.getMessage() != null) {
                    msg = e.getMessage();
                }

                if (e.getCause() != null) {
                    msg += e.getCause().getMessage();
                }
                arrayErrors.add("DecreeInventory : Error processing decreeInventoryElement ! ==> " + msg);
            }
        }

        if ((decreeInventoryType.getDecreeInventoryElement() == null)
                || (decreeInventoryType.getDecreeInventoryElement().size() == 0)) {
            return null;
        }

        contentType.setDecreeInventory(decreeInventoryType);

        return contentType;
    }

    /**
     * Génère l'en-tête de l'annonce
     * 
     * @return l'en-tête initialisée
     * 
     * @throws JadeSedexDirectoryInitializationException
     *             Exception levée si dépôt Sedex ne peut être trouvé
     * @throws DatatypeConfigurationException
     *             Exception levée si l'en-tête ne peut être initialisée
     * @throws ALAnnonceRafamException
     *             Exception levée si une erreur métier se produit
     */
    @Override
    public Object generateHeader() throws JadeSedexDirectoryInitializationException, DatatypeConfigurationException {

        // try {
        String messageType = simpleAnnonceSedex.getMessageType();
        String messageSubType = JadeStringUtil.fillWithZeroes(simpleAnnonceSedex.getMessageSubType(), 6);
        String recipientId = annonceInfos.getRecipientId();

        ObjectFactory of = new ObjectFactory();
        HeaderType header = of.createHeaderType();

        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        header.setRecipientId(recipientId);
        header.setMessageId(JadeUUIDGenerator.createStringUUID());

        header.setBusinessProcessId(new BigInteger("1"));

        header.setMessageType(messageType);
        header.setSubMessageType(messageSubType);

        header.setSendingApplication(getSendingApplicationType());

        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar nowDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        header.setMessageDate(nowDateTime);
        header.setAction(IAMSedex.MESSAGE_ACTION_NOUVEAU);
        header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());

        header.setDeclarationLocalReference(getDeclarationLocalReferenceType());

        return header;
        // } catch (Exception e) {
        // this.setErrorOnCreation(e);
        // }
        //
        // return null;
    }

    @Override
    public String getAction(Object message) {
        return ((Message) message).getHeader().getAction();
    }

    @Override
    public String getBusinessProcessId(Object message) {
        return ((Message) message).getHeader().getBusinessProcessId().toString();
    }

    @Override
    public ByteArrayOutputStream getContentMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return os;
    }

    @Override
    public String getDecreeId(Object message) {
        return "";
        // return ((Message) message).getContent().getDecreeInventory().getDecreeId();
    }

    @Override
    public ByteArrayOutputStream getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return os;
    }

    @Override
    public String getMessageId(Object message) {
        return ((Message) message).getHeader().getMessageId();
    }

    @Override
    public String getMinorVersion(Object message) {
        return ((Message) message).getMinorVersion().toString();
    }

    @Override
    public String getRecipientId(Object message) {
        return ((Message) message).getHeader().getRecipientId();
    }

    @Override
    public String getSenderId(Object message) {
        return ((Message) message).getHeader().getSenderId();
    }

    @Override
    public Object send(Object header, Object content) throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class whichClass() {
        return Message.class;
    }
}
