import { useState } from "react";
import "./CreateAccount.css";

const API = "http://localhost:8080/users";

function CreateAccount() {
  const [form, setForm] = useState({
    fname: "",
    lname: "",
    email: "",
    password: "",
  });

  const [status, setStatus] = useState("");

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // ✅ stop page refresh

    setStatus("Creating account...");

    try {
      const res = await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg || `HTTP ${res.status}`);
      }

      setStatus("✅ Account created!");
      setForm({ fname: "", lname: "", email: "", password: "" });
    } catch (err) {
      console.error(err);
      setStatus("❌ Failed to create account (check backend logs).");
    }
  };

  return (
    <div className="create-page">
      <div className="create-card">
        <div className="create-header">
          <h1 className="create-title">Create new account</h1>
          <p className="create-subtitle">
            Set up your profile to start scanning URLs.
          </p>
        </div>

        <form className="create-form" onSubmit={handleSubmit}>
          <div className="create-grid">
            <div className="field">
              <label className="field-label" htmlFor="fname">First Name</label>
              <input
                className="field-input"
                id="fname"
                name="fname"
                type="text"
                placeholder="Enter first name"
                value={form.fname}
                onChange={handleChange}
                required
              />
            </div>

            <div className="field">
              <label className="field-label" htmlFor="lname">Last Name</label>
              <input
                className="field-input"
                id="lname"
                name="lname"
                type="text"
                placeholder="Enter last name"
                value={form.lname}
                onChange={handleChange}
                required
              />
            </div>

            <div className="field">
              <label className="field-label" htmlFor="email">Email</label>
              <input
                className="field-input"
                id="email"
                name="email"
                type="email"
                placeholder="Enter email"
                value={form.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="field">
              <label className="field-label" htmlFor="password">Password</label>
              <input
                className="field-input"
                id="password"
                name="password"
                type="password"
                placeholder="Enter password"
                value={form.password}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="create-actions">
            <button type="submit" className="create-btn">
              Create Account
            </button>

            {status && <p className="create-status">{status}</p>}

            <p className="create-foot">
              Already have an account?{" "}
              <a className="create-link" href="/login">Log in</a>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateAccount;