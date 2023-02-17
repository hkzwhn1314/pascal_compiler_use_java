package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.intermediate.ICodeNode;

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
        return null;
    }
}
