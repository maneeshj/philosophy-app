Getting to Philosophy
==============

The application takes a Wikipedia URL or a title as input, and displays the path taken to reach the [Philosophy page](https://en.wikipedia.org/wiki/Philosophy) by clicking the first link of each page with few rules. <br>
For more details refer-https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy

This application is built using a [Vaadin UI framework](https://vaadin.com/home) and uses MYSQL to store the transaction data.
It also uses Redis for caching data.

Instructions on how to run this application
========
* Download the source code using `git clone git@github.com:maneeshj/philosophy-app.git`<br>
* Install **JDK, Eclipse** and open eclipse with the downloaded project in the specified workspace<br>
* In eclipse, go to *Help -> Eclipse MarketPlace*. Search for Vaadin plugin. Click Install.<br>
* Make sure **JDK** is used as the run time evironment rather than JRE. Go to *Window -> Preferences -> Java -> Installed JREs*. Locate your JDK, add it and check it and uncheck/remove if there exists a jre already.<br>
* Install **Redis**. For windows, you can locate it here - https://github.com/rgl/redis/downloads<br>
* Install MySQL server and workbench and run the queries mentioned in the [Queries.sql](https://github.com/maneeshj/philosophy-app/blob/master/src/main/java/com/project/database/Queries.sql) file located in com.project.database package. <br>
* Set up **Jetty** as your web server. *Run -> Run Configurations -> Maven Build(on left) -> Right click and hit new*<br>
 * Specifiy a new **Name** on the top(for eg: Run on jetty) 
 * For **Base directory**, click Browse Workspace and select the project
 * For **Goals**, type `jetty:run`
 * If you want to facilitate debugging for break points, go to **Source** tab and click **Add..** and select **Java Project** and  select the project. Hit **Apply** and **Run** the project <br>
* Now enter http://localhost:8080/ in the browser (8080 is the default port used) and enjoy figuring out your path towards philosophy.
 
Implementation details
==============
The main entry point is MyUI.java in *com.project.ui package*. This UI layer communicates with an application layer which is located in *com.project.app package*. This package has files related to the main web crawling solution. [**JSoup**](https://jsoup.org/) was used for parsing the HTML of each wikipedia page. <br>

The application layer communicates with another layer called caching layer located in *com.project.cache* package. Redis is used for caching. The Java client used here is [Jedis](https://github.com/xetorthio/jedis). Whenever a URL is entered, the cache stores the complete path to Philosophy in the REDIS memory cache as well as all subsequent paths for each intermediate wikipedia page. For example, if the path is "Knowledge" -> "Awareness" -> "Consciousness" -> "Quality (philosophy)" -> "Philosophy". Then, paths for each page - Knowledge("Knowledge" -> "Awareness" -> "Consciousness" -> "Quality (philosophy)" -> "Philosophy") , Awareness("Awareness" -> "Consciousness" -> "Quality (philosophy)" -> "Philosophy"), Consicousness("Consciousness" -> "Quality (philosophy)" -> "Philosophy") etc is stored in the cache.<br>

The motivation behind using a key-value data store such as Redis is because this application is READ - heavy and the value stored here could simply be a [Java Set](https://docs.oracle.com/javase/7/docs/api/java/util/Set.html). All subsequent paths are stored as well to facilitate caching and making the application even more fast and efficient. <br>

The application layer also communicates with the data layer which is located in *com.project.database*  package. This layer inserts each transaction to a MYSQL database. The database here can be used to track all the transactions by a user and see the paths stored, the number of hops, see if an error occured and type of error.

