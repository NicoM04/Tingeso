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

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false); // Estado para controlar la carga
  const navigate = useNavigate(); // Inicializa useNavigate

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true); // Activa el loading al iniciar sesión

    try {
      // Enviar la solicitud de inicio de sesión al backend
      const response = await fetch("http://52.137.120.247:80/api/user/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ mail: email, password }), // Envía el correo y la contraseña
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Inicio de sesión exitoso:", data);
        
        // Guarda el usuario en el almacenamiento local
        localStorage.setItem("user", JSON.stringify(data));
        
        // Notifica manualmente que se hizo un cambio en localStorage (por compatibilidad)
        window.dispatchEvent(new Event("storage"));

        // Redirige a la página de perfil o a la página principal
        navigate("/home"); // Cambia esto si quieres redirigir a otra página
      } else {
        const errorData = await response.json();
        console.error("Error al iniciar sesión:", errorData);
        alert("Credenciales incorrectas. Inténtalo de nuevo."); // Muestra un mensaje de error
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("Error de conexión. Inténtalo de nuevo."); // Muestra un mensaje de error en caso de fallo de conexión
    } finally {
      setLoading(false); // Desactiva el loading
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
