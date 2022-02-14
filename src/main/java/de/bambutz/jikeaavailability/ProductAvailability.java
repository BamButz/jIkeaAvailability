package de.bambutz.jikeaavailability;

import java.util.Date;

public record ProductAvailability(
        Date createdAt,
        ProductAvailabilityProbability probability,
        String productId,
        String buCode,
        int stock,
        Date restockDate
) {
}
