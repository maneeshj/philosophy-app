package com.project.app;

import java.util.ArrayList;

public class PathToWiki {
	public String SourcePage;
	public ArrayList<String> Path;
	private int HopCount;
	public boolean IsInfiniteLoop;
	public boolean DidConnectionTimeout;
	public boolean HasInvalidPage;
	
	public PathToWiki(){
	}	
	public PathToWiki(String source, ArrayList<String> path){
		this.SourcePage = source;
		this.Path = path;
	}
	public void SetHopCount(){
		this.HopCount = this.Path.size() - 1;
	}
	public int GetHopCount(){
		return this.HopCount;
	}
}
