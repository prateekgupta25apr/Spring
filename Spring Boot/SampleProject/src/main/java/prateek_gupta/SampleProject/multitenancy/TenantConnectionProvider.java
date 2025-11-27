package prateek_gupta.SampleProject.multitenancy;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static prateek_gupta.SampleProject.multitenancy.TenantContext.
        DEFAULT_SCHEMA_NAME;

@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {
    private final Logger log =
            LoggerFactory.getLogger(TenantConnectionProvider.class);

    /**
     * This field is like the Driver class which is used to connect to the Database
     */
    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * This method will actually connect to the schema
     */
    @Override
    public Connection getConnection(String schemaName) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            boolean useDefaultSchema=false;
            if (schemaName != null) {
                try{
                    connection.createStatement().execute("USE " + schemaName);
                }catch (Exception e){
                    log.error("Error occurred while setting schema for the tenant "+
                            "provided hence setting default schema");
                    useDefaultSchema=true;
                }
            } else
                useDefaultSchema=true;


            if(useDefaultSchema) {
                TenantContext.setCurrentTenant(DEFAULT_SCHEMA_NAME);
                connection.createStatement().execute("USE " + DEFAULT_SCHEMA_NAME);
            }
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Could not connect to the schema " + schemaName, e
            );
        }
        return connection;
    }

    @Override
    public void releaseConnection(String schemaName, Connection connection)
            throws SQLException {
        try {
            connection.createStatement().execute( "USE " + DEFAULT_SCHEMA_NAME );
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Could not release connection to the schema " + schemaName, e
            );
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
