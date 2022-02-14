package de.bambutz.jikeaavailability;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Stores {
    private static final HashMap<String, Store> stores = new HashMap<>();

    static {
        loadStores();
    }

    /**
     * @param buCodes array of store ids where the store should get returned
     * @return one or more store matching the given ids
     */
    public static Store[] findById(String[] buCodes) {
        return stores
                .values()
                .stream()
                .filter(e -> Arrays.asList(buCodes).contains(e.buCode()))
                .toArray(Store[]::new);
    }

    /**
     * @param buCode unique ikea store identification number
     * @return Store when a store with the exact given buCode exists when not returns null
     */
    public static Store findOneById(String buCode) {
        return stores.get(buCode);
    }

    /**
     * @param countryCode ISO 3166-1 alpha 2 country code whos stores
     *   should get returned. If the countrycode is not valid or known an empty
     *   array is returned
     * @return list of stores from the country
     */
    public static Store[] findByCountryCode(String countryCode) {
        String normalizedCountryCode = normalizeCountryCode(countryCode);

        return stores
                .values()
                .stream()
                .filter(store -> store.countryCode().equals(normalizedCountryCode))
                .toArray(Store[]::new);
    }

    /**
     * Returns an array with all ISO 3166-1 alpha 2 country codes that have at
     * least one store.
     * @return two-letter ISO 3166-2 alpha 2 country codes
     */
    public static String[] getCountryCodes() {
        return stores
                .values()
                .stream()
                .map(Store::countryCode)
                .distinct()
                .toArray(String[]::new);
    }

    /**
     * Returns a two-letter ISO 639-1 language code belonging to
     * the provided two-letter ISO 3166-2 alpha 2 country code
     * @param countryCode two-letter ISO 3166-2 alpha 2 country code
     * @return two-letter ISO 639-1 language code
     */
    public static String getLanguageCode(String countryCode) {
        String normalizedCountryCode = normalizeCountryCode(countryCode);

        HashMap<String, String> countryToLanguage = new HashMap<>();
        countryToLanguage.put("ae", "en");
        countryToLanguage.put("at", "de");
        countryToLanguage.put("au", "en");
        countryToLanguage.put("be", "fr");
        countryToLanguage.put("ca", "en");
        countryToLanguage.put("ch", "de");
        countryToLanguage.put("cn", "zh");
        countryToLanguage.put("cz", "cs");
        countryToLanguage.put("dk", "da");
        countryToLanguage.put("gb", "en");
        countryToLanguage.put("hk", "en");
        countryToLanguage.put("ie", "en");
        countryToLanguage.put("jo", "en");
        countryToLanguage.put("jp", "ja");
        countryToLanguage.put("kr", "ko");
        countryToLanguage.put("kw", "en");
        countryToLanguage.put("my", "en");
        countryToLanguage.put("qa", "en");
        countryToLanguage.put("sa", "en");
        countryToLanguage.put("se", "sv");
        countryToLanguage.put("sg", "en");
        countryToLanguage.put("th", "en");
        countryToLanguage.put("tw", "zh");
        countryToLanguage.put("us", "en");

        String languageCode = countryToLanguage.get(normalizedCountryCode);
        if (languageCode == null) {
            languageCode = normalizedCountryCode;
        }

        return languageCode;
    }

    private static String normalizeCountryCode(String countryCode) {
        return countryCode.trim().toLowerCase();
    }

    private static void loadStores() {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(Stores.class
                .getResourceAsStream("stores.json")), StandardCharsets.UTF_8)) {
            Store[] stores = new Gson().fromJson(reader, Store[].class);
            for(Store store : stores) {
                Stores.stores.put(store.buCode(), store);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
