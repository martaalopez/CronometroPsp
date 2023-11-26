package com.example.cronometropsp.model;

import javafx.application.Platform;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Cronometro")
public class Cronometro  implements Runnable{
    private int hora;
    private int minuto;
    private int segundo;
    private Suspender suspender=new Suspender();

    private String tiempo_hora;
    private String tiempo_minuto;
     private String tiempo_segundo;

    public Cronometro(int hora, int minuto, int segundo, Suspender suspender, String tiempo_hora, String tiempo_minuto, String tiempo_segundo) {
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
        this.suspender = suspender;
        this.tiempo_hora = tiempo_hora;
        this.tiempo_minuto = tiempo_minuto;
        this.tiempo_segundo = tiempo_segundo;
    }
    public Cronometro() {
        this.hora = 0;
        this.minuto = 0;
        this.segundo = 0;
        this.tiempo_hora = "00";
        this.tiempo_minuto = "00";
        this.tiempo_segundo = "00";
        this.suspender.setSuspender(false);
    }
    @XmlElement
    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    @XmlElement
    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    @XmlElement
    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    public Suspender getSuspender() {
        return suspender;
    }

    public void setSuspender(Suspender suspender) {
        this.suspender = suspender;
    }

    @XmlElement
    public String getTiempo_hora() {
        return tiempo_hora;
    }

    public void setTiempo_hora(String tiempo_hora) {
        this.tiempo_hora = tiempo_hora;
    }

    @XmlElement
    public String getTiempo_minuto() {
        return tiempo_minuto;
    }

    public void setTiempo_minuto(String tiempo_minuto) {
        this.tiempo_minuto = tiempo_minuto;
    }

    @XmlElement
    public String getTiempo_segundo() {
        return tiempo_segundo;
    }

    public void setTiempo_segundo(String tiempo_segundo) {
        this.tiempo_segundo = tiempo_segundo;
    }
    @Override
    public void run() {
        while (!this.suspender.getSuspender()) {
            Platform.runLater(() -> {
                segundo++;
                if (segundo < 10) {
                    tiempo_segundo = "0" + segundo;
                } else {
                    tiempo_segundo = String.valueOf(segundo);
                }
            });

            if (segundo == 59) {
                Platform.runLater(() -> {
                    minuto++;
                    if (minuto < 10) {
                        tiempo_minuto = "0" + minuto;
                    } else {
                        tiempo_minuto = String.valueOf(minuto);
                    }
                    segundo = 0;
                    tiempo_segundo = segundo + "0";
                });

                if (minuto == 59) {
                    Platform.runLater(() -> {
                        minuto = 0;
                        tiempo_minuto = String.valueOf(minuto);
                        hora++;
                        tiempo_hora = String.valueOf(hora);
                    });

                    if (hora == 24) {
                        Platform.runLater(() -> {
                            hora = 0;
                            tiempo_hora = String.valueOf(hora);
                        });
                    }
                }
            }

            try {
                Thread.sleep(1000);
                this.suspender.waitReanudar();
            } catch (InterruptedException e) {
                Platform.runLater(() -> {
                    segundo = 0;
                    tiempo_segundo = segundo + "0";

                    minuto = 0;
                    tiempo_minuto = String.valueOf(minuto);

                    hora = 0;
                    tiempo_hora = String.valueOf(hora);
                });
            }
        }
    }
}
