import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.regex.Pattern;

@XmlRootElement
public class Person {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}");

    private int age;
    private String name;
    private String mail;
    private String phone;
    private String address;

    // Default constructor (required for JAXB and JSON)
    public Person() {}

    // Parameterized constructor
    public Person(int age, String name, String mail, String phone, String address) {
        this.setAge(age);
        this.setName(name);
        this.setMail(mail);
        this.setPhone(phone);
        this.setAddress(address);
    }

    @XmlElement
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        this.age = age;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateString(name, "Name");
        this.name = name;
    }

    @XmlElement
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (!EMAIL_PATTERN.matcher(mail).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.mail = mail;
    }

    @XmlElement
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
        this.phone = phone;
    }

    @XmlElement
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateString(address, "Address");
        this.address = address;
    }

    private void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", mail=" + mail + ", phone=" + phone + ", address=" + address + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(name, person.name) &&
                Objects.equals(mail, person.mail) &&
                Objects.equals(phone, person.phone) &&
                Objects.equals(address, person.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, mail, phone, address);
    }
}
