package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractHeader extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAction getAction();

    public abstract AbstractComment getComment();

    public abstract AbstractDeclarationLocalReference getDeclarationLocalReference();

    public abstract AbstractEventDate getEventDate();

    public abstract AbstractEventPeriod getEventPeriod();

    public abstract AbstractInitialMessageDate getInitialMessageDate();

    public abstract AbstractMessageDate getMessageDate();

    public abstract AbstractMessageId getMessageId();

    public abstract AbstractMessagePriority getMessagePriority();

    public abstract AbstractMessageType getMessageType();

    public abstract AbstractObject getObject();

    public abstract AbstractOurBusinessReferenceID getOurBusinessReferenceID();

    public abstract AbstractRecipientId getRecipientId();

    public abstract AbstractReferenceMessageId getReferenceMessageId();

    public abstract AbstractSenderId getSenderId();

    public abstract AbstractSendingApplication getSendingApplication();

    public abstract AbstractSubject getSubject();

    public abstract AbstractSubMessageType getSubMessageType();

    public abstract AbstractTestDeliveryFlag getTestDeliveryFlag();

    public abstract AbstractYourBusinessReferenceId getYourBusinessReferenceId();

    public abstract void setAction(EAVSAbstractModel _action);

    public abstract void setComment(EAVSAbstractModel _comment);

    public abstract void setDeclarationLocalReference(EAVSAbstractModel _declarationLocalReference);

    public abstract void setEventDate(EAVSAbstractModel _eventDate);

    public abstract void setEventPeriod(EAVSAbstractModel _eventPeriod);

    public abstract void setExtension(EAVSAbstractModel _extension);

    public abstract void setInitialMessageDate(EAVSAbstractModel _initialMessageDate);

    public abstract void setMessageDate(EAVSAbstractModel _messageDate);

    public abstract void setMessageId(EAVSAbstractModel _messageId);

    public abstract void setMessagePriority(EAVSAbstractModel _messagePriority);

    public abstract void setMessageType(EAVSAbstractModel _messageType);

    public abstract void setObject(EAVSAbstractModel _object);

    public abstract void setOurBusinessReferenceID(EAVSAbstractModel _ourBusinessReferenceID);

    public abstract void setRecipientId(EAVSAbstractModel _recipientId);

    public abstract void setReferenceMessageId(EAVSAbstractModel _referenceMessageId);

    public abstract void setSenderId(EAVSAbstractModel _senderId);

    public abstract void setSendingApplication(EAVSAbstractModel _sendingApplication);

    public abstract void setSubject(EAVSAbstractModel _subject);

    public abstract void setSubMessageType(EAVSAbstractModel _subMessageType);

    public abstract void setTestData(EAVSAbstractModel _testData);

    public abstract void setTestDeliveryFlag(EAVSAbstractModel _testDeliveryFlag);

    public abstract void setYourBusinessReferenceId(EAVSAbstractModel _yourBusinessReferenceId);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}
