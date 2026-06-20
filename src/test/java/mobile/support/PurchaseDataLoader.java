package mobile.support;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PurchaseDataLoader {

    private static final String RESOURCE_PATH = "/mobile/testdata/purchase-data.json";
    private static final Pattern ENTRY_PATTERN = Pattern.compile(
            "\"([^\"]+)\"\\s*:\\s*(\"((?:\\\\.|[^\"\\\\])*)\"|(-?\\d+))"
    );

    private PurchaseDataLoader() {
    }

    public static PurchaseData loadDefault() {
        try (InputStream inputStream = PurchaseDataLoader.class.getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                throw new IllegalStateException("Unable to find purchase data resource: " + RESOURCE_PATH);
            }

            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return parse(json);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load purchase data from " + RESOURCE_PATH, e);
        }
    }

    private static PurchaseData parse(String json) {
        Map<String, String> values = new HashMap<>();
        Matcher matcher = ENTRY_PATTERN.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String stringValue = matcher.group(3);
            String numberValue = matcher.group(4);
            values.put(key, stringValue != null ? unescape(stringValue) : numberValue);
        }

        return new PurchaseData(
                required(values, "productName"),
                parseInt(values, "quantity"),
                required(values, "fullName"),
                required(values, "address1"),
                required(values, "address2"),
                required(values, "city"),
                required(values, "state"),
                required(values, "zip"),
                required(values, "country"),
                required(values, "cardHolderInput"),
                required(values, "cardHolderReview"),
                required(values, "cardNumberInput"),
                required(values, "cardNumberReview"),
                required(values, "expirationDate"),
                required(values, "securityCode"),
                required(values, "totalAmount")
        );
    }

    private static String required(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required purchase data field: " + key);
        }
        return value;
    }

    private static int parseInt(Map<String, String> values, String key) {
        String value = required(values, key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("Invalid integer value for purchase data field: " + key + " = " + value, ex);
        }
    }

    private static String unescape(String value) {
        return value.replace("\\\\", "\\").replace("\\\"", "\"");
    }
}
