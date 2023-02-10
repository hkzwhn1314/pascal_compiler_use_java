package wci.backend.interpreter;

import wci.backend.*;
import wci.intermediate.ICode;
import wci.intermediate.SymTabStack;
import wci.message.*;

import static wci.message.MessageType.INTERPRETER_SUMMARY;

/**
 * <h1>Executor</h1>
 *
 * <p>The executor for an interpreter back end.</p>
 *
 *
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class Executor extends Backend
{
    /**
     * Process the intermediate code and the symbol table generated by the
     * parser to execute the source program.
     * @param iCode the intermediate code.
     * @param symTabStack the symbol table stack.
     * @throws Exception if an error occurred.
     */
    public void process(ICode iCode, SymTabStack symTabStack)
        throws Exception
    {
        long startTime = System.currentTimeMillis();
        float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
        int executionCount = 0;
        int runtimeErrors = 0;

        // Send the interpreter summary message.
        sendMessage(new Message(INTERPRETER_SUMMARY,
                                new Number[] {executionCount,
                                              runtimeErrors,
                                              elapsedTime}));
    }
}
