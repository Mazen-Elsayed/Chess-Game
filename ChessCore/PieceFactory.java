/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessCore;

import ChessCore.Pieces.Bishop;
import ChessCore.Pieces.King;
import ChessCore.Pieces.Knight;
import ChessCore.Pieces.Pawn;
import ChessCore.Pieces.Piece;
import ChessCore.Pieces.Queen;
import ChessCore.Pieces.Rook;

/**
 *
 * @author EXPERTS
 */
public class PieceFactory {

    public Piece createpiece(String name, Player p) {
        switch (name) {
            case "Bishop" -> {
                return new Bishop(p);
            }
            case "King" -> {
                return new King(p);
            }
            case "Knight" -> {
                return new Knight(p);
            }
            case "Pawn" -> {
                return new Pawn(p);
            }
            case "Queen" -> {
                return new Queen(p);
            }
            case "Rook" -> {
                return new Rook(p);
            }
            default -> {
            }
        }
        return null;
    }
}
