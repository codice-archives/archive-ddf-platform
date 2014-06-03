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
package org.codice.ddf.notifications.cassandra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codice.ddf.notifications.NotificationStore;
import org.codice.ddf.notifications.PersistentNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import ddf.platform.cassandra.CassandraEmbeddedServer;

public class CassandraNotificationStore implements NotificationStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraNotificationStore.class);
    
    private CassandraEmbeddedServer cassandraServer;
    private Session session;
    
    
    public CassandraNotificationStore(CassandraEmbeddedServer cassandraServer) {
        this.cassandraServer = cassandraServer;
        this.session = this.cassandraServer.getSession();
    }
    
    @Override
    public List<Map<String, String>> getNotifications() {
        List<Map<String, String>> notifications = new ArrayList<Map<String, String>>();
        String query = "SELECT * FROM notifications";
        LOGGER.info("Executing query: {}", query);
        ResultSet resultSet = session.execute(query);
        
        for (Row row : resultSet.all()) {
            Map<String, String> notification = new HashMap<String, String>();
            UUID id = row.getUUID(PersistentNotification.NOTIFICATION_KEY_UUID);
            LOGGER.info("id = " + id.toString());
            notification.put(PersistentNotification.NOTIFICATION_KEY_UUID, id.toString());
            String userId = row.getString(PersistentNotification.NOTIFICATION_KEY_USER_ID);
            LOGGER.info("userId = " + userId);
            notification.put(PersistentNotification.NOTIFICATION_KEY_USER_ID, userId);
            String timestamp = row.getString(PersistentNotification.NOTIFICATION_KEY_USER_ID);
            LOGGER.info("timestamp = " + timestamp);
            notification.put(PersistentNotification.NOTIFICATION_KEY_TIMESTAMP, timestamp);
            String application = row.getString(PersistentNotification.NOTIFICATION_KEY_USER_ID);
            LOGGER.info("application = " + application);
            notification.put(PersistentNotification.NOTIFICATION_KEY_APPLICATION, application);
            String title = row.getString(PersistentNotification.NOTIFICATION_KEY_USER_ID);
            LOGGER.info("title = " + title);
            notification.put(PersistentNotification.NOTIFICATION_KEY_TITLE, title);
            String message = row.getString(PersistentNotification.NOTIFICATION_KEY_USER_ID);
            LOGGER.info("message = " + message);
            notification.put(PersistentNotification.NOTIFICATION_KEY_MESSAGE, message);
            notifications.add(notification);
        }
        
        return notifications;
    }

    @Override
    public List<Map<String, String>> getNotifications(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putNotification(Map<String, String> notification) {
        
        String query = String.format("INSERT INTO notifications (id, user, timestamp, application, title, message) VALUES (%s, '%s', '%s', '%s', '%s', '%s')",
                notification.get(PersistentNotification.NOTIFICATION_KEY_UUID),
                notification.get(PersistentNotification.NOTIFICATION_KEY_USER_ID),
                notification.get(PersistentNotification.NOTIFICATION_KEY_TIMESTAMP),
                notification.get(PersistentNotification.NOTIFICATION_KEY_APPLICATION),
                notification.get(PersistentNotification.NOTIFICATION_KEY_TITLE),
                notification.get(PersistentNotification.NOTIFICATION_KEY_MESSAGE));
        LOGGER.info("Executing query: {}", query);
        session.execute(query);    
    }

}
