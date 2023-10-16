package mao.activiti_group_task;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ActivitiGroupTaskApplicationTests
{
    private static final Logger log = LoggerFactory.getLogger(ActivitiGroupTaskApplicationTests.class);

    @Test
    void contextLoads()
    {
    }

    @Test
    void findGroupTaskList()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskCandidateUser("user2")//根据候选人查询
                .list();
        for (Task task : list)
        {
            log.info("流程实例id：" + task.getProcessInstanceId());
            log.info("任务id：" + task.getId());
            log.info("任务负责人：" + task.getAssignee());
            log.info("任务名称：" + task.getName());
        }
    }

    @Test
    void claimTask()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskId("7501")
                .taskCandidateUser("user2")//根据候选人查询
                .singleResult();
        if (task == null)
        {
            log.info("user2无资格");
        }
        else
        {
            taskService.claim("7501", "user2");
            log.info("user2 任务拾取成功");
        }
    }

    @Test
    void findPersonalTaskList()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskAssignee("user2")
                .list();
        for (Task task : list)
        {
            log.info("流程实例id：" + task.getProcessInstanceId());
            log.info("任务id：" + task.getId());
            log.info("任务负责人：" + task.getAssignee());
            log.info("任务名称：" + task.getName());
        }
    }

    @Test
    void completeTask()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("7501");
    }

    @Test
    void setAssigneeToGroupTask()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService
                .createTaskQuery()
                .taskId("7501")
                .taskAssignee("user2")
                .singleResult();
        if (task != null)
        {
            // 如果设置为null，归还组任务,该 任务没有负责人
            taskService.setAssignee("7501", null);
        }
    }

    @Test
    void setAssigneeToCandidateUser()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService
                .createTaskQuery()
                .taskId("7501")
                .taskAssignee("user2")
                .singleResult();
        if (task!=null)
        {
            taskService.setAssignee("7501", "user3");
        }
    }
}
