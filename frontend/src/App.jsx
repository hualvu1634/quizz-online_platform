import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import Home from './pages/Home';
import Exam from './pages/Exam'; // Đổi từ Course sang Exam
import Navbar from './components/Navbar';

function App() {
  return (
    <Router>
        <div className="d-flex flex-column min-vh-100 bg-light">
        <Navbar />
        <div className="flex-grow-1">
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/" element={<Home />} />
        
        <Route path="/exam/:examId" element={<Exam />} /> 
      </Routes>
       </div>
      </div>
    </Router>
  );
}

export default App;