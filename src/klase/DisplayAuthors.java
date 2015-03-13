
package klase;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DisplayAuthors {
    
    // database URL
    static final String DB_URL = "jdbc:mysql://localhost/book";
    
    public static void main(String[] args){
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //establish connection to database
            connection = DriverManager.getConnection(DB_URL,"root","");
            
            //create statement for querying database
            statement = connection.createStatement();
            
            //query database
            resultSet = statement.executeQuery("SELECT authorID, firstName, lastName FROM authors");
            //process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.print("Authors Table of Book Database:\n");
            
            for(int i=1; i<=numberOfColumns; i++)
                System.out.printf("%-8s\t", metaData.getColumnName(i));
            System.out.println();
            
            while(resultSet.next()){
                for (int i = 1; i <= numberOfColumns; i++) {
                    System.out.printf("%-8s\t", resultSet.getObject(i));
                    System.out.println();
                }
            }
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally{
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
