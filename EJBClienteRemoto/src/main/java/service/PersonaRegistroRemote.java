package service;

import java.util.List;

import model.Persona;



public interface PersonaRegistroRemote {
	
	public void registrarPersona(Persona persona);
	List<Persona> obtenerPersonas();

}
