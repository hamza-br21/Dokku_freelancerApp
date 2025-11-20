import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import securedApi from '../api/securedApi'; 

const MessageList = () => {
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Fonction pour charger les messages depuis l'API
    const fetchMessages = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await securedApi.get('/messages'); 
            
            //  NORMALISATION DES DONNÉES EN BOOLÉEN STRICT
            const data = response.data.map(msg => ({
                ...msg,
                // Assure que isRead est un booléen strict pour le JSX
                isRead: msg.isRead === true || msg.isRead === 'true', 
            }));
            
                 /*console.log('Valeur brute de isRead:', response.data.map(m => ({
                     id: m.id, 
                     isRead: m.isRead, 
                       type: typeof m.isRead
                      })));*/

            setMessages(data);
            
        } catch (err) {
            console.error('Erreur lors du chargement des messages:', err);
            setError("Impossible de charger les messages. Veuillez vous reconnecter.");
        } finally {
            setLoading(false);
        }
    };
    
    useEffect(() => {
        fetchMessages();
    }, []);


    // Mise à jour de l'état local + Appel PUT
    const handleMarkAsRead = async (messageId, currentStatus) => {
        try {
            const newStatus = !currentStatus;
            
            await securedApi.put(`/messages/${messageId}/read`, { isRead: newStatus }); 

            // Mise à jour de l'état local
            setMessages(messages.map(msg => 
                msg.id === messageId ? { ...msg, isRead: newStatus } : msg
            ));
           

        } catch (err) {
            console.error("Erreur lors de la mise à jour du statut:", err);
            setError("Échec de la mise à jour du statut de lecture.");
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Êtes-vous sûr de vouloir supprimer ce message ?")) {
            try {
                await securedApi.delete(`/messages/${id}`); 
                alert("Message supprimé.");
                setMessages(messages.filter(msg => msg.id !== id));
            } catch (err) {
                console.error("Erreur lors de la suppression:", err);
                setError("Échec de la suppression.");
            }
        }
    };

    if (loading) {
        return <div className="container">Chargement des messages...</div>;
    }

    if (error) {
        return <div sclassName="container" style={{ color: '#dc3545', fontWeight: 'bold' }}>Erreur : {error}</div>;
    }
    
    return (
        <div className="message-list-container">
            <h2>Liste des Messages de Contact ({messages.length})</h2>
            <button onClick={() => navigate('/admin/dashboard')} className="btn-secondary">Retour au Tableau de Bord</button>
            <hr style={{ margin: '20px 0' }}/>

            {messages.length === 0 ? (
                <p>Aucun message reçu pour l'instant.</p>
            ) : (
                <div className="message-list">
                    {messages.map(message => {
                        
                        // CORRECTION : Déclarer la variable LECTURE dans le scope de la fonction map
                        const isReadStatus = message.isRead; 
                        
                        return (
                            <div 
                                key={message.id} 
                                className={`message-item ${isReadStatus ? 'message-read' : 'message-unread'}`}
                            >
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                                <strong>De: {message.nameClient}</strong> 
                                <strong>Email: {message.emailClient}</strong>
                                <span >
                                  </span>
                                </div>
                                <span style={{ float: 'right', color: '#666', fontSize: '0.8em', marginTop: '-30px' }}>
                                    Reçu le: {new Date(message.createdAt).toLocaleDateString()}
                                </span>
                                <p style={{ marginTop: '5px', fontWeight: 'bold' }}>Objet: {message.subject || '(Pas d\'objet)'}</p>
                               {/*
                               <p className="message-body">
                                 {message.message}
                                  </p> */} 
                                  <textarea className="message-body" name="message-body" value= {message.message} readOnly />
                                {/* BOUTON MARQUER COMME LU/NON LU */}
                                <div className="message-status">
                                <button 
                                    // Utiliser le statut local normalisé
                                    onClick={() => handleMarkAsRead(message.id, isReadStatus)} 
                                        className={isReadStatus ? 'btn-secondary' : 'btn-primary'}
                                        style={{ marginRight: '10px', padding: '8px 15px' }}
                                >
                                    {isReadStatus ? ' Non Lu' : ' Lu'}
                                </button>
                                
                                <button 
                                        onClick={() => handleDelete(message.id)} 
                                        className="btn-danger"
                                        style={{ padding: '8px 15px' }}
                                         >
                                       Supprimer
                                     </button>
                                     </div>
                            </div>
                        ); // Fin du return du map
                    })}
                </div>
            )}
        </div>
    );
};

export default MessageList;