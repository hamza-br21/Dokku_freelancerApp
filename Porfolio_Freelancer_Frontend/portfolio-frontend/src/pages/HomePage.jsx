import React, { useState, useEffect } from 'react';
import publicApi from '../api/publicApi';
import ProjectForm from './ProjectForm';
// Importez votre composant ProjectCard ici une fois créé
import ProjectCard from './ProjectCard';

const HomePage = () => {
    const [projets, setProjets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProjets = async () => {
            try {
                const response = await publicApi.get('/projets'); // GET /api/projets
                setProjets(response.data);
            } catch (error) {
                console.error("Erreur lors de la récupération des projets:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchProjets();
    }, []);

    if (loading) return <div className="container">Chargement du portfolio...</div>;

    return (
        <div className="container">
            <h1 style={{ textAlign: 'center', color: '#333', marginBottom: '10px' }}>Portfolio Hamza.ber</h1>
           {/*  <p style={{ textAlign: 'center', color: '#666', fontSize: '1.2em' }}>Hamza, mon métier, mon slogan.</p>*/}
            <p style={{ textAlign: 'center', color: '#666', fontSize: '1.2em' }}>Hamza, __, __.</p>
            <hr style={{ margin: '30px 0' }}/>

             <h2>Mes Projets Réalisés</h2>

            <section className="project-gallery">
                {projets.length > 0 ? (
                    projets.map(projet => (
                       
                       <ProjectCard key={projet.id} projet={projet} />
                    ))
                ) : (
                    <p>Aucun projet à afficher pour le moment.</p>
                )}
            </section>
        </div>
    );
};

export default HomePage;