package Controller;

import Entity.Category;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 07/10/2016.
 */
public class ControllPage {
   public static String getDocumentInCategoriesPage(String url){

       HttpClient client = HttpClientBuilder.create().build();

       HttpGet getResource=new HttpGet(url);
       getResource.addHeader("Accept-Language","vi-VN,vi;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-US;q=0.2,en;q=0.2");
       getResource.addHeader("Accept-Encoding","gzip, deflate, sdch, br");
       getResource.addHeader("Cache-Control","max-age=0");
       getResource.addHeader("Connection","keep-alive");
       getResource.addHeader("Cookie","remember_82e5d2c56bdd0811318f0cf078b78bfc=eyJpdiI6IkVcL0Y2SFwvaVNWNFYwcmFQYzU0aUZUQT09IiwidmFsdWUiOiJQNks3Q01Ld3hVbHF5OUdTd0JzbUFRQnVnd085YURBWm5FK01cLzdTckpTVXd2VnhSVmxoOUltQ3lmK2RuNHRGUEZDRjZrSFdwdVdGdEkrZkVIRVM1M012bGFcL3ZIRVY2eDdaWE1DZ3JNU2k4PSIsIm1hYyI6IjlmN2I1Nzg5YzQ2ZDVkYmRlZmM0ZGEyMjY2Y2MyZWIwYWFkMTk4MzMxMDY4OWQ1MDFmYWI3OTYxYjNmZjZiNTgifQ%3D%3D; _ga=GA1.2.1627860303.1475825973 ");
       getResource.addHeader("X-Requested-With"," XMLHttpRequest");
       getResource.addHeader("Referer","https://viblo.asia/categories");
       getResource.addHeader("User-Agent"," Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

       HttpResponse responseOfResourceServer = null;
       try {
           responseOfResourceServer = client.execute(getResource);
           HttpEntity entity = responseOfResourceServer.getEntity();
           String response = EntityUtils.toString(entity, "UTF-8");
           System.out.println(responseOfResourceServer.getStatusLine().getStatusCode());
           System.out.println(response);
           return response;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }
   public static Document getDocmentInItemCategoryPage(String url) throws IOException {

           Document document= Jsoup.connect(url).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                   .header("Accept-Encoding","gzip, deflate, sdch, br")
                   .header("Accept-Language","vi-VN,vi;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-US;q=0.2,en;q=0.2")
                   .header("Cache-Control","max-age=0")
                   .header("Connection","keep-alive")
                   .header("Cookie","remember_82e5d2c56bdd0811318f0cf078b78bfc=eyJpdiI6IkVcL0Y2SFwvaVNWNFYwcmFQYzU0aUZUQT09IiwidmFsdWUiOiJQNks3Q01Ld3hVbHF5OUdTd0JzbUFRQnVnd085YURBWm5FK01cLzdTckpTVXd2VnhSVmxoOUltQ3lmK2RuNHRGUEZDRjZrSFdwdVdGdEkrZkVIRVM1M012bGFcL3ZIRVY2eDdaWE1DZ3JNU2k4PSIsIm1hYyI6IjlmN2I1Nzg5YzQ2ZDVkYmRlZmM0ZGEyMjY2Y2MyZWIwYWFkMTk4MzMxMDY4OWQ1MDFmYWI3OTYxYjNmZjZiNTgifQ%3D%3D; _ga=GA1.2.1627860303.1475825973; ")
                   .header("Referer","https://viblo.asia/categories")
                   .header("Upgrade-Insecure-Requests","1")
                   .header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
                   .get();
           return document;
       }
    public static void LoadPostByCategory(Category category){

        if(category.getImageByPost().size()<20){
            LoadNewThread run1=new LoadNewThread(category);
            Thread t1 = new Thread(run1);

        }
        else
        {
            int kq=category.getImageByPost().size()/4;
            int mod=category.getImageByPost().size()%4;
            LoadNewThread run1=new LoadNewThread(subCategory(category,0,kq));
            Thread t1 = new Thread(run1);
            t1.start();
            LoadNewThread run2=new LoadNewThread(subCategory(category,kq,kq*2));
            Thread t2 = new Thread(run2);
            t2.start();
            LoadNewThread run3=new LoadNewThread(subCategory(category,kq*2,kq*3));
            Thread t3 = new Thread(run3);
            t3.start();
            LoadNewThread run4=new LoadNewThread(subCategory(category,kq*3,kq*4+mod));
            Thread t4 = new Thread(run4);
            t4.start();



        }
       /* System.out.println(0+","+kq);
        System.out.println(kq+","+kq*2);
        System.out.println(kq*2+","+kq*3);
        System.out.println(kq*3+","+(kq*4+mod));*/

    }
    public static Category subCategory(Category category,int start,int end){
        Category sub=new Category();
        List<String> miniImageArr=category.getImageByPost().subList(start,end);
        List<String> miniUrlArr=category.getPostUrls().subList(start,end);
        sub.setTitle(category.getTitle());
        sub.setImageByPost(miniImageArr);
        sub.setPostUrls(miniUrlArr);

        return sub;

    }

   }

