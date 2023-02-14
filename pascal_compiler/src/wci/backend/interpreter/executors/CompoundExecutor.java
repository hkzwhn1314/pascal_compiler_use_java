package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;

import java.util.ArrayList;

/**
 * @Author zhaocenliu
 * @create 2023/2/13 6:23 PM
 */
public class CompoundExecutor extends StatementExecutor {

    public CompoundExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node) {
        // Loop over the children of the COMPOUND node and execute each child.
        StatementExecutor statementExecutor = new StatementExecutor(this);
        ArrayList<ICodeNode> children = node.getChildren();
        for (ICodeNode child : children) {
            statementExecutor.execute(child);
        }
        return null;
    }
}
