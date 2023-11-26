package com.example.cronometropsp.model;

public class Suspender {
     private boolean suspender;

   public boolean getSuspender(){
       return suspender;
   }
    public synchronized void setSuspender (boolean b) {
        this.suspender=b;
        notifyAll();
    }
    public synchronized void waitReanudar() throws InterruptedException {
        while(this.suspender) {
            wait();
        }
    }
}
