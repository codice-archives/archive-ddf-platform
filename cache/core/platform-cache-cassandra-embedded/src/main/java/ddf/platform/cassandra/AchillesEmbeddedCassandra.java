/**
 * Copyright (c) Codice Foundation
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 * 
 **/
package ddf.platform.cassandra;

import info.archinnov.achilles.embedded.CassandraEmbeddedServerBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

public class AchillesEmbeddedCassandra {

    private static Logger LOGGER = LoggerFactory.getLogger(AchillesEmbeddedCassandra.class);
    
    private static final String CLUSTER_NAME = "DDF Cluster";
    
    private String ddfHomeDir = System.getProperty("karaf.home");
    private String cassandraDir;
    private String keyspaceName;
    
    
    public AchillesEmbeddedCassandra(String keyspaceName) {
        LOGGER.info("Embedded Cassandra running with NativeSession ...");
        this.keyspaceName = keyspaceName;       
        
        cassandraDir = ddfHomeDir + "/data/cassandra";
        
        String commitDirName = cassandraDir + "/commitlog";
        LOGGER.info("Cassandra commitlog dir = {}", commitDirName);
        File dir = new File(commitDirName);
        if (!dir.exists()) {
            LOGGER.info("Creating commitlog dir");
            dir.mkdirs();
        }
        
        String dataDirName = cassandraDir + "/data";
        LOGGER.info("Cassandra data dir = {}", dataDirName);
        dir = new File(dataDirName);
        if (!dir.exists()) {
            LOGGER.info("Creating data dir");
            dir.mkdirs();
        }
        
        String savedCachesDirName = cassandraDir + "/saved_caches";
        LOGGER.info("Cassandra saved_caches dir = {}", savedCachesDirName);
        dir = new File(savedCachesDirName);
        if (!dir.exists()) {
            LOGGER.info("Creating saved_caches dir");
            dir.mkdirs();
        }
        
        String configYamlFilename = cassandraDir + "/conf/cassandra.yaml";
        LOGGER.info("Cassandra config YAML file = {}", configYamlFilename);
        File f = new File(configYamlFilename);
        if (!f.exists()) {
            LOGGER.error("Cassandra config YAML file {} does not exist", configYamlFilename);
        }
        
        //NOTE: If CassandraDaemon (which this EmbeddedServerBulder uses)
        // has a problem during startup, it calls System.exit() which takes down
        // all of DDF ...     
        Session session = CassandraEmbeddedServerBuilder
            .noEntityPackages()
            .withClusterName(CLUSTER_NAME)
            .withDataFolder(dataDirName)
            .withCommitLogFolder(commitDirName)
            .withSavedCachesFolder(savedCachesDirName)
            .cleanDataFilesAtStartup(false)
            .withConfigYamlFile(configYamlFilename)
            .withKeyspaceName(keyspaceName) 
            .withCQLPort(9042)
            .withThriftPort(9160)
            .withStoragePort(7000)
            .withStorageSSLPort(7001)
            .withDurableWrite(true)
            .buildNativeSessionOnly();

        Cluster cluster = session.getCluster();
        Metadata metadata = cluster.getMetadata();
        List<KeyspaceMetadata> keyspaceMetadataList = metadata.getKeyspaces();
        Map<String, TableMetadata> tables = new HashMap<String, TableMetadata>();
        LOGGER.info("keyspaceMetadataList.size() = {}", keyspaceMetadataList.size());
        for (KeyspaceMetadata ksMetadata : keyspaceMetadataList) {
            LOGGER.info("keyspace name = {}", ksMetadata.getName());
            if (ksMetadata.getName().equals(keyspaceName)) {
                
                for (TableMetadata tableMetadata : ksMetadata.getTables()) {
                    LOGGER.info("table name = {}", tableMetadata.getName());
                    tables.put(tableMetadata.getName(), tableMetadata);
                }
            }
        }
    
        try {    
            if (!tables.containsKey("notifications")) {
                String query = "CREATE TABLE " + this.keyspaceName + ".notifications (id uuid PRIMARY KEY, userId text, timestamp timeuuid, application text, title text, message text)";
                LOGGER.info("Executing query: {}", query);
                session.execute(query);
                
                query = "INSERT INTO notifications (id, userId, timestamp, application, title, message) VALUES (62c36092-82a1-3a00-93d1-46196ee77204, 'Hugh', now(), 'Downloads', 'A10.jpg', 'Product retrieval started.')";
                LOGGER.info("Executing query: {}", query);
                session.execute(query);
            }
            
            String query = "SELECT * FROM " + this.keyspaceName + ".notifications";
            LOGGER.info("Executing query: {}", query);
            ResultSet resultSet = session.execute(query);
            
            for (Row row : resultSet.all()) {
                UUID id = row.getUUID("id");
                LOGGER.info("id = " + id.toString());
                String userId = row.getString("userid");
                LOGGER.info("userId = " + userId);
            }
        } catch(Exception e) {
            LOGGER.info("Exception: ", e);
        }
    }    
//
//    @Override
//    public void configurationUpdateCallback( Map<String, String> properties )
//    {
//        if (properties != null && !properties.isEmpty())
//        {
//            String value = properties.get(ConfigurationManager.HOME_DIR);
//            if (StringUtils.isNotBlank(value))
//            {
//                this.ddfHomeDir = value;
//                if (LOGGER.isDebugEnabled())
//                {
//                    LOGGER.debug("ddfHomeDir = " + this.ddfHomeDir);
//                }
//            }
//            else
//            {
//                LOGGER.debug("ddfHomeDir = NULL");
//            }
//        }
//    }
    
    public void shutdown() {
        LOGGER.info("Cassandra shutdown() invoked ...");
        //TODO ...
        //session.close();   //will this do it???
    }
}
