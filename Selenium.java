import org.openqa.selenium.By; import org.openqa.selenium.Keys; import org.openqa.selenium.WebDriver; import org.openqa.selenium.WebElement; import org.openqa.selenium.chrome.ChromeDriver; import org.openqa.selenium.chrome.ChromeOptions; import org.openqa.selenium.support.ui.ExpectedConditions; import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration; import java.time.LocalDate; import java.time.format.DateTimeFormatter; import java.util.HashMap; import java.util.List; import java.util.Map; import java.util.Scanner;

public class qgendaneu { public static String getValidDate(Scanner scanner, String dateType, String st_date) { String datePattern = "\d{2}/\d{2}/\d{4}"; // Regex for MM/DD/YYYY format String date; DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	while (true) {
		System.out.print("Enter the " + dateType + " (MM/DD/YYYY) or press Enter for none: ");
		date = scanner.nextLine().trim();
		// If no date is provided
		if (date.isEmpty()) {
			LocalDate currentDate = LocalDate.now();

			if ("start date".equalsIgnoreCase(dateType)) {
				// If the dateType is "start date", set it to today's date
				return currentDate.format(formatter);
			} else if ("end date".equalsIgnoreCase(dateType)) {
				if (st_date != null && !st_date.isEmpty()) {
					// If a "start date" is provided, calculate end date as last day of the start
					// date's month
					LocalDate startDate = LocalDate.parse(st_date, formatter);
					LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
					return endDate.format(formatter);
				} else {
					// If no "start date" is provided, set end date to last day of current month
					LocalDate endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
					return endDate.format(formatter);
				}
			}
		}

		// Validate the date format (MM/DD/YYYY)
		if (date.matches(datePattern)) {
			// Return the entered date if valid
			return date;
		} else {
			System.out.println("Invalid date format. Please use MM/DD/YYYY.");
		}
	}
}

public static void main(String[] args) throws InterruptedException {
	Scanner scanner = new Scanner(System.in);

	// Get valid start and end dates
	String startDate = getValidDate(scanner, "start date", null);
	String endDate = getValidDate(scanner, "end date", startDate);
	System.out.println(
			"Give the Path you want to download the Files or Click Enter to Download in Default location:");

	String path = scanner.nextLine();

	ChromeOptions options = new ChromeOptions();
	Map<String, Object> prefs = new HashMap<String, Object>();
	prefs.put("download.default_directory", path);
	options.setExperimentalOption("prefs", prefs);
	test(startDate, endDate, options);

}

public static void test(String start_date, String end_date, ChromeOptions options) throws InterruptedException {
	// Set up WebDriver
	ChromeOptions Options = new ChromeOptions();  
	 Options.addArguments("--headless");  // Run in headless mode
      Options.addArguments("--disable-gpu");  // Necessary for some OS
      Options.addArguments("--window-size=1920,1080");// Set window size

// Options.addArguments("--headless=new"); // Options.addArguments("--no-sandbox"); // Options.addArguments("--disable-dev-shm-usage"); // Options.addArguments("--disable-gpu"); // Options.addArguments("--disable-extensions"); // Options.addArguments("--window-size=1920,1080");

	WebDriver driver = new ChromeDriver(Options);
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	driver.get("https://app.qgenda.com/landingpage/rpvravh");
	List<WebElement> links = driver.findElements(By.cssSelector("div[class='link-section indent'] a"));
	for (int i = 0; i < links.size(); i++) {
		// Re-locate the links after each navigation
		links = driver.findElements(By.cssSelector("div[class='link-section indent'] a"));
		// Click on the current link
		WebElement link = links.get(i);
		System.out.println("Navigating to: " + link.getText());
		link.click();
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("input[id='react-select-taskFilter-input']")))
				.sendKeys("VH");
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".buttonBase-0-2-31.outlineBrand-0-2-34.regularButtonSize-0-2-49"))).click();
		List<WebElement> vhVraElements1 = driver.findElements(By.xpath("//div[contains(text(),'VH VRA')]"));
		for (WebElement vhVraElement : vhVraElements1) {
			try {
				System.out.println("Clicking 'VH VRA' element: " + vhVraElement.getText());
				vhVraElement.click();
			} catch (Exception e) {
				System.out.println("Error clicking 'VH VRA' element: " + vhVraElement.getText());
				e.printStackTrace();
			}
		}
		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.cssSelector(".buttonBase-0-2-31.outlineBrand-0-2-34.regularButtonSize-0-2-49"))).click();
		} catch (Exception e) {
			driver.findElement(By.id("qgenda-select-task-filter-select")).click();
		}
		try {
			WebElement reportsButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='reportsButton']")));
			reportsButton.click();

			System.out.println("Reports button clicked successfully.");
		} catch (Exception e) {
			System.out.println("Failed to click on the reports button.");
			e.printStackTrace();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.id("reportsModalReportTypeDropdown"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Grid By Staff')]")))
				.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"//body/div[@id='wrapper']/main[@id='content']/div[@id='react-app']/div[@data-radium='true']/div[@id='reportsModal']/div[@class='qgenda-modal-body']/div/div[2]/div[1]/div[1]")))
				.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Excel')]"))).click();
		WebElement startDate = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@aria-label='Start Date']")));
		startDate.click();
		startDate.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
		startDate.sendKeys(Keys.BACK_SPACE); // Clear the field
		System.out.println("Start date field cleared.");
		startDate.sendKeys(start_date); // Enter new date
		System.out.println("Start date set.");

		// Locate the end date input field
		WebElement endDate = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@aria-label='End Date']")));
		endDate.click();
		endDate.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
		endDate.sendKeys(Keys.BACK_SPACE); // Clear the field
		System.out.println("End date field cleared.");
		endDate.sendKeys(end_date); // Enter new date
		System.out.println("End date set.");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"(//div[@class='qgenda-select__value-container qgenda-select__value-container--has-value css-1tly8xx'])[3]")))
				.click();
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//div[contains(text(),'Last Name, First Name')]"))).click();
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("div[id ='reportsModalRunReportButton']"))).click();
		Thread.sleep(1000);

		driver.navigate().back();
	}
	// Close the browser
	driver.quit();
}
