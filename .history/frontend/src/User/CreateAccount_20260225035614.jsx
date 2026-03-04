import { useState } from "react";
import "./CreateAccount.css";

function CreateAccount() {

  const [form, setForm] = useState({
    fname: "",
    lname: "",
    email: "",
    password: ""
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // ✅ stop page reload

    try {
      const response = await fetch("http://localhost:8080/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });

      if (response.ok) {
        alert("Account created successfully!");
        setForm({ fname: "", lname: "", email: "", password: "" });
      } else {
        alert("Failed to create account.");
      }

    } catch (error) {
      console.error("Error:", error);
      alert("Server error.");
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
                id="fname"
                name="fname"
                className="field-input"
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
                id="lname"
                name="lname"
                className="field-input"
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
                id="email"
                name="email"
                className="field-input"
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
                id="password"
                name="password"
                className="field-input"
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

            <p className="create-foot">
              Already have an account?{" "}
              <a className="create-link" href="/login">
                Log in
              </a>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateAccount;