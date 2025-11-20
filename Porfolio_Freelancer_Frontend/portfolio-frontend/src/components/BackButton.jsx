// components/BackButton.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const BackButton = () => {
    // 1. Hook pour accéder à l'historique de navigation
    const navigate = useNavigate();

    return (
        <button 
            className="btn-secondary" 
            onClick={() => navigate(-1)} 
            style={{ marginBottom: '20px' }} // Petite marge pour séparer du contenu
        >
            &larr; Retour
        </button>
    );
};

export default BackButton;