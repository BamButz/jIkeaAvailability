package de.bambutz.jikeaavailability.internal;

import de.bambutz.jikeaavailability.ProductAvailability;
import de.bambutz.jikeaavailability.ProductAvailabilityProbability;
import de.bambutz.jikeaavailability.Store;
import de.bambutz.jikeaavailability.Stores;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IOWS2Test {

    @Test
    void getStoreProductAvailability() throws IOException, InterruptedException {
        IOWS2 iows2 = new IOWS2("de", null);

        Store[] stores = Stores.findByCountryCode("de");

        for(Store store : stores) {
            ProductAvailability availability = iows2.getStoreProductAvailability(store.buCode(), "S49430193");

            if (availability.probability() != ProductAvailabilityProbability.LOW)
                System.out.println("Vorhanden in " + store.name());
        }
    }
}