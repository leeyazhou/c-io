package com.github.leeyazhou.cio.example;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {


	public static void main(String[] args) throws IOException {
		Socket socket =new Socket("127.0.0.1", 9999);
		socket.close();
	}

}
