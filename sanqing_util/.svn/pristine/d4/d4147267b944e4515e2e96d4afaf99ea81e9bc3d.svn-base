package com.sanqing.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.sanqing.exception.MyException;

public class Protocol {
	
	private String service = "";
	private String task = "";
	private String recode = "";
	private String remsg = "";
	private Map<String,Map<String,String>> data = new HashMap<String,Map<String,String>>();
	private Map<String,List<Map<String,Object>>> table = new HashMap<String,List<Map<String,Object>>>();
	
	public Protocol(String service, String task) {
		this.service = service;
		this.task = task;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getRecode() {
		return recode;
	}

	public void setRecode(String recode) {
		this.recode = recode;
	}

	public String getRemsg() {
		return remsg;
	}

	public void setRemsg(String remsg) {
		this.remsg = remsg;
	}

	public Map<String, Map<String, String>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, String>> data) {
		this.data = data;
	}

	public Map<String, List<Map<String, Object>>> getTable() {
		return table;
	}

	public void setTable(Map<String, List<Map<String, Object>>> table) {
		this.table = table;
	}
	
	public Map<String,String> getData(String dataName){
		if(data == null || dataName == null || "".equals(dataName)){
			return new HashMap<String, String>();
		}
		return data.get(dataName);
	}
	
	public void putData(String dataName,Map<String, String> data) throws MyException{
		if(dataName == null || "".equals(dataName)){
			throw new MyException(ReturnCode.OTHER_ERROR_CODE, "data名称不允许为空");
		}
		this.data.put(dataName, data);
	}

	public void removeData(String dataName) throws MyException{
		if(dataName == null || "".equals(dataName)){
			throw new MyException(ReturnCode.OTHER_ERROR_CODE, "data名称不允许为空");
		}
		this.data.remove(dataName);
	}
	
	public void clearData() throws Exception{
		this.data.clear();
	}
	
	public List<Map<String,Object>> getTable(String tableName){
		if(table == null || tableName == null || "".equals(tableName)){
			return new ArrayList<Map<String,Object>>();
		}		
		return table.get(tableName);
	}

	public void putTable(String tableName,List<Map<String,Object>> table) throws Exception{
		if(tableName == null || "".equals(tableName)){
			throw new MyException(ReturnCode.OTHER_ERROR_CODE, "table名称不允许为空");
		}
		this.table.put(tableName, table);
	}
	
	public void removeTable(String tableName) throws Exception{
		if(tableName == null || "".equals(tableName)){
			throw new MyException(ReturnCode.OTHER_ERROR_CODE, "table名称不允许为空");
		}
		this.table.remove(tableName);
	}
	
	public void clearTable() throws Exception{
		this.table.clear();
	}
	
	public String meToJson() throws Exception{
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	public static Protocol jsonToMe(String str){
		Gson gson = new Gson();
		Protocol p  = gson.fromJson(str, Protocol.class);
		return p;
	}
}
