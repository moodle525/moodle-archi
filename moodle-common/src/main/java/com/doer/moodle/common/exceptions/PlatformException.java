package com.doer.moodle.common.exceptions;

public class PlatformException extends Exception {
	private static final long serialVersionUID = -8886495803406807620L;
	private String errorCode;
	private String errorDetail;

	public PlatformException(String errorCode, String errorDetail) {
		super(errorCode + ":" + errorDetail);
		this.errorCode = errorCode;
		this.errorDetail = errorDetail;
	}

	public PlatformException(String errorCode, Exception ex) {
		super(errorCode + ":" + errorCode, ex);
		this.errorCode = errorCode;
	}

	public PlatformException(String errorCode, String errorDetail, Exception ex) {
		super(errorCode + ":" + errorDetail, ex);
		this.errorCode = errorCode;
		this.errorDetail = errorDetail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}

}
