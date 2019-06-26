package com.sanqing.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.Assert;

import com.sanqing.page.SplitPage;

public  class BaseDefaultDao {
	//������Ҫȫ����Ϊ��д����Сд�ĸ�ʽllll��
	
	private Map<String,Object> mapKeyToUpperOrLower(Map<String,Object> maporg,boolean upper){
		Map<String,Object> retMap = new HashMap<String, Object>();
		for(String s:maporg.keySet()){
			retMap.put(upper?s.toUpperCase():s.toLowerCase(), maporg.get(s));


		}
		return retMap;
	}
	private static Logger logger = Logger.getLogger(BaseDefaultDao.class);
	/* Ҫע������ݿ����� */
	private String dbType="oracle";
	public BaseDefaultDao(){}
	
	public SimpleJdbcTemplate jdbcHandle;
	private DataSource dataSource;
	
	public DataSource getDs() throws Exception{
		return this.dataSource;
	}

	/**
	 * ͨ�������õ�������ݽṹ
	 */
	private  Map<String, Object> getTableDesc(final String tablename) throws Exception{
		return (Map<String, Object>)
		jdbcHandle.getJdbcOperations().execute("select * from "+tablename, new PreparedStatementCallback(){
			public Map<String, Object> doInPreparedStatement(PreparedStatement p)throws SQLException, DataAccessException {
				HashMap<String ,Object> map=new HashMap<String ,Object> ();
				ResultSet rs=p.executeQuery();  
				ResultSetMetaData  md=rs.getMetaData();   
	              for (int   i=1;i<=md.getColumnCount();i++){   
	                  map.put(md.getColumnName(i), md.getColumnType(i));
	              }
				return mapKeyToUpperOrLower(map,true);
			}
			
		});
	}
	/**
	 * <p>��������Դ.������Ϊ����Daoʵ���Ļ��࣬Ϊdao�ṩ��ͳһ������Դ
	 * ͨ���ṩ��ͬ������Դ�������ڲ�ͬ���ݿ�֮������л�</p>
	 * @param dataSource
	 */
    public void setDataSource(DataSource dataSource) throws Exception{
        this.jdbcHandle = new SimpleJdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
   /**
    * <p>��ѯ��¼����</p>
    * @param sql ��ѯ��sql���
    * @param args ��ѯ������
    * @return
    */
    public long queryForLong(String sql,Object... args) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	long l = -1l;
    	try{
    		l = this.jdbcHandle.queryForLong(sql, args);
    	}catch(EmptyResultDataAccessException e){
    		System.out.println("û���������");
    	}
    	return l;
    }
    /**
     * ��ѯ�б�
    * @param sql ��ѯ��sql���
    * @param args ��ѯ������
     * @return
     */
    public List<Map<String,Object>> queryForList(String sql,Object...args) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	return this.jdbcHandle.queryForList(sql, args);
    }
    
    /**
     * ��ѯ����MAP
     * @param sql ��ѯ��sql���
     * @param args ��ѯ������
     * @return
     */
    public Map<String,Object> queryForMap(String sql,Object...args) throws Exception{
    	try{
    		Assert.hasText(sql, "sql must be not null");
    	    Map<String,Object>  m  = new HashMap<String, Object>();
    	    m  = this.jdbcHandle.queryForMap(sql, args);
    	    return mapKeyToUpperOrLower( m,true);  
    	}catch(Exception e){
    		return null;
    	}
    		
    }

    /**
     * �������ݸ���,��������͸���
      * @param sql ��ѯ��sql���
    * @param args ��ѯ������
     * @return
     */
    public int update(String sql,Object...args) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	return this.jdbcHandle.update(sql, args);
    }
   /**
    * ָ������
    * ָ����Ӧ�����ֶε�ֵ(Map)
    * ָ��Ҫ��ѯ������(Map)
    * @param tableName
    * @return
    * @throws Exception
    */
    public int update(String tableName,Map _valueMap,Map _conditionMap) throws Exception{
    	Assert.hasText(tableName, "must have table name ");
    	
    	Map<String,Object> tableViewMap = getTableDesc(tableName);
    	
    	Map valueMap = new HashMap();
    	Map conditionMap = new HashMap();
    	
    	for(Object s:_valueMap.keySet()){
    		if(tableViewMap.containsKey(s)){
    			valueMap.put(s, _valueMap.get(s));
    		}
    	}
    	for(Object s:_conditionMap.keySet()){
    		if(tableViewMap.containsKey(s)){
    			conditionMap.put(s, _conditionMap.get(s));
    		}
    	}
    	
    	List paramList = new ArrayList();
    	StringBuffer bufferSql = new StringBuffer();
    	bufferSql.append("update "+tableName+" set ");
    	Set vSet = valueMap.keySet();
    	for(Object key:vSet){
    		bufferSql.append(key+"=?,"); 
    		paramList.add(valueMap.get(key));
    	}
    	
    	bufferSql.deleteCharAt(bufferSql.length()-1);
    	bufferSql.append(" where 1=1 ");
    	Set cSet = conditionMap.keySet();
    	for(Object key:cSet){
    		bufferSql.append(" and "+key+"=? ");
    		paramList.add(conditionMap.get(key));
    	}
    	bufferSql.deleteCharAt(bufferSql.length()-1);
    	return update(bufferSql.toString(), paramList.toArray());
    }
    /**
     * ����µļ�¼
     * @param tableName
     * @param params
     * @return
     */
    public int add(String tableName,Map<String,Object> params ) throws Exception{
    	Assert.hasText(tableName, "must have table name ");
    	Map<String,Object> tableViewMap = getTableDesc(tableName);
    	
    	StringBuffer sql = new StringBuffer(100);
    	StringBuffer sql2 = new StringBuffer(100);
    	sql.append("insert into "+ tableName +"(");
    	sql2.append("values(");
    	List<Object> iList = new ArrayList<Object>();
    	
    	//�Ѵ���MAP��ֵȫת���ɴ�д
    	Map<String,Object> paramstemp = new HashMap<String, Object>();
    	for(String tm:params.keySet()){
    		paramstemp.put(tm.toUpperCase(), params.get(tm));
    	}
    	
    	for(String tn:tableViewMap.keySet()){
    		if(paramstemp.get(tn) != null){
    			sql.append(tn+",");
    			sql2.append("?,");
    			iList.add(paramstemp.get(tn));
    		}
    	}
    	if(iList.size() <= 0){
    		throw new Exception("��Чmap");
    	}
    	
    	sql.delete(sql.length()-1, sql.length());
    	sql2.delete(sql2.length()-1, sql2.length());
    	sql.append(") ");
    	sql2.append(") ");
    	sql.append(sql2);
    	
    	int in = this.jdbcHandle.update(sql.toString(), iList.toArray());
    	
    	return in;
    	
    }
    /**
     * ������ȥִ�ж���
     * @param sql
     */
    public void execute(String sql)throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	this.jdbcHandle.getJdbcOperations().execute(sql);
    }
    /**
     * ȡ�÷�ҳ���������ͷ�ҳ��¼������ŵ��Զ���ķ�ҳʵ����
     * @param sql
     * @param begin
     * @param end
     * @param args
     * @return
     */
    public List<Map<String,Object>> queryForPage(String sql,long begin,long end,Object... args ) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	String dbType = this.getDbType().trim().toLowerCase();
    	if(dbType.equals("oracle")){
    		sql = "select * from ( select split_rows.*, rownum as split_rows_num from (" + 
    		sql + ") split_rows where rownum < "+ end +") where split_rows_num > " + begin;
    	}else if(dbType.equals("mysql")){
    		sql = sql+" limit "+begin+","+(end-begin-1);
    	}else if(dbType.equals("sqlserver")){    		
    		sql = "select * top "+end+" from "+sql+" except select * top "+begin+" from "+sql;
    	}else if(dbType.equals("db2")){
    		sql = "(select * from "+sql+" fetch first "+end+" rows only) except (select * from "+sql+" fetch first "+end+" rows only)";
    	}else if(dbType.equals("postgresql")){
    		sql = sql+ "  limit "+(end-begin-1)+" offset "+begin+"";
    	}	
    	List<Map<String,Object>> list = queryForList(sql, args);
    	return list;
    }
    
    
    
    /**
     * ���ݴ������ͨsql�õ���ѯ������sql
     * @param sql
     * @return
     */
	public  String getCountSQL(String sql) throws Exception{
		
		return "select count(1) as rowcount from ("+ sql +") ";
	}
	/**
	 * �Ѵ��б������ֶ������滻Ϊ�����������ֶ�
	 * @param ms
	 * @return
	 */
	private Map<String,Object> transDotMapToPureMaps(Map<String,Object> ms) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		Set<String> keySet = ms.keySet();
		for(String s : keySet){
			Object o = ms.get(s);
			if(s.contains(".")){	
				String key = s.substring(s.indexOf(".")+1); 
				map.put(key, o);
			}else{
				map.put(s, o);
			}
		}
		return map;
		
	}
	
	/**
	 * ��������ɾ������
	 * @param tbName ����
	 * @param conds ����map
	 * @return
	 * @throws Exception
	 */
	public int delete(String tbName, Map<String, ?> conds) throws Exception{
		Assert.notEmpty(conds);
		List<Object> plist = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("DELETE ");
		sb.append(tbName.toUpperCase()).append(" WHERE ");
		
		for(String key : conds.keySet()){
			sb.append(key.toUpperCase()).append('=');			
			Object o = conds.get(key);
			sb.append('?');
			plist.add(o);			
			sb.append(" AND ");
		}
		sb.delete(sb.length() - 5, sb.length() - 1);
		
		return this.jdbcHandle.update(sb.toString(), plist.toArray());
	}
	
	/**
	 * ����ɾ��
	 * @param tbName ����
	 * @param conds ɾ��map
	 * @return
	 * @throws Exception
	 */
	public int batchDelete(String tbName, Map<String, Object[]> conds) throws Exception{
		Assert.notEmpty(conds);
		List<Object> plist = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("DELETE ");
		sb.append(tbName.toUpperCase()).append(" WHERE ");
		
		for (String key : conds.keySet()) {

			sb.append(key.toUpperCase()).append(" in(");
			Object[] o = conds.get(key);

			for (int j = 0; j < o.length; j++) {
				sb.append("?,");
				plist.add(o[j]);
			}
			sb.delete(sb.length() - 1, sb.length());
			sb.append(")");

			sb.append(" AND ");
		}
		sb.delete(sb.length() - 5, sb.length() - 1);
		return this.jdbcHandle.update(sb.toString(), plist.toArray());
	}
	
	
	//�����ύ
	public void batchUpdate(String[] sqls ) throws Exception{
		this.jdbcHandle.getJdbcOperations().batchUpdate(sqls);
	}	
	
	//�����ύ2
	public void batchUpdate(String sql ,List<Object[]> list) throws Exception{
		this.jdbcHandle.batchUpdate(sql, list);
	}

	
	public String getDbType() throws Exception{
		return dbType;
	}
	
	public void setDbType(String dbType) throws Exception{
		this.dbType = dbType;
	}
	
	public void rollback()throws Exception{
		this.getDs().getConnection().rollback();
	}
	
	public void commit()throws Exception{
		this.getDs().getConnection().commit();
	}
	
	//��ȡ����
	public long getNextval(String seqName) throws Exception {
		Map<String, Object> dMap = queryForMap("SELECT " + seqName +".nextval as seq FROM DUAL");
		return Long.parseLong(String.valueOf(dMap.get("SEQ")));
	}
	
	//��ҳ��ѯ
	public SplitPage splitPage(SplitPage splitPage,String sql,List<Object> searchParameterList) throws Exception{
		
		long pageSize = splitPage.getPageSize();
		long currPage = splitPage.getCurrPage();		
		
		long beginNum = 0;
		long endNum = 0;
		
		beginNum = (currPage - 1) * pageSize;
		if(beginNum > 0){
			endNum = beginNum + pageSize + 1;
		}else{
			beginNum = 0;
			endNum = beginNum + pageSize + 1;
		}
	
		long allcount =  queryForLong(getCountSQL(sql),searchParameterList.toArray());
		splitPage.setTotalCount(allcount);
		
		List<Map<String,Object>> list = queryForPage(sql, beginNum, endNum,searchParameterList.toArray());			
		splitPage.setListResult(list);
		
		return splitPage;
	}

	
	public static void main(String args[]) throws Exception{
		
//		Agent agent = HANDLE.agent;		
//		BusinessDAO businessDAO = agent.getBusinessDAO();
		
//		//ͨ��������ȡ��ṹ
//		Map<String,Object> map = businessDAO.back.getTableDesc("GG_CZRZ");
//		System.out.println(map);
		
//		//ͨ��sql��ѯ��¼����
//		String sql = "select count(1) from GG_CZRZ where GNMC =?";
//		String gnmc = "��¼";
//		long num = businessDAO.back.queryForLong(sql, gnmc);
//		System.out.println(num);
		
//		//ͨ��sql��ѯ���м�¼�б�
//		String sql = "select * from GG_CZRZ where GNMC =?";
//		String gnmc = "��¼eee";
//		List<Map<String,Object>> list = businessDAO.back.queryForList(sql, gnmc);
//		System.out.println(list);
		
//		//ͨ��sql��ѯ������¼
//		String sql = "select * from GG_CZRZ where GNMC =?";
//		String gnmc = "��¼";
//		Map<String,Object> map = businessDAO.back.queryForMap(sql, gnmc);
//		System.out.println(map);
		
//		//ͨ��sql�������ݸ��»����
//		String sql = "insert into GG_CZRZ(RZBH,GNMC) values(?,?)";
//		String rzbh = "9999";
//		String gnmc = "��¼";
//		int num = businessDAO.back.update(sql, rzbh,gnmc);
//		System.out.println(num);
//		
//		String sql = "update GG_CZRZ set GNMC = ? where RZBH = ?";
//		String rzbh = "9999";
//		String gnmc = "��¼";
//		int num = businessDAO.back.update(sql,gnmc,rzbh);
//		System.out.println(num);
		
//		//ͨ��������������
//		Map<String,Object> valueMap = new HashMap<String,Object>();		
//		String gnmc = "��¼";
//		valueMap.put("GNMC", gnmc);
//		
//		Map<String,Object> conditionMap = new HashMap<String,Object>();
//		String rzbh = "9999";
//		conditionMap.put("RZBH", rzbh);
//		
//		int num = businessDAO.back.update("GG_CZRZ",valueMap, conditionMap);
//		System.out.println(num);
		
//		//ͨ��������������
//		Map<String,Object> paramsMap = new HashMap<String,Object>();
//		String rzbh = "9999";
//		paramsMap.put("RZBH", rzbh);
//		
//		int num = businessDAO.back.add("GG_CZRZ", paramsMap);
//		System.out.println(num);
		
//		//ͨ������ɾ������
//		String rzbh = "9999";
//		Map<String,Object> conditionMap = new HashMap<String,Object>();
//		conditionMap.put("RZBH", rzbh);
//		int num = businessDAO.back.delete("GG_CZRZ", conditionMap);
//		System.out.println(num);
		
//		//ͨ����������ɾ������
//		Map<String,Object[]> map = new HashMap<String, Object[]>();
//		Object o[] = {234,235,236}; 
//		map.put("RZBH", o);
//		int num = businessDAO.back.batchDelete("GG_CZRZ", map);
//		System.out.println(num);
		
//		//ͨ��sql�������»����
//		//��������
//		String sql = "insert into GG_CZRZ(RZBH,GNMC) values(?,?)";
//		List<Object[]> list = new ArrayList<Object[]>();		
//		Object o[] = {"1","��½"};
//		list.add(o);
//		Object o2[] = {"2","��½"};
//		list.add(o2);
//		businessDAO.back.batchUpdate(sql, list);
//		
//		//��������
//		String sql = "update GG_CZRZ set GNMC = ? where RZBH = ?";
//		List<Object[]> list = new ArrayList<Object[]>();		
//		Object o[] = {"��½1","1"};
//		list.add(o);
//		Object o2[] = {"��½2","2"};
//		list.add(o2);
//		businessDAO.back.batchUpdate(sql, list);
		
//		//ͨ���������ƻ�ȡ������һ��ֵ
//		long num = businessDAO.back.getNextval("SEQ_CK_CZRZ");
//		System.out.println(num);
		
//		//ִ��SQL
//		String sql = "update GG_CZRZ set RZJB = 1";
//		businessDAO.back.execute(sql);
		
	} 
}