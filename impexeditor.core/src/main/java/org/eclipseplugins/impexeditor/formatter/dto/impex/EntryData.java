package org.eclipseplugins.impexeditor.formatter.dto.impex;

public class EntryData {

	boolean isHeader;
	boolean isEmpty;
	String Value;

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	@Override
	public String toString() {
		return isEmpty?"\t;":Value+" ;";
	}
	
	
	public String appendNSpace(int n){
		
		StringBuffer sb=new StringBuffer(Value);
		for (int i = 0; i < n-Value.length(); i++) {
			sb.append(" ");
		}
		sb.append(";");
		return sb.toString();
	}
	

}
