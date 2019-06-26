package com.sanqing.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sanqing.exception.MyException;
import com.sanqing.sca.service.Protocol;
import com.sanqing.sca.service.ReturnCode;

public class SplitPage {
	
	private long pageSize;
	private long currPage;
	private long totalCount;
	private List<Map<String,Object>> listResult = new ArrayList<Map<String,Object>>();

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<Map<String, Object>> getListResult() {
		return listResult;
	}
	
	public void setListResult(List<Map<String, Object>> listResult) {
		this.listResult = listResult;
	}
	
	public static SplitPage getInstance(Map<String,String> dataMap) throws Exception{
	
		if(dataMap == null || dataMap.isEmpty()){
			throw new MyException(ReturnCode.INPUT_DATA_ERROR_CODE, "无效分页信息");
		}
		
		SplitPage splitPage = new SplitPage();
		
		if(dataMap.get(PageConfig.PAGE_SIZE) == null || "".equals(dataMap.get(PageConfig.PAGE_SIZE))){
			splitPage.setPageSize(PageConfig.DEFAULT_PAGE_SIZE);
		}else{
			splitPage.setPageSize(Long.parseLong(dataMap.get(PageConfig.PAGE_SIZE)));
		}
		
		if(dataMap.get(PageConfig.CURR_PAGE) == null || "".equals(dataMap.get(PageConfig.CURR_PAGE))){
			splitPage.setCurrPage(PageConfig.DEFAULT_CURR_PAGE);
		}else{
			splitPage.setCurrPage(Long.parseLong(dataMap.get(PageConfig.CURR_PAGE)));
		}
		
		if(dataMap.get(PageConfig.TOTAL_COUNT) == null || "".equals(dataMap.get(PageConfig.TOTAL_COUNT))){
			splitPage.setTotalCount(0);
		}else{
			splitPage.setTotalCount(Long.parseLong(dataMap.get(PageConfig.TOTAL_COUNT)));
		}
		
		return splitPage;
	}
	
	public void buildProtocol(Protocol protocol) throws Exception{
		
		Map<String,String> datamap = new HashMap<String, String>();
		datamap.put(PageConfig.CURR_PAGE, this.currPage+"");
		datamap.put(PageConfig.PAGE_SIZE, this.pageSize+"");
		datamap.put(PageConfig.TOTAL_COUNT,this.totalCount+"");
		
		protocol.putData(PageConfig.PAGE_SPLIT, datamap);
		protocol.putTable(PageConfig.PAGE_LIST, this.listResult);		
	}
}
