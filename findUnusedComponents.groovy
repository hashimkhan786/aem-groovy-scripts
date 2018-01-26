/*
Find all the Components which are not used in the content and could be removed. 
@author Hashim Khan */


def predicates = [path: "/apps/geometrixx/components", type: "cq:Component",  "orderby.index": "true", "orderby.sort": "desc"]
def query = createQuery(predicates)
query.hitsPerPage = 1000
def result = query.result
println "${result.totalMatches} hits, execution time = ${result.executionTime}s\n--"

result.hits.each { hit ->
    def path=hit.node.path
    Resource res = resourceResolver.getResource(path)
    if(res!=null){
        path = path.substring(6,path.length())
        getAllReferences(path);
    }
}

def getAllReferences(assetpath) {
    def queryManager = session.workspace.queryManager
    def statement = "/jcr:root" + "/content/geometrixx//*" + "[@sling:resourceType='"+ assetpath+"']" 
    def query = queryManager.createQuery(statement, "xpath")
    def result = query.execute()
    def rows = result.getRows().size
    if(rows==0){
        println "Asset="+assetpath+"; Results="+rows
        println "********Unused Component*******: "
    }
  
}