package assign1.main;

import java.util.Random;
import java.util.TimerTask;
import java.util.Date;

public class VehicleApp {
    public double latitude;
    public double longitude;
    public Receiver receiver;

    public VehicleApp(Receiver receiver) {
        this.receiver = receiver;
    }

    class HeartBeatSender extends TimerTask {

        /*
        * Scheduled task to send heartbeat every 5 seconds,
        * this is called from ApplicationStarter
        */
        public void run(){
            Random random_number = new Random();
            int num = random_number.nextInt(50) + 1;

            if(num < 35){
                VehicleApp.this.receiver.receiveHeartbeat(new Date());
            }
        }
    }

    public string getCoordinates() {
        return "43.0846, 77.6743";
    }

    public void sendCoordinates() {

    }

}