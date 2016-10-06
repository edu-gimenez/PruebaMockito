package com.caseonit.pruebamockito;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;


/**
 * Aplicación de ejemplo para ser testeada con Mockito.
 *
 * Visor de fotos sencillo. Incluye acceso a archivos.
 */
public class MainActivity extends AppCompatActivity {

    //Pantalla
    public ImageView imageView1;                            //Para visualizar las fotos

    //Fotos
    public ArrayList<String> fotos = new ArrayList<>();     //Para contener los nombres de las fotos disponibles
    public int indiceFoto = 0;                              //Para apuntar a la foto a visualizar

    //Archivos
    public File carpeta;                                    //Carpeta donde se encuentran las fotos
    private String rutaFotos;                               //Ruta completa a la carpeta donde se encuentran las fotos

    //Whatsapp
    PackageManager pm;                                      //Para comprobar si está instalado Whatsapp
    Intent waIntent = new Intent(Intent.ACTION_SEND);       //Para abrir Whatsapp









////////////////////////////////////////  INICIALIZACIÓN  /////////////////////////////////////////

    /**
     * Entrada a la aplicación. Inicializa la pantalla, el sistema de archivos, y obtiene la lista de fotos.
      * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = getPackageManager();

        inicPantalla();
        inicArchivos();
        obtieneListaFotos();
        muestraFoto();
    }




    /**
     * Inicializa la pantalla: Carga el layout e inicializa el ImageView para mostrar las fotos
     */
    private void inicPantalla() {
        setContentView(R.layout.activity_main);
        imageView1 = (ImageView) findViewById(R.id.imageView);
    }




    /**
     * Inicializa las variables y objetos relacionados con el acceso a archivos.
     * Se utiliza una carpeta especial de pruebas, con imágenes de poco peso
     *    para hacer el código más sencillo y la ejecución más fluida
     * Este método se ha añadido para permitir los tests con Mockito, ya que declarando
     *    localmente los objetos de acceso a archivos no los podemos simular
     */
    public void inicArchivos() {
        File rutaImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        rutaFotos = rutaImg.getAbsolutePath().concat("/Pruebas/");
        carpeta = new File(rutaFotos);
    }




    /**
     * Obtiene los nombres de todas las fotos disponibles en la carpeta
     */
    public void obtieneListaFotos() {
        fotos.clear();
        String nombre;
//      File carpeta = new File(rutaFotos);            //Esta línea hace que no se pueda testear
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                nombre = archivo.getName();
                if (nombre.endsWith(".jpg") && !nombre.startsWith(".")) fotos.add(nombre);
            }
        }
    }






















    ////////////////////////////////////////  INTERACCIÓN  ////////////////////////////////////////

    /**
     * Muestra la foto anterior
     * @param v Botón "Anterior"
     */
    public void anteriorClick(View v) {
        siguienteFoto(-1);
        muestraFoto();
    }

    /**
     * Muestra la foto siguiente
     * @param v Botón "Siguiente"
     */
    public void siguienteClick(View v) {
        siguienteFoto(1);
        muestraFoto();
    }


    /**
     * Pasa a la foto anterior o la siguiente
     * Asegura que el índice de foto apunte a una foto válida
     * @param incremento 1:Siguiente foto.   -1:Foto anterior
     */
    public void siguienteFoto(int incremento) {
        indiceFoto = indiceFoto + incremento;
        if (indiceFoto >= fotos.size()) indiceFoto = 0;
        if (indiceFoto < 0) indiceFoto = fotos.size()-1;
    }

    /**
     * Pasa a la foto anterior o la siguiente
     * Asegura que el índice de foto apunte a una foto válida
     * Este método pasa el test, pero FALLA en una condición concreta
     * @param incremento 1:Siguiente foto.   -1:Foto anterior
     */
    public void siguienteFoto2(int incremento) {
        indiceFoto = (fotos.size() + indiceFoto + incremento) % fotos.size();
    }






    /**
     * Envía la foto por Whatsapp si Whatsapp está instalado
     * @param v Botón "Whatsapp"
     */
    public void whatsappClick(View v) {
        if (whatsappDisponible()) {
            enviaFotoWhatsapp();
        } else {
            Toast.makeText(this, "Whatsapp no instalado",Toast.LENGTH_SHORT).show();
        }
    }



















    ////////////////////////////////////  WHATSAPP  /////////////////////////////////////////////

    /**
     * Comprueba si Whatsapp está instalado
     * @return true si está instalado
     */
    public boolean whatsappDisponible() {
        try {
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
          return false;
        }
    }


    /**
     * Envía por Whatsapp la foto seleccionada
     */
    public void enviaFotoWhatsapp() {
        waIntent.setType("image/*");
        waIntent.setPackage("com.whatsapp");
        waIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(rutaFotos.concat(fotos.get(indiceFoto))));
        startActivity(waIntent);
    }


















   //////////////////////////////////////  VISUALIZACIÓN  ///////////////////////////////////////////

    /**
     * Muestra la foto seleccionada
     */
    public void muestraFoto() {
        if (fotos.size() > 0) {
            imageView1.setImageBitmap(BitmapFactory.decodeFile(rutaFotos.concat(fotos.get(indiceFoto))));
        }
    }


}
