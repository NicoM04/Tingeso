import { useState } from "react";
import { useNavigate } from "react-router-dom";
import paycheckService from "../services/paycheck.service";
import CalculateIcon from "@mui/icons-material/Calculate";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";

const PaycheckCalculate = () => {
  const [year, setYear] = useState("");
  const [month, setMonth] = useState("");

  const navigate = useNavigate();

  const calculatePaychek = (e) => {
    e.preventDefault();
    console.log("Solicitar calcular planilla.", year, "-", month);
    paycheckService
      .calculate(year, month)
      .then((response) => {
        console.log("Planilla ha sido actualizada.", response.data);
        navigate("/paycheck/list");
      })
      .catch((error) => {
        console.log(
          "Ha ocurrido un error al intentar calcular liquidaciones de sueldos.",
          error
        );
      });
    console.log("Fin calculo planilla.");
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      component="form"
    >
      <h3> Calcular Planilla Sueldos </h3>
      <hr />
      <form>
        <FormControl fullWidth>
          <TextField
            id="year"
            label="Year"
            value={year}
            variant="standard"
            onChange={(e) => setYear(e.target.value)}
          />
        </FormControl>

        <FormControl fullWidth>
          <TextField
            id="month"
            label="Month"
            value={month}
            select
            variant="standard"
            defaultValue="1"
            onChange={(e) => setMonth(e.target.value)}
            style={{ width: "25%" }}
          >
            <MenuItem value={1}>Enero</MenuItem>
            <MenuItem value={2}>Febrero</MenuItem>
            <MenuItem value={3}>Marzo</MenuItem>
            <MenuItem value={4}>Abril</MenuItem>
            <MenuItem value={5}>Mayo</MenuItem>
            <MenuItem value={6}>Junio</MenuItem>
            <MenuItem value={7}>Julio</MenuItem>
            <MenuItem value={8}>Agosto</MenuItem>
            <MenuItem value={9}>Septiembre</MenuItem>
            <MenuItem value={10}>Octubre</MenuItem>
            <MenuItem value={11}>Noviembre</MenuItem>
            <MenuItem value={12}>Diciembre</MenuItem>
          </TextField>
        </FormControl>

        <FormControl>
          <br />
          <Button
            variant="contained"
            color="info"
            onClick={(e) => calculatePaychek(e)}
            style={{ marginLeft: "0.5rem" }}
            startIcon={<CalculateIcon />}
          >
            Calcular Liquidaciones
          </Button>
        </FormControl>
      </form>
    </Box>
  );
};

export default PaycheckCalculate;
