import { useEffect, useState } from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import paycheckService from "../services/paycheck.service";

const PaycheckList = () => {
  const [paychecks, setPaycheck] = useState([]);

  const init = () => {
    paycheckService
      .getAll()
      .then((response) => {
        console.log(
          "Mostrando planilla de sueldos de los empleados.",
          response.data
        );
        setPaycheck(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar planilla de sueldos de los empleados.",
          error
        );
      });
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <TableContainer component={Paper}>
      <h3>Planilla de sueldos</h3>
      <hr />
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Rut
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Año
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Mes
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Sueldo.Base
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Bonif.Salario
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Bonif.Hijos
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Bonif.HrsExtra
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Sueldo.TOTAL
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {paychecks.map((paycheck) => (
            <TableRow
              key={paychecks.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell align="right">{paycheck.rut}</TableCell>
              <TableCell align="right">{paycheck.year}</TableCell>
              <TableCell align="right">{paycheck.month}</TableCell>

              <TableCell align="right">
                {new Intl.NumberFormat("es-CL", { style: "decimal" }).format(
                  paycheck.monthlySalary
                )}
              </TableCell>
              <TableCell align="right">
                {new Intl.NumberFormat("es-CL", { style: "decimal" }).format(
                  paycheck.salaryBonus
                )}
              </TableCell>
              <TableCell align="right">
                {new Intl.NumberFormat("es-CL", { style: "decimal" }).format(
                  paycheck.childrenBonus
                )}
              </TableCell>
              <TableCell align="right">
                {new Intl.NumberFormat("es-CL", { style: "decimal" }).format(
                  paycheck.extraHoursBonus
                )}
              </TableCell>
              <TableCell align="right">
                {new Intl.NumberFormat("es-CL", { style: "decimal" }).format(
                  paycheck.totalSalary
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default PaycheckList;
