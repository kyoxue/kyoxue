package com.ilib.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ilib.dao.TestDao;
import com.ilib.testcommon.BaseTest;
public class ActivitiTest extends BaseTest {
	Logger log = LoggerFactory.getLogger(ActivitiTest.class);
	@Autowired
	private TestDao userDao;
	@Autowired
	private RepositoryService repService;
	@Autowired
	private RuntimeService runService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService hisService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ManagementService managerService;
	@Test
	public void testDeploy()throws Exception{
		String filePath="qingjia.bpmn";
		Deployment db = repService.createDeployment()
				  .addClasspathResource("diagrams/".concat(filePath))
				  .deploy();
	}
	
	@Test
	public void testStartProcess()throws Exception{//流程只启动一次！
		System.out.println("----------------->1");
		Map<String, Object> map = new HashMap<>();
		map.put("starter", "kyoxue");
		runService.startProcessInstanceByKey("qingjia",map);
		System.out.println("----------------->2");
	}
	@Test
	public void testSuspen()throws Exception{//某节点的审批人暂停当前实例流程，不能审批和驳回。注意：这个挂住不是表示不能重新开始流程，流程有多个实例，实例之间是并列的。
		String assignee = "teacher2";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).singleResult();
		String processInstanceId =  task.getProcessInstanceId();
		runService.suspendProcessInstanceById(processInstanceId);
	}
	@Test
	public void testRun()throws Exception{
		String assignee = "teacher2";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).singleResult();
		String processInstanceId =  task.getProcessInstanceId();
		runService.activateProcessInstanceById(processInstanceId);
	}
	@Test
	public void testQueryTask()throws Exception{
		String assignee = "teacher2";
		//Task task = taskService.createTaskQuery().taskCandidateUser(assignee).singleResult();
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).singleResult();
		if(task == null){
			log.info("task is null....");
			return;
		}
		log.info(task.getId()+" "+task.getName());
	}
	/**
	 * 1、对于未分派的组任务（claim分派），在act_ru_task表中的ASSIGNEE（指派人）为null，所有候选人都可通过查询组任务查看
       2、同时，每次任务的每个指派人都会在act_ru_identitylink表以及act_hi_identitylink 表中各生成两条数据，
           TYPE为participant的表示为流程参与者，所以PROC_INST_ID不为空，TASKID为空
           TYPE为candidate的表示未任务候选人，所以TASKID不为空，PROC_INST_IDPROC_INST_ID
       3、分派后的组任务，指定了处理人，变为个人任务，只有处理人可查看
       4、任务完成后，act_ru_identitylink表以及act_ru_task表中关于该任务记录消失
	 */
	//核心注意：个人任务（setAssignee、claim）参与者是无法看的任务的，组任务（addCandidateUsers）大家都可以看到。
	//addCandidateUsers是将具有这个节点审批权限的人都添加到参与者中来，都具有审批权限
	//在没有分派（claim）前，所有设置的人可以查看，一单分派，只有被分派的人可以查看。当然也可以删除分派人，回归组查询。
	//这种设置不会在act_ru_task中生成assignee值
	//setAssignee是个人审批的权限，其余人都无从干预，这种设定会只在act_ru_task中生成assignee值
	//claim是参与者中某人登录后将审批权限指定给另外一个人，这个人将拥有个人审批权限，其余参与者都无从干预，相当于变成了assignee，在act_ru_task中生成assignee值
	//当前登录的审批人将任务指定给某个人
	//某个人就会具有个人审批的权限，其它参与者将不再有权限审批
	//某人审批提交前转移某人个人审批
	@Test
	public void testClaim()throws Exception{
		String currentUser = "teacher1";
		String userId = "teacher2";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(currentUser).singleResult();
		String taskId = task.getId();
		taskService.claim(taskId, userId);
	}
	//删除候选人
	@Test
	public void testDelCandidateUser()throws Exception{
		String userId = "teacher2";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		String taskId = task.getId();
		taskService.deleteCandidateUser(taskId, userId); 
	}
	@Test
	public void testCommit()throws Exception{
		System.out.println("----------------->1");
		String userId = "boss1";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		String taskId = task.getId();
		taskService.complete(taskId);
		System.out.println("----------------->2");
	}
	@Test
	public void testStepSkip()throws Exception{//任意向后跳任务
		String userId = "boss1";
		String target = "endevent1";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		if (task == null) {
			System.out.println("task is null...");
			return;
		}
		Object first = taskService.getVariableLocal(task.getId(), "first");
		if (first == null) {
			TaskServiceImpl taskServiceImpl=(TaskServiceImpl)taskService;  
			taskServiceImpl.getCommandExecutor().execute(new SkipTask(task.getExecutionId(), target));
		}
	}
	@Test
	public void testCancelTask()throws Exception{//取消申请
		String userId = "kyoxue";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		//(顺序不能换) 
		 runService.deleteProcessInstance(processInstanceId,"取消请假"); 
	     hisService.deleteHistoricProcessInstance(processInstanceId);
	}
	@Test
	public void testRollback()throws Exception{
		String userId = "teacher2";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		String taskId = task.getId();
		rollBackWorkFlow(taskId);
	}
	@Test
	public void testRollbackTo()throws Exception{
		String destTaskkey = "startevent1";
		String userId = "teacher1";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		String taskId = task.getId();
		rollBackToAssignWorkFlow(taskId, destTaskkey);
	}
	@Test
	public void testQueryCandidateUsers()throws Exception{//查询当前审批人所在组的所有审批人
		String userId = "teacher2";
		Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
		String taskId = task.getId();
		List<IdentityLink> ilinks  = taskService.getIdentityLinksForTask(taskId);
		if(null == ilinks || ilinks.size() == 0)
			return;
		for (IdentityLink identityLink : ilinks) {
			log.info(identityLink.getUserId()+" "+identityLink.getType());
		}
	}
	   @Test  
	   public void findHistoryPersonTask(){  //查询历史任务的办理人
		   	String userId = "teacher2";
			Task task = taskService.createTaskQuery().taskCandidateOrAssigned(userId).singleResult();
			String taskId = task.getId(); 
		    List<HistoricIdentityLink> list = hisService.getHistoricIdentityLinksForTask(taskId);  
		   if(null!=list && list.size()>0){  
		       for(HistoricIdentityLink historicIdentityLink:list){  
		           System.out.println("指派任务ID："+historicIdentityLink.getTaskId()+"指派类型："+historicIdentityLink.getType()+"任务候选人："+historicIdentityLink.getUserId()); 
		       }  
		   }
	   }  
	   
	 //退回上一个节点
     public String rollBackWorkFlow(String taskId) {
         try {
             Map<String, Object> variables;
             // 取得当前任务.当前任务节点
             HistoricTaskInstance currTask = hisService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
             // 取得流程实例，流程实例
             ProcessInstance instance = runService
            		 .createProcessInstanceQuery()
                     .processInstanceId(currTask.getProcessInstanceId())
                     .singleResult();
             if (instance == null) {
            	 log.info("流程结束");
                 log.error("出错啦！流程已经结束");
                 return "ERROR";
             }
             variables = instance.getProcessVariables();
             // 取得流程定义
             ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repService)
         			.getDeployedProcessDefinition(currTask
                    .getProcessDefinitionId());
             if (definition == null) {
            	 log.info("流程定义未找到");
                 log.error("出错啦！流程定义未找到");
                 return "ERROR";
             }
             // 取得上一步活动
             ActivityImpl currActivity = ((ProcessDefinitionImpl)definition).findActivity(currTask.getTaskDefinitionKey());
             //也就是节点间的连线
             List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
             // 清除当前活动的出口
             List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
             //新建一个节点连线关系集合
             List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
             //
             for (PvmTransition pvmTransition : pvmTransitionList) {
                 oriPvmTransitionList.add(pvmTransition);
             }
             pvmTransitionList.clear();
             // 建立新出口
             List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
             for (PvmTransition nextTransition : nextTransitionList) {
                 PvmActivity nextActivity = nextTransition.getSource();
                 ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                         .findActivity(nextActivity.getId());
                 TransitionImpl newTransition = currActivity
                         .createOutgoingTransition();
                 newTransition.setDestination(nextActivityImpl);
                 newTransitions.add(newTransition);
             }
             // 完成任务
             List<Task> tasks = taskService.createTaskQuery()
                     .processInstanceId(instance.getId())
                     .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
             for (Task task : tasks) {
                 taskService.complete(task.getId(), variables);
                 hisService.deleteHistoricTaskInstance(task.getId());
             }
             // 恢复方向
             for (TransitionImpl transitionImpl : newTransitions) {
                 currActivity.getOutgoingTransitions().remove(transitionImpl);
             }
             for (PvmTransition pvmTransition : oriPvmTransitionList) {
                 pvmTransitionList.add(pvmTransition);
             }
             log.info("OK");
             log.info("流程结束");
             return "SUCCESS";
         } catch (Exception e) {
          log.error("失败");
             log.error(e.getMessage());
             return "ERROR";
         }
     }
 
     
     
 
     //回退到指定节点
     public String rollBackToAssignWorkFlow(String taskId,String destTaskkey) {
         try {
             Map<String, Object> variables;
             // 取得当前任务.当前任务节点
             HistoricTaskInstance currTask = hisService
                     .createHistoricTaskInstanceQuery().taskId(taskId)
                     .singleResult();
             // 取得流程实例，流程实例
             ProcessInstance instance = runService
                     .createProcessInstanceQuery()
                     .processInstanceId(currTask.getProcessInstanceId())
                     .singleResult();
             if (instance == null) {
              log.info("流程结束");
                 log.error("出错啦！流程已经结束");
                 return "ERROR";
             }
             variables = instance.getProcessVariables();
             // 取得流程定义
             ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repService)
                     .getDeployedProcessDefinition(currTask
                             .getProcessDefinitionId());
             if (definition == null) {
              log.info("流程定义未找到");
                 log.error("出错啦！流程定义未找到");
                 return "ERROR";
             }
            //取得当前活动节点
             ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                     .findActivity(currTask.getTaskDefinitionKey());
             
              log.info("currActivity"+currActivity);
              // 取得上一步活动
             //也就是节点间的连线
             //获取来源节点的关系
             List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();

             // 清除当前活动的出口
             List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
             //新建一个节点连线关系集合
             //获取出口节点的关系
             List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
             //
             for (PvmTransition pvmTransition : pvmTransitionList) {
                 oriPvmTransitionList.add(pvmTransition);
             }
             pvmTransitionList.clear();
  
             // 建立新出口
             List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
             for (PvmTransition nextTransition : nextTransitionList) {
                 PvmActivity nextActivity = nextTransition.getSource();
                 
                             log.info("nextActivity"+nextActivity);
                             
                             log.info("nextActivity.getId()"+nextActivity.getId());
                             
                 //destTaskkey 流程活动的ID 如：startevent1 usertask1这类
                 ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(destTaskkey);
                 TransitionImpl newTransition = currActivity.createOutgoingTransition();
                 newTransition.setDestination(nextActivityImpl);
                 newTransitions.add(newTransition);
             }
             // 完成任务
             List<Task> tasks = taskService.createTaskQuery()
                     .processInstanceId(instance.getId())
                     .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
             for (Task task : tasks) {
                 taskService.complete(task.getId(), variables);
                 hisService.deleteHistoricTaskInstance(task.getId());
             }
             // 恢复方向
             for (TransitionImpl transitionImpl : newTransitions) {
                 currActivity.getOutgoingTransitions().remove(transitionImpl);
             }
             for (PvmTransition pvmTransition : oriPvmTransitionList) {

              pvmTransitionList.add(pvmTransition);
             }
             log.info("OK");
             log.info("流程结束");
          
             return "SUCCESS";
         } catch (Exception e) {
          log.error("失败");
             log.error(e.getMessage());
             return "ERROR";
         }
     }
}
 	class SkipTask implements Command<Comment> {  
	  
    protected String executionId;  
    protected String activityId;  
      
    public SkipTask(String executionId, String activityId) {  
        this.executionId = executionId;  
        this.activityId = activityId;  
    }  
    public Comment execute(CommandContext commandContext) {  
        for (TaskEntity taskEntity : Context.getCommandContext().getTaskEntityManager().findTasksByExecutionId(executionId)) {  
            Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity, "skip task", false);  
        }  
        ExecutionEntity executionEntity = Context.getCommandContext().getExecutionEntityManager().findExecutionById(executionId);  
        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();  
        ActivityImpl activity = processDefinition.findActivity(activityId);  
        executionEntity.executeActivity(activity);  
        return null;  
    }  
}  