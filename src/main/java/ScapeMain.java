
import Controller.ControllPage;
import Entity.Post;
import Entity.Category;
import Entity.Var;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.hateoas.alps.Doc;
import Entity.Category;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by quangminh on 05/10/2016.
 */
public class ScapeMain {

    public static void main(String[] args) throws IOException {
        Var.client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        Var.subClient = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        ObjectMapper mapper=new ObjectMapper();
        int pageCount=0;
        String html;
        ArrayList<String> urlCategories=new ArrayList<>();
        while(true) {
            String response = ControllPage.getDocumentInCategoriesPage("https://viblo.asia/categories?pageCount="+pageCount);
            JSONObject jsonObject = new JSONObject(response);
            html = jsonObject.getString("html");
            boolean boo = jsonObject.getBoolean("hideSeeMore");
            Document docHome=Jsoup.parse(html);
            Elements elements=docHome.select("div.category>a");
            for(Element e:elements){
                urlCategories.add(e.attr("abs:href"));
                System.out.println(e.attr("abs:href"));
            }
            /*if(boo)
                break;*/
            if(pageCount==2)
                break;
            pageCount++;

        }
        //get url in Category
        //Category c=new Category(urlCategories.get(0));
        //ControllPage.LoadPostByCategory(c);
        for(String urlCategory:urlCategories){
            Category category=new Category(urlCategory);
            String json=mapper.writeValueAsString(category);
            IndexResponse response = Var.client.prepareIndex("viblo", "categories")
                    .setSource(json)
                    .get();
            Var.categoryArrayList.add(category);
        }
        System.out.println("Connect to post");
        for(Category category:Var.categoryArrayList){
            ControllPage.LoadPostByCategory(category);


        }
    }

}
