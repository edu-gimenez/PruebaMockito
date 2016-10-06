package com.caseonit.pruebamockito;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.File;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


/**
 * Clase para probar los métodos de la aplicación.
 * Se utiliza Mockito para simular el método listFiles() de la clase File
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock File objFile;
    @Mock PackageManager packMan;
    @Mock Intent waIntent;


    @InjectMocks
    MainActivity actEnPruebas;



















/////////////////////////////////  TESTS DE INICIALIZACIÓN  ////////////////////////////////////

    /**
     * Prueba que funciona la obtención de los archivos
     * @throws Exception
     */
    @Test
    public void pruebaObtieneListaFotos() throws Exception {
        File[] lista = {new File("f1.jpg"), new File("f2.jpg"), new File("f3.xml"), new File("f4.jpg")};
        when(objFile.listFiles()).thenReturn(lista);
        actEnPruebas.obtieneListaFotos();
        assertEquals(3,actEnPruebas.fotos.size());
        assertEquals(actEnPruebas.fotos.get(0),"f1.jpg");
    }

















////////////////////////////////////  TESTS DE INTERACCIÓN  ///////////////////////////////////

    @Test
    public void pruebaCambioFoto() throws Exception {
        actEnPruebas.fotos.add("Foto_Indice_0.jpg");        //Preparamos 2 nombres de fotos
        actEnPruebas.fotos.add("Foto_Indice_1.jpg");

        actEnPruebas.indiceFoto = 0;                        //Empezamos en la foto 0
        actEnPruebas.siguienteFoto(1);                      //Siguiente => 1
        assertEquals(actEnPruebas.indiceFoto,1);
        actEnPruebas.siguienteFoto(1);                      //Siguiente => 0 otra vez
        assertEquals(actEnPruebas.indiceFoto,0);
        actEnPruebas.siguienteFoto(-1);                     //Anterior => 1 otra vez
        assertEquals(actEnPruebas.indiceFoto,1);
    }


    /**
     * Pasa el test a un método que falla si el número de fotos es 0
     * @throws Exception
     */
    @Test
    public void pruebaCambioFoto2() throws Exception {
        actEnPruebas.fotos.add("Foto_Indice_0.jpg");        //Preparamos 2 nombres de fotos
        actEnPruebas.fotos.add("Foto_Indice_1.jpg");

        actEnPruebas.indiceFoto = 0;                        //Empezamos en la foto 0
        actEnPruebas.siguienteFoto2(1);                     //Siguiente => 1
        assertEquals(actEnPruebas.indiceFoto,1);
        actEnPruebas.siguienteFoto2(1);                     //Siguiente => 0 otra vez
        assertEquals(actEnPruebas.indiceFoto,0);
        actEnPruebas.siguienteFoto2(-1);                    //Anterior => 1 otra vez
        assertEquals(actEnPruebas.indiceFoto,1);
    }






//////////////////////////////////////  TESTS DE WHATSAPP  ///////////////////////////////////

    @Test
    public void pruebaWhatsappDisponible() throws Exception {
        PackageInfo packinfo = new PackageInfo();
        when(packMan.getPackageInfo("com.whatsapp",PackageManager.GET_META_DATA)).thenReturn(packinfo);
        assertTrue(actEnPruebas.whatsappDisponible());
    }


    @Test
    public void pruebaWhatsapp() throws Exception {
        //Mockito no sirve para probar la llamada a startActivity
    }






}