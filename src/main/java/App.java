

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.wen.MallCatch.MallCatch;
import com.wen.dao.GoodsDao;
import com.wen.po.Goods;



public class App {
	SqlSessionFactory sf;
	@Before
	public void setup() throws IOException{
		InputStream ins = Resources.getResourceAsStream("myBatisConfig.xml");
		 sf = new SqlSessionFactoryBuilder().build(ins);
		System.out.println("。。。。。。");
	}
	
	@Test
	public void addGoods() throws Exception {
		SqlSession session = sf.openSession();
		GoodsDao goodsDao = session.getMapper(GoodsDao.class);
		
		List<Goods> list = MallCatch.getList();
		for(Goods goods:list){
			goodsDao.addGoods(goods);
			session.commit();
		}
		
		
		
	}
	

	
	
}
