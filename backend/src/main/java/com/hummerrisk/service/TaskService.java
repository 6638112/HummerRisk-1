package com.hummerrisk.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hummerrisk.base.domain.*;
import com.hummerrisk.base.mapper.*;
import com.hummerrisk.base.mapper.ext.ExtTaskMapper;
import com.hummerrisk.commons.constants.*;
import com.hummerrisk.commons.exception.HRException;
import com.hummerrisk.commons.utils.*;
import com.hummerrisk.controller.request.task.*;
import com.hummerrisk.dto.*;
import com.hummerrisk.i18n.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * @author harris
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskService {

    @Resource
    private FavoriteMapper favoriteMapper;
    @Resource
    private ExtTaskMapper extTaskMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskItemMapper taskItemMapper;
    @Resource
    private TaskItemResourceMapper taskItemResourceMapper;
    @Resource
    private TaskItemResourceLogMapper taskItemResourceLogMapper;
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private ServerRuleMapper serverRuleMapper;
    @Resource
    private ServerMapper serverMapper;
    @Resource
    private ServerGroupMapper serverGroupMapper;
    @Resource
    private ServerResultMapper serverResultMapper;
    @Resource
    private ServerService serverService;
    @Resource
    private ImageRuleMapper imageRuleMapper;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private ImageResultMapper imageResultMapper;
    @Resource
    private ImageService imageService;
    @Resource
    private RuleTagMappingMapper ruleTagMappingMapper;
    @Resource
    private RuleGroupMappingMapper ruleGroupMappingMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private CloudTaskService cloudTaskService;
    @Resource
    private CodeRuleMapper codeRuleMapper;
    @Resource
    private CodeResultMapper codeResultMapper;
    @Resource
    private CodeMapper codeMapper;
    @Resource
    private RuleService ruleService;
    @Resource
    private CloudTaskMapper cloudTaskMapper;
    @Resource
    private HistoryService historyService;
    @Resource
    private CodeService codeService;
    @Resource
    private CloudNativeMapper cloudNativeMapper;
    @Resource
    private CloudNativeConfigMapper cloudNativeConfigMapper;
    @Resource
    private CloudNativeRuleMapper cloudNativeRuleMapper;
    @Resource
    private CloudNativeConfigRuleMapper cloudNativeConfigRuleMapper;
    @Resource
    private CloudNativeResultMapper cloudNativeResultMapper;
    @Resource
    private CloudNativeConfigResultMapper cloudNativeConfigResultMapper;
    @Resource
    private K8sService k8sService;
    @Resource
    private ConfigService configService;
    @Resource
    private FileSystemRuleMapper fileSystemRuleMapper;
    @Resource
    private FileSystemResultMapper fileSystemResultMapper;
    @Resource
    private FileSystemMapper fileSystemMapper;
    @Resource
    private FileSystemService fileSystemService;


    public List<Favorite> listFavorites() {
        FavoriteExample example = new FavoriteExample();
        example.setOrderByClause("create_time desc");
        return favoriteMapper.selectByExample(example);
    }

    public AccountTreeDTO listAccounts() {
        AccountTreeDTO dto = new AccountTreeDTO();
        //云账号
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andPluginIdNotIn(PlatformUtils.getVulnPlugin());
        accountExample.setOrderByClause("create_time desc");
        List<AccountVo> accounts = extTaskMapper.selectAccountByExample(accountExample);
        dto.setCloudAccount(accounts);
        //漏洞
        AccountExample vulnExample = new AccountExample();
        vulnExample.createCriteria().andPluginIdIn(PlatformUtils.getVulnPlugin());
        vulnExample.setOrderByClause("create_time desc");
        List<AccountVo> vluns = extTaskMapper.selectVulnByExample(vulnExample);
        dto.setVulnAccount(vluns);
        //主机
        ServerExample serverExample = new ServerExample();
        serverExample.setOrderByClause("create_time desc");
        List<ServerVo> servers = extTaskMapper.selectServerByExample(serverExample);
        dto.setServerAccount(servers);
        //镜像
        ImageExample imageExample = new ImageExample();
        imageExample.setOrderByClause("create_time desc");
        List<ImageVo> images = extTaskMapper.selectImageByExample(imageExample);
        dto.setImageAccount(images);
        //源码
        CodeExample codeExample = new CodeExample();
        codeExample.setOrderByClause("create_time desc");
        List<CodeVo> codeVos = extTaskMapper.selectCodeByExample(codeExample);
        dto.setCodeAccount(codeVos);
        //文件
        FileSystemExample fileSystemExample = new FileSystemExample();
        fileSystemExample.setOrderByClause("create_time desc");
        List<FileSystemVo> fileSystemVos = extTaskMapper.selectFsByExample(fileSystemExample);
        dto.setFsAccount(fileSystemVos);
        //云原生
        CloudNativeExample cloudNativeExample = new CloudNativeExample();
        cloudNativeExample.setOrderByClause("create_time desc");
        List<K8sVo> k8sVos = extTaskMapper.selectK8sByExample(cloudNativeExample);
        dto.setK8sAccount(k8sVos);
        //部署
        CloudNativeConfigExample cloudNativeConfigExample = new CloudNativeConfigExample();
        cloudNativeConfigExample.setOrderByClause("create_time desc");
        List<ConfigVo> configVos = extTaskMapper.selectConfigByExample(cloudNativeConfigExample);
        dto.setConfigAccount(configVos);
        return dto;
    }

    public Favorite addOrDelFavorite(Favorite favorite) {
        FavoriteExample example = new FavoriteExample();
        example.createCriteria().andSourceIdEqualTo(favorite.getId());
        List<Favorite> list = favoriteMapper.selectByExample(example);
        if (list.size() > 0) {
            favoriteMapper.deleteByExample(example);
        } else {
            favorite.setSourceId(favorite.getId());
            favorite.setId(UUIDUtil.newUUID());
            favorite.setCreateTime(System.currentTimeMillis());
            favorite.setUpdateTime(System.currentTimeMillis());
            favorite.setCreator(SessionUtils.getUserId());
            favorite.setCreatorName(SessionUtils.getUser().getName());
            favoriteMapper.insertSelective(favorite);
        }
        return favorite;
    }

    public void deleteFavorite(String id) {
        favoriteMapper.deleteByPrimaryKey(id);
    }

    public List<RuleVo> allList(RuleVo ruleVo) {
        List<RuleVo> allList = new LinkedList<>();
        if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.cloudAccount.getType())) {
            allList = extTaskMapper.cloudRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.vulnAccount.getType())) {
            allList = extTaskMapper.vulnRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.serverAccount.getType())) {
            allList = extTaskMapper.serverRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.k8sAccount.getType())) {
            allList = extTaskMapper.k8sRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.configAccount.getType())) {
            allList = extTaskMapper.configRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.imageAccount.getType())) {
            allList = extTaskMapper.imageRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.codeAccount.getType())) {
            allList = extTaskMapper.codeRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.fsAccount.getType())) {
            allList = extTaskMapper.fsRuleList(ruleVo);
        }
        if (ruleVo.getAccountType() != null) allList.addAll(extTaskMapper.ruleTagList(ruleVo));
        if (ruleVo.getAccountType() != null && StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.cloudAccount.getType()))
            allList.addAll(extTaskMapper.ruleGroupList(ruleVo));
        return allList;
    }

    public List<RuleVo> ruleList(RuleVo ruleVo) {
        if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.cloudAccount.getType())) {
            return extTaskMapper.cloudRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.vulnAccount.getType())) {
            return extTaskMapper.vulnRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.serverAccount.getType())) {
            return extTaskMapper.serverRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.imageAccount.getType())) {
            return extTaskMapper.imageRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.k8sAccount.getType())) {
            return extTaskMapper.k8sRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.configAccount.getType())) {
            return extTaskMapper.configRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.codeAccount.getType())) {
            return extTaskMapper.codeRuleList(ruleVo);
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.fsAccount.getType())) {
            return extTaskMapper.fsRuleList(ruleVo);
        }
        return new LinkedList<>();
    }

    public List<RuleVo> ruleTagList(RuleVo ruleVo) {
        if (ruleVo.getAccountType() != null)
            return extTaskMapper.ruleTagList(ruleVo);
        return new LinkedList<>();
    }

    public List<RuleVo> ruleGroupList(RuleVo ruleVo) {
        if (ruleVo.getAccountType() != null && StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.cloudAccount.getType()))
            return extTaskMapper.ruleGroupList(ruleVo);
        return new LinkedList<>();
    }

    public TaskRuleDTO detailRule(RuleVo ruleVo) {
        TaskRuleDTO ruleDTO = new TaskRuleDTO();
        if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.cloudAccount.getType())) {
            ruleDTO.setRuleDTO(extTaskMapper.cloudDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.vulnAccount.getType())) {
            ruleDTO.setRuleDTO(extTaskMapper.vulnDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.serverAccount.getType())) {
            ruleDTO.setServerRuleDTO(extTaskMapper.serverDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.k8sAccount.getType())) {
            ruleDTO.setK8sRuleDTO(extTaskMapper.k8sDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.configAccount.getType())) {
            ruleDTO.setConfigRuleDTO(extTaskMapper.configDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.imageAccount.getType())) {
            ruleDTO.setImageRuleDTO(extTaskMapper.imageDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.codeAccount.getType())) {
            ruleDTO.setCodeRuleDTO(extTaskMapper.codeDetailRule(ruleVo));
        } else if (StringUtils.equalsIgnoreCase(ruleVo.getAccountType(), TaskEnum.fsAccount.getType())) {
            ruleDTO.setFsRuleDTO(extTaskMapper.fsDetailRule(ruleVo));
        }
        return ruleDTO;
    }

    public List<TaskTagGroupDTO> detailTag(RuleVo ruleVo) {
        return extTaskMapper.detailTag(ruleVo);
    }

    public List<TaskTagGroupDTO> detailGroup(RuleVo ruleVo) {
        return extTaskMapper.detailGroup(ruleVo);
    }

    public List<TaskVo> taskList(TaskRequest request) {
        return extTaskMapper.taskList(request);
    }

    public List<Task> allTaskList() {
        return taskMapper.selectByExample(null);
    }

    public TaskReportDTO report(String id) {
        return extTaskMapper.report(id);
    }

    public TaskVo getTask(String id) {
        TaskRequest request = new TaskRequest();
        request.setId(id);
        return extTaskMapper.taskList(request).get(0);
    }

    public int addTask(TaskDTO taskDTO) throws Exception {
        Task task = BeanUtils.copyBean(new Task(), taskDTO);
        task.setId(UUIDUtil.newUUID());
        task.setStatus(TaskConstants.TASK_STATUS.WAITING.name());
        task.setApplyUser(SessionUtils.getUserId());
        task.setCreateTime(System.currentTimeMillis());
        task.setType(TaskConstants.TaskType.manual.name());
        int i = taskMapper.insertSelective(task);
        for (TaskItem taskItem : taskDTO.getTaskItemList()) {
            taskItem.setId(UUIDUtil.newUUID());
            taskItem.setTaskId(task.getId());
            taskItem.setStatus(TaskConstants.TASK_STATUS.WAITING.name());
            taskItem.setCreateTime(System.currentTimeMillis());
            taskItemMapper.insertSelective(taskItem);
        }
        return i;
    }

    public int editTask(TaskDTO taskDTO) throws Exception {
        this.deleteTask(taskDTO.getId());
        Task task = BeanUtils.copyBean(new Task(), taskDTO);
        task.setId(UUIDUtil.newUUID());
        task.setStatus(TaskConstants.TASK_STATUS.WAITING.name());
        task.setApplyUser(SessionUtils.getUserId());
        task.setCreateTime(System.currentTimeMillis());
        int i = taskMapper.insertSelective(task);
        for (TaskItem taskItem : taskDTO.getTaskItemList()) {
            taskItem.setId(UUIDUtil.newUUID());
            taskItem.setTaskId(task.getId());
            taskItem.setStatus(TaskConstants.TASK_STATUS.WAITING.name());
            taskItem.setCreateTime(System.currentTimeMillis());
            taskItemMapper.insertSelective(taskItem);
        }
        return i;
    }

    public void deleteTask(String taskId) throws Exception {
        try {
            TaskItemExample example = new TaskItemExample();
            example.createCriteria().andTaskIdEqualTo(taskId);
            List<TaskItem> taskItems = taskItemMapper.selectByExample(example);
            for (TaskItem taskItem : taskItems) {
                TaskItemResourceExample taskItemResourceExample = new TaskItemResourceExample();
                taskItemResourceExample.createCriteria().andTaskIdEqualTo(taskId).andTaskItemIdEqualTo(taskItem.getId());
                taskItemResourceMapper.deleteByExample(taskItemResourceExample);
                TaskItemResourceLogExample taskItemResourceLogExample = new TaskItemResourceLogExample();
                taskItemResourceLogExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId());
                taskItemResourceLogMapper.deleteByExample(taskItemResourceLogExample);
                taskItemMapper.deleteByPrimaryKey(taskItem.getId());
            }
            taskMapper.deleteByPrimaryKey(taskId);
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
    }

    public List<TaskItem> taskItemList(TaskRequest request) {
        TaskItemExample example = new TaskItemExample();
        example.createCriteria().andTaskIdEqualTo(request.getId());
        example.setOrderByClause("task_order");
        return taskItemMapper.selectByExample(example);
    }

    public void executeTask(String id) throws Exception {
        Task task = taskMapper.selectByPrimaryKey(id);
        TaskItemExample example = new TaskItemExample();
        example.createCriteria().andTaskIdEqualTo(id);
        example.setOrderByClause("task_order desc");
        List<TaskItem> taskItems = taskItemMapper.selectByExample(example);
        for (TaskItem taskItem : taskItems) {
            String ruleType = taskItem.getRuleType();
            if (StringUtils.equalsIgnoreCase(ruleType, TaskConstants.RuleType.rule.name())) {
                String resourceId = "", ruleId = "", ruleName = "";
                if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.cloudAccount.getType())) {
                    Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.cloudResource(rule, taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.vulnAccount.getType())) {
                    Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.vulnResource(rule, taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.serverAccount.getType())) {
                    ServerRule rule = serverRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.serverResource(taskItem.getSourceId(), taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.k8sAccount.getType())) {
                    CloudNativeRule rule = cloudNativeRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.k8sResource(taskItem.getSourceId(), taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.configAccount.getType())) {
                    CloudNativeConfigRule rule = cloudNativeConfigRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.configResource(taskItem.getSourceId(), taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.imageAccount.getType())) {
                    ImageRule rule = imageRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.imageResource(taskItem.getSourceId(), taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.codeAccount.getType())) {
                    CodeRule rule = codeRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.codeResource(taskItem.getSourceId(), taskItem.getAccountId());
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.fsAccount.getType())) {
                    FileSystemRule rule = fileSystemRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                    ruleId = rule.getId();
                    ruleName = rule.getName();
                    resourceId = this.fsResource(taskItem.getSourceId(), taskItem.getAccountId());
                }
                this.insertTaskItemResource(taskItem, ruleId, ruleName, resourceId);
            } else if (StringUtils.equalsIgnoreCase(ruleType, TaskConstants.RuleType.tag.name())) {
                String resourceId = "";
                List<RuleTagMapping> ruleTagMappings = this.ruleTagMappings(taskItem.getSourceId());
                AccountWithBLOBs accountWithBLOBs = accountMapper.selectByPrimaryKey(taskItem.getAccountId());
                if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.cloudAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        Rule rule = ruleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (rule == null || rule.getPluginId() == null || !accountWithBLOBs.getPluginId().equals(rule.getPluginId())) {
                            continue;
                        }
                        resourceId = this.cloudResource(rule, taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, rule.getId(), rule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.vulnAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        Rule rule = ruleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (rule == null || rule.getPluginId() == null || !accountWithBLOBs.getPluginId().equals(rule.getPluginId())) {
                            continue;
                        }
                        resourceId = this.vulnResource(rule, taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, rule.getId(), rule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.serverAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        ServerRule serverRule = serverRuleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (serverRule == null) {
                            continue;
                        }
                        resourceId = this.serverResource(serverRule.getId(), taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, serverRule.getId(), serverRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.k8sAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        CloudNativeRule cloudNativeRule = cloudNativeRuleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (cloudNativeRule == null) {
                            continue;
                        }
                        resourceId = this.k8sResource(cloudNativeRule.getId(), taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, cloudNativeRule.getId(), cloudNativeRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.configAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        CloudNativeConfigRule cloudNativeConfigRule = cloudNativeConfigRuleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (cloudNativeConfigRule == null) {
                            continue;
                        }
                        resourceId = this.configResource(cloudNativeConfigRule.getId(), taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, cloudNativeConfigRule.getId(), cloudNativeConfigRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.imageAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        ImageRule imageRule = imageRuleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (imageRule == null) {
                            continue;
                        }
                        resourceId = this.imageResource(imageRule.getId(), taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, imageRule.getId(), imageRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.codeAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        CodeRule codeRule = codeRuleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (codeRule == null) {
                            continue;
                        }
                        resourceId = this.codeResource(codeRule.getId(), taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, codeRule.getId(), codeRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.fsAccount.getType())) {
                    for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                        FileSystemRule fileSystemRule = fileSystemRuleMapper.selectByPrimaryKey(ruleTagMapping.getRuleId());
                        if (fileSystemRule == null) {
                            continue;
                        }
                        resourceId = this.fsResource(fileSystemRule.getId(), taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, fileSystemRule.getId(), fileSystemRule.getName(), resourceId);
                    }
                }
            } else if (StringUtils.equalsIgnoreCase(ruleType, TaskConstants.RuleType.group.name())) {
                String resourceId = "";
                List<RuleGroupMapping> ruleGroupMappings = this.ruleGroupMappings(taskItem.getSourceId());
                if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.cloudAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        Rule rule = ruleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.cloudResource(rule, taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, rule.getId(), rule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.vulnAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        Rule rule = ruleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.vulnResource(rule, taskItem.getAccountId());
                        if (resourceId == null) continue;
                        this.insertTaskItemResource(taskItem, rule.getId(), rule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.serverAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        ServerRule serverRule = serverRuleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.serverResource(serverRule.getId(), taskItem.getAccountId());
                        this.insertTaskItemResource(taskItem, serverRule.getId(), serverRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.k8sAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        CloudNativeRule cloudNativeRule = cloudNativeRuleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.k8sResource(cloudNativeRule.getId(), taskItem.getAccountId());
                        this.insertTaskItemResource(taskItem, cloudNativeRule.getId(), cloudNativeRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.configAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        CloudNativeConfigRule cloudNativeConfigRule = cloudNativeConfigRuleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.configResource(cloudNativeConfigRule.getId(), taskItem.getAccountId());
                        this.insertTaskItemResource(taskItem, cloudNativeConfigRule.getId(), cloudNativeConfigRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.imageAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        ImageRule imageRule = imageRuleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.imageResource(imageRule.getId(), taskItem.getAccountId());
                        this.insertTaskItemResource(taskItem, imageRule.getId(), imageRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.codeAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        CodeRule codeRule = codeRuleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.codeResource(codeRule.getId(), taskItem.getAccountId());
                        this.insertTaskItemResource(taskItem, codeRule.getId(), codeRule.getName(), resourceId);
                    }
                } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.fsAccount.getType())) {
                    for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                        FileSystemRule fileSystemRule = fileSystemRuleMapper.selectByPrimaryKey(ruleGroupMapping.getRuleId());
                        resourceId = this.fsResource(fileSystemRule.getId(), taskItem.getAccountId());
                        this.insertTaskItemResource(taskItem, fileSystemRule.getId(), fileSystemRule.getName(), resourceId);
                    }
                }
            }
            taskItem.setStatus(TaskConstants.TASK_STATUS.APPROVED.name());
            taskItemMapper.updateByPrimaryKeySelective(taskItem);
        }
        task.setLastFireTime(System.currentTimeMillis());
        task.setStatus(TaskConstants.TASK_STATUS.APPROVED.name());
        taskMapper.updateByPrimaryKeySelective(task);
    }

    public void reExecute(String id) throws Exception {
        Task task = taskMapper.selectByPrimaryKey(id);
        TaskItemExample taskItemExample = new TaskItemExample();
        taskItemExample.createCriteria().andTaskIdEqualTo(id);
        taskItemExample.setOrderByClause("task_order desc");
        List<TaskItem> taskItems = taskItemMapper.selectByExample(taskItemExample);
        for (TaskItem taskItem : taskItems) {
            TaskItemResourceExample example = new TaskItemResourceExample();
            example.createCriteria().andTaskIdEqualTo(id).andTaskItemIdEqualTo(taskItem.getId());
            List<TaskItemResource> taskItemResources = taskItemResourceMapper.selectByExample(example);
            for (TaskItemResource taskItemResource : taskItemResources) {
                String ruleType = taskItem.getRuleType();
                if (StringUtils.equalsIgnoreCase(ruleType, TaskConstants.RuleType.rule.name())) {
                    String resourceId = "", ruleId = "";
                    if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.cloudAccount.getType())) {
                        Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = ruleService.reScan(taskItemResource.getResourceId(), taskItem.getAccountId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.vulnAccount.getType())) {
                        Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = ruleService.reScan(taskItemResource.getResourceId(), taskItem.getAccountId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.serverAccount.getType())) {
                        ServerRule rule = serverRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = serverService.rescan(taskItemResource.getResourceId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.k8sAccount.getType())) {
                        CloudNativeRule rule = cloudNativeRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = k8sService.reScan(taskItemResource.getResourceId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.configAccount.getType())) {
                        CloudNativeConfigRule rule = cloudNativeConfigRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = configService.reScan(taskItemResource.getResourceId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.imageAccount.getType())) {
                        ImageRule rule = imageRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = imageService.reScan(taskItemResource.getResourceId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.codeAccount.getType())) {
                        CodeRule rule = codeRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = codeService.reScan(taskItemResource.getResourceId());
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.fsAccount.getType())) {
                        FileSystemRule rule = fileSystemRuleMapper.selectByPrimaryKey(taskItem.getSourceId());
                        ruleId = rule.getId();
                        resourceId = fileSystemService.reScan(taskItemResource.getResourceId());
                    }
                    this.updateTaskItemResource(taskItemResource, ruleId, resourceId);
                } else if (StringUtils.equalsIgnoreCase(ruleType, TaskConstants.RuleType.tag.name())) {
                    String resourceId = "";
                    List<RuleTagMapping> ruleTagMappings = this.ruleTagMappings(taskItemResource.getResourceId());
                    if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.cloudAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            CloudTaskExample cloudTaskExample = new CloudTaskExample();
                            cloudTaskExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId()).andAccountIdEqualTo(taskItem.getAccountId());
                            List<CloudTask> cloudTasks = cloudTaskMapper.selectByExample(cloudTaskExample);
                            for (CloudTask cloudTask : cloudTasks) {
                                Rule rule = ruleMapper.selectByPrimaryKey(cloudTask.getRuleId());
                                resourceId = ruleService.reScan(cloudTask.getId(), cloudTask.getAccountId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, rule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.vulnAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            CloudTaskExample cloudTaskExample = new CloudTaskExample();
                            cloudTaskExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId()).andAccountIdEqualTo(taskItem.getAccountId());
                            List<CloudTask> cloudTasks = cloudTaskMapper.selectByExample(cloudTaskExample);
                            for (CloudTask cloudTask : cloudTasks) {
                                Rule rule = ruleMapper.selectByPrimaryKey(cloudTask.getRuleId());
                                resourceId = ruleService.reScan(cloudTask.getId(), cloudTask.getAccountId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, rule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.serverAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            ServerResultExample serverResultExample = new ServerResultExample();
                            serverResultExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId());
                            List<ServerResult> serverResults = serverResultMapper.selectByExample(serverResultExample);
                            for (ServerResult serverResult : serverResults) {
                                ServerRule serverRule = serverRuleMapper.selectByPrimaryKey(serverResult.getRuleId());
                                resourceId = serverService.rescan(serverResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, serverRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.k8sAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            CloudNativeResultExample cloudNativeResultExample = new CloudNativeResultExample();
                            cloudNativeResultExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId());
                            List<CloudNativeResult> cloudNativeResults = cloudNativeResultMapper.selectByExample(cloudNativeResultExample);
                            for (CloudNativeResult cloudNativeResult : cloudNativeResults) {
                                CloudNativeRule cloudNativeRule = cloudNativeRuleMapper.selectByPrimaryKey(cloudNativeResult.getRuleId());
                                resourceId = k8sService.reScan(cloudNativeResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, cloudNativeRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.configAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            CloudNativeConfigResultExample cloudNativeConfigResultExample = new CloudNativeConfigResultExample();
                            cloudNativeConfigResultExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId());
                            List<CloudNativeConfigResult> cloudNativeConfigResults = cloudNativeConfigResultMapper.selectByExample(cloudNativeConfigResultExample);
                            for (CloudNativeConfigResult cloudNativeConfigResult : cloudNativeConfigResults) {
                                CloudNativeConfigRule cloudNativeConfigRule = cloudNativeConfigRuleMapper.selectByPrimaryKey(cloudNativeConfigResult.getRuleId());
                                resourceId = configService.reScan(cloudNativeConfigResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, cloudNativeConfigRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.imageAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            ImageResultExample imageResultExample = new ImageResultExample();
                            imageResultExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId());
                            List<ImageResult> imageResults = imageResultMapper.selectByExample(imageResultExample);
                            for (ImageResult imageResult : imageResults) {
                                ImageRule imageRule = imageRuleMapper.selectByPrimaryKey(imageResult.getRuleId());
                                resourceId = imageService.reScan(imageResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, imageRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.codeAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            CodeResultExample codeResultExample = new CodeResultExample();
                            codeResultExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId());
                            List<CodeResult> codeResults = codeResultMapper.selectByExample(codeResultExample);
                            for (CodeResult codeResult : codeResults) {
                                CodeRule codeRule = codeRuleMapper.selectByPrimaryKey(codeResult.getRuleId());
                                resourceId = codeService.reScan(codeRule.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, codeRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.fsAccount.getType())) {
                        for (RuleTagMapping ruleTagMapping : ruleTagMappings) {
                            FileSystemResultExample fileSystemResultExample = new FileSystemResultExample();
                            fileSystemResultExample.createCriteria().andRuleIdEqualTo(ruleTagMapping.getRuleId());
                            List<FileSystemResult> fileSystemResults = fileSystemResultMapper.selectByExample(fileSystemResultExample);
                            for (FileSystemResult fileSystemResult : fileSystemResults) {
                                FileSystemRule fileSystemRule = fileSystemRuleMapper.selectByPrimaryKey(fileSystemResult.getRuleId());
                                resourceId = fileSystemService.reScan(fileSystemRule.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, fileSystemRule.getId(), resourceId);
                            }
                        }
                    }
                } else if (StringUtils.equalsIgnoreCase(ruleType, TaskConstants.RuleType.group.name())) {
                    String resourceId = "";
                    List<RuleGroupMapping> ruleGroupMappings = this.ruleGroupMappings(taskItemResource.getResourceId());
                    if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.cloudAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            CloudTaskExample cloudTaskExample = new CloudTaskExample();
                            cloudTaskExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId()).andAccountIdEqualTo(taskItem.getAccountId());
                            List<CloudTask> cloudTasks = cloudTaskMapper.selectByExample(cloudTaskExample);
                            for (CloudTask cloudTask : cloudTasks) {
                                Rule rule = ruleMapper.selectByPrimaryKey(cloudTask.getRuleId());
                                resourceId = ruleService.reScan(cloudTask.getId(), cloudTask.getAccountId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, rule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.vulnAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            CloudTaskExample cloudTaskExample = new CloudTaskExample();
                            cloudTaskExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId()).andAccountIdEqualTo(taskItem.getAccountId());
                            List<CloudTask> cloudTasks = cloudTaskMapper.selectByExample(cloudTaskExample);
                            for (CloudTask cloudTask : cloudTasks) {
                                Rule rule = ruleMapper.selectByPrimaryKey(cloudTask.getRuleId());
                                resourceId = ruleService.reScan(cloudTask.getId(), cloudTask.getAccountId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, rule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.serverAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            ServerResultExample serverResultExample = new ServerResultExample();
                            serverResultExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId());
                            List<ServerResult> serverResults = serverResultMapper.selectByExample(serverResultExample);
                            for (ServerResult serverResult : serverResults) {
                                ServerRule serverRule = serverRuleMapper.selectByPrimaryKey(serverResult.getRuleId());
                                resourceId = serverService.rescan(serverResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, serverRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.k8sAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            CloudNativeResultExample cloudNativeResultExample = new CloudNativeResultExample();
                            cloudNativeResultExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId());
                            List<CloudNativeResult> cloudNativeResults = cloudNativeResultMapper.selectByExample(cloudNativeResultExample);
                            for (CloudNativeResult cloudNativeResult : cloudNativeResults) {
                                CloudNativeRule cloudNativeRule = cloudNativeRuleMapper.selectByPrimaryKey(cloudNativeResult.getRuleId());
                                resourceId = k8sService.reScan(cloudNativeResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, cloudNativeRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.configAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            CloudNativeConfigResultExample cloudNativeConfigResultExample = new CloudNativeConfigResultExample();
                            cloudNativeConfigResultExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId());
                            List<CloudNativeConfigResult> cloudNativeConfigResults = cloudNativeConfigResultMapper.selectByExample(cloudNativeConfigResultExample);
                            for (CloudNativeConfigResult cloudNativeConfigResult : cloudNativeConfigResults) {
                                CloudNativeConfigRule cloudNativeConfigRule = cloudNativeConfigRuleMapper.selectByPrimaryKey(cloudNativeConfigResult.getRuleId());
                                resourceId = configService.reScan(cloudNativeConfigResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, cloudNativeConfigRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.imageAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            ImageResultExample imageResultExample = new ImageResultExample();
                            imageResultExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId());
                            List<ImageResult> imageResults = imageResultMapper.selectByExample(imageResultExample);
                            for (ImageResult imageResult : imageResults) {
                                ImageRule imageRule = imageRuleMapper.selectByPrimaryKey(imageResult.getRuleId());
                                resourceId = imageService.reScan(imageResult.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, imageRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.codeAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            CodeResultExample codeResultExample = new CodeResultExample();
                            codeResultExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId());
                            List<CodeResult> codeResults = codeResultMapper.selectByExample(codeResultExample);
                            for (CodeResult codeResult : codeResults) {
                                CodeRule codeRule = codeRuleMapper.selectByPrimaryKey(codeResult.getRuleId());
                                resourceId = codeService.reScan(codeRule.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, codeRule.getId(), resourceId);
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(taskItem.getAccountType(), TaskEnum.fsAccount.getType())) {
                        for (RuleGroupMapping ruleGroupMapping : ruleGroupMappings) {
                            FileSystemResultExample fileSystemResultExample = new FileSystemResultExample();
                            fileSystemResultExample.createCriteria().andRuleIdEqualTo(ruleGroupMapping.getRuleId());
                            List<FileSystemResult> fileSystemResults = fileSystemResultMapper.selectByExample(fileSystemResultExample);
                            for (FileSystemResult fileSystemResult : fileSystemResults) {
                                FileSystemRule fileSystemRule = fileSystemRuleMapper.selectByPrimaryKey(fileSystemResult.getRuleId());
                                resourceId = fileSystemService.reScan(fileSystemRule.getId());
                                if (resourceId == null) continue;
                                this.updateTaskItemResource(taskItemResource, fileSystemRule.getId(), resourceId);
                            }
                        }
                    }
                }
                taskItem.setStatus(TaskConstants.TASK_STATUS.APPROVED.name());
                taskItemMapper.updateByPrimaryKeySelective(taskItem);
            }
            task.setLastFireTime(System.currentTimeMillis());
            task.setStatus(TaskConstants.TASK_STATUS.APPROVED.name());
        }
        taskMapper.updateByPrimaryKeySelective(task);
    }

    private String dealCloudTask(Rule rule, AccountWithBLOBs account, Integer scanId) {
        try {
            if (rule.getStatus()) {
                QuartzTaskDTO quartzTaskDTO = new QuartzTaskDTO();
                BeanUtils.copyBean(quartzTaskDTO, rule);
                List<SelectTag> selectTags = new LinkedList<>();
                SelectTag s = new SelectTag();
                s.setAccountId(account.getId());
                JSONArray array = parseArray(account.getRegions() != null ? account.getRegions() : account.getRegions());
                JSONObject object;
                List<String> regions = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    try {
                        object = array.getJSONObject(i);
                        String value = object.getString("regionId");
                        regions.add(value);
                    } catch (Exception e) {
                        String value = array.get(0).toString();
                        regions.add(value);
                    }
                }
                s.setRegions(regions);
                selectTags.add(s);
                quartzTaskDTO.setSelectTags(selectTags);
                quartzTaskDTO.setType("manual");
                quartzTaskDTO.setAccountId(account.getId());
                quartzTaskDTO.setTaskName(rule.getName());
                CloudTask cloudTask = cloudTaskService.saveManualTask(quartzTaskDTO, null);
                historyService.insertScanTaskHistory(cloudTask, scanId, cloudTask.getAccountId(), TaskEnum.cloudAccount.getType());
                return cloudTask.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealVulnTask(Rule rule, AccountWithBLOBs account, Integer scanId) {
        try {
            if (rule.getStatus()) {
                QuartzTaskDTO quartzTaskDTO = new QuartzTaskDTO();
                BeanUtils.copyBean(quartzTaskDTO, rule);
                List<SelectTag> selectTags = new LinkedList<>();
                SelectTag s = new SelectTag();
                s.setAccountId(account.getId());
                JSONArray array = parseArray(account.getRegions() != null ? account.getRegions() : account.getRegions());
                JSONObject object;
                List<String> regions = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    try {
                        object = array.getJSONObject(i);
                        String value = object.getString("regionId");
                        regions.add(value);
                    } catch (Exception e) {
                        String value = array.get(0).toString();
                        regions.add(value);
                    }
                }
                s.setRegions(regions);
                selectTags.add(s);
                quartzTaskDTO.setSelectTags(selectTags);
                quartzTaskDTO.setType("manual");
                quartzTaskDTO.setAccountId(account.getId());
                quartzTaskDTO.setTaskName(rule.getName());
                CloudTask cloudTask = cloudTaskService.saveManualTask(quartzTaskDTO, null);
                historyService.insertScanTaskHistory(cloudTask, scanId, cloudTask.getAccountId(), TaskEnum.vulnAccount.getType());
                return cloudTask.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealServerTask(ServerRule rule, Server server, Integer scanId) {
        try {
            if (rule.getStatus()) {
                ServerResult result = new ServerResult();
                String serverGroupName = serverGroupMapper.selectByPrimaryKey(server.getServerGroupId()).getName();
                BeanUtils.copyBean(result, server);
                result.setId(UUIDUtil.newUUID());
                result.setServerId(server.getId());
                result.setServerGroupId(server.getServerGroupId());
                result.setServerGroupName(serverGroupName);
                result.setApplyUser(SessionUtils.getUserId());
                result.setCreateTime(System.currentTimeMillis());
                result.setUpdateTime(System.currentTimeMillis());
                result.setServerName(server.getName());
                result.setRuleId(rule.getId());
                result.setRuleName(rule.getName());
                result.setRuleDesc(rule.getDescription());
                result.setResultStatus(TaskConstants.TASK_STATUS.APPROVED.toString());
                result.setSeverity(rule.getSeverity());
                ServerResultExample serverResultExample = new ServerResultExample();
                serverResultExample.createCriteria().andRuleIdEqualTo(rule.getId()).andServerIdEqualTo(server.getId());
                serverResultMapper.deleteByExample(serverResultExample);
                serverResultMapper.insertSelective(result);

                serverService.saveServerResultLog(result.getId(), "i18n_start_server_result", "", true);
                OperationLogService.log(SessionUtils.getUser(), result.getId(), result.getServerName(), ResourceTypeConstants.SERVER.name(), ResourceOperation.CREATE, "i18n_start_server_result");
                historyService.insertScanTaskHistory(result, scanId, result.getServerId(), TaskEnum.serverAccount.getType());

                historyService.insertHistoryServerResult(BeanUtils.copyBean(new HistoryServerResult(), result));
                return result.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealK8sTask(CloudNativeRule rule, CloudNative cloudNative, Integer scanId) {
        try {
            if (rule.getStatus()) {
                CloudNativeResultWithBLOBs result = new CloudNativeResultWithBLOBs();
                BeanUtils.copyBean(result, cloudNative);
                result.setId(UUIDUtil.newUUID());
                result.setCloudNativeId(cloudNative.getId());
                result.setApplyUser(SessionUtils.getUserId());
                result.setCreateTime(System.currentTimeMillis());
                result.setUpdateTime(System.currentTimeMillis());
                result.setResultStatus(CloudTaskConstants.TASK_STATUS.APPROVED.toString());
                result.setUserName(SessionUtils.getUser().getName());
                result.setRuleId(rule.getId());
                result.setRuleName(rule.getName());
                result.setRuleDesc(rule.getDescription());
                result.setSeverity(rule.getSeverity());
                CloudNativeResultExample cloudNativeResultExample = new CloudNativeResultExample();
                cloudNativeResultExample.createCriteria().andRuleIdEqualTo(rule.getId()).andCloudNativeIdEqualTo(cloudNative.getId());
                cloudNativeResultMapper.deleteByExample(cloudNativeResultExample);
                cloudNativeResultMapper.insertSelective(result);

                k8sService.saveCloudNativeResultLog(result.getId(), "i18n_start_k8s_result", "", true);
                OperationLogService.log(SessionUtils.getUser(), result.getId(), result.getName(), ResourceTypeConstants.CLOUD_NATIVE.name(), ResourceOperation.CREATE, "i18n_start_k8s_result");
                historyService.insertScanTaskHistory(result, scanId, result.getCloudNativeId(), TaskEnum.k8sAccount.getType());

                historyService.insertHistoryServerResult(BeanUtils.copyBean(new HistoryServerResult(), result));
                return result.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealConfigTask(CloudNativeConfigRule rule, CloudNativeConfig config, Integer scanId) {
        try {
            if (rule.getStatus()) {
                CloudNativeConfigResult result = new CloudNativeConfigResult();
                BeanUtils.copyBean(result, config);
                result.setId(UUIDUtil.newUUID());
                result.setConfigId(config.getId());
                result.setApplyUser(SessionUtils.getUserId());
                result.setCreateTime(System.currentTimeMillis());
                result.setUpdateTime(System.currentTimeMillis());
                result.setResultStatus(CloudTaskConstants.TASK_STATUS.APPROVED.toString());
                result.setUserName(SessionUtils.getUser().getName());
                result.setRuleId(rule.getId());
                result.setRuleName(rule.getName());
                result.setRuleDesc(rule.getDescription());
                result.setSeverity(rule.getSeverity());
                CloudNativeConfigResultExample example = new CloudNativeConfigResultExample();
                example.createCriteria().andRuleIdEqualTo(rule.getId()).andConfigIdEqualTo(config.getId());
                cloudNativeConfigResultMapper.deleteByExample(example);
                cloudNativeConfigResultMapper.insertSelective(result);

                configService.saveCloudNativeConfigResultLog(result.getId(), "i18n_start_config_result", "", true);
                OperationLogService.log(SessionUtils.getUser(), result.getId(), result.getName(), ResourceTypeConstants.CLOUD_NATIVE_CONFIG.name(), ResourceOperation.CREATE, "i18n_start_config_result");
                historyService.insertScanTaskHistory(result, scanId, result.getConfigId(), TaskEnum.configAccount.getType());

                historyService.insertHistoryServerResult(BeanUtils.copyBean(new HistoryServerResult(), result));
                return result.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealCodeTask(CodeRule rule, Code code, Integer scanId) {
        try {
            if (rule.getStatus()) {
                CodeResult result = new CodeResult();
                BeanUtils.copyBean(result, code);
                result.setId(UUIDUtil.newUUID());
                result.setCodeId(code.getId());
                result.setApplyUser(SessionUtils.getUserId());
                result.setCreateTime(System.currentTimeMillis());
                result.setUpdateTime(System.currentTimeMillis());
                result.setRuleId(rule.getId());
                result.setRuleName(rule.getName());
                result.setRuleDesc(rule.getDescription());
                result.setResultStatus(TaskConstants.TASK_STATUS.APPROVED.toString());
                result.setSeverity(rule.getSeverity());
                result.setUserName(SessionUtils.getUser().getName());
                CodeResultExample codeResultExample = new CodeResultExample();
                codeResultExample.createCriteria().andCodeIdEqualTo(code.getId()).andRuleIdEqualTo(rule.getId());
                codeResultMapper.deleteByExample(codeResultExample);
                codeResultMapper.insertSelective(result);

                codeService.saveCodeResultLog(result.getId(), "i18n_start_code_result", "", true);
                OperationLogService.log(SessionUtils.getUser(), result.getId(), result.getName(), ResourceTypeConstants.CODE.name(), ResourceOperation.CREATE, "i18n_start_code_result");
                historyService.insertScanTaskHistory(result, scanId, result.getCodeId(), TaskEnum.codeAccount.getType());
                historyService.insertHistoryCodeResult(BeanUtils.copyBean(new HistoryCodeResult(), result));
                return result.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealFsTask(FileSystemRule rule, FileSystem fileSystem, Integer scanId) {
        try {
            if (rule.getStatus()) {
                FileSystemResult result = new FileSystemResult();
                BeanUtils.copyBean(result, fileSystem);
                result.setId(UUIDUtil.newUUID());
                result.setFsId(fileSystem.getId());
                result.setApplyUser(SessionUtils.getUserId());
                result.setCreateTime(System.currentTimeMillis());
                result.setUpdateTime(System.currentTimeMillis());
                result.setRuleId(rule.getId());
                result.setRuleName(rule.getName());
                result.setRuleDesc(rule.getDescription());
                result.setResultStatus(TaskConstants.TASK_STATUS.APPROVED.toString());
                result.setSeverity(rule.getSeverity());
                result.setUserName(SessionUtils.getUser().getName());
                FileSystemResultExample fileSystemResultExample = new FileSystemResultExample();
                fileSystemResultExample.createCriteria().andFsIdEqualTo(fileSystem.getId()).andRuleIdEqualTo(rule.getId());
                fileSystemResultMapper.deleteByExample(fileSystemResultExample);
                fileSystemResultMapper.insertSelective(result);

                fileSystemService.saveFsResultLog(result.getId(), "i18n_start_fs_result", "", true);
                OperationLogService.log(SessionUtils.getUser(), result.getId(), result.getName(), ResourceTypeConstants.FILE_SYSTEM.name(), ResourceOperation.CREATE, "i18n_start_fs_result");
                historyService.insertScanTaskHistory(result, scanId, result.getFsId(), TaskEnum.fsAccount.getType());
                historyService.insertHistoryFileSystemResult(BeanUtils.copyBean(new HistoryFileSystemResult(), result));
                return result.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String dealImageTask(ImageRule rule, Image image, Integer scanId) {
        try {
            if (rule.getStatus()) {
                ImageResultWithBLOBs result = new ImageResultWithBLOBs();
                BeanUtils.copyBean(result, image);
                result.setId(UUIDUtil.newUUID());
                result.setImageId(image.getId());
                result.setApplyUser(SessionUtils.getUserId());
                result.setCreateTime(System.currentTimeMillis());
                result.setUpdateTime(System.currentTimeMillis());
                result.setRuleId(rule.getId());
                result.setRuleName(rule.getName());
                result.setRuleDesc(rule.getDescription());
                result.setResultStatus(TaskConstants.TASK_STATUS.APPROVED.toString());
                result.setSeverity(rule.getSeverity());
                result.setUserName(SessionUtils.getUser().getName());
                ImageResultExample imageResultExample = new ImageResultExample();
                imageResultExample.createCriteria().andImageIdEqualTo(image.getId()).andRuleIdEqualTo(rule.getId());
                imageResultMapper.deleteByExample(imageResultExample);
                imageResultMapper.insertSelective(result);

                imageService.saveImageResultLog(result.getId(), "i18n_start_image_result", "", true);
                OperationLogService.log(SessionUtils.getUser(), result.getId(), result.getName(), ResourceTypeConstants.IMAGE.name(), ResourceOperation.CREATE, "i18n_start_image_result");
                historyService.insertScanTaskHistory(result, scanId, result.getImageId(), TaskEnum.imageAccount.getType());
                historyService.insertHistoryImageResult(BeanUtils.copyBean(new HistoryImageResultWithBLOBs(), result));
                return result.getId();
            } else {
                HRException.throwException(rule.getName() + ": " + Translator.get("i18n_disabled_rules_not_scanning"));
            }
        } catch (Exception e) {
            HRException.throwException(e.getMessage());
        }
        return "";
    }

    private String cloudResource(Rule rule, String accountId) throws Exception {
        if (rule == null) return null;
        AccountWithBLOBs account = accountMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(account);
        return this.dealCloudTask(rule, account, scanId);
    }

    private String vulnResource(Rule rule, String accountId) throws Exception {
        if (rule == null) return null;
        AccountWithBLOBs account = accountMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(account);
        return this.dealVulnTask(rule, account, scanId);
    }

    private String serverResource(String ruleId, String accountId) throws Exception {
        ServerRule serverRule = serverRuleMapper.selectByPrimaryKey(ruleId);
        Server server = serverMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(server);
        return this.dealServerTask(serverRule, server, scanId);
    }

    private String k8sResource(String ruleId, String accountId) throws Exception {
        CloudNativeRule cloudNativeRule = cloudNativeRuleMapper.selectByPrimaryKey(ruleId);
        CloudNative cloudNative = cloudNativeMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(cloudNative);
        return this.dealK8sTask(cloudNativeRule, cloudNative, scanId);
    }

    private String configResource(String ruleId, String accountId) throws Exception {
        CloudNativeConfigRule configRule = cloudNativeConfigRuleMapper.selectByPrimaryKey(ruleId);
        CloudNativeConfig config = cloudNativeConfigMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(config);
        return this.dealConfigTask(configRule, config, scanId);
    }

    private String codeResource(String ruleId, String accountId) throws Exception {
        CodeRule codeRule = codeRuleMapper.selectByPrimaryKey(ruleId);
        Code code = codeMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(code);
        return this.dealCodeTask(codeRule, code, scanId);
    }

    private String fsResource(String ruleId, String accountId) throws Exception {
        FileSystemRule fsRule = fileSystemRuleMapper.selectByPrimaryKey(ruleId);
        FileSystem fileSystem = fileSystemMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(fileSystem);
        return this.dealFsTask(fsRule, fileSystem, scanId);
    }

    private String imageResource(String ruleId, String accountId) throws Exception {
        ImageRule imageRule = imageRuleMapper.selectByPrimaryKey(ruleId);
        Image image = imageMapper.selectByPrimaryKey(accountId);
        Integer scanId = historyService.insertScanHistory(image);
        return this.dealImageTask(imageRule, image, scanId);
    }

    private void insertTaskItemResource(TaskItem taskItem, String ruleId, String ruleName, String resourceId) throws Exception {
        TaskItemResource record = new TaskItemResource();
        BeanUtils.copyBean(record, taskItem);
        record.setResourceId(resourceId);
        record.setRuleId(ruleId);
        record.setRuleName(ruleName);
        record.setTaskItemId(taskItem.getId());
        record.setCreateTime(System.currentTimeMillis());
        taskItemResourceMapper.insertSelective(record);
        int key = record.getId();

        saveTaskItemResourceLog(record.getTaskItemId(), String.valueOf(key), resourceId, "i18n_start_task", "", true);
    }

    void saveTaskItemResourceLog(String taskItemId, String taskItemResourceId, String resourceId, String operation, String output, boolean result) {
        TaskItemResourceLogWithBLOBs taskItemResourceLog = new TaskItemResourceLogWithBLOBs();
        String operator = "system";
        try {
            if (SessionUtils.getUser() != null) {
                operator = SessionUtils.getUser().getId();
            }
        } catch (Exception e) {
            //防止单元测试无session
        }
        taskItemResourceLog.setOperator(operator);
        taskItemResourceLog.setTaskItemId(taskItemId);
        taskItemResourceLog.setTaskItemResourceId(taskItemResourceId);
        taskItemResourceLog.setResourceId(resourceId);
        taskItemResourceLog.setCreateTime(System.currentTimeMillis());
        taskItemResourceLog.setOperation(operation);
        taskItemResourceLog.setOutput(output);
        taskItemResourceLog.setResult(result);
        taskItemResourceLogMapper.insertSelective(taskItemResourceLog);
    }

    private void updateTaskItemResource(TaskItemResource taskItemResource, String ruleId, String resourceId) throws Exception {
        taskItemResource.setResourceId(resourceId);
        taskItemResource.setRuleId(ruleId);
        taskItemResourceMapper.updateByPrimaryKeySelective(taskItemResource);

        saveTaskItemResourceLog(taskItemResource.getTaskItemId(), String.valueOf(taskItemResource.getId()), resourceId, "i18n_restart_task", "", true);
    }

    private List<RuleTagMapping> ruleTagMappings(String tagKey) {
        RuleTagMappingExample ruleTagMappingExample = new RuleTagMappingExample();
        ruleTagMappingExample.createCriteria().andTagKeyEqualTo(tagKey);
        List<RuleTagMapping> ruleTagMappings = ruleTagMappingMapper.selectByExample(ruleTagMappingExample);
        return ruleTagMappings;
    }

    private List<RuleGroupMapping> ruleGroupMappings(String groupId) {
        RuleGroupMappingExample ruleGroupMappingExample = new RuleGroupMappingExample();
        ruleGroupMappingExample.createCriteria().andGroupIdEqualTo(groupId);
        List<RuleGroupMapping> ruleGroupMappings = ruleGroupMappingMapper.selectByExample(ruleGroupMappingExample);
        return ruleGroupMappings;
    }

    public List<TaskLogVo> taskLogList(TaskRequest request) {
        List<TaskLogVo> list = extTaskMapper.taskLogList(request);
        return list;
    }

}
