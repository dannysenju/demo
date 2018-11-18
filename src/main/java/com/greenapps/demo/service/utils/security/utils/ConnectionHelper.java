package com.greenapps.demo.service.utils.security.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author dsolano
 */
public class ConnectionHelper {

    /**
     * instancia unica de la clase
     */
    private static ConnectionHelper instance = null;

    private static Connection conn = null;

    private ConnectionHelper(String dataSource) {
        if (conn == null) {
            initConn(dataSource);
        }
    }

    /**
     * @param dataSource
     * @return unique instance of class
     * @throws Exception
     */
    public static ConnectionHelper getInstance(String dataSource) throws Exception {
        if (instance == null) {
            instance = new ConnectionHelper(dataSource);
        }
        if (conn.isClosed()) {
            dispose();
            instance = new ConnectionHelper(dataSource);
        }
        return instance;
    }

    /**
     * Delete a dataBase connection
     */
    public static void dispose() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        instance = null;
    }

    /**
     * initialize a connection with datasource
     *
     * @param dataSourceName
     */
    private void initConn(String dataSourceName) {
        Context ctx = null;
        try {
            ctx = new InitialContext();
            DataSource ds = null;
            try {
                ds = (DataSource) ctx.lookup(dataSourceName);
            } catch (Exception e) {

                Context envCtx = (Context) ctx.lookup("java:/");
                ds = (DataSource) envCtx.lookup(dataSourceName);
            }
            conn = ds.getConnection();
        } catch (Exception e) {
            //Nothing
        }
    }

    /**
     * Get connection method
     *
     * @return
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * Close a connection
     *
     * @return
     */
    public boolean closeConn() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Obtains a Database connection managed by a given datasource
     *
     * @param dataSourceName
     * @return
     */
    public static Connection getConnDS(String dataSourceName) {
        Context ctx = null;
        try {
            ctx = new InitialContext();
            DataSource ds = null;
            try {
                ds = (DataSource) ctx.lookup(dataSourceName);
            } catch (Exception e) {
                Context envCtx = (Context) ctx.lookup(Constant.getInstance().getJNDI());
                ds = (DataSource) envCtx.lookup(dataSourceName);
            }
            conn = ds.getConnection();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
