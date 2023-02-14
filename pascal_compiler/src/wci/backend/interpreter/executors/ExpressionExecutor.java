package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;

/**
 * @Author zhaocenliu
 * @create 2023/2/13 6:23 PM
 */
public class ExpressionExecutor extends StatementExecutor {

    public ExpressionExecutor(Executor parent) {
        super(parent);
    }


    public Object execute(ICodeNode node) {
        return null;
    }
}
