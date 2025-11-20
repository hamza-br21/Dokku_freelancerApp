// Navbar.jsx (Exemple de composant de navigation)
import React from 'react';
import { Link, useLocation } from 'react-router-dom'; 

const Navbar = () => {
    const location = useLocation(); // Utile pour savoir où nous sommes

    return (
        <nav className="main-nav">
            <div className="container">
                {/* 1. Bouton HOME (racine /) */}
                <Link to="/" className={`nav-btn ${location.pathname === '/' ? 'active' : ''}`}>
                    Accueil
                </Link>
                
                {/* 2. Bouton CONTACT */}
                <Link to="/contact" className={`nav-btn ${location.pathname === '/contact' ? 'active' : ''}`}>
                    Contact
                </Link>

                {/* 3. Bouton Administrateur (si applicable) */}
                {/* Si vous avez une page Admin/Login, vous pouvez l'ajouter ici */}
                <Link to="/admin/login" className={`nav-btn ${location.pathname.startsWith('/admin') ? 'active' : ''}`}>
                    Admin
                </Link>
            </div>
        </nav>
    );
};

export default Navbar;