package mobile.support;

public record PurchaseData(
        String productName,
        int quantity,
        String fullName,
        String address1,
        String address2,
        String city,
        String state,
        String zip,
        String country,
        String cardHolderInput,
        String cardHolderReview,
        String cardNumberInput,
        String cardNumberReview,
        String expirationDate,
        String securityCode,
        String totalAmount
) {
}
