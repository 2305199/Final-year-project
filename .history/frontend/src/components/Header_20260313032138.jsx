import { Link, NavLink } from "react-router-dom";
import styles from "./Header.module.css";

function Header() {
  return (
    <nav className={styles.navbar}>
      
      {/* Logo */}
      <Link to="/" className={styles.logo}>
        Phishing <span className={styles.logoAccent}>Website</span>
      </Link>

      {/* Navigation */}
      <ul className={styles.navLinks}>
        <li>
          <NavLink to="/" className={({ isActive }) => isActive ? styles.activeLink : ""}>
            Home
          </NavLink>
        </li>

        <li>
          <NavLink to="/location" className={({ isActive }) => isActive ? styles.activeLink : ""}>
            Location
          </NavLink>
        </li>

        <li>
          <NavLink to="/history" className={({ isActive }) => isActive ? styles.activeLink : ""}>
            History
          </NavLink>
        </li>

      </ul>
    </nav>
  );
}

export default Header;