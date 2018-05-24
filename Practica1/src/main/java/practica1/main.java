package practica1;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.Scanner;

import java.net.*;
import java.io.*;


public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("======================================================================================\n");
        System.out.println("PROYECTO #1 - Creación Cliente HTTP – Apache Components");
        System.out.println("    Presentado por: Adonis A. Veloz");
        System.out.println("    Matrícula: 2014-0452");
        System.out.println("\n======================================================================================");

        //Declaración de Variables
        long countLine = 0;                     //Contador de líneas
        Elements images = new Elements();       //Lista de imágenes
        Elements parrafos = new Elements();     //Lista de Párrafos
        Elements formularios = new Elements();  //Lista de formularios.
        Elements inputs = new Elements();       //Lista de inputs

        //Variable de entrada por el teclado.
        Scanner user_input = new Scanner( System.in );


        System.out.print("Ingrese el URL de la página: ");
        String URL = "https://" + user_input.next().trim().replace("https://","");

        /*---------------------------------------------Entrada del URL---------------------------------------------
        * Aquí se le pide al usuario que escriba la URL que se va a analizar. Se dió la opción tanto de escribir como
        * ignorar el "http://", al mismo tiempo que se asegura de eliminar los espacios en blanco antes o despues de
        * que se escribió.
        * */

        System.out.println("======================================================================================");
        try {
            //A través del URL, se utiliza la librería JSOUP para parsear el HTML.
            Document doc = Jsoup.connect(URL).get();

            System.out.println("TÍTULO - " + doc.title());
            images = doc.select("img[src$=.png]");
            parrafos = doc.select("p");
            formularios = doc.select("form");
            inputs = doc.select("input[type]");

            /*------------------------------------------Parseador del HTML------------------------------------------
             * Tomando los objetos de tipo Elements declarado anteriormente, se guardan en listas de elementos todas
             * las etiquetas que tengan presente el cssQuery que se les pasa por parámetro al método select.
             * Adicionalmente, se muestra el título de la página seleccionada.
             * */

            URL oracle = new URL(URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                countLine++;
            in.close();

            /*-------------------------------------------Contador de Líneas-------------------------------------------
             * Para contar las líneas, se utilizan métodos alternativos al JSOUP, utilizando la librería nativa de
             * net. Aquí, se lee el HTML como archivo y se cuenta la cantidad de líneas que tiene con un ciclo while.
             * */

        } catch (IOException ex) {
            System.out.println("        <<Error al entrar al URL!>>");
            System.out.println( "Error: " + ex);
        }

        /*--------------------------------------------------Try Catch--------------------------------------------------
         * Se ha utilizado el try catch para evitar que el programa explote al dar un error al leer el archivo, lo
         * que puede ocurrir facilmente si el URL no es válido.
         * */

        System.out.println("======================================================================================\n");

        System.out.println("    a) Cantidad de líneas:   " + countLine);
        System.out.println("    b) Cantidad de párrafos: " + parrafos.size());
        System.out.println("    c) Cantidad de imágenes: " + images.size());
        System.out.println("    d) Cantidad de formularios: " + formularios.size());

        /*--------------------------------------------Mostrar los Contadores-------------------------------------------
         * Para contar las líneas para la parte "a", se utiliza el mismo contador visto anteriormente, mientras que
         * para los puntos del "b" hasta el "d", se utilizan los métodos size de los Elements obtenidos con las
         * etiquetas correspondientes.
         * */

        System.out.println("    e) Inputs");

        if(inputs.isEmpty())
            System.out.println("        - No se han encontrado Inputs.");
        else
            for(Element input : inputs)
                System.out.println("        -Tipo: " + input.attr("type"));

        /*------------------------------------------Mostrar los tipos de input------------------------------------------
         * Para mostrar los tipos de las entradas, se utiliza la lista de elementos con etiquetas "input" halladas
         * anteriormente y se obtiene su atributo "type" por el métodos attr al cual se le pasa el nombre del
         * atributo que se quiera obtener.
         * */

        System.out.println("    f) Respuesta del servidor al POST: " + respuestaPOST(URL));

        /*--------------------------------------------Parámetros y Headers--------------------------------------------
         * Se ejecuta la función respuestaPOST para obtener la respuesta del servidor retornada como un string.
         * */

        System.out.println("\n======================================================================================");

    }


    private static String respuestaPOST(String URL){

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse respuesta;
        String resuestaReturn;

        /*-------------------------------------------Variables de la función-------------------------------------------
         * En esta sección, declaramos las variables que se usarán al momento de hacer el POST, incluyendo el String
         * de respuesta que se planea retornar.
         * */

        HttpPost httpPost = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList <>();
        nvps.add(new BasicNameValuePair("Asignatura", "Práctica 1"));
        httpPost.setHeader("Matricula", "20140452");

        /*--------------------------------------------Parámetros y Headers--------------------------------------------
         * Se le pasa el atributo de asignatura con su valor al nvps y con el setHeader, se le añade el Header de
         * matrícula con la matrícula personal como valor.
         * */

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            respuesta = httpclient.execute(httpPost);

            /*--------------------------------------------POST y Respuesta--------------------------------------------
             * Con los parámetros y los headers puestos, se ejecuta el POST, lo cual retorna la respuesta del
             * servidor que se guardará en la variable respuesta.
             * */

        }
        catch (IOException ex)
        {
            System.out.println("======================================================================================");
            System.out.println("        <<Error al hacer el post!>>");
            System.out.println( "Error: " + ex);
            System.out.println("======================================================================================");
            return "Error al hacer el post!";
        }


        try {
            resuestaReturn = respuesta.getStatusLine().toString();
            HttpEntity entity = respuesta.getEntity();
            EntityUtils.consume(entity);
            respuesta.close();

            /*-----------------------------------------Cierre de la respuesta-----------------------------------------
             * Se guarda en el String de retorno la respuesta del servidor y se ceirra la respuesta.
             * */

        }catch (IOException ex){
            System.out.println("======================================================================================");
            System.out.println("        <<Error al hacer el post!>>");
            System.out.println("======================================================================================");
            resuestaReturn = "Error al hacer el post!";
        }

        return resuestaReturn;
    }
}
