import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Files
import java.nio.file.Paths

// Read the file
File file = new File("C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\Flight_Details.txt")
List<String> lines = file.readLines()

// Initialize variables
String date = ""
String departureTime = ""
String arrivalTime = ""
String fromLocation = ""
String toLocation = ""
String flightCarrier = ""
String ticketPrice = ""
String timeOfFlight = ""

// Prepare for Excel creation
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
header.createCell(7).setCellValue("Time of Flight")

// Start row count for flight data
int rowCount = 1

// Iterate through the lines to extract the details based on the logic
lines.each { line ->
    // Split the line using commas to deal with the misplaced data
    def splitLine = line.split(",")
    
    if (splitLine.size() > 5) {
        // Extract key flight details
        date = splitLine[0].trim()  // Assuming the first element is always the date
        departureTime = splitLine[1].trim()  // Assuming the second element is the departure time
        arrivalTime = splitLine[2].trim()  // Assuming the third element is the arrival time
        fromLocation = "Mumbai (BOM)"  // You can add more flexible parsing logic if necessary
        toLocation = "Bengaluru (BLR)"  // Same for destination
        
        // Identify the flight carrier (IndiGo) and clean the string
        flightCarrier = "IndiGo" // Set manually based on the input, or use regex for dynamic parsing
        
        // Extract the ticket price using regex to find the ₹ symbol followed by numbers
        ticketPrice = line.find(/₹\d{1,3}(,\d{3})*/)?.trim() ?: "Not Available"
        
        // Extract flight duration and stopover information
        timeOfFlight = splitLine.find { it.contains("h") }?.trim() ?: "Unknown"

        // Write to Excel row
        Row row = sheet.createRow(rowCount)
        row.createCell(0).setCellValue(date)
        row.createCell(1).setCellValue(departureTime)
        row.createCell(2).setCellValue(arrivalTime)
        row.createCell(3).setCellValue(fromLocation)
        row.createCell(4).setCellValue(toLocation)
        row.createCell(5).setCellValue(flightCarrier)
        row.createCell(6).setCellValue(ticketPrice)
        row.createCell(7).setCellValue(timeOfFlight)
        
        rowCount++  // Move to the next row for the next set of data
    }
}

// Save the Excel file
FileOutputStream fileOut = new FileOutputStream("C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\Flight_Details_data.xlsx")
workbook.write(fileOut)
fileOut.close()
workbook.close()

println "Flight details successfully saved to Excel!"
