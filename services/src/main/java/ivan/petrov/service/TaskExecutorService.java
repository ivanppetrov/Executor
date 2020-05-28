package ivan.petrov.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ivan.petrov.bean.Task;
import ivan.petrov.constant.ConstantMessage;
import ivan.petrov.exception.CyclicDependentTasksException;
import ivan.petrov.exception.WrongCommandException;

public class TaskExecutorService {
	private static final Logger LOGGER = LogManager.getLogger(TaskExecutorService.class);

	// Order and execute given tasks
	public static List<Task> execute(Map<String, Task> tasks)
			throws CyclicDependentTasksException, WrongCommandException {
		List<Task> orderedTasks = order(tasks);

		executeTasks(orderedTasks);

		return orderedTasks;
	}

	private static void executeTasks(List<Task> orderedTasks) throws WrongCommandException {
		orderedTasks.forEach(task -> {
			try {
				// TODO change path
				ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
						task.getCommand());
				processBuilder.redirectErrorStream(true);

				LOGGER.info("Executing command: " + task.getCommand());
				Process process = processBuilder.start();

				int result = process.waitFor();
				if (result == 0) {
					LOGGER.info("Done command: " + task.getCommand());
				} else {
					String errorMessage = String.format(ConstantMessage.COMMAND_EXECUTION_ERROR, task.getCommand());
					LOGGER.error(errorMessage);
					throw new WrongCommandException(errorMessage);
				}

			} catch (IOException | InterruptedException e) {
				LOGGER.error(e.getMessage(), e.getCause());
			}
		});
	}

	private static List<Task> order(Map<String, Task> tasks) throws CyclicDependentTasksException {
		TaskService taskService = new TaskService(tasks);
		return taskService.orderTasks();

	}
}
