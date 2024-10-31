import React from 'react';
import { Container, Typography, Paper, Grid } from '@mui/material';

const Profile = () => {
  const user = JSON.parse(localStorage.getItem("user")); // Recupera el usuario del almacenamiento local

  return (
    <Container maxWidth="sm" style={{ padding: '20px', marginTop: '20px' }}>
      <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px' }}>
        <Typography variant="h4" align="center" gutterBottom>
          Mi Perfil
        </Typography>
        {user ? (
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Typography variant="h6"><strong>ID:</strong> {user.id}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="h6"><strong>Nombre:</strong> {user.name}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="h6"><strong>Email:</strong> {user.mail}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="h6"><strong>Teléfono:</strong> {user.phoneN}</Typography>
            </Grid>
          </Grid>
        ) : (
          <Typography variant="h6" color="error" align="center">
            No has iniciado sesión.
          </Typography>
        )}
      </Paper>
    </Container>
  );
};

export default Profile;
