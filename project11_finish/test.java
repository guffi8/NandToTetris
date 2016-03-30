import java.io.File;


public class test {

	public static void main(String[] args) {
		
		JackTokenizer t = new JackTokenizer(new File(args[0]));
		
		t.advance();
		System.out.println(t.keyWord());
		System.out.println(t.peekVal());
		//System.out.println(t.peekTwiceVal());

		
		t.advance();
		System.out.println(t.identifier());
		System.out.println(t.peekVal());
		t.advance();
		System.out.println(t.symbol());
		System.out.println(t.peekVal());
		t.advance();
		System.out.println(t.keyWord());
		System.out.println(t.peekVal());
	}

}
