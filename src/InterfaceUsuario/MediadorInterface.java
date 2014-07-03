/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceUsuario;


import Comandosdevoz.Escucha;
import ManejoArchivo.Archivo;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.recognition.GrammarException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author nan
 */
public class MediadorInterface implements ActionListener, KeyListener {

    
    private Conexion.ConexionBD conectar;
    private static VentanaUsuario ventanaOpciones = new VentanaUsuario();
    private DefaultTableModel dtmProgramas;
    
    static String user;
    static String pass;
    

    public MediadorInterface( String u, String p){
        ventanaOpciones.setActionListeners(this);
        ventanaOpciones.setKeyListener(this);
        ventanaOpciones.show();
        ventanaOpciones.setLocationRelativeTo(null);
        ventanaOpciones.requestFocusInWindow();
        user = u;
        pass = p;
        conectar = new Conexion.ConexionBD(user,pass);
        this.ActualizarLista();
        ArmarArchivo();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int select = getVentanaOpciones().getjTableListaProgramas().getSelectedRow();
        if (e.getSource() == getVentanaOpciones().getjButtonInsertar()){
            InsertarPrograma();    
        }
        if (e.getSource() == getVentanaOpciones().getjButtonBorrarTodo()){
            BorrarTodo();
            ArmarArchivo(); 
        }
        if (e.getSource() == getVentanaOpciones().getjButtonEditar()){
            editar(select);
            ArmarArchivo();  
        }
        if (e.getSource() == getVentanaOpciones().getjButtonBorrar()){
            eliminar(select);
            ArmarArchivo();
        }
        if (e.getSource() == getVentanaOpciones().getjButtonActualizar()){
            ActualizarLista();
        }
        if (e.getSource() == getVentanaOpciones().getjButtonUpdate()){
            ActualizarPrograma();
            ArmarArchivo();  
        }
        if (e.getSource() == getVentanaOpciones().getjButtonSeleccionArchivo()){
            ElegirArchivo();
        }
        if (e.getSource() == getVentanaOpciones().getjButtonSalir()){
            ArmarArchivo();
            JOptionPane.showMessageDialog(null, "Si ejecto por medio de voz las opciones de voz devera cerrar el programa para guardar los cambios", "Cerrar para Recargar Archivo", JOptionPane.INFORMATION_MESSAGE);
            ventanaOpciones.dispose();   
        }
    }

    @Override
     public void keyTyped(KeyEvent e) {
        char caracter = e.getKeyChar();
        if(((caracter < 'A') || (caracter > 'Z')) && (caracter < 'a') || (caracter > 'z')){
            e.consume();
        }  
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    public void ActualizarLista(){
        String[] titulos = {"Codigo","Apodo", "Direccion", "Ejecutable"};
        Object[][] datos = {};
        dtmProgramas = new DefaultTableModel(datos, titulos);
        conectar.conectar();
        Statement st;
        try {
            st = conectar.getConn().createStatement();
            String consulta = "SELECT * FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\" order by \"IdEje\";";
            ResultSet rs = st.executeQuery(consulta);
            String id = null;
            while (rs.next()){
                Object[] fila = {rs.getString("IdEje"),
                rs.getString("Apodo"),
                rs.getString("Direccion"),
                rs.getString("Ejecutable")};
                getDtmProgramas().addRow(fila);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        getVentanaOpciones().getjTableListaProgramas().setModel(getDtmProgramas());
    }

    
    private void eliminar(int select){
        if (select != -1){
            String a = getDtmProgramas().getValueAt(select, 1).toString();
            conectar.conectar();
            int confirmado = JOptionPane.showConfirmDialog(null,"¿Está seguro que desea eliminar el Programa?", "Eliminar Programa", JOptionPane.YES_NO_OPTION);

            if (JOptionPane.OK_OPTION == confirmado){
                try {
                    Statement st = conectar.getConn().createStatement();
                    System.out.println(a);
                    String borrar = "DELETE FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\" WHERE \"Apodo\" = '"+a+"';";
                    st.execute(borrar);
                    st.close();
                    this.ActualizarLista();
                   // ArmarArchivo();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                conectar.cerrar();
                //this.ActualizarLista();
                
                JOptionPane.showMessageDialog(null, "Se ha eliminado el Programa", "Elemento eliminado", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "Error por seleccion nula", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * @return the dtmProgramas
     */
    public DefaultTableModel getDtmProgramas() {
        return dtmProgramas;
    }

    /**
     * @return the ventanaOpciones
     */
    public VentanaUsuario getVentanaOpciones() {
        return ventanaOpciones;
    }

    public void setVisible(boolean b) {
        if (b){
            ventanaOpciones.show(true);
            ventanaOpciones.requestFocus();
            ventanaOpciones.requestFocusInWindow();
            ventanaOpciones.getjTextFieldApodo().selectAll();
            ventanaOpciones.getjTextFieldApodo().requestFocus();
        }
        
    }
   
    public static void main (String[] arg){
        new MediadorInterface(user,pass);
    }

    private void editar(int s) {
        if (s != -1){
            ventanaOpciones.getjTextFieldCod().setText(getDtmProgramas().getValueAt(s, 0).toString());
            ventanaOpciones.getjTextFieldApodo().setText(getDtmProgramas().getValueAt(s, 1).toString());
            ventanaOpciones.getjTextFieldDireccion().setText(getDtmProgramas().getValueAt(s, 2).toString());
            ventanaOpciones.getjTextFieldEjecutable().setText(getDtmProgramas().getValueAt(s, 3).toString());
            ventanaOpciones.getjButtonInsertar().setEnabled(false);
            ventanaOpciones.getjButtonActualizar().setEnabled(false);
            this.ventanaOpciones.getjButtonBorrar().setEnabled(false);
            this.ventanaOpciones.getjButtonBorrarTodo().setEnabled(false);
            this.ventanaOpciones.getjButtonEditar().setEnabled(false);
            this.ventanaOpciones.getjButtonUpdate().setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "Error por seleccion nula", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ElegirArchivo(){
        String a = null;
        //Crear un objeto FileChooser
        JFileChooser fc = new JFileChooser();
        //Mostrar la ventana para abrir archivo y recoger la respuesta
        //En el parámetro del showOpenDialog se indica la ventana
        //  al que estará asociado. Con el valor this se asocia a la
        //  ventana que la abre.
        int respuesta = fc.showOpenDialog(this.ventanaOpciones);
        //Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION){
            //Crear un objeto File con el archivo elegido
            File archivoElegido = fc.getSelectedFile();
            //Mostrar el nombre del archvivo en un campo de texto
            
            String direccion = archivoElegido.getPath().replace(archivoElegido.getName(), "");
            String programa = archivoElegido.getName();
            System.out.println("Dir "+direccion);
            System.out.println("prog "+programa);
            ventanaOpciones.getjTextFieldDireccion().setText(direccion);
            ventanaOpciones.getjTextFieldEjecutable().setText(programa);
            
        }
        
    }
    
    private void InsertarPrograma() {
        conectar.conectar();
        Statement st;
        String Apodo = (getVentanaOpciones().getjTextFieldApodo().getText());
        String Direccion = (getVentanaOpciones().getjTextFieldDireccion().getText());
        String Ejecutable =(getVentanaOpciones().getjTextFieldEjecutable().getText());
        if ((!Apodo.equals("")&&!Direccion.equals("")&&!Ejecutable.equals(""))){
            try {
                st = conectar.getConn().createStatement();
                String insercion = "INSERT INTO \"ProgramaDeVoz\".\"ProgramasEjecutables\" (\"Apodo\", \"Direccion\", \"Ejecutable\") VALUES ('"+Apodo+"' ,'"+Direccion+"' ,'"+Ejecutable+"')";
                st.execute(insercion); 
                st.close();
                this.ActualizarLista();
                JOptionPane.showMessageDialog(null, "Se ha insertado el Programa", "Elemento insertado", JOptionPane.INFORMATION_MESSAGE);
                ArmarArchivoApodo(Apodo);
            } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Nose puede insertar", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            conectar.cerrar();
            getVentanaOpciones().getjTextFieldApodo().setText("");
            getVentanaOpciones().getjTextFieldDireccion().setText("");
            getVentanaOpciones().getjTextFieldEjecutable().setText("");
        }else{
            JOptionPane.showMessageDialog(null, "Deve escribir un apodo valido", "Campo Vacio", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void BorrarTodo() {
        conectar.conectar();
            Statement st;
            try {
                st = conectar.getConn().createStatement();
                String insercion = "DELETE FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\";";
                st.execute(insercion);
                st.close();
                JOptionPane.showMessageDialog(null, "Se ha Borrado todo los programas", "Elementos Borrados", JOptionPane.INFORMATION_MESSAGE);
                this.ActualizarLista();
                //ArmarArchivo();
            } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Nose puede Borrar", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
            conectar.cerrar();
    }

    private void ActualizarPrograma() {
            int cod = Integer.parseInt(ventanaOpciones.getjTextFieldCod().getText());
            String a = (ventanaOpciones.getjTextFieldApodo().getText());
            String b =(ventanaOpciones.getjTextFieldDireccion().getText());
            String c =(ventanaOpciones.getjTextFieldEjecutable().getText());
            conectar.conectar();
            Statement st = null;
            try {
                st = conectar.getConn().createStatement();
                String modifica = "UPDATE \"ProgramaDeVoz\".\"ProgramasEjecutables\" SET \"Apodo\" = '"+a+"', \"Direccion\"='"+b+"', \"Ejecutable\"='"+c+"' WHERE \"IdEje\"="+cod+";";
                st.execute(modifica);
                st.close();
                //--------------------------------------------------------------
                st = conectar.getConn().createStatement();
                String consulta = "SELECT \"DatoPrograma\" FROM \"ProgramaDeVoz\".\"DatosProgramas\" WHERE \"Apodo\"='"+a+"';";
                ResultSet rs = st.executeQuery(consulta);
                String aux1 = "";
                while (rs.next()){
                    aux1 = rs.getString("DatoPrograma");
                    break;
                }
                modifica = "UPDATE \"ProgramaDeVoz\".\"DatosProgramas\" SET \"DatoPrograma\" = '"+CambiarCadena(aux1,a)+"' WHERE \"Apodo\"='"+a+"';";
                st.execute(modifica);
                st.close();
                //--------------------------------------------------------------
                this.ActualizarLista();
                ventanaOpciones.getjButtonUpdate().setEnabled(false);
                ventanaOpciones.getjButtonInsertar().setEnabled(true);
                ventanaOpciones.getjButtonActualizar().setEnabled(true);
                this.ventanaOpciones.getjButtonBorrar().setEnabled(true);
                this.ventanaOpciones.getjButtonBorrarTodo().setEnabled(true);
                this.ventanaOpciones.getjButtonEditar().setEnabled(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Nose puede insertar", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            conectar.cerrar();
    }
    
    
    
    
    

    public void cerrar() {   
        getVentanaOpciones().dispose();
    }
    
    public void ArmarArchivoApodo(String apodo){
        Archivo a = new Archivo();
        conectar.conectar();
        Statement st;
        String dato, datoProg;
        try {
            a.AbrirArchivo(System.getProperty("user.home")+"/SimpleGrammarComandoVoz1.txt");
            st = conectar.getConn().createStatement();
            String consulta = "SELECT \"Id\", \"Datos\" FROM \"ProgramaDeVoz\".\"DatosProgramas\" order by \"Id\" ;";
            ResultSet rs = st.executeQuery(consulta);
            int clave = 0;
            while (rs.next()){
                clave = rs.getInt("Id");
                a.EscribirArchivo(rs.getString("Datos")); 
            }
            dato="[<dato"+clave+">]";
            a.EscribirArchivo(dato+";");
            a.SaltoLinea();
            consulta = "SELECT \"DatoPrograma\" FROM \"ProgramaDeVoz\".\"DatosProgramas\" order by \"Id\";";
            rs = st.executeQuery(consulta);
            while (rs.next()){
                a.EscribirArchivo(rs.getString("DatoPrograma"));
            }
            datoProg = "<dato"+clave+">="+apodo+";";
            a.EscribirArchivo(datoProg);
            st = conectar.getConn().createStatement();
            String insercion = "INSERT INTO \"ProgramaDeVoz\".\"DatosProgramas\" (\"Datos\", \"DatoPrograma\",\"Apodo\") VALUES ('"+dato+"' ,'"+datoProg+"','"+apodo+"')";
            st.execute(insercion); 
            st.close();
            a.cerrarArchivo();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ArmarArchivo(){
        Archivo a = new Archivo();
        conectar.conectar();
        Statement st;
        try {   
            a.AbrirArchivo(System.getProperty("user.home")+"/SimpleGrammarComandoVoz1.txt");
            st = conectar.getConn().createStatement();
            String consulta = "SELECT \"Id\", \"Datos\" FROM \"ProgramaDeVoz\".\"DatosProgramas\" order by \"Id\";";
            ResultSet rs = st.executeQuery(consulta);
            int clave = 0;
            while (rs.next()){
                clave = rs.getInt("Id");
                a.EscribirArchivo(rs.getString("Datos")); 
            }
            a.EscribirArchivo(";");
            a.SaltoLinea();
            consulta = "SELECT \"DatoPrograma\" FROM \"ProgramaDeVoz\".\"DatosProgramas\" order by \"Id\";";
            rs = st.executeQuery(consulta);
            while (rs.next()){
                a.EscribirArchivo(rs.getString("DatoPrograma"));
            }
            
            st.close();
            a.cerrarArchivo();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String CambiarCadena(String aux,String apodo) {
        String a = "";
        for ( int i = 0; i<aux.length(); i++){ 
            if (aux.charAt(i)!='='){
                a += aux.charAt(i);
            }else{
                break;
            }
        }
        return a+'='+apodo+';';
    }
    
}
