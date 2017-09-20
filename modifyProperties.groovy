/*
Modify a property in a complete site hierarchy as per business logic.
There was a real time problem in one of my project where we have to fill in jcr:title in the Page-title whenever the Page title was a null. Moreover we were having multiple languages sites and have to browse through all of them at once. We used groovy to solve this problem for multiple development environments. Similar to the below example where I am modifying a particular node and its property for the complete hierarchy using Groovy. I have used example for a Geometrixx site (AEM 6.0) so that you can may the results for yourself.
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
final def query = buildQuery(page, 'collab/calendar/components/event');
final def result = query.execute()

count = 0;
result.nodes.each {
    node - >
        String nodePath = node.path;

    if (nodePath.contains('event') && !nodePath.contains('jcr:versionStorage')) {
        /*The below iterator is used to fetch the child pages of the parent node */
        node.findAll {
            it.hasNodes()
        }.each {
            if (it.name.contains("event")) {
                count++;
                println 'Title--' + it.get('jcr:title');
                println 'Node Path--' + it.path;
                it.set('jcr:title', 'Hashim');
                println 'Title--' + it.get('jcr:title');
                session.save()
            }
        }
    }
}
println 'Number Of Component Found :' + result.nodes.size();
println 'Number of Component Modified:' + count;
