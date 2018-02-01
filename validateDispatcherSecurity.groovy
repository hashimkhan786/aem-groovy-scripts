/*

This script could be used to check the Dispatcher security by checking the the below paths as per 
Security Checklist https://helpx.adobe.com/experience-manager/dispatcher/using/dispatcher-configuration.html#TestingDispatcherSecurity

Make sure to Change the protocol, domain and one valid page as per your website. 

None of the curl response should give a 200 status. 

@author Hashim Khan */


/*Defines the protocol for th website */
def protocol="https://"
/*Defines the main domain URL for the website */
def domain="www.intel.com"
/*Defines a sample page in the application to check content grabbing.  */
def valid_page="/content/www/us/en/homepage"

/*Defines a list of security URLs which are used to verify Dispatcher Configurations. 
You can add more if you like. Current list is from https://helpx.adobe.com/experience-manager/dispatcher/using/dispatcher-configuration.html#TestingDispatcherSecurity  */
def list = [
    protocol+domain+"/admin",
    protocol+domain+"/system/console",
    protocol+domain+"/dav/crx.default",
    protocol+domain+"/crx",
    protocol+domain+"/bin/crxde/logs",
    protocol+domain+"/jcr:system/jcr:versionStorage.json",
    protocol+domain+"/_jcr_system/_jcr_versionStorage.json",
    protocol+domain+"/libs/wcm/core/content/siteadmin.html",
    protocol+domain+"/libs/collab/core/content/admin.html",
    protocol+domain+"/libs/cq/ui/content/dumplibs.html",
    protocol+domain+"/var/linkchecker.html",
    protocol+domain+"/etc/linkchecker.html",
    protocol+domain+"/home/users/a/admin/profile.json",
    protocol+domain+"/home/users/a/admin/profile.xml",
    protocol+domain+"/libs/cq/core/content/login.json",
    protocol+domain+"/content/../libs/foundation/components/text/text.jsp",
    protocol+domain+"/content/.{.}/libs/foundation/components/text/text.jsp",
    protocol+domain+"/apps/sling/config/org.apache.felix.webconsole.internal.servlet.OsgiManager.config/jcr%3acontent/jcr%3adata",
    protocol+domain+"/libs/foundation/components/primary/cq/workflow/components/participants/json.GET.servlet",
    protocol+domain+"/content.pages.json",
    protocol+domain+"/content.languages.json",
    protocol+domain+"/content.blueprint.json",
    protocol+domain+"/content.-1.json",
    protocol+domain+"/content.10.json",
    protocol+domain+"/content.infinity.json",
    protocol+domain+"/content.tidy.json",
    protocol+domain+"/content.tidy.-1.blubber.json",
    protocol+domain+"/content/dam.tidy.-100.json",
    protocol+domain+"/content/content/geometrixx.sitemap.txt",
    protocol+domain+valid_page+".query.json?statement=//*",
    protocol+domain+valid_page+".qu%65ry.js%6Fn?statement=//*",
    protocol+domain+valid_page+".query.json?statement=//*[@transportPassword]/(@transportPassword%20|%20@transportUri%20|%20@transportUser)",
    protocol+domain+valid_page+"/_jcr_content.json",
    protocol+domain+valid_page+"/_jcr_content.feed",
    protocol+domain+valid_page+"/jcr:content.feed",
    protocol+domain+valid_page+"._jcr_content.feed",
    protocol+domain+valid_page+".jcr:content.feed",
    protocol+domain+valid_page+".docview.xml",
    protocol+domain+valid_page+".docview.json",
    protocol+domain+valid_page+".sysview.xml",
    protocol+domain+"/etc.xml",
    protocol+domain+"/content.feed.xml",
    protocol+domain+"/content.rss.xml",
    protocol+domain+"/content.feed.html",
    protocol+domain+valid_page+".html?debug=layout"
]

list.each {
    checkUrlGetStatus(it)
    
}
checkUserGeneratedPath(protocol,domain)
checkDispatcherInvalidation(protocol,domain)

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
/*Method to make a POST call for the user generated path. */
def checkUserGeneratedPath(String protocol,String domain) {
    print "POST:"+protocol+domain+"/content/usergenerated/mytestnode  ,"
    def process ="curl -X POST -s -o /dev/null -w %{http_code} -u anonymous:anonymous ${protocol}${domain}/content/usergenerated/mytestnode".execute().text
    printf("%2s", "STATUS:") 
    process.each { text->
        print "${text}"
     }
     println ""
     
}
/*Method to make a Dispatcher invalidation call.  */
def checkDispatcherInvalidation(String protocol,String domain) {
    print "Dispatcher Invalidation: "+protocol+domain+"/dispatcher/invalidate.cache  ,"
    def process ="curl -s -o /dev/null -w %{http_code} -H \'CQ-Handle:/content\' -H \'CQ-Path:/content\' ${protocol}${domain}/dispatcher/invalidate.cache".execute().text
    printf("%2s", "STATUS:") 
    process.each { text->
        print "${text}"
    }
}

/* Curl Command Syntax to Parse the XML output. 
def proc = "curl -u admin:admin http://localhost:4502/content/geometrixx/en.html".execute().text

def crx = new XmlSlurper().parseText(proc);
def packages = crx.response.data.packages
def status = crx.response.status;
println "status:"+status;
packages.package.each { pack->
    println "${pack.name}: ${pack.size}"
}
*/
