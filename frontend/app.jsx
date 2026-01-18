import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./home";
import Emprunter from "./emprunter";
import Inscription from "./inscri";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/emprunter" element={<Emprunter />} />
        <Route path="/inscri" element={<Inscription />} />
      </Routes>
    </Router>
  );
}

export default App;
