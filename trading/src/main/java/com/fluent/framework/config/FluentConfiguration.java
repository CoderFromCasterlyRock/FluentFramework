package com.fluent.framework.config;

import com.fluent.framework.core.FluentContext;
import com.fluent.framework.core.FluentContext.*;
import com.fluent.framework.util.TimeUtil;

import static com.fluent.framework.util.FluentUtil.*;


public class FluentConfiguration{
	
	private final Region region;
	private final Environment environment;
	private final Instance instance;
	
	private final String appName;
	
	private final String appRawOpenTime;
	private final long appOpenTime;
	private final String appRawCloseTime;
	private final long appCloseTime;
	private final String workingHours;
	
	
	public FluentConfiguration( ){
	
		this.region 			= Region.getRegion( );
		this.environment		= Environment.getEnvironment();
		this.instance			= FluentContext.getInstance( );
		this.appName			= FluentContext.getAppName();
		
		this.appRawOpenTime		= "9:00:00";
		this.appOpenTime		= TimeUtil.parseTime( "OpenTime", appRawOpenTime );
		this.appRawCloseTime	= "17:00:00";
		this.appCloseTime		= TimeUtil.parseTime( "closeTime", appRawCloseTime );
		this.workingHours		= appRawCloseTime + DASH + appRawCloseTime;
		
	}


	public final Region getRegion( ){
		return region;
	}


	public final Environment getEnvironment( ){
		return environment;
	}


	public final Instance getInstance( ){
		return instance;
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
	

}

