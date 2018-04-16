package Chess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
	/**
	 * 传入的与客户端对应的Socket
	 */
	Socket mSocket = null;
	public ServerThread(Socket s) {
		mSocket = s;
	}
	/**
	证明可行，接下来好好设计服务端逻辑
	1.分组
	2.红黑方初始化，红黑分配，棋子初始可行否
	*/
	@Override
	public void run() {
	  try {
	    InputStream inputstream = mSocket.getInputStream();
	    OutputStream outputstream = mSocket.getOutputStream();
	    DataInputStream dataInputStream = new DataInputStream(inputstream);
	    DataOutputStream dataOutputStream = new DataOutputStream(outputstream);
	    //在正式循环前，先向socket客户端发送先验信号
	    //如果本socket为前面一个socket1,则没有对手，发送消息设为红方不能移动且显示未找到对手，等待对手进入；
	    //如果本socket为后面一个socket2,直接匹配成功，发送消息设为黑方，且让对手可以移动
	    int oneOrtwo = getSubscriptBySocket();
	    if (oneOrtwo >= 0) {
	    	 //如果为前一个
		    if (mSocket.equals(ChessStart.sockList.get(oneOrtwo).socket1)) {
		    	//标志为999
		    	int signForSocket1 = 999;
		    	dataOutputStream.writeInt(signForSocket1);
		    	dataOutputStream.flush();
		    }
		    //如果为后一个
		    else if (mSocket.equals(ChessStart.sockList.get(oneOrtwo).socket2)) {
		    	//标志为666
		    	int signForSocket2 = 666;
		    	dataOutputStream.writeInt(signForSocket2);
		    	dataOutputStream.flush();
		    	//发送消息给对手，让其可以移动;消息为696
		    	OutputStream outputstream1 = ChessStart.sockList.get(oneOrtwo).socket1.getOutputStream();
		    	DataOutputStream dataOutputStream1 = new DataOutputStream(outputstream1);
		    	int moveSign = 696;
		    	dataOutputStream1.writeInt(moveSign);
		    	dataOutputStream1.flush();
		    }
	    }
	    //正式游戏循环
	    while (true) {
	    	//本socket读取消息
	    	int arg1 = dataInputStream.readInt();
	    	int arg2 = dataInputStream.readInt();
	    	int sign = dataInputStream.readInt();
	    	//将消息发送给对手的socket
	    	//获取本socket所对应组的下标
	    	int subscript = getSubscriptBySocket();
	    	if (subscript >= 0) {
	    		//如果本socket为socket1
	    		if (mSocket.equals(ChessStart.sockList.get(subscript).socket1)) {
	    			OutputStream outputstream2 = ChessStart.sockList.get(oneOrtwo).socket2.getOutputStream();
	    			DataOutputStream dataOutputStream2 = new DataOutputStream(outputstream2);
	    			dataOutputStream2.writeInt(arg1);
	    			dataOutputStream2.writeInt(arg2);
	    			dataOutputStream2.writeInt(sign);
	    			dataOutputStream2.flush();
	    		}
	    		//如果本socket为socket2
	    		else {
	    			OutputStream outputstream1 = ChessStart.sockList.get(oneOrtwo).socket1.getOutputStream();
	    			DataOutputStream dataOutputStream1 = new DataOutputStream(outputstream1);
	    			dataOutputStream1.writeInt(arg1);
	    			dataOutputStream1.writeInt(arg2);
	    			dataOutputStream1.writeInt(sign);
	    			dataOutputStream1.flush();
	    		}
	    	}
	    }
	  }
	  catch(IOException e) {
		  //如果本socket断开连接,移除这个组
		  int thisSubscript = getSubscriptBySocket();
		  try {
		  mSocket.close();
		  ChessStart.sockList.get(thisSubscript).socket1.close();
		  ChessStart.sockList.get(thisSubscript).socket2.close();
		  } catch(IOException e1) {
			  e1.printStackTrace();
		  }
		  ChessStart.sockList.remove(thisSubscript);
		  e.printStackTrace();
		  return;
	  }
	}
	//得到当前socket所在组的对应下标
	private int getSubscriptBySocket() {
		for (int i = 0; i < ChessStart.sockList.size(); i++) {
			if (mSocket.equals(ChessStart.sockList.get(i).socket1)||mSocket.equals(ChessStart.sockList.get(i).socket2))
				return i;
		}
		//倘若找不到返回-1
		return -1;
	}
}
