import { useEffect, useState } from "react";
import styles from "./Home.module.css";

const API = "http://localhost:8080/api";

function Home() {
  const [scans, setScans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [scanning, setScanning] = useState(false);

  // Load scans
  const loadScans = async () => {
    try {
      const res = await fetch(`${API}/scans`);
      const data = await res.json();
      setScans(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadScans();
  }, []);

  // Run new scan
  const handleScan = async () => {
    try {
      setScanning(true);
      await fetch(`${API}/scans/run`, {
        method: "POST",
      });
      await loadScans(); // refresh after scan
    } catch (err) {
      console.error(err);
    } finally {
      setScanning(false);
    }
  };

  const latestScan = scans.length > 0 ? scans[0] : null;
  const recentScans = scans.slice(0, 4);

  return (
    <div>
      <div className={styles.title}>
        <h1>Welcome to the Home Page</h1>
      </div>

      <div className={styles.Box}>
        <div className={styles.topRow}>
          <div className={styles.Lscanned}>
            <p>
              Last Scanned:{" "}
              {loading
                ? "Loading..."
                : latestScan
                ? new Date(latestScan.scanDate).toLocaleString()
                : "No scans yet"}
            </p>
          </div>

          <div className={styles.Pfound}>
            <p>
              Phishing URLs found:{" "}
              {latestScan ? latestScan.phishingUrlsFound : 0}
            </p>
          </div>
        </div>

        <button
          className={styles.scanButton}
          onClick={handleScan}
          disabled={scanning}
        >
          {scanning ? "Scanning..." : "Scan now"}
        </button>
      </div>

      <div className={styles.Box}>
        <h2 className={styles.title2}>Recent scans</h2>

        <div className={styles.recentScans}>
          <div className={styles.topRow}>
            {recentScans.slice(0, 2).map((scan) => (
              <div className={styles.Lscanned} key={scan.id}>
                <p>
                  Scan #{scan.id}
                  <br />
                  Total: {scan.totalUrlsScanned}
                  <br />
                  Phish: {scan.phishingUrlsFound}
                  <br />
                  Safe: {scan.safeUrlsFound}
                </p>
              </div>
            ))}
          </div>

          <div className={styles.topRow}>
            {recentScans.slice(2, 4).map((scan) => (
              <div className={styles.Lscanned} key={scan.id}>
                <p>
                  Scan #{scan.id}
                  <br />
                  Total: {scan.totalUrlsScanned}
                  <br />
                  Phish: {scan.phishingUrlsFound}
                  <br />
                  Safe: {scan.safeUrlsFound}
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Lower dashboard blocks */}
      <section className={styles.section}>
        <div className={styles.bottomGrid}>
          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Analytics</h3>
            <div className={styles.analyticsCircles}>
              <div className={styles.circle}>
                {latestScan ? latestScan.totalUrlsScanned : 0}
              </div>
              <div className={styles.circle}>
                {latestScan ? latestScan.safeUrlsFound : 0}
              </div>
              <div className={styles.circle}>
                {latestScan ? latestScan.phishingUrlsFound : 0}
              </div>
            </div>
          </div>

          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Trends</h3>
            <div className={styles.chartPlaceholder}>Graph goes here</div>
          </div>

          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>History</h3>
            <div className={styles.panelBody}>View scan history</div>
          </div>

          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Map</h3>
            <div className={styles.panelBody}>
              Map location of phishing URLs
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Home;