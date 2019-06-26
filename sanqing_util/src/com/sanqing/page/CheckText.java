package com.sanqing.page;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sanqing.exception.MyException;
import com.sanqing.sca.service.ReturnCode;

public class CheckText {
	
	/**
	 * 字段检验
	 * @param text 检验字段值
	 * @param type 字段类型 "C" 为字符串，"N" 为数字,"D"为日期类型
	 * @param length 最大长度  整型数字 0表示不限制最大长度
	 * @param precision 精度 整型数字 表示保留小数位的长度,0表示是整数
	 * @param mode 整型数字   0（定长不允许空）/1（不定长不可空）/2（不定长允许空）/3（定长允许空）
	 * @param describe 描述
	 * @return 
	 */
	public static void checkFormat(String text,String type,int length, int precision,int mode,String describe)throws MyException{
		if(type == null || "".equals(type)){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"字段类型为空");
		}
		
		if(!"C".equals(type) && !"N".equals(type) && !"D".equals(type)){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"无法识别字段类型");
		}
		
		if(length < 0){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"非法长度");
		}
		
		if(precision < 0){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"非法精度");
		}
		
		if(mode < 0 || mode > 3){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"非法校验模式");
		}
		
		if(describe == null || "".equals(describe)){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,"字段名称为空");
		}		
		
		//字符串校验		
		if("C".equals(type)){
			if(mode == 0){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度为空");
				}
				if(length != 0 && text.getBytes().length != length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度不为"+ length);
				}
			}else if(mode == 1){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段为空");
				}else if(length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度大于"+ length);
				}
			}else if(mode == 2){
				if(text != null && length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度大于"+ length);
				}
			}else if(mode == 3){
				if(text != null && !"".equals(text) && text.getBytes().length != length && length != 0){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度不为"+ length);
				}
			}
		}else if("D".equals(type)){
			//判断是否是时间
			if(mode == 0){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度为空");
				}
				if(length != 0 && text.getBytes().length != length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度不为"+ length);
				}
				if(!isDate(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能格式化为时间类型");
				}
			}else if(mode == 1){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度为空");
				}
				if(length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度大于"+ length);
				}
				if(!isDate(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能格式化为时间类型");
				}
			}else if(mode == 2){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length > length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度大于"+ length);
					}
					if(!isDate(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能格式化为时间类型");
					}
				}
			}else if(mode == 3){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length != length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度不为"+ length);
					}
					if(!isDate(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能格式化为时间类型");
					}
				}
			}
		}else if("N".equals(type)){
			//判断是否是数字
			if(mode == 0){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段为空");
				}
				if(length != 0 && text.getBytes().length != length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度不为"+ length);
				}
				if(precision == 0){
					if(!isNumber(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成整数");
					}
				}else{
					String ts[] = text.split("\\.");
					if(ts.length == 1){
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
						}
					}else{
						if(ts.length != 2){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
						}
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
						}
						if(ts[1] != null && !"".equals(ts[1])){
							if(!isNumber(ts[1])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
							if(ts[1].getBytes().length > precision){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段精度大于"+ precision);
							}
						}					
					}				
				}
			}else if(mode == 1){
				if(text == null || "".equals(text)){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度为空");
				}
				if(length != 0 && text.getBytes().length > length){
					throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度大于"+ length);
				}
				if(precision == 0){
					if(!isNumber(text)){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成整数");
					}
				}else{
					String ts[] = text.split("\\.");
					if(ts.length == 1){
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
						}
					}else{
						if(ts.length != 2){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
						}
						if(!isNumber(ts[0])){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
						}
						if(ts[1] != null && !"".equals(ts[1])){
							if(!isNumber(ts[1])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
							if(ts[1].getBytes().length > precision){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段精度大于"+ precision);
							}
						}					
					}				
				}
			}else if(mode == 2){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length > length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度大于"+ length);
					}
					if(precision == 0){
						if(!isNumber(text)){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成整数");
						}
					}else{
						String ts[] = text.split("\\.");
						if(ts.length == 1){
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
						}else{
							if(ts.length != 2){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
							if(ts[1] != null && !"".equals(ts[1])){
								if(!isNumber(ts[1])){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
								}
								if(ts[1].getBytes().length > precision){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段精度大于"+ precision);
								}
							}					
						}				
					}				
				}
			}else if(mode == 3){
				if(text != null && !"".equals(text)){
					if(length != 0 && text.getBytes().length != length){
						throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段长度不为"+length);
					}
					if(precision == 0){
						if(!isNumber(text)){
							throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成整数");
						}
					}else{
						String ts[] = text.split("\\.");
						if(ts.length == 1){
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
						}else{
							if(ts.length != 2){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
							if(!isNumber(ts[0])){
								throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
							}
							if(ts[1] != null && !"".equals(ts[1])){
								if(!isNumber(ts[1])){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段不能转换成数字");
								}
								if(ts[1].getBytes().length > precision){
									throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE,describe +"字段精度大于"+ precision);
								}
							}					
						}				
					}
				}
			}
		}
	}
	
	//判断是否是整数
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
	
	//判断能否格式化成时间
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
//		//字符串验证
//		checkFormat("詹建文","C",6,0,0,"姓名");
//		checkFormat("詹建文","C",10,0,1,"姓名");
//		checkFormat("","C",10,0,2,"姓名");
//		checkFormat("詹建文","C",10,0,2,"姓名");
//		checkFormat("","C",10,0,3,"姓名");
//		checkFormat("詹建文","C",6,0,3,"姓名");
//	
//		//时间验证
//		checkFormat("2010-04-25","D",10,0,0,"时间");
//		checkFormat("2010-04-25","D",10,0,1,"时间");
//		
//		checkFormat("","D",10,0,2,"时间");
//		checkFormat("2010-04-25","D",10,0,2,"时间");
//		
//		checkFormat("","D",10,0,3,"时间");
//		checkFormat("2010-04-25","D",10,0,3,"时间");
//	
//		//数字校验
//		checkFormat("123","N",3,0,0,"数字");
//		checkFormat("123.04","N",6,2,0,"数字");
//		
//		checkFormat("123","N",10,0,1,"数字");
//		checkFormat("123.04","N",10,2,1,"数字");
//		
//		checkFormat("","N",10,0,2,"数字");
//		checkFormat("123","N",10,0,2,"数字");
//		checkFormat("123.04","N",10,2,2,"数字");
//		
//		checkFormat("","N",10,0,3,"数字");
//		checkFormat("123","N",3,0,3,"数字");
//		checkFormat("123.","N",4,2,3,"数字");
//		
	}
}
