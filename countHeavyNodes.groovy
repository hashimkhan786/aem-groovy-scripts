/*
Count Number of Nodes which have more than 1000+ child nodes.
This a common use case wherein you are asked to check whether a particular hierarchy has nodes which has more than 1000 child nodes . You can change the Search Path as per you convenience and list down the nodes under that hierarchy.
@author Hashim Khan */

import javax.jcr.NodeIterator

def path = "/etc/tags"
def variable = 1000

println 'Node,COUNT'
getNode(path).recurse {
    node >
        NodeIterator it = node.getNodes()
    def count = 0
    while (it.hasNext()) {
        def nodetemp = it.nextNode()
        count++
    }

    if (count >= variable)
        println node.path + ',' + count
}
