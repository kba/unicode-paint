package kba.gui.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import unicode.UCC_BlockElements;
import unicode.UCC_GeometricShapes;
import unicode.UCC_MiscellaneousSymbols;

public enum CharPalette {
	//	♠♥♦♣♤♡♢♧
	CARD_SUITS(
			UCC_MiscellaneousSymbols.BLACK_SPADE_SUIT,
			UCC_MiscellaneousSymbols.BLACK_HEART_SUIT,
			UCC_MiscellaneousSymbols.BLACK_CLUB_SUIT,
			UCC_MiscellaneousSymbols.BLACK_DIAMOND_SUIT,
			UCC_MiscellaneousSymbols.WHITE_SPADE_SUIT,
			UCC_MiscellaneousSymbols.WHITE_HEART_SUIT,
			UCC_MiscellaneousSymbols.WHITE_CLUB_SUIT,
			UCC_MiscellaneousSymbols.WHITE_DIAMOND_SUIT
			),
	// ♝♚♞♟♛♜♗♔♘♙♕♖
	CHESS(
			UCC_MiscellaneousSymbols.BLACK_CHESS_BISHOP,
			UCC_MiscellaneousSymbols.BLACK_CHESS_KING,
			UCC_MiscellaneousSymbols.BLACK_CHESS_KNIGHT,
			UCC_MiscellaneousSymbols.BLACK_CHESS_PAWN,
			UCC_MiscellaneousSymbols.BLACK_CHESS_QUEEN,
			UCC_MiscellaneousSymbols.BLACK_CHESS_ROOK,
			UCC_MiscellaneousSymbols.WHITE_CHESS_BISHOP,
			UCC_MiscellaneousSymbols.WHITE_CHESS_KING,
			UCC_MiscellaneousSymbols.WHITE_CHESS_KNIGHT,
			UCC_MiscellaneousSymbols.WHITE_CHESS_PAWN,
			UCC_MiscellaneousSymbols.WHITE_CHESS_QUEEN,
			UCC_MiscellaneousSymbols.WHITE_CHESS_ROOK),
	FILL(
			UCC_BlockElements.LIGHT_SHADE,
			UCC_BlockElements.MEDIUM_SHADE,
			UCC_BlockElements.DARK_SHADE,
			UCC_BlockElements.FULL_BLOCK,
			'╳'
			),
	SHAPES_BIG(
			UCC_GeometricShapes.BLACK_CIRCLE,
			UCC_GeometricShapes.BLACK_SQUARE,
			UCC_GeometricShapes.BLACK_DIAMOND,
			UCC_GeometricShapes.BLACK_DOWN_POINTING_TRIANGLE,
			UCC_GeometricShapes.BLACK_UP_POINTING_TRIANGLE,
			UCC_GeometricShapes.BLACK_LEFT_POINTING_TRIANGLE,
			UCC_GeometricShapes.BLACK_RIGHT_POINTING_TRIANGLE
			),
	;
	
	private final List<Character> chars = new ArrayList<>();
	public List<Character> getChars() { return chars; }

	CharPalette(char... chars) {
		for (char ch : chars) this.chars.add(ch);
	}
	

	@Override
	public String toString() {
		return StringUtils.join(chars, "");
	}

	public CharPalette getNext() { return values()[(ordinal()+1) % values().length]; }
}
