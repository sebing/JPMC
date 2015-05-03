package jpmc.sebin.impl;

import jpmc.external.Message;

import org.apache.log4j.Logger;

/**
 * @author Sebin
 *
 *A basic implementation of the message.
 *To identify the message it had a variable message id and for identifying the group it had the groupid.
 */
public class MessageImpl implements Message {
    private final static Logger logger = Logger.getLogger(MessageImpl.class);

    private int groupId;
    private int messageId;
    private boolean messageCompleted;
    private boolean terminationType;

    /**
     * @param messageid
     * @param groupId
     * This constructor will create an instance of the message class
     */
    public MessageImpl(int messageid, int groupId) {
        this.groupId = groupId;
        this.messageId = messageid;
        messageCompleted = false;
        terminationType = false;
    }

    /**
     * @param messageid
     * @param groupId
     * @param terminationType
     * an alternate constructor for creating a message of termination type.
     */
    public MessageImpl(int messageid, int groupId, boolean terminationType) {
        this.groupId = groupId;
        this.messageId = messageid;
        messageCompleted = false;
        this.terminationType = terminationType;
    }

    /*
     * this method will set the variable completed.
     */
    @Override
    public void completed() {
        messageCompleted = true;
        if (logger.isDebugEnabled()) {
            logger.debug("Message Completed" + this);
        }

    }

    /**
     * @return the group id
     * 
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return message id
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "MessageId: " + messageId + "\tGroupid: " + groupId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageImpl) {
            MessageImpl msg = (MessageImpl) obj;
            if (msg.getMessageId() == getMessageId() && msg.getGroupId() == getGroupId()
                && msg.isMessageCompleted() == isMessageCompleted() && msg.isTerminationType() == isTerminationType()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return messageId;
    }

    /**
     * @return message completed status
     */
    public boolean isMessageCompleted() {
        return messageCompleted;
    }

    /**
     * @return is the message of termination type
     */
    public boolean isTerminationType() {
        return terminationType;
    }

}
