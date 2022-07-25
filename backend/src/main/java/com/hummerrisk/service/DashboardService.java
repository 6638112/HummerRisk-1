package com.hummerrisk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hummerrisk.base.domain.*;
import com.hummerrisk.base.mapper.CloudTaskMapper;
import com.hummerrisk.base.mapper.ImageResultMapper;
import com.hummerrisk.base.mapper.PackageResultMapper;
import com.hummerrisk.base.mapper.ServerResultMapper;
import com.hummerrisk.base.mapper.ext.ExtDashboardMapper;
import com.hummerrisk.base.mapper.ext.ExtVulnMapper;
import com.hummerrisk.commons.constants.TaskConstants;
import com.hummerrisk.commons.constants.TaskEnum;
import com.hummerrisk.commons.utils.ChartData;
import com.hummerrisk.commons.utils.DashboardTarget;
import com.hummerrisk.commons.utils.PlatformUtils;
import com.hummerrisk.controller.request.dashboard.TaskCalendarVo;
import com.hummerrisk.dto.HistoryScanDTO;
import com.hummerrisk.dto.ImageChartDTO;
import com.hummerrisk.dto.PackageChartDTO;
import com.hummerrisk.dto.TopInfoDTO;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseArray;

@Service
public class DashboardService {

    @Resource
    private ExtVulnMapper extVulnMapper;
    @Resource
    private ExtDashboardMapper extDashboardMapper;
    @Resource
    private CloudTaskMapper cloudTaskMapper;
    @Resource
    private ServerResultMapper serverResultMapper;
    @Resource
    private PackageResultMapper packageResultMapper;
    @Resource
    private ImageResultMapper imageResultMapper;
    @Resource
    private HistoryService historyService;

    public List<ChartData> vulnDistribution(Map<String, Object> params) {

        final String group = MapUtils.getString(params, "group");
        switch (group) {
            case "overall":
                return extVulnMapper.overall(params);
            case "ruleGroup":
                return extVulnMapper.ruleGroup(params);
            case "report":
                return extVulnMapper.report(params);
            case "ruleList":
                return extVulnMapper.ruleList(params);
            case "accountList":
                return extVulnMapper.accountList(params);
            case "vulnList":
                return extVulnMapper.vulnList(params);
            case "regionsList":
                return extVulnMapper.regionsList(params);
            default:
                return new LinkedList<>();
        }
    }

    public List<Map<String, Object>> severityList(Map<String, Object> params) {
        return extVulnMapper.severityList(params);
    }

    public List<Map<String, Object>> totalPolicy(Map<String, Object> params) {
        return extVulnMapper.totalPolicy(params);
    }

    public List<DashboardTarget> target(Map<String, Object> params) {
        return extVulnMapper.target(params);
    }

    public List<HistoryScanDTO> history(Map<String, Object> params) {
        List<HistoryScanDTO> historyList = extVulnMapper.history(params);
        for (HistoryScanDTO scanHistory : historyList) {
            scanHistory.setOutput(toJSONString2(scanHistory.getOutput()!=null?scanHistory.getOutput():"[]"));
        }
        return historyList;
    }

    public List<HistoryScanDTO> vulnHistory(Map<String, Object> params) {
        List<HistoryScanDTO> historyList = extVulnMapper.vulnHistory(params);
        for (HistoryScanDTO scanHistory : historyList) {
            scanHistory.setOutput(toJSONString2(scanHistory.getOutput()!=null?scanHistory.getOutput():"[]"));
        }
        return historyList;
    }

    public String toJSONString2(String jsonString) {
        String res = JSON.parse(jsonString).toString();
        JSONArray jsonArray = parseArray(res);
        return JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    public TopInfoDTO topInfo(Map<String, Object> params) {
        return extDashboardMapper.topInfo(params);
    }

    public PackageChartDTO packageChart(Map<String, Object> params) {
        PackageChartDTO packageChartDTO = new PackageChartDTO();
        List<String> xAxis = extDashboardMapper.packageChartX(params);
        List<Integer> yAxis = extDashboardMapper.packageChartY(params);
        packageChartDTO.setxAxis(xAxis);
        packageChartDTO.setyAxis(yAxis);
        return packageChartDTO;
    }

    public ImageChartDTO imageChart(Map<String, Object> params) {
        ImageChartDTO imageChartDTO = new ImageChartDTO();
        List<String> xAxis = extDashboardMapper.imageChartX(params);
        List<Integer> yAxis = extDashboardMapper.imageChartY(params);
        imageChartDTO.setxAxis(xAxis);
        imageChartDTO.setyAxis(yAxis);
        return imageChartDTO;
    }

    public List<TaskCalendarVo> taskCalendar() {
        return extDashboardMapper.taskCalendar();
    }

    public Integer score() {
        int score = 100, sum = 0, count = 0;//计数器

        CloudTaskExample cloudTaskExample = new CloudTaskExample();
        cloudTaskExample.createCriteria().andStatusEqualTo(TaskConstants.TASK_STATUS.FINISHED.toString());
        List<CloudTask> cloudTasks = cloudTaskMapper.selectByExample(cloudTaskExample);
        for(CloudTask cloudTask : cloudTasks) {
            if (PlatformUtils.isSupportVuln(cloudTask.getPluginId())) {
                sum = sum + historyService.calculateScore(cloudTask.getId(), cloudTask, TaskEnum.vulnAccount.getType());
            } else {
                sum = sum + historyService.calculateScore(cloudTask.getId(), cloudTask, TaskEnum.cloudAccount.getType());
            }
        }

        ServerResultExample serverResultExample = new ServerResultExample();
        serverResultExample.createCriteria().andResultStatusEqualTo(TaskConstants.TASK_STATUS.FINISHED.toString());
        List<ServerResult> serverResults = serverResultMapper.selectByExample(serverResultExample);
        for(ServerResult serverResult : serverResults) {
            sum = sum + historyService.calculateScore(serverResult.getId(), serverResult, TaskEnum.serverAccount.getType());
        }

        ImageResultExample imageResultExample = new ImageResultExample();
        imageResultExample.createCriteria().andResultStatusEqualTo(TaskConstants.TASK_STATUS.FINISHED.toString());
        List<ImageResult> imageResults = imageResultMapper.selectByExample(imageResultExample);
        for(ImageResult imageResult : imageResults) {
            sum = sum + historyService.calculateScore(imageResult.getId(), imageResult, TaskEnum.imageAccount.getType());
        }

        PackageResultExample packageResultExample = new PackageResultExample();
        packageResultExample.createCriteria().andResultStatusEqualTo(TaskConstants.TASK_STATUS.FINISHED.toString());
        List<PackageResult> packageResults = packageResultMapper.selectByExample(packageResultExample);
        for(PackageResult packageResult : packageResults) {
            sum = sum + historyService.calculateScore(packageResult.getId(), packageResult, TaskEnum.packageAccount.getType());
        }

        count = cloudTasks.size() + serverResults.size() + imageResults.size() + packageResults.size();

        if(count != 0) score = Math.round(sum / count);

        return score;
    }


}

