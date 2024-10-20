import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import extraHoursService from "../services/extrahours.service";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";
import SaveIcon from "@mui/icons-material/Save";

const AddEditExtraHours = () => {
  const [rut, setRut] = useState("");
  const [date, setDate] = useState("");
  const [numExtraHours, setNumExtraHours] = useState("");
  const { id } = useParams();
  const [titleExtraHourForm, setTitleExtraHourForm] = useState("");
  const navigate = useNavigate();

  const saveExtraHour = (e) => {
    e.preventDefault();

    const extraHour = { rut, date, numExtraHours, id };
    if (id) {
      //Update data for Extra Hours
      extraHoursService
        .update(extraHour)
        .then((response) => {
          console.log("Hora Extra ha sido actualizada.", response.data);
          navigate("/extraHours/list");
        })
        .catch((error) => {
          console.log(
            "Ha ocurrido un error al intentar actualizar datos de Hora Extra.",
            error
          );
        });
    } else {
      //Create New Extra Hour
      extraHoursService
        .create(extraHour)
        .then((response) => {
          console.log("Hora Extra ha sido ingresada.", response.data);
          navigate("/extraHours/list");
        })
        .catch((error) => {
          console.log(
            "Ha ocurrido un error al intentar crear nueva Hora Extra.",
            error
          );
        });
    }
  };

  useEffect(() => {
    if (id) {
      setTitleExtraHourForm("Editar Hora Extra");
      extraHoursService
        .get(id)
        .then((extraHour) => {
          setRut(extraHour.data.rut);
          const formattedDate = new Date(extraHour.data.date).toISOString().split('T')[0];
          setDate(formattedDate);
          setNumExtraHours(extraHour.data.numExtraHours);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    } else {
      setTitleExtraHourForm("Nueva Hora Extra");
    }
  }, []);

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      component="form"
    >
      <h3> {titleExtraHourForm} </h3>
      <hr />
      <form>
        <FormControl fullWidth>
          <TextField
            id="rut"
            label="Rut"
            value={rut}
            variant="standard"
            onChange={(e) => setRut(e.target.value)}
            helperText="Ej. 12.587.698-8"
          />
        </FormControl>

        <FormControl fullWidth>
          <TextField
            id="date"
            label="Date"
            type="date"
            value={date}
            variant="standard"
            onChange={(e) => setDate(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
          />
        </FormControl>

        <FormControl fullWidth>
          <TextField
            id="numExtraHours"
            label="NumExtraHours"
            type="number"
            value={numExtraHours}
            variant="standard"
            onChange={(e) => setNumExtraHours(e.target.value)}
            helperText="Numero de Horas Extra"
          />
        </FormControl>

        <FormControl>
          <br />
          <Button
            variant="contained"
            color="info"
            onClick={(e) => saveExtraHour(e)}
            style={{ marginLeft: "0.5rem" }}
            startIcon={<SaveIcon />}
          >
            Grabar
          </Button>
        </FormControl>
      </form>
      <hr />
      <Link to="/extraHours/list">Back to List</Link>
    </Box>
  );
};

export default AddEditExtraHours;
