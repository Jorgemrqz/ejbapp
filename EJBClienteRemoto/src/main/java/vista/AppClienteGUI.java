package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.Persona;
import service.PersonaRegistroRemote;

public class AppClienteGUI extends JFrame {
	private PersonaRegistroRemote personaRegistroRemote;
	
	private JTextField txtNombre, txtApellido, txtEmail;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public AppClienteGUI() {
        initializeEJB();
        initializeGUI();
    }
    
    private void initializeEJB() {
        try {
            Hashtable<String, Object> jndiProps = new Hashtable<>();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            
            Context context = new InitialContext(jndiProps);
            personaRegistroRemote = (PersonaRegistroRemote) context.lookup(
            		"ejb:/EJBRemoto/PersonaRegistro!service.PersonaRegistroRemote"
            );
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con el EJB remoto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeGUI() {
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Cliente"));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtEmail = new JTextField();

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtApellido);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> registrarPersona());
        formPanel.add(btnGuardar);

        add(formPanel, BorderLayout.NORTH);

        // Tabla de clientes
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "Email"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

          setVisible(true);
    }

    private void registrarPersona() {
        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String email = txtEmail.getText();

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Persona persona = new Persona();
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setEmail(email);

            personaRegistroRemote.registrarPersona(persona);

            JOptionPane.showMessageDialog(this, "Persona registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            actualizarTabla();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar la persona.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
        try {
            List<Persona> personas = personaRegistroRemote.obtenerPersonas();
            tableModel.setRowCount(0); // Limpiar la tabla

            for (Persona p : personas) {
                tableModel.addRow(new Object[]{p.getId(), p.getNombre(), p.getApellido(), p.getEmail()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppClienteGUI::new);
    }

}
