package com.project.app;

import java.util.ArrayList;

public class PathHelper {
	private static final String baseWikiUri = "https://en.wikipedia.org/wiki/";
	private static final String infiniteLoopMessage = "INFINITE LOOP";
	private static final String pagedoesNotExisttimeoutMessage = "Page doesn't exist or Connection timed out(please try again)";
	private static final String invalidPageMessage = "Page doesn't exist/ Page with no outgoing links";
	
	public static String GetPathString(PathToWiki pathToWiki){
		ArrayList<String> list = pathToWiki.Path;
		StringBuilder sb = new StringBuilder();
		
    	for(int i = 0; i < list.size(); i++){
    		String currentElement = list.get(i);
    		sb.append(" -> ").append("<a target=\"_blank\" href=").append("\"").append(baseWikiUri)
    		.append(currentElement).append("\"").append(">").append(currentElement).append("</a>\n");
    	}
    	
    	if(pathToWiki.ErrorCode == ErrorCode.InfiniteLoop){
    		sb.append(" -> ").append(infiniteLoopMessage);
    	} else if(pathToWiki.ErrorCode == ErrorCode.PageDoesNotExistOrConnectionTimeout){
    		sb.append(" -> ").append(pagedoesNotExisttimeoutMessage);
    	} else if(pathToWiki.ErrorCode == ErrorCode.HasInValidPage){
    		sb.append(" -> ").append(invalidPageMessage);
    	}
		return sb.toString();
	}
}
