import React from 'react';

const Profile = () => {
  const user = JSON.parse(localStorage.getItem("user")); // Recupera el usuario del almacenamiento local

  return (
    <div style={{ padding: '20px' }}>
      <h1>Mi Perfil</h1>
      {user ? (
        <div>
          <p><strong>ID:</strong> {user.id}</p>
          <p><strong>Nombre:</strong> {user.name}</p>
          <p><strong>Email:</strong> {user.mail}</p>
          <p><strong>Teléfono:</strong> {user.phoneN}</p>
        </div>
      ) : (
        <p>No has iniciado sesión.</p>
      )}
    </div>
  );
};

export default Profile;
