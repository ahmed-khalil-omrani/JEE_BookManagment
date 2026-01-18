import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

function Inscription() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    confirm_password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (form.password !== form.confirm_password) {
      setError("Passwords do not match!");
      return;
    }

    const payload = {
      name: form.name,
      email: form.email,
      password: form.password,
      membershipType: "MEMBER", // Matches Backend Enum (ADMIN, MEMBER)
    };

    try {
      const res = await fetch("/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        let errorMsg = "Registration error";
        try {
          const errorData = await res.json();
          if (errorData.message) errorMsg = errorData.message;
          else if (typeof errorData === 'string') errorMsg = errorData;
        } catch {}
        throw new Error(errorMsg);
      }

      const data = await res.json();
      console.log("API Response:", data);

      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("username", data.name);
      localStorage.setItem("userId", data.id);
      localStorage.setItem("userRole", data.membershipType);

      navigate("/");
      window.location.reload();
    } catch (err) {
      setError("Registration error: " + err.message);
      console.error(err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-app-dark py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-app-surface p-8 rounded-xl shadow-lg border border-gray-700">
        <div>
          <h2 className="mt-2 text-center text-3xl font-extrabold text-white">
            REGISTRATION
          </h2>
          <p className="mt-2 text-center text-sm text-gray-400">
            Or{" "}
            <Link to="/login" className="font-medium text-app-red hover:text-app-red-hover">
              sign in to your existing account
            </Link>
          </p>
        </div>
        <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
          {error && (
            <div className="rounded-md bg-red-900/50 border border-red-700 p-4">
               <div className="text-sm text-red-200">{error}</div>
            </div>
          )}
          
          <div className="rounded-md shadow-sm space-y-4">
            <div>
              <label htmlFor="name" className="sr-only">Full Name</label>
              <input
                id="name"
                name="name"
                type="text"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-3 border border-transparent placeholder-gray-400 text-white bg-app-input focus:outline-none focus:ring-2 focus:ring-app-red focus:border-transparent sm:text-sm"
                placeholder="Full Name"
                value={form.name}
                onChange={handleChange}
              />
            </div>
            <div>
              <label htmlFor="email-address" className="sr-only">Email address</label>
              <input
                id="email-address"
                name="email"
                type="email"
                autoComplete="email"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-3 border border-transparent placeholder-gray-400 text-white bg-app-input focus:outline-none focus:ring-2 focus:ring-app-red focus:border-transparent sm:text-sm"
                placeholder="Email address"
                value={form.email}
                onChange={handleChange}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">Password</label>
              <input
                id="password"
                name="password"
                type="password"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-3 border border-transparent placeholder-gray-400 text-white bg-app-input focus:outline-none focus:ring-2 focus:ring-app-red focus:border-transparent sm:text-sm"
                placeholder="Password"
                value={form.password}
                onChange={handleChange}
              />
            </div>
            <div>
              <label htmlFor="confirm_password" className="sr-only">Confirm Password</label>
              <input
                id="confirm_password"
                name="confirm_password"
                type="password"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-3 border border-transparent placeholder-gray-400 text-white bg-app-input focus:outline-none focus:ring-2 focus:ring-app-red focus:border-transparent sm:text-sm"
                placeholder="Confirm Password"
                value={form.confirm_password}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="pt-4">
            <button
              type="submit"
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-app-red hover:bg-app-red-hover focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-app-red transition-colors duration-200"
            >
              Create Account
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Inscription;
