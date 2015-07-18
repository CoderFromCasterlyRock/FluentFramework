package com.fluent.framework.events.out;



public interface OutListener{

	public String name( );
    public boolean isSupported( OutType type );
    public boolean update( OutEvent event );

}
