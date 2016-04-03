package org.eclipseplugins.impexeditor.formatter.dto.variables;

public class VariableBloc {
	String leftOperand;
	String rightOperand;

	public VariableBloc(String leftOperand, String rightOperand) {

		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	public static VariableBloc buildVariableBloc(String bloc) {

		String[] operands = bloc.split("=");
		return new VariableBloc(operands[0], operands[1]);
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(leftOperand);
		sb.append("= ");
		sb.append(rightOperand);
		return sb.toString();
	}
	public String getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(String leftOperand) {
		this.leftOperand = leftOperand;
	}

	public String getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(String rightOperand) {
		this.rightOperand = rightOperand;
	}

}
