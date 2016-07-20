package com.gson;

import java.util.List;

public class Root
{
    private List<String> loc_id;

    public void setLoc_id(List<String> loc_id){
        this.loc_id = loc_id;
    }
    public List<String> getLoc_id(){
        return this.loc_id;
    }
    
	@Override
	public String toString() {
		String temp = "";
		for(int i=0;i<loc_id.size();i++)
		{
			temp = temp + loc_id.get(i) + ",";
		}
		return "\"loc_id\": [" + temp
				+ "]";
	}
}
