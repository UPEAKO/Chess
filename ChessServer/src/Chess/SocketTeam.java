package Chess;

import java.net.Socket;

public class SocketTeam {
	public Socket socket1;
	public Socket socket2;
	int sign = 0;
	public SocketTeam() {
		
	}
	public void addSocket(Socket socket) {
		if (sign == 0) {
			socket1 = socket;
			sign++;
		}
		else if (sign == 1) {
			socket2 = socket;
			sign++;
		}
	}
}
