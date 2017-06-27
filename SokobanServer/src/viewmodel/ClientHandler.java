package viewmodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {

	public void handle(InputStream in,OutputStream out,String cAddr) throws IOException,ClassNotFoundException;
}
