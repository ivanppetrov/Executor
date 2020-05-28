package ivan.petrov.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ivan.petrov.bean.Task;
import ivan.petrov.constant.ConstantMessage;
import ivan.petrov.exception.CyclicDependentTasksException;

public class TaskService {
	
	private List<Task> orderedTasks;
	private Set<String> notvisited;
	private Map<String, Task> tasks;

	TaskService(Map<String, Task> tasks) {
		this.orderedTasks = new ArrayList<Task>();
		this.notvisited = new HashSet<String>();
		this.tasks = tasks;
	}

	List<Task> orderTasks() throws CyclicDependentTasksException {
		Set<String> visited = new HashSet<String>();
		Iterator<String> verticesIterator = tasks.keySet().iterator();
		
		// iterate over all vertices in order to find if there is more than one graph 
		while (verticesIterator.hasNext()) {
			String vertex = verticesIterator.next();
			// find path for every not visited vertex
			if (!visited.contains(vertex)) {
				traverseTasks(vertex, visited);
			}
		}
		return orderedTasks;
	}

	private void traverseTasks(String startTask, Set<String> visited) throws CyclicDependentTasksException {
		notvisited.add(startTask);

		tasks.get(startTask).getRequires().forEach(task -> {
			if (!visited.contains(task)) {
				// check for cyclic dependencies
				if (notvisited.contains(task)) {
					throw new CyclicDependentTasksException(
							String.format(ConstantMessage.CYCLIC_DEPENDENCY_MESSAGE, startTask, task));
				}
				traverseTasks(task, visited);
			}
		});

		notvisited.remove(startTask);
		visited.add(startTask);
		orderedTasks.add(tasks.get(startTask));
	}
}
