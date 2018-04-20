package com.ilib.listener.activity.qingjia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Event;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ilib.dao.TestDao;
import com.ilib.model.TestBeanB;
import com.ilib.other.utils.MailHelper;
/**
 * 使用expression方式来，监听流程
 * <br>这种方式可以注入bean，比如这里的dao接口。监听器类上要加@Component由spring管理，并且这个类所在的包要被spring扫描到。
 * <br>expression写法：@Component的定义名.方法名(参数名)  actListener.qingjia(execution)
 * <br>注意：DelegateExecution只能绑定在起始 结束 网关 流程箭头线上 activiti:executionListener
 * <br>      DelegateTask只能绑在任务监听上 activiti:taskListener
 * @author Kyoxue
 *
 */
@Component(value="actListener")
public class ActivitiListener {
	@Autowired
	private TestDao userDao;
	@Autowired
	private MailHelper mailService;
	//  DelegateExecution只能绑定在起始 结束 网关 流程箭头线上
    public void qingjia(DelegateExecution execution) throws Exception { 
    	String insId = execution.getProcessInstanceId();
    	System.out.println(insId);
    	String key = execution.getCurrentActivityId();
    	String name = execution.getCurrentActivityName();
    	System.out.println("开始了流程..."+key+"  "+name);
        System.out.println("执行ID："+execution.getId()+"定义ID"+execution.getProcessDefinitionId()); 
        //一个执行可能有多个执行实例，当只有一条流程箭头时执行和执行实例是相同的。
        String processInstanceId = execution.getProcessInstanceId();//执行实例ID
        String starter = (String)execution.getEngineServices().getRuntimeService().getVariable(execution.getId(), "starter");
        execution.getEngineServices().getRuntimeService().addParticipantUser(processInstanceId, starter);//流程发起人
    }  
    public void over(DelegateExecution execution) throws Exception { 
    	//这里用的JAVA的邮件发送 未用activiti mailtask
    	VelocityContext v= new VelocityContext();
		v.put("to", "kyoxue2");
		v.put("assignee", "王五");
		String vm = "testMail.vm";
		String toMail = "838170000@qq.com";
		String title = "审核结果通知";
		mailService.sendTaskMail(v, vm, toMail, title);
    	System.out.println("流程已结束，这里可以设置结束时候的操作。");
    }  
    //  DelegateTask只能绑在任务监听上
    public void xsApprove(DelegateTask task)throws Exception{
    	if (task == null) {
    		System.out.println("task is null...");
    		return;
		}
    	System.out.println("申请请假前的准备开始...");
    	System.out.println("任务ID："+task.getId());
    	String executionId = task.getExecutionId();
    	System.out.println("当前执行的ID："+executionId);
    	String userId = (String)task.getExecution().getEngineServices().getRuntimeService().getVariable(executionId, "starter");
    	System.out.println("当前发起申请的人："+userId);
    	task.setAssignee(userId);//我个人审核，设置个人任务就行
    	System.out.println("设置了，申请请假的审核人："+userId);
    	task.getExecution().getEngineServices().getTaskService().setVariableLocal(task.getId(), "first", true);
    }
    
//	ProcessInstanceQuery processQuery = task.getExecution().getEngineServices().getRuntimeService().createProcessInstanceQuery();
//	ProcessInstance process = processQuery.processDefinitionId(task.getProcessDefinitionId()).singleResult();
//	String procId =process.getActivityId();//流程执行到哪就是哪的ID，不是数据库主键对应xml的id
//	String procKey = process.getProcessDefinitionKey();//act_re_procdef的主键
//	String insId = task.getProcessInstanceId();//act_ru_execution 执行实例的主键
//	String id =task.getId();//act_ru_task的主键
//	String name = task.getName();//当前任务的名字
    
    public void bzrApprove(DelegateTask task)throws Exception{
    	System.out.println("班主任审批前的准备开始...");
    	System.out.println("任务ID："+task.getId());
		try {
			ProcessDefinition prodef = task.getExecution().getEngineServices().getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
			String prodefKey = prodef.getKey();//整个流程的ID 对应xml 对应用户关系表权限的分类
			ListIterator<Event> event = task.getExecution().getEngineServices().getRuntimeService().getProcessInstanceEvents(task.getProcessInstanceId()).listIterator();
			while (event.hasNext()) {
				Event event2 = (Event) event.next();
				System.out.println(event2.getId()+"  "+event2.getMessage()+"   ------------event");
			}
			System.out.println(task.getExecution().getCurrentActivityId()+"  "+task.getExecution().getCurrentActivityName()+"   ------------Execution");
			
			String taskKey = task.getTaskDefinitionKey();//当前任务的ID 对应XML 对应用户关系表的权限
			List<TestBeanB> users = userDao.selectUserByRole(taskKey,prodefKey);
			Collection<String> candidateUsers = getCandidateUsers(users);
			task.addCandidateUsers(candidateUsers);//通知班主任审批节点的候选人
			//Map<String, List<String>> map = getTypeUser(users);
			//task.setAssignee(map.get("assignee").get(0));
			//System.out.println("设置了，申请请假的班主任审核负责人"+map.get("assignee").get(0));
//			task.addCandidateUsers(map.get("candidateUsers"));
//			System.out.println("设置了，申请请假的班主任审核人"+map.get("candidateUsers").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void jdczrApprove(DelegateTask task)throws Exception{
    	System.out.println("教务主任审批前的准备开始...");
    	System.out.println("任务ID："+task.getId());
		try {
			ProcessDefinition prodef = task.getExecution().getEngineServices().getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
			String prodefKey = prodef.getKey();//整个流程的ID 对应xml 对应用户关系表权限的分类
			String taskKey = task.getTaskDefinitionKey();//当前任务的ID 对应XML 对应用户关系表的权限
			List<TestBeanB> users = userDao.selectUserByRole(taskKey,prodefKey);
			Map<String, List<String>> map = getTypeUser(users);
			task.setAssignee(map.get("assignee").get(0));//通知教务处主任主办事人审批 只有一个领导，设置个人审批
			System.out.println("设置了，申请请假的教务主任审核负责人"+map.get("assignee").get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 设置用户 组 关系
     */
    private void setID(DelegateTask task)throws Exception{
    	ProcessDefinition prodef = task.getExecution().getEngineServices().getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
		String prodefKey = prodef.getKey();//整个流程的ID 对应xml 对应用户关系表权限的分类
    	System.out.println("开始部署用户组关系到工作流，当前工作流名称："+prodefKey);
		List<Map<String, String>> list = userDao.selectGroupAndUser(prodefKey);
		List<String> accounts = userDao.selectUserAccountByType(prodefKey);
		List<String> roles = userDao.selectRoleByType(prodefKey);
		if (isNullList(list)) {
			System.out.println("对应的用户和权限不存在！");
		}
		if (isNullList(accounts)) {
			System.out.println("对应的用户不存在！");
		}
		if (isNullList(roles)) {
			System.out.println("对应的权限不存在！");
		}
		IdentityService identityService =task.getExecution().getEngineServices().getIdentityService();
		for (String role : roles) {
			identityService.saveGroup(new GroupEntity(role));//建立组
		}
		for (String account : accounts) {
			identityService.saveUser(new UserEntity(account));//建立用户
		}
		for (Map<String, String> map : list) {
			String group = map.get("groupEntity");
			String user = map.get("userEntity");
			identityService.createMembership(user, group);//建立组和用户关系
		}
    	System.out.println("用户组关系部署完毕！");
    	List<org.activiti.engine.identity.User> userlist = identityService.createUserQuery().list();
    	for (org.activiti.engine.identity.User user : userlist) {
			System.out.println(user.getId()+"-----------U");
		}
    }
    private boolean isNullList(Collection<?> collection){
    	return (collection == null || collection.size() == 0)?true:false;
    }
  //区分权限负责人和参与者
    private Map<String, List<String>> getTypeUser(List<TestBeanB> users){
    	Map<String,List<String>> map = new HashMap<>();
    	List<String> assignee = new ArrayList<>();
    	List<String> candidateUsers = new ArrayList<>();
    	if (null != users && users.size() > 0) {
			for (TestBeanB user : users) {
				String account = user.getAccount();
				String mainRole = user.getMainRole();
				mainRole = (null == mainRole ? "" : mainRole.trim());
				if ("Y".equals(mainRole)) {//权限主要负责人
					assignee.add(account);
				}else{//参与审核人
					candidateUsers.add(account);
				}
			}
		}
    	map.put("assignee", assignee);
    	map.put("candidateUsers", candidateUsers);
    	return map;
    }
    private Collection<String> getCandidateUsers(List<TestBeanB> users){
    	List<String> candidateUsers = new ArrayList<>();
    	if (null != users && users.size() > 0) {
    		for (TestBeanB user : users) {
	    		String account = user.getAccount();
	    		candidateUsers.add(account);
    		}
    	}
    	return candidateUsers;
    }
}
