import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import javax.mail.*
import javax.mail.internet.*
import javax.activation.*
import java.util.Properties

// Define email properties
Properties props = new Properties()
props.put("mail.smtp.host", "smtp-mail.outlook.com") // Replace with your SMTP server
props.put("mail.smtp.port", "587") // Replace with your SMTP port
props.put("mail.smtp.auth", "true")
props.put("mail.smtp.starttls.enable", "true")

// Define email credentials
final String username = GlobalVariable.your_email // Replace with your email
final String password = GlobalVariable.your_email_password // Replace with your email password

// Create session
Session session = Session.getInstance(props, new Authenticator() {
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password)
	}
})

try {
	// Create a default MimeMessage object
	Message message = new MimeMessage(session)

	// Set From: header field
	message.setFrom(new InternetAddress(username))

	// Set To: header field
	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(GlobalVariable.recipient_email)) // Replace with recipient email

	// Set Subject: header field
	message.setSubject("latest automation run for flight data from benguluru to mumbai from expedia site By Aditya Agarwal")

	// Create the message part
	BodyPart messageBodyPart = new MimeBodyPart()
	
	// Include global variable in the message body
	String emailBody = "Please find the attached test results from the latest automation run for flight data from benguluru to mumbai from expedia site.\n\n"
	emailBody += "You can check the project details on GitHub: https://github.com/aditya1123mp/expedia_flight_data"
	messageBodyPart.setText(emailBody)

	// Create a multipart message
	Multipart multipart = new MimeMultipart()
	multipart.addBodyPart(messageBodyPart)

	// Attach the files
	String[] filenames = [
		"C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\amazon_ls_soundbar_pagination.xlsx",
		"C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\ls_speaker_data.txt"
	]

	for (String filename : filenames) {
		File file = new File(filename)
		println("Checking file: " + filename) // Log the file path
		if (file.exists()) {
			messageBodyPart = new MimeBodyPart()
			DataSource source = new FileDataSource(file)
			messageBodyPart.setDataHandler(new DataHandler(source))
			messageBodyPart.setFileName(file.getName())  // Only display file name in the email attachment
			multipart.addBodyPart(messageBodyPart)
			println("Attached file: " + file.getName())  // Log file attachment
		} else {
			println("File not found or inaccessible: " + filename)  // Log if a file is missing
		}
	}

	// Send the complete message parts
	message.setContent(multipart)

	// Send message
	Transport.send(message)

	println("Email sent successfully!")

} catch (MessagingException e) {
	println("Failed to send the email: " + e.message)
	e.printStackTrace()
}
//project expedia flight data automation
//By ADITYA_AGARWAL
//project