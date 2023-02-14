package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static wci.backend.interpreter.RuntimeErrorCode.DIVISION_BY_ZERO;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
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

    // Set of arithmetic operator node types.
    private static final EnumSet<ICodeNodeTypeImpl> ARITH_OPS =
            EnumSet.of(ADD, SUBTRACT, MULTIPLY, FLOAT_DIVIDE, INTEGER_DIVIDE);

    // TODO
    private Object executeBinaryOperator(ICodeNode node,
                                         ICodeNodeTypeImpl nodeType) {
        // Get the two operand children of the operator node.
        List<ICodeNode> children = node.getChildren();
        ICodeNode operandNode1 = children.get(0);
        ICodeNode operandNode2 = children.get(1);
        // Operands.
        Object operand1 = execute(operandNode1);
        Object operand2 = execute(operandNode2);
        boolean integerMode = (operand1 instanceof Integer) &&
                (operand2 instanceof Integer);
        // ====================
        // Arithmetic operators
        // ====================
        if (ARITH_OPS.contains(nodeType)) {
            if (integerMode) {
                int value1 = (Integer) operand1;
                int value2 = (Integer) operand2;
                // Integer operations.
                switch (nodeType) {
                    case ADD:
                        return value1 + value2;
                    case SUBTRACT:
                        return value1 - value2;
                    case MULTIPLY:
                        return value1 * value2;
                    case FLOAT_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return ((float) value1) / ((float) value2);
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                    case INTEGER_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                    case MOD: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 % value2;
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                }
            } else {
                float value1 = operand1 instanceof Integer
                        ? (Integer) operand1 : (Float) operand1;
                float value2 = operand2 instanceof Integer
                        ? (Integer) operand2 : (Float) operand2;
                // Float operations.
                switch (nodeType) {
                    case ADD:
                        return value1 + value2;
                    case SUBTRACT:
                        return value1 - value2;
                    case MULTIPLY:
                        return value1 * value2;
                    case FLOAT_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0.0f) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0.0f;
                        }
                    }
                }
            }
        }
        // ==========
        // AND and OR
        // ==========
        else if ((nodeType == AND) || (nodeType == OR)) {
            boolean value1 = (Boolean) operand1;
            boolean value2 = (Boolean) operand2;
            switch (nodeType) {
                case AND:
                    return value1 && value2;
                case OR:
                    return value1 || value2;
            }
        }
        // ====================
        // Relational operators
        // ====================
        else if (integerMode) {
            int value1 = (Integer) operand1;
            int value2 = (Integer) operand2;
            // Integer operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
            }
        } else {
            float value1 = operand1 instanceof Integer
                    ? (Integer) operand1 : (Float) operand1;
            float value2 = operand2 instanceof Integer
                    ? (Integer) operand2 : (Float) operand2;
            // Float operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
            }
        }
        return 0; // should never get here
    }
    // 99
}
