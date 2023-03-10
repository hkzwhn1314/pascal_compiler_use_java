package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;

import java.util.ArrayList;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

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
    private ICodeNode searchBranches(Object selectValue,
                                     ArrayList<ICodeNode> selectChildren) {
        // Loop over the SELECT_BRANCHes to find a match.
        for (int i = 1; i < selectChildren.size(); ++i) {
            ICodeNode branchNode = selectChildren.get(i);
            if (searchConstants(selectValue, branchNode)) {
                return branchNode;
            }
        }
        return null;
    }

    // TO LOOK
    private boolean searchConstants(Object selectValue, ICodeNode branchNode) {
        // Are the values integer or string?
        boolean integerMode = selectValue instanceof Integer;
        // Get the list of SELECT_CONSTANTS values.
        ICodeNode constantsNode = branchNode.getChildren().get(0);
        ArrayList<ICodeNode> constantsList = constantsNode.getChildren();
        // Search the list of constants.
        if (selectValue instanceof Integer) {
            for (ICodeNode constantNode : constantsList) {
                int constant = (Integer) constantNode.getAttribute(VALUE);
                if (((Integer) selectValue) == constant) {
                    return true; // match
                }
            }
        } else {
            for (ICodeNode constantNode : constantsList) {
                String constant = (String) constantNode.getAttribute(VALUE);
                if (((String) selectValue).equals(constant)) {
                    return true; // match
                }
            }
        }
        return false; // no match
    }
}
