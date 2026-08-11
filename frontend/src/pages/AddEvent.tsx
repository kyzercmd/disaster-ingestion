import { useState } from 'react';
import { addEvent } from '../api/client';
import type { DisasterEvent } from '../types/disaster';

const DIVISIONS = ['Barishal','Chattogram','Dhaka','Khulna','Mymensingh','Rajshahi','Rangpur','Sylhet'];
const HAZARD_TYPES = [
  { value: 'flood',         label: 'Flood (বন্যা)' },
  { value: 'cyclone',       label: 'Cyclone (ঘূর্ণিঝড়)' },
  { value: 'drought',       label: 'Drought (খরা)' },
  { value: 'river_erosion', label: 'River Erosion (নদীভাঙন)' },
];
const SEVERITY = ['low', 'medium', 'high', 'extreme'];

const EMPTY: Partial<DisasterEvent> = { hazardType: 'flood', division: 'Dhaka' };

export default function AddEvent() {
  const [form, setForm] = useState<Partial<DisasterEvent>>(EMPTY);
  const [status, setStatus] = useState<'idle'|'loading'|'success'|'error'>('idle');
  const [message, setMessage] = useState('');

  const set = (field: keyof DisasterEvent, value: unknown) =>
    setForm(prev => ({ ...prev, [field]: value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setStatus('loading');
    try {
      await addEvent(form as DisasterEvent);
      setStatus('success');
      setMessage(`Event "${form.eventId}" added successfully.`);
      setForm(EMPTY);
    } catch (err: unknown) {
      setStatus('error');
      const msg = (err as { response?: { data?: { error?: string } } })
        ?.response?.data?.error ?? 'Failed to add event.';
      setMessage(msg);
    }
  };

  return (
    <div>
      <h1 className="page-title">Add Disaster Event</h1>
      <p className="page-subtitle">Fill in all required fields to submit a new disaster record.</p>

      <div className="card">
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label htmlFor="eventId">Event ID *</label>
              <input id="eventId" type="text" placeholder="BD-FLOOD-2024-001"
                value={form.eventId ?? ''} onChange={e => set('eventId', e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="hazardType">Hazard Type *</label>
              <select id="hazardType" value={form.hazardType ?? ''}
                onChange={e => set('hazardType', e.target.value as DisasterEvent['hazardType'])} required>
                {HAZARD_TYPES.map(h => <option key={h.value} value={h.value}>{h.label}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="startDate">Start Date *</label>
              <input id="startDate" type="date" value={form.startDate ?? ''}
                onChange={e => set('startDate', e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="endDate">End Date *</label>
              <input id="endDate" type="date" value={form.endDate ?? ''}
                onChange={e => set('endDate', e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="division">Division *</label>
              <select id="division" value={form.division ?? ''}
                onChange={e => set('division', e.target.value)} required>
                {DIVISIONS.map(d => <option key={d} value={d}>{d}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="district">District *</label>
              <input id="district" type="text" placeholder="e.g. Sunamganj"
                value={form.district ?? ''} onChange={e => set('district', e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="upazila">Upazila *</label>
              <input id="upazila" type="text" placeholder="e.g. Dowarabazar"
                value={form.upazila ?? ''} onChange={e => set('upazila', e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="affectedPeople">Affected People *</label>
              <input id="affectedPeople" type="number" min={0} placeholder="0"
                value={form.affectedPeople ?? ''}
                onChange={e => set('affectedPeople', Number(e.target.value))} required />
            </div>
            <div className="form-group">
              <label htmlFor="deaths">Deaths *</label>
              <input id="deaths" type="number" min={0} placeholder="0"
                value={form.deaths ?? ''}
                onChange={e => set('deaths', Number(e.target.value))} required />
            </div>
            <div className="form-group">
              <label htmlFor="economicLossBdt">Economic Loss (BDT) *</label>
              <input id="economicLossBdt" type="number" min={0} step="any" placeholder="0"
                value={form.economicLossBdt ?? ''}
                onChange={e => set('economicLossBdt', Number(e.target.value))} required />
            </div>
            <div className="form-group">
              <label htmlFor="severityCategory">Severity Category</label>
              <select id="severityCategory" value={form.severityCategory ?? ''}
                onChange={e => set('severityCategory', e.target.value || undefined)}>
                <option value="">— optional —</option>
                {SEVERITY.map(s => <option key={s} value={s}>{s}</option>)}
              </select>
            </div>
            <div className="form-group full-width">
              <label htmlFor="sourceUrl">Source URL *</label>
              <input id="sourceUrl" type="url" placeholder="https://example.com/report"
                value={form.sourceUrl ?? ''} onChange={e => set('sourceUrl', e.target.value)} required />
            </div>
            <div className="form-group full-width">
              <label htmlFor="notes">Notes</label>
              <textarea id="notes" placeholder="Additional details (optional)..."
                value={form.notes ?? ''} onChange={e => set('notes', e.target.value)} />
            </div>
          </div>

          {status !== 'idle' && (
            <div className={`alert alert-${
              status === 'success' ? 'success' : status === 'loading' ? 'info' : 'error'
            }`}>
              {status === 'loading' ? 'Submitting...' : message}
            </div>
          )}

          <div style={{ marginTop: '1.25rem' }}>
            <button type="submit" className="btn btn-primary" disabled={status === 'loading'}>
              {status === 'loading' ? 'Saving…' : 'Submit Event'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
