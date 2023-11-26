package com.example.cronometropsp.conexion;

import com.example.cronometropsp.model.Cronometro;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cronometros")
public class CronometroListWrapper {

    private List<Cronometro> cronometros;

    @XmlElement(name = "cronometro")
    public List<Cronometro> getCronometros() {
        return cronometros;
    }

    public void setCronometros(List<Cronometro> cronometros) {
        this.cronometros = cronometros;
    }
}

