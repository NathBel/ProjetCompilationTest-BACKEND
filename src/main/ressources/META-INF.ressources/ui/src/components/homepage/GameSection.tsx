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
    const [sizePlayer1, setSizePlayer1] = useState({ width: 0, height: 0 });
    const [sizePlayer2, setSizePlayer2] = useState({ width: 0, height: 0 });
    const [coordinatesBall, setCoordinatesBall] = useState({ x: 0, y: 0 });
    const [serverRunning, setServerRunning] = useState<boolean>(false);
    const [isAskToJoin, setIsAskToJoin] = useState(false);
    const [isGameJoin, setIsGameJoin] = useState(false);
    const [twoPlayerConnected, setTwoPlayerConnected] = useState(false);
    const [isGameStarted, setIsGameStarted] = useState(false);
    const [webSocket, setWebSocket] = useState<WebSocket | null>(null);
    // "player1" or "player2" or null
    const [currentPlayer, setCurrentPlayer] = useState<String | null>(null)

    const [topPointPlayer1, setTopPointPlayer1] = useState({ x: 0, y: 0 });
    const [bottomPointPlayer1, setBottomPointPlayer1] = useState({ x: 0, y: 0 });
    const [leftPointPlayer1, setLeftPointPlayer1] = useState({ x: 0, y: 0 });
    const [rightPointPlayer1, setRightPointPlayer1] = useState({ x: 0, y: 0 });

    const [topPointPlayer2, setTopPointPlayer2] = useState({ x: 0, y: 0 });
    const [bottomPointPlayer2, setBottomPointPlayer2] = useState({ x: 0, y: 0 });
    const [leftPointPlayer2, setLeftPointPlayer2] = useState({ x: 0, y: 0 });
    const [rightPointPlayer2, setRightPointPlayer2] = useState({ x: 0, y: 0 });

    const updatePointsPlayer1 = () => {
        const halfWidth = sizePlayer1.width / 2;
        const halfHeight = sizePlayer1.height / 2;
      
        setTopPointPlayer1({ x: coordinatesPlayer1.x, y: coordinatesPlayer1.y + halfHeight });
        setBottomPointPlayer1({ x: coordinatesPlayer1.x, y: coordinatesPlayer1.y - halfHeight });
        setLeftPointPlayer1({ x: coordinatesPlayer1.x - halfWidth, y: coordinatesPlayer1.y});
        setRightPointPlayer1({ x: coordinatesPlayer1.x + halfWidth, y: coordinatesPlayer1.y});
    };

    useEffect(() => {
        updatePointsPlayer1();
    }, [coordinatesPlayer1, sizePlayer1]);


    const updatePointsPlayer2 = () => {
        const halfWidth = sizePlayer2.width / 2;
        const halfHeight = sizePlayer2.height / 2;
      
        setTopPointPlayer2({ x: coordinatesPlayer2.x, y: coordinatesPlayer2.y + halfHeight });
        setBottomPointPlayer2({ x: coordinatesPlayer2.x, y: coordinatesPlayer2.y - halfHeight });
        setLeftPointPlayer2({ x: coordinatesPlayer2.x - halfWidth, y: coordinatesPlayer2.y});
        setRightPointPlayer2({ x: coordinatesPlayer2.x + halfWidth, y: coordinatesPlayer2.y});
    };

    useEffect(() => {
        updatePointsPlayer2();
    }, [coordinatesPlayer2, sizePlayer2]);

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
                            setCoordinatesPlayer1(prevState => ({
                                ...prevState,
                                rotation: messageContent
                            }));    
                            break;
                        default:
                            const [coordinateX, coordinateY] = messageContent.split(',').map(value => parseFloat(value));
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
                            setCoordinatesPlayer2(prevState => ({
                                ...prevState,
                                rotation: messageContent
                            }));    
                            break;
                        default:
                            const [coordinateX, coordinateY] = messageContent.split(',').map(value => parseFloat(value));
                            setCoordinatesPlayer2(prevState => ({
                                ...prevState,
                                x: coordinateX,
                                y: coordinateY
                            }));                            
                            break;
                    }
                } else if (messageType === "ball") {
                    console.log('Coordinates ball: ', messageContent);
                    const [coordinateX, coordinateY] = messageContent.split(',').map(value => parseFloat(value));
                    setCoordinatesBall({ x: coordinateX, y: coordinateY });
                } else if (event.data === "game:stopped") {
                    console.log(event.data);
                    props.handleShowAlertMessage('Game stopped!', 'success');
                    setIsGameStarted(false);
                } else if (messageType === "player1,width") {
                    // console.log('Width player1: ', messageContent);
                    const [width, height] = messageContent.split(',').map(value => parseFloat(value));
                    setSizePlayer1({ width: width, height: height });
                } else if (messageType === "player2,width") {
                    // console.log('Width player2: ', messageContent);
                    const [width, height] = messageContent.split(',').map(value => parseFloat(value));
                    setSizePlayer2({ width: width, height: height });
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
                    let message = `move:${currentPlayer}-${event.key}` 
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
                        {/* Green car */}
                        <img 
                            className={`${styles.car} ${coordinatesPlayer1.rotation === 'rotateUp' && styles['rotated-up']} ${coordinatesPlayer1.rotation === 'rotateDown' && styles['rotated-down']} ${coordinatesPlayer1.rotation === 'rotateLeft' && styles['rotated-left']} ${coordinatesPlayer1.rotation === 'rotateRight' && styles['rotated-right']}`} 
                            src={greenCar}
                            alt="green car" 
                            style={{ left: `${coordinatesPlayer1.x}%`, bottom: `calc(${coordinatesPlayer1.y}% - 11%)`}}
                        />
                        <div
                            className={styles.pointCoordinate}
                            style={{ left: `${coordinatesPlayer1.x}%`, bottom: `${coordinatesPlayer1.y}%`}}
                        />
                        <div
                            className={styles.pointTop}
                            style={{ left: `${topPointPlayer1.x}%`, bottom: `${topPointPlayer1.y}%`}}
                        />
                        <div
                            className={styles.pointBottom}
                            style={{ left: `${bottomPointPlayer1.x}%`, bottom: `${bottomPointPlayer1.y}%`}}
                        />
                        <div
                            className={styles.pointLeft}
                            style={{ left: `${leftPointPlayer1.x}%`, bottom: `${leftPointPlayer1.y}%`}}
                        />
                        <div
                            className={styles.pointRight}
                            style={{ left: `${rightPointPlayer1.x}%`, bottom: `${rightPointPlayer1.y}%` }}
                        />

                        {/* Red car */}
                        <img 
                            className={`${styles.car} ${coordinatesPlayer2.rotation === 'rotateUp' && styles['rotated-up']} ${coordinatesPlayer2.rotation === 'rotateDown' && styles['rotated-down']} ${coordinatesPlayer2.rotation === 'rotateLeft' && styles['rotated-left']} ${coordinatesPlayer2.rotation === 'rotateRight' && styles['rotated-right']}`} 
                            src={redCar} 
                            alt="red car" 
                            style={{ left: `${coordinatesPlayer2.x}%`, bottom: `calc(${coordinatesPlayer2.y}% - 11%)`}}
                        />
                        <div
                            className={styles.pointCoordinate}
                            style={{ left: `${coordinatesPlayer2.x}%`, bottom: `${coordinatesPlayer2.y}%`}}
                        />
                        <div
                            className={styles.pointTop}
                            style={{ left: `${topPointPlayer2.x}%`, bottom: `${topPointPlayer2.y}%`}}
                        />
                        <div
                            className={styles.pointBottom}
                            style={{ left: `${bottomPointPlayer2.x}%`, bottom: `${bottomPointPlayer2.y}%`}}
                        />
                        <div
                            className={styles.pointLeft}
                            style={{ left: `${leftPointPlayer2.x}%`, bottom: `${leftPointPlayer2.y}%`}}
                        />
                        <div
                            className={styles.pointRight}
                            style={{ left: `${rightPointPlayer2.x}%`, bottom: `${rightPointPlayer2.y}%` }}
                        />

                        {/* Ball */}
                        <img id={styles.soccerBall} src={soccerBall} alt="soccer ball" style={{ left: `calc(${coordinatesBall.x}% + 0.3%)`, bottom: `calc(${coordinatesBall.y}% - 4%)`}} />
                        <div
                            className={styles.pointBall}
                            style={{ left: `${coordinatesBall.x}%`, bottom: `${coordinatesBall.y}%`}}
                        />
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