package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractHeader extends Ech010468Model implements EAVSNonFinalNode {
    public abstract AbstractAction getAction();

    public abstract AbstractEventDate getEventDate();

    public abstract AbstractMessageDate getMessageDate();

    public abstract AbstractMessageId getMessageId();

    public abstract AbstractMessageType getMessageType();

    public abstract AbstractRecipientId getRecipientId();

    public abstract AbstractSenderId getSenderId();

    public abstract AbstractSendingApplication getSendingApplication();

    public abstract AbstractSubject getSubject();

    public abstract AbstractSubMessageType getSubMessageType();

    public abstract AbstractTestDeliveryFlag getTestDeliveryFlag();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
