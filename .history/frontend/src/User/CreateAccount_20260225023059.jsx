function CreateAccount() {
    return (<div> 
   
       <h1>Create Account</h1>

   <form>
  <label htmlFor="fname">First Name:</label><br/>
  <input 
  required
    type="text" 
    id="fname" 
    name="fname" 
    placeholder="Enter First Name"
  /><br/><br />

  <label htmlFor="lname">Last Name:</label><br/>
  <input 
  required
    type="text" 
    id="lname" 
    name="lname" 
    placeholder="Enter Last Name"
  /><br/><br />

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


    <input type="submit" value="Submit" />
    <p>If you already have an account <a href="/login"> click here</a> </p>

  </form>
    </div>) }
export default CreateAccount