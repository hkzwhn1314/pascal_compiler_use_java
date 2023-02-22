package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalErrorCode.MISSING_THEN;
import static wci.frontend.pascal.PascalTokenType.ELSE;
import static wci.frontend.pascal.PascalTokenType.THEN;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class IfStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public IfStatementParser(PascalParserTD parent) {
        super(parent);
    }

    //IF (i = j) THEN t := 200
    //ELSE f := -200;
    // Synchronization set for THEN.
    private static final EnumSet<PascalTokenType> THEN_SET =
            StatementParser.STMT_START_SET.clone();

    static {
        THEN_SET.add(THEN);
        THEN_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    public ICodeNode parse(Token token) throws Exception {
        token = nextToken();
        // Create an IF node.
        ICodeNode ifNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.IF);
        // Parse the expression.
        // The IF node adopts the expression subtree as its first child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        ifNode.addChild(expressionParser.parse(token));

        // Synchronize at the THEN.
        token = synchronize(THEN_SET);
        if (token.getType() == THEN) {
            token = nextToken(); // consume the THEN
        } else {
            errorHandler.flag(token, MISSING_THEN, this);
        }
        // Parse the THEN statement.
        // The IF node adopts the statement subtree as its second child.
        StatementParser statementParser = new StatementParser(this);
        ifNode.addChild(statementParser.parse(token));

        token = currentToken();


        // Look for an ELSE.
        if (token.getType() == ELSE) {
            token = nextToken(); // consume the THEN
            // Parse the ELSE statement.
            // The IF node adopts the statement subtree as its third child.
            ifNode.addChild(statementParser.parse(token));
        }

        return ifNode;
    }
}

