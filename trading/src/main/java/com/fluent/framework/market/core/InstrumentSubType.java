package com.fluent.framework.market.core;

import static com.fluent.framework.market.core.InstrumentType.*;


public enum InstrumentSubType{
	
	OTR_TRASURY	( TREASURY ),
	ED_FUTURES	( FUTURES ),
	TY_FUTURES	( FUTURES );
	
	private final InstrumentType type;
	
	private InstrumentSubType( InstrumentType type ){
		this.type = type;
	}

	
	public final InstrumentType getType(){
		return type;
	}

}
