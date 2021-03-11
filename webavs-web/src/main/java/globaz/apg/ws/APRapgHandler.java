package globaz.apg.ws;

import globaz.jade.log.JadeLogger;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class APRapgHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        boolean isOK = true;
        // If it is an outgoing message
        if (outboundProperty.booleanValue()) {

            try {
                SOAPMessage soapMessagg = context.getMessage();

                SOAPEnvelope soapEnvelope = soapMessagg.getSOAPPart().getEnvelope();
                SOAPBody soapBody = soapEnvelope.getBody();

                if(soapBody == null){
                    soapBody = soapEnvelope.addBody();
                }

//                if(soapBody.getFirstChild() != null && isMyProdisRequest(soapBody.getFirstChild().getNodeName())){
//                    isOK = buildMyProdisBody(soapEnvelope, soapBody);
//                }

                soapMessagg.saveChanges();

                // Output the message to the Console -- for debug
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                soapMessagg.writeTo(out);
                String strMsg = new String(out.toByteArray());
                JadeLogger.info(this, "eBusiness request: " + strMsg);

                out.close();

            } catch (SOAPException e) {
                JadeLogger.error(this, e.getMessage());
            } catch (IOException e) {
                JadeLogger.error(this, e.getMessage());
            }

        } else {
            // incomming messages (responses)
            try {
                SOAPMessage soapMessagg = context.getMessage();
                SOAPEnvelope soapEnvelope = soapMessagg.getSOAPPart().getEnvelope();
                SOAPBody soapBody = soapEnvelope.getBody();

                if(soapBody.getFirstChild() != null) {
                    soapBody.getFirstChild().getAttributes();
                    SOAPElement parentNode = (SOAPElement)soapBody.getFirstChild();
                    QName parentQName = parentNode.getElementQName();
                    QName newQName = new QName("http://inforom.service-now.com/listService", parentQName.getLocalPart(), "");
                    parentNode.setElementQName(newQName);
//                    updateMyProdisBodyResponse(soapEnvelope, soapBody);
                    soapMessagg.saveChanges();
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                soapMessagg.writeTo(out);
                String strMsg = new String(out.toByteArray());
                JadeLogger.info(this, "eBusiness response: " + strMsg);
                out.close();

            } catch (SOAPException e) {
                JadeLogger.error(this, e.getMessage());
            } catch (IOException e) {
                JadeLogger.error(this, e.getMessage());
            }
        }
        return isOK;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {

    }
}
