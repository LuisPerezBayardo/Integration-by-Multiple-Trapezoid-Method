package integraciontrapeciomultiple;

import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.lang.Math.asin;
import static java.lang.Math.acos;
import static java.lang.Math.atan;
import static java.lang.Math.log;
import static java.lang.Math.log10;

import java.util.ArrayList;
import java.util.List;


public class Interpreter {
    
    String text;
    List<String> fx;
    List<Double> fxNumbers;
    Boolean error;
    final double e=2.71828182846;
    double lastNumber;
    int i, iNumbers, functionCallCounter;
    
    public Interpreter(){
        text=new String();
        fx=new ArrayList<>();
        fxNumbers=new ArrayList<>();
        error=false;
        lastNumber=0;
        i=0;
        iNumbers=0;
        functionCallCounter=0;
    }
    
    
    
    
    void functionSplit(String f){
        /************************/   // Init
        error=false;
        text=f;
        fx.clear();
        int operatorInd=5;  // 0: number, 1: constant or variable, 2: parenthesys begin, 3: parenthesys end, 4: sum or subtraction operator, 5: another operator, 6: trigonometric function or logarithm
        int j;
        String element="";
        /************************/   // Splitting
        for(j=0; j<text.length(); j++){
            if((text.charAt(j)=='s') || (text.charAt(j)=='c') || (text.charAt(j)=='t') || (text.charAt(j)=='l') || (text.charAt(j)=='L') || (text.charAt(j)=='a')){
                if((j+3)<text.length()){
		    if(text.substring(j, (j+4)).equals("sin(") ||
                            text.substring(j, (j+4)).equals("cos(") ||
                            text.substring(j, (j+4)).equals("csc(") ||
                            text.substring(j, (j+4)).equals("sec(") ||
                            text.substring(j, (j+4)).equals("tan(") ||
                            text.substring(j, (j+4)).equals("cot(") ||
                            text.substring(j, (j+4)).equals("log(")){
                        fx.add(text.substring(j, (j+3)));
                        j+=2;
                    }
                    else if(((j+6)<text.length()) && !(text.substring(j, (j+3)).equals("Ln("))){
                        if(text.substring(j, (j+7)).equals("arcsin(") ||
                                text.substring(j, (j+7)).equals("arccos(") ||
                                text.substring(j, (j+7)).equals("arccsc(") ||
                                text.substring(j, (j+7)).equals("arcsec(") ||
                                text.substring(j, (j+7)).equals("arctan(") ||
                                text.substring(j, (j+7)).equals("arccot(")){
                            fx.add(text.substring(j, (j+6)));
                            j+=5;
                        }
                    }
		    else if(text.substring(j, (j+3)).equals("Ln(")){
			fx.add(text.substring(j, (j+2)));
                        j+=1;
		    }
                    else{
                        error=true;
                        j=text.length()+1;
                        if(j==0);
                    }
		}
                else{
                    error=true;
                    j=text.length()+1;
                }
                operatorInd=6;
	    }
            else if(((text.charAt(j)>=48) && (text.charAt(j)<=57))   ||   (text.charAt(j)=='.')){
                element+=text.charAt(j);
                operatorInd=0;
            }
            else if((text.charAt(j)=='(') || (text.charAt(j)==')') || (text.charAt(j)=='+') || (text.charAt(j)=='-') || (text.charAt(j)=='*') || (text.charAt(j)=='/') || (text.charAt(j)=='^') || (text.charAt(j)=='x') || (text.charAt(j)=='e')){
                if(operatorInd==0){
                    fx.add(element);
                    element="";
                }
                fx.add(""+text.charAt(j));
                operatorInd=1;
            }
            else{
                error=true;
                j=text.length()+1;
            }
        }
        if(operatorInd==0 && !error) fx.add(element);
        /************************/   // Error search and signing
        operatorInd=5;
        Boolean sign=false; // false: positive, true: negative
        List<String> fx_=new ArrayList<>();
        if(!error){
            for(j=0; j<fx.size(); j++){
                switch (fx.get(j)) {
                    case "*":
                    case "/":
                    case "^":
                        if(!(operatorInd==0 || operatorInd==1 || operatorInd==3)){
                            error=true;
                            j=fx.size()+1;
                        }
                        else fx_.add(fx.get(j));
                        operatorInd=5;
                        break;
                    case "+":
                    case "-":
                        if(operatorInd==6 || ((j+1)==fx.size())){
                            error=true;
                            j=fx.size()+1;
                        }
                        else if(fx.get(j).equals("-")) sign^=true;
                        if((operatorInd==0 || operatorInd==1 || operatorInd==3) && !error) fx_.add(fx.get(j));
                        else if(!error){
                            if((!(fx.get(j+1).equals("+") || fx.get(j+1).equals("-"))) && sign) fx_.add("_");
                        }   operatorInd=4;
                        break;
                    case "(":
                        if(operatorInd==0 || operatorInd==1 || operatorInd==3){
                            error=true;
                            j=fx.size()+1;
                        }
                        else fx_.add(fx.get(j));
                        operatorInd=2;
                        break;
                    case ")":
                        if(!(operatorInd==0 || operatorInd==1 || operatorInd==3)){
                            error=true;
                            j=fx.size()+1;
                        }
                        else fx_.add(fx.get(j));
                        operatorInd=3;
                        break;
                    default:
                        if(!(operatorInd==2 || operatorInd==4 || operatorInd==5)){
                            error=true;
                            j=fx.size()+1;
                        }
                        else fx_.add(fx.get(j));
                        if(fx.get(j).equals("x") || fx.get(j).equals("e")) operatorInd=1;
                        else if(fx.get(j).charAt(0)<=57) operatorInd=0;
                        else operatorInd=6;
                        break;
                }
            }
            if(!(operatorInd==0 || operatorInd==1 || operatorInd==3)) error=true;
            //for(i=0; i<fx.size(); i++) System.out.println(i+"_: "+fx.get(i));     // Debugging
            //System.out.println("\nfinal:");                                       // Debugging
            fx.clear();
            fx=fx_;
            /************************/   // List for numbers in the function to improve speed and efficiency (lecture of numbers instead of Strings)
            for(j=0; j<fx.size(); j++){
                if(fx.get(j).charAt(0)<=57 && fx.get(j).charAt(0)>=48) fxNumbers.add(Double.valueOf(fx.get(j)));
            }
        }
        i=0;
        functionCallCounter=0;
    }
    
    
    
    
    double function(double x){
        functionCallCounter++;
        //System.out.println("fx.size(): "+fx.size()+"\n"); // Debugging
        Boolean operator=true;
        double result=0;
        while(operator){
            System.out.println("i: "+i+".   (function)"); // Debugging
            switch(fx.get(i)){
                case "(":
                    i++;
                    result=function(x);
                    break;
                case ")":
                    i++;
                    functionCallCounter--;
                    return result;
                case "+":
                    i++;
                    result+=add(x, 1);
                    break;
                case "-":
                    i++;
                    result+=add(x, -1);
                    break;
                case "_": // Negative sign
                    i++;
                    result=-function(x);
                    break;
                case "*":
                    i++;
                    result=mul(x, false, result);
                    break;
                case "/":
                    i++;
                    result=mul(x, true, result);
                    break;
                case "^":
                    i++;
                    result=pow(result, power(x));
                    break;
                case "x":
                    i++;
                    result=x;
                    break;
                case "e":
                    i++;
                    result=e;
                    break;
                default: // Number
                    if(fx.get(i).charAt(0)>57) result=trigonometric_logarithm(x);
                    else{
                        result=fxNumbers.get(iNumbers);
                        iNumbers++;
                    }
                    i++;
                    break;
            }
            if(i>=fx.size()){
                if(functionCallCounter==1){
                    i=0;
                    iNumbers=0;
                }
                functionCallCounter--;
                return result;
            }
        }
        functionCallCounter--;
        return result;
    }
    
    
    
    
    double add(double x, int add_sub){
        double result=0, accumulator=0;
        Boolean stop=false;
        double sign=1; // 1: positive, -1: negative
        while(!stop){
            System.out.println("i: "+i+".   (add)"); // Debugging
            //System.out.println("result: "+result+";     accumulator: "+accumulator+";       sign: "+sign);
            switch(fx.get(i)){
                case "(":
                    i++;
                    accumulator=function(x);
                    break;
                case ")":
                    i++;
                    result+=accumulator*sign;
                    return result;
                case "+":
                    result+=accumulator*sign;
                    sign=1;
                    i++;
                    break;
                case "-":
                    result+=accumulator*sign;
                case "_":
                    sign=-1;
                    i++;
                    break;
                case "*":
                    i++;
                    accumulator=mul(x, false, accumulator);
                    break;
                case "/":
                    i++;
                    accumulator=mul(x, true, accumulator);
                    break;
                case "^":
                    i++;
                    accumulator=pow(accumulator, power(x));
                    break;
                case "x":
                    accumulator=x;
                    i++;
                    break;
                case "e":
                    accumulator=e;
                    i++;
                    break;
                default:
                    System.out.println("i: "+i+".   (add, default)"); // Debugging
                    if(fx.get(i).charAt(0)>57) accumulator=trigonometric_logarithm(x);
                    else{
                        accumulator=fxNumbers.get(iNumbers);
                        iNumbers++;
                    }
                    i++;
                    break;
            }
            if(i>=fx.size()) {
                result+=accumulator*sign;
                stop=true;
            }
        }
        System.out.println("add, return");
        return result;
    }
    
    
    
    
    double mul(double x, Boolean mul_div, double arg){  // mul_div: (false: mul, true: div)
        double result, accumulator=arg, resultUnderWrite=1;
        Boolean stop=false;
        Boolean sign=false; // false: positive, true: negative
        result=accumulator;
        while(!stop){
            switch(fx.get(i)){
                case "(":
                    i++;
                    if(sign)accumulator=-function(x);
                    else accumulator=function(x);
                    resultUnderWrite=result;
                    if(mul_div) result/=accumulator;
                    else result*=accumulator;
                    break;
                case ")":
                    i++;
                    return result;
                case "+":
                case "-":
                    return result;
                case "_":
                    sign=true;
                    i++;
                    break;
                case "*":
                    i++;
                    mul_div=false;
                    sign=false;
                    break;
                case "/":
                    i++;
                    mul_div=true;
                    sign=false;
                    break;
                case "^":
                    i++;
                    accumulator=pow(accumulator, power(x));
                    result=resultUnderWrite/accumulator;
                    resultUnderWrite=result;
                    sign=false;
                    break;
                case "x":
                    if(sign)accumulator=-x;
                    else accumulator=x;
                    resultUnderWrite=result;
                    if(mul_div)result/=accumulator;
                    else result*=accumulator;
                    i++;
                    break;
                case "e":
                    if(sign)accumulator=-e;
                    else accumulator=e;
                    resultUnderWrite=result;
                    if(mul_div) result/=accumulator;
                    else result*=accumulator;
                    i++;
                    break;
                default:
                    if(fx.get(i).charAt(0)>57){
                        if(sign)accumulator=-trigonometric_logarithm(x);
                        else accumulator=trigonometric_logarithm(x);
                    }
                    else{
                        if(sign)accumulator=-fxNumbers.get(iNumbers);
                        else accumulator=fxNumbers.get(iNumbers);
                        iNumbers++;
                    }
                    resultUnderWrite=result;
                    if(mul_div) result/=accumulator;
                    else result*=accumulator;
                    i++;
                    break;
            }
            if(i>=fx.size()) stop=true;
        }
        return result;
    }
    
    
    
    
    double power(double x){
        double accumulator=1;
        Boolean stop=false;
        Boolean sign=false; // false: positive, true: negative
        while(!stop){
            switch(fx.get(i)){
                case "(":
                    i++;
                    if(sign)accumulator=-function(x);
                    else accumulator=function(x);
                    break;
                case ")":
                    i++;
                    return accumulator;
                case "+":
                case "-":
                case "*":
                case "/":
                    return accumulator;
                case "_":
                    sign=true;
                    i++;
                    break;
                case "^":
                    i++;
                    accumulator=pow(accumulator, power(x));
                    sign=false;
                    break;
                case "x":
                    if(sign)accumulator=-x;
                    else accumulator=x;
                    i++;
                    break;
                case "e":
                    if(sign)accumulator=-e;
                    else accumulator=e;
                    i++;
                    break;
                default:
                    if(fx.get(i).charAt(0)>57){
                        if(sign)accumulator=-trigonometric_logarithm(x);
                        else accumulator=trigonometric_logarithm(x);
                    }
                    else{
                        if(sign)accumulator=-fxNumbers.get(iNumbers);
                        else accumulator=fxNumbers.get(iNumbers);
                        iNumbers++;
                    }
                    i++;
                    break;
            }
            if(i>=fx.size()) stop=true;
        }
        return accumulator;
    }
    
    
    
    
    double trigonometric_logarithm(double x){
        i++;
        switch(fx.get(i)){
            case "sin(":
                return sin(function(x));
            case "cos(":
                return cos(function(x));
            case "tan(":
                return tan(function(x));
            case "csc(":
                return 1/sin(function(x));
            case "sec(":
                return 1/cos(function(x));
            case "cot(":
                return 1/cos(function(x));
            case "arcsin(":
                return asin(function(x));
            case "arccos(":
                return acos(function(x));
            case "arctan(":
                return atan(function(x));
            case "arccsc(":
                //return a(1/sin(function(x)));
                return 1;
            case "arcsec(":
                //return a(1/cos(function(x)));
                return 1;
            case "arccot(":
                //return a(1/cos(function(x)));
                return 1;
            case "log(":
                return log10(function(x));
            case "Ln(":
                return log(function(x));
        }
        return 0;
    }
    
}
