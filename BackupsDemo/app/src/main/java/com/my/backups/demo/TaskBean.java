package com.my.backups.demo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulimin on 2017/3/13.
 */

public class TaskBean {
    private int taskId;
    private String taskName;
    private String taskType;
    private String taskProtocol;
    private String taskDetail;

    public String getTaskDeveloper() {
        return taskDeveloper;
    }

    public void setTaskDeveloper(String taskDeveloper) {
        this.taskDeveloper = taskDeveloper;
    }

    private String taskDeveloper;


    public TaskBean() {
    }

    public TaskBean(int taskId, String taskName, String taskType, String taskProtocol, String taskDetail, String taskDeveloper) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskType = taskType;
        this.taskProtocol = taskProtocol;
        this.taskDetail = taskDetail;
        this.taskDeveloper=taskDeveloper;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskProtocol() {
        return taskProtocol;
    }

    public void setTaskProtocol(String taskProtocol) {
        this.taskProtocol = taskProtocol;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(TASKID+":"+taskId+"\t");
        sb.append(TASKNAME+":"+taskName+"\t");
        sb.append(TASKTYPE+":"+taskType+"\t");
        sb.append(TASKPROTOCOL+":"+taskProtocol+"\t");
        sb.append(TASKDETAIL+":"+taskDetail+"\t");
        sb.append(TASKDEVELOPER+":"+taskDeveloper);
        return sb.toString();
    }

    public static String TASKID="taskId";
    public static String TASKNAME="taskName";
    public static String TASKTYPE="taskType";
    public static String TASKPROTOCOL="taskProtocol";
    public static String TASKDETAIL="taskDetail";
    public static String TASKDEVELOPER="taskDeveloper";


    public static List<TaskBean> getListFromJSONArray(String jsonArray){
        List<TaskBean> list_array=new ArrayList<TaskBean>();
        try{
            JSONArray array=new JSONArray(jsonArray);
            for(int i=0; i<array.length(); i++){
                JSONObject js1=(JSONObject) array.get(i);
                TaskBean taskBean=new TaskBean();
                taskBean.setTaskId(js1.getInt(TaskBean.TASKID));
                taskBean.setTaskName(js1.getString(TaskBean.TASKNAME));
                taskBean.setTaskType(js1.getString(TaskBean.TASKTYPE));
                taskBean.setTaskProtocol(js1.getString(TaskBean.TASKPROTOCOL));
                taskBean.setTaskDetail(js1.getString(TaskBean.TASKDETAIL));
                taskBean.setTaskDeveloper(js1.getString(TaskBean.TASKDEVELOPER));
                Log.e("hook_taskbena_"+i, taskBean.toString());
                list_array.add(taskBean);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list_array;
    }
}

