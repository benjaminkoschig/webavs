package ch.globaz.eform.businessimpl.services.sedex.sender.dadossier;

import ch.globaz.eform.businessimpl.services.sedex.constant.GFMessageTypeSedex;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierHeaderElementSender;
import ch.globaz.eform.businessimpl.services.sedex.sender.GFDaDossierSender;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message;

public class GF2021000102Sender extends GFDaDossierSender<Message> {
    @Override
    protected Message createMessage() {
        Message message = new Message();

        message.setHeader(createHeader());

        return message;
    }

    private HeaderType createHeader() {
        HeaderType header = new HeaderType();

        header.setSenderId(elements.get(GFDaDossierHeaderElementSender.SENDER_ID));
        header.setRecipientId(elements.get(GFDaDossierHeaderElementSender.RECIPIENT_ID));
        if (elements.containsKey(GFDaDossierHeaderElementSender.MESSAGE_ID)) {
            header.setMessageId(elements.get(GFDaDossierHeaderElementSender.MESSAGE_ID));
        } else {
            header.setMessageId(identifiantGenerator.generateMessageId());
        }
        header.setReferenceMessageId(elements.get(GFDaDossierHeaderElementSender.REFERENCE_MESSAGE_ID));
        if (elements.containsKey(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID)) {
            header.setOurBusinessReferenceId(elements.get(GFDaDossierHeaderElementSender.OUR_BUSINESS_REFERENCE_ID));
        } else {
            header.setOurBusinessReferenceId(identifiantGenerator.generateBusinessReferenceId());
        }
        header.setYourBusinessReferenceId(elements.get(GFDaDossierHeaderElementSender.YOUR_BUSINESS_REFERENCE_ID));
        header.setMessageType(GFMessageTypeSedex.TYPE_2021_TRANSFERE.getMessageType());

        return header;
    }
}
