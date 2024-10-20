import React, { useState } from "react";
import {
  Box,
  TextField,
  Button,
  Typography,
  Paper,
  Backdrop,
  CircularProgress,
} from "@mui/material";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false); // Estado para controlar la carga

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true); // Activa el loading al iniciar sesión

    // Aquí implementa la lógica para enviar los datos al backend
    console.log("Email:", email);
    console.log("Password:", password);

    // Simula una solicitud al backend
    setTimeout(() => {
      setLoading(false); // Desactiva el loading después de un tiempo (simulación)
      // Aquí puedes manejar la respuesta de tu backend
    }, 2000); // Cambia esto por la llamada real al backend
  };

  return (
    <>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        {/* Usamos Paper de Material-UI para crear un cuadro estilizado */}
        <Paper elevation={3} sx={{ padding: 4, width: "400px" }}>
          <Typography variant="h5" component="h1" align="center" gutterBottom>
            Iniciar Sesión
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              label="Email"
              type="email"
              variant="outlined"
              fullWidth
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <TextField
              label="Contraseña"
              type="password"
              variant="outlined"
              fullWidth
              margin="normal"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <Button
              type="submit"
              variant="contained"
              color="primary"
              fullWidth
              sx={{ marginTop: 2 }}
            >
              Iniciar Sesión
            </Button>
          </form>
        </Paper>
      </Box>

      {/* Backdrop para oscurecer el fondo durante el login */}
      <Backdrop open={loading} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <CircularProgress color="inherit" />
      </Backdrop>
    </>
  );
};

export default Login;
