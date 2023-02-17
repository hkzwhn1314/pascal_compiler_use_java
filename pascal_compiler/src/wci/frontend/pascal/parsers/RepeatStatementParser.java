package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;

import static wci.frontend.pascal.PascalErrorCode.MISSING_UNTIL;
import static wci.frontend.pascal.PascalTokenType.UNTIL;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class RepeatStatementParser extends StatementParser {


    public RepeatStatementParser(PascalParserTD parent) {
        super(parent);
    }

    // 语法形式:REPEAT j := i;k := i;UNTIL i <= j
    public ICodeNode parse(Token token) throws Exception {
        token = nextToken(); // consume the REPEAT
        // Create the LOOP and TEST nodes.
        ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
        ICodeNode testNode = ICodeFactory.createICodeNode(TEST);
        StatementParser statementParser = new StatementParser(this);
        statementParser.parseList(token, loopNode, UNTIL, MISSING_UNTIL);
        token = currentToken();

        ExpressionParser expressionParser = new ExpressionParser(this);
        testNode.addChild(expressionParser.parse(token));
        loopNode.addChild(testNode);
        return null;
    }
}
