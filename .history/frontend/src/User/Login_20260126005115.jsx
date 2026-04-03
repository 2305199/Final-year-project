function Login() {
    return (<div> 
    <h1>Welcome to the Login Page</h1>

      <label htmlFor="email">Email:</label><br/>
  <input 
  required
    type="text" 
    id="email" 
    name="email" 
    placeholder="Enter Email"
  /><br/><br />

    <label htmlFor="password">Password:</label><br/>
  <input 
  required
    type="text" 
    id="password" 
    name="password" 
    placeholder="Enter Password"
  /><br/><br />

    <button>Login</button>
    

    </div>)  }
export default Login