package csc478.group2;

// 1.3.4 Word Placement and Validation Requirements
// Class Description: Processes placement requests. 
// 1.3.3.3 Tile placement shall occur only through validated PlacementRequest processing.
// 1.3.4.0 All moves shall be submitted through a PlacementRequest.
public class PlacementRequest {
	// 1.3.4.1 A PlacementRequest shall include a word string.
	public String word;
	// 1.3.4.2 A PlacementRequest shall include a starting position.
	public int row;
	public int col;
	// 1.3.4.3 A PlacementRequest shall include a direction (horizontal or
	// vertical).
	public boolean horizontal;
}