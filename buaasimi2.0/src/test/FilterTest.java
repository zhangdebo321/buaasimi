package test;

import com.buaasimi.text.ExtractException;
import com.buaasimi.text.StandardFilter;
import com.buaasimi.text.TextExtractor;

public class FilterTest {

	/**
	 * @param args
	 * @throws ExtractException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TextExtractor tx = new TextExtractor();
		try {
			tx.extract("d:/test.docx");
		} catch (ExtractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tx.addFilter(new StandardFilter());
		System.out.println(tx.filtrate());
	}

}
