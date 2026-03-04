import { useEffect, useState } from "react";
import "./History.css";

 function History() {
  const [reports, setReports] = useState([]);
  const [selected, setSelected] = useState(null);
  const [urls, setUrls] = useState([]);
  const [loading, setLoading] = useState(false);

  // Load all reports
  useEffect(() => {
    fetch("http://localhost:8080/api/reports")
      .then((res) => res.json())
      .then(setReports)
      .catch(console.error);
  }, []);

  // Load URLs for a scan
  const openReport = async (report) => {
    setSelected(report);
    setLoading(true);

    try {
      const res = await fetch(
        `http://localhost:8080/api/reports/${report.scanId}/urls`
      );
      const data = await res.json();
      setUrls(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const closeReport = () => {
    setSelected(null);
    setUrls([]);
  };

  return (
    <div className="history-page">
      <header className="history-header">
        <h1 className="app-title">HISTORY</h1>
      </header>


      <div className="reports-container">
        {reports.length === 0 ? (
          <p className="empty-text">No reports yet.</p>
        ) : (
          reports.map((r) => (
            <div className="report-card" key={r.reportId}>
              <div className="report-left">
                <div className="report-date">
                  <span className="label">Date:</span> {r.reportDate}
                </div>
                <div className="report-stats">
                  <span className="stat">
                    <span className="label">Total:</span> {r.total}
                  </span>
                  <span className="stat phishing">
                    <span className="label">Phishing:</span> {r.phishing}
                  </span>
                  <span className="stat safe">
                    <span className="label">Safe:</span> {r.safe}
                  </span>
                </div>
              </div>

              <button className="view-btn" onClick={() => openReport(r)}>
                View
              </button>
            </div>
          ))
        )}
      </div>

      {selected && (
        <div className="modal-overlay" onClick={closeReport}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 className="modal-title">
                Report Details (Scan #{selected.scanId})
              </h3>
              <button className="close-btn" onClick={closeReport}>
                ✕
              </button>
            </div>

            {loading ? (
              <p className="loading-text">Loading URLs...</p>
            ) : (
              <div className="url-table-wrapper">
                <table className="url-table">
                  <thead>
                    <tr>
                      <th>URL</th>
                      <th>Result</th>
                    </tr>
                  </thead>
                  <tbody>
                    {urls.map((u) => (
                      <tr key={u.urlId}>
                        <td className="url-cell">{u.url}</td>
                        <td>
                          <span
                            className={
                              u.result === "phishing"
                                ? "badge badge-phishing"
                                : "badge badge-safe"
                            }
                          >
                            {u.result}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
export default History;