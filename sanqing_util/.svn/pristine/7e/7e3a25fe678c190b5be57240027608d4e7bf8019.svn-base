package com.sanqing.sca.service;

import com.sanqing.dao.BusinessDAO;

public abstract class BaseAgent {
	
	public BusinessDAO dao;
	
	public BusinessDAO getDao() {
		return dao;
	}

	public void setDao(BusinessDAO dao) {
		this.dao = dao;
	}

	public abstract Protocol execute(Protocol protocol) throws Exception;
	
}
