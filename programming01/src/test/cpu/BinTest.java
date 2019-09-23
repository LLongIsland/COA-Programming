package cpu;

import cpu.present.Number;
import cpu.present.PresentType;
import org.junit.Test;
import src.test.TestUnit;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BinTest {

	@Test
	public void test() {
		TestUnit expected = new TestUnit("8", "8.0", "01000001000000000000000000000000","11000000000000000000000000001000", "00000000000000000000000000001000");
		ArrayList<PresentType> errs = expected.eq(PresentType.BIN.TWOS_COMPLEMENT, PresentType.BIN.TWOS_COMPLEMENT, "00000000000000000000000000001000");
		assertEquals(0, errs.size());
	}

	@Test
	public void test2() {
		TestUnit expected = new TestUnit("-21", "-21.0", "11000001101010000000000000000000","11010000000000000000000000100001", "11111111111111111111111111101011");
		ArrayList<PresentType> errs = expected.eq(PresentType.BIN.TWOS_COMPLEMENT, PresentType.BIN.TWOS_COMPLEMENT, new String[]{"11111111111111111111111111101011"});
		assertEquals(0, errs.size());
	}

	@Test
	public void test3() {
		TestUnit expected = new TestUnit("-21", "-21.0", "11000001101010000000000000000000","11010000000000000000000000100001", "11111111111111111111111111101011");
		ArrayList<PresentType> errs = expected.eq(PresentType.BIN.TWOS_COMPLEMENT, PresentType.BIN.TWOS_COMPLEMENT, new Number(PresentType.DEC.FLOAT, PresentType.BIN.FLOAT, new Number(PresentType.BIN.TWOS_COMPLEMENT, PresentType.BIN.TWOS_COMPLEMENT, "11111111111111111111111111101011").get(PresentType.DEC.FLOAT)).get(PresentType.BIN.TWOS_COMPLEMENT));
		assertEquals(0, errs.size());
	}

//	@Test
//	public void testOne1() {
//		cpu.present.cpu.TestUnit expected = new cpu.present.cpu.TestUnit("2147483649", "01111111100000000000000000000000", "00000111010010000011011001001001", "10000000000000000000000000000001", "10000000000000000000000000000001", "10000000000000000000000000000001");
//		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, "2147483649");
//		assertEquals(0, errs.size());
//	}
//
//	@Test
//	public void testOne2() {
//		cpu.present.cpu.TestUnit expected = new cpu.present.cpu.TestUnit("-2147483649", "11111111100000000000000000000000", "10000111010010000011011001001001", "00000000000000000000000000000001", "01111111111111111111111111111110", "01111111111111111111111111111111");
//		ArrayList<PresentType> errs = expected.eq(PresentType.DEC.INTEGER, "-2147483649");
//		assertEquals(0, errs.size());
//	}

}
