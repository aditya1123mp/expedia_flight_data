import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Files
import java.nio.file.Paths

// Read the file
File file = new File("C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\Flight_Details.txt")
List<String> lines = file.readLines()

// Split the file content into individual datasets using the separator
List<List<String>> datasets = []
List<String> currentDataset = []

lines.each { line ->
	if (line.contains("--------------------------------------------------------")) {
		if (currentDataset) {
			datasets.add(currentDataset)
			currentDataset = []
		}
	} else {
		currentDataset.add(line)
	}
}

// Don't forget to add the last dataset
if (currentDataset) {
	datasets.add(currentDataset)
}

// Create a new Excel workbook and sheet
Workbook workbook = new XSSFWorkbook()
Sheet sheet = workbook.createSheet("Flight Details")

// Create header row
Row header = sheet.createRow(0)
header.createCell(0).setCellValue("Date")
header.createCell(1).setCellValue("Departure Time")
header.createCell(2).setCellValue("Arrival Time")
header.createCell(3).setCellValue("From")
header.createCell(4).setCellValue("To")
header.createCell(5).setCellValue("Flight Carrier Name")
header.createCell(6).setCellValue("Ticket Price")
header.createCell(7).setCellValue("Flight Duration")

// Variable to keep track of row number in the Excel file
int rowNum = 1

// Iterate over each dataset and extract the details
datasets.each { dataset ->
	String date = "Unknown"
	String departureTime = "Unknown"
	String arrivalTime = "Unknown"
	String fromLocation = "Unknown"
	String toLocation = "Unknown"
	String flightCarrier = "Unknown"
	String ticketPrice = "Unknown"
	String flightDuration = "Unknown"
	String stopDetails = ""

	dataset.eachWithIndex { line, index ->
		// Date extraction
		if (index == 0 && line.contains("Date:")) {
			date = line.split(":")[1].trim()
		}

		// Departure and Arrival Times extraction
		if (index == 2 && line.contains(" - ")) {
			def times = line.split(" - ")
			departureTime = times[0].trim()
			arrivalTime = times[1].trim() // Keep +1 in arrival time if present
		}

		// From and To location extraction
		if (index == 3 && line.contains(" - ")) {
			def locations = line.split(" - ")
			fromLocation = locations[0].trim()
			toLocation = locations[1].trim()
		}

		// Flight duration extraction, including stops if present
		if (index == 4 && (line.contains("h") || line.contains("m"))) {
			flightDuration = line.trim()
			// If the flight has stop details (e.g., "1 stop"), extract from index 5
			if (line.contains("stop") && dataset.size() > index + 1) {
				stopDetails = dataset[index + 1].trim() // Get the stop details from index 5
				flightDuration += " - " + stopDetails // Combine stop details with duration
			}
		}

		// Flight carrier extraction
		if (index == 5) {
			flightCarrier = line.trim() // No need for specific flight names, take as a whole
		}

		// Ticket price extraction with fallback for "2 left" cases
		if (index == 6 && line.contains("₹")) {
			// Normal case where price is in index 6
			ticketPrice = line.replaceAll("[^0-9₹,]", "").trim()
		} else if (index == 8 && line.contains("₹")) {
			// Fallback case where price is in index 7 due to "2 left at this price"
			ticketPrice = dataset[index].replaceAll("[^0-9₹,]", "").trim()
		}

		// Handle numeric conversion of ticket price by removing commas
		if (ticketPrice.contains("₹")) {
			// Remove comma and convert the string to a double
			String numericTicketPrice = ticketPrice.replace("₹", "").replace(",", "").trim()
			ticketPrice = numericTicketPrice // This will now be a numeric string without commas
		}
	}

	// Write the extracted data to the Excel file
	Row row = sheet.createRow(rowNum++)
	row.createCell(0).setCellValue(date)
	row.createCell(1).setCellValue(departureTime)
	row.createCell(2).setCellValue(arrivalTime)
	row.createCell(3).setCellValue(fromLocation)
	row.createCell(4).setCellValue(toLocation)
	row.createCell(5).setCellValue(flightCarrier)
	
	// Write ticket price as a number if it’s numeric
	if (ticketPrice.isNumber()) {
		row.createCell(6).setCellValue(ticketPrice.toDouble()) // Save as numeric value
	} else {
		row.createCell(6).setCellValue(ticketPrice) // Save as text if it’s not numeric
	}
	
	row.createCell(7).setCellValue(flightDuration)
}

// Save the Excel file
FileOutputStream fileOut = new FileOutputStream("C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\Flight_Details_data_updated.xlsx")
workbook.write(fileOut)
fileOut.close()
workbook.close()

println "All flight details saved to Excel with proper ticket price formatting!"
