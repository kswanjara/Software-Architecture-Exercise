package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import assign1.main.VehicleApp;
import assign1.main.VehicleApp.HeartBeatSender;

public class ApplicationStarter {
    private static CommunicationInterface serverRef;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost" , 8888);
        serverRef = (CommunicationInterface) registry.lookup("ServerReference");

        VehicleApp vapp = new VehicleApp();
        Timer timer = new Timer();
        timer.schedule(vapp.new HeartBeatSender(), 0, 5000);
        
    }
}
