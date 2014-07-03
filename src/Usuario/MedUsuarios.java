/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Usuario;

import Comandosdevoz.Escucha;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author nan
 */
import java.awt.event.KeyEvent;
import javax.swing.InputMap;
import javax.swing.KeyStroke;



public class MedUsuarios implements ActionListener, KeyListener{

    private Usuarios ventanaPrin = new Usuarios();
    private Conexion.ConexionBD conectar;
    private InterfaceUsuario.MediadorInterface opciones;

    public MedUsuarios(){
        InputMap map = new InputMap();

        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
        ventanaPrin.getjButtonAceptar().setInputMap(0, map);
        ventanaPrin.getjButtonAceptar().setInputMap(0, map);
        //------------------------------------
        ventanaPrin.setActionListeners(this);
        ventanaPrin.setKeyListener(this);
        ventanaPrin.setLocationRelativeTo(null);
        ventanaPrin.show();
        ventanaPrin.getjTextFieldUsuario().requestFocus();
    }

    public void Mostrar(){
        ventanaPrin.show();
        ventanaPrin.getjPasswordField1().requestFocus(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ventanaPrin.getjButtonAceptar()){
            conectar = new Conexion.ConexionBD(ventanaPrin.getjTextFieldUsuario().getText(),ventanaPrin.getjPasswordField1().getText());
            if (conectar.conectar()){
                if(!conectar.SQLTableExists()){
                    conectar.CrearTablaBase();
                }
                Escucha c = new Escucha(ventanaPrin.getjTextFieldUsuario().getText(),ventanaPrin.getjPasswordField1().getText());
                c.ArmarArchivo();
                c.Escuchar();
                c.mostrar();
                ventanaPrin.dispose();
                conectar.cerrar();
            }else{
                JOptionPane.showMessageDialog(null, "Ingreso usuario o contraseña incorrecto ", "Usuario y contraseña BD Postgres", JOptionPane.INFORMATION_MESSAGE);
                ventanaPrin.getjPasswordField1().setText("");
                
            }
         }
         if (e.getSource() == ventanaPrin.getjButtonCancelar()){
             System.exit(0);
         }
         if (e.getSource() == ventanaPrin.getjButtonCargaProg()){
             conectar = new Conexion.ConexionBD(ventanaPrin.getjTextFieldUsuario().getText(),ventanaPrin.getjPasswordField1().getText());
             if (conectar.conectar()){
                if(!conectar.SQLTableExists()){
                    conectar.CrearTablaBase();
                }
                if (this.opciones == null){
                    opciones = new InterfaceUsuario.MediadorInterface(ventanaPrin.getjTextFieldUsuario().getText(),ventanaPrin.getjPasswordField1().getText());
                    opciones.setVisible(true);
                } else {
                    opciones.setVisible(true);
                }
             }else{
                JOptionPane.showMessageDialog(null, "Ingreso usuario o contraseña incorrecto ", "Usuario y contraseña BD Postgres", JOptionPane.INFORMATION_MESSAGE);
                ventanaPrin.getjPasswordField1().setText("");
            }   
         }
         if (e.getSource() == ventanaPrin.getjButtonReset()){
             conectar = new Conexion.ConexionBD(ventanaPrin.getjTextFieldUsuario().getText(),ventanaPrin.getjPasswordField1().getText());
             if (conectar.conectar()){
                if(!conectar.SQLTableExists()){
                    conectar.CrearTablaBase();
                }
                int i = JOptionPane.showConfirmDialog(null,"¿Realmente Desea eliminar todo los datos los datos?","Confirmar eliminar y Salida",JOptionPane.YES_NO_OPTION);
                if(i==0){
                    conectar = new Conexion.ConexionBD(ventanaPrin.getjTextFieldUsuario().getText(),ventanaPrin.getjPasswordField1().getText());
                    conectar.eliminarBasededatos();
                    System.exit(0);
                }
             }else{
                JOptionPane.showMessageDialog(null, "Ingreso usuario o contraseña incorrecto ", "Usuario y contraseña BD Postgres", JOptionPane.INFORMATION_MESSAGE);
                ventanaPrin.getjPasswordField1().setText("");
             } 
         }
    }

    public void keyTyped(KeyEvent e) {   
        if (e.getSource() == ventanaPrin.getjTextFieldUsuario() && e.getKeyChar() == KeyEvent.VK_ENTER){
            ventanaPrin.getjPasswordField1().requestFocus();
        } else if (e.getSource() == ventanaPrin.getjPasswordField1() && e.getKeyChar() == KeyEvent.VK_ENTER){
            ventanaPrin.getjButtonAceptar().doClick();
        }
        

    }

    public void keyPressed(KeyEvent e) {     
    
    }
    
    public void keyReleased(KeyEvent e) { 
    
    }  
    
}
