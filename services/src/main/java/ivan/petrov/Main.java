package ivan.petrov;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ivan.petrov.bean.Task;
import ivan.petrov.service.TaskExecutorService;

public class Main {

	public static void main(String[] args) {
		Task t1 = new Task("t1", "touch file1", Arrays.asList(new String[] { }));
		Task t2 = new Task("t2", "cat file1", Arrays.asList(new String[] { "t3" }));
		Task t3 = new Task("t3", "echo 'Hello World!' > file1", Arrays.asList(new String[] {"t1"}));
		Task t4 = new Task("t4", "echo 'Done'", Arrays.asList(new String[] { "t2", "t3" }));
		Task t5 = new Task("t5", "t5", Arrays.asList(new String[] {}));
		Task t6 = new Task("t6", "t6", Arrays.asList(new String[] { "t7" }));
		Task t7 = new Task("t7", "t7", Arrays.asList(new String[] { "t5" }));

		Map<String, Task> tasks = new HashMap<String, Task>();
		tasks.put("t1", t1);
		tasks.put("t2", t2);
		tasks.put("t3", t3);
		tasks.put("t4", t4);
//		tasks.put("t5", t5);
//		tasks.put("t6", t6);
//		tasks.put("t7", t7);


		
		Executor executor = Executors.newFixedThreadPool(4);
		
		for (int i = 0; i < 100; i++) {
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					TaskExecutorService.execute(tasks);
				// TODO Auto-generated method stub
					
				}
			});
		}
	}

}
