package com.ea.ja.server.socket;

import java.io.Serializable;

public class Message implements Serializable{

	 	private MessageCodes messageCodes;
	    private Object serializableObject;
	    private static final long serialVersionUID = 42L;

    public MessageCodes getMessageCodes() {
        return messageCodes;
    }

    private void setMessageCodes(MessageCodes messageCodes) {
        this.messageCodes = messageCodes;
    }

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
	     * @return type code of the message
	     */


	    /**
	     *
	     * @return the requested object
	     */
	    public Object getSerializableObject() {
	        return serializableObject;
	    }

	    /** 
	     * setter of the object
	     * @param serializableObject the object
	     */
	    private void setSerializableObject(Object serializableObject) {
	        this.serializableObject = serializableObject;
	    }
	
}
