package ivan.petrov.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ivan.petrov.bean.Task;
import ivan.petrov.exception.CyclicDependentTasksException;

public class TaskServiceTest {

	private static Map<String, Task> tasks;
	private static List<Task> orderedTasks;

	@BeforeAll
	public static void initMap() {
		// create a few tasks
		Task t1 = new Task("t1", "", Arrays.asList(new String[] {}));
		Task t2 = new Task("t2", "", Arrays.asList(new String[] { "t3" }));
		Task t3 = new Task("t3", "", Arrays.asList(new String[] { "t1" }));
		Task t4 = new Task("t4", "", Arrays.asList(new String[] { "t2", "t3" }));

		// add them to map
		tasks = new HashMap<String, Task>();
		tasks.put("t1", t1);
		tasks.put("t2", t2);
		tasks.put("t3", t3);
		tasks.put("t4", t4);

		// Initialize expected results list
		orderedTasks = Arrays.asList(t1, t3, t2, t4);
	}

	@Test
	public void orderTasksTest() {
		TaskService service = new TaskService(tasks);
		assertEquals(orderedTasks, service.orderTasks());
	}

	@Test
	public void cyclicDependencyTest() {
		Map<String, Task> cyclicDependencies = new HashMap<String, Task>();
		cyclicDependencies.put("t1", new Task("t1", "", Arrays.asList(new String[] { "t2" })));
		cyclicDependencies.put("t2", new Task("t2", "", Arrays.asList(new String[] { "t3" })));
		cyclicDependencies.put("t3", new Task("t3", "", Arrays.asList(new String[] { "t1" })));

		TaskService service = new TaskService(cyclicDependencies);

		assertThrows(CyclicDependentTasksException.class, () -> {
			service.orderTasks();
		});

	}
}
