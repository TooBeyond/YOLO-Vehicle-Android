package com.tencent.yolov5ncnn.Suffix;

import java.util.ArrayList;
import java.util.Stack;

public class suffix {
    /**
     * 此方式仅能够计算两个数,如果出现多个,可以分别计算,pow()可以用于幂运算
     * @author arthur: ZengZhitao
     */
    public static int Exp(String formula,char a,char b){   //假设现在有两个变量,变量传x,y
        //下面这两步替换,非常重要!!!!
        formula = formula.replace('x',a);//replace替换,必须要提供char,所以变量在这里用不了
        formula = formula.replace('y',b);

        ArrayList result = getStringList(formula);  //String转换为List
        result = getPostOrder(result);   //中缀变后缀
        System.out.println("后缀表达式"+result);
        int res = calculate(result);   //计算
        return res;
    }


    //仅仅是对字符串进行计算
    public static int Express(String formula){
        ArrayList result = getStringList(formula);  //String转换为List
        result = getPostOrder(result);   //中缀变后缀
        System.out.println("后缀表达式"+result);
        int res = calculate(result);   //计算
        return res;
    }


    public static ArrayList<String> getStringList(String str){
        ArrayList<String> result = new ArrayList<String>();
        String num = "";
        for (int i = 0; i < str.length(); i++) {
            if(Character.isDigit(str.charAt(i))){
                num = num + str.charAt(i);
            }else{
                if(num != ""){
                    result.add(num);
                }
                result.add(str.charAt(i) + "");
                num = "";
            }
        }
        if(num != ""){
            result.add(num);
        }
        return result;
    }

    /**
     * 将中缀表达式转化为后缀表达式
     * @param inOrderList
     * @return
     */
    public static ArrayList<String> getPostOrder(ArrayList<String> inOrderList){

        ArrayList<String> result = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < inOrderList.size(); i++) {
            if(Character.isDigit(inOrderList.get(i).charAt(0))){
                result.add(inOrderList.get(i));
            }else{
                switch (inOrderList.get(i).charAt(0)) {
                    case '(':
                        stack.push(inOrderList.get(i));
                        break;
                    case ')':
                        while (!stack.peek().equals("(")) {
                            result.add(stack.pop());
                        }
                        stack.pop();
                        break;
                    default:
                        while (!stack.isEmpty() && compare(stack.peek(), inOrderList.get(i))){
                            result.add(stack.pop());
                        }
                        stack.push(inOrderList.get(i));
                        break;
                }
            }
        }
        while(!stack.isEmpty()){
            result.add(stack.pop());
        }
        return result;
    }

    /**
     * 计算后缀表达式
     * @param postOrder
     * @return
     */
    public static Integer calculate(ArrayList<String> postOrder){
        Stack stack = new Stack();
        for (int i = 0; i < postOrder.size(); i++) {
            if(Character.isDigit(postOrder.get(i).charAt(0))){
                stack.push(Integer.parseInt(postOrder.get(i)));
            }else{
                Integer back = (Integer)stack.pop();
                Integer front = (Integer)stack.pop();
                Integer res = 0;
                switch (postOrder.get(i).charAt(0)) {
                    case '+':
                        res = front + back;
                        break;
                    case '-':
                        res = front - back;
                        break;
                    case '*':
                        res = front * back;
                        break;
                    case '/':
                        res = front / back;
                        break;
                    case '%':
                        res = front % back;
                        break;
                }
                stack.push(res);
            }
        }
        return (Integer)stack.pop();
    }

    /**
     * 比较运算符等级
     * @param peek
     * @param cur
     * @return
     */
    public static boolean compare(String peek, String cur){
        if("*".equals(peek) && ("/".equals(cur) || "*".equals(cur) ||"+".equals(cur) ||"-".equals(cur))){
            return true;
        }else if("/".equals(peek) && ("/".equals(cur) || "*".equals(cur) ||"+".equals(cur) ||"-".equals(cur))){
            return true;
        }else if("%".equals(peek) && ("/".equals(cur) || "*".equals(cur) ||"+".equals(cur) ||"-".equals(cur))){
            return true;
        }else if("+".equals(peek) && ("+".equals(cur) || "-".equals(cur))){
            return true;
        }else if("-".equals(peek) && ("+".equals(cur) || "-".equals(cur))){
            return true;
        }
        return false;
    }
}
