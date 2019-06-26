package com.sanqing.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class MyException extends Exception{

	private Throwable wrappedThrowable;
	protected String errorMessage;
	protected String errorCode;
	

	public MyException(String errorCode, String errorMessage){
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		wrappedThrowable = new Throwable(errorMessage);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Throwable getWrappedThrowable(){
		return wrappedThrowable;
	}

	public String getExceptionInfo(){
		String cause = getMessage();
		int intS = cause.indexOf("[") + 1;
		int intE = cause.indexOf("]");
		if (intS != 0 && intE != -1){
			cause = cause.substring(intS, intE);
		}
		return cause;
	}

	public void setWrappedThrowable(Throwable t){
		wrappedThrowable = t;
	}

	public void printStackTrace(){
		super.printStackTrace();
		if (wrappedThrowable != null){
			System.err.println("MISException: ");
			wrappedThrowable.printStackTrace();
		}
	}

	public void printStackTrace(PrintStream ps){
		super.printStackTrace(ps);
		if (wrappedThrowable != null){
			ps.println("MISException: ");
			wrappedThrowable.printStackTrace(ps);
		}
	}

	public void printStackTrace(PrintWriter pw){
		super.printStackTrace(pw);
		if (wrappedThrowable != null){
			pw.println("MYException: ");
			wrappedThrowable.printStackTrace(pw);
		}
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}
}