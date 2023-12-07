package testconnect;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author cscharff Sample of JDBC for PostgreSQL 
 * ACID is implemented
 */

public class PostgreDemoACID {

 public static void main(String args[]) throws SQLException, IOException, 
 ClassNotFoundException {

  System.out.println("Begining");
  
  // Load the PostgreSQL driver
  // Connect to the database for the project with credentials
  Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "Anqi218549");

  // For atomicity
  conn.setAutoCommit(false);

  // For isolation
  conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

  Statement stmt1 = null;
  try {
   // Create statement object to send SQL queries
   stmt1 = conn.createStatement();
   
   // The depot d1 is deleted from Depot and Stock
   // We have to delete depid d1 from stock and depot
   // As the depid is foreign key in stock table, we have to perform
   // delete operation on stock table first
   //After that we will perform delete operation on depot table
   stmt1.executeUpdate("DELETE FROM stock WHERE dep_id='d1'");
   stmt1.executeUpdate("DELETE FROM depot WHERE dep_id='d1'");
   
   System.out.println("The depot d1 is deleted from depot and Stock");
  } catch (SQLException e) {
   System.out.println("An exception was thrown");
   e.printStackTrace();
   
   // For atomicity
   conn.rollback();
   stmt1.close();
   conn.close();
   return;
  }
  conn.commit();
  stmt1.close();
  conn.close();
  System.out.println("End");
 }
}
