package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeNodeType;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalTokenType.DO;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class WhileStatementParser extends StatementParser {


    private ICodeNodeType TEST;

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public WhileStatementParser(PascalParserTD parent) {
        super(parent);
    }

    // Synchronization set for DO.
    private static final EnumSet<PascalTokenType> DO_SET =
            StatementParser.STMT_START_SET.clone();

    static {
        DO_SET.add(DO);
        DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    public ICodeNode parse(Token token) throws Exception {
        token = nextToken(); // consume the WHILE
        // Create LOOP, TEST, and NOT nodes.
        // TODO
        ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
        ICodeNode breakNode = ICodeFactory.createICodeNode(TEST);
        ICodeNode notNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NOT);
        return null;
    }
}
