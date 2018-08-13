package com.cosmos.fileservice.domain;

import java.util.ArrayList;
import java.util.List;

public class Command {
	
	private String windowCommand;
	
	private List<String> arguments = new ArrayList<>();

	public String getWindowCommand() {
		return windowCommand;
	}

	public void setWindowCommand(String windowCommand) {
		this.windowCommand = windowCommand;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
	
	

}
