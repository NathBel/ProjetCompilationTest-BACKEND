import React, { useEffect, useState } from 'react';
import { Box, Typography, Button } from "@mui/material";


const TestPage = () => {
    const ws = new WebSocket('ws://localhost:8080/game/123');

    // Function to send a message with your custom topic
    const sendMessage = () => {
        ws.send('Hello, server!');
    };

    return (
        <Box>
            <Button onClick={sendMessage} variant="contained" color="primary">Send Message</Button>
        </Box>
    );
};


export default TestPage;
