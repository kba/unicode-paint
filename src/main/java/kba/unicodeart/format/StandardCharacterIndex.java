package kba.unicodeart.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public enum StandardCharacterIndex {
	DEFAULT,
	;

	private List<Character> index = new ArrayList<>();
	public List<Character> index() {
		return index;
	}

	static {
		DEFAULT.index = new ArrayList<>();
		String filename = "/character-index/default_mapping";
		try {
			InputStream is = TMColorPalette.class.getResource(filename).openStream();
			try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				for(String line; (line = br.readLine()) != null; ) {
					if (line.matches("^\\s*#.*")) continue;
					if (! line.contains("\t")) continue;
					String[] segs = line.split("\t");
					if (segs.length < 3) {
						throw new IllegalArgumentException("Invalid mapping line: " + line);
					}
					char ch = segs[1].charAt(0);
					DEFAULT.index.add(ch);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't locate or parse file " + filename);
		}
	}

}
