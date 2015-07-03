package com.fluent.framework.config;

import com.typesafe.config.*;
import com.fluent.framework.util.*;
import com.fluent.framework.core.*;
import com.fluent.framework.core.FluentContext.*;

import static com.fluent.framework.util.FluentUtil.*;


public abstract class ConfigManager{
	
	private final Region region;
	private final Environment environment;
	private final String instance;
	private final String configFileName;
	private final Config configuration; 
	
	private final String appName;
	private final String appRawOpenTime;
	private final long appOpenTime;
	private final String appRawCloseTime;
	private final long appCloseTime;
	private final String workingHours;
	private final String appTimeZone;
	
	protected final static String APP_SECTION_KEY		= "fluent.";
	private final static String CORE_SECTION_KEY	= APP_SECTION_KEY + "core.";
	private final static String APP_NAME_KEY		= CORE_SECTION_KEY + "appName";
	private final static String APP_OPEN_TIME_KEY	= CORE_SECTION_KEY + "openTime";
	private final static String APP_CLOSE_TIME_KEY	= CORE_SECTION_KEY + "closeTime";
	private final static String APP_TIMEZONE_KEY	= CORE_SECTION_KEY + "timeZone";
	
	private final static String CONFIG_PREFIX		= "../config/";
	private final static String CONFIG_SUFFIX		= ".conf";
	
	
	public ConfigManager( ){
	
		this.region 			= Region.getRegion( );
		this.environment		= Environment.getEnvironment();
		this.instance			= FluentContext.getInstance( );
		this.configFileName		= createConfigFileName( );
		this.configuration		= loadConfigs( configFileName ); 
		this.appName			= configuration.getString( APP_NAME_KEY );
		this.appTimeZone		= configuration.getString( APP_TIMEZONE_KEY );
		
		this.appRawOpenTime		= configuration.getString( APP_OPEN_TIME_KEY );
		this.appOpenTime		= TimeUtil.parseTime( CORE_SECTION_KEY, APP_OPEN_TIME_KEY, appRawOpenTime );
		this.appRawCloseTime	= configuration.getString( APP_CLOSE_TIME_KEY );
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
	
	
	public final String getAppTimeZone( ){
		return appTimeZone;
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
	
	
	protected final Config getConfig( ){
		return configuration;
	}
	
	
	protected final Config loadConfigs( String fileName ){
		
		Config configuration= null;
		
		try{
			
			configuration 	= ConfigFactory.load( fileName );
			
		}catch( Exception e ){
			throw new RuntimeException("Failed to load " + fileName, e );
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
		return configuration.toString( );
	}

}

