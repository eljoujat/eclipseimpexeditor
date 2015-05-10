

# Syntax highlighting: 

The Syntax highlighting feature uses the rule based scanner class ;  the scanner toos a givent number of rule, it cosume the impex file and evalute each token, if the token match a rule , the scanner exit with the correspondant properties. 

The ruleset are based on the Hybris [Impex syntax documentation](https://wiki.hybris.com/display/release5/ImpEx+Syntax)


Before : 
![Before](/images/impex/avant.png)


After : 

![After](/images/impex/after.png)


# Preferences Of the plugins : 

I also used the Preferences API to allow customisation, to give the more friendly user experience. 

It's possible to customize the feel and look of the Impex file:

![Preference Snapshot](/images/impex/perferences_1.png) 

And you can configure the connection with hybris, to execute and validate the impex with any running instance of hybris !

![Preference Snapshot](/images/impex/perferences_2.png) 



# Detecting hyrbis Item and attributes : 

The first time eclipse run, the plugin connect to a the already configured running hybris instance (by default the localhost),call the Rest Webservice `allItems` , and `allAttributes` (exposed by hybris) and store the information to avoid call the web service again. 

To detect newly added Items or attributes, I Implementer an action to refresh the already stored data definition. 

# A challenge and a new techniques acquired :  

The web services exposed by hybris require a registered hybris account and it's secured against [Cross-site request forgery](http://en.wikipedia.org/wiki/Cross-site_request_forgery).

To make a successful call the request should pass a crsf token, the token is associated with the connected account. the challenge was that token is stored on the HTML code of a response, and I had to use the [jsoup](http://jsoup.org/) library to retrieve the token. 

- Make a first to login, the call return with a JSESSIONID, I store the JSESSIONID for a further call. 
- Use jsoup with the stored JSESSIONID, and get the crsf token from the html.
- Make a Rest Call to retrieve the Items and attributes definition.

# The coolest feature : Autocompletion .
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



> “I believe that there is always an other way to do it, and i hope that you let me know .”