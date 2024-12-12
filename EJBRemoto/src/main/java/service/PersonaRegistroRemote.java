package service;

import java.util.List;

import jakarta.ejb.Remote;
import model.Persona;

@Remote
public interface PersonaRegistroRemote {

	public void registrarPersona(Persona persona);
	List<Persona> obtenerPersonas();
}
