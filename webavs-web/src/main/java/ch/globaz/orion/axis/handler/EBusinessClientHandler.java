package ch.globaz.orion.axis.handler;

import globaz.jade.context.JadeThread;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterLocator;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import ch.globaz.orion.business.provider.OwnerProvider;

public class EBusinessClientHandler extends GenericHandler {
    private HandlerInfo handlerInfo = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.rpc.handler.GenericHandler#getHeaders()
     */
    @Override
    public QName[] getHeaders() {
        return handlerInfo.getHeaders();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.rpc.handler.GenericHandler#handleRequest(javax.xml.rpc.handler .MessageContext)
     */
    @Override
    public boolean handleRequest(MessageContext context) {
        boolean handleRequest = true;
        try {
            // Cast le message contexte en SOAP message, attention dépend
            // directement de l'implémentation, fonctionne puisque l'on
            // utilise
            // AXIS, mais peut ne plus fonctionner avec d'autres
            // implémentations
            SOAPMessageContext smc = (SOAPMessageContext) context;
            // Récupère le message SOAP
            SOAPMessage message = smc.getMessage();
            // Récupère la partie SOAP du message
            SOAPPart soapPart = message.getSOAPPart();
            // Récupère l'enveloppe SOAP
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // Récupère le Header SOAP afin d'y ajouter un tag
            SOAPHeader header = envelope.getHeader();

            // Ajoute le user id
            Name headerTagName = envelope.createName(SoapHeaderTags.USER_SESSION_LOCAL_NAME,
                    SoapHeaderTags.PREFIX_NAMESPACE, SoapHeaderTags.TARGET_URI);
            // Crée l'élément dans l'entête SOAP
            SOAPHeaderElement nameElement = header.addHeaderElement(headerTagName);
            // Défini la valeur contenue dans l'élément
            nameElement.addTextNode(JadeEncrypterLocator.getInstance()
                    .getAdapter(JadeDefaultEncrypters.GLOBAZ_ENCRYPTER).encrypt(JadeThread.currentUserId()));

            // Ajoute le owner
            headerTagName = envelope.createName(SoapHeaderTags.OWNER_LOCAL_NAME, SoapHeaderTags.PREFIX_NAMESPACE,
                    SoapHeaderTags.TARGET_URI);
            // Crée l'élément dans l'entête SOAP
            nameElement = header.addHeaderElement(headerTagName);
            // Défini la valeur contenue dans l'élément
            nameElement.addTextNode(JadeEncrypterLocator.getInstance()
                    .getAdapter(JadeDefaultEncrypters.GLOBAZ_ENCRYPTER).encrypt(OwnerProvider.getOwner()));

            // Ajoute le user name
            headerTagName = envelope.createName(SoapHeaderTags.USER_NAME_SESSION_LOCAL_NAME,
                    SoapHeaderTags.PREFIX_NAMESPACE, SoapHeaderTags.TARGET_URI);
            // Crée l'élément dans l'entête SOAP
            nameElement = header.addHeaderElement(headerTagName);
            // Défini la valeur contenue dans l'élément
            nameElement.addTextNode(JadeEncrypterLocator.getInstance()
                    .getAdapter(JadeDefaultEncrypters.GLOBAZ_ENCRYPTER).encrypt(JadeThread.currentUserName()));

            // Ajoute le user mail
            headerTagName = envelope.createName(SoapHeaderTags.USER_EMAIL_SESSION_LOCAL_NAME,
                    SoapHeaderTags.PREFIX_NAMESPACE, SoapHeaderTags.TARGET_URI);
            // Crée l'élément dans l'entête SOAP
            nameElement = header.addHeaderElement(headerTagName);
            // Défini la valeur contenue dans l'élément
            nameElement.addTextNode(JadeEncrypterLocator.getInstance()
                    .getAdapter(JadeDefaultEncrypters.GLOBAZ_ENCRYPTER).encrypt(JadeThread.currentUserEmail()));

            // Ajoute la langue locale
            headerTagName = envelope.createName(SoapHeaderTags.USER_LANGUE_SESSION_LOCAL_NAME,
                    SoapHeaderTags.PREFIX_NAMESPACE, SoapHeaderTags.TARGET_URI);
            // Crée l'élément dans l'entête SOAP
            nameElement = header.addHeaderElement(headerTagName);
            // Défini la valeur contenue dans l'élément
            nameElement.addTextNode(JadeEncrypterLocator.getInstance()
                    .getAdapter(JadeDefaultEncrypters.GLOBAZ_ENCRYPTER).encrypt(JadeThread.currentLanguage()));

            // Ajoute un contrôle
            headerTagName = envelope.createName(SoapHeaderTags.OWNER_USER_LOCAL_NAME, SoapHeaderTags.PREFIX_NAMESPACE,
                    SoapHeaderTags.TARGET_URI);
            // Crée l'élément dans l'entête SOAP
            nameElement = header.addHeaderElement(headerTagName);
            // Défini la valeur contenue dans l'élément
            nameElement.addTextNode(JadeEncrypterLocator.getInstance()
                    .getAdapter(JadeDefaultEncrypters.GLOBAZ_ENCRYPTER)
                    .encrypt(OwnerProvider.getOwner() + "_" + JadeThread.currentUserId()));
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Problem to init SOAP Header to access EBusiness Web services, reason : " + e.toString());
            handleRequest = false;
        }
        return handleRequest ? super.handleRequest(context) : handleRequest;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.rpc.handler.GenericHandler#handleResponse(javax.xml.rpc.handler .MessageContext)
     */
    @Override
    public boolean handleResponse(MessageContext context) {
        try {
            SOAPMessageContext smc = (SOAPMessageContext) context;
            // Récupère le message SOAP
            SOAPMessage message = smc.getMessage();
            // Récupère la partie SOAP du message
            SOAPPart soapPart = message.getSOAPPart();
            // Récupère l'enveloppe SOAP
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // Récupère le Header SOAP afin d'y récupérer le tag si il existe
            SOAPHeader header = envelope.getHeader();
            // Récupère les éléments contenus dans le header et les parcours
            Iterator it = header.examineHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);
            while (it.hasNext()) {
                SOAPElement sElement = (SOAPElement) it.next();
                // Regarde que l'élément récupéré soit bien celui que l'on
                // souhaite et affiche un message
                if (SoapHeaderTags.PREFIX_NAMESPACE.equals(sElement.getElementName().getPrefix())
                        && SoapHeaderTags.TARGET_URI.equals(sElement.getElementName().getURI())) {
                    if (SoapHeaderTags.LOG_MESSAGES_LOCAL_NAME.equals(sElement.getElementName().getLocalName())) {
                        Iterator childsIt = sElement.getChildElements();
                        while (childsIt.hasNext()) {
                            SOAPElement mElement = (SOAPElement) childsIt.next();
                            if (SoapHeaderTags.LOG_MESSAGE_LOCAL_NAME.equals(mElement.getElementName().getLocalName())) {
                                String level = null;
                                String messageId = null;
                                String source = null;
                                Iterator attIt = mElement.getAllAttributes();
                                while (attIt.hasNext()) {
                                    Name attName = (Name) attIt.next();
                                    if (SoapHeaderTags.LOG_MESSAGE_LEVEL.equals(attName.getLocalName())) {
                                        level = mElement.getAttributeValue(attName);
                                    } else if (SoapHeaderTags.LOG_MESSAGE_MESSAGE_ID.equals(attName.getLocalName())) {
                                        messageId = mElement.getAttributeValue(attName);
                                    } else if (SoapHeaderTags.LOG_MESSAGE_SOURCE.equals(attName.getLocalName())) {
                                        source = mElement.getAttributeValue(attName);
                                    }
                                }
                                ArrayList parameters = new ArrayList();
                                Iterator paramIt = mElement.getChildElements();
                                while (paramIt.hasNext()) {
                                    SOAPElement pElement = (SOAPElement) paramIt.next();
                                    if (SoapHeaderTags.LOG_MESSAGE_PARAMETER.equals(pElement.getElementName()
                                            .getLocalName())) {
                                        parameters.add(pElement.getValue());
                                    }
                                }
                                String[] sParameters = null;
                                if (parameters.size() > 0) {
                                    sParameters = new String[parameters.size()];
                                    for (int i = 0; i < parameters.size(); i++) {
                                        sParameters[i] = (String) parameters.get(i);
                                    }
                                }
                                if (Integer.toString(JadeBusinessMessageLevels.ERROR).equals(level)) {
                                    JadeThread.logError(source, messageId, sParameters);
                                } else if (Integer.toString(JadeBusinessMessageLevels.WARN).equals(level)) {
                                    JadeThread.logWarn(source, messageId, sParameters);
                                } else if (Integer.toString(JadeBusinessMessageLevels.INFO).equals(level)) {
                                    JadeThread.logInfo(source, messageId, sParameters);
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            JadeLogger.error(this, "Unable to read web services messages response ! Reason : " + e.toString());
            JadeThread.logError(this.getClass().getName(), "UNABLE TO WEB SERVICES MESSAGES RESPONSES !");
        }
        return super.handleResponse(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.rpc.handler.GenericHandler#init(javax.xml.rpc.handler.HandlerInfo )
     */
    @Override
    public void init(HandlerInfo info) {
        handlerInfo = info;
    }
}
