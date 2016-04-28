package com.ea.ja.server.domain;

public class Card {

	private final String type;
	private final int code;
	private final String action;
	
	public Card(String type, int code, String action) {
		super();
		this.type = type;
		this.code = code;
		this.action = action;
	}
	
	public String getType() {
		return type;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + code;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (code != other.code)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
