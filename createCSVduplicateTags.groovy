/*
Create a CSV File for Duplicate Tags List in the Application.
This script can be used to generate a CSV output and store into filesystem . It lists down all the tags which are Duplicate and all the pages where they are being used. It will help to analyse the System Taxonomy.


/** @author Hashim Khan */
import org.apache.sling.api.resource.Resource
import com.day.cq.tagging.Tag
import org.apache.sling.api.resource.ResourceResolver

def filePath = "/opt/adobe/output.csv"
def tagLocation = "/etc/tags/geometrixx-media"

def buildQuery(tagLocation) {
    def queryManager = session.workspace.queryManager;
    def statement = "/jcr:root" + tagLocation + "//element(*, cq:Tag)"
    def query = queryManager.createQuery(statement, 'xpath')
}

def findDuplicateTags(tagLocation, tagNodeName) {
    def queryManager = session.workspace.queryManager;
    def statement = "/jcr:root" + tagLocation + "//element(*, cq:Tag) [fn:name() = '" + tagNodeName + "' ]"
    def query = queryManager.createQuery(statement, 'xpath')
}

def findPagesWithTag(tagId, tagPath) {
    def queryManager = session.workspace.queryManager;
    def statement = "//element(*, cq:Page)[(jcr:content/@cq:tags = '" + tagId + "' or jcr:content/@cq:tags = '" + tagPath + "' )]"
    def query = queryManager.createQuery(statement, 'xpath')
}

final def query = buildQuery(tagLocation);
final def result = query.execute()

def tagList = []

f = new File(filePath)

result.nodes.each {
    node - >
        String nodeTitle = node.name;
    tagList.add(nodeTitle);
}

def duplicates = tagList.findAll {
    tagList.count(it) > 1
}
def uniqueUsers = duplicates.unique(mutate = false)

print 'TAGTITLE ,TAGID , Pages , Count' + '\n'
f.append('TAGTITLE ,TAGID , Pages , Count' + '\n')
uniqueUsers.each {
    def tagquery = findDuplicateTags(tagLocation, it);
    def pathresult = tagquery.execute()
    pathresult.nodes.each {
        node - >
            Resource r = resourceResolver.getResource(node.path)
        Tag t1 = r.adaptTo(com.day.cq.tagging.Tag)
        print t1.getTitle() + ','
        f.append(t1.getTitle() + ',')
        def pagequery = findPagesWithTag(t1.getTagID(), node.path);
        def pageresult = pagequery.execute()
        print t1.getTagID() + ','
        f.append(t1.getTagID() + ',')
        count = 0;
        def totalResults = pageresult.getTotalSize()
        pageresult.nodes.each {
            pagenode - >
                if (count > 0) {
                    print ','
                    f.append(',')
                }
            print pagenode.path + ','
            f.append(pagenode.path + ',')

            if (count == 0) {
                print t1.getCount() + ','
                f.append(t1.getCount()) + ','
            }
            count++;

            if (totalResults != count) {
                print '\n'
                f.append('\n')
            }
            print ','
            f.append(',')
        }
        print '\n'
        f.append('\n')
    }
    print '\n'
    f.append('\n')
}
