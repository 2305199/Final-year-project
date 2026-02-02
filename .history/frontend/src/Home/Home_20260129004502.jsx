import styles from './Home.module.css'
function Home() {
    return (     <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>PHISHING WEBSITE</h1>
      </header>

      {/* Top summary section */}
      <section className={styles.topSection}>
        <div className={styles.summaryGrid}>
          <div className={styles.summaryCard}>
            <p className={styles.cardLabel}>Phishing URLs detected</p>
            <p className={styles.cardValue}>{phishingCount}</p>
          </div>

          <div className={styles.summaryCard}>
            <p className={styles.cardLabel}>Last scanned</p>
            <p className={styles.cardValue}>{lastScanned}</p>
          </div>

          <button className={styles.scanButton}>SCAN NOW</button>
        </div>
      </section>

      {/* Recent scans */}
      <section className={styles.section}>
        <h2 className={styles.sectionTitle}>Most recent scans</h2>
        <div className={styles.recentScansGrid}>
          {recentScans.map((scan) => (
            <div key={scan.id} className={styles.scanCard}>
              <p className={styles.scanTitle}>{scan.title}</p>
              <p className={styles.scanDetails}>{scan.details}</p>
            </div>
          ))}
        </div>
      </section>

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