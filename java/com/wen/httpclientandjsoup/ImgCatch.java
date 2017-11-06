package com.wen.httpclientandjsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgCatch {
	public static void main(String[] args) {
		String url="http://jandan.net/ooxx/page-86#comments";
		String html = getUrlString(url);
		List<String> list = parsePicUrl(html);
		downloadImg(list);
	}
	
	//从指定网址获取HTML内容
	public static String getUrlString (String url){
		String html=null;
		//创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//以get方式请求该URL
		HttpGet get=new HttpGet(url);
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		try {
			//得到response对象
			CloseableHttpResponse response = client.execute(get);
			//返回码
			int status=response.getStatusLine().getStatusCode();
			if(status==HttpStatus.SC_OK){//200
				html=EntityUtils.toString(response.getEntity(), "utf-8");
				
			}
		} catch (Exception e) {
			System.out.println("访问【"+url+"】出现异常!");
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return html;
		
		
	}
	
	//解析返回的 HTML 内容，抓取图片地址
	public static List<String> parsePicUrl(String html){
		List<String> list=new ArrayList<String>();
		if(html!=null&&!html.equals("")){
			//使用 jsoup 解析 html，获取
			Document doc = Jsoup.parse(html);
			Elements imgs = doc.select("ol").select("li").select("img");
			
			for(Element e:imgs){
				String src="http:"+e.attr("src");
				list.add(src);
			}
			
		}
		
		return list;
		
	}
	
	public static void downloadImg(List<String> list){
		CloseableHttpClient client = HttpClients.createDefault();
		int i=1000;
		for(String url:list){
			HttpGet get=new HttpGet(url);
			get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			try {
				CloseableHttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				InputStream in = entity.getContent();
				System.out.println("开始下载图片"+i+".jpg");
				FileOutputStream out=new FileOutputStream(new File("d:\\chart\\img\\"+i+".jpg"));
				
				int len=-1;
				byte[] b=new byte[4096];
				
				while((len=in.read(b))!=-1){
					out.write(b, 0, len);
				}
				out.flush();
				out.close();
				in.close();
				i++;
				
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			
		}
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
