/*
XPATH QUERY with Groovy Script.
@author Hashim Khan */
/*This method is used to Query the JCR and find results as per the Query.*/
def buildQuery(page, term) {
    def queryManager = session.workspace.queryManager;
    def statement = "/jcr:root${page.path}//element(*, cq:Page)[jcr:content/@cq:template = '" + term + "']"
    /*Here term is the cq:template value*/
    def query = queryManager.createQuery(statement, 'xpath')
}

/*Defined Content Hierarchy */
final def page = getPage('/content/geometrixx/en/')
/*Component ResourceType which is searched in the content hierarchy */
final def query = buildQuery(page, '/apps/geometrixx/templates/contentpage');
final def result = query.execute()

count = 0;
result.nodes.each {
    node - >
        String nodePath = node.path;
    println nodePath
}
println 'No Of component found :' + result.nodes.size();
result.nodes.each {
    node - >
        println 'nodePath::' + node.path
}
