package ivan.petrov.bean;

import java.util.ArrayList;
import java.util.List;

public class Task {
	private String name;
	private String command;
	private List<String> requires;

	public Task(String name, String command, List<String> requires) {
		super();
		this.name = name;
		this.command = command;
		this.requires = requires;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getRequires() {
		return requires != null ? this.requires : new ArrayList<String>();
	}

	public void setRequires(List<String> requires) {
		this.requires = requires;
	}

	@Override
	public String toString() {
		return "Task [name=" + name + ", command=" + command + ", requires=" + requires + "]";
	}

}
