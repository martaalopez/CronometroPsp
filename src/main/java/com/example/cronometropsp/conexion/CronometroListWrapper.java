package com.example.cronometropsp.conexion;

import com.example.cronometropsp.model.Cronometro;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cronometros") // Define que esta clase representa la raíz del elemento XML
public class CronometroListWrapper {

    private List<Cronometro> cronometros; // Lista de objetos de tipo Cronometro

    @XmlElement(name = "cronometro") // Define que cada elemento de la lista se representará como "cronometro" en XML
    public List<Cronometro> getCronometros() {
        return cronometros; // Obtiene la lista de cronómetros
    }

    public void setCronometros(List<Cronometro> cronometros) {
        this.cronometros = cronometros; // Establece la lista de cronómetros
    }
}

