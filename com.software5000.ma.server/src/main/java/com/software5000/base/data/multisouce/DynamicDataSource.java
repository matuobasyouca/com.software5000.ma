package com.software5000.base.data.multisouce;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {  
	  
    /** 
     * 取得当前使用那个数据源。 
     */  
    @Override  
    protected Object determineCurrentLookupKey() {  
        return DbContextHolder.getDbType();    
    }  
  
}