package com.ccr.fluent.trading.events.in;


public interface ExecutionEventProvider{
	
	public boolean addExecutionEvent( ExecutionReportEvent event );

}
