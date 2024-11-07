import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import NotFound from './components/NotFound';
import Profile from './components/Profile';
import Login from './components/Login';
import Register from './components/Register';
import SimulateCredit from './components/SimulateCredit';
import CreditRequest from './components/CreditRequest';
import DocumentUpload from './components/DocumentUpload';
import CreditRequestDetail from './components/CreditRequestDetail';
import CreditRequestsTable from './components/CreditRequestsTable';
import UserCreditTable from './components/UserCreditTable';
import CreditDetails from './components/UserCreditDetails';

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
              <Route path="/creditRequestDetail/:creditId" element={<CreditRequestDetail />} />
              <Route path="/creditRequestTable" element={<CreditRequestsTable/>} />
              <Route path="/userCreditTable" element={<UserCreditTable/>} />
              <Route path="/userCreditDetails/:creditId" element={<CreditDetails />} />
              <Route path="/upload-documents/:creditId" element={<DocumentUpload />} />
              <Route path="/login" element={<Login/>} />
              <Route path="/register" element={<Register/>} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App