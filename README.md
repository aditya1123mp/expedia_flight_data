# Expedia Flight Data Automation (Mumbai to Bengaluru)

This project automates the process of retrieving flight data from Expedia for flights from Mumbai to Bengaluru, for the current date and the following 20 days. The automation is built using Katalon Studio and Groovy scripting.

## Overview

The main objective of this project is to extract flight information (including pricing, flight times, and airline details) and save it in an Excel file for further analysis. This automation helps users track flight availability and trends for flights between Mumbai and Bengaluru.

## Features

- Flight Data Collection: Retrieves flight details (price, airline, departure time, arrival time, etc.) from Expedia.
- Automated Search: Searches for flights from Mumbai (BOM) to Bengaluru (BLR) starting from the current day and continues for the next 20 days.
- Data Storage: Saves the extracted data in an Excel file (`flight_data.xlsx`), making it easy to analyze.
- File Attachment in Email: Sends the Excel sheet via email after completion of the automation run.
- Github Integration: Link to the project repository and automation progress.
  
## Prerequisites

To run this project successfully, ensure you have the following tools installed:

- Katalon Studio: [Download here](https://www.katalon.com/)
- JavaMail API: Used to send the extracted flight data via email.
- Groovy: Groovy scripting language is used within Katalon Studio for scripting the automation.
  
### Required Dependencies

In your Katalon project, ensure the following JARs are added to your `Drivers` folder:

- `javax.mail.jar`
- `javax.activation.jar`

These JAR files can be found [here](https://javaee.github.io/javamail/#Download_JavaMail_Release) or you can include them via Maven or other dependency managers.

## How It Works

### 1.Flight Search Automation
The script navigates to the Expedia website and performs a search for flights from Mumbai to Bengaluru. It collects the flight data for the current date and automatically proceeds to the next 20 days to gather more data.

### 2. Flight Data Extraction
The extracted flight data includes:
- Airline Name
- Departure and Arrival Times
- Price
- Duration of the flight
  
The extracted information is structured and stored in an Excel file for easy access.

### 3. Email Notification
After the completion of the automation run, the Excel file containing the flight data is emailed to the recipient, whose email address is specified in the global variables.

### 4. Next Day Search 
If no flights are found for the current day, the script automatically moves to the next day's search and continues until it has fetched data for all 20 days.

2. Open the project in Katalon Studio.

3. Set Global Variables in Katalon Studio for email credentials and recipient:
   - `GlobalVariable.your_email` (Your email address)
   - `GlobalVariable.your_email_password` (Your email password)
   - `GlobalVariable.recipient_email` (Recipient's email address)
   
4. Run the Test Case: Execute the main test case (`Expedia_Flight_Data_TC.groovy`) in Katalon Studio.

5. Review the Excel file: After the automation run, check the `flight_data.xlsx` file generated in the `Data Files` directory.

6. Email Notification: You will receive an email with the attached Excel file and a summary of the data collected.

## Future Enhancements

- Filtering by Airlines: Allow users to specify airlines for more focused data collection.
- Price Trends: Automatically analyze price trends for flights over the 20 days and generate a report.
- Additional Routes: Expand the search to other popular flight routes beyond Mumbai and Bengaluru.

## Contributing

Contributions to this project are welcome! Feel free to submit pull requests or open issues for any bugs or improvements.

## License

This project is licensed under the MIT License.

## Author

Aditya Agarwal

GitHub: [aditya1123mp](https://github.com/aditya1123mp)
