package com.example.cronometropsp.conexion;

import com.example.cronometropsp.model.Cronometro;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlManager {

    // Ruta del archivo XML
    private static final String XML_FILE_PATH = "captura.xml";

    // Método para guardar la lista de Cronometro en un archivo XML
    public static void saveCronometrosListToXml(List<Cronometro> cronometros) {
        try {
            // Crear un contexto JAXB para la clase CronometroListWrapper
            JAXBContext context = JAXBContext.newInstance(CronometroListWrapper.class);

            // Crear un objeto marshaller para convertir los objetos Java a XML
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Crear un archivo XML
            File file = new File(XML_FILE_PATH);

            // Crear un wrapper con la lista de cronómetros
            CronometroListWrapper wrapper = new CronometroListWrapper();
            wrapper.setCronometros(cronometros);

            // Realizar el marshalling de la lista de Cronometro al archivo XML
            marshaller.marshal(wrapper, file);

            System.out.println("Se han guardado los cronómetros en 'captura.xml'.");

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar la lista de Cronometro desde un archivo XML
    public static List<Cronometro> loadCronometroFromXml() {
        try {
            // Crear un contexto JAXB para la clase CronometroListWrapper
            JAXBContext context = JAXBContext.newInstance(CronometroListWrapper.class);

            // Crear un objeto unmarshaller para convertir el XML a objetos Java
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Obtener el archivo XML
            File file = new File(XML_FILE_PATH);

            // Si el archivo existe, realizar unmarshalling y devolver la lista de cronómetros
            if (file.exists()) {
                CronometroListWrapper wrapper = (CronometroListWrapper) unmarshaller.unmarshal(file);
                return wrapper.getCronometros();
            }
            // Si el archivo no existe, devolver una lista vacía
            return new ArrayList<>();

        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

