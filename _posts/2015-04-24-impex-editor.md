---
title: "Writing Hybris Impex with Style !"
layout: post
og_image_url: "http://eljoujat.github.io/images/impex/image004.jpg"
description: "Writing Hybris Impex with style: An eclipse plugin that will change the way you work with Impex."
disqus_comments: true
---


> Two things that drive us, software development addicts: enthusiasm and laziness...
ANONYMOUS


## The idea behind the current lab. 


Impex is a [hybris](http://hybris.com) specific language in top of SQL to import/export data the database.

The lack of tools for working with Impex, make this tasks a laborious one. In fact some available tools are:

- Web console offered by hybris:
	- Pros: Syntax highlighting, validation, execution.
	- Cons : requires a running instance of hybris, going out from eclipse, risking to loose all you work if the browser crashes.
- Excel or similar tools that could read and format CSV file:
	- Pros: Formatting that offers higher readability 
	- Cons: no syntax highlighting, no validation and execution.
- Eclipse or similar IDE:
	- Pros: you stay focus on your IDE.
	- Cons: no formatting, no syntax highlighting, no validation and execution.


The most used option is to use eclipse! Even if it's the poorest option, but developer tend to choose it to stay more focus.

## Eclipse Plugin : 

To boost my productivity and to be more focus,I decided to develop an Impex eclipse plugin.

The plugin should bring the hybris web console functionalities to eclipse.
The challenge was that all that I know about eclipse plugin development are some basic notions.

### Learn by example: 
 
I believe the best way to learn new things is to start with some theory, then jump to a practical example where the real wisdom is gained. 

- A good theorical Article is [Introduction To Eclipse Plugin Development](http://www.eclipsepluginsite.com/). 
- A good practical tutorial i found interesting is :  [Extending the Eclipse IDE - Plug-in development - Tutorial](http://www.vogella.com/tutorials/EclipsePlugIn/article.html)

I took as example the sample plugin project given by eclipse to create a xml editor .

### Components of the plugin :

#### Syntax highlighting: 

The Syntax highlighting feature uses the rule based scanner class ;  the scanner toos a givent number of rule, it cosume the impex file and evalute each token, if the token match a rule , the scanner exit with the correspondant properties. 

The ruleset are based on the Hybris [Impex syntax documentation](https://wiki.hybris.com/display/release5/ImpEx+Syntax)


Before : 
![Before](/images/impex/avant.png)


After : 

![After](/images/impex/after.png)


### Preferences Of the plugins : 

I also used the Preferences API to allow customisation, to give the more friendly user experience. 

It's possible to customize the feel and look of the Impex file:

![Preference Snapshot](/images/impex/perferences_1.png) 

And you can configure the connection with hybris, to execute and validate the impex with any running instance of hybris !

![Preference Snapshot](/images/impex/perferences_2.png) 



#### Detecting hyrbis Item and attributes : 

The first time eclipse run, the plugin connect to a the already configured running hybris instance (by default the localhost),call the Rest Webservice `allItems` , and `allAttributes` (exposed by hybris) and store the information to avoid call the web service again. 

To detect newly added Items or attributes, I Implementer an action to refresh the already stored data definition. 

#### A challenge and a new techniques acquired :  

The web services exposed by hybris require a registered hybris account and it's secured against [Cross-site request forgery](http://en.wikipedia.org/wiki/Cross-site_request_forgery).

To make a successful call the request should pass a crsf token, the token is associated with the connected account. the challenge was that token is stored on the HTML code of a response, and I had to use the [jsoup](http://jsoup.org/) library to retrieve the token. 

- Make a first to login, the call return with a JSESSIONID, I store the JSESSIONID for a further call. 
- Use jsoup with the stored JSESSIONID, and get the crsf token from the html.
- Make a Rest Call to retrieve the Items and attributes definition.

### The coolest feature : Autocompletion .
The auto-completion is the moste liked what the most user look for in any text editor, since i have stored the data deffinition, this feature was easy to implement as well : 

![Preference Snapshot](/images/impex/autosuggest.png) 


## Install the plugin : 

To install the plugin just copy [eclipseplugins](/labs/org.ucf.eclipseplugins.impexeditor_1.5.4.201505091329.jar) to the dropins folder under eclipse directory , and restart eclipse , and enjoy :) 



## What next: 

Other features i'm working on are : 

- Validate the impex with error markers.
- Execute the impex .
- Hyperlink features to easily locate where an Item is already valued from the same impex .
- Find usage Features, find all usage for the selected definied and selected item . 
- Formatting .

## Code Source Repos: 

[The code source repo is available here ](https://github.com/eljoujat/eclipseimpexeditor)




> “I believe that there is always an other way to do it, and i hope that you let me know .”