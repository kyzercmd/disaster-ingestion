export default function About() {
  return (
    <div style={{ maxWidth: 720 }}>
      <h1 className="page-title">About This System</h1>
      <p className="page-subtitle">Prototype Unified Disaster Data Ingestion System · Bangladesh</p>

      <div className="card" style={{ marginBottom: '1.25rem' }}>
        <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.75rem', color: 'var(--color-green)' }}>Purpose</h2>
        <p style={{ fontSize: '0.9rem', color: 'var(--color-text-muted)', lineHeight: 1.8 }}>
          This prototype demonstrates how a unified, hazard-agnostic dataset for Bangladesh disaster
          events (floods, cyclones, droughts, riverbank erosion) can be created, validated, and
          maintained in a single CSV file ready for machine learning pipelines and data analytics.
        </p>
      </div>

      <div className="card" style={{ marginBottom: '1.25rem' }}>
        <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.75rem', color: 'var(--color-green)' }}>Technical Architecture</h2>
        <ul style={{ fontSize: '0.875rem', color: 'var(--color-text-muted)', lineHeight: 2, paddingLeft: '1.25rem' }}>
          <li><strong>Frontend:</strong> React 18 + TypeScript + Vite, government-portal style UI</li>
          <li><strong>Backend:</strong> Java 17 + Spring Boot 3, REST API over HTTP</li>
          <li><strong>Storage:</strong> Flat-file CSV (no database required for prototype)</li>
          <li><strong>Validation:</strong> Multi-threaded via fixed thread pool (4 threads), each row a <code>Callable&lt;ValidationResult&gt;</code></li>
          <li><strong>OOP — Encapsulation:</strong> <code>DisasterEvent</code> with private fields + getters/setters</li>
          <li><strong>OOP — Abstraction:</strong> <code>EventRepository</code>, <code>EventValidationService</code>, <code>EventValidator</code> interfaces</li>
          <li><strong>OOP — Inheritance:</strong> <code>DisasterDataException</code> → <code>InvalidEventException</code>, <code>CsvParseException</code>, <code>StorageException</code></li>
          <li><strong>OOP — Polymorphism:</strong> <code>BasicEventValidator</code> and <code>StrictEventValidator</code> implement <code>EventValidator</code></li>
        </ul>
      </div>

      <div className="card">
        <h2 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.75rem', color: 'var(--color-green)' }}>Data Sources</h2>
        <p style={{ fontSize: '0.875rem', color: 'var(--color-text-muted)', lineHeight: 1.8 }}>
          Records can be sourced from EM-DAT, BDRCS situation reports, government district-level
          damage assessment reports, news articles, and other credible sources. All source URLs
          are stored alongside each record for traceability.
        </p>
      </div>
    </div>
  );
}
