import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function Navbar({ onLogout }) {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState("");
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    const loggedIn = localStorage.getItem("isLoggedIn") === "true";
    setIsLoggedIn(loggedIn);
    if (loggedIn) {
        setUsername(localStorage.getItem("username") || "User");
    }
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const query = formData.get("query");
    navigate(`/?search=${encodeURIComponent(query)}`);
  };

  const handleLogout = () => {
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("username");
    localStorage.removeItem("userRole"); 
    localStorage.removeItem("userId");
    setIsLoggedIn(false);
    if (onLogout) onLogout();
    alert("You have been logged out.");
    navigate("/");
    window.location.reload();
  };

  return (
    <nav className="bg-app-surface shadow-md sticky top-0 z-50 border-b border-gray-700">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            {/* Logo */}
            <div className="flex-shrink-0 flex items-center">
              <Link to="/">
                <img className="h-10 w-auto rounded-full border border-gray-600" src="/icon-logo.jpg" alt="Logo" />
              </Link>
            </div>
            {/* Desktop Menu */}
            <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
              <Link to="/" className="text-gray-300 hover:text-white inline-flex items-center px-1 pt-1 text-sm font-medium transition-colors">
                Home
              </Link>

              {isLoggedIn && (
                 <Link to="/mybooks" className="text-gray-300 hover:text-white inline-flex items-center px-1 pt-1 text-sm font-medium transition-colors">
                   My Books
                 </Link>
              )}
            </div>
          </div>

          <div className="flex items-center">
             {/* Search */}
            <form onSubmit={handleSearch} className="hidden md:flex items-center mr-4 bg-app-surface">
              <div className="relative">
                <input
                  type="text"
                  name="query"
                  placeholder="Search..."
                  className="bg-white text-black rounded-lg py-1 px-3 pl-10 text-sm focus:outline-none w-64 h-8"
                />
                <button type="submit" className="absolute left-0 top-0 mt-1 ml-2 text-app-red hover:text-app-red-hover">
                  <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                     <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                  </svg>
                </button>
              </div>
            </form>

            <div className="flex items-center space-x-4">
              {!isLoggedIn ? (
                <>
                  <Link
                    to="/login"
                    className="bg-app-red hover:bg-app-red-hover text-white px-4 py-1.5 rounded-md text-sm font-bold transition-colors duration-200"
                  >
                    Login
                  </Link>
                  <Link to="/inscri" className="p-1 rounded-full hover:bg-gray-700 transition">
                    <img className="h-8 w-8 rounded-full border border-app-red" src="/iconinscri.png" alt="Register" />
                  </Link>
                </>
              ) : (
                <div className="flex items-center space-x-4">
                  <span className="text-white font-medium hidden sm:block">{username}</span>
                  <button
                    onClick={handleLogout}
                    className="text-gray-400 hover:text-app-red font-medium text-sm transition-colors duration-200"
                  >
                    Logout
                  </button>
                </div>
              )}
            </div>
            
            {/* Mobile menu button */}
            <div className="-mr-2 flex items-center sm:hidden">
              <button
                onClick={() => setIsMenuOpen(!isMenuOpen)}
                className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none"
              >
                <span className="sr-only">Open main menu</span>
                <svg className={`${isMenuOpen ? 'hidden' : 'block'} h-6 w-6`} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
                <svg className={`${isMenuOpen ? 'block' : 'hidden'} h-6 w-6`} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      <div className={`${isMenuOpen ? 'block' : 'hidden'} sm:hidden bg-app-surface`}>
        <div className="pt-2 pb-3 space-y-1">
          <Link to="/" onClick={()=>setIsMenuOpen(false)} className="text-white block px-3 py-2 rounded-md text-base font-medium hover:bg-gray-700">Home</Link>

          {isLoggedIn && (
              <Link to="/mybooks" onClick={()=>setIsMenuOpen(false)} className="text-gray-300 block px-3 py-2 rounded-md text-base font-medium hover:bg-gray-700 hover:text-white">My Books</Link>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
