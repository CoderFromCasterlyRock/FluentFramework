package com.fluent.framework.admin.events;

import com.fluent.framework.events.in.*;

import static com.fluent.framework.events.in.FluentInType.*;


public final class MetronomeEvent extends FluentInEvent{

    private final static long serialVersionUID = 1L;


    public MetronomeEvent( ){
        super( METRONOME_EVENT );
    }

    @Override
    public final void toEventString( StringBuilder builder ) {}


}
