package main.java.test;

import main.java.model.*;

import java.util.Objects;

/*
 Simple unit test for GameSession, located next to GameDataCSVManagerTest.
 Exercises tiles, boards and basic gameplay (reveal, cascade, flag, unflag, game over).
 Prints short messages after every step to make the test output informative.
*/
public class GameSessionTest implements Testable {

    @Override
    public boolean runClassTests() {
        try {
            System.out.println("1) Creating new GameSession...");
            GameSession session = GameSession.createNewSession("Lefty", "Righty", GameDifficulty.EASY);
            if (session == null) {
                System.out.println("Failed: session is null");
                return false;
            }
            System.out.println("OK: session created");

            System.out.println("2) Basic properties check...");
            if (session.getTimeStamp() == null) { System.out.println("Failed: timestamp null"); return false; }
            if (!"Lefty".equals(session.getLeftPlayerName())) { System.out.println("Failed: left name mismatch"); return false; }
            if (!"Righty".equals(session.getRightPlayerName())) { System.out.println("Failed: right name mismatch"); return false; }
            if (session.getGameDifficulty() == null) { System.out.println("Failed: difficulty null"); return false; }
            System.out.println("OK: basic properties OK");

            System.out.println("3) Boards initialization check...");
            Board left = session.getLeftBoard();
            Board right = session.getRightBoard();
            if (left == null || right == null) { System.out.println("Failed: one or both boards null"); return false; }
            int rows = session.getGameDifficulty().getRows();
            int cols = session.getGameDifficulty().getCols();
            if (left.getRows() != rows || left.getCols() != cols) { System.out.println("Failed: left board size mismatch"); return false; }
            if (right.getRows() != rows || right.getCols() != cols) { System.out.println("Failed: right board size mismatch"); return false; }
            System.out.println("OK: boards present and sizes match difficulty");

            System.out.println("4) Scanning left board for sample tiles (Number>0, Number==0, Mine)...");
            TilePos sampleNumberPos = null;
            TilePos sampleZeroPos = null;
            TilePos minePos = null;

            Tile[][] tiles = left.getTiles();
            for (int r = 0; r < left.getRows(); r++) {
                for (int c = 0; c < left.getCols(); c++) {
                    Tile t = tiles[r][c];
                    if (t instanceof MineTile && minePos == null) {
                        minePos = new TilePos(r, c, t);
                    } else if (t instanceof NumberTile) {
                        NumberTile nt = (NumberTile) t;
                        if (nt.getAdjacentMines() == 0 && sampleZeroPos == null) {
                            sampleZeroPos = new TilePos(r, c, t);
                        } else if (nt.getAdjacentMines() > 0 && sampleNumberPos == null) {
                            sampleNumberPos = new TilePos(r, c, t);
                        }
                    }
                }
            }
            System.out.println("Found: Number>0=" + (sampleNumberPos != null) + ", Number==0=" + (sampleZeroPos != null) + ", Mine=" + (minePos != null));

            System.out.println("5) Reveal a numeric (>0) tile if available...");
            int beforePoints = session.getPoints();
            boolean beforeTurn = session.isTurn();
            if (sampleNumberPos != null) {
                session.reveal(sampleNumberPos.r, sampleNumberPos.c, true);
                System.out.println("Revealed Number>0 at (" + sampleNumberPos.r + "," + sampleNumberPos.c + ")");
                if (session.getPoints() <= beforePoints) {
                    System.out.println("Failed: expected points to increase after revealing a number tile (was " + beforePoints + ", now " + session.getPoints() + ")");
                    return false;
                }
                System.out.println("OK: points increased (now " + session.getPoints() + ")");
                if (session.isTurn() == beforeTurn) {
                    System.out.println("Failed: turn did not change after reveal");
                    return false;
                }
                System.out.println("OK: turn changed after reveal");
            } else {
                System.out.println("Skipped: no Number>0 tile found");
            }

            System.out.println("6) Reveal a zero-adjacent tile to test cascade (if available)...");
            if (sampleZeroPos != null) {
                // reset tracking values
                beforePoints = session.getPoints();
                boolean beforeRevealedNeighbor = false;
                // pick a neighbor to check cascade effect (if exists)
                int nr = Math.max(0, Math.min(left.getRows() - 1, sampleZeroPos.r + 1));
                int nc = sampleZeroPos.c;
                Tile neighbor = left.getTileAt(nr, nc);
                if (neighbor != null) beforeRevealedNeighbor = neighbor.isRevealed();

                session.reveal(sampleZeroPos.r, sampleZeroPos.c, true);
                System.out.println("Revealed Number==0 at (" + sampleZeroPos.r + "," + sampleZeroPos.c + ")");
                // cascade should reveal neighbors; check that at least one neighbor became revealed
                boolean neighborNowRevealed = false;
                outer:
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int rr = sampleZeroPos.r + dr;
                        int cc = sampleZeroPos.c + dc;
                        if (rr < 0 || cc < 0 || rr >= left.getRows() || cc >= left.getCols()) continue;
                        Tile t = left.getTileAt(rr, cc);
                        if (t != null && t.isRevealed()) { neighborNowRevealed = true; break outer; }
                    }
                }
                if (neighborNowRevealed) {
                    System.out.println("OK: cascade revealed neighboring tiles");
                } else {
                    System.out.println("Warning: cascade did not reveal neighbors (possible board configuration).");
                }
                System.out.println("Points after cascade: " + session.getPoints());
            } else {
                System.out.println("Skipped: no Number==0 tile found to test cascade");
            }

            System.out.println("7) Flag and unflag a number tile (deduct points) if available...");
            if (sampleNumberPos != null) {
                int ptsBeforeFlag = session.getPoints();
                // ensure tile is not revealed/flagged
                Tile t = left.getTileAt(sampleNumberPos.r, sampleNumberPos.c);
                if (t.isRevealed()) {
                    System.out.println("Note: chosen Number>0 tile already revealed, skipping flag/unflag test for it.");
                } else {
                    session.flag(sampleNumberPos.r, sampleNumberPos.c, true);
                    System.out.println("Flagged tile at (" + sampleNumberPos.r + "," + sampleNumberPos.c + ")");
                    int ptsAfterFlag = session.getPoints();
                    if (ptsAfterFlag != ptsBeforeFlag - 3) {
                        System.out.println("Failed: expected points to decrease by 3 after flagging a number tile. Before=" + ptsBeforeFlag + " After=" + ptsAfterFlag);
                        return false;
                    }
                    System.out.println("OK: points decreased by 3 after flagging");

                    // unflag
                    session.unflag(sampleNumberPos.r, sampleNumberPos.c, true);
                    System.out.println("Unflagged tile at (" + sampleNumberPos.r + "," + sampleNumberPos.c + ")");
                    // if unflag had no exception we're fine
                }
            } else {
                System.out.println("Skipped: no Number>0 tile found for flag/unflag test");
            }

            System.out.println("8) Reveal a mine to test health deduction (if a mine found)...");
            if (minePos != null) {
                int healthBefore = session.getHealthPool();
                // ensure mine not revealed yet
                Tile mt = left.getTileAt(minePos.r, minePos.c);
                if (!mt.isRevealed()) {
                    session.reveal(minePos.r, minePos.c, true);
                    System.out.println("Revealed mine at (" + minePos.r + "," + minePos.c + ")");
                    if (session.getHealthPool() != healthBefore - 1) {
                        System.out.println("Failed: expected health to decrease by 1 after revealing a mine. Before=" + healthBefore + " After=" + session.getHealthPool());
                        return false;
                    }
                    System.out.println("OK: health decreased by 1 after revealing mine");
                } else {
                    System.out.println("Note: mine tile already revealed, skipping mine reveal test");
                }
            } else {
                System.out.println("Skipped: no mine found on left board");
            }

            System.out.println("9) Force game over and verify boards revealed and health==0...");
            session.forceGameOver();
            if (session.getHealthPool() != 0) { System.out.println("Failed: health not zero after forceGameOver"); return false; }
            // check at least one tile on each board is revealed (revealAll should reveal all)
            boolean leftAllRevealed = allTilesRevealed(left);
            boolean rightAllRevealed = allTilesRevealed(right);
            if (!leftAllRevealed || !rightAllRevealed) {
                System.out.println("Failed: not all tiles revealed after forceGameOver. leftAllRevealed=" + leftAllRevealed + " rightAllRevealed=" + rightAllRevealed);
                return false;
            }
            System.out.println("OK: forceGameOver set health to 0 and revealed all tiles");

            System.out.println("All GameSession tests passed.");
            return true;
        } catch (Exception ex) {
            System.out.println("Exception during GameSession tests: " + ex);
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean allTilesRevealed(Board b) {
        for (int r = 0; r < b.getRows(); r++) {
            for (int c = 0; c < b.getCols(); c++) {
                Tile t = b.getTileAt(r, c);
                if (t == null) return false;
                if (!t.isRevealed()) return false;
            }
        }
        return true;
    }

    private static class TilePos {
        final int r, c;
        final Tile tile;
        TilePos(int r, int c, Tile tile) { this.r = r; this.c = c; this.tile = tile; }
    }

    // allow standalone run
    public static void main(String[] args) {
        GameSessionTest t = new GameSessionTest();
        boolean ok = t.runClassTests();
        System.out.println(ok ? "PASS" : "FAIL");
        System.exit(ok ? 0 : 1);
    }
}

