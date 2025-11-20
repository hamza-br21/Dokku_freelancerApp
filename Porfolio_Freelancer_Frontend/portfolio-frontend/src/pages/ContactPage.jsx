import React, { useState } from 'react';
import publicApi from '../api/publicApi';

const ContactPage = () => {
    const [formData, setFormData] = useState({ nameClient: '', emailClient: '', subject:'', message: '' });
    const [status, setStatus] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setStatus('Envoi en cours...');
        try {
            // POST /api/contact
            await publicApi.post('/contact', formData); 
            setStatus('Message envoyé avec succès ! Je vous répondrai bientôt.');
            setFormData({ nameClient: '', emailClient: '', subject:'', message: '' });
        } catch (error) {
            setStatus('Erreur lors de l\'envoi. Veuillez réessayer.');
            console.error('Erreur contact:', error);
        }
    };

    return (
        <div className="container">
            <h1>Contactez-moi</h1>
            <form onSubmit={handleSubmit}>
                <input type="text" name="nameClient" placeholder="Votre Nom" value={formData.nameClient} onChange={handleChange} required />
                <input type="email" name="emailClient" placeholder="Votre Email" value={formData.emailClient} onChange={handleChange} required />
                <input type="text" name="subject" placeholder="Votre objet" value={formData.subject} onChange={handleChange} />
                <textarea name="message" placeholder="Votre Message" rows="5" value={formData.message} onChange={handleChange} required />
            <div>
                <button type="submit" className="btn-primary">Envoyer le message</button>
             </div>
            </form>
            <p style={{ marginTop: '20px', fontWeight: 'bold', color: status.includes('succès') ? '#28a745' : '#dc3545' }}>{status}</p>
        </div>
    );
};

export default ContactPage;