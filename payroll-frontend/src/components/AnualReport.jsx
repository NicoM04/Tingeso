import * as React from "react";
import { BarChart } from "@mui/x-charts/BarChart";
import Box from "@mui/material/Box";

const dataset = [
  {
    month: "Ene",
    children_bonus: 1518032,
    extra_hours: 3356291,
    monthly_salary: 23845107,
    salary_bonus: 1074392,
    total_salary: 29793822,
  },
  {
    month: "Feb",
    children_bonus: 2058825,
    extra_hours: 3831405,
    monthly_salary: 23911193,
    salary_bonus: 1988180,
    total_salary: 31789603,
  },
  {
    month: "Mar",
    children_bonus: 1988296,
    extra_hours: 6565229,
    monthly_salary: 24241909,
    salary_bonus: 1158963,
    total_salary: 33954397,
  },
  {
    month: "Abr",
    children_bonus: 1033054,
    extra_hours: 6154627,
    monthly_salary: 22769864,
    salary_bonus: 1078131,
    total_salary: 31035676,
  },
  {
    month: "May",
    children_bonus: 1761147,
    extra_hours: 5845755,
    monthly_salary: 20726098,
    salary_bonus: 1128416,
    total_salary: 29461416,
  },
  {
    month: "Jun",
    children_bonus: 2367078,
    extra_hours: 3006937,
    monthly_salary: 24003152,
    salary_bonus: 1360745,
    total_salary: 30737912,
  },
  {
    month: "Jul",
    children_bonus: 1578067,
    extra_hours: 3809051,
    monthly_salary: 20168524,
    salary_bonus: 1317233,
    total_salary: 26872875,
  },
  {
    month: "Ago",
    children_bonus: 1820069,
    extra_hours: 4269796,
    monthly_salary: 21560874,
    salary_bonus: 1206458,
    total_salary: 28857197,
  },
  {
    month: "Sep",
    children_bonus: 2136203,
    extra_hours: 3259034,
    monthly_salary: 22418839,
    salary_bonus: 1086556,
    total_salary: 28900632,
  },
  {
    month: "Oct",
    children_bonus: 1584761,
    extra_hours: 4371604,
    monthly_salary: 23766348,
    salary_bonus: 1472698,
    total_salary: 31195411,
  },
  {
    month: "Nov",
    children_bonus: 1698817,
    extra_hours: 7665834,
    monthly_salary: 22879773,
    salary_bonus: 1278495,
    total_salary: 33522919,
  },
  {
    month: "Dic",
    children_bonus: 1209035,
    extra_hours: 7770951,
    monthly_salary: 23324507,
    salary_bonus: 1350444,
    total_salary: 33654937,
  },
];

const valueFormatter = (value) => `$ ${value.toLocaleString("en-US")}`;

export default function AnualReport() {
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
    >
      <BarChart
        width={1000}
        height={450}
        margin={{ top: 100, right: 10, left: 150, bottom: 20 }}
        xAxis={[{ scaleType: "band", dataKey: "month", align: "center" }]}
        dataset={dataset}
        series={[
          {
            dataKey: "monthly_salary",
            label: "Monthly Salary",
            valueFormatter,
          },
          { dataKey: "salary_bonus", label: "Salary Bonus", valueFormatter },
          { dataKey: "extra_hours", label: "Extra Hours", valueFormatter },
          {
            dataKey: "children_bonus",
            label: "Children Bonus",
            valueFormatter,
          },
        ]}
      />
    </Box>
  );
}
