package de.bambutz.jikeaavailability.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.bambutz.jikeaavailability.ProductAvailability;
import de.bambutz.jikeaavailability.ProductAvailabilityProbability;
import de.bambutz.jikeaavailability.Stores;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public class IOWS2 {
    private final String countryCode;
    private final String languageCode;
    private static final String baseUrl = "https://iows.ikea.com/retail/iows";

    public IOWS2(String countryCode, String languageCode) {
        this.countryCode = countryCode;
        this.languageCode = Objects.requireNonNullElseGet(languageCode, () -> Stores.getLanguageCode(countryCode));
    }

    public ProductAvailability getStoreProductAvailability(String buCode, String productId) throws IOException, InterruptedException {
        URI uri = buildURI(buCode, productId);

        JsonElement data;

        data = JsonParser.parseString(readUri(uri));
        JsonElement stockAvailability = data.getAsJsonObject().get("StockAvailability");
        JsonElement retailItemAvailability = stockAvailability.getAsJsonObject().get("RetailItemAvailability");

        String probability = retailItemAvailability
                .getAsJsonObject()
                .get("InStockProbabilityCode")
                .getAsJsonObject()
                .get("$")
                .getAsString();

        int stock = retailItemAvailability
                .getAsJsonObject()
                .get("AvailableStock")
                .getAsJsonObject()
                .get("$")
                .getAsInt();

        JsonElement restockDateTime = retailItemAvailability
                .getAsJsonObject()
                .get("RestockDateTime");

        Date restockDate = null;
        if (restockDateTime != null) {
            DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            try {
                restockDate = d.parse(restockDateTime
                        .getAsJsonObject()
                        .get("$")
                        .getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new ProductAvailability(
                new Date(),
                ProductAvailabilityProbability.valueOf(probability),
                productId,
                buCode,
                stock,
                restockDate
        );
    }

    private URI buildURI(String buCode, String productId) {
        boolean isSpr = productId.toLowerCase().startsWith("s");
        if (isSpr) {
            productId = productId.substring(1);
        }

        String[] fragments = new String[] {
                baseUrl,
                countryCode,
                languageCode,
                "stores",
                buCode,
                "availability",
                isSpr ? "SPR" : "ART",
                productId
        };

        try {
            return new URI(String.join("/", fragments));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readUri(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofMillis(5000))
                .header("ACCEPT", "application/vnd.ikea.iows+json;version=1.0")
                .header("Contract", "37249")
                .header("Consumer", "MAMMUT")
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> reponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return reponse.body();
    }
}
