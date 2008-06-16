package ia2.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public final class Resource {
	
	private static URI getAbsolutePath(String file) throws URISyntaxException{
		return Resource.class.getResource("/"+file).toURI();
	}
	public static File getResourceFile(String file) throws URISyntaxException{
		return new File(getAbsolutePath(file));
	}

	public static File getNewFile(String fileName) throws URISyntaxException{
		File file = new File(Resource.class.getResource("/").toURI());
		return new File(file.getParent()+"/"+fileName);
	}

}
