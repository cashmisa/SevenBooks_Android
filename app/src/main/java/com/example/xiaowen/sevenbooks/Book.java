package com.example.xiaowen.sevenbooks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book extends HashMap<String, String> {

    final static String baseUrl =
            "http://172.16.173.128/SevenBooksApplication/AndroidService.svc";
    final static String imageBaseUrl =
            "http://172.16.173.128/SevenBooksApplication/image/";

    public Book(String id, String title, String author, String isbn,
                String category, String stock, String price) {
        put("id", id);
        put("title", title);
        put("author", author);
        put("category", category);
        put("isbn", isbn);
        put("stock", stock);
        put("price", price);
    }

    public Book() {}

    public static List<Book> findAllBooks() {
        return convertJsonArrayToBookList(JSONParser.getJSONArrayFromUrl(baseUrl + "/Books"));
    }
    public static List<Book> findBookByCategory(String category) {
        return convertJsonArrayToBookList(
                JSONParser.getJSONArrayFromUrl(baseUrl + "/Book-category/" + category));
    }
    public static List<Book> findBookByTitile(String title) {
        return convertJsonArrayToBookList(JSONParser.getJSONArrayFromUrl(baseUrl + "/Book-title/" + title));
    }
    public static Book findBookById(String id) {
        return convertJsonObjectToBook(JSONParser.getJSONObjectFromUrl(baseUrl + "/Book/" + id));
    }
    //helper method：把一个JSONObject转换成Book
    private static Book convertJsonObjectToBook(JSONObject jsonObject) {
        try {
            return new Book(jsonObject.getString("id"),
                    jsonObject.getString("title"),
                    jsonObject.getString("author"),
                    jsonObject.getString("isbn"),
                    jsonObject.getString("category"),
                    jsonObject.getString("stock"),
                    jsonObject.getString("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //helper method：利用上一个method把一个JSONArray转换成List<Book>
    private static List<Book> convertJsonArrayToBookList(JSONArray jsonArray) {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                bookList.add(convertJsonObjectToBook((JSONObject) jsonArray.get(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bookList;
    }

    //把拿到的JSONArray of String (代表Category) 转换成 List<String>
    public static List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        JSONArray jsonArray = JSONParser.getJSONArrayFromUrl(baseUrl+ "/Categories");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                categories.add(jsonArray.get(i).toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }

    //直接从网页拿到图片
    public static Bitmap findImageById(String ISBN) {
        try {
            String imageUrl = String.format("%s/%s.jpg", imageBaseUrl, ISBN);
            JSONParser.getInputStream(imageUrl);
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            InputStream ins = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(ins);
            ins.close();
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
