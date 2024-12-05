package com.transys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CmController {
	
	  @RequestMapping(value= "/cm/cm01", method = RequestMethod.GET)
	    public String cm01(Model model) {
	        return "/cm/cm01.jsp"; // 
	    }	
	  
	  @RequestMapping(value= "/cm/cm02", method = RequestMethod.GET)
	    public String cm02(Model model) {
	        return "/cm/cm02.jsp"; // 
	    }	

}
