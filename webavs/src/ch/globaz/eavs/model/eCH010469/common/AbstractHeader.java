package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractHeader extends Ech010469Model implements EAVSNonFinalNode {
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

    public abstract void setAction(EAVSAbstractModel abstractAction);

    public abstract void setEventDate(EAVSAbstractModel abstractEventDate);

    public abstract void setMessageDate(EAVSAbstractModel abstractMessageDate);

    public abstract void setMessageId(EAVSAbstractModel abstractMessageId);

    public abstract void setMessageType(EAVSAbstractModel abstractMessageType);

    public abstract void setRecipientId(EAVSAbstractModel abstractRecipientId);

    public abstract void setSenderId(EAVSAbstractModel abstractSenderId);

    public abstract void setSendingApplication(EAVSAbstractModel abstractSendingApplication);

    public abstract void setSubject(EAVSAbstractModel abstractSubject);

    public abstract void setSubMessageType(EAVSAbstractModel abstractSubMessageType);

    public abstract void setTestDeliveryFlag(EAVSAbstractModel abstractTestDeliveryFlag);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

}
