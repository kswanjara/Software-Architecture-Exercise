package DataGenerator;

import common.ClientCommunicationInterface;
import common.ServerCommunicationInterface;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class DataGenerator implements Runnable {

    private static Properties props;

    static ClientCommunicationInterface primaryRef;
    static ClientCommunicationInterface backupRef;

    static boolean isPrimaryAvailable = true;

    public static void main(String[] args) {
        loadProperties();

        String primaryProcess = props.getProperty("primary.process.reference");
        String backupProcess = props.getProperty("backup.process.reference");

        try {
            Registry registry = LocateRegistry.getRegistry(props.getProperty("vehicle.app.ip"), Integer.parseInt(props.getProperty("vehicle.app.port1")));
            primaryRef = (ClientCommunicationInterface) registry.lookup(primaryProcess);

            Registry registry1 = LocateRegistry.getRegistry(props.getProperty("vehicle.app.ip"), Integer.parseInt(props.getProperty("vehicle.app.port2")));
            backupRef = (ClientCommunicationInterface) registry1.lookup(backupProcess);

            while (true) {
                Thread t = new Thread(new DataGenerator());
                t.start();
            }

        } catch (ConnectException e) {
            System.out.println("Processes not available is not available!");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads the properties of application.
     */
    private static void loadProperties() {
        try {
            InputStream is;
            is = DataGenerator.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean getCoordinates() throws IOException {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));

        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));

        if (isPrimaryAvailable) {
            try {
                primaryRef.collectData(latitude, longitude);
            } catch (Exception e) {
                isPrimaryAvailable = false;
                System.out.println("No Primary process available!");
            }
        }
        try {
            backupRef.collectData(latitude, longitude);
        } catch (Exception e) {
            System.out.println("No backup process available!");
        }

        return true;
    }

    @Override
    public void run() {
        try {
            getCoordinates();
            Thread.sleep(2000);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
