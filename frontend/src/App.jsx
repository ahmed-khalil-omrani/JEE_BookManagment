import React from "react";
import "./index.css";

import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./components/home";
import Inscription from "./components/inscri";
import Login from "./components/Login";
import AddBook from "./components/AddBook";
import EditBook from "./components/EditBook";
import AddAuthor from "./components/AddAuthor";
import MyBooks from "./components/MyBooks";
import Navbar from "./components/Navbar";


ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/inscri" element={<Inscription />} />
        <Route path="/login" element={<Login />} />
        <Route path="/addbook" element={<AddBook />} />
        <Route path="/addauthor" element={<AddAuthor />} />
        <Route path="/editbook/:id" element={<EditBook />} />
        <Route path="/mybooks" element={<MyBooks />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
