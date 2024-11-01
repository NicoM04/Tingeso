import * as React from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import HomeIcon from "@mui/icons-material/Home";
import RequestQuoteIcon from '@mui/icons-material/RequestQuote';  // Para "Simular Crédito"
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn';  // Para "Solicitudes por revisar"
import AssignmentIcon from '@mui/icons-material/Assignment'; // Para "Mis Solicitudes"
import CreditScoreIcon from '@mui/icons-material/CreditScore'; // Para "Solicitar Crédito"
import LoginIcon from '@mui/icons-material/Login'; // Para "Simular Crédito" sin iniciar sesión
import { useNavigate } from "react-router-dom";

export default function Sidemenu({ open, toggleDrawer }) {
  const navigate = useNavigate();

  // Obtener usuario desde localStorage
  const user = JSON.parse(localStorage.getItem("user"));

  // Verificar si es un ejecutivo
  const isEjecutive = user?.isEjecutive;

  // Verificar si el usuario ha iniciado sesión
  const isLoggedIn = !!user;

  const listOptions = () => (
    <Box
      role="presentation"
      onClick={toggleDrawer(false)}
    >
      <List>
        {/* Opción de Home para todos los usuarios */}
        <ListItemButton onClick={() => navigate("/home")}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <Divider />

        {isLoggedIn && isEjecutive && (
          <>
            {/* Opciones visibles solo para ejecutivos */}
            <ListItemButton onClick={() => navigate("/simulateCredit")}>
              <ListItemIcon>
                <RequestQuoteIcon />
              </ListItemIcon>
              <ListItemText primary="Simular Crédito" />
            </ListItemButton>

            <ListItemButton onClick={() => navigate("/creditRequestTable")}>
              <ListItemIcon>
                <AssignmentTurnedInIcon />
              </ListItemIcon>
              <ListItemText primary="Solicitudes por revisar" />
            </ListItemButton>

            <Divider />
          </>
        )}

        {isLoggedIn && !isEjecutive &&(
          <>
            {/* Opciones visibles para cualquier usuario autenticado */}
            <ListItemButton onClick={() => navigate("/simulateCredit")}>
              <ListItemIcon>
                <RequestQuoteIcon />
              </ListItemIcon>
              <ListItemText primary="Simular Crédito" />
            </ListItemButton>

            <ListItemButton onClick={() => navigate("/userCreditTable")}>
              <ListItemIcon>
                <AssignmentIcon />
              </ListItemIcon>
              <ListItemText primary="Mis Solicitudes" />
            </ListItemButton>

            <ListItemButton onClick={() => navigate("/CreditRequest")}>
              <ListItemIcon>
                <CreditScoreIcon />
              </ListItemIcon>
              <ListItemText primary="Solicitar Crédito" />
            </ListItemButton>
          </>
        )}

        {!isLoggedIn && (
          <>
            {/* Opciones visibles para usuarios no autenticados */}
            <ListItemButton onClick={() => navigate("/simulateCredit")}>
              <ListItemIcon>
                <RequestQuoteIcon />
              </ListItemIcon>
              <ListItemText primary="Simular Crédito" />
            </ListItemButton>
          </>
        )}
      </List>
    </Box>
  );

  return (
    <div>
      <Drawer anchor={"left"} open={open} onClose={toggleDrawer(false)}>
        {listOptions()}
      </Drawer>
    </div>
  );
}
