package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;
import wci.message.Message;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;
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
