import { BrowserRouter, Routes, Route, NavLink } from 'react-router-dom';
import Home         from './pages/Home';
import AddEvent     from './pages/AddEvent';
import UploadCsv    from './pages/UploadCsv';
import DownloadData from './pages/DownloadData';
import About        from './pages/About';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="container">
        <a href="/" className="navbar-brand">
          <span className="flag">🇧🇩</span>
          <span className="brand-text">
            <span>Disaster Ingestion System</span>
            <span className="brand-sub">দুর্যোগ তথ্য সংগ্রহ ব্যবস্থা · Bangladesh</span>
          </span>
        </a>
        <ul className="navbar-nav">
          <li><NavLink to="/" end>Home</NavLink></li>
          <li><NavLink to="/add-event">Add Event</NavLink></li>
          <li><NavLink to="/upload">Upload CSV</NavLink></li>
          <li><NavLink to="/download">Download</NavLink></li>
          <li><NavLink to="/about">About</NavLink></li>
        </ul>
      </div>
    </nav>
  );
}

function Footer() {
  return (
    <footer className="footer">
      © 2024 Bangladesh Disaster Management Portal · Unified Data Ingestion Prototype
    </footer>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <main className="page-wrapper">
        <div className="container">
          <Routes>
            <Route path="/"           element={<Home />} />
            <Route path="/add-event"  element={<AddEvent />} />
            <Route path="/upload"     element={<UploadCsv />} />
            <Route path="/download"   element={<DownloadData />} />
            <Route path="/about"      element={<About />} />
          </Routes>
        </div>
      </main>
      <Footer />
    </BrowserRouter>
  );
}
