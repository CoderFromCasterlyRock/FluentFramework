package com.ccr.fluent.trading.events.core;



public enum FluentJsonTags{

    //HEADER
    EVENT_ID            ("EventId"),
    EVENT_TYPE          ("EventType"),
    TIMESTAMP           ("Timestamp"),
    EVENT_VALID         ("EventValid"),
    EVENT_CATEGORY      ("EventCategory"),
        
    //Admin
    ADMIN_ID            ("AdminId"),
    REASON				("Reason"),
    USER				("User"),
    
    //Reference Data
    REFERENCE_DATA      ("ReferenceData"),
    
    
    //Trader Request Header
    STRATEGY_NAME       ("StrategyName"),
    STRATEGY_OWNER      ("StrategyOwner"),
    STRATEGY_SIDE       ("StrategySide"),
    STRATEGY_LEG_COUNT  ("StrategyLegCount"),
    MEQ                 ("Meq"),
    SPREAD              ("Spread"),
    SIDES               ("Sides"),
    QUANTITIES          ("Quantities"),
    INSTRUMENTS         ("Instruments"),
    MARKETS             ("Markets"),

    //Market Data Header
    MARKET_DATA_ID      ("MarketDataId"),
    MARKET              ("Market"),
    INSTRUMENT_NAME     ("InstrumentName"),
    BID0                ("Bid0"),
    BID1                ("Bid1"),
    BID2                ("Bid2"),
    BID3                ("Bid3"),
    BID4                ("Bid4"),
    BID0SIZE            ("Bid0Size"),
    BID1SIZE            ("Bid1Size"),
    BID2SIZE            ("Bid2Size"),
    BID3SIZE            ("Bid3Size"),
    BID4SIZE            ("Bid4Size"),
    ASK0                ("Ask0"),
    ASK1                ("Ask1"),
    ASK2                ("Ask2"),
    ASK3                ("Ask3"),
    ASK4                ("Ask4"),
    ASK0SIZE            ("Ask0Size"),
    ASK1SIZE            ("Ask1Size"),
    ASK2SIZE            ("Ask2Size"),
    ASK3SIZE            ("Ask3Size"),
    ASK4SIZE            ("Ask4Size"),


    //Order Header
    ORDER_ID            ("OrderId"),
    ORDER_EXTERNAL_ID   ("ExternalId"),
    ORDER_TYPE          ("OrderType"),
    FILL_STATUS         ("FillStatus"),
    IS_REJECTED         ("IsRejected"),
    REJECTED_REASON     ("RejectedReason"),
    SIDE                ("Side"),
    PRICE               ("Price"),
    SEND_QUANTITY       ("SendQuantity"),
    SHOW_QUANTITY       ("ShowQuantity"),
    TRADER_ID           ("TraderId"),
    TRADER_NAME         ("TraderName"),
    PORTFOLIO           ("Portfolio"),

    STRATEGY_ID         ("StrategyId"),

    //Execution Header
    EXECUTION_QUANTITY  ("ExecutionQuantity"),
    EXECUTION_PRICE     ("ExecutionPrice"),

    //LOOPBACK
    UPDATE_MESSAGE      ("UpdateMessage"),
    INPUT_EVENT_ID      ("InputEventId"),
    INPUT_EVENT_TYPE    ("InputEventType"),
     
    INPUT_JSON_MESSAGE  ("InputJSonMessage");


    private final String field;

    private FluentJsonTags( String field ){
        this.field = field;
    }


    public final String field( ){
        return field;
    }

}
