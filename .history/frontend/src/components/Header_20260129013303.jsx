function Header() {
    return (   <nav className={styles.navbar}>
      <h1 className={styles.logo}>
        <a href="/">Phishing Website</a>
      </h1>

      <ul className={styles.navLinks}>
        <li><a href="/">Home</a></li>
        <li><a href="/location">Location</a></li>
        <li><a href="/history">History</a></li>
        <li><a href="/createAccount">Create Account</a></li>
        <li><a href="/login">Login</a></li>
      </ul>
    </nav>)

}
export default Header