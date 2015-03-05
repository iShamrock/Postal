package iShamrock.Postal.database;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import iShamrock.Postal.database.photo.FileDownload;
import iShamrock.Postal.database.photo.FileImageUpload;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.entity.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lifengshuang on 2/16/15.
 */

/**
 * Implement all the methods
 * View the "User" and "PostalDataItem" class
 */
public class Connect {
    public static final String server="http://121.40.155.146:8080/postalservermine/data/";
    public static final String urlserver="http://121.40.155.146:8080/postalservermine/";
    static String ALBUM_PATH= Environment.getExternalStorageDirectory() + "/download_test/";
    static FileImageUpload f=new FileImageUpload();



    /**
     * @param user
     * @return the user's data, null if login failed
     */
    public static User login(String user, String password) throws IOException {

        String url = server + "UserLogin";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user));
        urlParameters.add(new BasicNameValuePair("password", password));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        String name=rd.readLine();
        String phone=rd.readLine();
        String photoURI=rd.readLine();
        String coverURI=rd.readLine();
        name=(name.equals("null"))?null:name;
        phone=(phone.equals("null"))?null:phone;
        photoURI=(photoURI.equals("null"))?null:photoURI;
        coverURI=(coverURI.equals("null"))?null:coverURI;


        User u=new User(name,phone,photoURI,coverURI);

        String uri=u.getCoverURI();
        if(uri!=null) {
            int pos = uri.split("/").length - 1;
            File file = new File(ALBUM_PATH + u.getPhone() + "/" + uri.split("/")[pos]);


            u.setCoverURI(Uri.fromFile(new File(ALBUM_PATH + u.getPhone() + "/" + u.getCoverURI().split("/")[pos])).toString());
            Thread fileDownload = new FileDownload(uri.split("/")[pos], urlserver + "img/" + u.getPhone() + "" + "/" + uri.split("/")[pos], u.getPhone());
            fileDownload.start();
        }
        uri = u.getPhotoURI();
        if(uri!=null) {

            int  pos = uri.split("/").length - 1;
            File file = new File(ALBUM_PATH + u.getPhone() + "/" + uri.split("/")[pos]);
            u.setPhotoURI(Uri.fromFile(file).toString());
            Thread  fileDownload = new FileDownload(uri.split("/")[pos], urlserver + "img/" + u.getPhone() + "" + "/" + uri.split("/")[pos], u.getPhone());
            fileDownload.start();
        }

        return u;


    }

    /**
     * @param user
     * @return all the postal of the user IN ORDER OF TIME
     */


    public static ArrayList<PostalDataItem> getPostalData(User user) throws IOException {

        //todo: changed the parameter from String to User
        //todo: return all the postal if the to_user or the from_user is parameter user
        //todo: don't forget download matters
        String url = server + "GetPostalItem";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        ArrayList<PostalDataItem> apid = new ArrayList<PostalDataItem>();
        int n = Integer.valueOf(rd.readLine());
        for (int i = 0; i < n; i++) {
            int type = Integer.valueOf(rd.readLine());
            String uri = rd.readLine();
            String text = rd.readLine();
            String time = rd.readLine();
            String title = rd.readLine();
            double locationx = 0.0, locationy = 0.0;
            try {
                locationx = Double.valueOf(rd.readLine());
            }catch (Exception e){
                System.out.println("Double parse failed");
            }
            try {
                locationy = Double.valueOf(rd.readLine());
            }catch (Exception e){
                System.out.println("Double parse failed");
            }
            String from_user = rd.readLine();
            String to_user = rd.readLine();
            String location_text = rd.readLine();


            uri = (uri.equals("null")) ? null : uri;
            text = (text.equals("null")) ? null : text;
            time = (time.equals("null")) ? null : time;
            title = (title.equals("null")) ? null : title;
            from_user = (from_user.equals("null")) ? null : from_user;
            to_user = (to_user.equals("null")) ? null : to_user;
            location_text = (location_text.equals("null")) ? null : location_text;
            double[] location = {locationx, locationy};

            PostalDataItem pdi = new PostalDataItem(type, uri, text, time, title, location, from_user, to_user, location_text);
            apid.add(pdi);






        }

        for( PostalDataItem pdi: apid){
            int pos=pdi.uri.split("/").length-1;
            File f=new File(ALBUM_PATH+pdi.from_user+"/"+pdi.uri.split("/")[pos]);
            if(f.exists()){
                continue;
            }

            Thread fileDownload=new FileDownload(pdi.uri.split("/")[pos],
                    urlserver+"img/"+pdi.from_user+""+"/"+pdi.uri.split("/")[pos],
                    pdi.from_user);
            fileDownload.start();

            pdi.uri(Uri.fromFile(new File(ALBUM_PATH + pdi.from_user + "/" + pdi.uri.split("/")[pos])).toString());



        }




        return apid;
    }




    public static void addPostal(PostalDataItem postalDataItem) throws IOException {
        //todo
        System.out.println("addPostal where " + postalDataItem.text);
        String url = server + "AddPostalItem";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("type", postalDataItem.type+""));
        urlParameters.add(new BasicNameValuePair("uri", postalDataItem.uri));
        urlParameters.add(new BasicNameValuePair("text", postalDataItem.text));
        urlParameters.add(new BasicNameValuePair("time", postalDataItem.time));
        urlParameters.add(new BasicNameValuePair("title", postalDataItem.title));
        urlParameters.add(new BasicNameValuePair("locationx", postalDataItem.location[0]+""));
        urlParameters.add(new BasicNameValuePair("locationy", postalDataItem.location[1]+""));
        urlParameters.add(new BasicNameValuePair("from", postalDataItem.from_user));
        urlParameters.add(new BasicNameValuePair("to", postalDataItem.to_user));
        urlParameters.add(new BasicNameValuePair("locationtext", postalDataItem.location_text));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

        if (postalDataItem.type != PostalDataItem.TYPE_TEXT) {
            File myCaptureFile = new File(postalDataItem.uri);
            if (myCaptureFile.exists()) {
                try {
                    myCaptureFile = new File(new URI(postalDataItem.uri));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                f.uploadFile(myCaptureFile, server + "OkServletUp", postalDataItem.from_user);
            }
        }

    }


    public static void addFriend(User user, User friend) throws IOException {


        String url = server + "AddFriend";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("user", user.getPhone()));
        urlParameters.add(new BasicNameValuePair("friend", friend.getPhone()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

    }


    public static void signUp(User user,String password) throws IOException {
        //todo:
        String url = server + "UserRegister";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));
        urlParameters.add(new BasicNameValuePair("password", password));
        urlParameters.add(new BasicNameValuePair("name", user.getNickname()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

        if(user.getNickname()!=null){
            updateUserInformation(user,user);
        }

    }



    /**
     * @param user
     * @return all the friends of the user
     */
    public static ArrayList<User> getFriendData(User user) throws IOException {
        //todo: return all the friends of the user
        String url = server + "GetAllFriendsData";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        ArrayList<User> auser = new ArrayList<User>();
        int n = Integer.valueOf(rd.readLine());
        for (int i = 0; i < n; i++) {

            String name=rd.readLine();
            String phone=rd.readLine();
            String photoURI=rd.readLine();
            String coverURI=rd.readLine();
            name=(name.equals("null"))?null:name;
            phone=(phone.equals("null"))?null:phone;
            photoURI=(photoURI.equals("null"))?null:photoURI;
            coverURI=(coverURI.equals("null"))?null:coverURI;

            User u=new User(name,phone,photoURI,coverURI);
            auser.add(u);
        }

        for(User u:auser) {


            String uri=u.getCoverURI();
            if(uri!=null) {
                int pos = uri.split("/").length - 1;
                File file = new File(ALBUM_PATH + u.getPhone() + "/" + uri.split("/")[pos]);
                u.setCoverURI(Uri.fromFile(file).toString());
                Thread fileDownload = new FileDownload(uri.split("/")[pos], urlserver + "img/" + u.getPhone() + "" + "/" + uri.split("/")[pos], u.getPhone());
                fileDownload.start();
            }
            uri = u.getPhotoURI();
            if(uri!=null) {

                int  pos = uri.split("/").length - 1;
                File file = new File(ALBUM_PATH + u.getPhone() + "/" + uri.split("/")[pos]);
                u.setPhotoURI(Uri.fromFile(file).toString());
                Thread  fileDownload = new FileDownload(uri.split("/")[pos], urlserver + "img/" + u.getPhone() + "" + "/" + uri.split("/")[pos], u.getPhone());
                fileDownload.start();
            }

        }
        return auser;

    }



    public static ArrayList<User> getAllUser() throws IOException {
        //todo: return all the users, it's used when the user wants to add a friend
        String url = server + "GetAllUser";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");






        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        ArrayList<User> auser = new ArrayList<User>();
        int n = Integer.valueOf(rd.readLine());
        for (int i = 0; i < n; i++) {

            String name=rd.readLine();
            String phone=rd.readLine();
            String photoURI=rd.readLine();
            String coverURI=rd.readLine();
            name=(name.equals("null"))?null:name;
            phone=(phone.equals("null"))?null:phone;
            photoURI=(photoURI.equals("null"))?null:photoURI;
            coverURI=(coverURI.equals("null"))?null:coverURI;

            User u=new User(name,phone,photoURI,coverURI);
            auser.add(u);
        }
        for(User u:auser) {


            String uri=u.getCoverURI();
            if(uri!=null) {
                int pos = uri.split("/").length - 1;
                File file = new File(ALBUM_PATH + u.getPhone() + "/" + uri.split("/")[pos]);
                u.setCoverURI(Uri.fromFile(file).toString());
                Thread fileDownload = new FileDownload(uri.split("/")[pos], urlserver + "img/" + u.getPhone() + "" + "/" + uri.split("/")[pos], u.getPhone());
                fileDownload.start();
            }
            uri = u.getPhotoURI();
            if(uri!=null) {

                int  pos = uri.split("/").length - 1;
                File file = new File(ALBUM_PATH + u.getPhone() + "/" + uri.split("/")[pos]);
                u.setPhotoURI(Uri.fromFile(file).toString());
                Thread  fileDownload = new FileDownload(uri.split("/")[pos], urlserver + "img/" + u.getPhone() + "" + "/" + uri.split("/")[pos], u.getPhone());
                fileDownload.start();
            }


        }

        return auser;

    }








    /*The following methods are not important*/



    public static void updateUserInformation(User user, User updatedUser) throws IOException {
        String url = server + "UpdateUserInformation";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));
        urlParameters.add(new BasicNameValuePair("name2", updatedUser.getNickname()));
        urlParameters.add(new BasicNameValuePair("phone2", updatedUser.getPhone()));
        urlParameters.add(new BasicNameValuePair("photoURI2", updatedUser.getPhotoURI()));
        urlParameters.add(new BasicNameValuePair("coverURI2", updatedUser.getCoverURI()));



        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

        try {
            File myPhotoImg = new File(new URI(updatedUser.getPhotoURI()));

            f.uploadFile(myPhotoImg, server + "OkServletUp", user.getPhone());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
/*        File myCoverImg = null;
        try {
            myCoverImg = new File(new URI(updatedUser.getCoverURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        f.uploadFile(myCoverImg, server + "OkServletUp", user.getPhone());*/


    }
    public void deleteFriend(User user, User friend){
        //todo
    }
    /**
     * Delete the item from the server
     * @param postalDataItem
     */
    public void deletePostal(PostalDataItem postalDataItem){
        //todo
    }

}


