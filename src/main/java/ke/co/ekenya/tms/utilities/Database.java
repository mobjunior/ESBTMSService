/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import ke.co.ekenya.tms.logsengine.TMSLog;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author julius
 */
public class Database {

    private Props props;
    private DataSource dataSource;
    private InitialContext initialContext;

    private Connection conn = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Statement statement = null;
    private String _sqlString;

    //private transient Logging logging;
    public Database(Props props) throws Exception {
        this.props = props;
        //this.logging = logging;
        try {
            //String url = "jdbc:mysql://localhost:3306/"+db;    //The database connection is here mysql
            String url = "jdbc:sqlserver://" + "10.20.2.22" + ";databaseName=" + "HudumaLife"; //The database connection is here mssql
            //Class.forName("com.mysql.jdbc.Driver"); //Call the driver for connecteion
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); //Call the driver for connecteion
            conn = DriverManager.getConnection(url, "sa", "!@#qweASD");
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }

    public Connection getDatabaseConnection() throws Exception {
        return this.conn;
    }

    public void closeDatabaseConnection() throws Exception {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (conn != null) {
            this.conn.close();
        }
        dataSource = null;
        if (initialContext != null) {
            initialContext.close();
        }
    }

    public ResultSet executeStatement(String _sqlString) {
        //logging.applicationLog(Utilities.logPreString() + "Query passed: \n" + _sqlString, "", "2");
        try {
            conn = getDatabaseConnection();

            statement = conn.createStatement();

            resultSet = statement.executeQuery(_sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            // logging.applicationLog(Utilities.logPreString() + "ERROR: SQLException \n" + e.getMessage(), "", "2");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            // logging.applicationLog(Utilities.logPreString() + "ERROR: Exception \n" + e.getMessage(), "", "2");
        }

        return resultSet;
    }

    public int execute(String _sqlString) {
        int affectedRows = 0;
        //logging.applicationLog(Utilities.logPreString() + "Query passed: \n" + _sqlString, "", "2");
        try {
            conn = getDatabaseConnection();

            statement = conn.createStatement();

            affectedRows = statement.executeUpdate(_sqlString);
            //  logging.applicationLog(Utilities.logPreString() + "Affected Rows: \n" + affectedRows, "", "2");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            // logging.applicationLog(Utilities.logPreString() + "ERROR: \n" + e.getMessage(), "", "2");
        }
        return affectedRows;
    }

    public void set_sqlString(String _sqlString) {
        this._sqlString = _sqlString;
    }

    public int executeInsertPreparedStatement(String _sqlString, Map<Integer, Object> params) {
//        logging.applicationLog(Utilities.logPreString() + "Query passed: \n" + _sqlString
//                + "\nWith Params:- " + params, "", "3");

        try {
            try (Connection conn = getDatabaseConnection()) {

                try (PreparedStatement preparedStatement = conn.prepareStatement(_sqlString, Statement.RETURN_GENERATED_KEYS)) {

                    if (!params.isEmpty()) {
                        for (int key : params.keySet()) {
                            preparedStatement.setObject(key, params.get(key));
                        }
                    }

                    int insertKey = 0;
                    preparedStatement.executeUpdate();

                    try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {

                        if (resultSet != null && resultSet.next()) {
                            insertKey = Integer.parseInt(String.valueOf(resultSet.getLong(1)));
                        }
                        return insertKey;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            //logging.applicationLog(Utilities.logPreString() + "ERROR: SQLException \n" + e.getMessage(), "", "2");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            // logging.applicationLog(Utilities.logPreString() + "ERROR: Exception \n" + e.getMessage(), "", "2");
            return 0;
        }
    }

    //2.Select statemet that returns a single column                              (Cool-Done)
    public String ExecuteQueryStringValue(String sql, String rslt, String index) {
        try {
            conn = getDatabaseConnection();  //Initiate a connecteeion to the database

            if (!conn.isClosed()) {

                //Test your query      
                statement = conn.createStatement();  //instaniate an object that is used to eecute sql statements
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    rslt = resultSet.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
            //  cl.logs(ex, e.getMessage());
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    try {
                        this.closeDatabaseConnection();
                    } catch (Exception ex) {
                        Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    conn.close();
                }
            } catch (SQLException e) {
                //  String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
                //cl.logs(ex, e.getMessage());
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
            }
        }
        return rslt;
    }
    
    //2.Select statemet that returns 2 columns                              (Cool-Done)
    public String ExecuteQueryStringValues(String sql, String rslt1,String rslt2, String index1,String index2) {
        try {
            conn = getDatabaseConnection();  //Initiate a connecteeion to the database

            if (!conn.isClosed()) {

                //Test your query      
                statement = conn.createStatement();  //instaniate an object that is used to eecute sql statements
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    rslt1 = resultSet.getString(index1);
                    rslt2 = resultSet.getString(index2);
                }
                if(rslt1=="" || rslt2 == ""){
                    rslt1="0";
                    rslt2="0";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
            //  cl.logs(ex, e.getMessage());
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    try {
                        this.closeDatabaseConnection();
                    } catch (Exception ex) {
                        Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    conn.close();
                }
            } catch (SQLException e) {
                //  String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
                //cl.logs(ex, e.getMessage());
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
            }
        }
        
        return rslt1 + "|"+rslt2;
    }

    public boolean ExecuteUpdate(String sql) {
        boolean result = false;
        try {

            conn = getDatabaseConnection();   //Initiate a connecteeion to the database

            if (!conn.isClosed()) {
                statement = conn.createStatement();  //instaniate an object that is used to eecute sql statements
                if (statement.executeUpdate(sql) >= 1) {
                    result = true;
                }
            }
        } catch (Exception e) {
            String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
            // cl.logs(ex, e.getMessage());
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    try {
                        this.closeDatabaseConnection();
                    } catch (Exception ex) {
                        Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    statement.close();
                }
            } catch (SQLException e) {
                String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
                // cl.logs(ex, e.getMessage());
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
            }
        }
        return result;
    }

    public ResultSet ExecuteQueryReturnString(String sql) {
        try {

            conn = getDatabaseConnection();   //Initiate a connecteeion to the database

            if (!conn.isClosed()) {
                //Test your query      
                statement = conn.createStatement();  //instaniate an object that is used to eecute sql statements
                resultSet = statement.executeQuery(sql);

            }
        } catch (Exception ex) {
            // String ex = Thread.currentThread().getStackTrace()[2].getMethodName();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        } finally {
            try {
                closeDatabaseConnection();
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                TMSLog el = new TMSLog(sw.toString());
                el.logfile();
            }
        }
        return resultSet;
    }
}
