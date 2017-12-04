package com.gc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.text.Document;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gc.dto.ProductDto;



/*
 * 
 * 
 * author: Antonella Solomon
 *
 */

@Controller
public class HomeController {

	// this method is currently returning all of the table info later this will be
	// pulled out into an arraylist method to be reused
	@RequestMapping("/welcome")
	public ModelAndView helloWorld() throws IOException {
		URL oracle = new URL("http://store.steampowered.com/search/?tags=19&page=1");
		BufferedReader in = new BufferedReader(
		    new InputStreamReader(
		    oracle.openStream()));
			
		String inputLine;
		
		
			

		
		while ((inputLine = in.readLine()) != null) 
		  System.out.println(inputLine);
		
		in.close(); 
		Configuration config = new Configuration().configure("hibernate.cfg.xml");
		/*
		 * The SessionFactory is a factory of session and client of Connection Provider.
		 * It holds second level cache (optional) of data
		 */
		SessionFactory sessionFactory = config.buildSessionFactory();
		/*
		 * A Session is used to get a physical connection with a database. The Session
		 * object is lightweight and designed to be instantiated each time an
		 * interaction is needed with the database. Persistent objects are saved and
		 * retrieved through a Session object.
		 * 
		 * The session objects should not be kept open for a long time because they are
		 * not usually thread safe and they should be created and destroyed them as
		 * needed. The main function of the Session is to offer, create, read, and
		 * delete operations for instances of mapped entity classes.
		 */
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(ProductDto.class); // the strikethrough indicates this is deprecated
		ArrayList<ProductDto> list = (ArrayList<ProductDto>) crit.list();
		
		
		tx.commit();
		session.close();

		return new ModelAndView("welcome", "message", list);
	}
	

	@RequestMapping("/getnewprod")
	public String newProduct() {
		return "addprodform";
	}
//	@RequestMapping(value="addnewproduct", method = RequestMethod.POST)
//
//	public String addNewCustomer(@RequestParam("tag") String tag, @RequestParam("gameName") String gameName,
//		
//		
//		@RequestParam("appID") int appID, Model model) {
//
//	Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
//
//	SessionFactory sessionFact = cfg.buildSessionFactory();
//
//	Session session = sessionFact.openSession();
//
//	Transaction tx = session.beginTransaction();
//
//	ProductDto newProduct = new ProductDto();
//
//	newProduct.setTag(tag);
//	newProduct.setGameName(gameName);
//	newProduct.setAppID(appID);
//
//	session.save(newProduct);
//	tx.commit();
//	System.out.println(newProduct);
//	System.out.println(session.save(newProduct));
//	System.out.println(tx);
//	System.out.println(session);
//	session.close();
//
//	model.addAttribute("newStuff", newProduct);
//	return "addprodsuccess";
//	}
	
	
	@RequestMapping(value="searchbyproduct", method = RequestMethod.GET)
	public ModelAndView searchProduct(@RequestParam("action19")String prod) {
		
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");

		SessionFactory sessionFact = cfg.buildSessionFactory();

		Session selectCustomers = sessionFact.openSession();

		selectCustomers.beginTransaction();

		// Criteria is used to create the query
		Criteria c = selectCustomers.createCriteria(ProductDto.class);

		// adding additional search criteria to the query
		// the first parameter is referencing the table column that we want to search
		// against
		c.add(Restrictions.like("code", "%" + prod + "%"));

		ArrayList<ProductDto> customerList = (ArrayList<ProductDto>) c.list();

		
		return new ModelAndView("welcome", "message", customerList);
	}
	@RequestMapping("/update")
	public ModelAndView updateForm(@RequestParam("id") int id) {

		return new ModelAndView("updateprodform", "id", id);
	}

	@RequestMapping("/updateproduct")
	public ModelAndView updateProduct(@RequestParam("id") int id,@RequestParam("tag") String tag, @RequestParam("gameName") String gameName,
			
			
			@RequestParam("appID") int appID, Model model) {

		// temp Object will store info for the object we want to update
		ProductDto temp = new ProductDto();
		temp.setId(id);
		temp.setAppID(appID);
		temp.setGameName(gameName);
		temp.setTag(tag);

		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");

		SessionFactory sessionFact = cfg.buildSessionFactory();

		Session codes = sessionFact.openSession();

		codes.beginTransaction();

		codes.update(temp); // update the object from the list

		codes.getTransaction().commit(); // update the row from the database table

		ArrayList<ProductDto> prodList = getAllProducts();

		return new ModelAndView("welcome", "message", prodList);
	}
	private ArrayList<ProductDto> getAllProducts() {
		Configuration config = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = config.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(ProductDto.class);
		ArrayList<ProductDto> list = (ArrayList<ProductDto>) crit.list();
		tx.commit();
		session.close();
		return list;
	}
	
	
	@RequestMapping(value="/create", method = RequestMethod.POST)

	public String addNewCustomer(@RequestParam("tag") String tag, @RequestParam("gameName") String gameName,
		
		
		@RequestParam("appID") int appID, Model model) throws IOException {

		URL oracle = new URL("view-source:http://store.steampowered.com/search/?tags=19&page=1");
		BufferedReader in = new BufferedReader(
		    new InputStreamReader(
		    oracle.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
		  System.out.println(inputLine);

		in.close();
		
		
	Configuration cfg = new Configuration().configure("hibernate.cfg.xml");

	SessionFactory sessionFact = cfg.buildSessionFactory();

	Session session = sessionFact.openSession();

	Transaction tx = session.beginTransaction();

	ProductDto newProduct = new ProductDto();

	newProduct.setTag(tag);
	newProduct.setGameName(gameName);
	newProduct.setAppID(appID);

	session.save(newProduct);
	tx.commit();
	System.out.println(newProduct);
	System.out.println(session.save(newProduct));
	System.out.println(tx);
	System.out.println(session);
	session.close();

	model.addAttribute("newStuff", newProduct);
	return "create";

	}
}
