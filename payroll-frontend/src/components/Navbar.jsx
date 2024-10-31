// Navbar.jsx
import React, { useState, useEffect } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import Sidemenu from "./Sidemenu";
import { Link, useNavigate } from "react-router-dom"; // Importa useNavigate

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [user, setUser] = useState(null); // Estado para almacenar el usuario
  const navigate = useNavigate(); // Inicializa useNavigate

  const toggleDrawer = (open) => (event) => {
    setOpen(open);
  };

  const handleLogout = () => {
    setUser(null); // Elimina el usuario del estado
    localStorage.removeItem("user"); // Elimina de localStorage
    navigate("/home"); // Redirige a /home después de cerrar sesión
  };

  useEffect(() => {
    // Comprueba si hay un usuario en el almacenamiento local al cargar
    const storedUser = JSON.parse(localStorage.getItem("user"));
    if (storedUser) {
      setUser(storedUser);
    }

    // Escuchar cambios en el localStorage
    const handleStorageChange = () => {
      const updatedUser = JSON.parse(localStorage.getItem("user"));
      setUser(updatedUser); // Actualiza el estado del usuario cuando hay un cambio
    };

    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange); // Limpia el evento cuando el componente se desmonta
    };
  }, []);

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={toggleDrawer(true)}
          >
            <MenuIcon />
          </IconButton>

          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            PrestaBanco
          </Typography>

          {!user ? ( // Si el usuario no ha iniciado sesión
            <>
              <Link to="/login">
                <Button sx={{ color: "#fff" }}>Iniciar sesión</Button>
              </Link>
              <Link to="/register">
                <Button sx={{ color: "#fff" }}>Registrarse</Button>
              </Link>
            </>
          ) : ( // Si el usuario ha iniciado sesión
            <>
              <Link to="/profile">
                <Button sx={{ color: "#fff" }}>Mi Perfil</Button>
              </Link>
              <Button sx={{ color: "#fff" }} onClick={handleLogout}>Cerrar Sesión</Button>
            </>
          )}
        </Toolbar>
      </AppBar>

      <Sidemenu open={open} toggleDrawer={toggleDrawer}></Sidemenu>
    </Box>
  );
}
