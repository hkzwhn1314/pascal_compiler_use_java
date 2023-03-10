package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;
import wci.message.Message;

import static wci.backend.interpreter.RuntimeErrorCode.UNIMPLEMENTED_FEATURE;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static wci.message.MessageType.SOURCE_LINE;

/**
 * @Author zhao cen liu
 * @create 2023/2/13 6:10 PM
 */
public class StatementExecutor extends Executor {
    // TODO
    public StatementExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node) {
        ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();
        // Send a message about the current source line.
        sendSourceLineMessage(node);
        switch (nodeType) {
            case COMPOUND: {
                CompoundExecutor compoundExecutor = new CompoundExecutor(this);
                return compoundExecutor.execute(node);
            }
            case ASSIGN: {
                AssignmentExecutor assignmentExecutor =
                        new AssignmentExecutor(this);
                return assignmentExecutor.execute(node);
            }
            case LOOP: {
                LoopExecutor loopExecutor = new LoopExecutor(this);
                return loopExecutor.execute(node);
            }
            case IF: {
                IfExecutor ifExecutor = new IfExecutor(this);
                return ifExecutor.execute(node);
            }
            case SELECT: {
                SelectExecutor selectExecutor = new SelectExecutor(this);
                return selectExecutor.execute(node);
            }
            case NO_OP:
                return null;
            default: {
                errorHandler.flag(node, UNIMPLEMENTED_FEATURE, this);
                return null;
            }
        }

    }

    private void sendSourceLineMessage(ICodeNode node) {
        Object lineNumber = node.getAttribute(LINE);
        // Send the SOURCE_LINE message.
        if (lineNumber != null) {
            sendMessage(new Message(SOURCE_LINE, lineNumber));
        }
    }
}
