package components;

import controllers.Executer;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import models.Link;
import models.Node;
import models.Relation;
import models.Request;
import models.TraversingConstraints.Mode;
import models.TraversingConstraints.Uniqueness;

/**
 * Concrete implementation of an executer
 * @note depth: limit traversing if (depth > 0 && depth < count(select)).
 *              if depth > count(select), add a padding with the last link
 * @note nodeUniqueness: prevents that a node returns childs
 * @note linkUniqueness: prevents to add the target of a relation
 * @author freaxmind
 */
public class RequestExecuter implements Executer {
    /**
     * Handle a level of execution (depth traversing)
     * can be extended if you find more usage
     */
    private static class ExecutionContext {
        public Node node;
        public int level;
        
        public ExecutionContext(Node node, int level) {
            this.node = node;
            this.level = level;
        }
    }
    
    /**
     * Dispatcher
     * @param request
     * @return distinct list of node (set)
     */
    @Override
    public Set<Node> execute(Request request) {
       Collection<Node> result;
       
       this.preExecute(request);
        
       if (request.getConstraints().getMode() == Mode.BREADTH) {
           result = this.breadthTraversing(request);
       } else {
           result = this.depthTraversing(request);
       }
       
       this.postExecute(result, request);
       
       return new LinkedHashSet<>(result);
    }
    
    /**
     * Operations pre execution
     * @param request 
     */
    private void preExecute(Request request) {
        // add a padding with the last link
        if (request.getSelect().size() < request.getConstraints().getDepth()) {
            Link last = new LinkedList<>(request.getSelect()).getLast();
            
            for (int i=request.getSelect().size(); i<request.getConstraints().getDepth(); i++) {
                request.getSelect().add(last);
            }
        }
    }
    
    /**
     * Operations post execution
     * @param request 
     */
    private void postExecute(Collection<Node> result, Request request) {
        // remove all explored flags
       request.getFrom().clean();
    }

    /**
     * Breadth traversing method
     * @param request
     * @return 
     */
    private Collection<Node> breadthTraversing(Request request) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(request.getFrom());
        
        // depth limit : count(select links) or depth constraints if it is set      
        int maxDepth = request.getSelect().size();
        if (request.getConstraints().getDepth() > 0 && request.getConstraints().getDepth() < maxDepth) {
            maxDepth = request.getConstraints().getDepth();
        }

        int level = 0;
        for (Link link : request.getSelect()) {
            // set a queue with nodes from the current level (avoid infinite loop)
            Queue<Node> workingQueue = new LinkedList<>(queue);
            queue = new LinkedList<>();
            
            while (!workingQueue.isEmpty()) {
                Node node = workingQueue.poll();
                
                // set explored on node if uniqueness constraint is set
                // don't handle the node if it's already explored
                if (node.isExplored()) {
                    continue;
                } else if (request.getConstraints().getNodeUniqueness() == Uniqueness.TOTAL) {
                    node.setExplored(true);
                }

                Collection<Relation> children = node.findByLink(link);
                for (Relation child : children) {
                    // set explored on relation if uniqueness constraint is set
                    // don't handle the relation if it's already explored
                    if (child.isExplored()) {
                        continue;
                    } else if (request.getConstraints().getLinkUniqueness() == Uniqueness.PARTIAL) {
                        child.setExplored(true, false);
                    } else if (request.getConstraints().getLinkUniqueness() == Uniqueness.TOTAL) {
                        child.setExplored(true, true);
                    }
                    
                    queue.add(child.getTarget());
                }
            }
            
            level++;
            // stop the traversing if depth limit is reached
            if (level >= maxDepth) {
                break;
            }
        }
        
        return queue;
    }

    /**
     * Depth traversing method
     * @param request
     * @return 
     */
    private Collection<Node> depthTraversing(Request request) {
        Stack<Node> resStack = new Stack<>();
        Stack<ExecutionContext> workingStack = new Stack<>();
        workingStack.push(new ExecutionContext(request.getFrom(), 0));
        LinkedList<Link> select = new LinkedList<>(request.getSelect());
        int maxDepth = select.size();
        
        // depth limit : count(select links) or depth constraints if it is set
        if (request.getConstraints().getDepth() > 0 && request.getConstraints().getDepth() < maxDepth) {
            maxDepth = request.getConstraints().getDepth();
        }
        
        while (!workingStack.isEmpty()) {
            ExecutionContext current = workingStack.pop();  // pop removes the element
            
            // check depth limit
            if (current.level >= maxDepth) {
                // add it to the result collection and continue
                resStack.add(current.node);
                continue;
            }
            
            // set explored on node if uniqueness constraint is set
            // don't handle the node if it's already explored
            if (current.node.isExplored()) {
                continue;
            } else if (request.getConstraints().getNodeUniqueness() == Uniqueness.TOTAL) {
                current.node.setExplored(true);
            }
            
            // get the link of the next level (level start at 1, select start at 0)
            Link link = select.get(current.level);
            
            Collection<Relation> children = current.node.findByLink(link);
            for (Relation child : children) {
                // set explored on relation if uniqueness constraint is set
                // don't handle the relation if it's already explored
                if (child.isExplored()) {
                    continue;
                } else if (request.getConstraints().getLinkUniqueness() == Uniqueness.PARTIAL) {
                    child.setExplored(true, false);
                } else if (request.getConstraints().getLinkUniqueness() == Uniqueness.TOTAL) {
                    child.setExplored(true, true);
                }
                workingStack.add(new ExecutionContext(child.getTarget(), current.level+1));
            }
        }
        
        return resStack;
    }
}
