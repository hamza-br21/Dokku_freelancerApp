//import './App.css'
// Dans src/main.jsx (ou App.jsx si vous utilisez create-react-app)
import React, { createContext, useState, useContext } from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AuthProvider, { useAuth } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

import Navbar from './components/Navbar';

import HomePage from './pages/HomePage';
import ContactPage from './pages/ContactPage';
import AdminLogin from './pages/AdminLogin';
import AdminDashboard from './pages/AdminDashboard';
// Importez vos autres composants (Header, Footer, MessageList, ProjectForm, etc.)
import ProjectForm from './pages/ProjectForm'; 
import MessageList from './pages/MessageList';

// --- NOUVEAU COMPOSANT : AppContent ---
// Ce composant est à l'intérieur de AuthProvider et peut donc appeler useAuth().
const AppContent = () => {
    // 🎯 CORRECTION : Utilisez le hook useAuth() pour obtenir le statut.
    // (Assurez-vous que votre useAuth() expose bien une propriété nommée 'isAuthenticated')
    const { isAuthenticated } = useAuth(); 

    return (
        <>
            {/* 🎯 LOGIQUE CONDITIONNELLE CORRIGÉE : Afficher Navbar si NON authentifié */}
            {!isAuthenticated && <Navbar />}
            
            <div className="container">
                <Routes>
                    {/* Routes publiques */}
                    <Route path="/" element={<HomePage />} />
                    <Route path="/contact" element={<ContactPage />} />
                    <Route path="/admin/login" element={<AdminLogin />} />

                    {/* Routes sécurisées */}
                    <Route element={<ProtectedRoute />}>
                        <Route path="/admin/dashboard" element={<AdminDashboard />} />
                        <Route path="/admin/messages" element={<MessageList />} />                   
                        <Route path="/admin/projets/new" element={<ProjectForm />} />
                        <Route path="/admin/projets/edit/:id" element={<ProjectForm />} /> 
                    </Route>
                    
                    <Route path="*" element={<h1>404 - Page non trouvée</h1>} />
                </Routes>
            </div>
        </>
    );
};
// -------------------------------------
function App() {
    return (
        // 1. Le Router DOIT être le wrapper le plus à l'extérieur
        <Router>
            {/* 2. L'AuthProvider (qui utilise useNavigate) doit être DANS le Router */}
            <AuthProvider>
                <AppContent />
            </AuthProvider>
        </Router>
    );
}

export default App;