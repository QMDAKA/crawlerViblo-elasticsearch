package Entity;

import Controller.ControllPage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jboss.logging.annotations.Pos;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 07/10/2016.
 */
public class Category {
    String title;
    String urlCategory;
    int totalPost;
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }

    @JsonIgnore
    List<String> imageByPost = new ArrayList<>();
    @JsonIgnore
    List<String> postUrls = new ArrayList<>();

    public List<String> getPostUrls() {
        return postUrls;
    }

    public void setPostUrls(List<String> postUrls) {
        this.postUrls = postUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlCategory() {
        return urlCategory;
    }

    public void setUrlCategory(String urlCategory) {
        this.urlCategory = urlCategory;
    }

    public List<String> getImageByPost() {
        return imageByPost;
    }

    public void setImageByPost(List<String> imageByPost) {
        this.imageByPost = imageByPost;
    }

    public Category(String url) {
        urlCategory = url;
        Document doc = null;
        try {
            doc = ControllPage.getDocmentInItemCategoryPage(url);
            title = doc.select("div.container>span").html();
            image = "https://viblo.asia/img/categories/320/" + title.toLowerCase() + ".png";

            String postCount = doc.select("div.btn-post-follow").html();
            totalPost = Integer.parseInt(postCount.split(" ")[0]);
            for (int count = 0; count < totalPost / 16 + 1; count++) {
                Document docByCount = ControllPage.getDocmentInItemCategoryPage(url + "?page=" + count);
                Elements elements = docByCount.select("div.box-top>a");
                for (Element e : elements) {

                    postUrls.add(e.attr("abs:href"));
                    imageByPost.add(e.select("img").attr("data-original"));
                    System.out.println(e.attr("abs:href"));

                }

            }
        } catch (IOException e) {
            System.out.println("ko ket noi ddn category");
        }

    }

    public Category() {
    }
}
