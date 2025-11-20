// ProjectCard.js
import React from 'react';

// Assurez-vous que les styles .project-card sont définis dans votre CSS
const ProjectCard = ({ projet }) => {
    return (
        <a key={projet.id} href={projet.projectUrl || '#'} className="project-card" target="_blank" rel="noopener noreferrer">
            <img src={projet.coverImageUrl} alt={projet.title} />
            <h3>{projet.title}</h3>
            <p>{projet.shortDescription}</p>
        </a>
    );
};

export default ProjectCard;