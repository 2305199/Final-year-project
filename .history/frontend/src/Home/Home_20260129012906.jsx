import styles from './Home.module.css'
function Home() {
    return ( <div>
        <div className={styles.title}> <h1>Welcome to the Home Page</h1></div>
   
   
<div className={styles.Box}>
  <div className={styles.topRow}>
    <div className={styles.Lscanned}>
      <p>Last Scanned:</p>
    </div>

    <div className={styles.Pfound}>
      <p>Phishing URLs found:</p>
    </div>
  </div>

  <button className={styles.scanButton}>Scan now</button>
</div>



    <div className={styles.Box}> 
        <h2>Recent scans</h2>
        <div className={styles.recentScans}>
            <div className={styles.topRow}>
            <div className={styles.Lscanned}><p>Scan 1 details</p></div>
            <div className={styles.Lscanned}><p>Scan 2 details</p></div>
            </div>
            <div className={styles.Lscanned}><p>Scan 3 details</p></div>
             <div className={styles.Lscanned}><p>Scan 3 details</p></div>
        </div>
    </div>

    </div>)
}
export default Home