import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

function Login() {

  const [form, setForm] = useState({
    email: "",
    password: ""
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // stop refresh

    try {
      const response = await fetch("http://localhost:8080/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });

      if (!response.ok) {
        alert("Invalid email or password");
        return;
      }

      const user = await response.json();

      // Optional: store user in localStorage
      localStorage.setItem("user", JSON.stringify(user));

      alert("Login successful!");

      navigate("/home"); // redirect after login

    } catch (error) {
      console.error("Login error:", error);
      alert("Server error");
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">

        <div className="login-header">
          <h1 className="login-title">Login</h1>
          <p className="login-subtitle">
            Access your phishing detection dashboard.
          </p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>

          <div className="login-field">
            <label htmlFor="email" className="login-label">
              Email
            </label>
            <input
              required
              type="email"
              id="email"
              name="email"
              placeholder="Enter Email"
              className="login-input"
              value={form.email}
              onChange={handleChange}
            />
          </div>

          <div className="login-field">
            <label htmlFor="password" className="login-label">
              Password
            </label>
            <input
              required
              type="password"
              id="password"
              name="password"
              placeholder="Enter Password"
              className="login-input"
              value={form.password}
              onChange={handleChange}
            />
          </div>

          <button type="submit" className="login-btn">
            Login
          </button>

          <div className="login-footer">
            Don’t have an account?{" "}
            <a href="/createAccount" className="login-link">
              Create one
            </a>
          </div>

        </form>
      </div>
    </div>
  );
}

export default Login;