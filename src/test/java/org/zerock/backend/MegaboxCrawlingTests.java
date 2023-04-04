package org.zerock.backend;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.hibernate.result.Output;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@Log4j2

public class MegaboxCrawlingTests {

    @Test
    public void test1() {

        String urlStr = "https://www.megabox.co.kr/on/oh/oha/Movie/selectMovieList.do";

        try {
            URL url = new URL(urlStr);

            InputStream in = url.openStream();

            log.info(in);

            StringBuilder response = new StringBuilder();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in, "utf-8"));

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }


            JsonElement jsonElement = JsonParser.parseString(response.toString());

            //log.info(jsonElement);

            JsonObject jsonObject  = jsonElement.getAsJsonObject();

            //log.info(jsonObject);

            JsonArray movieList = jsonObject.getAsJsonArray("movieList");


            movieList.forEach(movie -> {

                JsonObject obj = movie.getAsJsonObject();

                String movieName = obj.get("movieNm").getAsString();
                String posterURL = obj.get("imgPathNm").getAsString();

                log.info(movieName);
                log.info(posterURL);

                savePoster(movieName, posterURL);


                log.info("--------------------------");

            });



            br.close();
            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void savePoster(String movieName, String posterURL) {

        try{

            Path filePath = Paths.get("/Users/zerock/temp", movieName+".jpg");

            InputStream in = new URL("https://www.megabox.co.kr/" + posterURL).openStream();

            OutputStream out = new FileOutputStream(filePath.toFile());

            FileCopyUtils.copy(in,out);

            in.close();
            out.close();



        }catch(Exception e){

        }

    }

}
