import "./CreateAccount.css";

function CreateAccount() {
  return (
    <div className="create-page">
      <div className="create-card">
        <div className="create-header">
          <h1 className="create-title">Create new account</h1>
          <p className="create-subtitle">
            Set up your profile to start scanning URLs.
          </p>
        </div>

        <form className="create-form">
          <div className="create-grid">

            <div className="field">
              <label className="field-label">First Name</label>
              <input
                className="field-input"
                type="text"
                placeholder="Enter first name"
                required
              />
            </div>

            <div className="field">
              <label className="field-label">Last Name</label>
              <input
                className="field-input"
                type="text"
                placeholder="Enter last name"
                required
              />
            </div>

            <div className="field">
              <label className="field-label">Email</label>
              <input
                className="field-input"
                type="email"
                placeholder="Enter email"
                required
              />
            </div>

            <div className="field">
              <label className="field-label">Password</label>
              <input
                className="field-input"
                type="password"
                placeholder="Enter password"
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