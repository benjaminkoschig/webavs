package ch.globaz.eavs.model.eahviv2011000102.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractHeader extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAction getAction();

    public abstract AbstractDeclarationLocalReference getDeclarationLocalReference();

    public abstract AbstractMessageDate getMessageDate();

    public abstract AbstractMessageId getMessageId();

    public abstract AbstractMessageType getMessageType();

    public abstract AbstractPostalAddress getPostalAddress();

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
