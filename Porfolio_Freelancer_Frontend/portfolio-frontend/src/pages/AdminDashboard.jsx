import React, { useState, useEffect } from 'react';
import securedApi from '../api/securedApi';
import { useAuth } from '../contexts/AuthContext';

import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
/*
You use navigate() to redirect after un clic ou une action admin (edit, delete, etc.).
You use <Link> for liens dans le tableau ou la barre de navigation (ex: “Voir les messages”).
*/ 

const AdminDashboard = () => {
    const navigate = useNavigate();
    const [projets, setProjets] = useState([]);
    const [messagesCount, setMessagesCount] = useState(0);
    const [loading, setLoading] = useState(true);
    const { logout } = useAuth();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            // Récupérer les projets
            const projetsResponse = await securedApi.get('/projets');
            setProjets(projetsResponse.data);

            // Récupérer les messages(pour le compte)  (exemple de GET sécurisé)
            const messagesResponse = await securedApi.get('/messages'); // GET /api/messages
            setMessagesCount(messagesResponse.data.length);

        } catch (error) {
            console.error("Erreur de récupération des données admin:", error);
            // Si l'erreur est 401/403 (Token expiré), on déconnecte l'utilisateur
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                logout();
            }
        }finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Êtes-vous sûr de vouloir supprimer ce projet ?")) return;

        try {
            await securedApi.delete(`/projets/${id}`); // DELETE /api/projets/{id}
            alert("Projet supprimé avec succès !");
            //fetchData(); // Rafraîchir la liste
            // Mettre à jour la liste sans recharger la page
                setProjets(projets.filter(p => p.id !== id));
        } catch (error) {
            console.error("Erreur de suppression:", error);
            alert("Erreur lors de la suppression du projet.");
        } finally {
                  // re-synchroniser pour être sûr (optionnel selon coût)
                    fetchData();
  }
    };

    return (
        <div className="admin-dashboard">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h2>Tableau de Bord Admin 🔑</h2>
            <button onClick={logout} className="btn-secondary">Déconnexion</button>
         </div>   
            
            <div style={{ padding: '15px', backgroundColor: '#e9ecef', borderRadius: '4px', marginBottom: '25px' }}>
               <p style={{ margin: '0' }}>
                  Vous avez {projets.length} projets et  {messagesCount} messages reçus. 
                  <Link to="/admin/messages" style={{ marginLeft: '10px', fontWeight: 'bold' }}>Gérer les messages 📬</Link>
                </p>
            </div>
            
            <hr />
            
<h3>Gestion des Projets</h3>

            <button onClick={() => navigate('/admin/projets/new')} className="btn-primary" style={{ marginBottom: '20px' }}>
              + Ajouter un nouveau projet
            </button>

           <h4>Liste des Projets</h4>
            <div style={{ marginTop: '20px' }}>
             {projets.length === 0 ? (
             <p>Aucun projet n'a été créé pour l'instant.</p>
             ) : (
               <ul style={{ padding: '0' }}>
                 {projets.map(projet => (
                <li key={projet.id}>
                <h4>{projet.title}</h4>
                  <p style={{ color: '#666' }}>{projet.shortDescription}</p>
               <button 
                onClick={() => navigate(`/admin/projets/edit/${projet.id}`)}
                   className="btn-primary"
                  style={{ padding: '8px 15px' }}
                   >
                    Modifier
                   </button>
                   <button 
                  onClick={() => handleDelete(projet.id)} 
                 className="btn-danger"
                style={{ marginLeft: '10px', padding: '8px 15px' }}
                      >
                     Supprimer
                      </button>
                         </li>
                     ))}
                      </ul>
 
                    )}
                  </div>

                {/*
                    <table style={{ width: '100%', marginTop: '20px' }}>
                <thead>
                    <tr><th>Titre</th><th>Actions</th></tr>
                </thead>
                <tbody>
                    {projets.map(projet => (
                        <tr key={projet.id}>
                            <td>{projet.title}</td>
                            <td>
                                <Link to={`/admin/projets/edit/${projet.id}`}>Modifier</Link> |
                                <button onClick={() => handleDelete(projet.id)} style={{ marginLeft: '10px' }}>Supprimer</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
                    */} 

            
        </div>
    );
};

export default AdminDashboard;