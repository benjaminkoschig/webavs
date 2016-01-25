package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.gdk_cds.xmlns.pv_common._3.AddressType;
import ch.gdk_cds.xmlns.pv_common._3.DecreeType;
import ch.gdk_cds.xmlns.pv_common._3.InsuranceDataType;
import ch.gdk_cds.xmlns.pv_common._3.InsuranceQueryResultType;
import ch.gdk_cds.xmlns.pv_common._3.PersonType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

public abstract class AnnonceHandlerAbstract {
    protected SimpleAnnonceSedex annonceSedex = new SimpleAnnonceSedex();

    /**
     * Traitement d'une erreur lors de la réception. (Avec passage du type, sous-type et de l'expéditeur pour avoir des
     * infos sur l'annonce en erreur)
     * 
     * @param annonceSedex
     * @param e
     * @param subType
     * @param type
     * @param idSender
     */
    protected void _setErrorOnReception(SimpleAnnonceSedex annonceSedex, Throwable e, Object message) {
        try {
            String subject = "Web@Amal : Erreur lors de la réception d'une annonce !";
            String body = "Une erreur s'est produite lors de la réception d'une annonce : \n\n";
            body += "Emetteur : " + annonceSedex.getMessageEmetteur();
            body += "\nSous-type : "
                    + AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(annonceSedex.getMessageSubType());

            if (!(e == null) && !JadeStringUtil.isEmpty(e.getMessage())) {
                body += "\nErreur(s) : \n\t" + e.getMessage();
            }

            if (e.getCause() != null) {
                body += "\n" + e.getCause().getMessage();
            }

            if (JadeStringUtil.isBlankOrZero(body)) {
                body = " " + e.toString();
            }

            JadeThread.logError("SendAnnonce", body);

            String file = null;
            String fileName[] = null;
            try {
                Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5201_000101._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5211_000102._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5211_000103._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5201_000201._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5211_000202._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5211_000203._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5211_000301._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5202_000401._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5212_000402._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5203_000501._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5213_000601._3.Message.class,
                        ch.gdk_cds.xmlns.pv_5214_000701._3.Message.class };
                file = JAXBServices.getInstance().marshal(message, false, false, addClasses);
                fileName = new String[1];
                fileName[0] = file;
            } catch (Exception ex) {
                String errMsg = "";
                if (ex.getMessage() != null) {
                    errMsg = e.getMessage();
                }
                if (ex.getCause() != null) {
                    errMsg += "\nCause :";
                    errMsg += e.getCause().getMessage();
                }

                if (JadeStringUtil.isBlankOrZero(errMsg)) {
                    errMsg = " " + ex.toString();
                }
                body += "\nErreur lors du marshalling du message" + errMsg;
            }

            try {
                JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                        subject, body, fileName);
                JadeThread.logClear();
            } catch (Exception ex) {
                ex.printStackTrace();
                JadeThread.logError("SendErrorMail", "Error on reception : " + ex.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeThread.logInfo("SendErrorMail", "Error  : " + ex.getMessage());
            // Mise a jour échouée, on ne fait rien...
        }
    }

    /**
     * Execution du traitement de l'annonce.
     * 
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public abstract SimpleAnnonceSedex execute() throws JadeApplicationException, JadePersistenceException;

    public StringBuffer getAddressTypeInfos(AddressType addressType) {
        StringBuffer stringBufferInfos = new StringBuffer();
        return stringBufferInfos;
    }

    public StringBuffer getDecreeTypeInfos(DecreeType decreeType) {
        StringBuffer mapInfos = new StringBuffer();
        mapInfos.append("<strong>Données de la décision :</strong></br>");
        mapInfos.append("Montant mensuel : " + decreeType.getAmount().toString() + "</br>");
        if (decreeType.isLimitation()) {
            mapInfos.append("Limitation : Oui" + "</br>");
        } else {
            mapInfos.append("Limitation : Non" + "</br>");
        }

        String startDate = AMSedexRPUtil.getDateXMLToString(decreeType.getBeginMonth());
        mapInfos.append("Mois de début : " + startDate + "</br>");

        String endDate = AMSedexRPUtil.getDateXMLToString(decreeType.getEndMonth());
        mapInfos.append("Mois de fin : " + endDate + "</br>");
        mapInfos.append("***********************************************</br>");

        return mapInfos;
    }

    /**
     * Récupération des informations conçernant l'annonce
     * 
     * @return StringBuffer avec les infos de l'annonce
     */
    public abstract StringBuffer getDetailsAnnonce();

    /**
     * Formate les informations de l'objet InsuranceDataType
     * 
     * @param insuranceDataType
     * @return
     */
    public StringBuffer getInsuranceDataTypeInfos(InsuranceDataType insuranceDataType) {
        StringBuffer mapInfos = new StringBuffer();
        mapInfos.append("<strong>Données de l'assureur maladie :</strong></br>");
        mapInfos.append("Prime : " + insuranceDataType.getPremium().toString() + "</br>");
        if (insuranceDataType.isAccident()) {
            mapInfos.append("Accident : Oui</br>");
        } else {
            mapInfos.append("Accident : Non</br>");
        }

        String startDate = AMSedexRPUtil.getDateXMLToString(insuranceDataType.getContractStartDate());
        mapInfos.append("Début du rapport d'assurance : " + startDate + "</br>");

        String endDate = AMSedexRPUtil.getDateXMLToString(insuranceDataType.getContractEndDate());
        if (!JadeStringUtil.isBlankOrZero(endDate)) {
            mapInfos.append("Fin du rapport d'assurance : " + endDate + "</br>");
        }
        mapInfos.append("***********************************************</br>");

        return mapInfos;
    }

    /**
     * Formate les informations de l'objet InsuranceQueryResultType
     * 
     * @param insuranceQueryResultType
     * @return
     */
    public StringBuffer getInsuranceQueryResultTypeInfos(InsuranceQueryResultType insuranceQueryResultType) {
        StringBuffer mapInfos = new StringBuffer();
        return mapInfos;
    }

    /**
     * Formate les informations de l'objet PersonType
     * 
     * @param personType
     * @return
     */
    public StringBuffer getPersonTypeInfos(PersonType personType) {
        StringBuffer mapInfos = new StringBuffer();
        return mapInfos;
    }

    /**
     * Retrouve un enregistrement simpleAnnonce du même DecreeId
     * 
     * @param referenceMessageId
     *            Le decreeId
     * @param subTypeMessage
     *            A indiquer si l'on veut un sous-type spécifique, sinon null ou zéro
     * @return
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    protected SimpleAnnonceSedex getRelatedDecree(String decreeId, String referenceMessageId)
            throws AnnonceSedexException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (JadeStringUtil.isBlankOrZero(decreeId) && JadeStringUtil.isBlankOrZero(referenceMessageId)) {
            throw new AnnonceSedexException(
                    "Erreur, le decreeId ne peut pas être vide pour rechercher l'annonce parente !");
        }

        SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();

        if (!JadeStringUtil.isBlankOrZero(decreeId)) {
            simpleAnnonceSedexSearch.setForNumeroDecision(decreeId);
        }

        // BZ 9076 - SEDEX RP : Problème lorsque le decreeID est à 0
        if (!JadeStringUtil.isBlankOrZero(referenceMessageId)) {
            simpleAnnonceSedexSearch.setForMessageId(referenceMessageId);
        }

        simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(
                simpleAnnonceSedexSearch);

        if (simpleAnnonceSedexSearch.getSize() > 0) {
            SimpleAnnonceSedex simpleAnnonceSedex = (SimpleAnnonceSedex) simpleAnnonceSedexSearch.getSearchResults()[0];
            return simpleAnnonceSedex;
        } else {
            simpleAnnonceSedexSearch.setForNumeroDecision("");
            simpleAnnonceSedexSearch.setForMessageId(referenceMessageId);
            simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(
                    simpleAnnonceSedexSearch);
            if (simpleAnnonceSedexSearch.getSize() > 0) {
                SimpleAnnonceSedex simpleAnnonceSedex = (SimpleAnnonceSedex) simpleAnnonceSedexSearch
                        .getSearchResults()[0];
                return simpleAnnonceSedex;
            } else {
                throw new AnnonceSedexException("Erreur retrieve Parent annonce with referenceMessageId :"
                        + referenceMessageId);
            }
        }
    }

    /**
     * Insertion de l'annonce en DB
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws AnnonceSedexException
     * @throws DetailFamilleException
     */
    protected void recordAnnonce(XMLGregorianCalendar messageDateTime) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException {

        try {
            // BZ9279 : Récupérer la date d'une réponse dans le fichier XML et non pas par rapport à la date du jour
            String s_day = JadeStringUtil.fillWithZeroes(String.valueOf(messageDateTime.getDay()), 2);
            String s_month = JadeStringUtil.fillWithZeroes(String.valueOf(messageDateTime.getMonth()), 2);
            String s_year = JadeStringUtil.fillWithZeroes(String.valueOf(messageDateTime.getYear()), 4);
            String dateMessage = s_day + "." + s_month + "." + s_year;
            annonceSedex.setDateMessage(dateMessage);
        } catch (Exception e) {
            // En cas d'erreur, on prend la date du jour
            String dateToday = "";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            dateToday = sdf.format(cal.getTime());
            annonceSedex.setDateMessage(dateToday);
        }

        annonceSedex.setStatus(AMStatutAnnonceSedex.RECU.getValue());
    }

    protected Object unMarshallFromText(String text, Class<?>[] addClasses) throws Exception {
        // Instancie les services JAXB
        JAXBServices jaxb = JAXBServices.getInstance();
        // Construction de l'objet JAXB Content
        ByteArrayInputStream inputContent = new ByteArrayInputStream(text.getBytes("UTF-8"));
        Object o_content = jaxb.unmarshal(inputContent, false, false, addClasses);

        return o_content;
    }
}
