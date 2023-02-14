package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;
import wci.message.Message;

import java.util.ArrayList;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.DATA_VALUE;
import static wci.message.MessageType.ASSIGN;

/**
 * @Author zhaocenliu
 * @create 2023/2/13 6:22 PM
 */
public class AssignmentExecutor extends StatementExecutor {
    public AssignmentExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node) {
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode variableNode = children.get(0);
        ICodeNode expressionNode = children.get(1);

        // Execute the expression and get its value.
        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        Object value = expressionExecutor.execute(expressionNode);

        SymTabEntry variableId = (SymTabEntry) variableNode.getAttribute(ID);
        variableId.setAttribute(DATA_VALUE, value);
        sendMessage(node, variableId.getName(), value);
        ++executionCount;
        return null;
    }

    /**
     * Send a message about the assignment operation.
     *
     * @param node         the ASSIGN node.
     * @param variableName the name of the target variable.
     * @param value        the value of the expression.
     */
    private void sendMessage(ICodeNode node, String variableName, Object value) {
        Object lineNumber = node.getAttribute(LINE);
        // Send an ASSIGN message.
        if (lineNumber != null) {
            sendMessage(new Message(ASSIGN, new Object[]{lineNumber,
                    variableName,
                    value}));
        }
    }
}
