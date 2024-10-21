import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import EmployeeList from './components/EmployeesList';
import AddEditEmployee from './components/AddEditEmployee';
import ExtraHoursList from './components/ExtraHoursList';
import AddEditExtraHours from './components/AddEditExtraHours';
import NotFound from './components/NotFound';
import PaycheckList from './components/PaycheckList';
import PaycheckCalculate from './components/PaycheckCalculate';
import AnualReport from './components/AnualReport';
import Profile from './components/Profile';
import Login from './components/Login';
import Register from './components/Register';
import SimulateCredit from './components/SimulateCredit';
import CreditRequest from './components/CreditRequest';
//import { Login } from '@mui/icons-material';

function App() {
  
  return (
      <Router>
          <div className="container">
          <Navbar></Navbar>
            <Routes>
              <Route path="/home" element={<Home/>} />
              <Route path="/profile" element={<Profile/>} />
              <Route path="/simulateCredit" element={<SimulateCredit/>} />
              <Route path="/creditRequest" element={<CreditRequest/>} />
              <Route path="/login" element={<Login/>} />
              <Route path="/register" element={<Register/>} />
              <Route path="/employee/list" element={<EmployeeList/>} />
              <Route path="/employee/add" element={<AddEditEmployee/>} />
              <Route path="/employee/edit/:id" element={<AddEditEmployee/>} />
              <Route path="/paycheck/list" element={<PaycheckList/>} />
              <Route path="/paycheck/calculate" element={<PaycheckCalculate/>} />
              <Route path="/reports/AnualReport" element={<AnualReport/>} />
              <Route path="/extraHours/list" element={<ExtraHoursList/>} />
              <Route path="/extraHours/add" element={<AddEditExtraHours/>} />
              <Route path="/extraHours/edit/:id" element={<AddEditExtraHours/>} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App