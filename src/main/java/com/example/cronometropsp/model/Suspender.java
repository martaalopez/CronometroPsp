package com.example.cronometropsp.model;

public class Suspender {
    private boolean suspender; // Variable booleana que indica si se debe suspender o reanudar el hilo

    // Método getter para obtener el estado actual de la variable suspender
    public boolean getSuspender() {
        return suspender;
    }

    // Método synchronized para establecer el valor de suspender y notificar a los hilos en espera
    public synchronized void setSuspender(boolean b) {
        this.suspender = b; // Asigna el valor recibido a la variable suspender
        notifyAll(); // Notifica a todos los hilos en espera (wait()) en este objeto
    }

    // Método synchronized para poner el hilo en espera mientras suspender sea true
    public synchronized void waitReanudar() throws InterruptedException {
        while (this.suspender) {
            wait(); // Pone el hilo en espera hasta que suspender cambie a false
        }
    }
}

