package com.ea.ja.server.socket;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author achesnoiu
 * Message Class
 * @version 1.0
 */

public final class Message implements Serializable{

    private static final long serialVersionUID = 42L;
    private MessageCodes messageCodes;
    private final Vector<Object> objectVector = new Vector<>();

    /**
     *
     * @param messageCodes integer between 0 and NaN
     * @param serializableObject serializable object, respecting the contract of codes
     * @throws InvalidRequestedCode if the requested code is invalid
     */
    public Message(MessageCodes messageCodes, Object serializableObject) throws InvalidRequestedCode {
        setMessageCodes(messageCodes);
        setSerializableObject(serializableObject);
    }

    /**
     *
     * @param messageCodes integer between 0 and NaN
     * @param serializableObject serializable object, respecting the contract of codes
     * @throws InvalidRequestedCode if the requested code is invalid
     */
    public Message(MessageCodes messageCodes, Object serializableObject, Object serializableObject2) throws InvalidRequestedCode {
        setMessageCodes(messageCodes);
        setSerializableObject(serializableObject);
        setSerializableObject2(serializableObject2);
    }

    /**
     *
     * @param messageCodes integer between 0 and NaN
     * @param serializableObject serializable object, respecting the contract of codes
     * @throws InvalidRequestedCode if the requested code is invalid
     */
    public Message(MessageCodes messageCodes, Object serializableObject, Object serializableObject2, Object serializableObject3) throws InvalidRequestedCode {
        setMessageCodes(messageCodes);
        setSerializableObject(serializableObject);
        setSerializableObject2(serializableObject2);
        setSerializableObject3(serializableObject3);
    }

    /**
     *
     * @param messageCodes integer between 0 and NaN
     * @param serializableObject serializable object, respecting the contract of codes
     * @throws InvalidRequestedCode if the requested code is invalid
     */
    public Message(MessageCodes messageCodes, Object serializableObject, Object serializableObject2, Object serializableObject3,Object serializableObject4) throws InvalidRequestedCode {
        setMessageCodes(messageCodes);
        setSerializableObject(serializableObject);
        setSerializableObject2(serializableObject2);
        setSerializableObject3(serializableObject3);
        setSerializableObject4(serializableObject4);
    }

    /**
     *
     * @return the requested object
     */
    synchronized public Object getSerializableObject() {
        return objectVector.firstElement();
    }

    /**
     * setter of the object
     * @param serializableObject the object
     */
    synchronized private void setSerializableObject(Object serializableObject) {
        objectVector.add(serializableObject);
    }

    /**
     *
     * @return the requested object
     */
    synchronized public Object getSerializableObject2() {
        return objectVector.elementAt(1);
    }

    /**
     * setter of the object
     * @param serializableObject2 the object
     */
    synchronized private void setSerializableObject2(Object serializableObject2) {
        objectVector.add(serializableObject2);
    }

    /**
     *
     * @return the requested object
     */
    synchronized public Object getSerializableObject3() {
        return objectVector.elementAt(2);
    }

    /**
     * setter of the object
     * @param serializableObject3 the object
     */
    synchronized private void setSerializableObject3(Object serializableObject3) {
        objectVector.add(serializableObject3);
    }

    /**
     *
     * @return the requested object
     */
    synchronized private Object getSerializableObject4() {
        return objectVector.elementAt(3);
    }
    /**
     * setter of the object
     * @param serializableObject4 the object
     */

    synchronized private void setSerializableObject4(Object serializableObject4) {
        objectVector.add(serializableObject4);
    }

    /**
     * @author achesnoiu
     * @return messagecode
     */
    synchronized public MessageCodes getMessageCodes() {
        return messageCodes;
    }

    /**
     * sets the message code
     * @author achesnoiu
     * @param messageCodes MessageCode enum
     */
    synchronized private void setMessageCodes(MessageCodes messageCodes) {
        this.messageCodes = messageCodes;
    }
}
