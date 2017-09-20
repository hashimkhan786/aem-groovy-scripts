/*
Merge Duplicate Tags in an Application
This was a requirement in one of my client who asked us to merge the Duplicate Tags .  This way you can list out all the duplicate tags and merge all of them into the first Master Tag. All the related references in the Pages will automatically be changed as per the API.

 /** @author Hashim Khan */
import org.apache.sling.api.resource.Resource
import com.day.cq.tagging.Tag
import org.apache.sling.api.resource.ResourceResolver
import com.day.cq.tagging.TagManager
import javax.jcr.Node;
import java.lang.Thread.*;

def tagLocation = "/etc/tags"
def delay = 10; //in Milliseconds.

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

final def query = buildQuery(tagLocation);
final def result = query.execute()

def tagList = []

result.nodes.each {
    node - >
        String nodeTitle = node.name;
    tagList.add(nodeTitle);
}
def duplicates = tagList.findAll {
    tagList.count(it) > 1
}
def uniqueUsers = duplicates.unique(mutate = false)
def count = 0;
TagManager tm = resourceResolver.adaptTo(com.day.cq.tagging.TagManager);
def mergecount = 0;

uniqueUsers.each {
    def tagquery = findDuplicateTags(tagLocation, it);
    def pathresult = tagquery.execute()
    Tag tag, masterTag = null;

    count = 0;
    pathresult.nodes.each {
        node - >
            Resource r = resourceResolver.getResource(node.path)
        tag = r.adaptTo(com.day.cq.tagging.Tag)
        Node tempNode = r.adaptTo(javax.jcr.Node);
        if (count == 0) {
            masterTag = tag;
        } else if (tm != null && !(tag.getPath() == masterTag.getPath())) {
            if (!tempNode.hasNodes()) {
                println 'Merging Tag :: ' + tag.getPath() + ' into>> ' + masterTag.getPath()
                mergecount++
                tm.mergeTag(tag, masterTag)
            }
        }
        count++
        Thread.currentThread().sleep((long)(delay));
    }

}
println 'Merged tags count ::' + mergecount
