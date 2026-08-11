import { useEffect, useState } from 'react';
import { downloadCsv, getSummary } from '../api/client';
import type { Summary } from '../types/disaster';

export default function DownloadData() {
  const [summary, setSummary] = useState<Summary | null>(null);
  const [downloading, setDownloading] = useState(false);

  useEffect(() => { getSummary().then(setSummary).catch(() => {}); }, []);

  const handleDownload = async () => {
    setDownloading(true);
    try {
      const blob = await downloadCsv();
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url; a.download = 'disaster_events.csv'; a.click();
      URL.revokeObjectURL(url);
    } finally {
      setDownloading(false);
    }
  };

  return (
    <div>
      <h1 className="page-title">Download Unified Dataset</h1>
      <p className="page-subtitle">
        Download the consolidated disaster_events.csv, ready for machine learning pipelines.
      </p>

      <div className="card" style={{ maxWidth: 520, marginBottom: '1.25rem' }}>
        {summary && (
          <div className="alert alert-info" style={{ marginBottom: '1.25rem', marginTop: 0 }}>
            The dataset currently contains <strong>{summary.total}</strong>
            {' '}event record{summary.total !== 1 ? 's' : ''}.
          </div>
        )}
        <p style={{ fontSize: '0.875rem', color: 'var(--color-text-muted)', marginBottom: '1.25rem' }}>
          The CSV follows the unified hazard-agnostic schema with 15 columns including
          computed fields (<code>duration_days</code>, <code>economic_loss_usd</code>).
        </p>
        <button className="btn btn-primary" onClick={handleDownload} disabled={downloading}>
          {downloading ? 'Preparing…' : '⬇ Download disaster_events.csv'}
        </button>
      </div>

      <div className="card">
        <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.75rem', color: 'var(--color-green)' }}>
          CSV Schema
        </h2>
        <div className="data-table-wrapper">
          <table className="data-table">
            <thead>
              <tr><th>Column</th><th>Type</th><th>Required</th><th>Description</th></tr>
            </thead>
            <tbody>
              {([
                ['event_id',            'string',  'Yes',     'Unique identifier, e.g. BD-FLOOD-2024-001'],
                ['hazard_type',         'string',  'Yes',     'flood | cyclone | drought | river_erosion'],
                ['start_date',          'date',    'Yes',     'YYYY-MM-DD'],
                ['end_date',            'date',    'Yes',     'YYYY-MM-DD, must be ≥ start_date'],
                ['division',            'string',  'Yes',     'One of 8 Bangladesh divisions'],
                ['district',            'string',  'Yes',     'Free text'],
                ['upazila',             'string',  'Yes',     'Free text'],
                ['affected_people',     'integer', 'Yes',     '≥ 0'],
                ['deaths',              'integer', 'Yes',     '≥ 0'],
                ['economic_loss_bdt',   'numeric', 'Yes',     '≥ 0, total loss in BDT'],
                ['economic_loss_usd',   'numeric', 'Derived', '= BDT / 110 if not provided'],
                ['duration_days',       'integer', 'Derived', '= (end − start).days + 1'],
                ['severity_category',   'string',  'Optional','low | medium | high | extreme'],
                ['source_url',          'string',  'Yes',     'URL to source report'],
                ['notes',               'string',  'Optional','Free text'],
              ] as const).map(([col, type, req, desc]) => (
                <tr key={col}>
                  <td><code>{col}</code></td>
                  <td>{type}</td>
                  <td>{req}</td>
                  <td>{desc}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
