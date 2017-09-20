/*
SQL QUERY with Groovy Script.
@author Hashim Khan */
/*This method is used to Query the JCR and find results as per the Query.*/
def buildQuery(page, term) {
    def queryManager = session.workspace.queryManager;
    def statement = 'select * from nt:base where jcr:path like \'' + page.path + '/%\' and sling:resourceType = \'' + term + '\'';
    /*Here term is the sling:resourceType property value*/
    queryManager.createQuery(statement, 'sql');
}

/*Defined Content Hierarchy */
final def page = getPage('/content/geometrixx/en/')
/*Template which is searched in the content hierarchy */
final def query = buildQuery(page, 'geometrixx/components/contentpage');
final def result = query.execute()

println 'No Of pages found = ' + result.nodes.size();

result.nodes.each {
    node - >
        println 'nodePath::' + node.path
}
