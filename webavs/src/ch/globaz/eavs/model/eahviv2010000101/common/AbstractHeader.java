package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractHeader extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    public abstract AbstractSenderId getSenderId();

    public abstract AbstractDeclarationLocalReference getDeclarationLocalReference();

    public abstract AbstractRecipientId getRecipientId();

    public abstract AbstractMessageId getMessageId();

    public abstract AbstractOurBusinessReferenceId getOurBusinessReferenceId();

    public abstract AbstractMessageType getMessageType();

    public abstract AbstractSubMessageType getSubMessageType();

    public abstract AbstractSendingApplication getSendingApplication();

    public abstract AbstractSubject getSubject();

    public abstract AbstractObject getObject();

    public abstract AbstractMessageDate getMessageDate();

    public abstract AbstractEventDate getEventDate();

    public abstract AbstractAction getAction();

    public abstract AbstractTestDeliveryFlag getTestDeliveryFlag();

    public abstract AbstractMessagePriority getMessagePriority();

    public abstract AbstractEventPeriod getEventPeriod();
}
