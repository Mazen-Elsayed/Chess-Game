/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package frontend;

import ChessCore.GameStatus;
import ChessCore.Square;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author EXPERTS
 */
public interface ClickObserver {
    public void firstClick(List<Square> validMoves, JButton clickedButton);
    public void secondClick(Square selectedSquare, Square targetSquare, int targetRow, int targetCol, JButton clickedButton);
    public void invalidMove();
    public void handleGameStatus(GameStatus status);
}
