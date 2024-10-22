package ChessCore;

import ChessCore.Pieces.*;

public final class ClassicBoardInitializer implements BoardInitializer {
    private static final BoardInitializer instance = new ClassicBoardInitializer();
    PieceFactory pf = new PieceFactory();

    private ClassicBoardInitializer() {
    }

    public static BoardInitializer getInstance() {
        return instance;
    }

    @Override
    public Piece[][] initialize() {
        Piece[][] initialState = {
            {pf.createpiece("Rook",Player.WHITE), pf.createpiece("Knight",Player.WHITE), pf.createpiece("Bishop",Player.WHITE),pf.createpiece ("Queen",Player.WHITE), pf.createpiece("King",Player.WHITE), pf.createpiece("Bishop",Player.WHITE),pf.createpiece("Knight",Player.WHITE),pf.createpiece("Rook",Player.WHITE)},
            {pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE), pf.createpiece("Pawn",Player.WHITE)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK), pf.createpiece("Pawn",Player.BLACK)},
            {pf.createpiece("Rook",Player.BLACK), pf.createpiece("Knight",Player.BLACK), pf.createpiece("Bishop",Player.BLACK), pf.createpiece("Queen",Player.BLACK), pf.createpiece("King",Player.BLACK), pf.createpiece("Bishop",Player.BLACK), pf.createpiece("Knight",Player.BLACK), pf.createpiece("Rook",Player.BLACK)}
        };
        return initialState;
    }
}
