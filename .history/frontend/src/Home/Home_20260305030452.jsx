import { useEffect, useMemo, useState } from "react";
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

  // last 10 scans (newest -> oldest)
  const last10Scans = useMemo(() => scans.slice(0, 10), [scans]);

  // ✅ Totals across last 10 scans for Analytics
  const totalsLast10 = useMemo(() => {
    return last10Scans.reduce(
      (acc, s) => {
        acc.total += s.totalUrlsScanned || 0;
        acc.safe += s.safeUrlsFound || 0;
        acc.phishing += s.phishingUrlsFound || 0;
        return acc;
      },
      { total: 0, safe: 0, phishing: 0 }
    );
  }, [last10Scans]);

  // ✅ Chart data: last 10 scans (oldest -> newest)
  const chartData = useMemo(() => {
    return scans
      .slice(0, 10)
      .reverse()
      .map((scan) => ({
        name: `#${scan.id}`,
        phishing: scan.phishingUrlsFound,
        safe: scan.safeUrlsFound,
      }));
  }, [scans]);

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

          {/* ✅ For latest scan: Total + Safe + Phishing */}
          <div className={styles.statCard}>
            <div className={styles.statLabel}>Most recent scan summary</div>
            <div className={styles.statValue}>
              <div>
                <b>Total:</b> {latestScan ? latestScan.totalUrlsScanned : 0}
              </div>
              <div className={styles.badSafe}>
                <b>Safe:</b> {latestScan ? latestScan.safeUrlsFound : 0}
              </div>
              <div className={styles.badPhish}>
                <b>Phishing:</b> {latestScan ? latestScan.phishingUrlsFound : 0}
              </div>
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
  <div className={styles.topRow}>

    <div className={styles.statCard}>
      <div className={styles.statLabel}>Last scanned</div>
      <div className={styles.statValue}>{lastScannedText}</div>
    </div>

    <div className={styles.statCard}>
      <div className={styles.statLabel}>Total URLs scanned</div>
      <div className={styles.statValue}>
        {latestScan ? latestScan.totalUrlsScanned : 0}
      </div>
    </div>

    <div className={styles.statCard}>
      <div className={styles.statLabel}>Safe URLs</div>
      <div className={`${styles.statValue} ${styles.badSafe}`}>
        {latestScan ? latestScan.safeUrlsFound : 0}
      </div>
    </div>

    <div className={styles.statCard}>
      <div className={styles.statLabel}>Phishing URLs</div>
      <div className={`${styles.statValue} ${styles.badPhish}`}>
        {latestScan ? latestScan.phishingUrlsFound : 0}
      </div>
    </div>

  </div>
      </section>

      {/* LOWER DASHBOARD */}
      <section className={styles.section}>
        <div className={styles.bottomGrid}>
          {/* ✅ Analytics now shows totals across last 10 scans */}
          <div className={styles.panel}>
            <h2 className={styles.panelTitle}>Analytics</h2>
            <h3>Totals (Last 10 scans)</h3>

            <div className={styles.analyticsCircles}>
              <div className={styles.metric}>
                <div className={styles.circle}>{totalsLast10.total}</div>
                <div className={styles.metricLabel}>Total scanned</div>
              </div>

              <div className={styles.metric}>
                <div className={styles.circle}>{totalsLast10.safe}</div>
                <div className={styles.metricLabel}>Safe</div>
              </div>

              <div className={styles.metric}>
                <div className={styles.circle}>{totalsLast10.phishing}</div>
                <div className={styles.metricLabel}>Phishing</div>
              </div>
            </div>
          </div>

          {/* ✅ Trends graph unchanged */}
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