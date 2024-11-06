// Login.jsx
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
import { useNavigate } from "react-router-dom"; // Importa useNavigate
import userClient from "../services/userClient.service"; // Importa el servicio
const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false); // Estado para controlar la carga
  const navigate = useNavigate(); // Inicializa useNavigate

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true); // Activa el loading al iniciar sesión

    try {
      // Llamar al método de login en el servicio
      const response = await userClient.login(email, password);

      // Verificar si la respuesta es correcta
      if (response.status === 200) {
        const data = response.data;
        console.log("Inicio de sesión exitoso:", data);
        
        // Guarda el usuario en el almacenamiento local
        localStorage.setItem("user", JSON.stringify(data));
        
        // Notifica cambios en localStorage para actualización en otros componentes
        window.dispatchEvent(new Event("storage"));

        // Redirige a la página principal o de perfil
        navigate("/home");
      } else {
        console.error("Error al iniciar sesión:", response.data);
        alert("Credenciales incorrectas. Inténtalo de nuevo.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("Error de conexión. Inténtalo de nuevo.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
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

      <Backdrop open={loading} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <CircularProgress color="inherit" />
      </Backdrop>
    </>
  );
};

export default Login;
