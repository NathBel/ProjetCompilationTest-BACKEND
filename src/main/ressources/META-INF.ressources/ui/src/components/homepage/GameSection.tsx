import { Box, Typography, Button } from "@mui/material"
import React, { useEffect, useState } from 'react';
import styles from "../../styles/components/homepage/gameSection.module.scss"
import footballStadium from "../../assets/footballStadium.webp"
import greenCar from "../../assets/greenCar.webp"
import redCar from "../../assets/redCar.webp"
import soccerBall from "../../assets/soccerBall.webp"
import MQTTClient from "../mqtt/MQTTClient.tsx";

const GameSection = (props) => {
    const timeOfGame = 120;
    const [remainingTime, setRemainingTime] = useState(timeOfGame); // 2 minutes in seconds
    const [coordinatesPlayer1, setCoordinatesPlayer1] = useState({ x: 0, y: 0, rotation: 'rotateDown' });
    const [coordinatesPlayer2, setCoordinatesPlayer2] = useState({ x: 0, y: 0, rotation: 'rotateDown' });
    const [coordinatesBall, setCoordinatesBall] = useState({ x: 0, y: 0 });
    const [serverRunning, setServerRunning] = useState<boolean>(false);
    const [isAskToJoin, setIsAskToJoin] = useState(false);
    const [isGameJoin, setIsGameJoin] = useState(false);
    const [twoPlayerConnected, setTwoPlayerConnected] = useState(false);
    const [isGameStarted, setIsGameStarted] = useState(false);
    const [webSocket, setWebSocket] = useState<WebSocket | null>(null);
    // "player1" or "player2" or null
    const [currentPlayer, setCurrentPlayer] = useState<String | null>(null)

    useEffect(() => {
        if(isAskToJoin) {
            const websocket = new WebSocket('ws://localhost:8080/game/123');
            setWebSocket(websocket)
           
            websocket.onopen = () => {
                console.log('WebSocket connection established');
                setServerRunning(true)
            };
    
            websocket.onmessage = (event) => {
                const dataParts = event.data.split(':');
                const messageType = dataParts[0];
                const messageContent = dataParts[1];
            
                if (messageType === "green") { 
                    setCurrentPlayer("player1");
                    props.handleShowAlertMessage('You are the green car! Wait for another player...', 'success');
                } else if (messageType === "red") { 
                    setCurrentPlayer("player2");
                    props.handleShowAlertMessage('You are the red car! You can start the game!', 'success');
                } else if (messageType === "2") {
                    props.handleShowAlertMessage('All players are ready. You can start the game!', 'success');
                    setTwoPlayerConnected(true);
                } else if (event.data === "game:started") {
                    props.handleShowAlertMessage('Game started!', 'success');
                    setIsGameStarted(true);
                } else if (messageType === "player1") {
                    switch (messageContent) {
                        case "rotateUp":
                        case "rotateLeft":
                        case "rotateRight":
                        case "rotateDown":
                            console.log('Rotation player1: ', messageContent);
                            setCoordinatesPlayer1(prevState => ({
                                ...prevState,
                                rotation: messageContent
                            }));    
                            break;
                        default:
                            console.log('Coordinates player1: ', messageContent);
                            const [coordinateX, coordinateY] = messageContent.split(',');
                            setCoordinatesPlayer1(prevState => ({
                                ...prevState,
                                x: coordinateX,
                                y: coordinateY
                            }));
                            break;
                    }
                } else if (messageType === "player2") {
                    switch (messageContent) {
                        case "rotateUp":
                        case "rotateLeft":
                        case "rotateRight":
                        case "rotateDown":
                            console.log('Rotation player2: ', messageContent);
                            setCoordinatesPlayer2(prevState => ({
                                ...prevState,
                                rotation: messageContent
                            }));    
                            break;
                        default:
                            console.log('Coordinates player2: ', messageContent);
                            const [coordinateX, coordinateY] = messageContent.split(',');
                            setCoordinatesPlayer2(prevState => ({
                                ...prevState,
                                x: coordinateX,
                                y: coordinateY
                            }));                            
                            break;
                    }
                } else if (messageType === "ball") {
                    console.log('Coordinates ball: ', messageContent);
                    const [coordinateX, coordinateY] = messageContent.split(',');
                    setCoordinatesBall({ x: coordinateX, y: coordinateY });
                } else if (event.data === "game:stopped") {
                    console.log(event.data);
                    props.handleShowAlertMessage('Game stopped!', 'success');
                    setIsGameStarted(false);
                } else { 
                    console.log("=> Message received: ", event.data);
                } 
            };
            
    
            websocket.onerror = (error) => {
                console.error('WebSocket error:', error);
                setServerRunning(false)
            };
            
            websocket.onclose = () => {
                console.log('WebSocket connection closed');
                setServerRunning(false)
            };

            
            setIsAskToJoin(false)
            
        }
    }, [isAskToJoin])

    useEffect(() => {
        if(serverRunning) {
            setIsGameJoin(true)
        } else {
            setIsGameJoin(false)
            setIsAskToJoin(false)
            setIsGameStarted(false)
        }
    }, [serverRunning])

    // Join the unique game
    const joinGame = () => {
        if (!serverRunning) {
            setIsAskToJoin(true)
        }
    }

    const sendMessageToServer = (message: any) => {
        if(webSocket == null) {
            props.handleShowAlertMessage('The server is not responding', 'error');
            return;
        }
        webSocket.send(message);
        console.log("==============Message sent !=================== : ", message)
    }
    
    const startGame = () => {
        sendMessageToServer("game:start")
    }
    
    const stopGame = () => {
        sendMessageToServer("game:stop")
    }

    useEffect(() => {
        if(currentPlayer == null) {
            console.log("no current player")
        }
        const handleKeyDown = (event) => {
            switch (event.key) {
                case 'ArrowUp':
                case 'ArrowDown':
                case 'ArrowLeft':
                case 'ArrowRight':
                    event.preventDefault(); // Prevent scrolling
                    // move:player1:ArrowUp
                    let message = `move:${currentPlayer}-${event.key}` 
                    console.log("=========== Message to send: ")
                    console.log(message)
                    sendMessageToServer(message)
                    break;
                default:
                    break;
            }
        };

        window.addEventListener('keydown', handleKeyDown);

        return () => {
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, [currentPlayer]);


    return (
        <Box ref={props.refGame} id={styles.boxSection}>
            <Box id={styles.boxInfoGame}>
                <Box id={styles.boxScore}>
                    <Typography variant="h2" color="primary">0</Typography>
                    <Typography variant="h2" color="initial">-</Typography>
                    <Typography variant="h2" color="secondary">0</Typography>
                </Box>
                <Box id={styles.boxTime}>
                    <Typography variant="h2" color="initial">time</Typography>
                </Box>
            </Box>
            <Box id={styles.boxStadium}>   
                <img id={styles.footballStadium} src={footballStadium} alt="football stadium" />
                {isGameStarted && (
                    <>
                        <img 
                            className={`${styles.car} ${coordinatesPlayer1.rotation === 'rotateUp' && styles['rotated-up']} ${coordinatesPlayer1.rotation === 'rotateDown' && styles['rotated-down']} ${coordinatesPlayer1.rotation === 'rotateLeft' && styles['rotated-left']} ${coordinatesPlayer1.rotation === 'rotateRight' && styles['rotated-right']}`} 
                            id={styles.greenCar} 
                            src={greenCar}
                            alt="green car" 
                            style={{ left: `max(8%, min(${coordinatesPlayer1.x}%, 92%))`, bottom: `max(8%, min(${coordinatesPlayer1.y}%, 92%))`}}
                        />
                        <img 
                            className={`${styles.car} ${coordinatesPlayer2.rotation === 'rotateUp' && styles['rotated-up']} ${coordinatesPlayer2.rotation === 'rotateDown' && styles['rotated-down']} ${coordinatesPlayer2.rotation === 'rotateLeft' && styles['rotated-left']} ${coordinatesPlayer2.rotation === 'rotateRight' && styles['rotated-right']}`} 
                            id={styles.redCar}
                            src={redCar} 
                            alt="red car" 
                            style={{ left: `max(8%, min(${coordinatesPlayer2.x}%, 92%))`, bottom: `max(8%, min(${coordinatesPlayer2.y}%, 92%))`}} 
                        />
                        <img id={styles.soccerBall} src={soccerBall} alt="soccer ball" style={{ left: `max(8%, min(${coordinatesBall.x}%, 92%))`, bottom: `max(8%, min(${coordinatesBall.y}%, 92%))`}} />
                    </>
                )}
                {!isGameJoin && (
                    <Button id={styles.buttonJoinGame} variant="contained" color="secondary" onClick={joinGame}>Join the game !</Button>
                )}

            </Box>
            {(isGameJoin && !isGameStarted) && (
                <Box id={styles.boxActionGame}>
                    <Button id={styles.buttonStopGame} variant="contained" color="secondary" onClick={startGame} disabled={!twoPlayerConnected}>Start the game</Button>
                </Box>
            )}
            {isGameStarted && (
                <Box id={styles.boxActionGame}>
                    <Button id={styles.buttonStopGame} variant="contained" color="secondary" onClick={stopGame}>Stop Game</Button>
                </Box>
            )}
        </Box>
    )
}

export default GameSection;