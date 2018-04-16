package Chess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChessStart {
	//原始equals比较对象相同
	public static Vector<SocketTeam> sockList = new Vector<>();
	public static int searchForUseful() {
		//没有组
		if (sockList.size() == 0)
			return -1;
		//有组没满
		if (sockList.size() > 0) {
			for (int i = 0; i < sockList.size(); i++) {
				if (sockList.get(i).sign < 2)
					return i;
			}
		}
		//有组满了
		return -2;
	}
	//如果没有一个组，生成一个；如果有组，但都满了，也生成一个；如果有组有没满，向没满的组中添加
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(30000);
		while (true)  {
			Socket s = ss.accept();
			System.out.println("Socket连接成功");
			//如果一个组也没有，添加一个组
			if (ChessStart.searchForUseful() == -1) {
				SocketTeam socketteam = new SocketTeam();
				socketteam.addSocket(s);
				ChessStart.sockList.add(socketteam);
				System.out.println("1");
			} 
			//如果有组满了
			else if (ChessStart.searchForUseful() == -2){
				SocketTeam socketteam = new SocketTeam();
				socketteam.addSocket(s);
				ChessStart.sockList.add(socketteam);
				System.out.println("2");
			}
			//如果有组没满
			else {
				//得到没满的组的位置
				int location = ChessStart.searchForUseful();
				//向没满的位置添加	
				ChessStart.sockList.get(location).addSocket(s);
				System.out.println("3");
			}
			//最终对每一个连接成功的socket都直接启动一个新线程
			new ServerThread(s).start();
		}
	}
}
