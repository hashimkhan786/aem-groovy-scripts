/*
This script could be used to create Cache by reading Sitemap and making a GET call for all the paths present in it. 
Make sure to Change the protocol and domain as per your website. 
Note that none of the request should give a 404. 
@author Hashim Khan */


/*Defines the protocol for the website */
def protocol="https://"
/*Defines the main domain URL for the website */
def domain="www.lifetime.life"

readSitemapXML(protocol,domain)

/*Method to make a GET call for the above list. */
def checkUrlGetStatus(String path) {
    print path+" , "
    def process ="curl -s -o /dev/null -w %{http_code} ${path} ".execute().text
   
    printf("%2s", "STATUS:") 
    process.each { text->
        print "${text}"
    }
    println ""
}


/*Method to make a GET call at Sitemap and read the XML output recursively. */
/* Curl Command Syntax to Parse the XML output. */
def readSitemapXML( String protocol,String domain) {
    
    def proc = "curl -X GET ${protocol}${domain}/sitemap.xml".execute().text
    def output = new XmlSlurper().parseText(proc);
    def urls = output.url.loc
    urls.each { path->
       def url = "${path}"
       checkUrlGetStatus(url)
       
    }
}

