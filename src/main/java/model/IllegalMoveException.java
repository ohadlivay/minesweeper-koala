package main.java.model;
//justification for this being in model? -ohad
//Exception for when a move is illegal
public class IllegalMoveException extends RuntimeException
{
    //Constructor
    public IllegalMoveException(String message)
    {
        super("Illegal move! cannot "+message);
    }
}
