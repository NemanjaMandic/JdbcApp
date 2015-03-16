

package addressbook_application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonQueries {
    
    public static final String URL = "jdbc:derby://localhost:1527/addressbook";
    public static final String USERNAME = "nemanja";
    public static final String PASS = "nemanja123";
    
    private Connection con = null;
    private PreparedStatement selectAllPeople = null;
    private PreparedStatement selectPeopleByLastName = null;
    private PreparedStatement insertNewPerson = null;
    
    public PersonQueries(){
        try{
            con = DriverManager.getConnection(URL, USERNAME, PASS);
            
            String selectAllSql = "SELECT * FROM address";
            selectAllPeople = con.prepareStatement(selectAllSql);
            
            String byLastNameSql = "SELECT * FROM address WHERE lastname = ?";
            selectPeopleByLastName = con.prepareStatement(byLastNameSql);
            
            String newPersonSql = "INSERT INTO address (firstname, lastname, email, phonenumber) VALUES(?,?,?,?)";
            insertNewPerson = con.prepareStatement(newPersonSql);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public List<Person> getAllPeople(){
        List<Person> results = null;
        ResultSet rs = null;
        try{
            rs = selectAllPeople.executeQuery();
            results = new ArrayList<>();
            while(rs.next()){
                results.add(new Person(
                        rs.getInt("addressID"),
                        rs.getString("firstName"), 
                        rs.getString("lastName"), 
                        rs.getString("email"), 
                        rs.getString("phoneNumber")));
            }
        }catch(SQLException exc){
            exc.printStackTrace();
        }finally{
            try{
                rs.close();
            }catch(SQLException ex){
                ex.printStackTrace();
               
            }
        }
        return results;
    }
    public List<Person> getPeopleByLastName(String name){
        List<Person> results = null;
        ResultSet rs = null;
        try{
            selectPeopleByLastName.setString(1, name);
            
            results = new ArrayList<>();
            while(rs.next()){
                results.add(new Person(rs.getInt("addressID"),
                        rs.getString("firstName"), 
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            try{
                rs.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return results;
    }
    public int addPerson(String fname, String lname, String email, String num){
        int result = 0;
        try{
          insertNewPerson.setString(1, fname);
          insertNewPerson.setString(2, lname);
          insertNewPerson.setString(3, email);
          insertNewPerson.setString(4, num);
          
          result = insertNewPerson.executeUpdate();
        }catch(SQLException exc){
            exc.printStackTrace();
            close();
        }
        return result;
    }
    public void close(){
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
