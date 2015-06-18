package lm.com.brainhoney.web;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lm.com.brainhoney.dao.GeneralDao;
import lm.com.brainhoney.model.Domain;
import lm.com.brainhoney.model.Models;
import lm.com.brainhoney.model.Session;
import lm.com.brainhoney.model.User;


import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;







/*import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.reporting.ReportOutputFormat;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
/*import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;*/

@Controller
public class BrainHoneyController {
	String message = "";
	Document result = null;
	
	@Autowired
	Session session;
	
	@Autowired
    private ServletContext servletContext;
	
	@Autowired
	private GeneralDao generalDao;	
    
    Models model;
    
    @Autowired
    User user;
    @Autowired
    Domain domain;
	 
	@RequestMapping("/hello")
	public ModelAndView showMessage(
			@RequestParam(value = "page", required = false, defaultValue = "login") String page) {
		
		ModelAndView mv = new ModelAndView(page);
		mv.addObject("message", message);		
		return mv;
	}
	
	@RequestMapping("/login")
	public ModelAndView loginUser(
			@RequestParam(value = "prefixName", required = true) String prefixName,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password) throws ParserConfigurationException, TransformerException, IOException, SAXException {
		// The DLAP server uses cookies, so be sure to keep them
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        
		result = session.Login(prefixName, userName, password);
		
		/*try {
		    URL myURL = new URL("http://gls.agilix.com/SSOLogin?domainid=27237470&url=http%3A%2F%2Fwww.myschool.edu/logincompleted?token%3D%25TOKEN%25%26state%3D%25STATE%25");
		    URLConnection myURLConnection = myURL.openConnection();
		    myURLConnection.connect();
		} 
		catch (MalformedURLException e) { 
		    // new URL() failed
		    // ...
		} 
		catch (IOException e) {   
		    // openConnection() failed
		    // ...
		}*/
		
//        result = session.Login("sinet-lm-dev", "administrator", "g#c9=WW9");
        if (!Session.IsSuccess(result))
        {
            System.out.println("Unable to login: " + Session.GetMessage(result));
            message = "Unable to login: " + Session.GetMessage(result);	
            
            // return response
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("message", message);
            message = "";
            return mv;
        }
        
        //	persist into database.        
        generalDao.insertUser(prefixName, userName, password);
        
        Element userElement = (Element)result.getElementsByTagName("user").item(0);
        String domainid = userElement.getAttribute("domainid");
        // Query the user
        Map<String, String> getuserMap = new HashMap<String, String>();
        getuserMap.put("domainid", domainid);
        
        // generating JSON for grid view
        String formatedUserJson = session.generateJsonFromXml(session.Get("listusers", getuserMap), model = user);
        
        String formatedDomainJson = session.generateJsonFromXml(session.Get("listdomains", getuserMap), model = domain);
        
        message = "Login successfully.";
        
        // return response
        ModelAndView mv = new ModelAndView("actions");
        mv.addObject("message", message);
        mv.addObject("tabIndex", 0);
        mv.addObject("listUser", formatedUserJson);
        mv.addObject("listDomain", formatedDomainJson);
        message = "";
		return mv;		
	}
	
	@RequestMapping("/createUser")
	public ModelAndView createUser(
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password) throws TransformerException, IOException, ParserConfigurationException, SAXException {
		// The DLAP server uses cookies, so be sure to keep them
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        
		result = session.Login("sinet-lm-dev", "administrator", "g#c9=WW9");

		// Get the domain id
        Element userElement = (Element)result.getElementsByTagName("user").item(0);
        String domainid = userElement.getAttribute("domainid");
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document createusersDoc = docBuilder.newDocument();
        Element requestsElement = createusersDoc.createElement("requests");
        createusersDoc.appendChild(requestsElement);
        Element newUserElement = createusersDoc.createElement("user");
        requestsElement.appendChild(newUserElement);
        newUserElement.setAttribute("username", userName);
        newUserElement.setAttribute("password", password);
        newUserElement.setAttribute("firstname", firstName);
        newUserElement.setAttribute("lastname", lastName);
        newUserElement.setAttribute("domainid", domainid);
        result = session.Post("createusers", createusersDoc);
       
        if (!Session.IsSuccess(result))
        {
            System.out.println("Unable to create user: " + Session.GetMessage(result));
            message = "Unable to create user: " + Session.GetMessage(result) + "";		            
             
            // return response         
            ModelAndView mv = new ModelAndView("actions");
            mv.addObject("message", message);
            mv.addObject("tabIndex", 1);
            message = "";
            return mv;
        }

        // Get the user's id
        userElement = (Element)result.getElementsByTagName("user").item(0);
        String userid = userElement.getAttribute("userid");

        // Query the user
        Map<String, String> getuserMap = new HashMap<String, String>();
        getuserMap.put("userid", userid);
        result = session.Get("getuser", getuserMap);
        
        message += "User created successfully. (" + userName + ")"; //Session.getSuccessMessage(result);		        
        
        // return response
        ModelAndView mv = new ModelAndView("actions");
        mv.addObject("message", message);
        mv.addObject("tabIndex", 1);
        message = "";
        return mv;
	}

	@RequestMapping("/createDomain")
	public ModelAndView createDomain(			
			@RequestParam(value = "reference", required = true) String reference,
			@RequestParam(value = "domainName", required = true) String domainName,
			@RequestParam(value = "userspace", required = true) String userspace) throws TransformerException, IOException, ParserConfigurationException, SAXException {
		// The DLAP server uses cookies, so be sure to keep them
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        
		// Create Domain        		
		result = session.Login("sinet-lm-dev", "administrator", "g#c9=WW9");
		// Get the domain id
        Element userElement = (Element)result.getElementsByTagName("user").item(0);
        String domainid = userElement.getAttribute("domainid");
        
        Map<String, String> getDomainMap = new HashMap<String, String>();
        getDomainMap.put("domainid", domainid);
        result = session.Get("listdomains", getDomainMap);
        System.out.println(session.getSuccessMessage(result));
        
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document createDomainDoc = docBuilder.newDocument();
        Element requestsElement = createDomainDoc.createElement("requests");
        createDomainDoc.appendChild(requestsElement);
        Element newDomainElement = createDomainDoc.createElement("domain"); //27237470
        requestsElement.appendChild(newDomainElement);
        newDomainElement.setAttribute("name", domainName);
        newDomainElement.setAttribute("userspace", userspace);
        newDomainElement.setAttribute("parentid", domainid);
        newDomainElement.setAttribute("reference", reference);		        
        result = session.Post("createdomains", createDomainDoc);
        
        if (!Session.IsSuccess(result))
        {
            System.out.println("Unable to create domain: " + Session.GetMessage(result));
            message = "Unable to create domain: " + Session.GetMessage(result) + "";
            		             
            // return response
            ModelAndView mv = new ModelAndView("actions");
            mv.addObject("message", message);
            mv.addObject("tabIndex", 0);
            message = "";
            return mv;
            
        }

        // Query the domain
        Map<String, String> getuserMap = new HashMap<String, String>();
        getuserMap.put("domainid", domainid);        
        
        // generating JSON for grid view     
        String formatedUserJson = session.generateJsonFromXml(session.Get("listusers", getuserMap), model = user);
     
        String formatedDomainJson = session.generateJsonFromXml(session.Get("listdomains", getuserMap), model = domain);
        
        message = "Domain created successfully. (" + domainName + ")"; //Session.getSuccessMessage(result);		        
    		        
       
		// return response
        ModelAndView mv = new ModelAndView("actions");
        mv.addObject("message", message);
        mv.addObject("tabIndex", 0);
        mv.addObject("listUser", formatedUserJson);
        mv.addObject("listDomain", formatedDomainJson);
        message = "";
        return mv;
	}
	
	/*@SuppressWarnings("static-access")
	@RequestMapping("/jasperReport")
	public ModelAndView createReport(
			@RequestParam(value = "reportPage") String page) {				
		JasperPrint jasperPrint = null;
		DefaultTableModel tableModel = TableModelData();
		
		try {			
			JasperCompileManager.compileReportToFile(session.getFile() + "/" + page + ".jrxml", session.getFile() + "/" + page + ".jasper");
            jasperPrint = JasperFillManager.fillReport(session.getFile() + "/" + page + ".jasper", new HashMap(),
                    new JRTableModelDataSource(tableModel));
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.viewReport(jasperPrint, false);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
		
		ModelAndView mv = new ModelAndView("message");		
		return mv;
	}*/
	
	@RequestMapping("/runJRS")
	public ModelAndView runJRS(
			@RequestParam(value = "reportPage") String page) {
		
		// URL on which jasper server is ruuning
        String serverURL = "http://192.168.2.86:8080/jasperserver";
		RestClientConfiguration configuration = new RestClientConfiguration(serverURL);
		
		//	Client installation
		JasperserverRestClient client = new JasperserverRestClient(configuration);
		
		//	Authentication
		try {
		com.jaspersoft.jasperserver.jaxrs.client.core.Session jsSession = client.authenticate("jasperadmin", "jasperadmin");
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		OperationResult<InputStream> result = client
		        .authenticate("jasperadmin", "jasperadmin")
		        .reportingService()
		        .report("/reports/samples/Cascading_multi_select_report")
		        .prepareForRun(ReportOutputFormat.HTML, 1)
		        .parameter("Cascading_name_single_select", "A & U Stalker Telecommunications, Inc")
		        .run();
		InputStream report = result.getEntity();
		
		byte[] buffer;
		try {
			buffer = new byte[report.available()];
			report.read(buffer);
	        String in = new String(buffer, "cp1252");
	       
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File("e:/reports/saheb.html")));
	        out.writeUTF(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
		ModelAndView mv = new ModelAndView(message);
		return mv;
	}
	
	@RequestMapping("/runReport")
	public ModelAndView runReport(
			@RequestParam(value = "reportPage") String page) {
		
		// URL on which jasper server is ruuning
        String serverURL = "http://192.168.2.86:8080/jasperserver/";
        // Report name (in jasper sever we have some sample reports I am using some sample report for the example, below is the path to the report)
        String reportName = "reports/samples/Department";
        String reportFormat = "HTML";
        
        // Use HTTP clinet,POST,PUT methods to consume REST service of the jasper server
        
//        ClientConfig config = new DefaultClientConfig();
        
        // Setting Login URL in a POST method
        /*String loginURL = "rest/login";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(serverURL + loginURL);

        // Set authentication parameters(To connect the jasper server through post methods)
        postMethod.addParameter("j_username", "jasperadmin");
        postMethod.addParameter("j_password", "jasperadmin");

        // Send POST with login request
        int statusCode;
		try {
			statusCode = client.executeMethod(postMethod);
			message = "Jasper server login successful: " + postMethod.getResponseBodyAsString();
			//  Check correct login process
	        if (statusCode != HttpStatus.SC_OK) {
	        	message = "Login failed: " + postMethod.getStatusLine();
	            ModelAndView mv = new ModelAndView("message");
	            mv.addObject("message", message);
	            return mv;
	        }
		}
		catch (HttpException e) {}		
		catch (IOException e) {}

		// Settting resource URL in a GET method to get descriptor                       
        String resourceURL = serverURL+"rest/resource/"+reportName;
        GetMethod getMethod = new GetMethod(resourceURL);

        // Send GET request for descriptor
        try {
			statusCode = client.executeMethod(getMethod);
			
			//  Check correct descriptor process
            if (statusCode != HttpStatus.SC_OK) {
            	System.out.println("Descriptor failed: " + getMethod.getStatusLine());
            	message = "Descriptor failed: " + getMethod.getStatusLine();
            	ModelAndView mv = new ModelAndView("message");
	            mv.addObject("message", message);
	            return mv;
            }
            
            // Get the response body as String
            String descriptorSource = getMethod.getResponseBodyAsString();

            //Transform descriptor from String into XML Document
            Document descriptorXML = stringToDom(descriptorSource);
            message = "doc:" + descriptorSource;
            
            // Settting PUT method to run report, the url contains the RUN_OUTPUT_FORMAT parameter
            String reportURL = serverURL+"rest/report/"+reportName+"?RUN_OUTPUT_FORMAT="+reportFormat;
            PutMethod putMethod = new PutMethod(reportURL);
            
         // Setting the request Body. The descriptor XML Document is transform to String and add to the request body.
            try {
				putMethod.setRequestBody(domToString(descriptorXML));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            // Send PUT request to execute the report.
            statusCode = client.executeMethod(putMethod);

            //  Check correct report process
            if (statusCode != 201) {
            	message = "Report failed: " + putMethod.getStatusLine();
            	ModelAndView mv = new ModelAndView("message");
	            mv.addObject("message", message);
	            return mv;
            }
            
            // Get the response body
            String reportSource = putMethod.getResponseBodyAsString();
            // Transform report information into XML Document.
            Document reportXML = stringToDom(reportSource);
            message = reportXML.toString();
            
            // Extrac from XML Document the report's UUID
            NodeList nodes = reportXML.getElementsByTagName("uuid");
            String reportUuid = nodes.item(0).getTextContent();
            
            // Setting GET request to download the report
            String reportFileURL = serverURL+"rest/report/"+reportUuid;//+"?file=report";
            GetMethod getMethodFile = new GetMethod( reportFileURL );
            
            //Send GET request to download the report
            statusCode = client.executeMethod(getMethodFile);
            
            //  Check correct report process
            if (statusCode != 200) {
            	System.out.println("Downlaod failed: " + putMethod.getStatusLine());
            }
            // Getting the report body            
            InputStream is=  getMethodFile.getResponseBodyAsStream();
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String in = new String(buffer, "cp1252");
           
			DataOutputStream out = new DataOutputStream(new FileOutputStream(session.getFile() + "/" + "hai.txt"));
            out.writeUTF(in);
            int read = 0;
            while ((read = is.read(buffer)) != -1) {
    			out.write(buffer, 0, read);
    		}
           
            System.out.println("Mithun");
		} catch (HttpException e) {} 
        catch (IOException e) {} 
        catch (SAXException e) {} 
        catch (ParserConfigurationException e) {}*/
        
        /*String input = "{\"j_username\":\"jasperadmin\",\"j_password\":\"jasperadmin\"}";
        ClientResponse response = webResource.path(loginURL).type("application/json")                
     		   .post(ClientResponse.class, input);
      
 		if (response.getStatus() != 201) {
 			throw new RuntimeException("Failed : HTTP error code : "
 			     + response.getStatus());
 		}
  
 		System.out.println("Output from Server .... \n");
 		String output = response.getEntity(String.class);
 		System.out.println(output);*/
     		
     // Return code should be 201 == created resource
//        System.out.println(response);        
		
		ModelAndView mv = new ModelAndView("message");
		mv.addObject("message", message);
		return mv;
	}
	
	
	@RequestMapping("/logout")
	public ModelAndView createDomain() {
		// The DLAP server uses cookies, so be sure to keep them
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        
		message = "User logout successfully.";
		// return response
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("message", message);
        message = "";
        return mv;
	}
	
	private DefaultTableModel TableModelData() {
        String[] columnNames = {"Id", "Name", "Department", "Email"};
        String[][] data = {
            {"111", "G Conger", " Orthopaedic", "jim@wheremail.com"},
            {"222", "A Date", "ENT", "adate@somemail.com"},
            {"333", "R Linz", "Paedriatics", "rlinz@heremail.com"},
            {"444", "V Sethi", "Nephrology", "vsethi@whomail.com"},
            {"555", "K Rao", "Orthopaedics", "krao@whatmail.com"},
            {"666", "V Santana", "Nephrology", "vsan@whenmail.com"},
            {"777", "J Pollock", "Nephrology", "jpol@domail.com"},
            {"888", "H David", "Nephrology", "hdavid@donemail.com"},
            {"999", "P Patel", "Nephrology", "ppatel@gomail.com"},
            {"101", "C Comer", "Nephrology", "ccomer@whymail.com"}
        };
        return new DefaultTableModel(data, columnNames);
    }
	
	public static Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {
        
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlSource));

		Document doc = db.parse(is);
		
		return doc;
	}
	
	public static String domToString( Document xml ) throws Exception{
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xml), new StreamResult(writer));
        return writer.getBuffer().toString();
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	
}
