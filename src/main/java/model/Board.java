// Java
package main.java.model;

import main.java.test.Testable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board implements Testable {

    private final int PK;
    private int minesLeft;
    private Tile[][] tiles;
    private static final Random RANDOM = new Random();
    private final GameDifficulty gameDifficulty;
    private boolean turn;
    private GameSession gameSession;
    private TurnListener turnListener;
    private MinesLeftListener minesLeftListener;

    private Board(GameDifficulty gameDifficulty) {
        this.PK = RANDOM.nextInt(99999999);
        this.tiles = new Tile[gameDifficulty.getRows()][gameDifficulty.getCols()];
        this.minesLeft = gameDifficulty.getMineCount();
        this.gameDifficulty = gameDifficulty;
        this.tiles = populateBoard();
        this.turn = false;
        this.gameSession = GameSession.getInstance();
    }

    private Tile[][] populateBoard() {
        BoardGenerator boardGenerator = new BoardGenerator(this.gameDifficulty);
        int seed = RANDOM.nextInt(); // use a fresh random seed per board
        Tile[][] tiles = boardGenerator.generateValidBoard(seed);

        //give the tiles their parent board
        for(Tile[] row : tiles) {
            for(Tile tile : row) {
                tile.setParentBoard(this);
            }
        }
        return tiles;
    }

    // we might want to implement a factory design pattern here
    public static Board createNewBoard(GameDifficulty gameDifficulty){
        return new Board(gameDifficulty);
    }
    public int reveal(Tile tile) {
        if(tile.isRevealed())
            return 0;
        if(tile.isFlagged())
            return 0;
        System.out.println("Revealing tile " + tile);

        if (tile instanceof MineTile){
            setMinesLeft(getMinesLeft() -1);
            tile.setIsRevealed(true);
            return 1;
        }
        if (tile instanceof NumberTile){
            if(((NumberTile) tile).getAdjacentMines() == 0){
                System.out.println("cascading " + tile);
                return this.cascade(tile);
            }
            else{
                tile.setIsRevealed(true);
                return 1;
            }
        }
        return 0;
    }

    protected void revealAll()
    {
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getCols(); c++) {
                Tile tile = this.getTiles()[r][c];
                tile.forceReveal();
                if (tile instanceof SpecialTile specialTile) {specialTile.setUsed();}
            }
        }
    }

    protected void revealRandomMine()
    {
        if (getMinesLeft() == 0)
        {
            System.out.println("No mines left to reveal");
            return;
        }
        int row,col;
        Tile tile;
        do {
            row = RANDOM.nextInt(getRows());
            col = RANDOM.nextInt(getCols());
            tile = this.getTiles()[row][col];
        }while (!(tile instanceof MineTile) || tile.isRevealed());
        System.out.println("Revealing random mine at (" + (row+1) + "," + (col+1)+")");
        reveal(tile);

    }

    protected void revealGrid()
    {
        if(this.getCols()<3||this.getRows()<3)
        {
            System.out.println("Cannot reveal grid, board too small");
            return;
        }
        List<int[]> candidates = new ArrayList<>();
        int minUnrevealedCount = 0;
        for (int r=0;r<=this.getRows()-3;r++)
            for (int c=0;c<=this.getCols()-3;c++)
            {
                int unRevealedCount = 0;
                boolean hasRevealedTile = false;
                for (int i=0;i<3;i++)
                    for (int j=0;j<3;j++)
                    {
                        Tile tile = this.getTiles()[r+i][c+j];
                        if (!tile.isRevealed())
                        {
                            unRevealedCount++;
                            hasRevealedTile = true;
                        }
                    }
                if (hasRevealedTile)
                {
                    candidates.add(new int[]{r,c,unRevealedCount});
                    minUnrevealedCount = Math.max(minUnrevealedCount,unRevealedCount);
                }
            }
        if (candidates.isEmpty())
        {
            System.out.println("No grid found");
            return;
        }
        List<int[]> optimalCandidates = new ArrayList<>();
        for (int[] candidate:candidates)
        {
            int unrevealed = candidate[2];
            if (unrevealed == minUnrevealedCount) optimalCandidates.add(candidate);
        }
        if (optimalCandidates.isEmpty())
            optimalCandidates = candidates;
        int randomIndex = RANDOM.nextInt(optimalCandidates.size());
        int[] optimalCandidate = optimalCandidates.get(randomIndex);
        int r = optimalCandidate[0];
        int c = optimalCandidate[1];
        System.out.println("Revealing "+minUnrevealedCount+" tiles in grid at ("+(r+1)+","+(c+1)+")");
        for (int i=0;i<3;i++)
            for (int j=0;j<3;j++)
            {
                Tile tile = this.getTiles()[r+i][c+j];
                if (tile instanceof MineTile&&!tile.isRevealed()) setMinesLeft(getMinesLeft() -1);
                tile.forceReveal();
            }


    }

    protected void flag(Tile tile)
    {
        if(tile.isRevealed()) {

            System.out.println("Cannot flag a revealed tile");
            return;
        }
        if(tile.isFlagged()) {
            System.out.println("Cannot flag a flagged tile, umflagging");
            this.unflag(tile);
            return;
        }
        if (tile instanceof MineTile) {
            System.out.println("Cannot flag a mine tile");
            reveal(tile);
            return;
        }
        System.out.println("Flagging tile: " + tile);


        tile.setIsFlagged(true);

    }
    protected void unflag(Tile tile)
    {
        System.out.println("Unflagging tile: " + tile);
        tile.setIsFlagged(false);
    }
    private int cascade(Tile tile) {
        Cascader cascader = new Cascader(tile,this.tiles);
        int revealedCount = 0;
        for( Tile t : cascader.getTilesToReveal()){
            t.setIsRevealed(true);
            revealedCount++;
        }
        return revealedCount;
    }

    protected boolean allMinesRevealed()
    {
        return getMinesLeft() == 0;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public int getRows() {
        return (tiles == null) ? 0 : tiles.length;
    }

    public int getCols() {
        return (tiles == null || tiles.length == 0) ? 0 : tiles[0].length;
    }

    public boolean getTurn(){
        return this.turn;
    }
    public void setTurn(boolean turn){
        if (this.getTurn() == turn) return;  // no state change

        this.turn = turn;
        if (turnListener != null) {
            turnListener.updateTurn();
        }
    }
    public void setTurnListener(TurnListener turnListener) {
        this.turnListener = turnListener;
    }
    private void setMinesLeft(int minesLeft){
        this.minesLeft = minesLeft;
        if(minesLeftListener  != null) {
            minesLeftListener.updateMinesLeft(getMinesLeft());
        }
    }
    public int getMinesLeft(){
        return this.minesLeft;
    }

    @Override
    public boolean runClassTests() {
        return false;
    }
}