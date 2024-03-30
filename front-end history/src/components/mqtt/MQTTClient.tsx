import React, { useEffect, useState } from 'react';

const MQTTClient = ({ onCoordinatesReceived, setServerRunning, serverRunning }) => {
    const [ws, setWs] = useState<WebSocket | null>(null);

    useEffect(() => {
        // WebSocket settings
        const wsUrl = 'wss://mqtt.eclipseprojects.io:443/mqtt'; // WebSocket URL
        const coordinatesTopic = 'car/coordinates';

        // Connect to WebSocket server
        const webSocket = new WebSocket(wsUrl);

        // Set WebSocket client
        setWs(webSocket);

        // WebSocket event listeners
        webSocket.addEventListener('open', () => {
            console.log('Connected to WebSocket server======================================');
        });

        webSocket.onmessage = (event) => {
            console.log('Received message:', event.data);
            const coordinates = JSON.parse(event.data);
        }

        webSocket.addEventListener('message', (event) => {
            console.log("*********************************")
            console.log('Received message:', event.data);
            // Process the received message here
            const coordinates = JSON.parse(event.data);
            onCoordinatesReceived(coordinates);
        });

        webSocket.addEventListener('close', () => {
            console.log('WebSocket connection closed');
        });

        webSocket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });

        // Clean up on component unmount
        return () => {
            if (webSocket.readyState === WebSocket.OPEN || webSocket.readyState === WebSocket.CONNECTING) {
                webSocket.close();
            }
            console.log('Disconnected from WebSocket server');
        };
    }, [onCoordinatesReceived]);

    return null; // No need to render anything for the WebSocket client component
};

export default MQTTClient;