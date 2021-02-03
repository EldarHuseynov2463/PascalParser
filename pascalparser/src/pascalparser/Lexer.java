package pascalparser;
import java.util.ArrayList;
import java.util.Arrays;
public class Lexer {
    static int i=0,n;
    static char c;
    
    static public ArrayList<token> seq=new ArrayList<>();
    static public ArrayList<String> lexems=new ArrayList<>();
    
    static String[] keyw={"begin","end",
            "program","uses","type","var","const","label",
            "function","procedure",
            "array","of","set","string","file","text","record","object"};
    static token getKeyword(String s)
    {
        s=s.toLowerCase();
        switch(s)
        {
            case "begin":return token._begin;
            case "end":return token._end;
            case "program":return token._program;
            case "if":return token._if;
            case "then":return token._then;
            case "else":return token._else;
            case "while":return token._while;
            case "do":return token._do;
            case "repeat":return token._repeat;
            case "until":return token._until;
            case "case":return token._case;
            case "of":return token._of;
            case "for":return token._for;
            case "to":return token._to;
            case "downto":return token._downto;
            case "type":return token._type;
            case "array":return token._array;
            case "var":return token._var;
            case "uses":return token._uses;
            case "const":return token._const;
            //case "begin":return token.begin;
            
        }
        return token.id;
    }
    static void str()
    {
        
    }
    public enum state{_var,_const,_uses,_proc,_func,_block}
    public static boolean parse(String r)
    {
        
        
        //types, bool, if-else-...
        
        ArrayList<String> keywords=new ArrayList<String>();
        keywords.addAll(Arrays.asList(keyw));
        
        n=r.length(); int index; String s="";token lex;state st;
        while(i<n)
        {
            if(Character.isDigit(r.charAt(i)))
            {
                s=r.substring(i);
                index=getNumber(s);
                if(index==0) {System.out.println("Number parsing error");return false;}
                index+=i;
                s=r.substring(i, index);
                lexems.add(s);
                if(s.contains("E")||s.contains("."))
                    lex=token.lit_real;
                else
                    lex=token.lit_int;
                seq.add(lex);
                i=index+1;
            }
            //REDO for Var, Const, Procedure, Function cases and keywords
            else if(Character.isLetter(r.charAt(i))||r.charAt(i)=='_')
            {
                index=i;
                do{
                    ++index;
                }while(Character.isLetterOrDigit(r.charAt(index))||r.charAt(index)=='_');
                s=r.substring(i, index);
                lexems.add(s);
                lex=getKeyword(s);
                switch(lex)
                {
                    case _var: st=state._var;break;
                    case _const: st=state._const;break;
                    case _uses: st=state._uses;break;
                    //case _proc:
                    //case _func:
                    case _begin: st=state._block;break;
                    case id: break;
                }
                seq.add(lex);
                i=index+1;
            }
            else switch(r.charAt(i))
            {
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                    ++i;
                    break;
                case '\'':
                    index = r.indexOf("'", i+1);
                    while(r.charAt(index + 1)=='\'')
                        index = r.indexOf("'", index + 2);
                    s=r.substring(i, index+1);
                    lexems.add(s);
                    if(s.length()==3)
                        seq.add(token.lit_char);
                    else seq.add(token.lit_string);
                    i=index+1;
                    break;
                    
                case '{':
                    index = r.indexOf("}", i+1);
                    //lexems.add(r.substring(i, index+1));
                    //seq.add(token.comment);
                    i=index+1;
                    break;
                case '(':
                    if(r.charAt(i+1)=='*')
                    {
                        index = r.indexOf(")", i+3);//(**)
                        while(r.charAt(index - 1)!='*')
                            index = r.indexOf(")", index + 1);
                        //lexems.add(r.substring(i, index+1));
                        //seq.add(token.comment);
                        i=index+1;
                    }
                    else 
                    {
                        lexems.add("(");
                        seq.add(token.br_l);
                        ++i;
                    }
                    break;
                case ')':
                    lexems.add(")");
                    seq.add(token.br_r);
                    ++i;
                    break;
                case '[':
                    lexems.add("[");
                    seq.add(token.sq_l);
                    ++i;
                    break;
                case ']':
                    lexems.add("]");
                    seq.add(token.sq_r);
                    ++i;
                    break;
                case '.':
                    //dot ,double dot for range, or a real num
                    if(r.charAt(i+1)=='.'){
                        lexems.add("..");
                        seq.add(token.double_dot);
                        ++i;++i;
                    }
                    else if(!Character.isDigit(r.charAt(i+1)))
                    {
                        lexems.add(".");
                        seq.add(token.dot);
                        ++i;
                    }
                    else{
                        s=r.substring(i);
                        index=getNumber(s);
                        if(index==0) {System.out.println("Number parsing error");return false;}
                        index+=i;
                        s=r.substring(i, index);
                        lexems.add(s);
                        if(s.contains("E")||s.contains("."))
                            lex=token.lit_real;
                        else
                            lex=token.lit_int;
                        seq.add(lex);
                        i=index+1;
                    }
                    break;
                case '>':
                    if(r.charAt(++i)=='='){
                        lexems.add(">=");
                        seq.add(token.g_equal);
                        ++i;
                    }
                    else{
                        lexems.add(">");
                        seq.add(token.greater);
                    }
                    break;
                case '<':
                    if(r.charAt(++i)=='='){
                        lexems.add("<=");
                        seq.add(token.l_equal);
                        ++i;
                    }
                    else if(r.charAt(++i)=='>'){
                        lexems.add("<>");
                        seq.add(token.n_equal);
                        ++i;
                    }
                    else{
                        lexems.add("<");
                        seq.add(token.lesser);
                    }
                    break;
                case ':':
                    if(r.charAt(++i)=='='){
                        lexems.add(":=");
                        seq.add(token.assign);
                        ++i;
                    }
                    else{
                        lexems.add(":");
                        seq.add(token.colon);
                    }
                    break;
                
                default:++i;
            }//switch        
        }//while
    return true;
    }
    static int getNumber(String s)//
    {
        int i=0; boolean isint=true;
        if(s.charAt(i)=='-'||s.charAt(i)=='+') ++i;
        if(!Character.isDigit(s.charAt(i)) && s.charAt(i)!='.') return 0;
        
        if(Character.isDigit(s.charAt(i)))
        {
            while(i<s.length() && Character.isDigit(s.charAt(i)))
                ++i;
        }
        if(s.charAt(i)=='.')
        {
            isint=false;
            ++i;
            if(!Character.isDigit(s.charAt(i)) && s.charAt(i)!='E') return i;
            while(i<s.length() && Character.isDigit(s.charAt(i)))
                ++i;
        }
        if(s.charAt(i)=='E')
        {
            isint=false;
            
            ++i;if(s.charAt(i)=='-'||s.charAt(i)=='+') ++i;
            if(!Character.isDigit(s.charAt(i))) return 0;
            while(i<s.length() && Character.isDigit(s.charAt(i)))
                ++i;
        }
        
        
        return i;
    }
}
