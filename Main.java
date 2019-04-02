


/**

 This class is a lexical analyzer for the tokens defined by the grammar:

 <plus> --> +
 <minus> --> -
 <times> --> *
 <div> --> /
 <LParen> --> "("
 <RParen> --> ")"
 <int> --> { <digit> }+
 <id> --> <letter> { <letter> | <digit> }
 <float> --> { <digit> }+ "." { <digit> }+
 <floatE> --> <float> (E|e) [+|-] { <digit> }+

 This class implements a DFA that will accept the above tokens.

 The DFA states are represented by the Enum type "State".
 The DFA has the following 10 final states represented by enum-type literals:

 state     token accepted

 Id        identifiers
 Int       integers
 Float     floats without exponentiation part
 FloatE    floats with exponentiation part
 Plus      +
 Minus     -
 Times     *
 Div       /
 LParen    (
 RParen    )

 The DFA also uses the following 4 non-final states:

 state      string recognized

 Start      the empty string
 Period     float parts ending with "."
 E          float parts ending with E or e
 EPlusMinus float parts ending with + or - in exponentiation part

 The function "driver" operates the DFA.
 The array "nextState" returns the next state given the current state and the input character.

 To recognize a different token set, modify the following:

 enum type "State" and function "isFinal"
 size of array "nextState"
 function "setNextState"

 The functions "driver", "getToken", "setLex" remain the same.

 **/


abstract class LexArithArray extends IO
{
    public enum State
    {
        // non-final states     ordinal number
        Start,             // 0
        Period,            // 1
        E,                 // 2
        EPlusMinus,        // 3

        //final states

        Id,                // 4
        Int,               // 5
        Float,             // 6
        FloatE,            // 7
        Plus,              // 8
        Minus,             // 9
        Times,             // 10
        Div,               // 11
        LParen,            // 12
        RParen,            // 13
        LBrace,            // 14
        RBrace,            // 15
        LBracket,          // 16
        RBracket,          // 17
        semicolon,         // 18
        comma,             // 19
        inv,               // 20
// I have implemented a method for eq,Assign,gt,lt,ge,le,and,or and
// other keyWords (if, for, while,nextValue,print,new)


        UNDEF;




        private boolean isFinal()
        {
            return ( this.compareTo(State.Id) >= 0 );
        }
    }

    // By enumerating the non-final states first and then the final states,
    // test for a final state can be done by testing if the state's ordinal number
    // is greater than or equal to that of Id.

    // The following variables of "IO" class are used:

    //   static int a; the current input character
    //   static char c; used to convert the variable "a" to the char type whenever necessary

    public static String t; // holds an extracted token
    public static State state; // the current state of the FA

    private static State nextState[][] = new State[21][128];

    // This array implements the state transition function State x (ASCII char set) --> State.
    // The state argument is converted to its ordinal number used as
    // the first array index from 0 through 13.

    private static int driver()

    // This is the driver of the FA.
    // If a valid token is found, assigns it to "t" and returns 1.
    // If an invalid token is found, assigns it to "t" and returns 0.
    // If end-of-stream is reached without finding any non-whitespace character, returns -1.

    {
        State nextSt; // the next state of the FA

        t = "";
        state = State.Start;

        if ( Character.isWhitespace((char) a) )
            a = getChar(); // get the next non-whitespace character
          //  t = t + c;
        if ( a == -1 ) // end-of-stream is reached
            return -1;



        while ( a != -1 ) // do the body if "a" is not end-of-stream
        {
            c = (char) a;
            char b;
            //System.out.print(c);

            nextSt = nextState[state.ordinal()][a];
            if ( nextSt == State.UNDEF ) // The FA will halt.
            {
                if (state.isFinal()) {
                    return 1; // valid token extracted
                } else // "c" is an unexpected character
                {

                    t = t + c;
                    a =  getNextChar();
                    b=(char)a;
                    if (c=='=' ){ //if the Char read is "="
                        if( b=='=') { //eq "if "==" only will print "eq"
                            t = "";
                            t = c + "" + b;

                            //System.out.println("okay " + t);
                            a =  getNextChar();


                            return 2;
                        }else {  //if the first char is "=" will print "Assign"
                            //System.out.println("jk" );
                            return 7;
                        }

                    } else if (c=='<'){ //if the first char is "<" will print "lt"->lessThan
                        if(b=='=') { //le,  if the consecutive char is "=" its <= lessthatn or euqal "le"

                            t = "";
                            t = c + "" + b;
                            //System.out.println("okay1 " + t);
                            a =  getNextChar();

                            return 3;
                        }else return 8;
                    } else if (c=='>'){//if the first char is ">" will print "lt"->greaterThan
                        if (b=='=') { //ge,  if the consecutive char is "=" its <= greaterThan or euqal "ge"

                        t="";
                        t=c+""+b;
                            a =  getNextChar();

                        return 4;}
                        else return 9;

                    } else if (c==('|') && b=='|') { //if both char read equals "||" will print "or"

                        t="";
                        t=c+""+b;
                        a =  getNextChar();

                        return 5;
                    } else if (c==('&') && b=='&') { //and , if both char read equals "&&" will print "and"
                        t="";
                        t=c+""+b;
                        a =  getNextChar();
                        //System.out.println("after"+(char)a);

                        return 6;
                    }



                     return 0; // invalid token found
                    }

            }

            else // The FA will go on.
            {
                state = nextSt;
                t = t+c;

                a = getNextChar();
            }
        }

        // end-of-stream is reached while a token is being extracted

        if ( state.isFinal() )
            return 1; // valid token extracted
        else
            return 0; // invalid token found
    } // end driver

    public static void getToken()

    // Extract the next token using the driver of the FA.
    // If an invalid token is found, issue an error message.

    {
        int i = driver();
        if ( i == 0 )
            displayln(t + " : Lexical Error, invalid token");

    }


    private static void setNextState()
    {
        for (int s = 0; s <= 20; s++ )
            for (int c = 0; c <= 127; c++ )
                nextState[s][c] = State.UNDEF;

        for (char c = 'A'; c <= 'Z'; c++)
        {
            nextState[State.Start.ordinal()][c] = State.Id;
            nextState[State.Id   .ordinal()][c] = State.Id;
        }

        for (char c = 'a'; c <= 'z'; c++)
        {
            nextState[State.Start.ordinal()][c] = State.Id;
            nextState[State.Id   .ordinal()][c] = State.Id;
        }

        for (char d = '0'; d <= '9'; d++)
        {
            nextState[State.Start     .ordinal()][d] = State.Int;
            nextState[State.Id        .ordinal()][d] = State.Id;
            nextState[State.Int       .ordinal()][d] = State.Int;
            nextState[State.Period    .ordinal()][d] = State.Float;
            nextState[State.Float     .ordinal()][d] = State.Float;
            nextState[State.E         .ordinal()][d] = State.FloatE;
            nextState[State.EPlusMinus.ordinal()][d] = State.FloatE;
            nextState[State.FloatE    .ordinal()][d] = State.FloatE;


        }



        nextState[State.Start.ordinal()]['+'] = State.Plus;
        nextState[State.Start.ordinal()]['-'] = State.Minus;
        nextState[State.Start.ordinal()]['*'] = State.Times;
        nextState[State.Start.ordinal()]['/'] = State.Div;
        nextState[State.Start.ordinal()]['('] = State.LParen;
        nextState[State.Start.ordinal()][')'] = State.RParen;
        nextState[State.Start.ordinal()]['{'] = State.LBrace;
        nextState[State.Start.ordinal()]['}'] = State.RBrace;
        nextState[State.Start.ordinal()]['['] = State.LBracket;
        nextState[State.Start.ordinal()][']'] = State.RBracket;
        nextState[State.Start.ordinal()][';'] = State.semicolon;
        nextState[State.Start.ordinal()][','] = State.comma;
        nextState[State.Start.ordinal()]['!'] = State.inv;
        nextState[State.Int.ordinal()]['.'] = State.Period;

        nextState[State.Float.ordinal()]['E'] = State.E;
        nextState[State.Float.ordinal()]['e'] = State.E;

        nextState[State.E.ordinal()]['+'] = State.EPlusMinus;
        nextState[State.E.ordinal()]['-'] = State.EPlusMinus;


    } // end setNextState

    public static void setLex()

    // Sets the nextState array.

    {
        setNextState();
    }

    public static void main(String argv[])

    {
        // argv[0]: input file containing source code using tokens defined above
        // argv[1]: output file displaying a list of the tokens

        setIO( argv[0], argv[1] );
        setLex();

        int i;

        while ( a != -1 ) // while "a" is not end-of-stream
        {
            i = driver(); // extract the next token
            if (i == 1) //I assign all the string as "Id" first and
                        // if it equals to below keyword will print desire keyword
                if (t.equals("while")) {
                    displayln(t + " : keyword_while");
                } else if (t.equals("if")) {
                    displayln(t + " : keyword_if");
                } else if (t.equals("else")) {
                    displayln(t + " : keyword_else");
                } else if (t.equals("returnVal")) {
                    displayln(t + " : keyword_returnVal");
                } else if (t.equals("new")) {
                    displayln(t + " : keyword_new");
                } else if (t.equals("print")) {
                    displayln(t + " : keyword_print");
                }else
                displayln(t + "   : " + state.toString());

            else if (i==2)
                displayln(t + " : eq");
            else if (i==3)
                displayln(t + " : le");
            else if (i==4)
                displayln(t + " : ge");
            else if (i==5)
                displayln(t + " : or");
            else if (i==6)
                displayln(t + " : and");
            else if (i==7)
                displayln(t + " : Assign");
            else if (i==8)
                displayln(t + " : gt");
            else if (i==9)
                displayln(t + " : lt");
            else if ( i == 0 )
                displayln( t+" : Lexical Error, invalid token");
        }

        closeIO();
    }
}


