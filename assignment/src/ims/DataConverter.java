package ims;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.thoughtworks.xstream.XStream;

import customer.Customer;
import customer.General;
import customer.Student;
import product.MovieTicket;
import product.ParkingPass;
import product.Product;
import product.Refreshment;
import product.SeasonPass;

/**
 * The DataConverter class parses and stores data in newly created objects 
 * from the old Invoice System located on flat files.  Then the objects are 
 * serialized into both XML and JSON with the use of appropriate external 
 * libraries. 
 * 
 * @authors Reid Stagemeyer and Dat Nguyen 
 * @version 3.0
 * added: 
 * 	synchronize object from database to Arraylist of person, customer, product, invoice.
 * @since 2018-05-24
 * 
 */

public class DataConverter {
	
	// number of entries in flat files
	private static int NUM_OF_CUSTOMER;
	private static int NUM_OF_PERSON;
	private static int NUM_OF_PRODUCT;
	private static int NUM_OF_INVOICE;
	
	/* Lists created to store created Person, Customer, and Product objects 
	 * respectively.
	 */
	private static ArrayList<Person> persons = new ArrayList<Person>();
	private static ArrayList<Customer> customers = new ArrayList<Customer>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<Invoice> invoices = new ArrayList<Invoice>();
	
	
	public static void flatDataToObject(){
		
		//read and parse files, create appropriate objects
		DataConverter.readPersonFile();
		DataConverter.readCustomerFile();
		DataConverter.readProductFile();
		
		// write to JSON file 
		toJsonFile("data/Persons.json",DataConverter.persons,"persons");
		toJsonFile("data/Customers.json",DataConverter.customers,"customers");
		toJsonFile("data/Products.json",DataConverter.products,"products");
		
		
		// write to XML file format
		toXmlFile("data/Persons.xml", DataConverter.persons,"persons");
		toXmlFile("data/Customers.xml", DataConverter.customers,"customers");
		toXmlFile("data/Products.xml", DataConverter.products,"products");
		
		
	}// end main
	
//	public static void main(String[] args){
//		
//		//read and parse files, create appropriate objects
//		DataConverter.readPersonFile();
//		DataConverter.readCustomerFile();
//		DataConverter.readProductFile();
//		
//		// write to JSON file 
//		toJsonFile("data/Persons.json",DataConverter.persons,"persons");
//		toJsonFile("data/Customers.json",DataConverter.customers,"customers");
//		toJsonFile("data/Products.json",DataConverter.products,"products");
//		
//		
//		// write to XML file format
//		toXmlFile("data/Persons.xml", DataConverter.persons,"persons");
//		toXmlFile("data/Customers.xml", DataConverter.customers,"customers");
//		toXmlFile("data/Products.xml", DataConverter.products,"products");
//		
//		
//	}// end main
 
	/**
	 * Searches for and returns a stored person object based on a person code.
	 * @param personCode the person code of the person being searched for
	 * @param persons the List of stored person objects
	 * @return the matching person object 
	 */
	public static Person findPerson(String personCode, ArrayList<Person> persons) {
		Person p = null;
		for(Person person : persons) {
			if(person.getPersonCode().equals(personCode)) {
				p = person;
			}
		}
		return p;
	}
	
	/**
	 * Searches for and returns a stored customer object based on a customer code.
	 * @param customerCode the customer code of the customer being searched for
	 * @param customers the List of stored customer objects
	 * @return the matching customer object 
	 */
	public static Customer findCustomer(String customerCode, ArrayList<Customer> customers) {
		Customer c = null;
		for(Customer customer : customers) {
			if(customer.getCustomerCode().equals(customerCode)) {
				c = customer;
			}
		}
		return c;
	}
	
	/**
	 * Searches for and returns a stored product object based on a product code.
	 * @param productCode the product code of the product being searched for
	 * @param products the List of stored product objects
	 * @return the matching product object 
	 */
	public static Product findProduct(String productCode, ArrayList<Product> products) {
		Product p = null;
		for(Product product : products) {
			if(product.getProductCode().equals(productCode)) {
				p = product;
			}
		}
		return p;
	}


	public static ArrayList<Customer> getCustomers() {
		return customers;
	}

	public static ArrayList<Product> getProducts() {
		return products;
	}

	public static ArrayList<Person> getPersons() {
		return persons;
	}
	
	public static ArrayList<Invoice> getInvoices() {
		return invoices;
	}


	public static void setInvoices(ArrayList<Invoice> invoices) {
		DataConverter.invoices = invoices;
	}


	/**
	 * This function parses the given Object to JSON format 
	 * @param fileOutput name/path of resulting json file
	 * @param obj object to be converted
	 * @param objName Alias of object for display
	 * return output a file with JSON format
	 */
	public static void toJsonFile(String fileOutput, Object obj,String objName) { 		

		Gson gson = new Gson();
				
		//use to print neat tree model JSON result 
		gson = new GsonBuilder().setPrettyPrinting().create();
	
		String jsonInString = String.format("{\n \"%s\": %s}",objName,gson.toJson(obj) );
		//System.out.println(jsonInString);
		// parse JSON output to aa file 
		FileWriter output = null;
		try {
			output = new FileWriter(fileOutput);
			output.write(jsonInString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// close output file
		try {
			output.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This function parses the given Object to XML format 
	 * @param fileOutput name/path of resulting xml file
	 * @param obj object to be converted
	 * @param fieldName Alias of object for display
	 */
	public static void toXmlFile(String fileOutput, Object obj, String fieldName) {
		
		XStream xstream = new XStream();

		xstream.alias("product", Product.class);
		
		if(fieldName.equals("persons")) {
			xstream.alias("person", Person.class);
			xstream.alias("persons", Set.class);
		}else if(fieldName.equals("customers")) {
			
			xstream.alias("customer", Customer.class);
			xstream.alias("customers", Set.class);
			xstream.alias("email", String.class);
			xstream.alias("student", Student.class);
			xstream.alias("general", General.class);

		}else if(fieldName.equals("products")) {
			xstream.alias("product", Customer.class);
			xstream.alias("products", Set.class);
			xstream.alias("refreshment", Refreshment.class);
			xstream.alias("movieticket", MovieTicket.class);
			xstream.alias("seasonpass", SeasonPass.class);
			xstream.alias("parkingpass", ParkingPass.class);
		}else {
			System.err.println("toXmlFile error"); 
		}
		String version = "<?xml version=\"1.0\"?>";
		String xmlInString = String.format("%s \n %s ", version, xstream.toXML(obj));
				
		// parse JSON output to aa file 
		FileWriter output = null;
		try {
			output = new FileWriter(fileOutput);
			output.write(xmlInString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// close output file
		try {
			output.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets scanner to new file of given file name.
	 * @param fileName name of file to be opened
	 * @return Scanner object set to new file
	 */
	public static Scanner openFile(String fileName) {
		Scanner s;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e2) {
			throw new RuntimeException("Error: File Not Found!");
		}
		return s;
	}
	
	/**
	 * Scans in data from a flat file and creates the person objects
	 * and adds them to an array list after checking for duplicates.
	 */
	public static void readPersonFile() {
		// read customer.dat file
				Scanner scan = null;
				
				//open Person data file, and create object 
				scan = DataConverter.openFile("data/Persons.dat");
				scan.hasNext();
				NUM_OF_PERSON = Integer.parseInt(scan.nextLine().trim());
				
				for(int i = 0; i < NUM_OF_PERSON; i++) {
					String nextLine = scan.nextLine();
					Person p = new Person();
					p.setAttribute(nextLine);
					
					int flag = 0;
					//check to make sure there is no duplicate
					for(Person person : persons){
						if(person.getPersonCode().equals(p.getPersonCode())){
							flag ++;
						}
					}// end for
					
					if(flag == 0){
						persons.add(p);
					}
				}
	}// end readgetIPersonFile()
	

	/**
	 * Scans in data from a flat file and creates the customer objects
	 * and adds them to an array list after checking for duplicates.
	 */
	public static void readCustomerFile() {
		
		Scanner scan = null;
		//open Customer.dat file, and create object
		scan = DataConverter.openFile("data/Customers.dat");
		scan.hasNext();
		NUM_OF_CUSTOMER = Integer.parseInt(scan.nextLine().trim());
		
		for(int i = 0; i < NUM_OF_CUSTOMER; i++) {
			String nextLine = scan.nextLine();
			String[] token = nextLine.split(";");
			String customerCode = token[1];
			Customer c = null;
			if(customerCode.equalsIgnoreCase("S")) {
				c = new Student();
				c.setAttribute(nextLine);
			}else if(customerCode.equalsIgnoreCase("G")){
				c = new General();
				c.setAttribute(nextLine);
			}
		
			
			int flag = 0; 
			//check to make sure there is no duplicate
			for(Customer customer : customers){
				if(customer.getCustomerCode().equals(c.getCustomerCode())){
					flag ++;
				}
			}// end for
			
			if(flag == 0){
				customers.add(c);
			}
		}// end main for()
			
		scan.close();
	}
	
	/**
	 * Scans in data from a flat file and creates the product objects
	 * and adds them to an array list after checking for duplicates.
	 */
	public static void readProductFile() {
		
		Scanner scan = null;
		//parse Product data File, create appropriate objects
		scan = DataConverter.openFile("data/Products.dat");
		
		// read the first line.
		NUM_OF_PRODUCT = Integer.parseInt(scan.nextLine().trim());
		
		
		for(int i = 0; i < NUM_OF_PRODUCT; i++) {
			String nextLine = scan.nextLine();
			String[] token = nextLine.split(";");
			Product p = null;
			if(token[1].equalsIgnoreCase("M")) {
				p = new MovieTicket(token);
			} else if (token[1].equalsIgnoreCase("S")) {
				p = new SeasonPass(token);
			} else if (token[1].equalsIgnoreCase("P")) {
				p = new ParkingPass(token);
			} else if (token[1].equalsIgnoreCase("R")) {
				p = new Refreshment(token);
			}
			
			int flag = 0;
			//check to make sure there is no duplicate
			for(Product product : products){
				if(product.getProductCode().equals(p.getProductCode())){
					flag ++;
				}
			}// end for
			
			if(flag == 0){
				products.add(p);
			}
		}
		
		scan.close();
	}
	
	/**
	 * Scans in data from a flat file and creates the invoice objects
	 * and adds them to an array list after checking for duplicates.
	 */
	public static void readInvoiceFile() {
		Scanner scan = DataConverter.openFile("data/Invoices.dat");
		
		// read the num of invoices
		scan.hasNext();
		NUM_OF_INVOICE = Integer.parseInt(scan.nextLine().trim());
		
		for(int i = 0;i < NUM_OF_INVOICE; i++) {
			
			String nextLine = scan.nextLine();
			Invoice invoice = new Invoice();
			invoice.setAttribute(nextLine);
			
			int flag = 0; 
			//check to make sure there is no duplicate
			for(Invoice iv : invoices){
				if(iv.getInvoiceCode().equals(invoice.getInvoiceCode())){
					flag ++;
				}
			}// end for
			
			if(flag == 0){
				invoices.add(invoice);
			}
		}
	}
	
}// end DataConverter
