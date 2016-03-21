package com.fluent.framework.market.event;

import java.util.*;

import com.fluent.framework.events.out.*;
import com.fluent.framework.reference.*;


public final class MarketDataSubscribeEvent extends FluentOutEvent{

    private final ReferenceDataEvent[ ] refEvents;

    private static final long           serialVersionUID = 1L;


    public MarketDataSubscribeEvent( ReferenceDataEvent[ ] refEvents ){
        super( FluentOutType.MD_SUBSCRIBE_EVENT );

        this.refEvents = refEvents;

    }


    public final ReferenceDataEvent[ ] getReferenceEvents( ) {
        return refEvents;
    }


    @Override
    public final void toEventString( StringBuilder builder ) {
        builder.append( Arrays.deepToString( refEvents ) );
    }


}
