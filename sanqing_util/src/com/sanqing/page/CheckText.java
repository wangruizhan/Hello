package com.sanqing.page;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sanqing.exception.MyException;
import com.sanqing.sca.service.ReturnCode;

public class CheckText {
	
	/**
	 * �ֶμ���
	 * @param text �����ֶ�ֵ
	 * @param type �ֶ����� "C" Ϊ�ַ�����"N" Ϊ����,"D"Ϊ��������
	 * @param length ��󳤶�  �������� 0��ʾ��������󳤶�
	 * @param precision ���� �������� ��ʾ����С��λ�ĳ���,0��ʾ������
	 * @param mode ��������   0������������գ�/1�����������ɿգ�/2������������գ�/3����������գ�
	 * @param describe ����
	 * @return 
	 */
	public static void checkFormat(String text,String type,int length, int precision,int mode,String describe)throws MyException{
		if(type == null || "".equals(type)){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"�ֶ�����Ϊ��");
		}
		
		if(!"C".equals(type) && !"N".equals(type) && !"D".equals(type)){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"�޷�ʶ���ֶ�����");
		}
		
		if(length < 0){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"�Ƿ�����");
		}
		
		if(precision < 0){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"�Ƿ�����");
		}
		
		if(mode < 0 || mode > 3){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"�Ƿ�У��ģʽ");
		}
		
		if(describe == null || "".equals(describe)){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"�ֶ�����Ϊ��");
		}		
		
		//�ַ���У��		
		if("C".equals(type)){
			if(mode == 0){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ���Ϊ��");
				}
				if(length != 0 && text.getBytes().length != length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��Ȳ�Ϊ"+ length);
				}
			}else if(mode == 1){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶ�Ϊ��");
				}else if(length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��ȴ���"+ length);
				}
			}else if(mode == 2){
				if(text != null && length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��ȴ���"+ length);
				}
			}else if(mode == 3){
				if(text != null && !"".equals(text) && text.getBytes().length != length && length != 0){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��Ȳ�Ϊ"+ length);
				}
			}
		}else if("D".equals(type)){
			//�ж��Ƿ���ʱ��
			if(mode == 0){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ���Ϊ��");
				}
				if(length != 0 && text.getBytes().length != length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��Ȳ�Ϊ"+ length);
				}
				if(!isDate(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ��ܸ�ʽ��Ϊʱ������");
				}
			}else if(mode == 1){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ���Ϊ��");
				}
				if(length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��ȴ���"+ length);
				}
				if(!isDate(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ��ܸ�ʽ��Ϊʱ������");
				}
			}else if(mode == 2){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length > length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��ȴ���"+ length);
					}
					if(!isDate(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ��ܸ�ʽ��Ϊʱ������");
					}
				}
			}else if(mode == 3){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length != length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��Ȳ�Ϊ"+ length);
					}
					if(!isDate(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ��ܸ�ʽ��Ϊʱ������");
					}
				}
			}
		}else if("N".equals(type)){
			//�ж��Ƿ�������
			if(mode == 0){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶ�Ϊ��");
				}
				if(length != 0 && text.getBytes().length != length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��Ȳ�Ϊ"+ length);
				}
				if(precision == 0){
					if(!isNumber(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
					}
				}else{
					String ts[] = text.split("\\.");
					if(ts.length == 1){
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
					}else{
						if(ts.length != 2){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
						if(ts[1] != null && !"".equals(ts[1])){
							if(!isNumber(ts[1])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
							if(ts[1].getBytes().length > precision){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶξ��ȴ���"+ precision);
							}
						}					
					}				
				}
			}else if(mode == 1){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ���Ϊ��");
				}
				if(length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��ȴ���"+ length);
				}
				if(precision == 0){
					if(!isNumber(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
					}
				}else{
					String ts[] = text.split("\\.");
					if(ts.length == 1){
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
					}else{
						if(ts.length != 2){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
						if(ts[1] != null && !"".equals(ts[1])){
							if(!isNumber(ts[1])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
							if(ts[1].getBytes().length > precision){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶξ��ȴ���"+ precision);
							}
						}					
					}				
				}
			}else if(mode == 2){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length > length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��ȴ���"+ length);
					}
					if(precision == 0){
						if(!isNumber(text)){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
					}else{
						String ts[] = text.split("\\.");
						if(ts.length == 1){
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
						}else{
							if(ts.length != 2){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
							if(ts[1] != null && !"".equals(ts[1])){
								if(!isNumber(ts[1])){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
								}
								if(ts[1].getBytes().length > precision){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶξ��ȴ���"+ precision);
								}
							}					
						}				
					}				
				}
			}else if(mode == 3){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length != length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶγ��Ȳ�Ϊ"+length);
					}
					if(precision == 0){
						if(!isNumber(text)){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
						}
					}else{
						String ts[] = text.split("\\.");
						if(ts.length == 1){
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
						}else{
							if(ts.length != 2){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
							}
							if(ts[1] != null && !"".equals(ts[1])){
								if(!isNumber(ts[1])){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶβ���ת��������");
								}
								if(ts[1].getBytes().length > precision){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"�ֶξ��ȴ���"+ precision);
								}
							}					
						}				
					}
				}
			}
		}
	}
	
	//�ж��Ƿ�������
	public static boolean isNumber(String str){
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str.trim());
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	//�ж��ܷ��ʽ����ʱ��
	public static boolean isDate(String str){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try{
			df.parse(str); 

			String ts[] = str.split("-");
			if(ts.length != 3){
				return false;
			}
			if(ts[0].length() != 4 || ts[1].getBytes().length >2 || ts[2].getBytes().length >2){
				return false;
			}
			
			int year = Integer.parseInt(ts[0]);
			int month = Integer.parseInt(ts[1]);
			int date = Integer.parseInt(ts[2]);
			int maxdate = 0;
			boolean isrn = false;
						
			if(month > 12 || month <= 0){
				return false;
			}
			
			if((year % 400 == 0) || ((year %  4 == 0) && (year %  100 != 0)) ){
				isrn = true;
			}

			if(month == 2){
				if(isrn){
					maxdate = 29;
				}else{
					maxdate = 28;
				}
			}else if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
				maxdate = 31;
			}else{
				maxdate = 30;
			}

			if(date > maxdate || date <= 0){
				return false;
			}
			
			return true;
		}catch (Exception e) {

			return false;
		}
	}
	
	public static void main(String args[]) throws MyException{
//		//�ַ�����֤
//		checkFormat("ղ����","C",6,0,0,"����");
//		checkFormat("ղ����","C",10,0,1,"����");
//		checkFormat("","C",10,0,2,"����");
//		checkFormat("ղ����","C",10,0,2,"����");
//		checkFormat("","C",10,0,3,"����");
//		checkFormat("ղ����","C",6,0,3,"����");
//	
//		//ʱ����֤
//		checkFormat("2010-04-25","D",10,0,0,"ʱ��");
//		checkFormat("2010-04-25","D",10,0,1,"ʱ��");
//		
//		checkFormat("","D",10,0,2,"ʱ��");
//		checkFormat("2010-04-25","D",10,0,2,"ʱ��");
//		
//		checkFormat("","D",10,0,3,"ʱ��");
//		checkFormat("2010-04-25","D",10,0,3,"ʱ��");
//	
//		//����У��
//		checkFormat("123","N",3,0,0,"����");
//		checkFormat("123.04","N",6,2,0,"����");
//		
//		checkFormat("123","N",10,0,1,"����");
//		checkFormat("123.04","N",10,2,1,"����");
//		
//		checkFormat("","N",10,0,2,"����");
//		checkFormat("123","N",10,0,2,"����");
//		checkFormat("123.04","N",10,2,2,"����");
//		
//		checkFormat("","N",10,0,3,"����");
//		checkFormat("123","N",3,0,3,"����");
//		checkFormat("123.","N",4,2,3,"����");
//		
	}
}
