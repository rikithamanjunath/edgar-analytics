package com.DO;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTimeConverter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Date date = TimeConverter.getDate("2017-06-30", "10:00:00", "UTC");
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		System.out.println(month);
		assertEquals(month, 06);
	}

	@Test
	public void testTimeDifference() {
		long epoch1 = TimeConverter.getEpochTimeInSeconds("2017-06-30", "10:00:00", "UTC");

		System.out.println(epoch1);

		long epoch2 = TimeConverter.getEpochTimeInSeconds("2017-06-30", "10:00:01", "UTC");

		System.out.println(epoch2);

		assertEquals(epoch2 - epoch1, 1);
	}

	@Test
	public void testTimeDifference2() {
		long epoch1 = TimeConverter.getEpochTimeInSeconds("2017-06-29", "23:59:59", "UTC");

		System.out.println(epoch1);

		long epoch2 = TimeConverter.getEpochTimeInSeconds("2017-06-30", "00:00:00", "UTC");

		System.out.println("Differnce is " + (epoch2 - epoch1));

		assertEquals(epoch2 - epoch1, 1);
		
//		String[] ids = TimeZone.getAvailableIDs();
//		for (String id : ids) {
//			System.out.println(id + "\t" + TimeZone.getTimeZone(id));
//		}
		
	}

}
