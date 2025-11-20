import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
// Assurez-vous d'importer votre instance Axios configurée pour inclure le JWT
import  securedApi  from '../api/securedApi'; 
// Si vous n'avez pas de 'securedApi', utilisez 'axios' et ajoutez l'en-tête d'autorisation manuellement

const ProjectForm = ({ projectToEdit }) => {
    const navigate = useNavigate();

    //Récupérer l'ID depuis l'URL (si présent)
    const { id } = useParams();

    //Déterminer si nous sommes en mode édition
    const isEditing = !!id; // On utilise maintenant l'ID de l'URL pour déterminer le mode

    // --- NOUVEAUX ÉTATS POUR LA GESTION DES FICHIERS ---
    const [selectedFile, setSelectedFile] = useState(null); // Fichier sélectionné par l'utilisateur
    const [isUploading, setIsUploading] = useState(false); // Statut du téléchargement du fichier
    // ----------------------------------------------------
    // Initialisation de l'état du formulaire
    const [projet, setProjet] = useState(
        projectToEdit || { // Utilise les données existantes si on modifie, sinon crée un objet vide
            title: '',
            shortDescription: '',
            description: '',
            coverImageUrl: '',
            projectUrl: '',
            // Remarque: La gestion des 'images' est plus complexe et sera simplifiée pour l'instant.
        }
    );
    const [loading, setLoading] = useState(isEditing); // Charger si on est en mode édition
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);

    // 2. Charger les données du projet existant si nous sommes en mode édition
    useEffect(() => {
        if (isEditing) {
            const fetchProject = async () => {
                try {
                    // On utilise l'ID de l'URL
                    const response = await securedApi.get(`/projets/${id}`); 
                    setProjet(response.data);
                } catch (err) {
                    console.error("Erreur lors du chargement du projet:", err);
                    setError("Impossible de charger le projet. Vérifiez l'ID.");
                } finally {
                    setLoading(false);
                }
            };
            fetchProject();
        }
    }, [id, isEditing]); // Dépendance à l'ID et isEditing

        //isEditing = !!projectToEdit;
    //deja seccuredApi a comme url de base /api donc pas besoin de le rajouter
   // const apiUrl = isEditing ? `/api/projets/${projet.id}` : '/api/projets';  XXX nooon
   const apiUrl = isEditing ? `/projets/${projet.id}` : '/projets'; //oui sans /api
    const httpMethod = isEditing ? 'PUT' : 'POST';

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProjet(prev => ({ ...prev, [name]: value }));
    };

    // NOUVELLE FONCTION : Gérer la sélection du fichier
    const handleFileChange = (e) => {
        setSelectedFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        let finalProjet = { ...projet }; // Copie de l'état du formulaire

        try {
            let response;

            // 1. GESTION DE L'UPLOAD DE FICHIER
            if (selectedFile) {
                setIsUploading(true);
                const formData = new FormData();
                formData.append('file', selectedFile); // 'file' doit correspondre à @RequestParam("file")

                // Requête POST Multipart/Form-data sécurisée
                const uploadResponse = await securedApi.post('/files/upload', formData, {
                   
                    headers: {
                        'Content-Type': 'multipart/form-data', // Axios gère généralement cela, mais c'est une bonne pratique
                    },
                });
                

                
                // Récupérer l'URL publique renvoyée par le backend (Body de la réponse)
                finalProjet.coverImageUrl = uploadResponse.data; 
                setIsUploading(false);
            }
            // FIN DE GESTION DE L'UPLOAD
            
            // 2. SOUMISSION DU PROJET (POST ou PUT)
            
            // Si c'est une création et qu'aucune image n'a été téléchargée, on arrête
            if (!isEditing && !finalProjet.coverImageUrl) {
                setError("Veuillez sélectionner une image de couverture.");
                setIsSubmitting(false);
                return;
            }
            
            // L'appel à l'API via securedApi inclura automatiquement le token JWT
            if (isEditing) {
                response = await securedApi.put(apiUrl, finalProjet);
               
            } else {
                response = await securedApi.post(apiUrl, finalProjet);
              
            }

            alert(`Projet ${isEditing ? 'mis à jour' : 'créé'} avec succès!`);
            
            // Rediriger vers le tableau de bord après succès
            navigate('/admin/dashboard'); 
            
        } catch (err) {
            console.error('Erreur lors de la soumission du projet:', err);
            // Si le statut est 403 ou 401, il y a un problème de permission
            if (err.response && (err.response.status === 403 || err.response.status === 401)) {
                setError("Permission refusée. Vérifiez que vous êtes connecté en tant qu'ADMIN.");
            } else {
                setError(`Erreur serveur: ${err.message || 'La soumission a échoué.'}`);
            }
            const errorMessage = err.response?.data || err.message;
            setError(`Erreur: ${errorMessage}`);
        } finally {
            setIsSubmitting(false);
            setIsUploading(false);
        }
    };
    if (loading) return <div className="container">Chargement des données du projet...</div>;

    return (
        <div className="container">
            <h2>{isEditing ? 'Modifier le Projet' : 'Ajouter un Nouveau Projet'}</h2>
            
            {error && <p style={{ color: 'red' }}>{error}</p>}
            
            <form onSubmit={handleSubmit}>
                
                {/* Champ Titre */}
                <div>
                    <label>Titre:</label>
                    <input
                        type="text"
                        name="title"
                        value={projet.title}
                        onChange={handleChange}
                        required
                    />
                </div>
                
                {/* Champ Description Courte */}
                <div>
                    <label>Description Courte (Max 255 chars):</label>
                    <input
                        type="text"
                        name="shortDescription"
                        value={projet.shortDescription}
                        onChange={handleChange}
                        maxLength="255"
                        required
                    />
                </div>

                {/* Champ Description Longue */}
                <div>
                    <label>Description Détaillée:</label>
                    <textarea
                        name="description"
                        value={projet.description}
                        onChange={handleChange}
                        rows="5"
                        required
                    />
                </div>

                {/* 🎯 NOUVEAU CHAMP : Upload de Fichier */}
                <div>
                    <label>Image de Couverture:</label>
                    <input
                        type="file"
                        accept="image/*" // Permet uniquement les fichiers image
                        onChange={handleFileChange}
                        // Obligatoire uniquement si on crée ET qu'il n'y a pas déjà d'image
                        required={!isEditing && !projet.coverImageUrl} 
                    />
                    
                    {/* Affichage du statut d'upload */}
                    {(isUploading || isSubmitting) && <p style={{ color: 'blue' }}>{isUploading ? 'Téléchargement de l\'image en cours...' : 'Soumission...'}</p>}

                    {/* Affichage de l'image actuelle (pour la modification) */}
                    {(projet.coverImageUrl && !selectedFile) && (
    <div style={{ marginTop: '10px', padding: '10px', border: '1px solid #e0e0e0', borderRadius: '4px', backgroundColor: '#fff' }}>
        <p style={{ margin: '0 0 5px 0', fontWeight: 'bold' }}>Image actuelle :</p>
        {/* Assurez-vous que projet.coverImageUrl est bien l'URL HTTP, PAS le chemin C:/ */}
        <img 
            src={projet.coverImageUrl} 
            alt="Couverture actuelle" 
           style={{ maxWidth: '150px', maxHeight: '150px', objectFit: 'cover', borderRadius: '4px' }}
        />
    </div>
)}
                </div>

                {/* Champ URL du Projet Live */}
                <div>
                    <label>URL du Projet Live (Optionnel):</label>
                    <input
                        type="url"
                        name="projectUrl"
                        value={projet.projectUrl}
                        onChange={handleChange}
                    />
                </div>

                <div>
                <button type="submit"
                className="btn-primary"
                 disabled={isSubmitting || isUploading}>
                    {isUploading ? 'Téléchargement...' : (isSubmitting ? 'Soumission...' : (isEditing ? 'Mettre à Jour le Projet' : 'Créer le Projet'))}
                </button>
                <button type="button" 
                className="btn-secondary"
                onClick={() => navigate('/admin/dashboard')}>
                Annuler
                </button>
             </div>
         </form>
    </div>
    );
};

export default ProjectForm;
