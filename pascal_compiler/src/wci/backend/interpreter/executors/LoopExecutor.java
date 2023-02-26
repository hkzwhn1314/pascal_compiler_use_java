package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;

/**
 * @Author zhaocenliu
 * @create 2023/2/25 7:55 PM
 */
public class LoopExecutor extends StatementExecutor{
    public LoopExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node){
        return null;
    }
}
