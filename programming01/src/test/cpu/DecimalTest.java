package cpu;

import cpu.present.PresentType;
import org.junit.Test;
import src.test.TestUnit;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DecimalTest {

	@Test
	public void testInteger1() {
		TestUnit expected = new TestUnit("213", "213.0", "01000011010101010000000000000000", "11000000000000000000001000010011", "00000000000000000000000011010101");
		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, PresentType.BIN.TWOS_COMPLEMENT, new String[]{"213"});
		assertEquals(0, errs.size());
	}

	//0000000000000000
	@Test
	public void testInteger2() {
		TestUnit expected = new TestUnit("2414151",
				"2414151.0",
				"01001010000100110101100100011100",
				"11000010010000010100000101010001",
				"00000000001001001101011001000111");
		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, PresentType.BIN.TWOS_COMPLEMENT, new String[]{"2414151"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testInteger3() {
		TestUnit expected = new TestUnit("-2414151",
				"-2414151.0",
				"11001010000100110101100100011100",
				"11010010010000010100000101010001",
				"11111111110110110010100110111001");
		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, PresentType.BIN.TWOS_COMPLEMENT, new String[]{"-2414151"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testInteger4() {
		TestUnit expected = new TestUnit("-48009508371",  "-48009508371.0",
				"11111111100000000000000000000000",
				"11011001010100001000001101110001",
				"11010010011010010000100111101101");
		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, PresentType.BIN.TWOS_COMPLEMENT, new String[]{"-48009508371"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testInteger5() {
		TestUnit expected = new TestUnit("-1",
				"-1.0",
				"10111111100000000000000000000000",
				"11010000000000000000000000000001",
				"11111111111111111111111111111111");
		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, PresentType.BIN.TWOS_COMPLEMENT, new String[]{"-1"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testFloat1() {
		TestUnit expected = new TestUnit(null,
				String.valueOf(Math.pow(2,-127)),
				"00000000010000000000000000000000",
				null,
				null);
		ArrayList<PresentType> errs = expected.eq(
				PresentType.DEC.FLOAT,
				PresentType.BIN.FLOAT,
				String.valueOf(Math.pow(2,-127)), "8", "23");
		assertEquals(0, errs.size());
	}

	@Test
	public void testFloat2() {
		TestUnit expected = new TestUnit(null,
				""+Float.MAX_VALUE,
				"01111111111000000000000000000000",
				null,
				null);
		ArrayList<PresentType> errs = expected.eq(
				PresentType.DEC.FLOAT,
				PresentType.BIN.FLOAT,
				""+Double.MAX_VALUE, "8", "23");
		assertEquals(0, errs.size());
	}

	@Test
	public void testFloat3() {
		TestUnit expected = new TestUnit(null,
				"-"+1.6328125*Math.pow(2,-20),
				"10110101110100010000000000000000",
				null,
				null);
		ArrayList<PresentType> errs = expected.eq(
				PresentType.DEC.FLOAT,
				PresentType.BIN.FLOAT,
				"-"+1.6328125*Math.pow(2,-20), "8", "23");
		assertEquals(0, errs.size());
	}

	@Test
	public void testFloat4() {
		TestUnit expected = new TestUnit(null,
				"0.5",
				"00111111000000000000",
				null,
				null);
		ArrayList<PresentType> errs = expected.eq(
				PresentType.BIN.FLOAT,
				PresentType.BIN.FLOAT,
				"00111111000000000000", "8", "11");
		assertEquals(0, errs.size());
	}

	@Test
	public void testFloat6() {
		TestUnit expected = new TestUnit(null,
				"11.375",
				"01000001001101100000",
				null,
				null);
		ArrayList<PresentType> errs = expected.eq(
				PresentType.BIN.FLOAT,
				PresentType.BIN.FLOAT,
				"01000001001101100000", "8", "11");
		assertEquals(0, errs.size());
	}

	@Test
	public void testFloat7() {
		TestUnit expected = new TestUnit(null,
				"-"+1.6328125*Math.pow(2,20),
				"11001001110100010000000000000000",
				null,
				null);
		ArrayList<PresentType> errs = expected.eq(
				PresentType.BIN.FLOAT,
				PresentType.BIN.FLOAT,
				"11001001110100010000000000000000", "8", "23");
		assertEquals(0, errs.size());
	}
}