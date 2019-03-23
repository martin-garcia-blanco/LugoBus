package com.example.martin.lugobus;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

class Servizo {

    // pongo 10.0.2.2 porque es lo que se recomienda para acceder a un servidor local desde un emulador
    private static String urlBase = "https://lugobus.alwaysdata.net/lugoBus/";

    /**
     * PARA CONECTARNOS AL SERVIDOR, LAS CREDENCIALES SON : lugobus      abc123.
     * PARA SUBIR COSAS POR FTP, EN UNA VENTANA DEL EXPLORADOR ESCRIBIMOS
     * ftp://ftp-lugobus.alwaysdata.net
     */

    //Convierte un String a URL
    private static URL cadea2URL(String cadeaUrl) {
        try {
            return new URL(cadeaUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Este método codifica la URL para que no tenga espacios, como un trim, pero para URL
    private static String encode(String cadea) {
        try {
            return URLEncoder.encode(cadea, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    //todo, CREAR EL LOGIN,
    // URL de login por ahora no lo uso
    public static URL urlLogin(String usuario, String password) {
        String cadeaURL = urlBase + "login.php?login=" + encode(usuario) + "&password=" + encode(password);
        System.out.println(cadea2URL(cadeaURL));
        return cadea2URL(cadeaURL);
    }


    //URL para descargar acceder al .php que contiene las lineas
    public static URL urlDescargaLineas() {
        String cadeaURL = urlBase + "lineas.php";
        return cadea2URL(cadeaURL);
    }


    //URL para descargar acceder al .php que contiene las paradas de una linea
    public static URL urlDescargaParadasLinea(long id) {
        String cadeaURL = urlBase + "paradas.php?idLinea="+id;
        return cadea2URL(cadeaURL);
    }


    //URL para descargar acceder al .php que contiene las paradas de una linea
    public static URL urlDescargaLineaCoincidentes(long id) {
        String cadeaURL = urlBase + "lineasCoincidentes.php?idParada="+id;
        return cadea2URL(cadeaURL);
    }



    //URL para descargar acceder al .php que contiene las paradas de una linea
    public static URL urlDescargaParadaId(long id) {
        String cadeaURL = urlBase + "paradaId.php?idParada="+id;
        System.out.println(cadea2URL(cadeaURL));
        return cadea2URL(cadeaURL);
    }

/**************************EXPLICACIÓN COMO ACCEDER A SHELL DO EMULADOR*******************
 * PRIMEIRO IR DESDE SHELL Á CARPETA  C:\Users\MartinGarcia\AppData\Local\Android\Sdk\platform-tools
 * EXECUTAT NESA CARPETA ADB.EXE
 * EXECUTAR ADB DEVICES, SE APARECEN DISPOSITIVOS
 * EXECUTAR ADB SHELL
 * UNHA VEZ FEITO ESTO ENTRAMOS O SHELL DE LINUX DO EMUADOR ANDROID
 * EXECUTAR RUN-AS com.example.martin.lugobus
 */

}
