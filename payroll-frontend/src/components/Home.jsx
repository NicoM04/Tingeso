import React from "react";
import { Link } from "react-router-dom"; // Importa Link para la navegación interna en React Router

const Home = ({ isLoggedIn }) => {
  return (
    <div>
      <h1>PrestaBanco</h1>
      <p>
        PrestaBanco es una aplicación web diseñada para gestionar y facilitar el proceso de solicitudes de créditos para usuarios. La plataforma permite a los usuarios crear, monitorear y administrar sus solicitudes de crédito de manera eficiente. 
        Esta aplicación ha sido desarrollada utilizando tecnologías modernas como{" "}
        <a href="https://spring.io/projects/spring-boot">Spring Boot</a> (para el backend), <a href="https://reactjs.org/">React</a> (para el frontend) y <a href="https://www.mysql.com/products/community/">MySQL</a> (para la base de datos).
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
