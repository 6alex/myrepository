package com.wen.jsoup;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class jsoup01 {
	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("http://www.ujiuye.com/").get();
		String title = doc.title();
		System.out.println(title);
		
	}
}
