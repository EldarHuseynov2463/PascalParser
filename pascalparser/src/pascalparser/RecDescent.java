/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pascalparser;

import static pascalparser.PascalParser.i;
import static pascalparser.PascalParser.w;

/**
 *
 * @author Имя
 */
public class RecDescent {
    
    static TreeNode expr()
    {
        if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.expr);
        token a=w.get(i); TreeNode r;
        if(a==token.lit_int||a==token.lit_real||a==token.lit_string||a==token.lit_char||a==token.lit_int||a==token.id||a==token.id_const)
            {res.add(a);++i;}
        else if(a==token.op_un){
            res.add(a);++i; 
            if(i>=w.size())return null;
            r=expr();
            if(r==null)return null;
            res.add(r);
        }
        else if(a==token.id){//function
            res.add(a);++i; if(i>=w.size())return null;a=w.get(i); 
            if(a==token.br_l){//with args
                res.add(a);++i;if(i>=w.size())return null;
                
                if(i>=w.size())return null;
                
                r=list_arg();
                if(r==null) return null;
                res.add(r);
                
                if(i>=w.size())return null;a=w.get(i);
                if(a!=token.br_r) return null;
                res.add(a);++i;
            }
        }else return null;
        
        if(i>=w.size())return res;
        int fix_i=i;
        r=expr();
        if(r==null)i=fix_i; else res.add(r);
        return res;
    }
    static TreeNode block()
    {
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.block);
        token a=w.get(i); 
        if(a!=token._begin){return null;}
        res.add(a);++i; if(i>=w.size())return null;
        
        r=list_statement();
        if(r==null)return null;
        res.add(r);
        
        if(i>=w.size())return null;a=w.get(i);
        if(a!=token._end){return null;}
        res.add(a);++i; 
        
        return res;
    }
    
    static TreeNode list_statement()
    {
        if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.list_statement);
        token a;TreeNode r;
        r=statement();
        if(r==null) {return null;}
        res.add(r);
        
        if(i>=w.size()){return res;}
        a=w.get(i);
        if(a==token.semicolon){
            res.add(a);++i;if(i>=w.size()){return null;}
            
            r=list_statement();
            if(r==null){return null;}
            res.add(r);
        }
        return res;
    }
    static TreeNode list_arg()
    {
        if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.list_arg);
        token a;TreeNode r;
        r=expr();
        if(r==null) {return null;}
        res.add(r);
        
        if(i>=w.size()){return res;}
        a=w.get(i);
        if(a==token.comma){
            res.add(a);++i;if(i>=w.size()){return null;}
            
            r=list_arg();
            if(r==null){return null;}
            res.add(r);
        }
        return res;
    }
//statement
    static TreeNode statement()
    {
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        int fix_i;
        if(a==token._if)
        {
            fix_i=i;
            r=if_statement();
            if(r==null) i=fix_i;
            else return r;
        }
        if(a==token._case)
        {
            fix_i=i;
            r=case_statement();
            if(r==null) i=fix_i;
            else return r;
        }
        if(a==token._while)
        {
            fix_i=i;
            r=while_statement();
            if(r==null) i=fix_i;
            else return r;
        }
        
        if(a==token._repeat)
        {
            fix_i=i;
            r=repeat_statement();
            if(r==null) i=fix_i;
            else return r;
        }
        if(a==token._for)
        {
            fix_i=i;
            r=for_statement();
            if(r==null) i=fix_i;
            else return r;
        }
        if(a==token.id)//assignment
        {
            fix_i=i;
            r=assign_statement();
            if(r==null) i=fix_i;
            else return r;
        }
        if(a==token._begin)
        {
            fix_i=i;
            r=block();
            if(r==null) i=fix_i;
            else {res.add(r);return res;}
        }
        if(a==token.id)//procedure
        {
            res.add(a);++i; if(i>=w.size())return null;a=w.get(i); 
            if(a==token.br_l){//with args
                res.add(a);++i;if(i>=w.size())return null;
                
                if(i>=w.size())return null;
                
                r=list_arg();
                if(r==null) return null;
                res.add(r);
                
                if(i>=w.size())return null;a=w.get(i);
                if(a!=token.br_r) return null;
                res.add(a);++i;
            }
        }
        return res;
    }
    static TreeNode if_statement()
    {
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        if(a!=token._if)return null;
        
        res.add(a);++i;if(i>=w.size())return null;
        r=expr();
        if(r==null)return null;
        res.add(r);
        if(i>=w.size())return null;
        a=w.get(i); 
        if(a!=token._then)return null;
        res.add(a);++i;if(i>=w.size())return null;
        r=statement();
        if(r==null)return null;
        res.add(r);
        
        if(i>=w.size())return res;
        a=w.get(i); 
        if(a!=token._else)return res;
        else{
            res.add(a);++i;if(i>=w.size())return null;
            r=statement();
            if(r==null)return null;
            res.add(r);
        }
        return res;
    }
static TreeNode while_statement()
    {
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        if(a!=token._while)return null;
        
        res.add(a);++i;if(i>=w.size())return null;
        r=expr();
        if(r==null)return null;
        res.add(r);
        if(i>=w.size())return null;
        a=w.get(i); 
        if(a!=token._do)return null;
        res.add(a);++i;if(i>=w.size())return null;
        r=statement();
        if(r==null)return null;
        res.add(r);
        
        return res;
    }
static TreeNode repeat_statement()
    {
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        if(a!=token._repeat)return null;
        
        res.add(a);++i;if(i>=w.size())return null;
        r=list_statement();
        if(r==null)return null;
        res.add(r);
        if(i>=w.size())return null;
        a=w.get(i); 
        if(a!=token._until)return null;
        res.add(a);++i;if(i>=w.size())return null;
        r=expr();
        if(r==null)return null;
        res.add(r);
        
        return res;
    }
static TreeNode assign_statement()
{
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        
        
        if(a!=token.id) return null;
        res.add(a);++i;if(i>=w.size())return null;
        a=w.get(i);
        if(a!=token.assign) return null;
        res.add(a);++i;if(i>=w.size())return null;
        
        r=expr();
        if(r==null)return null;
        res.add(r);
        
        return res;
}
static TreeNode for_statement()
{
            if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        if(a!=token._for)return null;
        res.add(a);++i;if(i>=w.size())return null;
        
        a=w.get(i);
        if(a!=token.id) return null;
        res.add(a);++i;if(i>=w.size())return null;
        a=w.get(i);
        if(a!=token.assign) return null;
        res.add(a);++i;if(i>=w.size())return null;
        
        r=expr();
        if(r==null)return null;
        res.add(r);if(i>=w.size())return null;
        
        a=w.get(i); 
        if(a!=token._to||a!=token._downto)return null;
        res.add(a);++i;if(i>=w.size())return null;
        
        r=expr();
        if(r==null)return null;
        if(i>=w.size())return null;
        
        a=w.get(i); 
        if(a!=token._do)return null;
        res.add(a);++i;if(i>=w.size())return null;
        r=statement();
        if(r==null)return null;
        res.add(r);
        
        return res;
}
//case
static TreeNode case_case()
{//expr colon list_statement
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.case_case);
        token a; 
        
        r=expr();
        if(r==null)return null;
        res.add(r);
        
        if(i>=w.size())return null;a=w.get(i);
        if(a!=token.colon){return null;}
        res.add(a);++i; if(i>=w.size())return null;
        
        r=list_statement();
        if(r==null) {return null;}
        res.add(r);
        
        return res;
}
static TreeNode list_case()
{
    if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.list_case);
        token a;TreeNode r;
        r=case_case();
        if(r==null) {return null;}
        res.add(r);
        
        if(i>=w.size()){return res;}
        a=w.get(i);
        if(a==token.semicolon){
            res.add(a);++i;if(i>=w.size()){return null;}
            
            r=list_case();
            if(r==null){return null;}
            res.add(r);
        }
        return res;
}
static TreeNode case_statement()
{
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.statement);
        token a=w.get(i); 
        if(a!=token._case)return null;
        
        res.add(a);++i;if(i>=w.size())return null;
        r=expr();
        if(r==null)return null;
        res.add(r);
        if(i>=w.size())return null;
        a=w.get(i); 
        if(a!=token._of)return null;
        res.add(a);++i;if(i>=w.size())return null;
        r=list_case();
        if(r==null)return null;
        res.add(r);if(i>=w.size())return null;
        
        a=w.get(i); 
        if(a!=token._end)return null;
        res.add(a);++i;
        
        return res;
}

static TreeNode type_def()
{
        if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.type_def);
        token a; 
        
        r=id_type();
        if(r==null)return null;
        res.add(r);if(i>=w.size())return null;
        
        a=w.get(i);
        if(a!=token.equal) return null;
        res.add(a);++i;if(i>=w.size())return null;
        
        r=type_desc();
        if(r==null)return null;
        res.add(r);
        
        return res;
}
static TreeNode id_type()//
{
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.id_type);
        token a; 
        
        a=w.get(i);
        if(a!=token.id) {return null;}
        res.add(a);++i;
        
        return res;
}
static TreeNode type_desc()//
{
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.type_desc);
        token a; 
        
        a=w.get(i);
        if(a!=token.id) {return null;}
        res.add(a);++i;
        
        return res;
}

static TreeNode list_var()
{
    if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.list_statement);
        token a;TreeNode r;
        
        r=list_id();
        if(r==null) {return null;}
        res.add(r);
        
        if(i>=w.size())return null;
        a=w.get(i);
        if(a!=token.colon) return null;
        res.add(a);++i;if(i>=w.size())return null;
        
        r=type_desc();
        if(r==null) {return null;}
        res.add(r);
        
        if(i>=w.size()){return res;}
        a=w.get(i);
        if(a==token.semicolon){
            res.add(a);++i;if(i>=w.size()){return null;}
            
            r=list_var();
            if(r==null){return null;}
            res.add(r);
        }
        return res;
}
static TreeNode list_id()
    {
        if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.list_id);
        token a;TreeNode r;
        a=w.get(i);
        if(a!=token.id) return null;
        res.add(a);++i;if(i>=w.size()){return res;}
        
        a=w.get(i);
        if(a==token.comma){
            res.add(a);++i;if(i>=w.size()){return null;}
            
            a=w.get(i);
            if(a!=token.id) {return null;}
            res.add(a);++i;
        }
        return res;
    }
static TreeNode sect_var()
{
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.sect_var);
        token a; 
        
        a=w.get(i);
        if(a!=token._var) {return null;}
        res.add(a);++i;
        
        r=list_var();
        if(r==null) {return null;}
        res.add(r);
                
        return res;
}

static TreeNode id_const()//
{
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.id_const);
        token a; 
        
        a=w.get(i);
        if(a!=token.id) {return null;}
        res.add(a);++i;
        
        return res;
}
static TreeNode id_module()//
{
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.id_module);
        token a; 
        
        a=w.get(i);
        if(a!=token.id) {return null;}
        res.add(a);++i;
        
        return res;
}
static TreeNode list_module()
{
    if(i>=w.size())return null;
        TreeNode res=new TreeNode(token.list_module);
        token a;TreeNode r;
        r=id_module();
        if(r==null) {return null;}
        res.add(r);
        
        if(i>=w.size()){return res;}
        a=w.get(i);
        if(a==token.comma){
            res.add(a);++i;if(i>=w.size()){return null;}
            
            r=list_module();
            if(r==null){return null;}
            res.add(r);
        }
        return res;
}
static TreeNode sect_uses()
{
    if(i>=w.size())return null;
        TreeNode r;
        TreeNode res=new TreeNode(token.sect_uses);
        token a; 
        
        a=w.get(i);
        if(a!=token._uses) {return null;}
        res.add(a);++i;
        
        r=list_module();
        if(r==null) {return null;}
        res.add(r);
                
        return res;
}
}
