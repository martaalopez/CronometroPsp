package com.example.cronometropsp.controller;

import com.example.cronometropsp.conexion.XmlManager;
import com.example.cronometropsp.model.Cronometro;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
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
        private Timeline timeline;

        private final double centerX = 286.0; // Coordenada X del centro del círculo grande
        private final double centerY = 142.0; // Coordenada Y del centro del círculo grande
        private final double radiusBig = 128.0; // Radio del círculo grande
        private final double radiusSmall = 14.0;

        // Método inicial al cargar el controlador
        @FXML
        void initialize() {
                // Inicia la actualización de las etiquetas de tiempo
                actualizarLabelsTiempo();

                // Establece la posición inicial del círculo pequeño en el centro del círculo grande
                double centerXBig = 288;
                double centerYBig = 270;
                peq_pelota.setLayoutX(centerXBig);
                peq_pelota.setLayoutY(centerYBig);

                // Carga los datos desde el archivo XML para mostrar en la lista
                cargarDatosDesdeXML();
        }

        // Método para actualizar las etiquetas de tiempo
        private void actualizarLabelsTiempo() {
                Thread actualizarLabelsThread = new Thread(() -> {
                        while (true) {
                                try {
                                        Thread.sleep(1000);
                                        Platform.runLater(() -> {
                                                // Actualiza las etiquetas de tiempo en la interfaz gráfica
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

        // Método para iniciar el cronómetro y la animación
        @FXML
        void iniciar(ActionEvent event) {
                c.getSuspender().setSuspender(false);
                t = new Thread(c);
                t.start();

                // Habilita y deshabilita los botones para iniciar el cronómetro
                this.btn_pause.setDisable(false);
                this.btn_init.setDisable(true);
                this.btn_init.setText("CONTANDO");

                // Obtiene las posiciones del círculo grande
                double centerXBig = gr_pelota.getLayoutX();
                double centerYBig = gr_pelota.getLayoutY();

                double speed = 1; // Velocidad de rotación
                final double[] angle = {100}; // Inicializa el ángulo en 180 grados para empezar en el lado izquierdo

                // Ajusta el valor para la posición de la pelota respecto al borde del círculo grande
                double adjustedRadiusBig = radiusBig; // Por ejemplo, resta 10 unidades al radio del círculo grande

                if (timeline == null) {
                        // Crea una nueva animación si no existe una
                        timeline = new Timeline(
                                new KeyFrame(Duration.seconds(1.0 / 60.0), e -> {
                                        // Calcula las nuevas posiciones para la animación
                                        double x = centerXBig + adjustedRadiusBig * Math.cos(Math.toRadians(angle[0]));
                                        double y = centerYBig + adjustedRadiusBig * Math.sin(Math.toRadians(angle[0]));

                                        // Establece las posiciones para la pelota pequeña
                                        peq_pelota.setLayoutX(x);
                                        peq_pelota.setLayoutY(y);

                                        angle[0] -= speed; // Actualiza el ángulo para la rotación hacia la izquierda
                                })
                        );
                        timeline.setCycleCount(Animation.INDEFINITE);
                        timeline.play();
                } else {
                        // Reanuda la animación desde el principio si existe
                        timeline.playFromStart();
                }
        }

        // Método para detener o reanudar el cronómetro y la animación
        @FXML
        void parar(ActionEvent event) {
                if (this.btn_pause.getText().equals("PARAR")) {
                        // Detiene el cronómetro y la animación
                        c.getSuspender().setSuspender(true);
                        this.btn_init.setText("DETENIDO");
                        this.btn_pause.setText("REANUDAR");

                        if (timeline != null) {
                                timeline.pause(); // Pausa la animación
                        }
                } else {
                        // Reanuda el cronómetro y la animación
                        c.getSuspender().setSuspender(false);
                        this.btn_init.setText("CONTANDO");
                        this.btn_pause.setText("PARAR");

                        if (timeline != null) {
                                timeline.play(); // Reanuda la animación
                        }
                }
        }

        // Método para reiniciar el cronómetro y la animación
        @FXML
        void reiniciar(ActionEvent event) {
                if (t != null && t.isAlive()) {
                        // Detiene el cronómetro si está en ejecución
                        c.getSuspender().setSuspender(true);
                        t.interrupt();
                        try {
                                t.join();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }

                // Reinicia el cronómetro y restablece los valores a cero
                c.getSuspender().setSuspender(false);
                c.setHora(0);
                c.setMinuto(0);
                c.setSegundo(0);

                Platform.runLater(() -> {
                        // Actualiza la interfaz de usuario
                        label_segundo.setText("00");
                        label_minuto.setText("00");
                        label_hora.setText("00");

                        // Habilita o deshabilita los botones correspondientes
                        this.btn_init.setDisable(false);
                        this.btn_init.setText("INICIAR");
                        this.btn_pause.setText("PARAR");
                        this.btn_pause.setDisable(true);

                        // Mueve la pelota a la nueva posición
                        double centerXBig = 288;
                        double centerYBig = 270;
                        peq_pelota.setLayoutX(centerXBig);
                        peq_pelota.setLayoutY(centerYBig);

                        // Detiene la animación y crea una pausa antes de reiniciarla
                        if (timeline != null) {
                                timeline.stop();
                        }

                        // Crea una pausa de dos segundos antes de iniciar la animación nuevamente
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(e -> {
                                if (timeline != null) {
                                        timeline.play(); // Inicia la animación después de la pausa de dos segundos
                                }
                        });
                        pause.play();
                });
        }

        // Método para guardar el cronómetro en un archivo XML
        @FXML
        void guardarEnXML(ActionEvent event) {
                // Obtiene el tiempo actual del cronómetro
                int horas = c.getHora();
                int minutos = c.getMinuto();
                int segundos = c.getSegundo();

                // Crea un nuevo cronómetro
                Cronometro cronometro = new Cronometro();
                cronometro.setHora(horas);
                cronometro.setMinuto(minutos);
                cronometro.setSegundo(segundos);

                cronometrosRegistrados.add(cronometro); // Agrega el cronómetro a la lista

                // Guarda el cronómetro en un archivo XML
                XmlManager.saveCronometrosListToXml(cronometrosRegistrados);

                // Actualiza la lista mostrada en la interfaz de usuario
                actualizarListaVuelta(cronometro);
        }

        // Método para actualizar la lista de vuelta con el nuevo cronómetro
        private void actualizarListaVuelta(Cronometro nuevoCronometro) {
                String textoCronometro = String.format("%02d:%02d:%02d", nuevoCronometro.getHora(), nuevoCronometro.getMinuto(), nuevoCronometro.getSegundo());

                Platform.runLater(() -> {
                        tb_vuelta.getItems().add(textoCronometro); // Agrega el nuevo cronómetro a la lista en la interfaz
                });
        }

        // Método para cargar los datos desde un archivo XML
        private void cargarDatosDesdeXML() {
                // Obtiene la lista de cronómetros desde el archivo XML
                List<Cronometro> cronometros = XmlManager.loadCronometroFromXml();

                // Crea una lista de cadenas para mostrar en el ListView
                List<String> listaCronometros = new ArrayList<>();
                for (Cronometro cronometro : cronometros) {
                        // Convierte cada cronómetro a una cadena para su visualización
                        String textoCronometro = String.format("%02d:%02d:%02d", cronometro.getHora(), cronometro.getMinuto(), cronometro.getSegundo());
                        listaCronometros.add(textoCronometro);
                }

                // Muestra la lista en el ListView de la interfaz
                tb_vuelta.getItems().addAll(listaCronometros);
        }
}

