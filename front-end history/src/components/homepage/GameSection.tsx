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
    const [isGameStarted, setIsGameStarted] = useState(false);
    const [remainingTime, setRemainingTime] = useState(timeOfGame); // 2 minutes in seconds
    const [coordinates, setCoordinates] = useState({ x: 0, y: 0 });
    const [serverRunning, setServerRunning] = useState<boolean>(false);

    useEffect(() => {
        let countdownInterval;

        if (isGameStarted) {
            countdownInterval = setInterval(() => {
                setRemainingTime((prevTime) => {
                    if (prevTime > 0) {
                        return prevTime - 1;
                    } else {
                        clearInterval(countdownInterval);
                        setIsGameStarted(false);
                        return 0;
                    }
                });
            }, 1000);
        } else {
            setRemainingTime(timeOfGame); // Reset the timer when stopping the game
            clearInterval(countdownInterval);
        }

        return () => clearInterval(countdownInterval);
    }, [isGameStarted]);

    const startGame = () => {
        if (!serverRunning) {
            props.handleShowAlertMessage('The server is not responding', 'error');
            console.log('The server is not responding');
        }
        setIsGameStarted(true);
    }

    const stopGame = () => {
        setIsGameStarted(false);
    }

    const formatTime = (seconds: number) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
    };

    const handleCoordinatesReceived = (newCoordinates: any) => {
        setCoordinates(newCoordinates);
    };

    useEffect(() => {
        const handleKeyDown = (event) => {
            switch (event.key) {
                case 'ArrowUp':
                case 'ArrowDown':
                case 'ArrowLeft':
                case 'ArrowRight':
                    event.preventDefault(); // Prevent scrolling
                    console.log(`Key pressed: ${event.key}`);
                    break;
                default:
                    break;
            }
        };

        window.addEventListener('keydown', handleKeyDown);

        return () => {
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, []);


    return (
        <Box ref={props.refGame} id={styles.boxSection}>
            <MQTTClient onCoordinatesReceived={handleCoordinatesReceived} setServerRunning={setServerRunning} serverRunning={serverRunning} />
            <Box id={styles.boxInfoGame}>
                <Box id={styles.boxScore}>
                    <Typography variant="h2" color="primary">0</Typography>
                    <Typography variant="h2" color="initial">-</Typography>
                    <Typography variant="h2" color="secondary">0</Typography>
                </Box>
                <Box id={styles.boxTime}>
                    <Typography variant="h2" color="initial">{formatTime(remainingTime)}</Typography>
                </Box>
            </Box>
            <Box id={styles.boxStadium}>   
                <img id={styles.footballStadium} src={footballStadium} alt="football stadium" />
                <img className={styles.car} id={styles.greenCar} src={greenCar} alt="green car" style={{ left: `${coordinates.x}px`, top: `${coordinates.y}px`}} />
                <img className={styles.car} id={styles.redCar} src={redCar} alt="red car" />
                {isGameStarted ? (
                    <img id={styles.soccerBall} src={soccerBall} alt="soccer ball" />
                ) : (
                    <Button id={styles.buttonStartGame} variant="contained" color="secondary" onClick={startGame}>Start Game !</Button>
                )}
            </Box>
            {isGameStarted && (
                <Box id={styles.boxActionGame}>
                    <Button id={styles.buttonStopGame} variant="contained" color="secondary" onClick={stopGame}>Stop Game</Button>
                </Box>
            )}
        </Box>
    )
}

export default GameSection;