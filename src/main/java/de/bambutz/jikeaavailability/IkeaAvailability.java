package de.bambutz.jikeaavailability;

import de.bambutz.jikeaavailability.internal.IOWS2;

import java.io.IOException;

public class IkeaAvailability {
    public static ProductAvailability availability(String buCode, String productId) throws IOException, InterruptedException {
        Store store = Stores.findOneById(buCode);
        IOWS2 iows2 = new IOWS2(store.countryCode(), null);
        return iows2.getStoreProductAvailability(buCode, productId);
    }
}
