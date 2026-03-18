import { useEffect, useState } from "react";
import styles from "./Home.module.css";

import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
  Legend,
} from "recharts";

const API = "http://localhost:8080/api";

function Home() {
  const [scans, setScans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [scanning, setScanning] = useState(false);

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

  const handleScan = async () => {
    try {
      setScanning(true);
      await fetch(`${API}/scans/run`, { method: "POST" });
      await loadScans();
    } catch (err) {
      console.error(err);
    } finally {
      setScanning(false);
    }
  };

  const latestScan = scans.length > 0 ? scans[0] : null;
  const recentScans = scans.slice(0, 4);

  const lastScannedText = loading
    ? "Loading..."
    : latestScan
    ? new Date(latestScan.scanDate).toLocaleString()
    : "No scans yet";

  // ✅ Chart data: last 10 scans (oldest -> newest)
  const chartData = scans
    .slice(0, 10)
    .reverse()
    .map((scan) => ({
      name: `#${scan.id}`, // x-axis label
      phishing: scan.phishingUrlsFound,
      safe: scan.safeUrlsFound,
    }));

  return (
    <div className={styles.page}>
      <header className={styles.title}>
        <h1>Homepage</h1>
      </header>

      {/* TOP SUMMARY */}
      <section className={styles.Box}>
        <div className={styles.topRow}>
          <div className={styles.statCard}>
            <div className={styles.statLabel}>Last scanned</div>
            <div className={styles.statValue}>{lastScannedText}</div>
          </div>

          <div className={styles.statCard}>
            <div className={styles.statLabel}>Phishing URLs found</div>
            <div className={styles.statValue}>
              {latestScan ? latestScan.phishingUrlsFound : 0}
            </div>
          </div>
        </div>

        <button
          className={styles.scanButton}
          onClick={handleScan}
          disabled={scanning}
        >
          {scanning ? "Scanning..." : "Scan now"}
        </button>
      </section>

      {/* RECENT SCANS */}
      <section className={styles.Box}>
        <div className={styles.sectionHeader}>
          <h2 className={styles.title2}>Recent scans</h2>
          <p className={styles.helperText}>Go to History to view full details</p>
        </div>

        {recentScans.length === 0 ? (
          <p className={styles.emptyText}>No scans yet.</p>
        ) : (
          <div className={styles.recentGrid}>
            {recentScans.map((scan) => (
              <div className={styles.scanMiniCard} key={scan.id}>
                <div className={styles.scanMiniTop}>
                  <span className={styles.scanMiniTitle}>Scan #{scan.id}</span>
                  <span className={styles.scanMiniDate}>
                    {new Date(scan.scanDate).toLocaleDateString()}
                  </span>
                </div>

                <div className={styles.scanMiniStats}>
                  <div>
                    <b>Total:</b> {scan.totalUrlsScanned}
                  </div>
                  <div className={styles.badPhish}>
                    <b>Phish:</b> {scan.phishingUrlsFound}
                  </div>
                  <div className={styles.badSafe}>
                    <b>Safe:</b> {scan.safeUrlsFound}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      {/* LOWER DASHBOARD */}
      <section className={styles.section}>
        <div className={styles.bottomGrid}>
          <div className={styles.panel}>
            <h2 className={styles.panelTitle}>Analytics</h2>

            <h3>Most recent scan</h3>
            <div className={styles.analyticsCircles}>
              <div className={styles.metric}>
                <div className={styles.circle}>
                  {latestScan ? latestScan.totalUrlsScanned : 0}
                </div>
                <div className={styles.metricLabel}>Total scanned</div>
              </div>

              <div className={styles.metric}>
                <div className={styles.circle}>
                  {latestScan ? latestScan.safeUrlsFound : 0}
                </div>
                <div className={styles.metricLabel}>Safe</div>
              </div>

              <div className={styles.metric}>
                <div className={styles.circle}>
                  {latestScan ? latestScan.phishingUrlsFound : 0}
                </div>
                <div className={styles.metricLabel}>Phishing</div>
              </div>
            </div>
          </div>

          {/* ✅ TRENDS GRAPH */}
          <div className={styles.panel}>
            <h3 className={styles.panelTitle}>Trends (Last 10 scans)</h3>

            {chartData.length === 0 ? (
              <div className={styles.chartPlaceholder}>No scan data yet</div>
            ) : (
              <div style={{ width: "100%", height: 260 }}>
                <ResponsiveContainer>
                  <LineChart data={chartData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line
                      type="monotone"
                      dataKey="phishing"
                      stroke="#d40128"
                      strokeWidth={2}
                      name="Phishing URLs"
                    />
                    <Line
                      type="monotone"
                      dataKey="safe"
                      stroke="#0dbd48"
                      strokeWidth={2}
                      name="Safe URLs"
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            )}
          </div>
        </div>
      </section>
    </div>
  );
}

export default Home;