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
	
	//��ָ����ַ��ȡHTML����
	public static String getUrlString (String url){
		String html=null;
		//����httpclient����
		CloseableHttpClient client = HttpClients.createDefault();
		//��get��ʽ�����URL
		HttpGet get=new HttpGet(url);
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		try {
			//�õ�response����
			CloseableHttpResponse response = client.execute(get);
			//������
			int status=response.getStatusLine().getStatusCode();
			if(status==HttpStatus.SC_OK){//200
				html=EntityUtils.toString(response.getEntity(), "utf-8");
				
			}
		} catch (Exception e) {
			System.out.println("���ʡ�"+url+"�������쳣!");
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
	
	//�������ص� HTML ���ݣ�ץȡͼƬ��ַ
	public static List<String> parsePicUrl(String html){
		List<String> list=new ArrayList<String>();
		if(html!=null&&!html.equals("")){
			//ʹ�� jsoup ���� html����ȡ
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
				System.out.println("��ʼ����ͼƬ"+i+".jpg");
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
