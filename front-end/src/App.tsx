import React from 'react';
import { createTheme, responsiveFontSizes, ThemeProvider, StyledEngineProvider } from '@mui/material/styles';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import "./styles/styles.scss"
import variables from "./styles/abstract/variables.module.scss"
import HomePage from './pages/HomePage/HomePage.tsx';

let theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: variables.primaryColor,
    },
    secondary: {
      main: variables.secondaryColor,
    },
    text: {
      primary: variables.textColor,
      disabled: variables.greyColor,
    },
    background: {
      default: variables.whiteColor,
      paper: variables.whiteColor,
    },
  },
  typography: {
    h1: {
      fontFamily: [
        'Fredoka',
        'cursive',
      ].join(','),
    },
    h2: {
      fontFamily: [
        'Fredoka',
        'cursive',
      ].join(','),
    },
    h3: {
      fontFamily: [
        'Fredoka',
        'sans-serif',
      ].join(','),
    },
    h4: {
      fontFamily: [
        'Fredoka',
        'sans-serif',
      ].join(','),
    },
    h5: {
      fontFamily: [
        'Fredoka',
        'sans-serif',
      ].join(','),
    },
    h6: {
      fontFamily: [
        'Fredoka',
        'sans-serif',
      ].join(','),
    },
    subtitle1: {
      fontFamily: [
        'Raleway',
        'sans-serif',
      ].join(','),
    },
    body1: {
      fontFamily: [
        'Raleway',
        'sans-serif',
      ].join(','),
    },
    caption: {
      fontFamily: [
        'Raleway',
        'sans-serif',
      ].join(','),
    },
    button: {
      fontFamily: [
        'Fredoka',
        'cursive',
      ].join(','),
      fontWeight: "bold",
    }
  },
  shape: {
    borderRadius: 30
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: { // Apply to all buttons
          fontSize: '1rem',
          padding: '8px 16px',
        },
        sizeLarge: { // Specifically for large buttons
          fontSize: '1.4rem',
          padding: '11px 24px',
        },
      },
    },
  },

});

theme = responsiveFontSizes(theme);

const App = () => {

  return (
    <ThemeProvider theme={theme}>
        <StyledEngineProvider injectFirst>
            <BrowserRouter> 
            <Routes>
                <Route path="/" element={<HomePage />}/>
                <Route path="*" element={<Navigate replace to="/" />} />
            </Routes>
            </BrowserRouter>
        </StyledEngineProvider>
    </ThemeProvider>
  );
}

export default App;
