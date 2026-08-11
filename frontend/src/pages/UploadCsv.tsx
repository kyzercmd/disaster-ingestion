import { useRef, useState } from 'react';
import { uploadCsv } from '../api/client';
import type { UploadResult } from '../types/disaster';

export default function UploadCsv() {
  const inputRef = useRef<HTMLInputElement>(null);
  const [file, setFile] = useState<File | null>(null);
  const [over, setOver] = useState(false);
  const [status, setStatus] = useState<'idle'|'loading'|'done'|'error'>('idle');
  const [result, setResult] = useState<UploadResult | null>(null);
  const [errorMsg, setErrorMsg] = useState('');

  const handleFile = (f: File) => { setFile(f); setStatus('idle'); setResult(null); };

  const handleUpload = async () => {
    if (!file) return;
    setStatus('loading');
    try {
      const res = await uploadCsv(file);
      setResult(res);
      setStatus('done');
    } catch (err: unknown) {
      setStatus('error');
      setErrorMsg(
        (err as { response?: { data?: { error?: string } } })?.response?.data?.error ??
        'Upload failed.'
      );
    }
  };

  const SAMPLE_CSV = [
    'event_id,hazard_type,start_date,end_date,division,district,upazila,affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days,severity_category,source_url,notes',
    'BD-FLOOD-2024-001,flood,2024-06-01,2024-06-15,Dhaka,Manikganj,Singair,5000,3,1000000,,,high,https://example.com/flood2024,Major flooding event',
    'BD-CYC-2024-001,cyclone,2024-05-20,2024-05-22,Barishal,Barguna,Amtali,10000,15,2000000,,,extreme,https://example.com/cyc2024,'
  ].join('\n');

  const downloadSample = () => {
    const blob = new Blob([SAMPLE_CSV], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url; a.download = 'sample_disaster_events.csv'; a.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div>
      <h1 className="page-title">Upload Disaster CSV</h1>
      <p className="page-subtitle">
        Upload a CSV file following the unified schema. Rows are validated in parallel (4 threads).
      </p>

      <div className="card" style={{ marginBottom: '1.25rem' }}>
        <div
          className={`drop-zone${over ? ' over' : ''}`}
          onClick={() => inputRef.current?.click()}
          onDragOver={e => { e.preventDefault(); setOver(true); }}
          onDragLeave={() => setOver(false)}
          onDrop={e => {
            e.preventDefault(); setOver(false);
            const f = e.dataTransfer.files[0];
            if (f) handleFile(f);
          }}
        >
          <input
            ref={inputRef} type="file" accept=".csv"
            onChange={e => { const f = e.target.files?.[0]; if (f) handleFile(f); }}
          />
          {file
            ? <><strong style={{ color: 'var(--color-green)' }}>📄 {file.name}</strong><br />
                <small>{(file.size / 1024).toFixed(1)} KB selected</small></>
            : <><strong>Click to choose or drag &amp; drop a CSV</strong><br />
                <small>Accepted: .csv files only</small></>
          }
        </div>

        <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem', alignItems: 'center' }}>
          <button className="btn btn-primary" onClick={handleUpload}
            disabled={!file || status === 'loading'}>
            {status === 'loading' ? 'Uploading…' : 'Upload & Validate'}
          </button>
          <button className="btn btn-outline" onClick={downloadSample}>
            Download Sample CSV
          </button>
        </div>
      </div>

      {status === 'error' && <div className="alert alert-error">{errorMsg}</div>}

      {status === 'done' && result && (
        <div className="card">
          <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.9rem', color: 'var(--color-green)' }}>
            Upload Results
          </h2>
          <div className="stats-grid" style={{ marginBottom: '1rem' }}>
            <div className="stat-card">
              <div className="stat-value" style={{ color: 'var(--color-green)' }}>{result.accepted}</div>
              <div className="stat-label">Accepted</div>
            </div>
            <div className="stat-card">
              <div className="stat-value" style={{ color: 'var(--color-red)' }}>{result.rejected}</div>
              <div className="stat-label">Rejected</div>
            </div>
          </div>
          {result.errors.length > 0 && (
            <>
              <p style={{ fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.5rem', color: '#a01a28' }}>
                Validation Errors:
              </p>
              <ul className="error-list">
                {result.errors.map((err, i) => <li key={i}>{err}</li>)}
              </ul>
            </>
          )}
        </div>
      )}
    </div>
  );
}
