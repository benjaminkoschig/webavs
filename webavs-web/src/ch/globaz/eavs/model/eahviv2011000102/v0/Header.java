package ch.globaz.eavs.model.eahviv2011000102.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractAction;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractDeclarationLocalReference;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractHeader;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractMessageDate;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractMessageId;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractMessageType;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractPostalAddress;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractRecipientId;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractSenderId;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractSendingApplication;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractSubMessageType;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractSubject;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractTestDeliveryFlag;

public class Header extends AbstractHeader implements EAVSNonFinalNode {
    private Action action = null;
    private DeclarationLocalReference declarationLocalReference = null;
    private MessageDate messageDate = null;
    private MessageId messageId = null;
    private MessageType messageType = null;
    private RecipientId recipientId = null;
    private SenderId senderId = null;
    private SendingApplication sendingApplication = null;
    private Subject subject = null;
    private SubMessageType subMessageType = null;
    private TestDeliveryFlag testDeliveryFlag = null;

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
        result.add(messageType);
        result.add(subMessageType);
        result.add(sendingApplication);
        result.add(subject);
        result.add(messageDate);
        result.add(action);
        result.add(testDeliveryFlag);
        return result;
    }

    @Override
    public AbstractDeclarationLocalReference getDeclarationLocalReference() {
        if (declarationLocalReference == null) {
            declarationLocalReference = new DeclarationLocalReference();
        }
        return declarationLocalReference;
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
    public AbstractMessageType getMessageType() {
        if (messageType == null) {
            messageType = new MessageType();
        }
        return messageType;
    }

    @Override
    public AbstractPostalAddress getPostalAddress() {
        return null;
    }

    @Override
    public AbstractRecipientId getRecipientId() {
        if (recipientId == null) {
            recipientId = new RecipientId();
        }
        return recipientId;
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

}
