/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch (op.getId()) 
        {
            case Token.LPAREN:
                return 0;  
            case Token.PLUS:
            case Token.MINUS:
                return 1;
            case Token.MULT:
            case Token.DIV:
            case Token.MOD:
                return 2;
            case Token.EXP:       
                return 3; 
            default:
                return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        double a = this.operandos.pop();
        double b = this.operandos.pop();

        switch (op.getId()) 
        {
            case Token.PLUS: 
                operandos.push(a + b); 
                break;
            case Token.MINUS: 
                operandos.push(a - b); 
                break;
            case Token.MULT: 
                operandos.push(a * b); 
                break;
            case Token.DIV: 
                operandos.push(a / b);
                break;
            case Token.MOD: 
                operandos.push(a % b); 
                break;
            case Token.EXP: 
                operandos.push(Math.pow(a, b)); 
                break;
        }

        /* El codigo de esta seccion se explicara en clase */
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */

        if (op.getId() == Token.LPAREN) 
        {
            operadores.push(op);
            return;
        }

        if (op.getId() == Token.RPAREN) 
        {
            while (!operadores.empty() && operadores.peek().getId() != Token.LPAREN) 
            {
                popOp();
            }
            if (!operadores.empty() && operadores.peek().getId() == Token.LPAREN) 
            {
                operadores.pop();
            }
            return;
        }

        while ((!operadores.empty()) && (pre(op) <= pre(operadores.peek())) && (operadores.peek().getId() != Token.LPAREN)) 
        {
            popOp();
        }
        operadores.push(op);

        /* Casi todo el codigo para esta seccion se vera en clase */
    	
    	// Si no hay operandos automaticamente ingresamos op al stack

    	// Si si hay operandos:
    		// Obtenemos la precedencia de op
        	// Obtenemos la precedencia de quien ya estaba en el stack
        	// Comparamos las precedencias y decidimos si hay que operar
        	// Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
        	// Al terminar operaciones pendientes, guardamos op en stack

    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() 
    {
        if (G()) 
        {
            return F();
        }
        return false;
    }

    /* TODO: sus otras funciones aqui */

    private boolean F()
    {
        if (term(Token.PLUS)) 
        {
            if (G()) 
            {
                return F();
            }
            return false;
        } 
        else if (term(Token.MINUS)) 
        {
            if (G()) 
            {
                return F();
            }
            return false;
        }
        return true;
    }

    private boolean G()
    {
        if (I()) 
        {
            return H();
        }
        return false;
    }

    private boolean H()
    {
        if (term(Token.MULT)) 
        {
            if (I()) 
            {
                return H();
            }
            return false;
        } 
        else if (term(Token.DIV)) 
        {
            if (I()) 
            {
                return H();
            }
            return false;
        } 
        else if (term(Token.MOD)) 
        {
            if (I()) 
            {
                return H();
            }
            return false;
        }
        return true; 
    }

    private boolean I()
    {
        if (K()) 
        {
            return J();
        }
        return false;
    }

    private boolean J()
    {
        if (term(Token.EXP)) 
        {
            if (K()) 
            {
                return J();
            }
            return false;
        }
        return true; 
    }

    private boolean K()
    {
        if (term(Token.MINUS)) 
        {
            return K();
        } 
        else if (term(Token.LPAREN)) 
        {
            if (E()) 
            {
                return term(Token.RPAREN);
            }
            return false;
        } 
        else if (term(Token.NUMBER)) 
        {
            return true;
        }
        return false;
    }
}
