package com.fluent.framework.config;

import java.util.*;
import org.slf4j.*;
import com.typesafe.config.*;

import com.fluent.framework.admin.StateManager;
import com.fluent.framework.core.FluentServiceType;
import com.fluent.framework.core.FluentService;
import com.fluent.framework.core.FluentContext.*;
import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.ExchangeDetails;

import static com.fluent.framework.util.TimeUtil.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;


public final class ConfigManager implements FluentService{
	
	private final Region region;
	private final Environment environment;
	private final Role role;
	private final String configFileName;
	private final Config configuration;
	
	private final String appName;
	private final String appRawOpenTime;
	private final long appOpenTime;
	private final String appRawCloseTime;
	private final long appCloseTime;
	private final String workingHours;
	private final TimeZone appTimeZone;
	
	private final Map<Exchange, ExchangeDetails> exchangeMap;
	
	public final static String APP_SECTION_KEY			= "fluent";
	public final static String FRAMEWORK_SECTION_KEY	= APP_SECTION_KEY + ".framework.";
	public final static String EXCHANGE_SECTION_KEY		= APP_SECTION_KEY + ".exchanges";
	public final static String MD_ADAPTORS_SECTION_KEY	= APP_SECTION_KEY + ".marketDataAdaptors.";
	private final static String APP_NAME_KEY			= FRAMEWORK_SECTION_KEY + "appName";
	private final static String APP_OPEN_TIME_KEY		= FRAMEWORK_SECTION_KEY + "openTime";
	private final static String APP_CLOSE_TIME_KEY		= FRAMEWORK_SECTION_KEY + "closeTime";
	private final static String APP_TIMEZONE_KEY		= FRAMEWORK_SECTION_KEY + "timeZone";
	
	
    private final static String NAME        = ConfigManager.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


	public ConfigManager( String configFileName ) throws Exception{
	
		this.configFileName		= notBlank(configFileName, "Config file name is invalid.");
		this.configuration		= loadConfigs( configFileName );
				
		this.region 			= Region.getRegion( configuration );
		this.environment		= Environment.getEnvironment( configuration );
		this.role				= Role.getRole( configuration );
		this.appName			= configuration.getString( APP_NAME_KEY );
		this.appTimeZone		= parseTimeZone(configuration.getString( APP_TIMEZONE_KEY ));
		this.appRawOpenTime		= configuration.getString( APP_OPEN_TIME_KEY );
		this.appRawCloseTime	= configuration.getString( APP_CLOSE_TIME_KEY );
		this.appOpenTime		= getAdjustedOpen(appRawOpenTime, appRawCloseTime, appTimeZone, System.currentTimeMillis());
		this.appCloseTime		= getAdjustedClose(appRawOpenTime, appRawCloseTime, appTimeZone, System.currentTimeMillis());
		
		this.workingHours		= appRawOpenTime + DASH + appRawCloseTime;
		this.exchangeMap		= createExchangeDetailsMap();
		
	}

	
	@Override
	public final FluentServiceType getServiceType( ){
		return FluentServiceType.CONFIG_MANAGER;
	}
	
	
	@Override
	public final void start( ){
		//verify all the necessary params
		LOGGER.info("Successfully started [{}]", getServiceType() );
	}
	
	public final Role getRole( ){
		return role;
	}
	

	public final Region getRegion( ){
		return region;
	}


	public final Environment getEnvironment( ){
		return environment;
	}
	
	
	public final boolean isProd( ){
        return ( Environment.PROD == environment );
    }
	
	
	public final String getConfigFileName( ){
		return configFileName;
	}
	
	
	public final String getAppName( ){
		return appName;
	}
	
	
	public final TimeZone getAppTimeZone( ){
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
	
	
	public final Map<Exchange, ExchangeDetails> getExchangeMap(){
		return exchangeMap;
	}
	
	
	protected final Config getConfig( ){
		return configuration;
	}
	
	
	public final String getFrameworkInfo( ){

        StringBuilder builder  = new StringBuilder( TWO * SIXTY_FOUR );
        
        builder.append( L_BRACKET );
        builder.append( "Name:" ).append( appName );
        builder.append( ", Environment:" ).append( environment );
        builder.append( ", Region:" ).append( region );
        builder.append( ", Role:" ).append( role );
        builder.append( ", State:" ).append( StateManager.getState() );
        builder.append( ", Process:" ).append( getFullProcessName() );
        builder.append( R_BRACKET );

        return builder.toString();

    }
	
	
	protected final Config loadConfigs( String fileName ){
		
		Config configuration= null;
		
		try{
			
			configuration 	= ConfigFactory.load(fileName);
			
		}catch( Exception e ){
			throw new RuntimeException("Failed to load " + fileName, e );
		}
		
		return configuration;

	}
	
	public final Map<Exchange, ExchangeDetails> getExchangeDetailsMap( ){
		return exchangeMap;
	}
	
	
	protected final Map<Exchange, ExchangeDetails> createExchangeDetailsMap( ) throws Exception{
		
		Map<Exchange, ExchangeDetails> eMAP = new HashMap<>( );
		List<? extends Config> eConfigList	= configuration.getConfigList(EXCHANGE_SECTION_KEY);
		
		for( Config eConfig : eConfigList ){
			
			String exchangeKey		= eConfig.getString("name");
			String openTime			= eConfig.getString("openTime");
			String closeTime		= eConfig.getString("closeTime");
			String timeZone			= eConfig.getString("timeZone");
			String speedLimit		= eConfig.getString("speedLimit");
			
			ExchangeDetails details	= new ExchangeDetails(exchangeKey, openTime, closeTime, timeZone, speedLimit);
			eMAP.put(details.getExchange(), details);
			
		}
		
		return eMAP;
		
	}
	
	
	public final Set<Config> getMarketDataAdaptorConfigs(){
		return Collections.emptySet();
	}
	
	
	@Override
	public final void stop( ){
	}
	
	
	@Override
	public String toString( ){
		return configuration.toString( );
	}

}

