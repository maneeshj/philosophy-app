“Getting to Philosophy”
==============

The way this application works is that takes a Wikipedia URL as input, and display the path taken from clicking the first link of each page until it reached the [Philosophy page](https://en.wikipedia.org/wiki/Philosophy)
For more details refer - https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy

This application is built using a [Vaadin UI framework](https://vaadin.com/home) and uses MYSQL to store the transaction data.
It also uses Redis for caching data.

Instructions on how to run this application
========
1) Download the source code using `git clone git@github.com:maneeshj/philosophy-app.git`<br>
2) Install **JDK, Eclipse** and open eclipse with the downloaded project in the specified workspace<br>
3) In eclipse, go to *Help -> Eclipse MarketPlace*. Search for Vaadin plugin. Click Install.<br>
4) Make sure **JDK** is used as the run time evironment rather than JRE. Go to *Window -> Preference -> Java -> Installed JREs*. Locate your JDK, add it and check it and uncheck/remove if there exists a jre.<br>
5) Install **Redis**. For windows, you can locate it here - https://github.com/rgl/redis/downloads<br>
6) Set up **Jetty** as your web server. *Run -> Run Configurations -> Maven Build(on left) -> Right click and hit new*<br>
* Specifiy a new **Name** on the top(for eg: Run on jetty) 
* For **Base directory**, click Browse Workspace and select the project
* For **Goals**, type `jetty:run`
* If you want to facilitate debugging for break points, go to **Source** tab and click **Add..** and select **Java Project** and  select the project. Hit **Apply** and **Run** the project
 
