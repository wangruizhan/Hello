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
	//根据需要全部改为大写或者小写的格式llll。
	
	private Map<String,Object> mapKeyToUpperOrLower(Map<String,Object> maporg,boolean upper){
		Map<String,Object> retMap = new HashMap<String, Object>();
		for(String s:maporg.keySet()){
			retMap.put(upper?s.toUpperCase():s.toLowerCase(), maporg.get(s));


		}
		return retMap;
	}
	private static Logger logger = Logger.getLogger(BaseDefaultDao.class);
	/* 要注入的数据库类型 */
	private String dbType="oracle";
	public BaseDefaultDao(){}
	
	public SimpleJdbcTemplate jdbcHandle;
	private DataSource dataSource;
	
	public DataSource getDs() throws Exception{
		return this.dataSource;
	}

	/**
	 * 通过表名得到表的数据结构
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
	 * <p>设置数据源.此类作为所有Dao实例的基类，为dao提供了统一的数据源
	 * 通过提供不同的数据源，可以在不同数据库之间灵活切换</p>
	 * @param dataSource
	 */
    public void setDataSource(DataSource dataSource) throws Exception{
        this.jdbcHandle = new SimpleJdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
   /**
    * <p>查询记录条数</p>
    * @param sql 查询的sql语句
    * @param args 查询的条件
    * @return
    */
    public long queryForLong(String sql,Object... args) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	long l = -1l;
    	try{
    		l = this.jdbcHandle.queryForLong(sql, args);
    	}catch(EmptyResultDataAccessException e){
    		System.out.println("没有相关数据");
    	}
    	return l;
    }
    /**
     * 查询列表
    * @param sql 查询的sql语句
    * @param args 查询的条件
     * @return
     */
    public List<Map<String,Object>> queryForList(String sql,Object...args) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	return this.jdbcHandle.queryForList(sql, args);
    }
    
    /**
     * 查询单个MAP
     * @param sql 查询的sql语句
     * @param args 查询的条件
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
     * 进行数据更新,包括插入和更新
      * @param sql 查询的sql语句
    * @param args 查询的条件
     * @return
     */
    public int update(String sql,Object...args) throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	return this.jdbcHandle.update(sql, args);
    }
   /**
    * 指定表名
    * 指定相应更新字段的值(Map)
    * 指定要查询的条件(Map)
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
     * 添加新的记录
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
    	
    	//把传入MAP键值全转换成大写
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
    		throw new Exception("无效map");
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
     * 仅仅是去执行而已
     * @param sql
     */
    public void execute(String sql)throws Exception{
    	Assert.hasText(sql, "sql must be not null");
    	this.jdbcHandle.getJdbcOperations().execute(sql);
    }
    /**
     * 取得分页的总条数和分页记录，并存放到自定义的分页实体中
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
     * 根据传入的普通sql得到查询数量的sql
     * @param sql
     * @return
     */
	public  String getCountSQL(String sql) throws Exception{
		
		return "select count(1) as rowcount from ("+ sql +") ";
	}
	/**
	 * 把带有表名的字段名称替换为不带表名的字段
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
	 * 根据条件删除数据
	 * @param tbName 表名
	 * @param conds 条件map
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
	 * 批量删除
	 * @param tbName 表名
	 * @param conds 删除map
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
	
	
	//批量提交
	public void batchUpdate(String[] sqls ) throws Exception{
		this.jdbcHandle.getJdbcOperations().batchUpdate(sqls);
	}	
	
	//批量提交2
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
	
	//获取序列
	public long getNextval(String seqName) throws Exception {
		Map<String, Object> dMap = queryForMap("SELECT " + seqName +".nextval as seq FROM DUAL");
		return Long.parseLong(String.valueOf(dMap.get("SEQ")));
	}
	
	//分页查询
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
		
//		//通过表名获取表结构
//		Map<String,Object> map = businessDAO.back.getTableDesc("GG_CZRZ");
//		System.out.println(map);
		
//		//通过sql查询记录条数
//		String sql = "select count(1) from GG_CZRZ where GNMC =?";
//		String gnmc = "登录";
//		long num = businessDAO.back.queryForLong(sql, gnmc);
//		System.out.println(num);
		
//		//通过sql查询所有记录列表
//		String sql = "select * from GG_CZRZ where GNMC =?";
//		String gnmc = "登录eee";
//		List<Map<String,Object>> list = businessDAO.back.queryForList(sql, gnmc);
//		System.out.println(list);
		
//		//通过sql查询单条记录
//		String sql = "select * from GG_CZRZ where GNMC =?";
//		String gnmc = "登录";
//		Map<String,Object> map = businessDAO.back.queryForMap(sql, gnmc);
//		System.out.println(map);
		
//		//通过sql进行数据更新或插入
//		String sql = "insert into GG_CZRZ(RZBH,GNMC) values(?,?)";
//		String rzbh = "9999";
//		String gnmc = "登录";
//		int num = businessDAO.back.update(sql, rzbh,gnmc);
//		System.out.println(num);
//		
//		String sql = "update GG_CZRZ set GNMC = ? where RZBH = ?";
//		String rzbh = "9999";
//		String gnmc = "登录";
//		int num = businessDAO.back.update(sql,gnmc,rzbh);
//		System.out.println(num);
		
//		//通过表名更新数据
//		Map<String,Object> valueMap = new HashMap<String,Object>();		
//		String gnmc = "登录";
//		valueMap.put("GNMC", gnmc);
//		
//		Map<String,Object> conditionMap = new HashMap<String,Object>();
//		String rzbh = "9999";
//		conditionMap.put("RZBH", rzbh);
//		
//		int num = businessDAO.back.update("GG_CZRZ",valueMap, conditionMap);
//		System.out.println(num);
		
//		//通过表名插入数据
//		Map<String,Object> paramsMap = new HashMap<String,Object>();
//		String rzbh = "9999";
//		paramsMap.put("RZBH", rzbh);
//		
//		int num = businessDAO.back.add("GG_CZRZ", paramsMap);
//		System.out.println(num);
		
//		//通过表名删除数据
//		String rzbh = "9999";
//		Map<String,Object> conditionMap = new HashMap<String,Object>();
//		conditionMap.put("RZBH", rzbh);
//		int num = businessDAO.back.delete("GG_CZRZ", conditionMap);
//		System.out.println(num);
		
//		//通过表名批量删除数据
//		Map<String,Object[]> map = new HashMap<String, Object[]>();
//		Object o[] = {234,235,236}; 
//		map.put("RZBH", o);
//		int num = businessDAO.back.batchDelete("GG_CZRZ", map);
//		System.out.println(num);
		
//		//通过sql批量更新或插入
//		//批量插入
//		String sql = "insert into GG_CZRZ(RZBH,GNMC) values(?,?)";
//		List<Object[]> list = new ArrayList<Object[]>();		
//		Object o[] = {"1","登陆"};
//		list.add(o);
//		Object o2[] = {"2","登陆"};
//		list.add(o2);
//		businessDAO.back.batchUpdate(sql, list);
//		
//		//批量更新
//		String sql = "update GG_CZRZ set GNMC = ? where RZBH = ?";
//		List<Object[]> list = new ArrayList<Object[]>();		
//		Object o[] = {"登陆1","1"};
//		list.add(o);
//		Object o2[] = {"登陆2","2"};
//		list.add(o2);
//		businessDAO.back.batchUpdate(sql, list);
		
//		//通过序列名称获取序列下一个值
//		long num = businessDAO.back.getNextval("SEQ_CK_CZRZ");
//		System.out.println(num);
		
//		//执行SQL
//		String sql = "update GG_CZRZ set RZJB = 1";
//		businessDAO.back.execute(sql);
		
	} 
}