import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

function Login() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await fetch("/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (!res.ok) {
        let errorMsg = `HTTP error! status: ${res.status}`;
        try {
          const errorData = await res.json();
          // Backend returns plain string or object with message, handle accordingly
          if (errorData && typeof errorData === 'object' && errorData.message) {
             errorMsg = errorData.message;
          } else if (typeof errorData === 'string') {
             errorMsg = errorData;
          }
        } catch {}

        throw new Error(errorMsg);
      }

      const data = await res.json();
      console.log("Login Success:", data);
      localStorage.setItem("isLoggedIn", "true");
      // Backend User entity has 'name', 'email', 'membershipType'
      localStorage.setItem("username", data.name || data.email);
      localStorage.setItem("userId", data.id);
      localStorage.setItem("userRole", data.membershipType); // Role is enum field 'membershipType'

      // Navigate to home and reload to update Navbar state
      navigate("/");
      window.location.reload();
    } catch (err) {
      setError("Login failed: " + err.message);
      console.error(err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-app-dark py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-app-surface p-8 rounded-xl shadow-lg border border-gray-700">
        <div>
          <h2 className="mt-2 text-center text-3xl font-extrabold text-white">
            LOGIN
          </h2>
          <p className="mt-2 text-center text-sm text-gray-400">
            Or{" "}
            <Link to="/inscri" className="font-medium text-app-red hover:text-app-red-hover">
              create a new account
            </Link>
          </p>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          {error && (
            <div className="rounded-md bg-red-900/50 border border-red-700 p-4">
              <div className="flex">
                <div className="text-sm text-red-200">{error}</div>
              </div>
            </div>
          )}
          <div className="rounded-md shadow-sm -space-y-px">
            <div className="mb-4">
              <label htmlFor="email-address" className="sr-only">
                Email address
              </label>
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
              <label htmlFor="password" className="sr-only">
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="current-password"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-3 border border-transparent placeholder-gray-400 text-white bg-app-input focus:outline-none focus:ring-2 focus:ring-app-red focus:border-transparent sm:text-sm"
                placeholder="Password"
                value={form.password}
                onChange={handleChange}
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-app-red hover:bg-app-red-hover focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-app-red transition-colors duration-200"
            >
              Sign in
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;
