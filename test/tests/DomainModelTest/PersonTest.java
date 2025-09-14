package tests.DomainModelTest;

import DomainModel.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testFullConstructor() {
        Person person = new Person(1, "John", "Doe", "john@example.com", "secret");
        assertEquals(1, person.getPersonID());
        assertEquals("John", person.getName());
        assertEquals("Doe", person.getSurname());
        assertEquals("john@example.com", person.getEmail());
        assertEquals("secret", person.getPassword());
    }

    @Test
    void testConstructorWithoutPassword() {
        Person person = new Person(2, "Jane", "Doe", "jane@example.com");
        assertEquals(2, person.getPersonID());
        assertEquals("Jane", person.getName());
        assertEquals("Doe", person.getSurname());
        assertEquals("jane@example.com", person.getEmail());
        assertNull(person.getPassword());
    }

    @Test
    void testConstructorWithOnlyID() {
        Person person = new Person(3);
        assertEquals(3, person.getPersonID());
        assertNull(person.getName());
        assertNull(person.getSurname());
        assertNull(person.getEmail());
        assertNull(person.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        Person person = new Person(1, "John", "Doe", "john@example.com", "secret");

        person.setName("Mike");
        person.setSurname("Smith");
        person.setEmail("mike@example.com");
        person.setPassword("newpass");
        person.setPersonID(10);

        assertEquals("Mike", person.getName());
        assertEquals("Smith", person.getSurname());
        assertEquals("mike@example.com", person.getEmail());
        assertEquals("newpass", person.getPassword());
        assertEquals(10, person.getPersonID());
    }

    @Test
    void testUpdateEmailAndPassword() {
        Person person = new Person(1, "John", "Doe", "john@example.com", "secret");
        person.updateEmail("john.new@example.com");
        person.updatePassword("newsecret");

        assertEquals("john.new@example.com", person.getEmail());
        assertEquals("newsecret", person.getPassword());
    }
}
