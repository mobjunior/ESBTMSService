/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author julius
 */
public class DBFunctions {

    private final Props props;

    public DBFunctions(Props props) {
        this.props = props;
    }

    public String getCustomerName(String accountNumber) {
        String response = "";

        try {
            String sql = "select * FROM [dbo].[TBAGENTS] where AGENTID=15";

            Database database = new Database(props);
            try (Connection connection = database.getDatabaseConnection()) {
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(sql)) {

                        while (resultSet.next()) {
                            String col1 = resultSet.getString(1);
                            String col2 = resultSet.getString(2);
                            // String col3 = resultSet.getString(3);

                            response = col1 + " " + col2 + " ";
                        }
                        if (response.trim().isEmpty()) {
                            System.out.println("No Data");
                        } else {
                            response = accountNumber;
                            System.out.println("DB Response : " + response);

                        }
                    } catch (SQLException ex) {
//                        logging.applicationLog(Utilities.logPreString() + "ERROR ON Statement handling: \n" + ex.getMessage(), "", "2");
                    }
                } catch (SQLException ex) {
//                    logging.applicationLog(Utilities.logPreString() + "ERROR ON Statement handling: \n" + ex.getMessage(), "", "2");
                }
            } catch (SQLException ex) {
//                logging.applicationLog(Utilities.logPreString() + "ERROR ON Statement handling: \n" + ex.getMessage(), "", "2");
            }
        } catch (Exception ex) {
//            logging.applicationLog(Utilities.logPreString()
//                    + "ERROR in Query Generation:- " + ex.getMessage(), "", "2");
        }
        return response;
    }
}
