package client.EJBClienteRemoto;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import model.Persona;
import service.PersonaRegistroRemote;


public class AppCliente {
	private PersonaRegistroRemote personaRegistro;

    // Método para inicializar el cliente EJB remoto
    public void initialize() throws Exception {
        Hashtable<String, Object> jndiProps = new Hashtable<>();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

        try {
            Context context = new InitialContext(jndiProps);
            personaRegistro = (PersonaRegistroRemote) context.lookup(
                "ejb:/EJBRemoto/PersonaRegistro!service.PersonaRegistroRemote"
            );
        } catch (Exception e) {
            System.err.println("Error durante el lookup del EJB remoto.");
            e.printStackTrace();
            throw e;
        }
    }

    // Método para registrar una nueva persona
    public void registerPersona(String nombre, String apellido, String email) throws Exception {
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setEmail(email);

        personaRegistro.registrarPersona(persona);
        System.out.println("Persona registrada exitosamente: " + nombre + " " + apellido);
    }

    // Método para listar todas las personas registradas
    public void listPersonas() throws Exception {
        List<Persona> personas = personaRegistro.obtenerPersonas();
        System.out.println("Lista de personas registradas:");
        for (Persona persona : personas) {
            System.out.printf("ID: %d, Nombre: %s, Apellido: %s, Email: %s%n",
                persona.getId(), persona.getNombre(), persona.getApellido(), persona.getEmail());
        }
    }

    public static void main(String[] args) {
        AppCliente client = new AppCliente();
        try {
            // Inicializar el cliente EJB
            client.initialize();

            // Registrar una nueva persona
            client.registerPersona("Juan", "Pérez", "juan.perez@example.com");

            // Mostrar la lista de personas registradas
            client.listPersonas();
        } catch (Exception e) {
            System.err.println("Error al interactuar con el EJB remoto.");
            e.printStackTrace();
        }
    }
}
