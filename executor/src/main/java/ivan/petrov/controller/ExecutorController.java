package ivan.petrov.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import ivan.petrov.bean.RequestBean;
import ivan.petrov.bean.Task;
import ivan.petrov.constant.ConstantMessage;
import ivan.petrov.exception.CyclicDependentTasksException;
import ivan.petrov.exception.WrongCommandException;
import ivan.petrov.service.TaskExecutorService;

@RestController
public class ExecutorController {

	private static final Logger LOGGER = LogManager.getLogger(ExecutorController.class);
	private final Gson gson = new Gson();

	@RequestMapping(value = "execute", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<String> execute(@RequestBody String json) {

		try {
			Map<String, Task> tasks = parseJson(json);

			List<Task> orderedTasks = TaskExecutorService.execute(tasks);
			return ResponseEntity.ok().body(gson.toJson(orderedTasks));
		} catch (CyclicDependentTasksException | WrongCommandException e) {
			return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e.getCause());
			return ResponseEntity.badRequest().body(gson.toJson(ConstantMessage.UNEXPECTED_ERROR));
		}
	}

	private Map<String, Task> parseJson(String json) {
		RequestBean request = gson.fromJson(json, RequestBean.class);
		Iterator<Task> taskIterator = request.getTasks().iterator();
		Map<String, Task> tasks = new HashMap<String, Task>();

		while (taskIterator.hasNext()) {
			Task task = (Task) taskIterator.next();
			tasks.put(task.getName(), task);
		}

		return tasks;
	}
}
