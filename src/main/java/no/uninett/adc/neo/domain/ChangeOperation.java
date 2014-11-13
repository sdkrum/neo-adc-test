package no.uninett.adc.neo.domain;

public enum ChangeOperation {
	CREATE(0), DELETE(1), UPDATE(2);

	public int getId() {
		return this.ordinal();
	}

	private int code;

	private ChangeOperation(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
