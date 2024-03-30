import React, { useEffect, useState } from 'react';
import { Box, Typography, Button } from "@mui/material";

const TestPage = () => {
    const [ws, setWs] = useState<WebSocket | null>(null);
    const [receivedMessage, setReceivedMessage] = useState(null);

    useEffect(() => {
        const wsUrl = 'wss://mqtt.eclipseprojects.io:443/mqtt'; // Replace with your WebSocket server URL

        // Establish WebSocket connection
        const webSocket = new WebSocket(wsUrl);

        // Set up event listeners for WebSocket events
        webSocket.addEventListener('open', () => {
            console.log('Connected to WebSocket server');
        });

        webSocket.addEventListener('message', (event) => {
            console.log('Received message:', event.data);
            setReceivedMessage(event.data); // Set received message state
        });

        webSocket.addEventListener('close', () => {
            console.log('WebSocket connection closed');
        });

        webSocket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });

        // Save WebSocket connection object in state
        setWs(webSocket);

        // Clean up function to close WebSocket connection on component unmount
        return () => {
            webSocket.close();
            console.log('Disconnected from WebSocket server');
        };
    }, []);

    // Function to send a message with your custom topic
    const sendMessage = () => {
        if (ws && ws.readyState === WebSocket.OPEN) {
            const message = 'Your message content here'; // Replace with your message content
            const topic = 'YourTopicHere'; // Replace with your custom topic

            // Construct message object with topic and content
            const messageObject = {
                topic: topic,
                message: message
            };

            // Convert message object to JSON and send it over WebSocket
            ws.send(JSON.stringify(messageObject));
            console.log('Sent message:', messageObject);
        } else {
            console.error('WebSocket connection not open');
        }
    };

    return (
        <Box>
            <Typography variant="body1" color="initial">Received Message: {receivedMessage}</Typography>
            <Button onClick={sendMessage} variant="contained" color="primary">Send Message</Button>
        </Box>
    );
};

export default TestPage;
