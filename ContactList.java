import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
public class ContactList {

    // Enum for search criteria
    public enum SearchType {
        NAME, EMAIL, PHONE
    }

    // List to store Person objects
    private List<Person> contactList;

    // Constructor to initialize the contact list
    public ListOfContacts() {
        contactList = new ArrayList<>();
    }

    @XmlElement
    public List<Person> getContactList() {
        return contactList;
    }

    public void setContactList(List<Person> contactList) {
        this.contactList = contactList;
    }

    // Method to add a person to the contact list
    public void addPerson(Person person) {
        if (person != null) {
            contactList.add(person);
            System.out.println("Person added successfully: " + person);
        } else {
            System.out.println("Cannot add a null person.");
        }
    }

    // Method to print all people in the contact list
    public void printContactList() {
        if (contactList.isEmpty()) {
            System.out.println("Contact list is empty.");
        } else {
            StringBuilder contacts = new StringBuilder("Contact List:\n");
            contactList.forEach(person -> contacts.append(person).append("\n"));
            System.out.println(contacts);
        }
    }

    // Generalized search method
    public void search(String query, SearchType searchType) {
        if (isInvalidInput(query)) {
            System.out.println("Invalid input for search.");
            return;
        }

        List<Person> results = contactList.stream()
                .filter(person -> matchesSearchCriteria(person, query, searchType))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No contact found with the " + searchType.name().toLowerCase() + ": " + query);
        } else {
            System.out.println("Search Results:");
            results.forEach(System.out::println);
        }
    }

    // Helper method to validate input
    private boolean isInvalidInput(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Helper method to match search criteria
    private boolean matchesSearchCriteria(Person person, String query, SearchType searchType) {
        switch (searchType) {
            case NAME:
                return person.getName().equalsIgnoreCase(query);
            case EMAIL:
                return person.getMail().equalsIgnoreCase(query);
            case PHONE:
                String normalizedQuery = query.replaceAll("[^0-9]", "");
                String normalizedPhone = person.getPhone().replaceAll("[^0-9]", "");
                return normalizedPhone.equals(normalizedQuery);
            default:
                return false;
        }
    }
}
