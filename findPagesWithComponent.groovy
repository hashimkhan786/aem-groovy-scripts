/*
Find all the pages wherein a particular component is being used.
Sometimes there will be a scenario where you have to find (and modify/delete) a particular component from the complete site structure in multiple environments. There is no better solution for that kind of problem , other than Groovy Script.
@author Hashim Khan */
/*This method is used to Query the JCR and find results as per the Query.*/
def buildQuery(page, term) {
    def queryManager = session.workspace.queryManager;
    def statement = 'select * from nt:base where jcr:path like \'' + page.path + '/%\' and sling:resourceType = \'' + term + '\'';
    queryManager.createQuery(statement, 'sql');
}

/*Defined Content Hierarchy */
final def page = getPage('/content/geometrixx/en/')
/*Component ResourceType which is searched in the content hierarchy */
final def query = buildQuery(page, 'foundation/components/text');
final def result = query.execute()

count = 0;
result.nodes.each {
    node - >
        String nodePath = node.path;
    println nodePath
}
println 'No Of Pages found :' + result.nodes.size();
