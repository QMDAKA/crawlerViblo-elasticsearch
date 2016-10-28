package Controller;

import Entity.Category;
import Entity.Post;
import Entity.Var;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ThreadedActionListener;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by quangminh on 21/10/2016.
 */
public class LoadNewThread implements Runnable {
    private Category category;


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LoadNewThread(Category category) {
        this.category = category;
    }

    public LoadNewThread() {
    }

    @Override
    public void run() {
        try {

            ObjectMapper mapper=new ObjectMapper();
            for(int i=0;i<this.category.getPostUrls().size();i++) {
                String urlPost = category.getPostUrls().get(i);
                String title = category.getTitle().toLowerCase();
                String urlImage = category.getImageByPost().get(i);
                Post post = new Post(urlPost, title, urlImage);

                if(post.getBody().length()>3) {
                    String json = mapper.writeValueAsString(post);
                    try {
                        IndexResponse response = Var.client.prepareIndex("viblo", title)
                                .setSource(json)
                                .get();
                        System.out.println(response);
                    }catch (IllegalArgumentException ex){
                        System.out.println("something wrong ");
                        IndexResponse response = Var.subClient.prepareIndex("viblo", title)
                                .setSource(json)
                                .get();
                        System.out.println(response);
                    }
                }

            }
        } catch (JsonProcessingException e) {
            System.out.println("loi dinh dang JSON");
        } catch (NullPointerException ex){
            System.out.println("Bai viet rong");

        }
    }



}
