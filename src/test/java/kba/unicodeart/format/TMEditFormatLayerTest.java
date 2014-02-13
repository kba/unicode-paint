package kba.unicodeart.format;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")
public class TMEditFormatLayerTest {
	
	private static final int testSize = 10;
	private static final Logger log = LoggerFactory.getLogger(TMEditFormatLayerTest.class);

	@Test
	@Ignore
	public void testWriteXML() throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
	    XMLStreamWriter writer = factory.createXMLStreamWriter(System.out);
	    TMEditFormatLayer layer = new TMEditFormatLayer("foo", testSize, testSize, "null");
	    layer.writeXML(writer);
	    writer.close();
	}

	@Test
		public void testFromXML() throws Exception {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			StringWriter sw = new StringWriter();
//			PrintStream sw = System.out;
//			OutputStream sw = ByteStreams.nullOutputStream();
		    XMLStreamWriter writer = factory.createXMLStreamWriter(sw);
		    TMEditFormatLayer layerOut = new TMEditFormatLayer(testSize, testSize);
		    layerOut.get(2, 2).setCharacter('x');
		    layerOut.writeXML(writer);
		    writer.close();
	
		    String savedMap = sw.toString();
		    log.debug(savedMap);
			StringReader inStream = new StringReader(savedMap);
		    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		    log.debug("IS_COALESCING: " + inputFactory.getProperty(XMLInputFactory.IS_COALESCING));
		    inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
		    XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(inStream);
		    TMEditFormatLayer layerIn = TMEditFormatLayer.fromXML(testSize, testSize, xmlStreamReader);
	//	    log.debug(layerOut.exportMapToString());
	//	    log.debug(layerIn.exportMapToString());
		    assertEquals(layerOut.getName(), layerIn.getName());
		    assertEquals(layerOut.get(2, 2).getCharacter(), 'x');
		    assertEquals(String.valueOf(layerOut.get(2, 2).getCharacter()), String.valueOf(layerIn.get(2, 2).getCharacter()));
		}

}
