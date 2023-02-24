package wci.frontend.pascal.parsers;

import wci.frontend.EofToken;
import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;

import java.util.EnumSet;
import java.util.HashSet;

import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class CaseStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CaseStatementParser(PascalParserTD parent) {
        super(parent);
    }

    //CASE i+1 OF
    //1: j := i;
    //4: j := 4*i;
    //5, 2, 3: j := 523*i;
    //END
// Synchronization set for starting a CASE option constant.
    private static final EnumSet<PascalTokenType> CONSTANT_START_SET =
            EnumSet.of(IDENTIFIER, INTEGER, PLUS, MINUS, STRING);
    // Synchronization set for OF.
    private static final EnumSet<PascalTokenType> OF_SET =
            CONSTANT_START_SET.clone();

    static {
        OF_SET.add(OF);
        OF_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    public ICodeNode parse(Token token) throws Exception {
        token = nextToken(); // consume the CASE
        // Create a SELECT node.
        ICodeNode selectNode = ICodeFactory.createICodeNode(SELECT);
        // Parse the CASE expression.
        // The SELECT node adopts the expression subtree as its first child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        selectNode.addChild(expressionParser.parse(token));

        // Synchronize at the OF.
        token = synchronize(OF_SET);
        if (token.getType() == OF) {
            token = nextToken(); // consume the OF
        } else {
            errorHandler.flag(token, MISSING_OF, this);
        }

        // Set of CASE branch constants.
        HashSet<Object> constantSet = new HashSet<Object>();

        while (!(token instanceof EofToken) && (token.getType() != END)) {
            // The SELECT node adopts the CASE branch subtree.
            selectNode.addChild(parseBranch(token, constantSet));
            token = currentToken();
            TokenType tokenType = token.getType();
            // Look for the semicolon between CASE branches.
            if (tokenType == SEMICOLON) {
                token = nextToken(); // consume the ;
            }
            // If at the start of the next constant, then missing a semicolon.
            else if (CONSTANT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, MISSING_SEMICOLON, this);
            }
        }

        // Look for the END token.
        if (token.getType() == END) {
            token = nextToken(); // consume END
        } else {
            errorHandler.flag(token, MISSING_END, this);
        }

        return selectNode;

    }


    /**
     * Parse a CASE branch.
     *
     * @param token       the current token.
     * @param constantSet the set of CASE branch constants.
     * @return the root SELECT_BRANCH node of the subtree.
     * @throws Exception if an error occurred.
     */
    // currentToken = of
    private ICodeNode parseBranch(Token token, HashSet<Object> constantSet)
            throws Exception {
        ICodeNode branchNode = ICodeFactory.createICodeNode(SELECT_BRANCH);
        ICodeNode constantsNode =
                ICodeFactory.createICodeNode(SELECT_CONSTANTS);
        branchNode.addChild(constantsNode);

        // Parse the list of CASE branch constants.
        // The SELECT_CONSTANTS node adopts each constant.
        parseConstantList(token, constantsNode, constantSet);

        return null;
    }


    // Synchronization set for COMMA.
    private static final EnumSet<PascalTokenType> COMMA_SET =
            CONSTANT_START_SET.clone();

    static {
        COMMA_SET.add(COMMA);
        COMMA_SET.add(COLON);
        COMMA_SET.addAll(StatementParser.STMT_START_SET);
        COMMA_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse a list of CASE branch constants.
     *
     * @param token         the current token.
     * @param constantsNode the parent SELECT_CONSTANTS node.
     * @param constantSet   the set of CASE branch constants.
     * @throws Exception if an error occurred.
     */


    private void parseConstantList(Token token, ICodeNode constantsNode,
                                   HashSet<Object> constantSet) throws Exception {
        while (CONSTANT_START_SET.contains(token.getType())) {
            constantsNode.addChild(parseConstant(token, constantSet));
        }
    }

    /**
     * Parse CASE branch constant.
     *
     * @param token       the current token.
     * @param constantSet the set of CASE branch constants.
     * @return the constant node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseConstant(Token token, HashSet<Object> constantSet)
            throws Exception {
        return null;
    }
}
