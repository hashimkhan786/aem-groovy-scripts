/*
Find all the pages of a particular Template .
I have depicted to display all the pages using a specific template. In real world you might be asked to modify some of the properties of a template or add, subtract something. All this you can easily do in an instant using groovy script.
@author Hashim Khan */
import javax.jcr.Node

/*Flag to count the number of pages*/
noOfPages = 0
/*Pathfield which needs to be iterated for an operation*/
path = '/content/geometrixx/en/'
findAllPagesWidTemplate()

/*This method is used to Iterate all the pages under a hierarchy
 *and find pages with a specific template
 */

def findAllPagesWidTemplate() {
    getPage(path).recurse {
        page - >
            def content = page.node
        def property = content.get('sling:resourceType')
        if (property == "geometrixx/components/contentpage") {
            noOfPages++
            println 'Page Path:' + content.path
        }
    }
}
println 'No Of Pages::' + noOfPages
