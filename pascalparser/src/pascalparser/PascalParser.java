
package pascalparser;
import java.util.*;
import java.io.*;


enum token{prog, header, list_sect, block,
    sect, sect_uses, sect_type, sect_var, sect_const, sect_label,
    list_module, list_type, list_var, list_const, list_label,list_id,
    type_def, type_desc, enum_type_value, enum_value,
    
    subprog, subprog_header, list_param, list_arg,
    expr, list_case, case_case,
    lit_char, lit_int, lit_string, lit_real,
    op_un, op_bin,
    list_statement, statement,
    id, id_type, id_module, id_const, label,
    
    br_l, br_r, sq_l,sq_r,
    assign,
    dot, double_dot,comma, colon, semicolon,
    equal,n_equal,l_equal,g_equal,greater,lesser,
    
    _begin, _end, _program,
    _if,_then,_else,_while,_do,_repeat,_until,_case,_of,_for,_to,_downto,
    _type,_array,_var,_uses,_const,
    
    comment
    } 

class TreeNode
{
    token data;
    ArrayList<TreeNode> children;
    
    public TreeNode(token data) {
        this.data = data;
        children = new ArrayList<TreeNode>();
    }
        public void print()
        {
            if(children.isEmpty()){PascalParser.seq.add(data); System.out.println(data.toString());}
            else for(TreeNode child: children) child.print();
        }
        public void add(TreeNode subtree)
        {
            children.add(subtree);
        }
        public void add(token data)
        {
            children.add(new TreeNode(data));
        }
}
public class PascalParser {
    
    public static ArrayList seq=new ArrayList<token>();
    
    static ArrayList<token> w = new ArrayList<token>();
    static int i=0;
    static boolean err=false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //String s="'that''s it, Pascal string literal' {comment} (*co\nmment*)";
        String s;
        s="program helloworld begin  'ghj' 'd' end.";
         //s="123 1.2E-4 1. -01 .010101 -1.2 1.2E6";
        Lexer.parse(s);
        ArrayList<token> seq=Lexer.seq;
        ArrayList<String> lexems=Lexer.lexems;
        System.out.println(s);
        for(int i=0;i<seq.size();++i)
            System.out.println(seq.get(i).toString()+"\t"+lexems.get(i));
        //w.add(token._if);w.add(token.lit_int);w.add(token._then);w.add(token.statement);
        //w.add(token._else);w.add(token.statement);
        //if_statement().print();
        //w.add(token.begin);w.add(token.statement);w.add(token.end);
        //block().print();
        
        //w.add(token.id);w.add(token.comma);w.add(token.lit_int);
        //list_arg().print();
    }
}

