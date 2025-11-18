package main.java.model;

//Exception for when a move is illegal
public class IllegalMoveException extends RuntimeException
{
    //Constructor
    public IllegalMoveException(String message)
    {
        super("Illegal move! cannot "+message);
    }
}
