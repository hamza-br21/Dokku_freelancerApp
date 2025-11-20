import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const ProtectedRoute = () => {
    const { isAuthenticated } = useAuth();
    
    // Si l'utilisateur est authentifié, affiche la page demandée (Outlet)
    // Sinon, le redirige vers la page de connexion
    return isAuthenticated ? <Outlet /> : <Navigate to="/admin/login" replace />;
};

export default ProtectedRoute;