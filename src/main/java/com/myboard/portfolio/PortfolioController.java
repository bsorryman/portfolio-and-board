package com.myboard.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortfolioController {
	
	@GetMapping("/portfolio")
	public String getPortfolioHome() {
		
		return "thymeleaf/portfolio/index";
	}
	
	@GetMapping("/project/{project}")
	public String getRecipeBagePage(@PathVariable("project") String project) {
		
		return "thymeleaf/project/" + project;
	}
}
