package com.wen.MallCatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wen.po.Goods;

public class MallCatch {
	
	//这是抓取主程序
	
	public static void main(String[] args) {
		String html = getUrlString("http://mall.998.com/gallery-1146.html");
		//System.out.println(html);
		parseHtml(html);
	}
	
	public static List<Goods> getList(){
		String html = getUrlString("http://mall.998.com/gallery-1146.html");
		List<Goods> list = parseHtml(html);
		
		return list;
		
	}
	
	
	
	//从指定网址获取HTML内容
	public static String getUrlString(String url){
		String html=null;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get=new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			int status=response.getStatusLine().getStatusCode();
			if(status==200){
				 html = EntityUtils.toString(entity,"utf-8");
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return html;
		
	}
	
	
	public static List<Goods> parseHtml(String html){
		List<Goods> list=new ArrayList<Goods>();
		if(html!=null&&!html.equals("")){
			Document doc = Jsoup.parse(html);
			Elements elements = doc.select("li[class=items-gallery  itemsList]");
			for(Element e:elements){
				Element a = e.select("a").first();
				Element em = e.select("em").first();
				String href="http://mall.998.com"+a.attr("href");
				String picture=a.select("img").attr("lazyload");
				String name=a.select("img").attr("alt");
				double price=Double.parseDouble(em.text().trim().substring(1));
				String catalog_name="户外用品";
				//System.out.println(name+"\n"+picture+"\n"+href+"\n"+price+"\n"+catalog_name+"\n");
				Goods g=new Goods();
				g.setCatalog_name(catalog_name);
				g.setName(name);
				g.setPicture(picture);
				g.setPrice(price);
				g.setUrl(href);
				list.add(g);
			}
		}
		
		return list;
		
	}
	
}
