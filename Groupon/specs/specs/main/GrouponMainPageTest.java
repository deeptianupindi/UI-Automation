package specs.main;

//import static org.junit.Assert.*;

//import org.junit.Test;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import com.main.GrouponMainPage;

@RunWith(ConcordionRunner.class)
public class GrouponMainPageTest {

    public String initializeDriverWithSampleUrl(String url) {
    	GrouponMainPage pageParser = new GrouponMainPage();    	
    	String pageTitle = pageParser.initializeDriver(url);
    	pageParser.getDriver().close();
    	return pageTitle;
    }
        
    public String initializeDriverWithEmptyUrl(String url) {
    	GrouponMainPage pageParser = new GrouponMainPage();    	
    	String pageTitle = pageParser.initializeDriver(url);
    	pageParser.getDriver().close();
    	return pageTitle;
   	
    }

    public String navigateToAllDealsAndGetPageTitle(String testEmail, String testZip) {
    	GrouponMainPage pageParser = new GrouponMainPage();  
    	pageParser.initializeDriver("http://www.groupon.com");
    	String pageTitle = pageParser.navigateToAllDeals(testEmail, testZip);
    	pageParser.getDriver().close();
    	return pageTitle;
   	
    }

}


