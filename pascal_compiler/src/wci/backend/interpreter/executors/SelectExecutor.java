package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;

import java.util.ArrayList;

/**
 * @Author zhaocenliu
 * @create 2023/2/25 7:55 PM
 */
public class SelectExecutor extends StatementExecutor {
    public SelectExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node) {
        // Get the SELECT node&apos;s children.
        ArrayList<ICodeNode> selectChildren = node.getChildren();
        ICodeNode exprNode = selectChildren.get(0);
        // Evaluate the SELECT expression.
        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        Object selectValue = expressionExecutor.execute(exprNode);
        // Attempt to select a SELECT_BRANCH.
        ICodeNode selectedBranchNode = searchBranches(selectValue,
                selectChildren);
        // If there was a selection, execute the SELECT_BRANCH&apos;s statement.
        if (selectedBranchNode != null) {
            ICodeNode stmtNode = selectedBranchNode.getChildren().get(1);
            StatementExecutor statementExecutor = new StatementExecutor(this);
            statementExecutor.execute(stmtNode);
        }
        ++executionCount; // count the SELECT statement itself
        return null;
    }
    //TODO
    private ICodeNode searchBranches(Object selectValue, ArrayList<ICodeNode> selectChildren) {
        return null;
    }
}
