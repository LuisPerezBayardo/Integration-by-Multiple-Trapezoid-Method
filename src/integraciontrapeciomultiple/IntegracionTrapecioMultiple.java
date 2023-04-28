package integraciontrapeciomultiple;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Integer.parseInt;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;



public class IntegracionTrapecioMultiple extends JFrame implements ActionListener{
    
    static Interpreter I;
    
    JLabel lblFuncion, lblLimitU, lblLimitD, lblResultado, lblN;
    JTextArea taFuncion, taLimitU, taLimitD, taResultado, taN, TA;
    JButton submit;
    
    double upLimit, downLimit;
    Boolean error=false;
    final double e=2.71828182846, pi=3.14159265359;
    String fx;
    
    
    public IntegracionTrapecioMultiple(){
        taFuncion=new JTextArea();
        taFuncion.setVisible(true);
        taFuncion.setBounds(80, 15, 2000, 25);
        taFuncion.setBackground(Color.WHITE);
        taFuncion.setEditable(true);
        taFuncion.setText("x");
        this.add(taFuncion);
        lblFuncion=new JLabel();
        lblFuncion.setVisible(true);
        lblFuncion.setBounds(0, 15, 70, 25);
        lblFuncion.setText("f(x)=");
        this.add(lblFuncion);
        taLimitU=new JTextArea();
        taLimitU.setVisible(true);
        taLimitU.setBounds(80, 50, 500, 25);
        taLimitU.setBackground(Color.WHITE);
        taLimitU.setEditable(true);
        taLimitU.setText("10");
        this.add(taLimitU);
        lblLimitU=new JLabel();
        lblLimitU.setVisible(true);
        lblLimitU.setBounds(0, 50, 500, 25);
        lblLimitU.setText("Up limit");
        this.add(lblLimitU);
        taN=new JTextArea();
        taN.setVisible(true);
        taN.setBounds(830, 50, 100, 25);
        taN.setBackground(Color.WHITE);
        taN.setEditable(true);
        taN.setText("10");
        this.add(taN);
        lblN=new JLabel();
        lblN.setVisible(true);
        lblN.setBounds(800, 50, 30, 25);
        lblN.setText("n");
        this.add(lblN);
        taLimitD=new JTextArea();
        taLimitD.setVisible(true);
        taLimitD.setBounds(80, 80, 500, 25);
        taLimitD.setBackground(Color.WHITE);
        taLimitD.setEditable(true);
        taLimitD.setText("0");
        this.add(taLimitD);
        lblLimitD=new JLabel();
        lblLimitD.setVisible(true);
        lblLimitD.setBounds(0, 80, 70, 25);
        lblLimitD.setText("Down limit");
        this.add(lblLimitD);
        taResultado=new JTextArea();
        taResultado.setVisible(true);
        taResultado.setBounds(80, 150, 500, 25);
        taResultado.setBackground(Color.YELLOW);
        taResultado.setEditable(true);
        this.add(taResultado);
        lblResultado=new JLabel();
        lblResultado.setVisible(true);
        lblResultado.setBounds(0, 150, 100, 25);
        lblResultado.setText("Result");
        this.add(lblResultado);
        submit=new JButton();
        submit.setVisible(true);
        submit.setText("Submit");
        submit.setBounds(450, 680, 100, 30);
        submit.addActionListener(this);
        this.add(submit);
        TA=new JTextArea();
        TA.setVisible(true);
        TA.setBounds(0, 190, 2000, 470);
        TA.setBackground(Color.WHITE);
        TA.setEditable(false);
        this.add(TA);
        /*************/
        this.setBounds(0,0,2000,2000);
        this.setBackground(Color.cyan);
        this.setResizable(false);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public static void main(String[] args) {
        IntegracionTrapecioMultiple ITM=new IntegracionTrapecioMultiple();
        I=new Interpreter();
        ITM.show();
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int i, n;
        double up, down, intervalo, sum=0;
        I.functionSplit(taFuncion.getText());
        if(!I.error){
            up=Double.valueOf(taLimitU.getText());
            down=Double.valueOf(taLimitD.getText());
            n=parseInt(taN.getText());
            intervalo=((up-down)/n);
            TA.setText("");
            for(i=1; i<n; i++){
                sum+=I.function(down+(intervalo*i));
                outWriter("\nf(i="+String.valueOf(i)+"): "+String.valueOf(sum));
            }
            sum*=2;
            outWriter("\nf(x)*=2: "+String.valueOf(sum));
            sum+=I.function(down);
            outWriter("\nf(x)+=f("+String.valueOf(down)+"): "+String.valueOf(sum)+"\t\t(down)");
            sum+=I.function(up);
            outWriter("\nf(x)+=f("+String.valueOf(up)+"): "+String.valueOf(sum)+"\t(up)");
            sum*=intervalo/2;
            outWriter("\n"+String.valueOf(sum));
            taResultado.setText(String.valueOf(sum));
            
            for(i=0; i<I.fx.size(); i++) System.out.println(I.fx.get(i));
            outWriter("\n\n\n\nValues of f(up limit) and f(down limit)\n\nf("+up+")="+String.valueOf(I.function(up)));
            outWriter("\nf("+down+")="+String.valueOf(I.function(down)));
        }
        else taResultado.setText("Syntax error");
    }
    
    
    double funcion(double x){
        return x;
    }
    
    
    
    void outWriter(String s){
        TA.setText(TA.getText()+s);
    }
}
