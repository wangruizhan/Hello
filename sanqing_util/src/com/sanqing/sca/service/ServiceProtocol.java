package com.sanqing.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sanqing.sca.message.ProtocolDecode.DataObject;
import com.sanqing.sca.message.ProtocolDecode.ProtocolMessage;
import com.sanqing.sca.message.ProtocolDecode.ProtocolMessage.Builder;
import com.sanqing.sca.message.ProtocolDecode.SimpleData;
import com.sanqing.sca.message.ProtocolDecode.SimpleMap;
import com.sanqing.sca.message.ProtocolDecode.Table;
import com.sanqing.sca.service.Protocol;
import com.sanqing.sca.service.ReturnCode;

public class ServiceProtocol {
	
	private static Logger logger = Logger.getLogger(ServiceProtocol.class);
	
	//从Builder中获取Service
	public static String getService(Builder builder){
		if(builder == null){
			return "";
		}
		return builder.getService();
	}
	
	//从Builder中获取Task
	public static String getTask(Builder builder){
		if(builder == null){
			return "";
		}
		return builder.getTask();
	}
	
	//从Builder中获取DataObject
	public static Map<String,Map<String,String>> getDataObject(Builder builder){
		if(builder == null){
			return new HashMap<String,Map<String,String>>();
		}		
		Map<String,Map<String,String>> dataMap = new HashMap<String,Map<String,String>>();			
		List<DataObject> listData = builder.getDataObjectList();
		for(int i=0;i<listData.size();i++){
			DataObject dataObject = listData.get(i);
			String dataName = dataObject.getDataName();
			SimpleMap simpleMap = dataObject.getSimpleMap();
			Map<String,String> innerMap = new HashMap<String, String>();		
			if(simpleMap != null){
				List<SimpleData> listSimpleData = simpleMap.getSimpleDataList();
				for(int j=0;j<listSimpleData.size();j++){				
					SimpleData simpleData = listSimpleData.get(j);
					innerMap.put(simpleData.getName(), simpleData.getValue());
				}
			}		
			dataMap.put(dataName,innerMap);
		}
		return dataMap;
	}
	
	//从Builder中获取Table
	public static Map<String,List<Map<String,Object>>> getTable(Builder builder){
		if(builder == null){
			return new HashMap<String,List<Map<String,Object>>>();
		}	
		Map<String,List<Map<String,Object>>> tableMap = new HashMap<String,List<Map<String,Object>>>();
		List<Table> listTable = builder.getTableList();
		for(int i=0;i<listTable.size();i++){
			Table table = listTable.get(i);
			String tableName = table.getName();
			List<SimpleMap>  listSimpleMap = table.getSimpleMapList();
			List<Map<String,Object>> listInner = new ArrayList<Map<String,Object>>();
			for(int j=0;j<listSimpleMap.size();j++){
				SimpleMap simpleMap = listSimpleMap.get(j);
				List<SimpleData> listSimpleData = simpleMap.getSimpleDataList();				
				Map<String,Object> innerInnerMap = new HashMap<String, Object>();
				for(int k=0;k<listSimpleData.size();k++){
					SimpleData simpleData = listSimpleData.get(k);
					innerInnerMap.put(simpleData.getName(), simpleData.getValue());
				}
				listInner.add(j,innerInnerMap);
			}
			tableMap.put(tableName, listInner);
		}
		return tableMap;		
	}
	
	//从ProtocolMessage中获取DataObject
	public static Map<String,Map<String,String>> getDataObject(ProtocolMessage protocolMessage){
		if(protocolMessage == null){
			return new HashMap<String,Map<String,String>>();
		}
		
		Map<String,Map<String,String>> dataMap = new HashMap<String,Map<String,String>>();
			
		List<DataObject> listData = protocolMessage.getDataObjectList();
		for(int i=0;i<listData.size();i++){
			DataObject dataObject = listData.get(i);
			String dataName = dataObject.getDataName();
			SimpleMap simpleMap = dataObject.getSimpleMap();
			Map<String,String> innerMap = new HashMap<String, String>();		
			if(simpleMap != null){
				List<SimpleData> listSimpleData = simpleMap.getSimpleDataList();
				for(int j=0;j<listSimpleData.size();j++){				
					SimpleData simpleData = listSimpleData.get(j);
					innerMap.put(simpleData.getName(), simpleData.getValue());
				}
			}		
			dataMap.put(dataName,innerMap);
		}
		
		return dataMap;
	}
	
	//从ProtocolMessage中获取Table
	public static Map<String,List<Map<String,Object>>> getTable(ProtocolMessage protocolMessage){
		if(protocolMessage == null){
			return new HashMap<String,List<Map<String,Object>>>();
		}
		
		Map<String,List<Map<String,Object>>> tableMap = new HashMap<String,List<Map<String,Object>>>();
		List<Table> listTable = protocolMessage.getTableList();
		for(int i=0;i<listTable.size();i++){
			Table table = listTable.get(i);
			String tableName = table.getName();
			List<SimpleMap>  listSimpleMap = table.getSimpleMapList();
			List<Map<String,Object>> listInner = new ArrayList<Map<String,Object>>();
			for(int j=0;j<listSimpleMap.size();j++){
				SimpleMap simpleMap = listSimpleMap.get(j);
				List<SimpleData> listSimpleData = simpleMap.getSimpleDataList();				
				Map<String,Object> innerInnerMap = new HashMap<String, Object>();
				for(int k=0;k<listSimpleData.size();k++){
					SimpleData simpleData = listSimpleData.get(k);
					innerInnerMap.put(simpleData.getName(), simpleData.getValue());
				}
				listInner.add(j,innerInnerMap);
			}
			tableMap.put(tableName, listInner);
		}
		
		return tableMap;		
	}
	
	//map转换成SimpleMap
	private static SimpleMap.Builder mapToDataObjectSimpleMap(Map<String,String> map){		
		SimpleMap.Builder sm = SimpleMap.newBuilder();
		if(map == null){
			return sm;
		}
		Set<Map.Entry<String, String>> entryseSet = map.entrySet();
		for(Map.Entry<String, String> entry:entryseSet){
			SimpleData.Builder simpleData = SimpleData.newBuilder();
			simpleData.setName(entry.getKey());
			simpleData.setValue(""+entry.getValue());
			sm.addSimpleData(simpleData);
		}
		return sm;
	}
	
	//map转换成SimpleMap
	private static SimpleMap.Builder mapToTableSimpleMap(Map<String,Object> map){		
		SimpleMap.Builder sm = SimpleMap.newBuilder();
		if(map == null){
			return sm;
		}
		Set<Map.Entry<String, Object>> entryseSet = map.entrySet();
		for(Map.Entry<String, Object> entry:entryseSet){
			SimpleData.Builder simpleData = SimpleData.newBuilder();
			simpleData.setName(entry.getKey());
			if(entry.getValue() == null || "".equals(entry.getValue())){
				simpleData.setValue("");
			}else{
				simpleData.setValue(entry.getValue().toString());
			}			
			sm.addSimpleData(simpleData);
		}
		return sm;
	}
	
	//map转换成DataObject列表
	private static List<DataObject.Builder> mapToDataObjectList(Map<String,Map<String,String>> map){
		List<DataObject.Builder> list = new ArrayList<DataObject.Builder>();
		if(map == null){
			return list;
		}
		Set<Map.Entry<String,Map<String,String>>> entryseSet = map.entrySet();
		for(Map.Entry<String,Map<String,String>> entry:entryseSet){
			DataObject.Builder dataObject = DataObject.newBuilder();
			dataObject.setDataName(entry.getKey());
			if(entry.getValue() != null){
				dataObject.setSimpleMap(mapToDataObjectSimpleMap(entry.getValue()));
			}		
			list.add(dataObject);
		}
		return list;
	}
	
	//map转换成Table列表
	private static List<Table.Builder> mapToTableList(Map<String,List<Map<String,Object>>> tableMap){
		List<Table.Builder> listTable = new ArrayList<Table.Builder>();
		if(tableMap == null){
			return listTable;
		}		
		Set<Map.Entry<String,List<Map<String,Object>>>> entryseSet = tableMap.entrySet();
		for(Map.Entry<String,List<Map<String,Object>>> entry:entryseSet){
			Table.Builder table = Table.newBuilder();
			table.setName(entry.getKey());
			List<Map<String,Object>> tableList = entry.getValue();
			if(tableList != null){
				for(int i=0;i<tableList.size();i++){
					SimpleMap.Builder simpleMap = mapToTableSimpleMap(tableList.get(i));
					table.addSimpleMap(i, simpleMap);
				}
				listTable.add(table);
			}		
		}		
		return listTable;
	}
	
	//通过protocol构建Builder
	public static Builder creatBuilder(Protocol protocol){
		Builder builder = ProtocolMessage.newBuilder();
		if(protocol == null){
			return builder;
		}
		
		String service = protocol.getService();
		String task = protocol.getTask();
		Map<String,Map<String,String>> dataMap = protocol.getData();
		Map<String,List<Map<String,Object>>> tableMap = protocol.getTable();		
		
		builder.setService(service);
		builder.setTask(task);
		
		List<DataObject.Builder> listData = ServiceProtocol.mapToDataObjectList(dataMap);
		for(int i=0;i<listData.size();i++){
			builder.addDataObject(i,listData.get(i));
		}
		
		List<Table.Builder> listTable = ServiceProtocol.mapToTableList(tableMap);
		for(int i=0;i<listTable.size();i++){
			builder.addTable(i, listTable.get(i));
		}
		return builder;
	}
	
	//通过protocol更新Builder
	public static Builder createBackBuilder(Builder builder,Protocol protocol){
		if(protocol == null){
			builder.setRecode(ReturnCode.WORK_EXECUTE_NULL_CODE);
			builder.setRemsg(ReturnCode.WORK_EXECUTE_NULL_REMSG);
			return builder;
		}
		
		List<DataObject.Builder> listData = mapToDataObjectList(protocol.getData());
		for(int i=0;i<listData.size();i++){
			builder.addDataObject(i,listData.get(i));
		}
		
		List<Table.Builder> listTable = mapToTableList(protocol.getTable());
		for(int i=0;i<listTable.size();i++){
			builder.addTable(i,listTable.get(i));
		}
		
		builder.setRecode(protocol.getRecode());
		builder.setRemsg(protocol.getRemsg());
		return builder;
	}
}
