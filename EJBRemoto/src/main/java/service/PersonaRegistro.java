package service;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Persona;

@Stateless
public class PersonaRegistro implements PersonaRegistroRemote{

	 @PersistenceContext
	    private EntityManager entityManager;

	 @Override
	    public void registrarPersona(Persona persona) {
	        entityManager.persist(persona);
	    }

	 @Override
	 	public List<Persona> obtenerPersonas() {
	        return entityManager.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
	 	}

}
