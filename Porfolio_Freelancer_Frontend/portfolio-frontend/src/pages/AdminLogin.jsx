import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';

const AdminLogin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth(); 

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await login(username, password);
        
        } catch (err) {
            setError(err.message || 'Échec de la connexion. Vérifiez vos identifiants.');
        }
    };

    return (
            <div 
            className="login-container"
            style={{ 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center', 
            padding: '40px', 
            maxWidth: '400px', 
            margin: '10vh auto 0', 
            border: '1px solid #ccc', 
            borderRadius: '8px', 
            boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
            
           }}>

            <h1 style={{ color: '#007bff' }}>Connexion Admin</h1>
            <form onSubmit={handleSubmit} style={{ width: '100%', border: 'none', padding: '0', backgroundColor: 'transparent' }}>
                <input type="text" placeholder="Nom d'utilisateur" value={username} onChange={(e) => setUsername(e.target.value)} required />
                <input type="password" placeholder="Mot de passe" value={password} onChange={(e) => setPassword(e.target.value)} required />
                <button type="submit" className="btn-primary" style={{ marginTop: '10px' }}>Se connecter</button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default AdminLogin;