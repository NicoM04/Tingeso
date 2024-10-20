import React from "react";
import { Link } from "react-router-dom"; // Importa Link para la navegación interna en React Router

const Home = ({ isLoggedIn }) => {
  return (
    <div>
      <h1>SisGR: Sistema de Gestión Remuneraciones</h1>
      <p>
        SisGR es una aplicación web para gestionar planillas de sueldos de
        empleados. Esta aplicación ha sido desarrollada usando tecnologías como{" "}
        <a href="https://spring.io/projects/spring-boot">Spring Boot</a> (para
        el backend), <a href="https://reactjs.org/">React</a> (para el frontend)
        y <a href="https://www.mysql.com/products/community/">MySQL</a> (para la
        base de datos).
      </p>
      {isLoggedIn && ( // Verifica si el usuario está autenticado
        <Link to="/profile">
          <button>Ir a mi perfil</button>
        </Link>
      )}
    </div>
  );
};

export default Home;
