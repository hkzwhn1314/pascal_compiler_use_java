package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalErrorCode.MISSING_DO;
import static wci.frontend.pascal.PascalErrorCode.MISSING_TO_DOWNTO;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class ForStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public ForStatementParser(PascalParserTD parent) {
        super(parent);
    }

    // Synchronization set for TO or DOWN TO.
    static final EnumSet<PascalTokenType> TO_DOWNTO_SET =
            ExpressionParser.EXPR_START_SET.clone();

    static {
        TO_DOWNTO_SET.add(TO);
        TO_DOWNTO_SET.add(DOWNTO);
        TO_DOWNTO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    // Synchronization set for DO.
    private static final EnumSet<PascalTokenType> DO_SET =
            StatementParser.STMT_START_SET.clone();

    static {
        DO_SET.add(DO);
        DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    // FOR k := j TO 5 DO n := k
    public ICodeNode parse(Token token) throws Exception {
        token = nextToken(); // consume the FOR
        Token targetToken = token;

        // Create the loop COMPOUND, LOOP, and TEST nodes.
        ICodeNode compoundNode = ICodeFactory.createICodeNode(COMPOUND);
        ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
        ICodeNode testNode = ICodeFactory.createICodeNode(TEST);

        // Parse the embedded initial assignment.
        AssignmentStatementParser assignmentParser =
                new AssignmentStatementParser(this);
        ICodeNode initAssignNode = assignmentParser.parse(token);

        // Set the current line number attribute.
        setLineNumber(initAssignNode, targetToken);

        // The COMPOUND node adopts the initial ASSIGN and the LOOP nodes
        // as its first and second children.
        compoundNode.addChild(initAssignNode);
        compoundNode.addChild(loopNode);


        // Synchronize at the TO or DOWNTO.
        token = synchronize(TO_DOWNTO_SET);
        TokenType direction = token.getType();

        // Look for the TO or DOWNTO.
        if ((direction == TO) || (direction == DOWNTO)) {
            token = nextToken(); // consume the TO or DOWNTO
        } else {
            direction = TO;
            errorHandler.flag(token, MISSING_TO_DOWNTO, this);
        }

        // Create a relational operator node: GT for TO, or LT for DOWNTO.
        // replace the to in > or <
        ICodeNode relOpNode = ICodeFactory.createICodeNode(direction == TO
                ? GT : LT);

        // for to k := 5 initAssignNode.getChildren().get(0); = node(k)
        ICodeNode controlVarNode = initAssignNode.getChildren().get(0);
        relOpNode.addChild(controlVarNode.copy());

        // Parse the termination expression. The relational operator node
        // adopts the expression as its second child.
        // to solve the :=
        ExpressionParser expressionParser = new ExpressionParser(this);
        relOpNode.addChild(expressionParser.parse(token));

        testNode.addChild(relOpNode);
        loopNode.addChild(testNode);

        // Synchronize at the DO.
        token = synchronize(DO_SET);
        if (token.getType() == DO) {
            token = nextToken(); // consume the DO
        } else {
            errorHandler.flag(token, MISSING_DO, this);
        }


        return null;
    }
}
