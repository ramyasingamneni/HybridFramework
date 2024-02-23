package driverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	public static WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	String inputpath = "./FileInput/DataEngine.xlsx";
	String outputpath = "./FileOutput/HybridResults.xlsx";

	public void startTest() throws Throwable {
		String Modulestatus = "";
		// create object for excelfileutil class
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		String TestCases = "MasterTestCases";
		// iterate all rows in Testcases sheet
		for (int i = 1; i <= xl.rowCount(TestCases); i++) {
			if (xl.getCellData(TestCases, i, 2).equalsIgnoreCase("Y")) {
				// read all test case or corresponding sheets
				String TCModule = xl.getCellData(TestCases, i, 1);
				// define path of html
				report = new ExtentReports("./target/Reports/" + TCModule + FunctionLibrary.generateDate() + ".html");
				logger = report.startTest(TCModule);
				// iterate all rows in TCModule sheet
				for (int j = 1; j <= xl.rowCount(TCModule); j++) {
					// read all cells from TCModule
					String Description = xl.getCellData(TCModule, j, 0);
					String Object_Type = xl.getCellData(TCModule, j, 1);
					String Locator_Type = xl.getCellData(TCModule, j, 2);
					String Locator_Value = xl.getCellData(TCModule, j, 3);
					String Test_Data = xl.getCellData(TCModule, j, 4);
					try {
						if (Object_Type.equalsIgnoreCase("startBrowser")) {
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("openUrl")) {
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("waitForElement")) {
							FunctionLibrary.waitForElement(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("typeAction")) {
							FunctionLibrary.typeAction(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("clickAction")) {
							FunctionLibrary.clickAction(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("validateTitle")) {
							FunctionLibrary.validateTitle(Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("dropDownAction")) {
							FunctionLibrary.dropDownAction(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("captureStockNum")) {
							FunctionLibrary.captureStockNum(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("stockTable")) 
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureSup"))
						{
							FunctionLibrary.captureSup(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureCus"))
						{
							FunctionLibrary.captureCus(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						// write as pass into status cell in TCModule
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Modulestatus = "True";
					} catch (Exception e) {
						System.out.println(e.getMessage());
						// write as Fail into status cell in TCModule
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Modulestatus = "False";
					}
					if (Modulestatus.equalsIgnoreCase("true")) {
						// write as pass into Test cases sheet
						xl.setCellData(TestCases, i, 3, "Pass", outputpath);
					} else {
						// write as Fail into Test cases sheet
						xl.setCellData(TestCases, i, 3, "Fail", outputpath);
					}
					report.endTest(logger);
					report.flush();
				}
			} else {
				// write as blocked into status cell for Flag N
				xl.setCellData(TestCases, i, 3, "Blocked", outputpath);
			}
		}
	}
}
