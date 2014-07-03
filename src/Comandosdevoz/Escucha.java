package Comandosdevoz;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
  
/**
 *
 * @author nan
 */
import ManejoArchivo.Archivo;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.speech.*;
import javax.speech.recognition.*;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
 
public class Escucha extends ResultAdapter{

    /**
     * @return the exit
     */
    public RuleGrammar rg;
    private Conexion.ConexionBD conectar;
    static Recognizer recognizer;
    String gst, completoGst;
    InterfaceUsuario.MediadorInterface opciones;
    private static MuestraComando vent = new MuestraComando();
    public static ComandoValidos comandoVent = new ComandoValidos();
    String user;
    String pass;
    
    public Escucha(String u, String p){
        user = u;
        pass = p;
        conectar = new Conexion.ConexionBD(u,p);
    }
    
    public void mostrar(){
        vent.show(true);
    }
    
    
    @Override
    public void resultAccepted(ResultEvent re){
        vent.show();
        //vent.requestFocus();
        vent.requestFocusInWindow();
        vent.setAlwaysOnTop(true);
        vent.setAutoRequestFocus(false);
        completoGst = "";
        Runtime obj = Runtime.getRuntime();
        try{
            Result res = (Result)(re.getSource());//Escucha lo que digo por la voz
            ResultToken tokens[] = res.getBestTokens();//Separa por espacio lo que digo
            String args="";
            gst = tokens[0].getSpokenText();
            String pri="";
            String ult="";
            
            //touken          
            for (int i=0; i < tokens.length; i++){
                //Separa por cada espacio lo dicho y lo junta
                gst = tokens[i].getSpokenText();
                completoGst += gst;
                if(i==0){
                    pri=gst;
                }
                ult=gst;
            }   
            if (SentenciaSistema(completoGst, args)||SentenciaProgramas(pri,ult,args)){
                vent.getjTextField1().setText(completoGst);
            }

            System.out.println(completoGst);
            recognizer.resume();
        }catch(Exception ex){
            System.out.println("Ha ocurrido algo inesperado " + ex);
        }
    }
    
 public boolean SentenciaSistema(String a, String args) throws IOException, EngineException{
     //Metodo para abrir programas
     boolean bandera = true;
     switch ( a ) {
                case "CerrarProgramaDeVoz":
                    //Cierra Programa de voz
                    recognizer.deallocate();
                    args="Cerrando";
                    Lee.main(args);
                    
                    System.exit(0);
                    break;
                case "EjecutarOpcionesDeVoz":
                    if (this.opciones == null){
                        opciones = new InterfaceUsuario.MediadorInterface(user,pass);
                        opciones.setVisible(true);
                    } else {
                        opciones.setVisible(true);
                    }
                    args="Abriendo";
                    Lee.main(args);
                    break;
                case "CerrarOpcionesDeVoz":
                    JOptionPane.showMessageDialog(null, "Deve cerrar el programa para que se carguen los programas cargados ", "Cerrar para Recargar Archivo", JOptionPane.INFORMATION_MESSAGE);
                    opciones.cerrar();
                    args="Cerrando";
                    Lee.main(args);
                    break;    
                case "MinimizaVentanaActual":
                    //Minimiza ventanas
                    minimizarVentana();
                    args="Minimizado";
                    Lee.main(args);
                    break;
                case "MaximizaVentanaActual":
                    //Maximixa ventanas
                    maximizarVentana();
                    args="Maximizado";
                    Lee.main(args);
                    break;
                case "RestaurarVentanaActual":
                    //restaura ventana
                    restaurarVentana();
                    args="Restaurando";
                    Lee.main(args);
                    break;
                case "CambiarVentanaActual":
                    //Cambia ventana
                    cambiarVentana();
                    args="Cambiando";
                    Lee.main(args);
                    break;
                case "EjecutarComandoDeVoz":
                    ActualizarComandos();
                    comandoVent.show();
                    args="Abriendo";
                    Lee.main(args);
                    break;
                case "CerrarComandoDeVoz":
                    comandoVent.dispose();
                    args="Cerrando";
                    Lee.main(args);
                    break;    
                case "CerrarVentanaActual":
                    //Cierra ventana actual
                    cerrarVentana();
                    args="Cerrando";
                    Lee.main(args);
                    break;    
                default:
                    bandera = false;
                    break;
         }
 return bandera;        
 }
 
 public boolean SentenciaProgramas(String a,String b, String args){
     //Metodo para abrir programas
     boolean bandera = true;
     switch ( a ) {
                case "Ejecutar":
                    //Abrir programas y navegadores
                    if(Abrir(completoGst,b)){
                        args="Abriendo";
                        Lee.main(args);
                    }
                    break;
                case "Cerrar":
                    //Cerrar Programas y navegadores
                    if(Cerrar(completoGst,b)){
                        args="Cerrando";
                        Lee.main(args); 
                    }
                    break;    
                default:
                    bandera = false;
                    break;
         }
 return bandera;        
 }
 
 public void restaurarVentana(){
        try {
            //maximiza ventana con comandos de teclado
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyPress(KeyEvent.VK_R);
            robot.keyRelease(KeyEvent.VK_R);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            Logger.getLogger(Escucha.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     
 }
 
 public void maximizarVentana(){
        try {
            //maximiza ventana con comandos de teclado
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyPress(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            Logger.getLogger(Escucha.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     
 }
 
 public void minimizarVentana(){
        try {
            //minimiza ventana con comandos de rteclado
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyPress(KeyEvent.VK_N);
            robot.keyRelease(KeyEvent.VK_N); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            Logger.getLogger(Escucha.class.getName()).log(Level.SEVERE, null, ex);
        }
     
       
 }
 
 private void cambiarVentana() {
        try {
            //cambia ventana
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            Logger.getLogger(Escucha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
 public void cerrarVentana(){
        try {
            //cierra ventana con los comandos de teclado
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            Logger.getLogger(Escucha.class.getName()).log(Level.SEVERE, null, ex);
        }
 }
 
 public void Escuchar(){
    //Metodo escucha prinsipal para ejecutarse
     vent.show();
    try{
        recognizer = Central.createRecognizer(new EngineModeDesc(Locale.ROOT));
        recognizer.allocate();
        FileReader grammar1 = new FileReader(System.getProperty("user.home")+"/SimpleGrammarComandoVoz1.txt");
        rg = recognizer.loadJSGF(grammar1);
        rg.setEnabled(true);    
        recognizer.addResultListener(new Escucha(user,pass));
        System.out.println("Empieze Dictado");
        String args="Empieze Dictado";
        vent.getjTextField1().setText(args);
        Lee.main(args);
        recognizer.commitChanges();
        recognizer.requestFocus();
        recognizer.resume();
        
    }catch (Exception e){
        System.out.println("Exception en " + e.toString());
        e.printStackTrace();
        System.exit(0);
    }
    
  }

    private boolean Abrir(String completoGst, String ult) {
        boolean bandera = true;
        Runtime obj = Runtime.getRuntime();
        conectar.conectar();
        Statement st;
        String eje = null, dire = null;
        try {
            st = conectar.getConn().createStatement();
            String consulta = "SELECT * FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\"WHERE \"Apodo\" = '"+ult+"';";
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()){
                dire = rs.getString("Direccion");
                eje = rs.getString("Ejecutable");
                break;
            }
            System.out.println(eje);
            if (completoGst.equals("EjecutarPrograma"+ult)&& (eje != null) && (dire != null)){
                obj.exec(dire+eje);
            }else{
                bandera = false;
            }
        } catch (Exception e){
            bandera =false;
            e.printStackTrace();
        }
        return bandera;
    }
            
    private boolean Cerrar(String completoGst, String ult) {
        boolean bandera = true;
        Runtime obj = Runtime.getRuntime();
        conectar.conectar();
        Statement st;
        String eje = null;
        try {
            st = conectar.getConn().createStatement();
            String consulta = "SELECT * FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\"WHERE \"Apodo\" = '"+ult+"';";
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()){
                eje = rs.getString("Ejecutable");
                break;
            }
            if (completoGst.equals("CerrarPrograma"+ult)&& (eje != null)){
                String aux = eje.replaceAll(".exe","");
                System.out.println(aux);
                obj.exec("tskill "+aux);
            }else{
                bandera = false;
            }
        } catch (Exception e){
            bandera =false;
            e.printStackTrace();
        }
        return bandera;
    }
    
    public void ActualizarComandos(){
        String[] titulos = {"Apodo"};
        Object[][] datos = {};
        DefaultTableModel dtmApodo = new DefaultTableModel(datos, titulos);
        conectar.conectar();
        Statement st;
        try {
            st = conectar.getConn().createStatement();
            String consulta = "SELECT \"Apodo\" FROM \"ProgramaDeVoz\".\"ProgramasEjecutables\";";
            ResultSet rs = st.executeQuery(consulta);
            String id = null;
            while (rs.next()){
                Object[] fila = {
                rs.getString("Apodo")};
                dtmApodo.addRow(fila);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        comandoVent.getjTable1().setModel(dtmApodo);
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
}
