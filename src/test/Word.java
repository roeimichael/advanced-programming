package test;

import java.util.Arrays;
import java.util.Objects;

public class Word {

	private Tile[] tiles;
	private int row,col;
	private boolean vertical;
	
	public Word(Tile[] tiles, int row, int col, boolean vertical) {
		super();
		this.tiles = tiles;
		this.row = row;
		this.col = col;
		this.vertical = vertical;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isVertical() {
		return vertical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(tiles);
		result = prime * result + Objects.hash(col, row, vertical);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		return col == other.col && row == other.row && Arrays.equals(tiles, other.tiles) && vertical == other.vertical;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(Tile t : tiles)
			sb.append(t.letter);
		sb.append(" ("+row+","+col+") "+(vertical?"vertical":"horizontal"));
		return sb.toString();
	}
	
}
