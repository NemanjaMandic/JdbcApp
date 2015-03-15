
package klase;

import java.sql.Connection;
 import java.sql.Statement;
 import java.sql.DriverManager;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel{

    private Connection con;
    private Statement st;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numberOfRows;
    
    private boolean connectedToDatabase = false;
    
    public ResultSetTableModel(String url, String username,
            String password, String query) throws SQLException{
            con = DriverManager.getConnection(url, username, password);
                st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            connectedToDatabase = true;
            
            setQuery(query);
       
       
    }
     public Class getColumnClass(int column){
       if(!connectedToDatabase)
           throw new IllegalStateException("Not connected to Database");
       
          try{
               String className = metaData.getColumnClassName(column + 1);
               return Class.forName(className);
           } catch (Exception ex) {
               
               System.out.println(ex.getCause());
             
       }
        
          return Object.class;
    }
   
    public int getRowCount() {
        if(!connectedToDatabase)
         throw new IllegalStateException("Not connected to database");
        return numberOfRows;
    }

    public int getColumnCount() {
       if(!connectedToDatabase)
           throw new IllegalStateException("Not connected to database");
           
           try{
               return metaData.getColumnCount();
           }catch(SQLException exc){
               System.out.println(exc.getMessage());
           }
           return 0;
       }
     public String getColumnName(int col){
         if(!connectedToDatabase)
             throw new IllegalStateException("Not connected to database");
         try{
             return metaData.getColumnName(col+1);
         }catch(SQLException exc){
             exc.printStackTrace();
         }
        return "";
     }

    @Override
    public Object getValueAt(int row, int col) {
       if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to database");
       try{
           rs.absolute(row+1);
           return rs.getObject(col+1);
       }catch(SQLException ex){
           ex.printStackTrace();
       }
       return "";
    }
    
   public void setQuery(String query) throws SQLException{
       if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to database");
       
       rs = st.executeQuery(query);
       metaData = rs.getMetaData();
       
       rs.last();
       numberOfRows = rs.getRow();
       
       fireTableStructureChanged();
   }
   public void disconnectFromDatabase(){
       if(connectedToDatabase){
           try{
               rs.close();
               st.close();
               con.close();
           }catch(SQLException ex){
               ex.printStackTrace();
           }finally{
               connectedToDatabase = false;
           }
       }
   }
}
