package com.gson;

public class Task {

	private String ref_id;  
    private String user_id;  
    private String task_time;
      
    private String task_name; 
    private String task_event;   
    
    private String doc_id;
    private String task_id;
    
    private String loc_id;
    private String box_id;
    private String sku;
    private String qty;
    private String last_opt_id;
    //private int pushstate;
//    +"ref_id integer primary key," 
//    +"user_id text not null,"
//    +"task_time timestamp not null default (datetime('now','localtime')),"
//    +"task_name text not null,"
//    +"task_event text,"
//    +"doc_id integer,"
//    +"task_id integer,"
//    +"loc_id text,"
//    +"box_id text,"
//    +"sku text,"
//    +"qty integer,"
//    +"last_opt_id integer,"
//    +"pushstate integer not null"
  
    public String getRefId() {  
        return ref_id;  
    }  
  
    public void setRefId(String ref_id) {  
        this.ref_id = ref_id;  
    }  
  
    public String getUserId() {  
        return user_id;  
    }  
  
    public void setUserId(String user_id) {  
        this.user_id = user_id;  
    }  
  
    public String getTaskTime() {  
        return task_time;  
    }  
  
    public void setTaskTime(String task_time) {  
        this.task_time = task_time;  
    }  
        
    public String getTaskName() {  
        return task_name;  
    }  
  
    public void setTaskName(String task_name) {  
        this.task_name = task_name;  
    }  
    
    public String getTaskEvent() {  
        return task_event;  
    }  
  
    public void setTaskEvent(String task_event) {  
        this.task_event = task_event;  
    }  

    public String getDocId() {  
        return doc_id;  
    }  
  
    public void setDocId(String doc_id) {  
        this.doc_id = doc_id;  
    } 
    
    public String getTaskId() {  
        return task_id;  
    }  
  
    public void setTaskId(String task_id) {  
        this.task_id = task_id;  
    } 
    
    public String getLocId() {  
        return loc_id;  
    }  
  
    public void setLocId(String loc_id) {  
        this.loc_id = loc_id;  
    } 
    
    public String getBoxId() {  
        return box_id;  
    }  
  
    public void setBoxId(String box_id) {  
        this.box_id = box_id;  
    } 
    
    public String getSku() {  
        return sku;  
    }  
    
    public String getQty()
    {
    	return qty;
    }
    
    public void setQty(String qty)
    {
    	this.qty = qty;
    }
  
    public void setSku(String sku) {  
        this.sku = sku;  
    } 
    
    public String getLastOptId()
    {
    	return last_opt_id;
    }
    
    public void setLastOptId(String last_opt_id)
    {
    	this.last_opt_id = last_opt_id;
    }
    
//    public int getPushState()
//    {
//    	return pushstate;
//    }
//    
//    public void setPushState(int pushstate)
//    {
//    	this.pushstate = pushstate;
//    }   
    
	@Override
	public String toString() {
		return "Task: [ref_id=" + ref_id 
				+ ", user_id=" + user_id 
				+ ", task_time=" + task_time
				+ ", task_name=" + task_name
				+ ", task_event=" + task_event
				+ ", doc_id=" + doc_id
				+ ", task_id=" + task_id
				+ ", loc_id=" + loc_id
				+ ", box_id=" + box_id
				+ ", sku=" + sku
				+ ", qty=" + qty
				+ ", last_opt_id=" + last_opt_id
				//+ ", pushstate=" + pushstate
				+ "]";
	}
}
