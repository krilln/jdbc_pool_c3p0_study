package jdbc_pool_c3p0_study;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;



public class MyDataSource {
	private static final MyDataSource instance = new MyDataSource();

	public static MyDataSource getInstance() {
		return instance;
	}

	private DataSource dataSource;
	private MyDataSource() {
		Properties prop = loadProperties();
		
		try {
			DataSource ds_unpooled = DataSources.unpooledDataSource(prop.getProperty("url"), prop);
			Map<String, Object> overrides = new HashMap<>();
			overrides.put("maxStatements", "200");
			overrides.put("maxPoolSize", new Integer(50));
			
			dataSource = DataSources.pooledDataSource(ds_unpooled, overrides);
		}catch (SQLException e){
			e.printStackTrace();
		}
		/*for(Entry<Object, Object> e: prop.entrySet()) {
			System.out.printf("%s -> %s%n", e.getKey(), e.getValue());
		}*/
	}
	
	private  Properties loadProperties() {
		Properties properties = new Properties();
		try(InputStream is = ClassLoader.getSystemResourceAsStream("db.properties")){
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void close() {
		try {
			DataSources.destroy(dataSource);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
