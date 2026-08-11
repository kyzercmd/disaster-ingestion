import { useEffect, useState } from 'react';
import { getSummary } from '../api/client';
import type { Summary } from '../types/disaster';
import { Link } from 'react-router-dom';

export default function Home() {
  const [summary, setSummary] = useState<Summary | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    getSummary()
      .then(setSummary)
      .catch(() => setError('Could not load summary. Is the backend running on port 8080?'));
  }, []);

  const HAZARD_LABELS: Record<string, string> = {
    flood: 'Floods', cyclone: 'Cyclones', drought: 'Droughts', river_erosion: 'River Erosion'
  };

  return (
    <div>
      <h1 className="page-title">Bangladesh Disaster Event Repository</h1>
      <p className="page-subtitle">
        Unified hazard-agnostic dataset for floods, cyclones, droughts, and riverbank erosion.
        Add records manually or upload a CSV for parallel validation.
      </p>

      {error && <div className="alert alert-error">{error}</div>}

      {summary && (
        <>
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-value">{summary.total}</div>
              <div className="stat-label">Total Events</div>
            </div>
            {Object.entries(summary.byHazardType).map(([k, v]) => (
              <div className="stat-card" key={k}>
                <div className="stat-value">{v}</div>
                <div className="stat-label">{HAZARD_LABELS[k] ?? k}</div>
              </div>
            ))}
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.25rem' }}>
            <div className="card">
              <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.9rem', color: 'var(--color-green)' }}>By Division</h2>
              {Object.keys(summary.byDivision).length === 0
                ? <p style={{ color: 'var(--color-text-muted)', fontSize: '0.875rem' }}>No data yet.</p>
                : <table style={{ width: '100%', fontSize: '0.875rem' }}>
                    <tbody>
                      {Object.entries(summary.byDivision).map(([div, cnt]) => (
                        <tr key={div}>
                          <td style={{ padding: '0.3rem 0', color: 'var(--color-text-muted)' }}>{div}</td>
                          <td style={{ padding: '0.3rem 0', textAlign: 'right', fontWeight: 700 }}>{cnt}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
              }
            </div>
            <div className="card">
              <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.9rem', color: 'var(--color-green)' }}>By Year</h2>
              {Object.keys(summary.byYear).length === 0
                ? <p style={{ color: 'var(--color-text-muted)', fontSize: '0.875rem' }}>No data yet.</p>
                : <table style={{ width: '100%', fontSize: '0.875rem' }}>
                    <tbody>
                      {Object.entries(summary.byYear).sort().reverse().map(([yr, cnt]) => (
                        <tr key={yr}>
                          <td style={{ padding: '0.3rem 0', color: 'var(--color-text-muted)' }}>{yr}</td>
                          <td style={{ padding: '0.3rem 0', textAlign: 'right', fontWeight: 700 }}>{cnt}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
              }
            </div>
          </div>
        </>
      )}

      <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
        <Link to="/add-event" className="btn btn-primary">+ Add Event</Link>
        <Link to="/upload"    className="btn btn-outline">Upload CSV</Link>
        <Link to="/download"  className="btn btn-outline">Download Data</Link>
      </div>
    </div>
  );
}
