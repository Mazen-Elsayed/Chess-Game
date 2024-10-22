/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessCore;

/**
 *
 * @author EXPERTS
 */
public class Memento {
    private ChessBoard board;

    public Memento(ChessBoard board) {
        this.board = new ChessBoard(board);
    }

    public ChessBoard getBoard() {
        return board;
    }
    
}
