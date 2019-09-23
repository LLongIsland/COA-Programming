package cpu;

import cpu.present.PresentType;
import org.junit.Test;
import src.test.TestUnit;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BCDTest {

	@Test
	public void testBCD1() {
		TestUnit expected = new TestUnit("213",
				"213.0",
				"01000011010101010000000000000000",
				"11000000000000000000001000010011",
				"00000000000000000000000011010101");
		ArrayList<PresentType> errs = expected.eq(PresentType.BCD.W8421, PresentType.BCD.W8421, new String[]{"11000000000000000000001000010011"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testBCD2() {
		TestUnit expected = new TestUnit("-213",
				"-213.0",
				"11000011010101010000000000000000",
				"11010000000000000000001000010011",
				"11111111111111111111111100101011");
		ArrayList<PresentType> errs = expected.eq(PresentType.BCD.W8421, PresentType.BCD.W8421, new String[]{"11010000000000000000001000010011"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testBCD3() {
		TestUnit expected = new TestUnit("66",
				"66.0",
				"01000010100001000000000000000000",
				"11000000000000000000000001100110",
				"00000000000000000000000001000010");
		ArrayList<PresentType> errs = expected.eq(PresentType.BCD.W8421, PresentType.BCD.W8421, new String[]{"11000000000000000000000001100110"});
		assertEquals(0, errs.size());
	}

	@Test
	public void testBCD4() {
		TestUnit expected = new TestUnit("-66",
				"-66.0",
				"11000010100001000000000000000000",
				"11010000000000000000000001100110",
				"11111111111111111111111110111110");
		ArrayList<PresentType> errs = expected.eq(PresentType.BCD.W8421, PresentType.BCD.W8421, new String[]{"11010000000000000000000001100110"});
		assertEquals(0, errs.size());
	}

}
