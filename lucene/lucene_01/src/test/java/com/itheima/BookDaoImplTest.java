package com.itheima;

import com.ithaima.lucene.pojo.Book;
import com.itheima.lucene.dao.IBookDao;
import com.itheima.lucene.dao.impl.BookDaoImpl;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.FilterDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QiuDeSheng
 * @date 2019/3/22  21:32
 */
public class BookDaoImplTest {
	
	public void add(){}

    @Test
    public void luceneTest(){

        try {
            IBookDao bookDao = new BookDaoImpl();
            List<Book> bookList = bookDao.findAll();

            //创建文档集合对象
            List<Document> documents = new ArrayList<Document>();

            for (Book book : bookList) {
                Document document = new Document();
                document.add(new StringField("id",book.getId()+"",Field.Store.YES));
                document.add(new TextField("bookName",book.getBookName(),Field.Store.YES));
                document.add(new DoubleField("bookPrice",book.getPrice(),Field.Store.YES));
                document.add(new StoredField("bookPic",book.getPic()));
                document.add(new TextField("bookDesc",book.getBookDesc(),Field.Store.NO));

                documents.add(document);
            }

            //Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new IKAnalyzer();


            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
            //设置索引库打开的模式
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            Directory directory = FSDirectory.open(new File("D:\\develop\\luke\\index"));
            IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);

            for (Document document : documents) {
                indexWriter.addDocument(document);
                indexWriter.commit();
            }

            indexWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    @Test
    public void query() throws ParseException, IOException {

        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();

        QueryParser queryParser = new QueryParser("bookName",analyzer);
        Query query = queryParser.parse("java精传智播客");
        System.out.println("Query:" + query);

        Directory directory = FSDirectory.open(new File("D:\\develop\\luke\\index"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TopDocs topDocs = indexSearcher.search(query,5);
        System.out.println("总命中数:"+topDocs.totalHits);

        //获取文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("--------------------------");
            System.out.println("文档id:"+ scoreDoc.doc + "/t文档分值:" + scoreDoc.score );

            //根据文档id获取指定的文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书id:" + doc.get("id"));
            System.out.println("图书名称:" + doc.get("bookName"));
            System.out.println("图书价格:" + doc.get("bookPrice"));
            System.out.println("图书图片:" + doc.get("bookPic"));
            System.out.println("图书描述:" + doc.get("bookDesc"));

        }

        //释放资源
        indexReader.close();

    }

}
