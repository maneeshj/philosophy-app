package com.project.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.project.cache.JedisCache;
import com.project.database.DatabaseService;

public class WebCrawler {
	private static final String philosophyUrl = "https://en.wikipedia.org/wiki/Philosophy";
	private static final String titleClass = "firstHeading";
	private static final String preParagraphDivId = "mw-content-text";
	private static final String bracketsRegex = "\\(([^()]*|\\([^()]*\\))*\\)";
	private static final String hrefTag = "a[href]";
	private static final String redLinkTag = "redlink=1";
	private static final String baseWikiUri = "https://en.wikipedia.org";
	private static final String philosophyTitle = "Philosophy";
	
	private JedisCache jedisCache = new JedisCache();	
	
	public PathToWiki GetPathToPhilosophy(String url){
    	Document doc = null;
    	DatabaseService dbService = new DatabaseService();
    	List<String> visitedTitles = new ArrayList<String>();
    	PathToWiki pathToWiki = new PathToWiki(url, new ArrayList<String>());

    	String currentTitle = "";
    	String currentUrl = url;

    	while(!currentUrl.equals(philosophyUrl)){
    		// Connect to the URL and get the HTML document
    		doc = GetDocumentToParse(currentUrl);
    		if(doc == null){
    			pathToWiki.DidConnectionTimeout = true;
    			break;    			
    		} 		
    		// Get main page title and add it to the path
    		currentTitle = doc.getElementsByClass(titleClass).text();
    		pathToWiki.Path.add(currentTitle);
    		
    		// Check REDIS cache to see if we already have path from currentTitle to Philosophy
    		if(jedisCache.ContainsKey(currentTitle)){
    			System.out.println("Cache hit for: "+currentTitle);
    			Set<String> remainingPathSet = jedisCache.GetCachedValues(currentTitle);
    			pathToWiki.Path.addAll(remainingPathSet);
    			CacheAllPaths(pathToWiki);
    			pathToWiki.SetHopCount();
    			// Insert transaction to database
    			dbService.InsertPath(pathToWiki);
    			return pathToWiki;
    		}
    		
    		// Check if this title was already visited in any of the previous iterations. If YES, then it will result in an INFINITE loop
    		if(visitedTitles.contains(currentTitle)){
    			// loop -> return the string so far
    			pathToWiki.IsInfiniteLoop = true;
    			break;
    		}    		
    		visitedTitles.add(currentTitle);
    		
    		// Get the the paragraphs right after "mw-content-text" div tag
    		List<Element> paras = doc.getElementById(preParagraphDivId).select(">p");
    		Element trueLink = null;
    		// Loop through each paragraph to find the first valid link    		
    		for(Element body : paras){            
	            // Maintain two versions of body
	            // 1. Body without brackets(body1)   2. Body with brackets (body)
	            String bodyNobracketText = body.toString().replaceAll(bracketsRegex, "");
	            Element bodyNobracket = Jsoup.parse(bodyNobracketText);
	            
	            // Get first link from body without brackets
	            List<Element> linksNoBracket = bodyNobracket.select(hrefTag);
	            Element linkNoBracket = null;
	            // Ignore citation links
	            for(int i = 0; i < linksNoBracket.size(); i++){
	            	// Exclude external citation links, red links and links to current page
	            	String linkAttribute = linksNoBracket.get(i).attr("href");
	            	if(!linkAttribute.contains("#") && !linkAttribute.contains(redLinkTag)
	            			&& !linkAttribute.contains(currentTitle)){
	            		linkNoBracket = linksNoBracket.get(i);
	            		break;
	            	}
	            }
	            // If there are no valid links in the current para, move on the next one
	            if(linkNoBracket == null){
	            	continue;
	            }
	            	            
	            // Get all links from body with brackets
	            List<Element> linksWithBracket = body.select(hrefTag);
	            
	            // Iteratively compare both links to identify the correct one
	            for(int i = 0; i < linksWithBracket.size(); i++){
	            	Element linkWithBracket = linksWithBracket.get(i);
	            	String linkAttribute = linkWithBracket.attr("href");
	            	// Compare both links
	            	if(linkNoBracket.attr("href").length() != linkAttribute.length()
	            			&& linkAttribute.contains(linkNoBracket.attr("href"))
	            			&& !linkAttribute.contains("#") // external, citation links
	            			&& !linkAttribute.contains(redLinkTag) // red links
		            		&& !linkAttribute.contains(currentTitle)){  // links to current page
	            		trueLink = linkWithBracket;
	            		break;
	            	} else if(linkNoBracket.attr("href").equals(linkWithBracket.attr("href"))){
	            		trueLink = linkNoBracket;
	            		break;
	            	} else{
	            		continue;
	            	}
	            }
	            if(trueLink != null){
	            	break;
	            }
    		}
    		
    		// Page with no outgoing links or page doesn't exist
    		if(trueLink == null){
    			pathToWiki.HasInvalidPage = true;
    			break;
    		}
    		
            trueLink.setBaseUri(baseWikiUri);            
            String absHref = trueLink.attr("abs:href"); // "http://jsoup.org/"

            System.out.println(absHref);
            currentUrl = absHref;
    	}
    	// Add philosophy as the last path which was not added, since parsing was not required above
    	if(!(pathToWiki.IsInfiniteLoop || pathToWiki.DidConnectionTimeout || pathToWiki.HasInvalidPage)){
    		pathToWiki.Path.add(philosophyTitle);
        	pathToWiki.SetHopCount();
    	}   	
    	// Cache all paths (final path + intermediate paths)
    	CacheAllPaths(pathToWiki);
    	// Insert transaction to database
    	dbService.InsertPath(pathToWiki);
    	return pathToWiki;
	}
	
	private Document GetDocumentToParse(String url){
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// Page doesn't exist or connection timed out
			System.out.println(e.getMessage());    			
		}
		return doc;
	}
		
	private void CacheAllPaths(PathToWiki pathToWiki){
		ArrayList<String> path = pathToWiki.Path; 
		for(int i = 0; i < path.size() - 1; i++){
			String key = path.get(i);
			// If cache already has the key, don't add
			if(jedisCache.ContainsKey(key)){
				continue;
			}
			ArrayList<String> values = new ArrayList<String>();
			for(int j = i+1; j< path.size(); j++){
				values.add(path.get(j));
			}
			jedisCache.SetCache(key, values);
		}
	}
	
}
