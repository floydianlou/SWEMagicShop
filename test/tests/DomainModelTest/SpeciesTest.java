package tests.DomainModelTest;

import DomainModel.Species;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesTest {

    @Test
    void testMinimalConstructor() {
        Species species = new Species(2, "Human");
        assertEquals(2, species.getSpeciesID());
        assertEquals("Human", species.getName());
        assertEquals(0, species.getLegalAge());
        assertEquals(0, species.getLimitAge());
    }

    @Test
    void testGetters() {
        Species species = new Species(3, "Dwarf", 50, 400);
        assertEquals(3, species.getSpeciesID());
        assertEquals("Dwarf", species.getName());
        assertEquals(50, species.getLegalAge());
        assertEquals(400, species.getLimitAge());
    }
}

