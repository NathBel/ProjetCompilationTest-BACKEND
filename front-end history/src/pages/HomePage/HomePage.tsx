import { scrollIntoView } from "seamless-scroll-polyfill";
import { useLocation } from "react-router-dom";
import { useEffect, useRef } from "react";
import AlertComponent from "../../components/general/Alert.tsx";
import useAlert from "../../hooks/useAlerts.tsx";
import React from 'react';
import WelcomeBanner from '../../components/homepage/WelcomeBanner.tsx';
import GameSection from '../../components/homepage/GameSection.tsx';

const HomePage = () => {

    // Display alert message from location state
    const location = useLocation();
    const { alertMessage, handleShowAlertMessage } = useAlert();
    useEffect(() => {
        if (location?.state?.message !== undefined) {
            handleShowAlertMessage(location.state.message, location.state.severity);
        }
    }, [location, handleShowAlertMessage]);

    // Button "Commencer"
    const refGame = useRef(null);
    const scrollGame = () => {
        scrollIntoView(refGame.current!, {behavior : "smooth"});
        window.history.pushState(null, "", '#game'); // Add a "#fonctionnement" to the URL
    }

    useEffect(() => {
        if (window.location.hash === '#game') {
            scrollGame();
        }
    }, []);
    
	return(
        <>
            {alertMessage.content !== "" && <AlertComponent message={alertMessage.content} severity={alertMessage.severity} />}
            <WelcomeBanner scrollGame={scrollGame} />
            <GameSection refGame={refGame} handleShowAlertMessage={handleShowAlertMessage} />
        </>
	)
}

export default HomePage
