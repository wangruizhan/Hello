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
			setErrorInfo("未知错误信息");
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
				setErrorInfo("试图破坏一个唯一性限制");
			}else if (e.toString().indexOf("ORA-06511") > 1){
				setErrorInfo("试图打开一个已打开的游标");
			}else if (e.toString().indexOf("ORA-01001") > 1){
				setErrorInfo("试图使用一个无效的游标");
			}else if (e.toString().indexOf("ORA-01722") > 1){
				setErrorInfo("试图对非数字值进行数字操作");
			}else if (e.toString().indexOf("ORA-01017") > 1){
				setErrorInfo("无效的用户名或者口令");
			}else if (e.toString().indexOf("ORA-00403") > 1){
				setErrorInfo("查询未找到数据");
			}else if (e.toString().indexOf("ORA-01012") > 1){
				setErrorInfo("还未连接就试图数据库操作");
			}else if (e.toString().indexOf("ORA-06501") > 1){
				setErrorInfo("内部错误");
			}else if (e.toString().indexOf("ORA-06504") > 1){
				setErrorInfo("主变量和游标的类型不兼容");
			}else if (e.toString().indexOf("ORA-06500") > 1){
				setErrorInfo("内部错误");
			}else if (e.toString().indexOf("ORA-06502") > 1){
				setErrorInfo("转换或者裁剪错误");
			}else if (e.toString().indexOf("ORA-00051") > 1){
				setErrorInfo("发生超时");
			}else if (e.toString().indexOf("ORA-01476") > 1){
				setErrorInfo("试图被零除");
			}else if (e.toString().indexOf("ORA-01422") > 1){
				setErrorInfo("SELECT INTD命令返回的多行");
			}else if (e.toString().indexOf("ORA-00000") > 1){
				setErrorInfo("由于死锁提交被退回");
			}else if (e.toString().indexOf("ORA-01650") > 1){
				setErrorInfo("回滚段表空间不足");
			}else if (e.toString().indexOf("ORA-01652") > 1){
				setErrorInfo("ORACLE临时段表空间不足");
			}else if (e.toString().indexOf("ORA-01628") > 1){
				setErrorInfo("一个回滚段和一个表空间已经达到MAXEXTENTS参数设置的极限");
			}else if (e.toString().indexOf("ORA-01688") > 1){
				setErrorInfo("指定的tablespace空间已经被占用满，无法扩展");
			}else if (e.toString().indexOf("ORA-01401") > 1){
				setErrorInfo("插入的值对于列过大");
			}else if (e.toString().indexOf("ORA-01400") > 1){
				setErrorInfo("空值不允许");
			}else if (e.toString().indexOf("ORA-00933") > 1){
				setErrorInfo(" SQL 命令未正确结束");
			}else
				setErrorInfo("数据库操作失败");
			return;
		}
		//设定任务执行异常的错误号
		this.setErrorCode(ReturnCode.TASK_EXCEPTION_CODE);
		if (e instanceof ConnectException){
			setErrorInfo("网络连接错误");
			return;
		}
		
		if (e instanceof BindException){
			setErrorInfo("网络连接错误");
			return;
		}
		
		if (e instanceof NoRouteToHostException){
			setErrorInfo("网络连接错误");
			return;
		}
		if (e instanceof UnknownHostException){
			setErrorInfo("网络连接错误");
			return;
		}
		if (e instanceof UnknownServiceException){
			setErrorInfo("网络连接错误");
			return;
		}
		
		if (e instanceof SocketException){
			setErrorInfo("网络连接错误");
			return;
		}
		
		if (e instanceof NullPointerException){
			setErrorInfo("空指针错误");
			return;
		}
		if (e instanceof ClassCastException){
			setErrorInfo("类造型错误");
			return;
		}
		
		if (e instanceof ArrayIndexOutOfBoundsException){
			setErrorInfo("数组越界错误");
			return;
		}
		
		if (e instanceof IndexOutOfBoundsException){
			setErrorInfo("指针越界错误");
			return;
		}
		if (e instanceof ArrayStoreException){
			setErrorInfo("数组存储错误");
			return;
		}
		if (e instanceof UnsupportedOperationException){
			setErrorInfo("不支持的操作错误");
			return;
		}
		
		if (e instanceof ArithmeticException){
			setErrorInfo("算法错误");
			return;
		}
		if (e instanceof NumberFormatException){
			setErrorInfo("数字格式错误");
			return;
		}
		
		if (e instanceof NameNotFoundException){
			setErrorInfo("JNDIName没有找到");
			return;
		}
		
		if (e instanceof AccessException){
			setErrorInfo("远程方法拒绝请求");
			return;
		}
		
		if (e instanceof java.rmi.ConnectException){
			setErrorInfo("网络连接错误");
			return;
		}
		
		if (e instanceof ConnectIOException){
			setErrorInfo("网络连接错误");
			return;
		}
		
		if (e instanceof ServletException){
			setErrorInfo("ServLet运行时错误");
			return;
		}
		
		if (e instanceof RuntimeException){
			setErrorInfo("运行时错误");
			return;
		}
		
		if (e instanceof TransactionRolledbackException){
			setErrorInfo("事务回滚异常，检查数据库连接");
			return;
		}
		
		if (e instanceof FileNotFoundException){
			setErrorInfo("文件未发现，检查文件是否存在");
			return;
		}
		
		if (e instanceof NotSerializableException){
			setErrorInfo("类没有序列化");
			return;
		}
		
		if (e instanceof CharConversionException){
			setErrorInfo("输入输出错误，字符转化异常");
			return;
		}
		
		if (e instanceof InterruptedIOException){
			setErrorInfo("被中断的输入输出");
			return;
		}
		
		if (e instanceof InvalidClassException){
			setErrorInfo("无效的类调用");
			return;
		}
		
		if (e instanceof InvalidObjectException){
			setErrorInfo("无效的对象调用");
			return;
		}
		
		if (e instanceof NotActiveException){
			setErrorInfo("输入输出错误，系统激活失败");
			return;
		}
		
		if (e instanceof SyncFailedException){
			setErrorInfo("输入输出错误，同步失败");
			return;
		}
		
		if (e instanceof IOException){
			setErrorInfo("输入输出错误");
			return;
		}
		
		if (e instanceof ClassNotFoundException){
			setErrorInfo("类找不到");
			return;
		}
		
		if (e instanceof InterruptedException){
			setErrorInfo("中断异常");
			return;
		}
		
		if (e instanceof InstantiationException){
			setErrorInfo("实例化错误");
			return;
		}
		
		if (e instanceof IOException){
			setErrorInfo("输入输出错误");
			return;
		} else{
			setErrorInfo("未定义错误");
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