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
import { useNavigate } from "react-router-dom";
import userClient from "../services/userClient.service"; // Importa el servicio
const Register = () => {
  const [name, setName] = useState("");
  const [rut, setRut] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    try {
      // Llamar al método de registro (create) en el servicio
      const response = await userClient.create({
        name: name,
        rut: rut,
        mail: email,
        phoneN: phone,
        password: password,
        isEjecutive: false, // Cambia según la lógica de tu aplicación
      });

      if (response.status === 200) {
        const data = response.data;
        console.log("Registro exitoso:", data);
        
        // Redirige a la página de inicio de sesión
        navigate("/login");
      } else {
        console.error("Error al registrarse:", response.data);
        alert("Error al registrarse. Inténtalo de nuevo.");
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
            Registrarse
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              label="Nombre"
              variant="outlined"
              fullWidth
              margin="normal"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
            <TextField
              label="RUT"
              variant="outlined"
              fullWidth
              margin="normal"
              value={rut}
              onChange={(e) => setRut(e.target.value)}
              required
            />
            <TextField
              label="Correo Electrónico"
              type="email"
              variant="outlined"
              fullWidth
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <TextField
              label="Teléfono"
              variant="outlined"
              fullWidth
              margin="normal"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
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
              Registrarse
            </Button>
          </form>
        </Paper>
      </Box>

      {/* Backdrop para mostrar la carga durante el registro */}
      <Backdrop
        open={loading}
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
      >
        <CircularProgress color="inherit" />
      </Backdrop>
    </>
  );
};

export default Register;
