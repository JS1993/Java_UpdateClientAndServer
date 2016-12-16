package com.jiangsu.UpdateClientAndServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UpdateServer {

	private static ServerSocket server;

	public static void main(String[] args) throws IOException {
		
		//3.建立多线程服务器
		server = new ServerSocket(12345);
		System.out.println("服务器启动，绑定12345端口");
		//4.读取文件名
		while(true){
			 final Socket socket = server.accept();				//接收请求
			new Thread(){
				public void run(){
					try {
						InputStream is = socket.getInputStream();
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						PrintStream ps = new PrintStream(socket.getOutputStream());
						String fileName = br.readLine();   //读取文件名
					
						//5.判断文件是否存在，结果返回给客户端
						File dir = new File("update");
						dir.mkdir();						  //创建文件夹
						File file = new File(dir,fileName);   //封装成 file 对象
						if(file.exists()){					  //如果服务器已经存在这个文件，将存在写给客户端
							ps.println("存在");
							socket.close();
							return;
						}else{
							ps.println("不存在");
						}
						
						//8.定义 FileOutputStream,从网络读取数据，存储到本地
						FileOutputStream fos = new FileOutputStream(file);
						byte[] arr = new byte[8192];
						int len;
						while((len = is.read(arr))!=-1){
							fos.write(arr,0,len);
						}
						fos.close();
						socket.close();
	
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
			}.start();
		}
		
		
		
	}

}
