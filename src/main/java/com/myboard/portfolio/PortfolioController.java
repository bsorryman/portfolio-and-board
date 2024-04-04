package com.myboard.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {
	
	@GetMapping("/home")
	public String getPortfolioHome() {
		
		return "thymeleaf/portfolio/index";
	}
	
	@GetMapping("/{project}")
	public String getRecipeBagePage(@PathVariable("project") String project) {
		
		return "thymeleaf/portfolio/" + project;
	}
}
