import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

public class JSONManager {

    // Define the JSON file path as a constant
    private static final String FILE_PATH = "contacts.json";

    // Save contacts to JSON file
    public static void saveToJSON(ListOfContacts contactListApp) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(contactListApp, writer);
            logMessage("Contacts successfully saved to " + FILE_PATH + ".");
        } catch (IOException e) {
            logError("Error saving contacts to JSON", e);
        }
    }

    // Load contacts from JSON file
    public static ListOfContacts loadFromJSON() {
        Gson gson = new Gson();
        ListOfContacts contacts = new ListOfContacts();

        if (!isFileExists(FILE_PATH)) {
            logMessage(FILE_PATH + " not found. Initializing with an empty contact list.");
            return contacts;
        }

        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type contactListType = new TypeToken<ListOfContacts>() {}.getType();
            contacts = gson.fromJson(reader, contactListType);

            if (contacts == null || contacts.getContactList() == null) { // Handle empty or malformed JSON
                contacts = new ListOfContacts();
                logMessage("JSON file was empty or malformed. Initialized empty contact list.");
            } else {
                logMessage("Contacts successfully loaded from " + FILE_PATH + ".");
            }
        } catch (IOException e) {
            logError("Error loading contacts from JSON", e);
        }

        return contacts;
    }

    // Helper method to check if a file exists
    private static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    // Helper method to log messages
    private static void logMessage(String message) {
        System.out.println(message);
    }

    // Helper method to log errors
    private static void logError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}
