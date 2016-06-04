package com.szl.zhaozhao2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.Socket;

import android.os.ParcelFileDescriptor;

import com.szl.zhaozhao2.log.LogPrinter;


public final class CloseUtils {

	public final static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}

	public final static void close(Writer is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}

	public final static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}

	public final static void close(BufferedReader bufferedReader) {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}

	public final static void close(ParcelFileDescriptor pfd) {
		if (pfd != null) {
			try {
				pfd.close();
			} catch (IOException e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}

	public final static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}

	public final static void close(DatagramSocket datagramSocket) {
		if (datagramSocket != null) {
			try {
				datagramSocket.close();
			} catch (Exception e) {
				// #debug
				LogPrinter.e("CloseUtils", e.getMessage(), e.getCause());
			}
		}
	}
}
