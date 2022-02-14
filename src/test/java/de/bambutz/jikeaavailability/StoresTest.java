package de.bambutz.jikeaavailability;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StoresTest {
    @Test
    void findById() {
        Store[] stores = Stores.findById(new String[]{"174", "187"});

        Assertions.assertEquals(2, stores.length);
    }

    @Test
    void findOneById() {
        Store store = Stores.findOneById("174");

        Assertions.assertNotNull(store);
        Assertions.assertEquals("Kassel", store.name());
    }

    @Test
    void findByCountryCode() {
        Store[] stores = Stores.findByCountryCode("de");

        Assertions.assertEquals(54, stores.length);
    }
}