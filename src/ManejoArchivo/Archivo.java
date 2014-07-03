/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManejoArchivo;

import java.io.*;

/**
 *
 * @author nan
 */
public class Archivo {
File f;    
FileWriter w;
BufferedWriter bw;
PrintWriter wr;
        
public void AbrirArchivo(String a) throws IOException{
    f = new File(a);
    if (f.exists()){
        f.delete();
        
        f.createNewFile();
    }else{
        f.createNewFile();
    }
    w = new FileWriter(f);
    bw = new BufferedWriter(w);
    wr = new PrintWriter(bw);
    wr.write("#JSGF V1.0;");
    bw.newLine();
    wr.write("grammar sentence;");
    bw.newLine();
    bw.newLine();
    wr.write("public <sentence> =");
    
}

public void cerrarArchivo() throws IOException{
   bw.close();
   wr.close();
}

public void EscribirArchivo(String a) throws IOException{
//Escritura
bw.newLine();    
wr.write(a);   
}

public void SaltoLinea() throws IOException{
    bw.newLine();
}

}