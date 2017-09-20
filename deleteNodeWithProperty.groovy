/*
Delete all the nodes of a particular type with a specific property.
Deletion of a particular node is quite handy when you have to similar use case and want to modify the content quickly and easily.
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
final def query = buildQuery(page, 'foundation/components/flash');
final def result = query.execute()

count = 0;
result.nodes.each {
    node - >
        String nodePath = node.path;

    if (nodePath.contains('flash') && !nodePath.contains('jcr:versionStorage')) {
        count++;
        println 'deleting--' + nodePath;
        node.remove();
        /* Save this session if you are sure the correct nodes are being deleted. Once the session is saved the nodes couldn't be retrieved back.
         *session.save();*/
    }
}
println 'No Of component found :' + result.nodes.size();
println 'Number of Component Deleted: ' + count;
