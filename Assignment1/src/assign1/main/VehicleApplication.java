package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;

public class VehicleApplication {

    private static CommunicationInterface serverRef;
    private static Timer timer_heartbeat = new Timer();

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 8888);
        serverRef = (CommunicationInterface) registry.lookup("ServerReference");

        timer_heartbeat.schedule(new Heartbeat(serverRef), 0, 400);

        boolean validCoordinates = true;
        while (validCoordinates) {
            if (!getCoordinates()) {
                validCoordinates = false;
                timer_heartbeat.cancel();
            }
        }
    }

    public static boolean getCoordinates() {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));

        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));

        if (latitude > 89.5 && longitude < 0.5) {
            System.out.println("Error in critical process");
            return false;
        }
        
        return true;
    }
}