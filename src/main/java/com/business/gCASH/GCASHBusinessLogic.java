package com.business.gCASH;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.Datasheet.GCashAPI_TestData_DataProvider;
import io.restassured.response.ValidatableResponse;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.driverInstance.CommandBase;
import com.extent.ExtentReporter;
import com.mixpanelValidation.Mixpanel;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class GCASHBusinessLogic extends Utilities {

	public GCASHBusinessLogic(String Application) throws InterruptedException {
		new CommandBase(Application);
		init();
	}
	GCashAPI_TestData_DataProvider dataProvider = new GCashAPI_TestData_DataProvider();

	private int timeout;
	SoftAssert softAssertion = new SoftAssert();
	boolean launch = "" != null;
	/** Retry Count */
	private int retryCount;
	Mixpanel mixpanel = new Mixpanel();
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */

	static LoggingUtils logger = new LoggingUtils();

	String language = "abcdefghijklmnopqrstuvwxyz";

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;

	/** The Android driver. */
	public IOSDriver<WebElement> iOSDriver;

	static String code = "";
	String asset_SubType = "";
	String assetType = "";
	Set<String> hash_Set = new HashSet<String>();
	String viewAllTrayname = "";
	@SuppressWarnings("unused")
	private String LacationBasedLanguge;

	public String titleLanguage = "";

	List<String> LocationLanguage = new ArrayList<String>();

	List<String> DefaultLanguage = new ArrayList<String>();

	List<String> SelectedCONTENTLanguageInWelcomscreen = new ArrayList<String>();

	List<String> SelectedCONTENTLanguageInHamburgerMenu = new ArrayList<String>();

	Response resp;

	ArrayList<String> MastheadTitleApi = new ArrayList<String>();
	public String onboardingTraytitle = "";
	public String onboardingPremiumContenttitle = "";

	public static boolean relaunchFlag = false;
	public static boolean appliTools = false;

	public static boolean PopUp = false;

	private static String IP = "-s 192.168.0.89:5555";

	public String title = "";

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String carouselTitle = "";
	public String carouselDescription = "";
	public String trayTitle = "";

	public String contentName = "";

	public String titleDescription = "";

	String userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	String parentpasswordNonSub = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
			.getParameter("NonsubscribedPassword");

	String parentpasswordSub = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
			.getParameter("SubscribedPassword");

	/**
	 * Initiate Property File.
	 *
	 * @param byLocator the by locator
	 */

	public void init() {
		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info(
				"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void tearDown() {
		getDriver().quit();
	}

	public void TokenGCash_200(String url) throws IOException {
		extent.HeaderChildNode("Api-TokenGCash");
        Object[][] data =  dataProvider.GCashapi("Gcashapi_ValidData");
        ValidatableResponse response = TokenGCashAPI(data, url);

        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 200);
        logger.info("Response Status Code :" +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);

        String Token =response.extract().body().jsonPath().get("token_type");
        Assert.assertEquals(Token, "bearer");
        logger.info("Token is : " + Token);
        extent.extentLogger("Token", "Token is : " + Token);

        String access_token =response.extract().body().jsonPath().get("access_token");
        Assert.assertTrue(access_token !=null);
        logger.info("Access_Token is: "+access_token);
        extent.extentLogger("Access_Token", "Access_Token is:"+access_token);
        
        int expires_in =response.extract().body().jsonPath().get("expires_in");
        logger.info("expires in Code= "+expires_in);
        extent.extentLogger("Expire_Code", "expires in Code= "+expires_in);
        Assert.assertEquals(expires_in, 7200);

        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_200_scehma.json"));
    }


    public void InvalidClientId_TokenGCash(String url) throws IOException {
    	extent.HeaderChildNode("Api-InvalidClientId_TokenGCash");

        Object[][] data =  dataProvider.GCashapi("Gcashapi_InvalidClientId");
        ValidatableResponse response = TokenGCashAPI(data, url);

        //Verify Status Code
        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 400);
        logger.info("Response Status Code: " +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);

        //Verify Body
        String Token =response.extract().body().jsonPath().get("error");
        Assert.assertEquals(Token, "invalid_client");
        logger.info("Token is: " + Token);
        extent.extentLogger("Token", "Token is: " + Token);

        String access_token =response.extract().body().jsonPath().get("error_description");
        Assert.assertEquals(access_token, "Invalid client authentication");
        logger.info("Access token is: "+access_token);
        extent.extentLogger("Access token", "Access token is: "+access_token);

        //Verify Schema
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_400_schema.json"));
    }


    public void EmptyClientId_TokenGCash(String url) throws IOException {
    	extent.HeaderChildNode("Api-EmptyClientId_TokenGCash");
    	
        Object[][] data =  dataProvider.GCashapi("Gcashapi_EmptyClientId");
        ValidatableResponse response = TokenGCashAPI(data, url);

        //Verify Status Code
        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 400);
        logger.info("Response Status Code: " +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);

        String Token =response.extract().body().jsonPath().get("error");
        Assert.assertEquals(Token, "invalid_client");
        logger.info("Token is: " + Token);
        extent.extentLogger("Token", "Token is: " + Token);

        String access_token =response.extract().body().jsonPath().get("error_description");
        Assert.assertEquals(access_token, "Invalid client authentication");
        logger.info("Access Token is: "+access_token);
        extent.extentLogger("Access Token", "Access Token is: "+access_token);

        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_400_schema.json"));
    }

    public void EmptyClientSecret_TokenGCash(String url) throws IOException {
    	extent.HeaderChildNode("Api-EmptyClientSecret_TokenGCash");
    	
        Object[][] data =  dataProvider.GCashapi("Gcashapi_EmptyClientSecret");
        ValidatableResponse response = TokenGCashAPI(data, url);

        //Verify Status Code
        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 400);
        logger.info("Response Status Code: " +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);

        String Token =response.extract().body().jsonPath().get("error");
        Assert.assertEquals(Token, "invalid_client");
        logger.info("Token is: " + Token);
        extent.extentLogger("Token", "Token is: " + Token);

        String access_token =response.extract().body().jsonPath().get("error_description");
        Assert.assertEquals(access_token, "Invalid client authentication");
        logger.info("Access Token is: "+access_token);
        extent.extentLogger("Access token", "Access Token is: "+access_token);
        
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_400_schema.json"));
    }

    public void InvalidClientSecret_TokenGCash(String url) throws IOException {
    	extent.HeaderChildNode("Api-InvalidClientSecret_TokenGCash");

        Object[][] data =  dataProvider.GCashapi("Gcashapi_InvalidClientSecret");
        ValidatableResponse response = TokenGCashAPI(data, url);

        //Verify Status Code
        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 400);
        logger.info("Response Status Code: " +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);

        String Token =response.extract().body().jsonPath().get("error");
        Assert.assertEquals(Token, "invalid_client");
        logger.info("Token is: " + Token);
        extent.extentLogger("Token", "Token is: " + Token);

        String access_token =response.extract().body().jsonPath().get("error_description");
        Assert.assertEquals(access_token, "Invalid client authentication");
        logger.info("Access Token is: "+access_token);
        extent.extentLogger("Access token", "Access Token is: "+access_token);
        
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_400_schema.json"));


    }

    public void InvalidGrantType_TokenGCash(String url) throws IOException {
    	extent.HeaderChildNode("Api-InvalidGrantType_TokenGCash");
    	
        Object[][] data =  dataProvider.GCashapi("Gcashapi_InvalidGrantType");
        ValidatableResponse response = TokenGCashAPI(data, url);

        //Verify Status Code
        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 400);
        logger.info("Response Status Code: " +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);

        String Token =response.extract().body().jsonPath().get("error");
        Assert.assertEquals(Token, "unsupported_grant_type");
        logger.info("Token is: " + Token);
        extent.extentLogger("Token", "Token is: " + Token);

        String access_token =response.extract().body().jsonPath().get("error_description");
        Assert.assertEquals(access_token, "Invalid grant_type");
        logger.info("Access Token is: "+access_token);
        extent.extentLogger("Access token", "Access Token is: "+access_token);
        
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_400_schema.json"));


    }

    public void EmptyGrantType_TokenGCash(String url) throws IOException {
    	extent.HeaderChildNode("Api-EmptyGrantType_TokenGCash");

        Object[][] data =  dataProvider.GCashapi("Gcashapi_EmptyGrantType");
        ValidatableResponse response = TokenGCashAPI(data, url);

        //Verify Status Code
        int responseToken = response.extract().statusCode();
        String responseTokenString = String.valueOf(responseToken);
        Assert.assertEquals(response.extract().statusCode(), 400);
        logger.info("Response Status Code: " +response.extract().statusCode());
        extent.extentLogger("Response Code", "Response Status Code: " +responseTokenString);
       
        
        String Token =response.extract().body().jsonPath().get("error");
        Assert.assertEquals(Token, "unsupported_grant_type");
        logger.info("Token is: " + Token);
        extent.extentLogger("Token", "Token is: " + Token);

        String access_token =response.extract().body().jsonPath().get("error_description");
        Assert.assertEquals(access_token, "Invalid grant_type");
        logger.info("Access Token is: "+access_token);
        extent.extentLogger("Access token", "Access Token is: "+access_token);
        
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("gcash_400_schema.json"));

    }
}
	