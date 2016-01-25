package ch.globaz.eavs.model.eahviv2010000101.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractAction;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractDeclarationLocalReference;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractEventDate;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractEventPeriod;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractHeader;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractMessageDate;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractMessageId;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractMessagePriority;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractMessageType;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractObject;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOurBusinessReferenceId;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractRecipientId;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractSenderId;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractSendingApplication;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractSubMessageType;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractSubject;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractTestDeliveryFlag;

public class Header extends AbstractHeader implements EAVSNonFinalNode {
    private SenderId senderId = null;
    private Action action = null;
    private DeclarationLocalReference declarationLocalReference = null;
    private AbstractEventDate eventDate = null;
    private EventPeriod eventPeriod = null;
    private MessageDate messageDate = null;
    private MessageId messageId = null;
    private MessagePriority messagePriority = null;
    private MessageType messageType = null;
    private Object object = null;
    private OurBusinessReferenceID ourBusinessReferenceId = null;
    private RecipientId recipientId = null;
    private SendingApplication sendingApplication = null;
    private SubMessageType subMessageType = null;
    private Subject subject = null;
    private TestDeliveryFlag testDeliveryFlag = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(senderId);
        result.add(declarationLocalReference);
        result.add(recipientId);
        result.add(messageId);
        result.add(ourBusinessReferenceId);
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
        return result;
    }

    @Override
    public AbstractSenderId getSenderId() {
        if (senderId == null) {
            senderId = new SenderId();
        }
        return senderId;
    }

    @Override
    public AbstractAction getAction() {
        if (action == null) {
            action = new Action();
        }
        return action;
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
    public AbstractOurBusinessReferenceId getOurBusinessReferenceId() {
        if (ourBusinessReferenceId == null) {
            ourBusinessReferenceId = new OurBusinessReferenceID();
        }
        return ourBusinessReferenceId;
    }

    @Override
    public AbstractRecipientId getRecipientId() {
        if (recipientId == null) {
            recipientId = new RecipientId();
        }
        return recipientId;
    }

    @Override
    public AbstractSendingApplication getSendingApplication() {
        if (sendingApplication == null) {
            sendingApplication = new SendingApplication();
        }
        return sendingApplication;
    }

    @Override
    public AbstractSubMessageType getSubMessageType() {
        if (subMessageType == null) {
            subMessageType = new SubMessageType();
        }
        return subMessageType;
    }

    @Override
    public AbstractSubject getSubject() {
        if (subject == null) {
            subject = new Subject();
        }
        return subject;
    }

    @Override
    public AbstractTestDeliveryFlag getTestDeliveryFlag() {
        if (testDeliveryFlag == null) {
            testDeliveryFlag = new TestDeliveryFlag();
        }
        return testDeliveryFlag;
    }

}
