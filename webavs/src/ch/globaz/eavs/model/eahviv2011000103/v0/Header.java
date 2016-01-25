package ch.globaz.eavs.model.eahviv2011000103.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAction;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractComment;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractDeclarationLocalReference;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEventDate;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEventPeriod;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractExtension;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractHeader;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractInitialMessageDate;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMessageDate;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMessageId;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMessagePriority;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMessageType;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractObject;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOurBusinessReferenceID;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractRecipientId;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractReferenceMessageId;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSenderId;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSendingApplication;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSubMessageType;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSubject;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractTestDeliveryFlag;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractYourBusinessReferenceId;
import ch.globaz.eavs.model.eahviv2011000103.v2.Comment;
import ch.globaz.eavs.model.eahviv2011000103.v2.Extension;
import ch.globaz.eavs.model.eahviv2011000103.v2.TestData;

public class Header extends AbstractHeader {
    private Action action = null;
    private Comment comment = null;
    private DeclarationLocalReference declarationLocalReference = null;
    private EventDate eventDate = null;
    private EventPeriod eventPeriod = null;
    private Extension extension = null;
    private InitialMessageDate initialMessageDate = null;
    private MessageDate messageDate = null;
    private MessageId messageId = null;
    private MessagePriority messagePriority = null;
    private MessageType messageType = null;
    private Object object = null;
    private OurBusinessReferenceID ourBusinessReferenceID = null;
    private RecipientId recipientId = null;
    private ReferenceMessageId referenceMessageId = null;
    private SenderId senderId = null;
    private SendingApplication sendingApplication = null;
    private Subject subject = null;
    private SubMessageType subMessageType = null;
    private TestData testData = null;
    private TestDeliveryFlag testDeliveryFlag = null;
    private YourBusinessReferenceId yourBusinessReferenceId = null;

    @Override
    public AbstractAction getAction() {
        if (action == null) {
            action = new Action();
        }
        return action;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(senderId);
        result.add(declarationLocalReference);
        result.add(recipientId);
        result.add(messageId);
        result.add(referenceMessageId);
        result.add(ourBusinessReferenceID);
        result.add(yourBusinessReferenceId);
        result.add(messageType);
        result.add(subMessageType);
        result.add(sendingApplication);
        result.add(subject);
        result.add(object);
        result.add(messageDate);
        result.add(eventDate);
        result.add(action);
        result.add(testDeliveryFlag);
        result.add(messagePriority);
        result.add(eventPeriod);
        result.add(initialMessageDate);
        result.add(comment);
        result.add(extension);
        result.add(testData);
        return result;
    }

    @Override
    public AbstractComment getComment() {
        if (comment == null) {
            comment = new Comment();
        }
        return comment;
    }

    @Override
    public AbstractDeclarationLocalReference getDeclarationLocalReference() {
        if (declarationLocalReference == null) {
            declarationLocalReference = new DeclarationLocalReference();
        }
        return declarationLocalReference;
    }

    @Override
    public AbstractEventDate getEventDate() {
        if (eventDate == null) {
            eventDate = new EventDate();
        }
        return eventDate;
    }

    @Override
    public AbstractEventPeriod getEventPeriod() {
        if (eventPeriod == null) {
            eventPeriod = new EventPeriod();
        }
        return eventPeriod;
    }

    public AbstractExtension getExtension() {
        if (extension == null) {
            extension = new Extension();
        }
        return extension;
    }

    @Override
    public AbstractInitialMessageDate getInitialMessageDate() {
        if (initialMessageDate == null) {
            initialMessageDate = new InitialMessageDate();
        }
        return initialMessageDate;
    }

    @Override
    public AbstractMessageDate getMessageDate() {
        if (messageDate == null) {
            messageDate = new MessageDate();
        }
        return messageDate;
    }

    @Override
    public AbstractMessageId getMessageId() {
        if (messageId == null) {
            messageId = new MessageId();
        }
        return messageId;
    }

    @Override
    public AbstractMessagePriority getMessagePriority() {
        if (messagePriority == null) {
            messagePriority = new MessagePriority();
        }
        return messagePriority;
    }

    @Override
    public AbstractMessageType getMessageType() {
        if (messageType == null) {
            messageType = new MessageType();
        }
        return messageType;
    }

    @Override
    public AbstractObject getObject() {
        if (object == null) {
            object = new Object();
        }
        return object;
    }

    @Override
    public AbstractOurBusinessReferenceID getOurBusinessReferenceID() {
        if (ourBusinessReferenceID == null) {
            ourBusinessReferenceID = new OurBusinessReferenceID();
        }
        return ourBusinessReferenceID;
    }

    @Override
    public AbstractRecipientId getRecipientId() {
        if (recipientId == null) {
            recipientId = new RecipientId();
        }
        return recipientId;
    }

    @Override
    public AbstractReferenceMessageId getReferenceMessageId() {
        if (referenceMessageId == null) {
            referenceMessageId = new ReferenceMessageId();
        }
        return referenceMessageId;
    }

    @Override
    public AbstractSenderId getSenderId() {
        if (senderId == null) {
            senderId = new SenderId();
        }
        return senderId;
    }

    @Override
    public AbstractSendingApplication getSendingApplication() {
        if (sendingApplication == null) {
            sendingApplication = new SendingApplication();
        }
        return sendingApplication;
    }

    @Override
    public AbstractSubject getSubject() {
        if (subject == null) {
            subject = new Subject();
        }
        return subject;
    }

    @Override
    public AbstractSubMessageType getSubMessageType() {
        if (subMessageType == null) {
            subMessageType = new SubMessageType();
        }
        return subMessageType;
    }

    @Override
    public AbstractTestDeliveryFlag getTestDeliveryFlag() {
        if (testDeliveryFlag == null) {
            testDeliveryFlag = new TestDeliveryFlag();
        }
        return testDeliveryFlag;
    }

    @Override
    public AbstractYourBusinessReferenceId getYourBusinessReferenceId() {
        if (yourBusinessReferenceId == null) {
            yourBusinessReferenceId = new YourBusinessReferenceId();
        }
        return yourBusinessReferenceId;
    }

    @Override
    public void setAction(EAVSAbstractModel _action) {
        action = (Action) _action;
    }

    @Override
    public void setComment(EAVSAbstractModel _comment) {
        comment = (Comment) _comment;
    }

    @Override
    public void setDeclarationLocalReference(EAVSAbstractModel _declarationLocalReference) {
        declarationLocalReference = (DeclarationLocalReference) _declarationLocalReference;
    }

    @Override
    public void setEventDate(EAVSAbstractModel _eventDate) {
        eventDate = (EventDate) _eventDate;
    }

    @Override
    public void setEventPeriod(EAVSAbstractModel _eventPeriod) {
        eventPeriod = (EventPeriod) _eventPeriod;
    }

    @Override
    public void setExtension(EAVSAbstractModel _extension) {
        extension = (Extension) _extension;
    }

    @Override
    public void setInitialMessageDate(EAVSAbstractModel _initialMessageDate) {
        initialMessageDate = (InitialMessageDate) _initialMessageDate;
    }

    @Override
    public void setMessageDate(EAVSAbstractModel _messageDate) {
        messageDate = (MessageDate) _messageDate;
    }

    @Override
    public void setMessageId(EAVSAbstractModel _messageId) {
        messageId = (MessageId) _messageId;
    }

    @Override
    public void setMessagePriority(EAVSAbstractModel _messagePriority) {
        messagePriority = (MessagePriority) _messagePriority;
    }

    @Override
    public void setMessageType(EAVSAbstractModel _messageType) {
        messageType = (MessageType) _messageType;
    }

    @Override
    public void setObject(EAVSAbstractModel _object) {
        object = (Object) _object;
    }

    @Override
    public void setOurBusinessReferenceID(EAVSAbstractModel _ourBusinessReferenceID) {
        ourBusinessReferenceID = (OurBusinessReferenceID) _ourBusinessReferenceID;
    }

    @Override
    public void setRecipientId(EAVSAbstractModel _recipientId) {
        recipientId = (RecipientId) _recipientId;
    }

    @Override
    public void setReferenceMessageId(EAVSAbstractModel _referenceMessageId) {
        referenceMessageId = (ReferenceMessageId) _referenceMessageId;
    }

    @Override
    public void setSenderId(EAVSAbstractModel _senderId) {
        senderId = (SenderId) _senderId;
    }

    @Override
    public void setSendingApplication(EAVSAbstractModel _sendingApplication) {
        sendingApplication = (SendingApplication) _sendingApplication;
    }

    @Override
    public void setSubject(EAVSAbstractModel _subject) {
        subject = (Subject) _subject;
    }

    @Override
    public void setSubMessageType(EAVSAbstractModel _subMessageType) {
        subMessageType = (SubMessageType) _subMessageType;
    }

    @Override
    public void setTestData(EAVSAbstractModel _testData) {
        testData = (TestData) _testData;
    }

    @Override
    public void setTestDeliveryFlag(EAVSAbstractModel _testDeliveryFlag) {
        testDeliveryFlag = (TestDeliveryFlag) _testDeliveryFlag;
    }

    @Override
    public void setYourBusinessReferenceId(EAVSAbstractModel _yourBusinessReferenceId) {
        yourBusinessReferenceId = (YourBusinessReferenceId) _yourBusinessReferenceId;
    }

}
