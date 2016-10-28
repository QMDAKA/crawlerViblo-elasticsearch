package Entity;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by quangminh on 07/10/2016.
 */
public class Post {
    Date createAt;
    String title;
    String body;
    String url;
    String byCategory;
    String image;

    public String getByCategory() {
        return byCategory;
    }

    public void setByCategory(String byCategory) {
        this.byCategory = byCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Post(String url,String category,String image) {
        try {
            byCategory=category;
            this.image=image;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Document doc = Jsoup.connect(url).timeout(10000).
                    userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").
                    get();
            this.title=doc.select("p.post-title").html();
            this.body=doc.select("section.markdownContent").html();
            String dateInPost=doc.select("div.post-in-theme>span").html();
            /*try {
                this.createAt = formatter.parse(dateInPost);
            } catch (ParseException e1) {
                System.out.println("Loi dinh dang ngay thang");
                this.createAt=new Date(System.currentTimeMillis());;
            }*/
        } catch (IOException e) {
            System.out.println("ko ket noi dc voi bai viet : " +url);

        }
    }

    public Post() {
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }





}
