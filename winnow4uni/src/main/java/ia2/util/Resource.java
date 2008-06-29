package ia2.util;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public final class Resource {
	
	private static URI getURI(String file) throws URISyntaxException{
		return Resource.class.getResource("/"+file).toURI();
	}
	public static File getResourceFile(String filePath) throws URISyntaxException{
		return new File(getURI(filePath));
	}

	public static File getNewFile(String fileName) throws URISyntaxException{
		File file = new File(Resource.class.getResource("/").toURI());
		return new File(file.getParent()+"/"+fileName);
	}
	
	public static InputStream getResourceAsStream(String filePath) throws URISyntaxException{
		return Resource.class.getResourceAsStream("/" + filePath);
	}

}
