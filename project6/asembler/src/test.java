
public class test {
	
	public static void main(String[] args) {
		
		String x = "// saaSDASSFSFSDFSD";
		x = x.replace(" ", ""); //remove all white space
		if(x.contains("//"))
		{
			x = x.substring(0, x.indexOf("//"));
		}
		
		System.out.println(x.equals(" "));
	}
}
