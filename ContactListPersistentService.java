import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class ContactListPersistentService extends JFrame {

    private ListOfContacts contactListApp;

    public ContactListAppUI() {
        contactListApp = JSONManager.loadFromJSON();

        setTitle("Contact List Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createInputPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        add(createSearchPanel(), BorderLayout.NORTH);

        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addressLabel);
        panel.add(addressField);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(e -> addContact(nameField, ageField, emailField, phoneField, addressField));

        panel.add(addButton);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton showButton = new JButton("Show Contacts");
        showButton.addActionListener(e -> showContacts());

        panel.add(showButton);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel searchLabel = new JLabel("Search by:");
        JTextField searchField = new JTextField(10);

        String[] searchOptions = {"Name", "Email", "Phone"};
        JComboBox<String> searchCombo = new JComboBox<>(searchOptions);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch(searchField.getText(), (String) searchCombo.getSelectedItem()));

        panel.add(searchLabel);
        panel.add(searchCombo);
        panel.add(searchField);
        panel.add(searchButton);

        return panel;
    }

    private void addContact(JTextField nameField, JTextField ageField, JTextField emailField, JTextField phoneField, JTextField addressField) {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            validateInputs(name, age, email, phone);

            Person newPerson = new Person(age, name, email, phone, address);
            contactListApp.addPerson(newPerson);
            JSONManager.saveToJSON(contactListApp);

            clearFields(nameField, ageField, emailField, phoneField, addressField);

            JOptionPane.showMessageDialog(this, "Contact added successfully!");
        } catch (NumberFormatException ex) {
            showError("Age must be a valid number.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showContacts() {
        if (contactListApp.getContactList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No contacts found.");
        } else {
            StringBuilder contacts = new StringBuilder();
            for (Person person : contactListApp.getContactList()) {
                contacts.append(person).append("\n");
            }
            JTextArea textArea = new JTextArea(contacts.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Contacts", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void performSearch(String query, String searchBy) {
        if (query.isEmpty()) {
            showError("Please enter a search term.");
            return;
        }

        List<Person> results = contactListApp.getContactList().stream()
                .filter(person -> matchesQuery(person, query, searchBy))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No contact found with the " + searchBy.toLowerCase() + ": " + query);
        } else {
            StringBuilder resultText = new StringBuilder();
            results.forEach(person -> resultText.append(person).append("\n"));
            JTextArea textArea = new JTextArea(resultText.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean matchesQuery(Person person, String query, String searchBy) {
        query = query.toLowerCase();
        switch (searchBy) {
            case "Name":
                return person.getName().toLowerCase().contains(query);
            case "Email":
                return person.getMail().toLowerCase().contains(query);
            case "Phone":
                return person.getPhone().replaceAll("[^0-9]", "").contains(query.replaceAll("[^0-9]", ""));
            default:
                return false;
        }
    }

    private void validateInputs(String name, int age, String email, String phone) {
        if (name.isEmpty() || age < 0) {
            throw new IllegalArgumentException("Name cannot be empty and age must be positive.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!isValidPhoneNumber(phone)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^(\\+\\d{1,3}[- ]?)?(\\(\\d{3}\\)[- ]?|\\d{3}[- ]?)\\d{3}[- ]?\\d{4}$");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    public static void main(String[] args) {
        new ContactListAppUI();
    }
}
