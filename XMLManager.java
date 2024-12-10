import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLManager {

    private XMLManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Saves the contact list to an XML file.
     *
     * @param contactListApp the contact list to save.
     * @param filePath       the path to the XML file.
     */
    public static void saveToXML(ListOfContacts contactListApp, String filePath) {
        if (contactListApp == null || filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid input: contact list or file path cannot be null/empty.");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ListOfContacts.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(filePath);
            marshaller.marshal(contactListApp, file);

            System.out.println("Contacts successfully saved to XML: " + filePath);
        } catch (Exception e) {
            System.err.println("Error saving contacts to XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the contact list from an XML file.
     *
     * @param filePath the path to the XML file.
     * @return the loaded contact list.
     */
    public static ListOfContacts loadFromXML(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        ListOfContacts contacts = new ListOfContacts();

        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                JAXBContext context = JAXBContext.newInstance(ListOfContacts.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                contacts = (ListOfContacts) unmarshaller.unmarshal(file);

                System.out.println("Contacts successfully loaded from XML: " + filePath);
            } else {
                System.out.println("File does not exist or is not a valid file: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading contacts from XML: " + e.getMessage());
            e.printStackTrace();
        }

        return contacts;
    }
}

