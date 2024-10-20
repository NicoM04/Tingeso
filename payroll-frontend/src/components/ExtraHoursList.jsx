import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import extraHoursService from "../services/extrahours.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import MoreTimeIcon from '@mui/icons-material/MoreTime';

const ExtraHoursList = () => {
  const [extraHours, setExtraHours] = useState([]);

  const navigate = useNavigate();

  const init = () => {
    extraHoursService
      .getAll()
      .then((response) => {
        console.log("Mostrando listado de todos las Hrs Extra.", response.data);
        setExtraHours(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de todas las Hrs Extra.",
          error
        );
      });
  };

  useEffect(() => {
    init();
  }, []);

  const handleDelete = (id) => {
    console.log("Printing id", id);
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar esta Hora Extra?"
    );
    if (confirmDelete) {
      extraHoursService
        .remove(id)
        .then((response) => {
          console.log("Hora Extra ha sido eliminada.", response.data);
          init();
        })
        .catch((error) => {
          console.log(
            "Se ha producido un error al intentar eliminar la Hora Extra",
            error
          );
        });
    }
  };

  const handleEdit = (id) => {
    console.log("Printing id", id);
    navigate(`/extraHours/edit/${id}`);
  };

  return (
    <TableContainer component={Paper}>
      <br />
      <Link
        to="/extraHours/add"
        style={{ textDecoration: "none", marginBottom: "1rem" }}
      >
        <Button
          variant="contained"
          color="primary"
          startIcon={<MoreTimeIcon />}
        >
          Ingresar Horas Extra
        </Button>
      </Link>
      <br /> <br />
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Rut
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Fecha
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Nro. HrsExtra
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {extraHours.map((extraHour) => (
            <TableRow
              key={extraHour.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell align="left">{extraHour.rut}</TableCell>
              <TableCell align="left">{new Date(extraHour.date).toISOString().split('T')[0]}</TableCell>
              <TableCell align="right">{extraHour.numExtraHours}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  color="info"
                  size="small"
                  onClick={() => handleEdit(extraHour.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<EditIcon />}
                >
                  Editar
                </Button>

                <Button
                  variant="contained"
                  color="error"
                  size="small"
                  onClick={() => handleDelete(extraHour.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<DeleteIcon />}
                >
                  Eliminar
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ExtraHoursList;
