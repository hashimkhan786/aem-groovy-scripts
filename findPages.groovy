/*
Find Number of Pages, Page Title , Page Names in a Site hierarchy
@author Hashim Khan */
import javax.jcr.Node

/*Flag to count the number of pages*/
noOfPages = 0
/*Pathfield which needs to be iterated for an operation*/
path = '/content/geometrixx/en/'
findAllPages()

/*This method is used to Iterate all the pages under a hierarchy
 *and get their page title ,path and the overall number of
 *pages.*/

def findAllPages() {
    getPage(path).recurse {
        page ->
            println 'Title:' + page.title
        println 'Path:' + page.path
        noOfPages++
    }
}
