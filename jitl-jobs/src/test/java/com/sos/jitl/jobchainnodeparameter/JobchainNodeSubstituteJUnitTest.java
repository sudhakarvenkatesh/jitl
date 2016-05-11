

package com.sos.jitl.jobchainnodeparameter;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstitute;
import com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteOptions;


public class JobchainNodeSubstituteJUnitTest extends JSToolBox {
	@SuppressWarnings("unused")	
	private final static String	CLASSNAME = "ConfigurationMonitorJUnitTest"; 
	@SuppressWarnings("unused")	
	private static final Logger	LOGGER = Logger.getLogger(JobchainNodeSubstituteJUnitTest.class);

	protected JobchainNodeSubstituteOptions	objOptions			= null;
	private JobchainNodeSubstitute objE = null;
	
	
	public JobchainNodeSubstituteJUnitTest() {
		//
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		objE = new JobchainNodeSubstitute();
		objE.registerMessageListener(this);
		objOptions = objE.getOptions();
		objOptions.registerMessageListener(this);
		
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() throws Exception {
		
		
		objE.Execute();
		
//		assertEquals ("auth_file", objOptions.auth_file.Value(),"test"); 
//		assertEquals ("user", objOptions.user.Value(),"test"); 


	}
}  // class ConfigurationMonitorJUnitTest