package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.ArrayList;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.DATA_VALUE;

/**
 * @Author zhaocenliu
 * @create 2023/2/13 6:23 PM
 */
public class ExpressionExecutor extends StatementExecutor {

    public ExpressionExecutor(Executor parent) {
        super(parent);
    }


    public Object execute(ICodeNode node) {
        ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();
        switch (nodeType) {

            case VARIABLE: {
                // Get the variable&apos;s symbol table entry and return its value.
                SymTabEntry entry = (SymTabEntry) node.getAttribute(ID);
                return entry.getAttribute(DATA_VALUE);
            }

            case INTEGER_CONSTANT: {
                // Return the integer value.
                return (Integer) node.getAttribute(VALUE);
            }

            case REAL_CONSTANT: {
                // Return the float value.
                return (Float) node.getAttribute(VALUE);
            }
            case STRING_CONSTANT: {
                // Return the string value.
                return (String) node.getAttribute(VALUE);
            }

            case NEGATE: {
                // Get the NEGATE node&apos;s expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);
                // Execute the expression and return the negative of its value.
                Object value = execute(expressionNode);
                if (value instanceof Integer) {
                    return -((Integer) value);
                } else {
                    return -((Float) value);
                }
            }

            case NOT: {
                // Get the NOT node&apos;s expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);
                // Execute the expression and return the "not" of its value.
                // return the result of the true or false
                // ! -> true follow anti the false  *******
                boolean value = (Boolean) execute(expressionNode);
                return !value;
            }

            default:
                return executeBinaryOperator(node, nodeType);
        }

    }

    // TODO
    private ICodeNode executeBinaryOperator(ICodeNode node, ICodeNodeTypeImpl nodeType) {
        return null;
    }
}
