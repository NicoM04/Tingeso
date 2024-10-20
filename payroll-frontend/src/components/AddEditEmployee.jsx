import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import employeeService from "../services/employee.service";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";
import SaveIcon from "@mui/icons-material/Save";

const AddEditEmployee = () => {
  const [rut, setRut] = useState("");
  const [name, setName] = useState("");
  const [salary, setSalary] = useState("");
  const [children, setChildren] = useState("");
  const [category, setCategory] = useState("");
  const { id } = useParams();
  const [titleEmployeeForm, setTitleEmployeeForm] = useState("");
  const navigate = useNavigate();

  const saveEmployee = (e) => {
    e.preventDefault();

    const employee = { rut, name, salary, children, category, id };
    if (id) {
      //Actualizar Datos Empelado
      employeeService
        .update(employee)
        .then((response) => {
          console.log("Empleado ha sido actualizado.", response.data);
          navigate("/employee/list");
        })
        .catch((error) => {
          console.log(
            "Ha ocurrido un error al intentar actualizar datos del empleado.",
            error
          );
        });
    } else {
      //Crear nuevo empleado
      employeeService
        .create(employee)
        .then((response) => {
          console.log("Empleado ha sido añadido.", response.data);
          navigate("/employee/list");
        })
        .catch((error) => {
          console.log(
            "Ha ocurrido un error al intentar crear nuevo empleado.",
            error
          );
        });
    }
  };

  useEffect(() => {
    if (id) {
      setTitleEmployeeForm("Editar Empleado");
      employeeService
        .get(id)
        .then((employee) => {
          setRut(employee.data.rut);
          setName(employee.data.name);
          setSalary(employee.data.salary);
          setChildren(employee.data.children);
          setCategory(employee.data.category);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    } else {
      setTitleEmployeeForm("Nuevo Empleado");
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
      <h3> {titleEmployeeForm} </h3>
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
            id="name"
            label="Name"
            value={name}
            variant="standard"
            onChange={(e) => setName(e.target.value)}
          />
        </FormControl>

        <FormControl fullWidth>
          <TextField
            id="salary"
            label="Salary"
            type="number"
            value={salary}
            variant="standard"
            onChange={(e) => setSalary(e.target.value)}
            helperText="Salario mensual en Pesos Chilenos"
          />
        </FormControl>

        <FormControl fullWidth>
          <TextField
            id="children"
            label="Children"
            type="number"
            value={children}
            variant="standard"
            onChange={(e) => setChildren(e.target.value)}
          />
        </FormControl>

        <FormControl fullWidth>
          <TextField
            id="category"
            label="Category"
            value={category}
            select
            variant="standard"
            defaultValue="A"
            onChange={(e) => setCategory(e.target.value)}
            style={{ width: "25%" }}
          >
            <MenuItem value={"A"}>A</MenuItem>
            <MenuItem value={"B"}>B</MenuItem>
            <MenuItem value={"C"}>C</MenuItem>
          </TextField>
        </FormControl>

        <FormControl>
          <br />
          <Button
            variant="contained"
            color="info"
            onClick={(e) => saveEmployee(e)}
            style={{ marginLeft: "0.5rem" }}
            startIcon={<SaveIcon />}
          >
            Grabar
          </Button>
        </FormControl>
      </form>
      <hr />
      <Link to="/employee/list">Back to List</Link>
    </Box>
  );
};

export default AddEditEmployee;
