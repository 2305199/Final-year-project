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

  <label htmlFor="wname">Website Name:</label><br/>
  <input 
  required
    type="text" 
    id="wname" 
    name="wname" 
    placeholder="Enter Website Name"
  /><br/><br />

  <label htmlFor="url">Website URL:</label><br/>
  <input 
  required
    type="text" 
    id="url" 
    name="url" 
    placeholder="Enter Website URL"
  /><br/><br />

  <label htmlFor="akey">API key:</label><br/>
  <input 
  required
    type="text" 
    id="akey" 
    name="akey" 
    placeholder="Enter API key"
  /><br/><br />

    <input type="submit" value="Submit" />
    <p>If you already have an account <a href="/login"> click here</a> </p>

  </form>
    </div>) }
export default CreateAccount