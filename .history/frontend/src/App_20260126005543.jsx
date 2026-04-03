import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home/Home.jsx'
import Location from './Location/Location.jsx'
import History from './History/History.jsx'
import CreateAccount from './User/CreateAccount.jsx'
import Login from './User/Login.jsx'
import Header from './components/Header.jsx'

function App() {

  return (
    <Router>
    <Header />
   <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/location" element={<Location />} />
      <Route path="/history" element={<History />} />
      <Route path="/createAccount" element={<CreateAccount />} />
      <Route path="/login" element={<Login />} />
   </Routes>
    </Router>
  )
}

export default App
