import { Box, Typography, Button } from "@mui/material"
import React from 'react';
import styles from "../../styles/components/homepage/welcomeBanner.module.scss"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAnglesDown } from '@fortawesome/free-solid-svg-icons'

const WelcomeBanner = (props) => {
    return(
        <Box id={styles.boxSection}>
            <div className={styles.area} >
                <Typography className={styles.content} variant="h1" color="initial">NitroBall 2D</Typography>
                <Button className={styles.content} variant="contained" color="secondary" size="large" onClick={props.scrollGame}>Play !</Button>
                <Box className={styles.content} id={styles.boxIcon} onClick={props.scrollGame}>
                    <FontAwesomeIcon id={styles.icon} icon={faAnglesDown} />
                </Box>
                <ul className={styles.circles}>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                        <li></li>
                </ul>
            </div >
        </Box>
    )
}

export default WelcomeBanner