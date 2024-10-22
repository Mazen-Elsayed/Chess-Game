/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frontend;

import ChessCore.BoardFile;
import ChessCore.BoardRank;
import ChessCore.Pieces.Piece;
import ChessCore.ClassicChessGame;
import ChessCore.GameStatus;
import static ChessCore.GameStatus.BLACK_UNDER_CHECK;
import static ChessCore.GameStatus.BLACK_WON;
import static ChessCore.GameStatus.INSUFFICIENT_MATERIAL;
import static ChessCore.GameStatus.STALEMATE;
import static ChessCore.GameStatus.WHITE_UNDER_CHECK;
import static ChessCore.GameStatus.WHITE_WON;
import ChessCore.Move;
import ChessCore.PawnPromotion;
import ChessCore.Pieces.Bishop;
import ChessCore.Pieces.King;
import ChessCore.Pieces.Knight;
import ChessCore.Pieces.Pawn;
import ChessCore.Pieces.Queen;
import ChessCore.Pieces.Rook;
import static ChessCore.Player.BLACK;
import static ChessCore.Player.WHITE;
import ChessCore.Square;
import ChessCore.Utilities;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author EXPERTS
 */
public class Board extends javax.swing.JFrame implements ClickObserver {

    ClassicChessGame game = new ClassicChessGame();
    private Square selectedSquare;
    private final JButton[][] buttons;
    private JButton undoButton;
    int firstclickedRow = -1;
    int firstclickedCol = -1;

    ImageIcon whitebishop = new ImageIcon("WhiteBishop.png");
    ImageIcon blackbishop = new ImageIcon("BlackBishop.png");
    ImageIcon whiterook = new ImageIcon("WhiteRook.png");
    ImageIcon blackrook = new ImageIcon("BlackRook.png");
    ImageIcon whitepawn = new ImageIcon("WhitePawn.png");
    ImageIcon blackpawn = new ImageIcon("BlackPawn.png");
    ImageIcon whitequeen = new ImageIcon("WhiteQueen.png");
    ImageIcon blackqueen = new ImageIcon("BlackQueen.png");
    ImageIcon whiteknight = new ImageIcon("WhiteKnight.png");
    ImageIcon blackknight = new ImageIcon("BlackKnight.png");
    ImageIcon whiteking = new ImageIcon("WhiteKing.png");
    ImageIcon blackking = new ImageIcon("BlackKing.png");

    /**
     * Creates new form Board
     */
    public Board() {
        initComponents();
        buttons = new JButton[8][8];
        initializeButtons();
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeButtons() {
        setLayout(new GridLayout(9, 8));
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                buttons[row][col] = new JButton();
                if ((row + col) % 2 == 0) {
                    buttons[row][col].setBackground(Color.BLACK);
                } else {
                    buttons[row][col].setBackground(Color.WHITE);
                }
                Square square = new Square(getBoardFile(col), getBoardRank(row));
                setButtonIcon(buttons[row][col], square);
                buttons[row][col].addActionListener((ActionEvent e) -> handleButtonClick(e));
                add(buttons[row][col]);
            }
        }
        undoButton = new JButton("Undo");
        undoButton.addActionListener((ActionEvent e) -> frontendUndoMove());
        add(undoButton);
    }

    private void setButtonIcon(JButton button, Square square) {
        Piece piece = game.getPieceAtSquare(square);
        if (piece != null) {
            Icon pieceIcon = getIconForPiece(piece);
            button.setIcon(pieceIcon);
        } else {
            button.setIcon(null);
        }
    }

    private void handleButtonClick(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        int secondclickedRow = -1;
        int secondclickedCol = -1;

        if (selectedSquare == null) {
            outerLoop:
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (buttons[r][c] == clickedButton) {
                        firstclickedRow = r;
                        firstclickedCol = c;
                        break outerLoop;
                    }
                }
            }
            updateButtons();
            selectedSquare = new Square(getBoardFile(firstclickedCol), getBoardRank(firstclickedRow));
            List<Square> arr = game.getAllValidMovesFromSquare(selectedSquare);
            firstClick(arr, clickedButton);
        } else {
            outerLoop:
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (buttons[r][c] == clickedButton) {
                        secondclickedRow = r;
                        secondclickedCol = c;
                        break outerLoop;
                    }
                }
            }
            int targetRow = secondclickedRow;
            int targetCol = secondclickedCol;
            Square targetSquare = new Square(getBoardFile(targetCol), getBoardRank(targetRow));
            secondClick(selectedSquare, targetSquare, targetRow, targetCol, clickedButton);
            selectedSquare = null;
        }
    }

    @Override
    public void firstClick(List<Square> validMoves, JButton clickedButton) {
        for (int i = 0; i < validMoves.size(); i++) {
            int col = validMoves.get(i).getFile().getValue();
            int row = validMoves.get(i).getRank().getValue();
            buttons[row][col].setBackground(Color.BLUE);
        }
    }

    @Override
    public void secondClick(Square selectedSquare, Square targetSquare, int targetRow, int targetCol, JButton clickedButton) {
        Move promotionMove;
        Piece p = game.getPieceAtSquare(selectedSquare);
        if (p instanceof Pawn && ((targetRow == 7 && firstclickedRow == 6) || (targetRow == 0 && firstclickedRow == 1))) {
            System.out.println("true");
            String promote = JOptionPane.showInputDialog("Promotion Move: Choose a Piece to promote to.");
            promotionMove = new Move(selectedSquare, targetSquare, getPromotion(promote));
            game.makeMove(promotionMove);
            GameStatus status = game.getGameStatus();
            updateButtons();
            handleGameStatus(status);
        } else if (game.makeMove(new Move(selectedSquare, targetSquare))) {
            GameStatus status = game.getGameStatus();
            updateButtons();
            handleGameStatus(status);
        } else {
            invalidMove();
            GameStatus status = game.getGameStatus();
            handleGameStatus(status);
        }
    }

    @Override
    public void invalidMove() {
        JOptionPane.showMessageDialog(rootPane, "Invalid Move");
        updateButtons();
    }

    @Override
    public void handleGameStatus(GameStatus status) {
        if (null != status) {
            switch (status) {
                case WHITE_UNDER_CHECK -> {
                    Square king = Utilities.getKingSquare(game.getWhoseTurn(), game.getBoard());
                    buttons[king.getRank().getValue()][king.getFile().getValue()].setBackground(Color.RED);
                }
                case BLACK_UNDER_CHECK -> {
                    Square king = Utilities.getKingSquare(game.getWhoseTurn(), game.getBoard());
                    buttons[king.getRank().getValue()][king.getFile().getValue()].setBackground(Color.RED);
                }
                case WHITE_WON -> {
                    JOptionPane.showMessageDialog(rootPane, "White Won");
                    setVisible(false);
                }
                case BLACK_WON -> {
                    JOptionPane.showMessageDialog(rootPane, "Black Won");
                    setVisible(false);
                }
                case STALEMATE -> {
                    JOptionPane.showMessageDialog(rootPane, "Stalemate");
                    setVisible(false);
                }
                case INSUFFICIENT_MATERIAL -> {
                    JOptionPane.showMessageDialog(rootPane, "Insufficient Material");
                    setVisible(false);
                }
                default -> {
                }
            }
        }
    }

    private PawnPromotion getPromotion(String promotion) {
        switch (promotion) {
            case "Queen" -> {
                return PawnPromotion.Queen;
            }
            case "Knight" -> {
                return PawnPromotion.Knight;
            }
            case "Bishop" -> {
                return PawnPromotion.Bishop;
            }
            case "Rook" -> {
                return PawnPromotion.Rook;
            }
            case "None" -> {
                return PawnPromotion.None;
            }
            default -> {
                return null;
            }
        }
    }

    private void frontendUndoMove() {
        game.undoMove();
        updateButtons();
    }

    private void updateButtons() {
        if (game.getWhoseTurn() == WHITE) {
            for (int row = 7; row >= 0; row--) {
                for (int col = 0; col < 8; col++) {
                    if ((row + col) % 2 == 0) {
                        buttons[row][col].setBackground(Color.BLACK);
                    } else {
                        buttons[row][col].setBackground(Color.WHITE);
                    }
                    Square square = new Square(getBoardFile(col), getBoardRank(row));
                    Piece piece = game.getPieceAtSquare(square);

                    if (piece != null) {
                        Icon pieceIcon = getIconForPiece(piece);
                        buttons[row][col].setIcon(pieceIcon);
                    } else {
                        buttons[row][col].setIcon(null);
                    }
                    add(buttons[row][col]);
                }
            }
            add(undoButton);
        } else if (game.getWhoseTurn() == BLACK) {
            for (int row = 0; row < 8; row++) {
                for (int col = 7; col >= 0; col--) {
                    if ((row + col) % 2 == 0) {
                        buttons[row][col].setBackground(Color.BLACK);
                    } else {
                        buttons[row][col].setBackground(Color.WHITE);
                    }
                    Square square = new Square(getBoardFile(col), getBoardRank(row));
                    Piece piece = game.getPieceAtSquare(square);

                    if (piece != null) {
                        Icon pieceIcon = getIconForPiece(piece);
                        buttons[row][col].setIcon(pieceIcon);
                    } else {
                        buttons[row][col].setIcon(null);
                    }
                    add(buttons[row][col]);
                }
            }
            add(undoButton);
        }
        revalidate();
        repaint();
    }

    private BoardFile getBoardFile(int col) {
        return switch (col) {
            case 0 ->
                BoardFile.A;
            case 1 ->
                BoardFile.B;
            case 2 ->
                BoardFile.C;
            case 3 ->
                BoardFile.D;
            case 4 ->
                BoardFile.E;
            case 5 ->
                BoardFile.F;
            case 6 ->
                BoardFile.G;
            case 7 ->
                BoardFile.H;
            default ->
                null;
        };
    }

    private BoardRank getBoardRank(int row) {
        return switch (row) {
            case 0 ->
                BoardRank.FIRST;
            case 1 ->
                BoardRank.SECOND;
            case 2 ->
                BoardRank.THIRD;
            case 3 ->
                BoardRank.FORTH;
            case 4 ->
                BoardRank.FIFTH;
            case 5 ->
                BoardRank.SIXTH;
            case 6 ->
                BoardRank.SEVENTH;
            case 7 ->
                BoardRank.EIGHTH;
            default ->
                null;
        };
    }

    private Icon getIconForPiece(Piece piece) {
        if (piece instanceof Pawn && piece.getOwner() == WHITE) {
            return whitepawn;
        } else if (piece instanceof Rook && piece.getOwner() == WHITE) {
            return whiterook;
        } else if (piece instanceof Knight && piece.getOwner() == WHITE) {
            return whiteknight;
        } else if (piece instanceof Bishop && piece.getOwner() == WHITE) {
            return whitebishop;
        } else if (piece instanceof Queen && piece.getOwner() == WHITE) {
            return whitequeen;
        } else if (piece instanceof King && piece.getOwner() == WHITE) {
            return whiteking;
        }
        if (piece instanceof Pawn && piece.getOwner() == BLACK) {
            return blackpawn;
        } else if (piece instanceof Rook && piece.getOwner() == BLACK) {
            return blackrook;
        } else if (piece instanceof Knight && piece.getOwner() == BLACK) {
            return blackknight;
        } else if (piece instanceof Bishop && piece.getOwner() == BLACK) {
            return blackbishop;
        } else if (piece instanceof Queen && piece.getOwner() == BLACK) {
            return blackqueen;
        } else if (piece instanceof King && piece.getOwner() == BLACK) {
            return blackking;
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();
        jButton55 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton49.setBackground(new java.awt.Color(255, 255, 255));
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        jButton50.setBackground(new java.awt.Color(0, 0, 0));
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });

        jButton51.setBackground(new java.awt.Color(255, 255, 255));
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jButton52.setBackground(new java.awt.Color(0, 0, 0));
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        jButton53.setBackground(new java.awt.Color(255, 255, 255));
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });

        jButton54.setBackground(new java.awt.Color(0, 0, 0));
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });

        jButton55.setBackground(new java.awt.Color(255, 255, 255));
        jButton55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton55ActionPerformed(evt);
            }
        });

        jButton56.setBackground(new java.awt.Color(0, 0, 0));
        jButton56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton56ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton54ActionPerformed

    private void jButton55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton55ActionPerformed

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton56ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Board.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Board().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JLayeredPane jLayeredPane1;
    // End of variables declaration//GEN-END:variables

}
