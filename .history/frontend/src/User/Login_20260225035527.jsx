import "./Login.css";

function Login() {
  return (
    <div className="login-page">
      <div className="login-card">

        <div className="login-header">
          <h1 className="login-title">Login</h1>
          <p className="login-subtitle">
            Access your phishing detection dashboard.
          </p>
        </div>

        <form className="login-form">

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