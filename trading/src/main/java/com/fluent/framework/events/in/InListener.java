package com.fluent.framework.events.in;



public interface InListener{

	public String name();
    public boolean isSupported( InType type );
    public boolean inUpdate( InEvent event );

}
