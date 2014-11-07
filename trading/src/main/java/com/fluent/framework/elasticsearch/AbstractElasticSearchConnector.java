package com.fluent.framework.elasticsearch;

import org.slf4j.*;
import org.elasticsearch.node.*;

import com.fluent.framework.core.*;

import org.elasticsearch.client.*;
import org.elasticsearch.action.index.*;
import org.elasticsearch.action.delete.*;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.action.admin.indices.delete.*;
import org.elasticsearch.action.admin.indices.exists.indices.*;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;


public abstract class AbstractElasticSearchConnector implements FluentService{

	private final Node node;
	private final Client client;
    private final String indexName;
    private final String typeName;
    private final String clusterName;
    private final IndicesAdminClient adminClient;

    private final static String TYPE    = "TABLE";
    private final static String NAME    = AbstractElasticSearchConnector.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );


    public AbstractElasticSearchConnector( String indexName, String clusterName ){
        this( indexName, TYPE, clusterName );
    }

    public AbstractElasticSearchConnector( String indexName, String typeName, String clusterName ){
		this.indexName      = indexName;
        this.typeName       = typeName;
        this.clusterName	= clusterName;

        this.node  			= nodeBuilder().clusterName( clusterName ).node();
		this.client 		= node.client();
        this.adminClient    = client.admin().indices();

	}


    protected final String getIndexName( ){
        return indexName;
    }

    protected final String getClusterName( ){
        return clusterName;
    }

    protected final Client getClient( ){
        return client;
    }


    protected final boolean indexExists( ){
        IndicesExistsResponse exists    = adminClient.exists( new IndicesExistsRequest( indexName ) ).actionGet();
        return exists.isExists();
    }


    protected final boolean createIndex( ){

        boolean indexedCreated          = false;

        try{

            if( indexExists() ){
                LOGGER.warn( "Index [{}] already exists!", indexName );
                return false;
            }

            IndexRequestBuilder builder = client.prepareIndex( indexName, typeName );
            IndexResponse response 	    = builder.execute().actionGet();
            indexedCreated              = response.isCreated();

        }catch( Exception e ){
            LOGGER.warn( "FAILED to create index with Type: [{}], IndexName: [{}].", typeName, indexName );
            LOGGER.warn( "Exception", e );
        }

        return indexedCreated;

    }



    //http://blog.florian-hopf.de/2013/05/getting-started-with-elasticsearch-part.html
    protected final void configureMapping( ){

        try{

            if( !indexExists() ){
                LOGGER.warn("Failed to add Field mapping as Index [{}] doesn't exist.", indexName );
                return;
            }

            XContentBuilder builder = XContentFactory.jsonBuilder().
                    startObject().
                    startObject( typeName ).
                    startObject( "properties" ).
                    startObject( "Time" ).
                    field( "type", "string" ).field("store", "yes").field( "index", "not_analyzed" ).
                    endObject().
                    startObject( "EventId" ).
                    field( "type", "string" ).field("store", "yes").field( "analyzer", "no" ).
                    endObject().
                    // more mapping
                    endObject().
                    endObject().
                    endObject();

            adminClient.preparePutMapping( indexName ).setType( typeName ).setSource(builder).execute().actionGet();

        }catch( Exception e ){
            LOGGER.warn( "Exception while attempting to configure mapping for Index [{}].", indexName, e );
        }
    }



    protected final boolean indexMessage( String eventId, String message  ){

        boolean indexedSuccessfully     = false;

        try{

            IndexRequestBuilder builder = getClient().prepareIndex( indexName, typeName, eventId );
            IndexResponse response 	    = builder.setSource( message ).execute().actionGet();
            
            client.admin().indices().prepareRefresh(indexName); 
            indexedSuccessfully         = response.isCreated();

        }catch( Exception e ){
            LOGGER.warn( "Exception while attempting to delete Index [{}].", indexName, e );
        }

        return indexedSuccessfully;

    }

    protected final boolean deleteIndex(  ){

        boolean deletedSuccessfully     = false;

        try{

            boolean indexExists         = indexExists( );
            if( !indexExists ){
                LOGGER.warn( "Discarding call to deleted Index [{}] as it doesn't exist.", indexName );
                return false;
            }

            DeleteIndexResponse delete  = adminClient.delete( new DeleteIndexRequest( indexName ) ).actionGet();
            if( !delete.isAcknowledged() ){
                LOGGER.warn("FAILED to delete Index [{}].",  indexName );
                return false;
            }

            LOGGER.debug( "Successfully deleted Index [{}].", indexName );
            deletedSuccessfully         = true;

        }catch(Exception e ){
            LOGGER.warn( "Exception while attempting to delete Index [{}].", indexName, e );
        }

        return deletedSuccessfully;

    }


    protected final boolean deleteMessage( String type, String id ){
        boolean deletedSuccessfully = false;

        try{

            boolean indexExists     = indexExists( );
            if( !indexExists ){
                LOGGER.warn( "Discarding call to deleted Index [{}] as it doesn't exist.", indexName );
                return false;
            }
            DeleteResponse response = client.prepareDelete( indexName, type, id ).execute().actionGet();
            deletedSuccessfully     = response.isFound();

            LOGGER.debug( "Deleted documents from Cluster [{}]", clusterName );

        }catch(Exception e ){
            LOGGER.warn( "Failed to deleted documents from Cluster [{}]", clusterName );
            LOGGER.warn( "Exception", e );
        }

        return deletedSuccessfully;
    }


    @Override
	public final void stop( ){
        try{
		    node.close();
            client.close();
		    LOGGER.debug( "Stopped client: {}", client.toString() );

        }catch( Exception e ){
            LOGGER.warn("Exception while closing client", e );
        }
	}

}
