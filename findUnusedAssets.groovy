/*
Find all the Assets which are not referenced in the content and could be removed. 
@author Hashim Khan */



def predicates = [path: "/content/dam/geometrixx", type: "dam:Asset",  "orderby.index": "true", "orderby.sort": "desc"]
def query = createQuery(predicates)
query.hitsPerPage = 500
def result = query.result
println "${result.totalMatches} hits, execution time = ${result.executionTime}s\n--"

result.hits.each { hit ->
    def path=hit.node.path
    Resource res = resourceResolver.getResource(path)
    if(res!=null){
        getAllReferences(path);
    }
}

def getAllReferences(assetpath) {
    def queryManager = session.workspace.queryManager
    def statement = "/jcr:root" + "/content/geometrixx//*" + "[jcr:contains(., '"+assetpath+"')]"
    def query = queryManager.createQuery(statement, "xpath")
    def result = query.execute()
    def rows = result.getRows().size
    if(rows==0){
        println "Unused Asset: "+assetpath
    }
  
}