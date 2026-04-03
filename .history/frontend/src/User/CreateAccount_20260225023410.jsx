import "./CreateAccount.css";

function CreateAccount() {
  return (
    <div className="create-page">
      <div className="create-card">

        <h1 className="create-title">Create Account</h1>
        <p className="create-subtitle">
          Sign up to start scanning URLs and tracking results.
        </p>

        <form className="create-form">

          <div className="field">
            <label htmlFor="fname" className="create-label">
              First Name
            </label>
            <input
              required
              type="text"
              id="fname"
              name="fname"
              placeholder="Enter First Name"
              className="create-input"
            />
          </div>

          <div className="field">
            <label htmlFor="lname" className="create-label">
              Last Name
            </label>
            <input
              required
              type="text"
              id="lname"
              name="lname"
              placeholder="Enter Last Name"
              className="create-input"
            />
          </div>

          <div className="field">
            <label htmlFor="email" className="create-label">
              Email
            </label>
            <input
              required
              type="email"
              id="email"
              name="email"
              placeholder="Enter Email"
              className="create-input"
            />
          </div>

          <div className="field">
            <label htmlFor="password" className="create-label">
              Password
            </label>
            <input
              required
              type="password"
              id="password"
              name="password"
              placeholder="Enter Password"
              className="create-input"
            />
          </div>

          <button type="submit" className="create-btn">
            Create Account
          </button>

          <div className="create-footer">
            If you already have an account{" "}
            <a href="/login" className="create-link">
              click here
            </a>
          </div>

        </form>
      </div>
    </div>
  );
}

export default CreateAccount;