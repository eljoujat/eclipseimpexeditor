package org.eclipseplugins.impexeditor.core.dto;

public class ImpexValidationDto {

	String validationResultMsg;
	String content;
	Severity severity;

	public ImpexValidationDto(String content, String validationResultMsg, Severity severity) {
		this.content = content;
		this.validationResultMsg = validationResultMsg;
		this.severity = severity;
	}

	public String getValidationResultMsg() {
		return validationResultMsg;
	}

	public void setValidationResultMsg(String validationResultMsg) {
		this.validationResultMsg = validationResultMsg;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBeginLine() {
		// TODO Auto-generated method stub
		return 3;
	}

	public int getEndLine() {
		// TODO Auto-generated method stub
		return 4;
	}

	public int getBeginColumn() {
		String typeTokem = validationResultMsg.split("'")[1];
		return content.indexOf(typeTokem);
	}

	public int getEndColumn() {
		String typeTokem = validationResultMsg.split("'")[1];
		return getBeginColumn() + typeTokem.length() - 1;
	}

	public enum Severity {
		ERROR("error"), WARN("warning"), SUCESS("success");
		private final String code;

		Severity(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

	}

}
