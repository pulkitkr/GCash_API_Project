package com.GCash_GGivesScripts;

import com.business.gCASH.GCASHBusinessLogic;
import com.extent.ExtentReporter;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.utility.Utilities;

import java.io.IOException;

public class GCASHScripts {
//Hello	
	private com.business.gCASH.GCASHBusinessLogic GCASHBusiness;

	@BeforeTest
	public void Before() throws InterruptedException {
		GCASHBusiness = new com.business.gCASH.GCASHBusinessLogic("gcash");
	}

	@Test(priority = 0)
	@Parameters({ "APIUrl" })
	public void GCashToken(String url) throws Exception {
		GCASHBusiness.TokenGCash_200(url);
		ExtentReporter.jiraID = "PP-41";
	}
//Hello
	@Test(priority = 1)
	@Parameters({ "APIUrl"})
	public void GCashClientId(String APIUrl) throws Exception {
		GCASHBusiness.InvalidClientId_TokenGCash(APIUrl);
		ExtentReporter.jiraID = "PP-43";
	}

	@Test(priority = 2)
	@Parameters({ "APIUrl" })
	public void GCashEmptyId(String APIUrl) throws Exception {
		GCASHBusiness.EmptyClientId_TokenGCash(APIUrl);
		ExtentReporter.jiraID = "PP-44";
	}

	@Test(priority = 3)
	@Parameters({ "APIUrl" })
	public void GCashSecretId(String APIUrl) throws Exception {
		GCASHBusiness.EmptyClientSecret_TokenGCash(APIUrl);
		ExtentReporter.jiraID = "PP-45";
	}

	@Test(priority = 4)
	@Parameters({ "APIUrl" })
	public void GCashInvalidSecret(String APIUrl) throws Exception {
		GCASHBusiness.InvalidClientSecret_TokenGCash(APIUrl);
		ExtentReporter.jiraID = "PP-46";
	}

	@Test(priority = 5)
	@Parameters({ "APIUrl" })
	public void GCashGrant(String APIUrl) throws Exception {
		GCASHBusiness.InvalidGrantType_TokenGCash(APIUrl);
		ExtentReporter.jiraID = "PP-47";
	}

	@Test(priority = 6)
	@Parameters({ "APIUrl" })
	public void GCashEmptyGrant(String APIUrl) throws Exception {
		GCASHBusiness.EmptyGrantType_TokenGCash(APIUrl);
		ExtentReporter.jiraID = "PP-48";
	}

}
