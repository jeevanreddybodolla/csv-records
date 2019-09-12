import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.patientping.Book;
import com.patientping.BookStats;

public class TestBookStats {
	@Test
	public void testLoadCsv() throws IOException {
		String inputData = "headers,headers,headers,headers,headers,headers,headers,headers,headers,headers\n" + "14428,The Inheritors,William Golding,3.53,0156443791,9780156443791,en-US,240,2681,257\n" + "2386,Moby Dick,Herman Melville-William Hootkins,3.49,9626343583,9789626343586,eng,25,66,17\n";
		InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
		BookStats stats = new BookStats();
		List<Book> books = stats.loadCsv(inputStream);
		assertEquals(2, books.size());

		assertEquals("William Golding", books.get(0).getAuthors());
		assertEquals("The Inheritors", books.get(0).getTitle());
		assertEquals("en-US", books.get(0).getLanguage());
		assertEquals("Herman Melville-William Hootkins", books.get(1).getAuthors());
		assertEquals("Moby Dick", books.get(1).getTitle());
		assertEquals("eng", books.get(1).getLanguage());
	}
}
