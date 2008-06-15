package ia2.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import ia2.BuildOnline;

public class Resource {
	
	public static URI getAbsolutePath(String file) throws URISyntaxException{
		return BuildOnline.class.getResource(file).toURI();
	}
	public static File getFile(String file) throws URISyntaxException{
		return new File(getAbsolutePath(file));
	}
	
	

}
