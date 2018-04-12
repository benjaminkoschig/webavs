package ch.globaz.orion.ws.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebAvsSoapHandler implements SOAPHandler<SOAPMessageContext> {
    private static Logger logger = LoggerFactory.getLogger(WebAvsSoapHandler.class);

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public void close(MessageContext smc) {
    }

    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        try {
            logSoapMsgIfDebugLevel("FAULT SOAP MESSAGE", smc);
        } catch (IOException e) {
            logger.error("unable to log fault SOAP message", e);
        }
        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        try {
            if (!outboundProperty.booleanValue()) {
                logInfoCalledService("WEBAVS SERVICE CALLED", smc);
                logSoapMsgIfDebugLevel("INCOMING SOAP MESSAGE", smc);
            } else {
                logSoapMsgIfDebugLevel("OUTGOING SOAP MESSAGE", smc);
                logInfoCalledService("RESPONSE SEND BY WEBAVS SERVICE", smc);
            }
        } catch (Exception e) {
            logger.error("an error occurred while log SOAP message", e);
        }
        return true;
    }

    private void logInfoCalledService(String freeText, SOAPMessageContext smc) {
        // afficher l'action appelée
        SOAPEnvelope env;
        SOAPBody body;
        try {
            env = smc.getMessage().getSOAPPart().getEnvelope();
            body = env.getBody();
            String ns = body.getFirstChild().getNamespaceURI();
            String op = body.getFirstChild().getLocalName();
            String operationPerformed = ns + op;

            logger.info(freeText + " " + operationPerformed);
        } catch (Exception e) {
            logger.error("unable to log info SOAP message");
        }
    }

    private void logSoapMsgIfDebugLevel(String freeText, SOAPMessageContext smc) throws IOException {
        if (logger.isDebugEnabled()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                smc.getMessage().writeTo(byteArrayOutputStream);
                String strMsg = new String(byteArrayOutputStream.toByteArray());
                logger.debug(freeText + " - " + strMsg);
            } catch (Exception e) {
                logger.error("unable to log SOAP message");
            } finally {
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
            }
        }
    }
}
