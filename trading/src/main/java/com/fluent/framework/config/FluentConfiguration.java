package com.fluent.framework.config;

import java.io.*;
import org.ini4j.*;

import com.fluent.framework.core.FluentContext;
import com.fluent.framework.core.FluentContext.*;
import com.fluent.framework.util.TimeUtil;

import static com.fluent.framework.util.FluentUtil.*;


public abstract class FluentConfiguration{
	
	private final Region region;
	private final Environment environment;
	private final String instance;
	private final String configFileName;
	private final Wini winiConfig; 
	
	private final String appName;
	private final String appRawOpenTime;
	private final long appOpenTime;
	private final String appRawCloseTime;
	private final long appCloseTime;
	private final String workingHours;
	
	private final static String CORE_SECTION_KEY	= "core";;
	private final static String APP_NAME_KEY		= "appName";
	private final static String APP_OPEN_TIME_KEY	= "openTime";
	private final static String APP_CLOSE_TIME_KEY	= "closeTime";
	private final static String CONFIG_PREFIX		= "../config/";
	private final static String CONFIG_SUFFIX		= ".ini";
	
	
	public FluentConfiguration( ){
	
		this.region 			= Region.getRegion( );
		this.environment		= Environment.getEnvironment();
		this.instance			= FluentContext.getInstance( );
		this.configFileName		= createConfigFileName( );
		this.winiConfig			= loadConfigs( configFileName ); 
		this.appName			= parseSection( CORE_SECTION_KEY, APP_NAME_KEY );
		
		this.appRawOpenTime		= parseSection( CORE_SECTION_KEY, APP_OPEN_TIME_KEY );
		this.appOpenTime		= TimeUtil.parseTime( CORE_SECTION_KEY, APP_OPEN_TIME_KEY, appRawOpenTime );
		this.appRawCloseTime	= parseSection( CORE_SECTION_KEY, APP_CLOSE_TIME_KEY );
		this.appCloseTime		= TimeUtil.parseTime( CORE_SECTION_KEY, APP_CLOSE_TIME_KEY, appRawCloseTime );
		this.workingHours		= appRawOpenTime + DASH + appRawCloseTime;
		
	}


	public final Region getRegion( ){
		return region;
	}


	public final Environment getEnvironment( ){
		return environment;
	}


	public final String getInstance( ){
		return instance;
	}

	
	public final String getConfigFileName( ){
		return configFileName;
	}
	
	
	public final String getAppName( ){
		return appName;
	}
	

	public final String getRawAppOpenTime( ){
		return appRawOpenTime;
	}

	
	public final long getAppOpenTime( ){
		return appOpenTime;
	}
	

	public final String getRawAppCloseTime( ){
		return appRawCloseTime;
	}
	
	
	public final long getAppCloseTime( ){
		return appCloseTime;
	}


	public final String getWorkingHours( ){
		return workingHours;
	}
	
	
	protected final Wini getWiniConfig( ){
		return winiConfig;
	}
	
	
	protected final String parseSection( String section, String name ){
		Ini.Section core 	= winiConfig.get( section );
		String value		= core.get( name );
		
		return value;
	}
	
		
	protected final Wini loadConfigs( String fileName ){
		
		Wini configuration	= null;
		FileReader reader 	= null;
		
		try{
			
			reader 			= new FileReader(fileName);
			configuration 	= new Wini( reader );
        
		}catch( Exception e ){
			throw new RuntimeException("Failed to load " + fileName, e );
			
		}finally{
			if( reader != null ){
				try{
					reader.close();
				}catch( Exception e ){}
			}
		}
		
		return configuration;

	}
	
	
	protected final String createConfigFileName( ){
		
		StringBuilder builder 	= new StringBuilder( );
	
		builder.append( CONFIG_PREFIX );
		builder.append( getEnvironment() );
		builder.append( SLASH);
		builder.append( getRegion() );
		builder.append( SLASH );
		builder.append( getInstance() );
		builder.append( CONFIG_SUFFIX );
		
		return builder.toString( );
	
		
	}

	
	@Override
	public String toString( ){
		return winiConfig.toString( );
	}

}

