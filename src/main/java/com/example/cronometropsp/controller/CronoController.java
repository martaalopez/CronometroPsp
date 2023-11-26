package com.example.cronometropsp.controller;


import com.example.cronometropsp.conexion.XmlManager;
import com.example.cronometropsp.model.Cronometro;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class CronoController {


        @FXML
        private ListView<String> tb_vuelta;
        private List<Cronometro> cronometrosRegistrados = new ArrayList<>();


        @FXML
        private Circle gr_pelota;

        @FXML
        private Circle peq_pelota;

        @FXML
        private Button btn_init;

        @FXML
        private Button btn_guardar;

        @FXML
        private Button btn_pause;

        @FXML
        private Button btn_reset;

        @FXML
        private Label label_hora;

        @FXML
        private Label label_minuto;

        @FXML
        private Label label_segundo;

        private Cronometro c = new Cronometro();
        private Thread t;


        private final double centerX = 286.0; // Coordenada X del centro del círculo grande
        private final double centerY = 142.0; // Coordenada Y del centro del círculo grande
        private final double radiusBig = 128.0; // Radio del círculo grande
        private final double radiusSmall = 14.0;


        @FXML
        void initialize() {
                actualizarLabelsTiempo();
                // Establecer la posición inicial del círculo pequeño con respecto al círculo grande
                double initialX = centerX + radiusBig - radiusSmall; // Ajustar las coordenadas iniciales como se desee
                double initialY = centerY - radiusSmall; // Ajustar las coordenadas iniciales como se desee

                peq_pelota.setLayoutX(initialX);
                peq_pelota.setLayoutY(initialY);
                cargarDatosDesdeXML();
        }

        private void actualizarLabelsTiempo() {
                Thread actualizarLabelsThread = new Thread(() -> {
                        while (true) {
                                try {
                                        Thread.sleep(1000);
                                        Platform.runLater(() -> {
                                                label_segundo.setText(String.format("%02d", c.getSegundo()));
                                                label_minuto.setText(String.format("%02d", c.getMinuto()));
                                                label_hora.setText(String.format("%02d", c.getHora()));
                                        });
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                });
                actualizarLabelsThread.setDaemon(true);
                actualizarLabelsThread.start();
        }
        @FXML
        void iniciar(ActionEvent event) {
                c.getSuspender().setSuspender(false);
                t = new Thread(c);
                t.start();

                this.btn_pause.setDisable(false);
                this.btn_init.setDisable(true);
                this.btn_init.setText("CONTANDO");

        }


        @FXML
        void parar(ActionEvent event) {
                if (this.btn_pause.getText().equals("PARAR")) {
                        c.getSuspender().setSuspender(true);
                        this.btn_init.setText("DETENIDO");
                        this.btn_pause.setText("REANUDAR");
                } else {
                        c.getSuspender().setSuspender(false);
                        this.btn_init.setText("CONTANDO");
                        this.btn_pause.setText("PARAR");
                }
        }

        @FXML
        void reiniciar(ActionEvent event) {
                if (t != null && t.isAlive()) {
                        c.getSuspender().setSuspender(true); // Establecer suspender en true
                        t.interrupt();
                        try {
                                t.join();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }

                c.getSuspender().setSuspender(false); // Restablecer suspender en false al reiniciar

                c.setHora(0);
                c.setMinuto(0);
                c.setSegundo(0);

                Platform.runLater(() -> { // Actualizar la interfaz de usuario dentro del hilo de la interfaz de usuario
                        label_segundo.setText("00");
                        label_minuto.setText("00");
                        label_hora.setText("00");

                        this.btn_init.setDisable(false);
                        this.btn_init.setText("INICIAR");
                        this.btn_pause.setText("PARAR");
                        this.btn_pause.setDisable(true);
                });
        }
        @FXML
        void guardarEnXML(ActionEvent event) {
                int horas = c.getHora();
                int minutos = c.getMinuto();
                int segundos = c.getSegundo();

                Cronometro cronometro = new Cronometro();
                cronometro.setHora(horas);
                cronometro.setMinuto(minutos);
                cronometro.setSegundo(segundos);

                cronometrosRegistrados.add(cronometro);

                // Guardar el nuevo cronómetro en el archivo XML
                XmlManager.saveCronometrosListToXml(cronometrosRegistrados);

                // Actualizar la lista tb_vuelta con el nuevo cronómetro guardado
                actualizarListaVuelta(cronometro);
        }

        private void actualizarListaVuelta(Cronometro nuevoCronometro) {
                String textoCronometro = String.format("%02d:%02d:%02d", nuevoCronometro.getHora(), nuevoCronometro.getMinuto(), nuevoCronometro.getSegundo());

                Platform.runLater(() -> {
                        tb_vuelta.getItems().add(textoCronometro);
                });
        }

        private void cargarDatosDesdeXML() {
                // Obtener la lista de cronómetros desde el archivo XML
                List<Cronometro> cronometros = (List<Cronometro>) XmlManager.loadCronometroFromXml();

                // Crear una lista de cadenas para mostrar en el ListView
                List<String> listaCronometros = new ArrayList<>();
                for (Cronometro cronometro : cronometros) {
                        // Crear una representación de cadena para cada cronómetro
                        String textoCronometro = String.format("%02d:%02d:%02d", cronometro.getHora(), cronometro.getMinuto(), cronometro.getSegundo());
                        listaCronometros.add(textoCronometro);
                }

                // Mostrar la lista en el ListView
                tb_vuelta.getItems().addAll(listaCronometros);
        }



}
