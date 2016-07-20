package com.gson;

public class TaskList {
	private String listName;  
    private String taskList;
    
    public String getListName() {  
        return listName;  
    }  
  
    public void setListName(String listName) {  
        this.listName = listName;  
    }  

    public String getTaskList() {  
        return taskList;  
    }  
  
    public void setTaskList(String taskList) {  
        this.taskList = taskList;  
    } 
    
    @Override
	public String toString() {
		return  listName
				+ ": [taskList=" + taskList 				
				+ "]";
	}
}
