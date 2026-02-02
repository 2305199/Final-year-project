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
            <div className={styles.topRow}>
            <div className={styles.Lscanned}><p>Scan 3 details</p></div>
             <div className={styles.Lscanned}><p>Scan 3 details</p></div>
             </div>
        </div>
    </div>
          {/* Lower dashboard blocks */}
      <section className={styles.section}>
        <div className={styles.bottomGrid}>
            
          {/* Analytics numbers */}
          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Analytics</h3>
            <div className={styles.analyticsCircles}>
              <div className={styles.circle}>50</div>
              <div className={styles.circle}>40</div>
              <div className={styles.circle}>10</div>
            </div>
          </div>

          {/* Chart placeholder */}
          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Trends</h3>
            <div className={styles.chartPlaceholder}>Graph goes here</div>
          </div>

          {/* History */}
          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>History</h3>
            <div className={styles.panelBody}>View scan history</div>
          </div>

          {/* Map */}
          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Map</h3>
            <div className={styles.panelBody}>Map location of phishing URLs</div>
          </div>
        </div>
      </section>

    </div>)
}
export default Home