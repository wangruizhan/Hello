package com.sanqing.exception;

import java.io.CharConversionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.NotSerializableException;
import java.io.SyncFailedException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.rmi.AccessException;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.naming.NameNotFoundException;
import javax.servlet.ServletException;
import javax.transaction.TransactionRolledbackException;

import org.springframework.dao.DataAccessException;

import com.sanqing.sca.service.ReturnCode;

public class ErrorMessage{
	private String errorInfo;
	private String errorCode;
	private Exception ex;

	public ErrorMessage(){
		errorInfo = "";
	}

	public ErrorMessage(Exception e){
		errorInfo = "";
		try{
			setExcetionObject(e);
			if (e instanceof RemoteException){
				e = (Exception)((RemoteException)e).detail;
			}
			
			if(e instanceof DataAccessException){
				e = (Exception)(((DataAccessException)e).getRootCause());
			}
			
			if (e instanceof MyException){
				this.setErrorCode(((MyException)e).getErrorCode());
				this.setErrorInfo(((MyException)e).getErrorMessage());
				return;
			}
			
			parseException(e);
		}
		catch (Exception ex){
			this.setErrorCode(ReturnCode.TASK_EXCEPTION_CODE);
			setErrorInfo("δ֪������Ϣ");
		}finally{
			e.printStackTrace();
		}
	}
	
	public void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}

	public void setExcetionObject(Exception ex){
		this.ex = ex;
	}

	public Exception getExcetionObject(){
		return ex;
	}
	
	private void parseException(Exception e){
		
		if (e instanceof SQLException){
			this.setErrorCode(ReturnCode.DATABASE_ERROR_CODE);
			if (e.toString().indexOf("ORA-00001") > 1){
				setErrorInfo("��ͼ�ƻ�һ��Ψһ������");
			}else if (e.toString().indexOf("ORA-06511") > 1){
				setErrorInfo("��ͼ��һ���Ѵ򿪵��α�");
			}else if (e.toString().indexOf("ORA-01001") > 1){
				setErrorInfo("��ͼʹ��һ����Ч���α�");
			}else if (e.toString().indexOf("ORA-01722") > 1){
				setErrorInfo("��ͼ�Է�����ֵ�������ֲ���");
			}else if (e.toString().indexOf("ORA-01017") > 1){
				setErrorInfo("��Ч���û������߿���");
			}else if (e.toString().indexOf("ORA-00403") > 1){
				setErrorInfo("��ѯδ�ҵ�����");
			}else if (e.toString().indexOf("ORA-01012") > 1){
				setErrorInfo("��δ���Ӿ���ͼ���ݿ����");
			}else if (e.toString().indexOf("ORA-06501") > 1){
				setErrorInfo("�ڲ�����");
			}else if (e.toString().indexOf("ORA-06504") > 1){
				setErrorInfo("���������α�����Ͳ�����");
			}else if (e.toString().indexOf("ORA-06500") > 1){
				setErrorInfo("�ڲ�����");
			}else if (e.toString().indexOf("ORA-06502") > 1){
				setErrorInfo("ת�����߲ü�����");
			}else if (e.toString().indexOf("ORA-00051") > 1){
				setErrorInfo("������ʱ");
			}else if (e.toString().indexOf("ORA-01476") > 1){
				setErrorInfo("��ͼ�����");
			}else if (e.toString().indexOf("ORA-01422") > 1){
				setErrorInfo("SELECT INTD����صĶ���");
			}else if (e.toString().indexOf("ORA-00000") > 1){
				setErrorInfo("���������ύ���˻�");
			}else if (e.toString().indexOf("ORA-01650") > 1){
				setErrorInfo("�ع��α�ռ䲻��");
			}else if (e.toString().indexOf("ORA-01652") > 1){
				setErrorInfo("ORACLE��ʱ�α�ռ䲻��");
			}else if (e.toString().indexOf("ORA-01628") > 1){
				setErrorInfo("һ���ع��κ�һ����ռ��Ѿ��ﵽMAXEXTENTS�������õļ���");
			}else if (e.toString().indexOf("ORA-01688") > 1){
				setErrorInfo("ָ����tablespace�ռ��Ѿ���ռ�������޷���չ");
			}else if (e.toString().indexOf("ORA-01401") > 1){
				setErrorInfo("�����ֵ�����й���");
			}else if (e.toString().indexOf("ORA-01400") > 1){
				setErrorInfo("��ֵ������");
			}else if (e.toString().indexOf("ORA-00933") > 1){
				setErrorInfo(" SQL ����δ��ȷ����");
			}else
				setErrorInfo("���ݿ����ʧ��");
			return;
		}
		//�趨����ִ���쳣�Ĵ����
		this.setErrorCode(ReturnCode.TASK_EXCEPTION_CODE);
		if (e instanceof ConnectException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		
		if (e instanceof BindException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		
		if (e instanceof NoRouteToHostException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		if (e instanceof UnknownHostException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		if (e instanceof UnknownServiceException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		
		if (e instanceof SocketException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		
		if (e instanceof NullPointerException){
			setErrorInfo("��ָ�����");
			return;
		}
		if (e instanceof ClassCastException){
			setErrorInfo("�����ʹ���");
			return;
		}
		
		if (e instanceof ArrayIndexOutOfBoundsException){
			setErrorInfo("����Խ�����");
			return;
		}
		
		if (e instanceof IndexOutOfBoundsException){
			setErrorInfo("ָ��Խ�����");
			return;
		}
		if (e instanceof ArrayStoreException){
			setErrorInfo("����洢����");
			return;
		}
		if (e instanceof UnsupportedOperationException){
			setErrorInfo("��֧�ֵĲ�������");
			return;
		}
		
		if (e instanceof ArithmeticException){
			setErrorInfo("�㷨����");
			return;
		}
		if (e instanceof NumberFormatException){
			setErrorInfo("���ָ�ʽ����");
			return;
		}
		
		if (e instanceof NameNotFoundException){
			setErrorInfo("JNDINameû���ҵ�");
			return;
		}
		
		if (e instanceof AccessException){
			setErrorInfo("Զ�̷����ܾ�����");
			return;
		}
		
		if (e instanceof java.rmi.ConnectException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		
		if (e instanceof ConnectIOException){
			setErrorInfo("�������Ӵ���");
			return;
		}
		
		if (e instanceof ServletException){
			setErrorInfo("ServLet����ʱ����");
			return;
		}
		
		if (e instanceof RuntimeException){
			setErrorInfo("����ʱ����");
			return;
		}
		
		if (e instanceof TransactionRolledbackException){
			setErrorInfo("����ع��쳣��������ݿ�����");
			return;
		}
		
		if (e instanceof FileNotFoundException){
			setErrorInfo("�ļ�δ���֣�����ļ��Ƿ����");
			return;
		}
		
		if (e instanceof NotSerializableException){
			setErrorInfo("��û�����л�");
			return;
		}
		
		if (e instanceof CharConversionException){
			setErrorInfo("������������ַ�ת���쳣");
			return;
		}
		
		if (e instanceof InterruptedIOException){
			setErrorInfo("���жϵ��������");
			return;
		}
		
		if (e instanceof InvalidClassException){
			setErrorInfo("��Ч�������");
			return;
		}
		
		if (e instanceof InvalidObjectException){
			setErrorInfo("��Ч�Ķ������");
			return;
		}
		
		if (e instanceof NotActiveException){
			setErrorInfo("�����������ϵͳ����ʧ��");
			return;
		}
		
		if (e instanceof SyncFailedException){
			setErrorInfo("�����������ͬ��ʧ��");
			return;
		}
		
		if (e instanceof IOException){
			setErrorInfo("�����������");
			return;
		}
		
		if (e instanceof ClassNotFoundException){
			setErrorInfo("���Ҳ���");
			return;
		}
		
		if (e instanceof InterruptedException){
			setErrorInfo("�ж��쳣");
			return;
		}
		
		if (e instanceof InstantiationException){
			setErrorInfo("ʵ��������");
			return;
		}
		
		if (e instanceof IOException){
			setErrorInfo("�����������");
			return;
		} else{
			setErrorInfo("δ�������");
			return;
		}
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorInfo() {
		return errorInfo;
	}
}