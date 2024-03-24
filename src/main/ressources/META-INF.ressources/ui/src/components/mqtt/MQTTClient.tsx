import React, { useEffect, useState } from 'react';

const MQTTClient = ({ onCoordinatesReceived, setServerRunning, serverRunning }) => {
    const [isWebSocketOpen, setIsWebSocketOpen] = useState(false);
    
    useEffect(() => {
        if (!isWebSocketOpen) {
            const webSocket = new WebSocket('ws://localhost:8080/game/123');
        

            // webSocket.addEventListener('open', () => {
            //     setIsWebSocketOpen(true);
            //     setServerRunning(true);
            //     console.log('Connected to WebSocket server');
            // });

            webSocket.onopen = () => {
                setIsWebSocketOpen(true);
                setServerRunning(true);
                console.log('Connected to WebSocket server');
            }

            webSocket.onmessage = (event) => {
                console.log('Received message:', event.data);
                //const coordinates = JSON.parse(event.data);
                //onCoordinatesReceived(coordinates);
            }

            // webSocket.addEventListener('message', (event) => {
            //     console.log('Received message:', event.data);
            //     const coordinates = JSON.parse(event.data);
            //     onCoordinatesReceived(coordinates);
            // });

            webSocket.onclose = () => {
                console.log('WebSocket connection closed');
                setServerRunning(false)
            }

            // webSocket.addEventListener('close', () => {
            //     console.log('WebSocket connection closed');
            //     setServerRunning(false)
            // });
            
            webSocket.onerror = (error) => {
                console.error('WebSocket error:', error);
                setServerRunning(false)
            }

            // webSocket.addEventListener('error', (error) => {
            //     console.error('WebSocket error:', error);
            //     setServerRunning(false)
            // });
            
            return () => {
                if (webSocket.readyState === WebSocket.OPEN || webSocket.readyState === WebSocket.CONNECTING) {
                    webSocket.close();
                    setServerRunning(false)
                }
                console.log('Disconnected from WebSocket server');
            };
        }
    }, [isWebSocketOpen, onCoordinatesReceived, setServerRunning]);

    return null; // No need to render anything for the WebSocket client component
};

export default MQTTClient;