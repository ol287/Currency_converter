import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    private String apiKey;
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    // Constructor to initialize the CurrencyConverter with the provided API key
    public CurrencyConverter(String apiKey) {
        this.apiKey = apiKey;
    }

    // Method to convert currency from one type to another
    public double convert(String fromCurrency, String toCurrency, double amount) throws Exception {
        // Construct the URL for the API request
        String urlString = API_URL + apiKey + "/pair/" + fromCurrency + "/" + toCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // Set the request method to GET

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // If the response code is 200 (OK)
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Read the response line by line
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            double conversionRate = jsonResponse.getDouble("conversion_rate"); // Get the conversion rate from the JSON response
            return amount * conversionRate; // Calculate and return the converted amount
        } else {
            throw new Exception("Failed to get currency conversion rate"); // Throw an exception if the API call fails
        }
    }

    // Main method to run the currency converter
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user to enter the source currency
            System.out.print("Enter the source currency (e.g., USD): ");
            String fromCurrency = scanner.nextLine().toUpperCase(); // Convert to uppercase to ensure correct format

            // Prompt the user to enter the target currency
            System.out.print("Enter the target currency (e.g., EUR): ");
            String toCurrency = scanner.nextLine().toUpperCase(); // Convert to uppercase to ensure correct format

            // Prompt the user to enter the amount to convert
            System.out.print("Enter the amount to convert: ");
            double amount = scanner.nextDouble();

            // Create an instance of CurrencyConverter with the API key
            CurrencyConverter converter = new CurrencyConverter("YOUR_API_KEY");

            // Perform the currency conversion
            double convertedAmount = converter.convert(fromCurrency, toCurrency, amount);

            // Print the result of the conversion
            System.out.println(amount + " " + fromCurrency + " is equal to " + convertedAmount + " " + toCurrency);
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace if an exception occurs
        }
    }
}

