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

        private static final String XML_FILE_PATH = "captura.xml";

        public static void saveCronometrosListToXml(List<Cronometro> cronometros) {
            try {
                JAXBContext context = JAXBContext.newInstance(CronometroListWrapper.class);

                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                File file = new File(XML_FILE_PATH);

                CronometroListWrapper wrapper = new CronometroListWrapper();
                wrapper.setCronometros(cronometros);

                // Marshalling de la lista de Cronometro al archivo XML
                marshaller.marshal(wrapper, file);

                System.out.println("Se han guardado los cronómetros en 'captura.xml'.");

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

    public static List<Cronometro> loadCronometroFromXml() {
        try {
            JAXBContext context = JAXBContext.newInstance(CronometroListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            File file = new File(XML_FILE_PATH);
            if (file.exists()) {
                CronometroListWrapper wrapper = (CronometroListWrapper) unmarshaller.unmarshal(file);
                return wrapper.getCronometros();
            }
            return new ArrayList<>(); // Retorna una lista vacía si el archivo no existe
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retorna una lista vacía si ocurre un error en la deserialización
        }
    }

}

