package org.acme;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientManager implements MqttCallback {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String COORDINATES_TOPIC = "coordinates";
    private static final String DIRECTION_TOPIC = "direction";

    private MqttClient client;
    private double latitude;
    private double longitude;

    public MqttClientManager() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(BROKER_URL, MqttClient.generateClientId(), persistence);
        client.setCallback(this);
        client.connect();
        client.subscribe(DIRECTION_TOPIC);
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void publishCoordinates() throws MqttException {
        String message = latitude + "," + longitude;
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(COORDINATES_TOPIC, mqttMessage);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connexion MQTT perdue : " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (topic.equals(DIRECTION_TOPIC)) {
            String direction = new String(message.getPayload());
            System.out.println("Direction reçue : " + direction);
            // Ajoutez ici la logique pour mettre à jour la direction de la voiture
            // en fonction de la commande reçue
            // ...
            // Publiez les nouvelles coordonnées de la voiture
            publishCoordinates();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Livraison de message terminée");
    }

    public void close() throws MqttException {
        client.disconnect();
        client.close();
    }
}

