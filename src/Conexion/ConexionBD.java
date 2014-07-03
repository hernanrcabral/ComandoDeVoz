/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Conexion;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nan
 */
public class ConexionBD  {

    private String driver = "org.postgresql.Driver"; // el nombre de nuestro driver Postgres.
    private String connectString = "jdbc:postgresql://localhost:5432/"; // llamamos nuestra bd
    private String user = "postgres"; // usuario postgres
    private String password = "postgres"; // no tiene password nuestra bd.
    private Connection conn;

   
    
    public ConexionBD( String user , String pass){
        this.user = user;
        this.password = pass;
    }
    
    /**
     * @param args the command line arguments
     */
    
    /*public static void main(String[] args) {
        ConexionBD a = new ConexionBD();
        a.conectar();
        a.cerrar();
    }*/

    
    public boolean conectar() {
        boolean a = false;
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn = DriverManager.getConnection(connectString, getUser(), getPassword());
            //Si la conexion fue realizada con exito, muestra el sgte mensaje.
            System.out.println("Conexion a la base de datos Ejemplo realizada con exito! ");
            a = true;
        } catch (SQLException ex) {
            System.out.println("Se ha producido un error en la conexion a la base de datos Ejemplo! ");
        }
        //Si la conexion fue realizada con exito, muestra el sgte mensaje.

        return a;
        //Cerramos la conexion
    }

    public void cerrar(){
        try {
            conn.close();
        }
        //Si se produce una Excepcion y no nos podemos conectar, muestra el sgte. mensaje.
        catch(SQLException e) {
            System.out.println("Se ha producido un error en la conexion a la base de datos Ejemplo! ");
        }
    }

    /**
     * @return the conn
     */

    public void Comit() throws SQLException{
        conn.commit();
    }
    
    public void rolbac() throws SQLException{
        
            conn.rollback();
        
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConn() {
        return conn;
    
    }

    public boolean SQLTableExists() {
        boolean exists = false;
        try {
            Statement stmt = conn.createStatement();
            String sqlText = "SELECT * FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\";";        
            if(stmt.execute(sqlText)) {
                System.out.println("esta creada");
                exists = true;
            }else{
                System.out.println("no esta creada");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exists;
    }
    
    public boolean CrearTablaBase(){
        boolean a = false;
        try {
            Statement stmt = conn.createStatement();
            String sqlText = "CREATE SCHEMA \"ProgramaDeVoz\";";
            stmt.execute(sqlText);
            sqlText = "CREATE TABLE \"ProgramaDeVoz\".\"DatosProgramas\" (\"Id\" Serial NOT NULL,\"Datos\" text NOT NULL,\"DatoPrograma\" text NOT NULL,\"Apodo\" text,CONSTRAINT \"DatosProgramas_pkey\" PRIMARY KEY (\"Id\"));";
            stmt.execute(sqlText);
            sqlText = "CREATE TABLE \"ProgramaDeVoz\".\"ProgramasEjecutables\" (\"IdEje\" Serial Not Null,\"Apodo\" text NOT NULL,\"Direccion\" text NOT NULL,\"Ejecutable\" text NOT NULL,CONSTRAINT \"ClavePrimaria\" PRIMARY KEY (\"Apodo\"),CONSTRAINT \"UpdateDelete\" FOREIGN KEY (\"Apodo\")REFERENCES \"ProgramaDeVoz\".\"ProgramasEjecutables\" (\"Apodo\") MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE);";
            stmt.execute(sqlText);
            sqlText = "Insert into \"ProgramaDeVoz\".\"DatosProgramas\" (\"Datos\", \"DatoPrograma\", \"Apodo\") values ('[<dato0>]','<dato0>=Ejecutar;',null),('[<dato1>]','<dato1>=Cerrar;',null),('[<dato2>]','<dato2>=Comando;',null),('[<dato3>]','<dato3>=Maximiza;',null),('[<dato4>]','<dato4>=Minimiza;',null),('[<dato5>]','<dato5>=Programa;',null),('[<dato6>]','<dato6>=Opciones;',null),('[<dato7>]','<dato7>=Cambiar;',null),('[<dato8>]','<dato8>=Restaurar;',null),('[<dato9>]','<dato9>=De;',null),('[<dato10>]','<dato10>=Ventana;',null),('[<dato11>]','<dato11>=Voz;',null),('[<dato12>]','<dato12>=Actual;',null);";
            stmt.execute(sqlText);
            a = true;
         } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
         }
         return a;
    }
    
    
    public boolean eliminarBasededatos(){
        boolean a = false;
        try {
            Statement stmt = conn.createStatement();
            String sqlText = "DROP SCHEMA \"ProgramaDeVoz\" CASCADE;";
            stmt.execute(sqlText);
            a = true;
         } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
         }
         return a;
    }
    
    
}