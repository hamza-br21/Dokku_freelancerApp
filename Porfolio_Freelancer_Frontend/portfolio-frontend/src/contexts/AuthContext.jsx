import React, { createContext, useState, useContext, useEffect } from 'react';
import publicApi from '../api/publicApi';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    // Vérifie l'état de connexion au chargement
    useEffect(() => {
        const token = localStorage.getItem('authToken');
        if (token) {
            // Dans une vraie application, on validerait le token ici (ex: /api/auth/validate)
            setIsAuthenticated(true);
        }
    }, []);

    // Fonction de connexion
    const login = async (username, password) => {
        try {
            const response = await publicApi.post('/auth/login', { username, password });
            
            // Le token est dans response.data.accessToken (selon votre DTO JwtAuthResponse)
            const token = response.data.accessToken; 
            
            localStorage.setItem('authToken', token);
            setIsAuthenticated(true);
            navigate('/admin/dashboard');
        } catch (error) {
            console.error('Erreur de connexion:', error);
            // Gérer les erreurs de login (401 Unauthorized)
            throw new Error('Identifiants incorrects ou erreur serveur.');
        }
    };

    // Fonction de déconnexion
    const logout = () => {
        // 1. Supprimer le token du stockage local
        localStorage.removeItem('authToken');
        setIsAuthenticated(false);

         // 2. Mettre à jour l'état du contexte
       // setToken(null);

        //navigate('/');        
        // 3. Rediriger l'utilisateur vers la page de connexion
        navigate('/admin/login');
        
        // OPTIONNEL: Ajouter ici un appel API pour invalider le token côté serveur 
        // si vous implémentez une liste noire (blacklist), mais ce n'est pas obligatoire pour l'instant.
   
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;