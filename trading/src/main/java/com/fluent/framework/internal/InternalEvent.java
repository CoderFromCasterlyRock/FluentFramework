package com.fluent.framework.internal;

import com.eclipsesource.json.JsonObject;
import com.fluent.framework.events.core.*;


public abstract class InternalEvent extends FluentInboundEvent{

	private static final long serialVersionUID = 1L;


	public InternalEvent( FluentInboundType type ){
		super(type);
	}
	
	
	@Override
	public void toJSON( JsonObject object ){}

}