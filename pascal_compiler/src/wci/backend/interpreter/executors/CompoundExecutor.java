package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;

/**
 * @Author zhaocenliu
 * @create 2023/2/13 6:23 PM
 */
public class CompoundExecutor extends StatementExecutor {

    public CompoundExecutor(Executor parent) {
        super(parent);
    }
}
